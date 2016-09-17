package com.example.dm.myapplication.me;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Selection;
import android.text.Spannable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dm.myapplication.R;
import com.example.dm.myapplication.beans.AppUser;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by dm on 16-4-13.
 * 编辑签名
 */
public class MeEditorMessageAty extends Activity {
    private static final String LOG = "LOG";

    private ImageButton titleLeftImv;
    private TextView titleRightTv;
    private EditText meEditorMessageEt;
    private ImageView meEditorClearImv;

    private String messageStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.me_editor_message_layout);

        initViews();
        eventsDeal();
    }

    private void initViews() {
        titleLeftImv = (ImageButton) findViewById(R.id.title_imv);
        titleRightTv = (TextView) findViewById(R.id.title_right_tv);
        meEditorMessageEt = (EditText) findViewById(R.id.me_editor_message_et);
        meEditorClearImv = (ImageView) findViewById(R.id.me_editor_message_clear_imv);
    }

    private void eventsDeal() {
        final Intent intent = getIntent();
        String getMessageValue = intent.getStringExtra("MeInfoDetailActivity.messageStr");
        meEditorMessageEt.setText(getMessageValue);

        // 设置光标置于EditText行尾
        CharSequence charSequence = meEditorMessageEt.getText();
        if (charSequence != null) {
            Spannable spannable = (Spannable) charSequence;
            Selection.setSelection(spannable, charSequence.length());
        }

        meEditorClearImv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                meEditorMessageEt.setText("");
            }
        });


        titleLeftImv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MeEditorMessageAty.this.finish();
            }
        });

        titleRightTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messageStr = meEditorMessageEt.getText().toString();
                Log.i("log", ">>>>>> messageStr:::" + messageStr);
                AppUser newAppUser = new AppUser();
                newAppUser.setUserMessage(messageStr);
                AppUser currentAppUser = BmobUser.getCurrentUser(AppUser.class);
                newAppUser.update(currentAppUser.getObjectId(), new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            Toast.makeText(MeEditorMessageAty.this, "修改成功, 数据已上传!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MeEditorMessageAty.this, "修改失败! " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                Intent intent1 = new Intent();
                intent1.putExtra("MeEditorMessageAty.messageStr", messageStr);
                MeEditorMessageAty.this.setResult(RESULT_OK, intent1);
                MeEditorMessageAty.this.finish();
            }
        });


    }
}
