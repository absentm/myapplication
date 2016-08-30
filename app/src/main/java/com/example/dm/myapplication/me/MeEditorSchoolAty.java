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
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by dm on 16-4-13.
 * 编辑学校名称
 */
public class MeEditorSchoolAty extends Activity {
    private static final String LOG = "LOG";

    private ImageView titleLeftImv;
    private TextView titleRightTv;
    private EditText meEditorSchoolEt;

    private String schoolNameStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.me_editor_school_layout);

        initViews();
        eventsDeal();
    }

    private void initViews() {
        titleLeftImv = (ImageView) findViewById(R.id.title_imv);
        titleRightTv = (TextView) findViewById(R.id.title_right_tv);
        meEditorSchoolEt = (EditText) findViewById(R.id.me_editor_school_et);
    }

    private void eventsDeal() {
        final Intent intent = getIntent();
        String getSchoolNameValue = intent.getStringExtra("MeInfoDetailActivity.schoolNameStr");
        meEditorSchoolEt.setText(getSchoolNameValue);

        // 设置光标置于EditText行尾
        CharSequence charSequence = meEditorSchoolEt.getText();
        if (charSequence != null) {
            Spannable spannable = (Spannable) charSequence;
            Selection.setSelection(spannable, charSequence.length());
        }

        titleLeftImv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MeEditorSchoolAty.this.finish();
            }
        });

        titleRightTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                schoolNameStr = meEditorSchoolEt.getText().toString();
                Log.i("log", ">>>>>> schoolNameStr:::" + schoolNameStr);
                AppUser newAppUser = new AppUser();
                newAppUser.setUserSchool(schoolNameStr);
                AppUser currentAppUser = BmobUser.getCurrentUser(AppUser.class);
                newAppUser.update(currentAppUser.getObjectId(), new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            Toast.makeText(MeEditorSchoolAty.this, "修改成功!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MeEditorSchoolAty.this, "修改失败! " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                Intent intent1 = new Intent();
                intent1.putExtra("MeEditorSchoolAty.schoolNameStr", schoolNameStr);
                MeEditorSchoolAty.this.setResult(RESULT_OK, intent1);
                MeEditorSchoolAty.this.finish();
            }
        });
    }
}
