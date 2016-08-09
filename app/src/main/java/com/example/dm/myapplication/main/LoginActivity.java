package com.example.dm.myapplication.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Selection;
import android.text.Spannable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dm.myapplication.R;
import com.example.dm.myapplication.beans.AppUser;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;

/**
 *
 */
public class LoginActivity extends Activity {
    private static final String LOG_MSG = "LoginActivity";
    private static final int BMOB_101 = 101;
    private static final int BMOB_9016 = 9016;

    private boolean isHidden = true;

    // 登录界面输入框控件
    private EditText userEmailEt;
    private EditText userPasswordEt;
    private ImageView emailWarnImv;
    private ImageView passwordEyeImv;
    private Button loginBtn;
    private TextView forgetPasswdTv;

    // 登录信息
    private String emailAddressStr;
    private String passwordStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        initView();
        userLogin(); // 用户登录
    }

    /**
     * 初始化控件
     */
    private void initView() {
        // 标题栏控件
        ImageView titleImv = (ImageView) findViewById(R.id.title_imv);
        titleImv.setImageResource(R.drawable.ic_navigate_before_white_24dp);
        titleImv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LoginActivity.this.finish();
            }
        });

        TextView titleCenterTv = (TextView) findViewById(R.id.title_center_text_tv);
        titleCenterTv.setText("欢迎使用");

        TextView titleRightTv = (TextView) findViewById(R.id.title_right_text_tv);
        titleRightTv.setText("注册");
        titleRightTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        // 登录页面控件
        userEmailEt = (EditText) findViewById(R.id.login_email_et);
        userPasswordEt = (EditText) findViewById(R.id.login_passwd_et);
        emailWarnImv = (ImageView) findViewById(R.id.login_email_warn_imv);
        passwordEyeImv = (ImageView) findViewById(R.id.login_password_view_off_imv);
        loginBtn = (Button) findViewById(R.id.login_btn);
        forgetPasswdTv = (TextView) findViewById(R.id.login_forget_passwd_tv);
    }

    /**
     * 用户登录
     */
    private void userLogin() {
        // 密码是否可见控件的处理逻辑
        passwordEyeImv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isHidden) {
                    // 当需要可见密码时
                    passwordEyeImv.setImageResource(R.drawable.ic_visibility_grey600_18dp);
                    userPasswordEt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    // 当需要密码不可见时
                    passwordEyeImv.setImageResource(R.drawable.ic_visibility_off_grey600_18dp);
                    userPasswordEt.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }

                isHidden = !isHidden;
                userPasswordEt.postInvalidate();

                // 切换后将passwordEt光标置于末尾
                CharSequence charSequence = userPasswordEt.getText();
                if (charSequence instanceof Spannable) {
                    Spannable spannable = (Spannable) charSequence;
                    Selection.setSelection(spannable, charSequence.length());
                }
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailAddressStr = userEmailEt.getText().toString().trim();
                passwordStr = userPasswordEt.getText().toString().trim();

                // 输入框的内容的简单校验
                if ("".equals(emailAddressStr)) {
                    emailWarnImv.setVisibility(View.VISIBLE);
                    Toast.makeText(LoginActivity.this, "请输入注册的邮箱地址!", Toast.LENGTH_LONG).show();
                } else if ("".equals(passwordStr)) {
                    Toast.makeText(LoginActivity.this, "请输入您的密码!", Toast.LENGTH_LONG).show();
                } else if (!"".equals(emailAddressStr) && !"".equals(passwordStr)) {
                    emailWarnImv.setVisibility(View.GONE);

                    BmobUser.loginByAccount(LoginActivity.this, emailAddressStr, passwordStr, new LogInListener<AppUser>() {
                        @Override
                        public void done(AppUser appUser, BmobException e) {
                            if (appUser != null) {
                                Log.i(LOG_MSG, "$$$$$$: 用户登陆成功!");
                                Toast.makeText(LoginActivity.this, "登录成功, 欢迎使用!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            } else {
                                Log.i(LOG_MSG, ">>>>>> 登录失败: " + e.getErrorCode() + ",  " + e.getMessage());
                                if (BMOB_101 == e.getErrorCode()) {
                                    Toast.makeText(LoginActivity.this, "用户名或密码错误!", Toast.LENGTH_LONG).show();
                                } else if (BMOB_9016 == e.getErrorCode()) {
                                    Toast.makeText(LoginActivity.this, "网络不可用, 请检查您的网络设置!", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(LoginActivity.this, "登录失败!", Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    });
                }
            }
        });

        // 忘记密码控件的点击处理
        forgetPasswdTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ResetPasswordActivity.class));
            }
        });
    }
}
