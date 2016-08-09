package com.example.dm.myapplication.main;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dm.myapplication.R;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.ResetPasswordByEmailListener;

/**
 * Created by dm on 16-4-7.
 * 重置密码
 */
public class ResetPasswordActivity extends Activity {
    private static final String LOG_MSG = "ResetPasswordActivity";
    private static final int BMOB_205 = 205;
    private static final int BMOB_301 = 301;
    private static final int BMOB_9016 = 9016;

    private EditText resetEmailEt;
    private ImageView resetWarnImv;
    private Button resetPasswdBtn;
    private String resetEmailStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_reset_activy);

        initView(); // 初始化界面控件
        userResetPassword();  // 重置密码业务逻辑
    }

    /**
     * 初始化页面
     */
    private void initView() {
        // 初始化页面标题栏
        ImageView titleLeftImv = (ImageView) findViewById(R.id.title_imv);
        titleLeftImv.setImageResource(R.drawable.ic_navigate_before_white_24dp);
        titleLeftImv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResetPasswordActivity.this.finish();
            }
        });

        TextView titleCenterTv = (TextView) findViewById(R.id.title_center_text_tv);
        titleCenterTv.setText("重置密码");

        TextView titleRightTv = (TextView) findViewById(R.id.title_right_text_tv);
        titleRightTv.setVisibility(View.GONE);

        // // 初始化界面控件
        resetEmailEt = (EditText) findViewById(R.id.reset_email_et);
        resetWarnImv = (ImageView) findViewById(R.id.reset_email_warn_imv);
        resetPasswdBtn = (Button) findViewById(R.id.reset_passwd_btn);
    }

    /**
     * 重置密码业务逻辑
     */
    private void userResetPassword() {
        resetPasswdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetEmailStr = resetEmailEt.getText().toString().trim();

                if ("".equals(resetEmailStr)) {
                    resetWarnImv.setVisibility(View.VISIBLE);
                    Toast.makeText(ResetPasswordActivity.this, "请输入登录邮箱!", Toast.LENGTH_LONG).show();
                } else {
                    resetWarnImv.setVisibility(View.GONE);
                    BmobUser.resetPasswordByEmail(ResetPasswordActivity.this, resetEmailStr, new ResetPasswordByEmailListener() {
                        @Override
                        public void onSuccess() {
                            Toast.makeText(ResetPasswordActivity.this, "重置密码请求成功，请到" + resetEmailStr + "邮箱进行密码重置操作", Toast.LENGTH_LONG).show();
                            ResetPasswordActivity.this.finish();
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            Log.i(LOG_MSG, ">>>>>> 重置密码失败: " + i + ", " + s);
                            switch (i) {
                                case BMOB_9016:
                                    Toast.makeText(ResetPasswordActivity.this, "网络不可用, 请检查您的网络设置!", Toast.LENGTH_LONG).show();
                                    break;
                                case BMOB_205:
                                    Toast.makeText(ResetPasswordActivity.this, "使用该邮箱地址注册的用户不存在!", Toast.LENGTH_LONG).show();
                                    break;
                                case BMOB_301:
                                    Toast.makeText(ResetPasswordActivity.this, "邮箱地址不正确!", Toast.LENGTH_LONG).show();
                                    break;
                                default:
                                    Toast.makeText(ResetPasswordActivity.this, "重置密码失败!", Toast.LENGTH_LONG).show();
                            }


                        }
                    });
                }
            }
        });


    }
}
