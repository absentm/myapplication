package com.example.dm.myapplication.find;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.dm.myapplication.R;
import com.example.dm.myapplication.beans.AppUser;
import com.example.dm.myapplication.beans.NotesBean;
import com.example.dm.myapplication.utiltools.DateUtil;
import com.example.dm.myapplication.utiltools.SystemUtils;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * FindAddNoteAty
 * Created by dm on 16-9-24.
 */
public class FindAddNoteAty extends Activity {
    private static final int MIN = 0;
    private static final int MAX = 13;

    private ImageButton titleCancleIbtn;
    private ImageButton titleAddOkIbtn;

    private EditText mNoteTitleEt;
    private EditText mNoteContentEt;

    private String mNoteTitleStr;
    private String mNoteContentStr;
    private String mNoteTimeStr;
    private long mNoteId;

    private AppUser appUser = BmobUser.getCurrentUser(AppUser.class);
    private MaterialDialog mMaterialDialog;
    private boolean isConnected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_add_note_lay);

        initView();
        eventDeal();
    }

    private void initView() {
        isConnected = SystemUtils.checkNetworkConnection(FindAddNoteAty.this);
        titleCancleIbtn = (ImageButton) findViewById(R.id.notes_add_cancle_imv);
        titleAddOkIbtn = (ImageButton) findViewById(R.id.title_addok_ibtn);
        mNoteTitleEt = (EditText) findViewById(R.id.find_add_note_title_et);
        mNoteContentEt = (EditText) findViewById(R.id.find_add_note_content_et);
    }

    private void eventDeal() {
        titleCancleIbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final MaterialDialog materialDialog = new MaterialDialog.Builder(FindAddNoteAty.this)
                        .content("确定退出编辑？")
                        .negativeColorRes(R.color.teal)
                        .negativeText("取消")
                        .positiveColorRes(R.color.teal)
                        .positiveText("确定")
                        .show();

                View cancleBtn = materialDialog.getActionButton(DialogAction.NEGATIVE);
                View okQuitBtn = materialDialog.getActionButton(DialogAction.POSITIVE);

                cancleBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        materialDialog.cancel();
                    }
                });

                okQuitBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        materialDialog.dismiss();
                        FindAddNoteAty.this.finish();
                    }
                });
            }
        });

        titleAddOkIbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isConnected = SystemUtils.checkNetworkConnection(FindAddNoteAty.this);

                if (!mNoteContentEt.getText().toString().trim().isEmpty()) {
                    mNoteContentStr = mNoteContentEt.getText().toString().trim();
                    mNoteTitleStr = mNoteTitleEt.getText().toString().trim();

                    if (mNoteTitleStr.isEmpty() && (mNoteContentStr.length() >= MAX)) {
                        mNoteTitleStr = mNoteContentStr.substring(MIN, MAX - 1);

                    } else {
                        mNoteTitleStr = mNoteContentStr;
                    }

                    mNoteId = DateUtil.getCurrentTimeMills();
                    mNoteTimeStr = DateUtil.getCurrentTimeStr() + " " +
                            DateUtil.getCurrentWeekStr();

                    final NotesBean notesBean = new NotesBean();
                    if (appUser != null) {
                        notesBean.setUserNameStr(appUser.getUsername());
                        notesBean.setNoteId(mNoteId);
                        notesBean.setNoteTitle(mNoteTitleStr);
                        notesBean.setNoteContent(mNoteContentStr);
                        notesBean.setNoteTime(mNoteTimeStr);
                    } else {
                        Toast.makeText(FindAddNoteAty.this, "请先登录！",
                                Toast.LENGTH_SHORT).show();
                    }

                    if (isConnected) {
                        mMaterialDialog = new MaterialDialog.Builder(FindAddNoteAty.this)
                                .content("Please waiting...")
                                .progress(true, 0)
                                .progressIndeterminateStyle(false)
                                .cancelable(false)
                                .show();

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                notesBean.save(new SaveListener<String>() {
                                    @Override
                                    public void done(String s, BmobException e) {
                                        if (e == null) {
                                            mMaterialDialog.dismiss();
                                            Intent intent = new Intent();
                                            intent.putExtra("newNoteInfos", notesBean);
                                            FindAddNoteAty.this.setResult(RESULT_OK, intent);
                                            FindAddNoteAty.this.finish();
                                        } else {
                                            mMaterialDialog.dismiss();
                                            Toast.makeText(FindAddNoteAty.this, "error! " + e.getMessage(),
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        }).start();
                    } else {
                        SystemUtils.noNetworkAlert(FindAddNoteAty.this);
                    }
                } else {
                    Toast.makeText(FindAddNoteAty.this, "内容不能为空！",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


}
