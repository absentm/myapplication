package com.example.dm.myapplication.find;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.dm.myapplication.R;
import com.example.dm.myapplication.beans.NotesBean;
import com.example.dm.myapplication.utiltools.DateUtil;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * FindNoteDetailAty
 * Created by dm on 16-9-24.
 */
public class FindNoteDetailAty extends Activity implements View.OnClickListener {
    private ImageButton mBackIbtn;
    private ImageButton mShareIbtn;
    private ImageButton mEditIbtn;
    private ImageButton mMoreIbtn;

    private EditText mNoteTitleEt;
    private EditText mNoteContentEt;

    private String mNoteTitleStr;
    private String mNoteContentStr;

    private NotesBean notesBean;
    private int flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_note_detail_lay);

        initView();
        setUpListener();
        fillNoteDatas();
    }

    private void initView() {
        mBackIbtn = (ImageButton) findViewById(R.id.title_back_ibtn);
        mShareIbtn = (ImageButton) findViewById(R.id.title_share_ibtn);
        mEditIbtn = (ImageButton) findViewById(R.id.title_edit_ibtn);
        mMoreIbtn = (ImageButton) findViewById(R.id.title_more_ibtn);

        mNoteTitleEt = (EditText) findViewById(R.id.find_edit_note_title_et);
        mNoteContentEt = (EditText) findViewById(R.id.find_edit_note_content_et);
    }

    private void setUpListener() {
        mBackIbtn.setOnClickListener(this);
        mShareIbtn.setOnClickListener(this);
        mEditIbtn.setOnClickListener(this);
        mMoreIbtn.setOnClickListener(this);
    }

    private void fillNoteDatas() {
        Intent intent = getIntent();
        notesBean = (NotesBean) intent.getSerializableExtra("noteInfos");

        Log.i("TAG", "notesBean.getNoteTitle()" + notesBean.getNoteTitle());
        Log.i("TAG", "notesBean.getNoteContent()()" + notesBean.getNoteContent());

        mNoteTitleStr = notesBean.getNoteTitle();
        mNoteContentStr = notesBean.getNoteContent();

        mNoteTitleEt.setText(mNoteTitleStr);
        mNoteContentEt.setText(mNoteContentStr);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back_ibtn:
                FindNoteDetailAty.this.finish();
                break;
            case R.id.title_share_ibtn:
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT,
                        "来自AbsentM-我的云笔记:\n" + mNoteTitleStr + "\n\n" + mNoteContentStr);
                shareIntent.setType("text/plain");
                //设置分享列表的标题，并且每次都显示分享列表
                startActivity(Intent.createChooser(shareIntent, "分享到"));

                break;
            case R.id.title_edit_ibtn:
                switch (flag) {
                    case 0:
                        enableEditNote();
                        break;
                    case 1:
                        updateNote();
                        break;
                }

                break;
            case R.id.title_more_ibtn:
                moreEventDeals();
                break;
        }
    }

    private void enableEditNote() {
        mNoteTitleEt.setFocusable(true);
        mNoteTitleEt.setFocusableInTouchMode(true);
        mNoteTitleEt.requestFocus();

        mNoteContentEt.setFocusable(true);
        mNoteContentEt.setFocusableInTouchMode(true);
        mNoteContentEt.requestFocus();

        mEditIbtn.setImageResource(R.drawable.ic_done_white_24dp);
        flag = 1;
    }

    private void updateNote() {
        mNoteTitleStr = mNoteTitleEt.getText().toString().trim();
        mNoteContentStr = mNoteContentEt.getText().toString().trim();

        notesBean.setNoteTitle(mNoteTitleStr);
        notesBean.setNoteContent(mNoteContentStr);
        notesBean.setNoteTime(DateUtil.getCurrentTimeStr() + " " + DateUtil.getCurrentWeekStr());
        notesBean.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    mNoteTitleEt.setFocusable(false);
                    mNoteTitleEt.setFocusableInTouchMode(false);

                    mNoteContentEt.setFocusable(false);
                    mNoteContentEt.setFocusableInTouchMode(false);

                    mEditIbtn.setImageResource(R.drawable.ic_mode_edit_white_18dp);
                    flag = 0;
                    Intent intent = new Intent();
                    intent.putExtra("updateNoteInfos", notesBean);
                    FindNoteDetailAty.this.setResult(RESULT_OK, intent);
                    FindNoteDetailAty.this.finish();
                }
            }
        });
    }

    private void moreEventDeals() {
        new MaterialDialog
                .Builder(this)
                .items(R.array.note_detail_value)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView,
                                            int position, CharSequence text) {
                        switch ((String) text) {
                            case "复制全文":
                                copyNoteDatas();
                                break;
                            case "字数统计":
                                showTotalMsg();
                                break;
                        }
                    }
                }).show();

    }

    private void copyNoteDatas() {
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(
                Context.CLIPBOARD_SERVICE);
        // 将文本复制到剪贴板
        clipboardManager.setPrimaryClip(
                ClipData.newPlainText("data", mNoteTitleStr + "\n" + mNoteContentStr));

        Toast.makeText(FindNoteDetailAty.this,
                "已复制到剪贴板",
                Toast.LENGTH_SHORT).show();
    }

    private void showTotalMsg() {
        new MaterialDialog.Builder(this)
                .title("字数统计")
                .content("标题字数： " + mNoteTitleStr.length() + "\n\n"
                        + "内容字数： " + mNoteContentStr.length())
                .show();

    }
}
