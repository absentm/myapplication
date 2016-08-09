package com.example.dm.myapplication.com;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dm.myapplication.customviews.xlistview.adapter.ViewPagerFixed;
import com.example.dm.myapplication.utiltools.ActivityCollector;
import com.example.dm.myapplication.utiltools.BimpUtil;
import com.example.dm.myapplication.utiltools.ResUtil;

import java.util.ArrayList;

/**
 * Created by dm on 16-4-26.
 * 显示已添加的照片
 */
public class ComPhotoShowActivity extends Activity {
    private Intent intent;
    // 返回按钮
    private Button back_bt;
    // 发送按钮
    private Button send_bt;
    //删除按钮
    private Button del_bt;
    //顶部显示预览图片位置的textview
    private TextView positionTextView;
    //获取前一个activity传过来的position
    private int position;
    //当前的位置
    private int location = 0;

    private ArrayList<View> listViews = null;
    private ViewPagerFixed pager;
    private MyPageAdapter adapter;

    private Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(ResUtil.getLayoutID("com_post_plugin_camera_photo_show"));// 切屏到主界面

        ActivityCollector.addActivity(this);

        mContext = this;

        back_bt = (Button) findViewById(ResUtil.getWidgetID("photo_bt_exit"));
        send_bt = (Button) findViewById(ResUtil.getWidgetID("photo_bt_enter"));
        del_bt = (Button) findViewById(ResUtil.getWidgetID("photo_bt_del"));

        back_bt.setOnClickListener(new BackListener());
        send_bt.setOnClickListener(new GallerySendListener());
        del_bt.setOnClickListener(new DelListener());

        intent = getIntent();
        Bundle bundle = intent.getExtras();
        position = Integer.parseInt(intent.getStringExtra("position"));
        isShowOkBt();
        // 为发送按钮设置文字
        pager = (ViewPagerFixed) findViewById(ResUtil.getWidgetID("viewpager01"));
        pager.setOnPageChangeListener(pageChangeListener);
        for (int i = 0; i < BimpUtil.tempSelectBitmap.size(); i++) {
            initListViews(BimpUtil.tempSelectBitmap.get(i).getBitmap());
        }

        adapter = new MyPageAdapter(listViews);
        pager.setAdapter(adapter);
        pager.setPageMargin((int) getResources().getDimensionPixelOffset(ResUtil.getDimenID("ui_10_dip")));
        int id = intent.getIntExtra("ID", 0);
        pager.setCurrentItem(id);
    }

    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {

        public void onPageSelected(int arg0) {
            location = arg0;
        }

        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        public void onPageScrollStateChanged(int arg0) {

        }
    };

    private void initListViews(Bitmap bm) {
        if (listViews == null)
            listViews = new ArrayList<View>();
        ImageView img = new ImageView(this);
        img.setBackgroundColor(0xff000000);
        img.setImageBitmap(bm);
        img.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        listViews.add(img);
    }

    // 返回按钮添加的监听器
    private class BackListener implements View.OnClickListener {
        public void onClick(View v) {
            finish();
        }
    }

    // 删除按钮添加的监听器
    private class DelListener implements View.OnClickListener {

        public void onClick(View v) {
            if (listViews.size() == 1) {
                BimpUtil.tempSelectBitmap.clear();
                BimpUtil.max = 0;
//                send_bt.setText(ResUtil.getString("finish") + "(" + BimpUtil.tempSelectBitmap.size() + "/" + PublicWay.num + ")");
                Intent intent = new Intent("data.broadcast.action");
                sendBroadcast(intent);
                finish();
            } else {
                BimpUtil.tempSelectBitmap.remove(location);
                BimpUtil.max--;
                pager.removeAllViews();
                listViews.remove(location);
                adapter.setListViews(listViews);
//                send_bt.setText(ResUtil.getString("finish") + "(" + BimpUtil.tempSelectBitmap.size() + "/" + PublicWay.num + ")");
                adapter.notifyDataSetChanged();
            }
        }
    }

    // 完成按钮的监听
    private class GallerySendListener implements View.OnClickListener {
        public void onClick(View v) {
            finish();
            intent.setClass(mContext, ComPostTopicActivity.class);
            startActivity(intent);
        }

    }

    public void isShowOkBt() {
        if (BimpUtil.tempSelectBitmap.size() > 0) {
//            send_bt.setText(ResUtil.getString("finish") + "(" + BimpUtil.tempSelectBitmap.size() + "/" + PublicWay.num + ")");
            send_bt.setPressed(true);
            send_bt.setClickable(true);
            send_bt.setTextColor(Color.WHITE);
        } else {
            send_bt.setPressed(false);
            send_bt.setClickable(false);
            send_bt.setTextColor(Color.parseColor("#E1E0DE"));
        }
    }

    /**
     * 监听返回按钮
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (position == 1) {
                this.finish();
                intent.setClass(ComPhotoShowActivity.this, ComPostTopicActivity.class);
                startActivity(intent);
            } else if (position == 2) {
                this.finish();
                intent.setClass(ComPhotoShowActivity.this, ComPostTopicActivity.class);
                startActivity(intent);
            }
        }
        return true;
    }


    class MyPageAdapter extends PagerAdapter {

        private ArrayList<View> listViews;

        private int size;

        public MyPageAdapter(ArrayList<View> listViews) {
            this.listViews = listViews;
            size = listViews == null ? 0 : listViews.size();
        }

        public void setListViews(ArrayList<View> listViews) {
            this.listViews = listViews;
            size = listViews == null ? 0 : listViews.size();
        }

        public int getCount() {
            return size;
        }

        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        public void destroyItem(View arg0, int arg1, Object arg2) {
            ((ViewPagerFixed) arg0).removeView(listViews.get(arg1 % size));
        }

        public void finishUpdate(View arg0) {
        }

        public Object instantiateItem(View arg0, int arg1) {
            try {
                ((ViewPagerFixed) arg0).addView(listViews.get(arg1 % size), 0);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return listViews.get(arg1 % size);
        }

        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}
