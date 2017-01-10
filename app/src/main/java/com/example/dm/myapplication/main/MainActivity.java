package com.example.dm.myapplication.main;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dm.myapplication.R;

import cn.bmob.v3.update.BmobUpdateAgent;

/**
 * 主页面内容
 * Created by dm on 16-1-19.
 */
public class MainActivity extends FragmentActivity implements View.OnClickListener {
    // 定义4个Fragment对象
    private FirstFragment fg1;
    private SecondFragment fg2;
    private ThirdFragment fg3;
    private FourthFragment fg4;

    // 帧布局对象，用来存放Fragment对象
    private FrameLayout frameLayout;

    // 定义每个选项中的相关控件
    private RelativeLayout firstLayout;
    private RelativeLayout secondLayout;
    private RelativeLayout thirdLayout;
    private RelativeLayout fourthLayout;

    private ImageView firstImage;
    private ImageView secondImage;
    private ImageView thirdImage;
    private ImageView fourthImage;

    private TextView firstText;
    private TextView secondText;
    private TextView thirdText;
    private TextView fourthText;

    // 定义几个颜色
    private int whirt = 0xFFFFFFFF;
    private int gray = 0xff4b4b4b;
    private int dark = 0xff000000;
    private int teal = 0xff009688;

    // 定义FragmentManager对象管理器
    private FragmentManager fragmentManager;

    // 定义两个时间间隔
    private long waitTime = 2000;
    private long touchTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 自动检测更新
        BmobUpdateAgent.setUpdateOnlyWifi(false);
        BmobUpdateAgent.update(this);

        fragmentManager = getSupportFragmentManager();
        initView(); // 初始化界面控件
        setChioceItem(0);   // 初始化页面加载时显示第一个选项卡
    }

    /**
     * 初始化页面
     */
    private void initView() {
        // 初始化底部导航栏的控件
        firstImage = (ImageView) findViewById(R.id.first_image);
        secondImage = (ImageView) findViewById(R.id.second_image);
        thirdImage = (ImageView) findViewById(R.id.third_image);
        fourthImage = (ImageView) findViewById(R.id.fourth_image);

        firstText = (TextView) findViewById(R.id.first_text);
        secondText = (TextView) findViewById(R.id.second_text);
        thirdText = (TextView) findViewById(R.id.third_text);
        fourthText = (TextView) findViewById(R.id.fourth_text);

        firstLayout = (RelativeLayout) findViewById(R.id.first_layout);
        secondLayout = (RelativeLayout) findViewById(R.id.second_layout);
        thirdLayout = (RelativeLayout) findViewById(R.id.third_layout);
        fourthLayout = (RelativeLayout) findViewById(R.id.fourth_layout);

        firstLayout.setOnClickListener(MainActivity.this);
        secondLayout.setOnClickListener(MainActivity.this);
        thirdLayout.setOnClickListener(MainActivity.this);
        fourthLayout.setOnClickListener(MainActivity.this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.first_layout:
                setChioceItem(0);
                break;
            case R.id.second_layout:
                setChioceItem(1);
                break;
            case R.id.third_layout:
                setChioceItem(2);
                break;
            case R.id.fourth_layout:
                setChioceItem(3);
                break;
            default:
                break;
        }

    }

    /**
     * 设置点击选项卡的事件处理
     *
     * @param index 选项卡的标号：0, 1, 2, 3
     */
    private void setChioceItem(int index) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        clearChioce(); // 清空, 重置选项, 隐藏所有Fragment
        hideFragments(fragmentTransaction);

        switch (index) {
            case 0:
                // 需要的话自行修改
                firstImage.setImageResource(R.drawable.ic_home_teal_24dp);
                firstText.setTextColor(teal);

                // 如果fg1为空，则创建一个并添加到界面上
                if (fg1 == null) {
                    fg1 = new FirstFragment();
                    fragmentTransaction.add(R.id.content, fg1);
                } else {
                    // 如果不为空，则直接将它显示出来
                    fragmentTransaction.show(fg1);
                }

                break;

            case 1:
                secondImage.setImageResource(R.drawable.ic_public_teal_24dp);
                secondText.setTextColor(teal);

                if (fg2 == null) {
                    fg2 = new SecondFragment();
                    fragmentTransaction.add(R.id.content, fg2);
                } else {
                    fragmentTransaction.show(fg2);
                }

                break;

            case 2:
                thirdImage.setImageResource(R.drawable.ic_whatshot_teal_24dp);
                thirdText.setTextColor(teal);

                if (fg3 == null) {
                    fg3 = new ThirdFragment();
                    fragmentTransaction.add(R.id.content, fg3);
                } else {
                    fragmentTransaction.show(fg3);
                }

                break;

            case 3:
                fourthImage.setImageResource(R.drawable.ic_person_teal_24dp);
                fourthText.setTextColor(teal);

                if (fg4 == null) {
                    fg4 = new FourthFragment();
                    fragmentTransaction.add(R.id.content, fg4);
                } else {
                    fragmentTransaction.show(fg4);
                }

                break;
        }

        fragmentTransaction.commit();   // 提交

    }

    /**
     * 当选中其中一个选项卡时，其他选项卡重置为默认
     */
    private void clearChioce() {
        firstImage.setImageResource(R.drawable.ic_home_grey600_24dp);
        firstText.setTextColor(gray);
        firstLayout.setBackgroundColor(whirt);

        secondImage.setImageResource(R.drawable.ic_public_grey600_24dp);
        secondText.setTextColor(gray);
        secondLayout.setBackgroundColor(whirt);

        thirdImage.setImageResource(R.drawable.ic_whatshot_grey600_24dp);
        thirdText.setTextColor(gray);
        thirdLayout.setBackgroundColor(whirt);

        fourthImage.setImageResource(R.drawable.ic_person_grey600_24dp);
        fourthText.setTextColor(gray);
        fourthLayout.setBackgroundColor(whirt);
    }

    /**
     * 隐藏Fragment
     *
     * @param fragmentTransaction fragmentTransaction
     */
    private void hideFragments(FragmentTransaction fragmentTransaction) {
        if (fg1 != null) {
            fragmentTransaction.hide(fg1);
        }

        if (fg2 != null) {
            fragmentTransaction.hide(fg2);
        }

        if (fg3 != null) {
            fragmentTransaction.hide(fg3);
        }

        if (fg4 != null) {
            fragmentTransaction.hide(fg4);
        }
    }


    /**
     * 改写物理按键, 连按两次退出APP
     *
     * @param keyCode 物理按键代码
     * @param event   按键事件
     * @return super
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN && KeyEvent.KEYCODE_BACK == keyCode) {
            long currentTime = System.currentTimeMillis();
            if ((currentTime - touchTime) >= waitTime) {
                //让Toast的显示时间和等待时间相同
                Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
                touchTime = currentTime;
            } else {
                MainActivity.this.finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
