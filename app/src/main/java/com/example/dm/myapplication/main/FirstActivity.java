package com.example.dm.myapplication.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;

import com.example.dm.myapplication.R;
import com.example.dm.myapplication.utiltools.SystemUtils;

import cn.bmob.v3.Bmob;

/**
 * Created by dm on 16-3-30.
 * 启动画面
 */
public class FirstActivity extends Activity {
    private final static String BMOB_APP_ID = "e2d69e80b15a0f55066e1b77654e20f9";
    private String aMapSHA1Value = "";
    private TextView launchTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 初始化 Bmob SDK
        // 使用时请将第二个参数Application ID替换成你在Bmob服务器端创建的Application ID
        Bmob.initialize(this, BMOB_APP_ID);

        setContentView(R.layout.launch_layout);

        initViews();

        aMapSHA1Value = SystemUtils.aMapSHA1(FirstActivity.this);
        Log.i("FirstActivity", "aMapSHA1Value >>> " + aMapSHA1Value);

        AnimationSet animationSet = new AnimationSet(true);
        ScaleAnimation scaleAnimation = new ScaleAnimation(1, 1, 1, 1,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0.1f);
        animationSet.setDuration(3000);
        animationSet.addAnimation(alphaAnimation);
        animationSet.addAnimation(scaleAnimation);

        launchTv.startAnimation(animationSet);
        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                startActivity(new Intent(FirstActivity.this, MainActivity.class));
                FirstActivity.this.finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void initViews() {
        launchTv = (TextView) findViewById(R.id.launch_tv);
    }
}
