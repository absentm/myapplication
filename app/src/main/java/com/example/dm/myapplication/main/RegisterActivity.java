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

import cn.bmob.v3.listener.SaveListener;

/**
 * Created by dm on 16-4-2.
 * 注册页面
 */
public class RegisterActivity extends Activity {
    private static final String LOG_MSG = "RegisterActivity";
    private static final int BMOB_301 = 301;
    private static final int BMOB_202 = 202;
    private static final int BMOB_9016 = 9016;

    private boolean isHidden = true;

    private AppUser appUser;

    private ImageView titleImv;
    private TextView titleCenterTv;
    private TextView titleRightTv;

    // 注册界面输入框控件
    private EditText nickNameEt;
    private EditText emailAddressEt;
    private EditText passwordEt;
    private ImageView nickNameWarnImv;
    private ImageView emailWarnImv;
    private ImageView passwordEyeImv;
    private Button registerBtn;

    // 注册信息
    private String nickNameStr;
    private String emailAddressStr;
    private String passwordStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);
        initView(); // 初始化界面控件

        userRegister();  // 用户注册

    }

    /**
     * 注册页面的初始化
     */
    private void initView() {
        titleImv = (ImageView) findViewById(R.id.title_imv);
        titleImv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterActivity.this.finish();
            }
        });

        titleCenterTv = (TextView) findViewById(R.id.title_center_text_tv);
        titleCenterTv.setText("用户注册");

        titleRightTv = (TextView) findViewById(R.id.title_right_text_tv);
        titleRightTv.setVisibility(View.GONE);

        nickNameEt = (EditText) findViewById(R.id.nickname_et);
        emailAddressEt = (EditText) findViewById(R.id.email_et);
        passwordEt = (EditText) findViewById(R.id.passwd_et);

        nickNameWarnImv = (ImageView) findViewById(R.id.reg_nickname_warning_imv);
        emailWarnImv = (ImageView) findViewById(R.id.reg_email_warning_imv);
        passwordEyeImv = (ImageView) findViewById(R.id.reg_password_view_off_imv);

        registerBtn = (Button) findViewById(R.id.register_btn);
    }

    /**
     * 用户注册
     */
    private void userRegister() {
        // 密码是否可见控件的处理逻辑
        passwordEyeImv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isHidden) {
                    // 当需要可见密码时
                    passwordEyeImv.setImageResource(R.drawable.ic_visibility_grey600_18dp);
                    passwordEt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    // 当需要密码不可见时
                    passwordEyeImv.setImageResource(R.drawable.ic_visibility_off_grey600_18dp);
                    passwordEt.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }

                isHidden = !isHidden;
                passwordEt.postInvalidate();

                // 切换后将passwordEt光标置于末尾
                CharSequence charSequence = passwordEt.getText();
                if (charSequence instanceof Spannable) {
                    Spannable spannable = (Spannable) charSequence;
                    Selection.setSelection(spannable, charSequence.length());
                }
            }
        });

        // 注册按钮控件的处理逻辑
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nickNameStr = nickNameEt.getText().toString().trim();
                emailAddressStr = emailAddressEt.getText().toString().trim();
                passwordStr = passwordEt.getText().toString().trim();

                // 输入框的内容的简单校验
                if (nickNameStr.equals("")) {
                    nickNameWarnImv.setVisibility(View.VISIBLE);
                    Toast.makeText(RegisterActivity.this, "请输入昵称!", Toast.LENGTH_LONG).show();
                } else if ("".equals(emailAddressStr)) {
                    emailWarnImv.setVisibility(View.VISIBLE);
                    Toast.makeText(RegisterActivity.this, "请输入注册邮箱!", Toast.LENGTH_LONG).show();
                } else if ("".equals(passwordStr)) {
                    Toast.makeText(RegisterActivity.this, "请输入密码!", Toast.LENGTH_LONG).show();
                } else if (!"".equals(nickNameStr) && !"".equals(emailAddressStr) && !"".equals(passwordStr)) {
                    nickNameWarnImv.setVisibility(View.GONE);
                    emailWarnImv.setVisibility(View.GONE);

                    appUser = new AppUser();
                    appUser.setUserNickName(nickNameStr);
                    appUser.setUsername(emailAddressStr);
                    appUser.setPassword(passwordStr);
                    appUser.setEmail(emailAddressStr);

                    appUser.signUp(RegisterActivity.this, new SaveListener() {
                        @Override
                        public void onSuccess() {
                            Log.i(LOG_MSG, "$$$$$$: 注册成功");
                            Toast.makeText(RegisterActivity.this, "注册成功!", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            Log.i(LOG_MSG, ">>>>> " + i + "=   " + s);

                            if (BMOB_301 == i) {
                                Toast.makeText(RegisterActivity.this, "注册失败, 邮箱地址不正确!", Toast.LENGTH_LONG).show();
                            } else if (BMOB_202 == i) {
                                Toast.makeText(RegisterActivity.this, "注册失败, 该邮箱地址已被占用!", Toast.LENGTH_LONG).show();
                            } else if (BMOB_9016 == i) {
                                Toast.makeText(RegisterActivity.this, "网络不可用, 请检查您的网络设置!", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(RegisterActivity.this, "注册失败!", Toast.LENGTH_LONG).show();
                            }

                        }
                    });
                }

            }
        });


    }
}
