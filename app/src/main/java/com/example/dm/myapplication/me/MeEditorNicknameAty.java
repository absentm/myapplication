package com.example.dm.myapplication.me;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Selection;
import android.text.Spannable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dm.myapplication.R;
import com.example.dm.myapplication.beans.AppUser;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by dm on 16-4-9.
 * 编辑昵称
 */
public class MeEditorNicknameAty extends Activity {
    private static final String LOG = "LOG";
    private static final int RESULT_CODE = 2;

    private ImageView titleLeftImv;
    private TextView titleRightTv;
    private EditText meEditorNicknameEt;

    private String nicknameStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.me_editor_nickname_layout);

        initViews();
        eventsDeal();
    }

    private void initViews() {
        titleLeftImv = (ImageView) findViewById(R.id.title_imv);
        titleRightTv = (TextView) findViewById(R.id.title_right_tv);
        meEditorNicknameEt = (EditText) findViewById(R.id.me_editor_nickname_et);
    }

    private void eventsDeal() {
        final Intent intent = getIntent();
        String getNickNameValue = intent.getStringExtra("MeInfoDetailActivity.nickname");
        meEditorNicknameEt.setText(getNickNameValue);

        // 设置光标置于EditText行尾
        CharSequence charSequence = meEditorNicknameEt.getText();
        if (charSequence instanceof Spannable) {
            Spannable spannable = (Spannable) charSequence;
            Selection.setSelection(spannable, charSequence.length());
        }

        titleLeftImv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MeEditorNicknameAty.this.finish();
            }
        });

        titleRightTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nicknameStr = meEditorNicknameEt.getText().toString();
                Log.i("log", ">>>>>> nicknameStr:::" + nicknameStr);
                AppUser newAppUser = new AppUser();
                newAppUser.setUserNickName(nicknameStr);
                AppUser currentAppUser = BmobUser.getCurrentUser(MeEditorNicknameAty.this, AppUser.class);
                newAppUser.update(MeEditorNicknameAty.this, currentAppUser.getObjectId(), new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(MeEditorNicknameAty.this, "修改成功!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        Log.i(LOG, "i = " + i + ", s = " + s);
                    }
                });

                Intent intent1 = new Intent();
                intent1.putExtra("MeEditorNicknameAty.nickname", nicknameStr);
                MeEditorNicknameAty.this.setResult(RESULT_CODE, intent1);

                MeEditorNicknameAty.this.finish();
            }
        });
    }
}
