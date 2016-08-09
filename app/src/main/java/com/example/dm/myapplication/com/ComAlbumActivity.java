package com.example.dm.myapplication.com;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.dm.myapplication.R;
import com.example.dm.myapplication.beans.ImageBucket;
import com.example.dm.myapplication.beans.ImageItem;
import com.example.dm.myapplication.customviews.com.adapter.ComAlbumGridViewAdapter;
import com.example.dm.myapplication.utiltools.ActivityCollector;
import com.example.dm.myapplication.utiltools.BimpUtil;
import com.example.dm.myapplication.utiltools.ComAlbumHelper;
import com.example.dm.myapplication.utiltools.ResUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dm on 16-4-26.
 * 从相册中选取照片
 */
public class ComAlbumActivity extends Activity {
    // 显示手机里的所有图片的列表控件
    private GridView gridView;
    // 当手机里没有图片时，提示用户没有图片的控件
    private TextView tv;
    // gridView的adapter
    private ComAlbumGridViewAdapter gridImageAdapter;
    // 完成按钮
    private TextView okButton;
    // 返回按钮
    private TextView back;
    // 取消按钮
    private TextView cancel;
    private Intent intent;
    // 预览按钮
    private TextView preview;
    private Context mContext;
    private ArrayList<ImageItem> dataList;
    private ComAlbumHelper helper;
    public static List<ImageBucket> contentList;
    public static Bitmap bitmap;

    private IntentFilter filter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.com_post_plugin_camera_album);

        ActivityCollector.addActivity(this);
        mContext = this;
        // 注册一个广播，这个广播主要是用于在GalleryActivity进行预览时，防止当所有图片都删除完后，再回到该页面时被取消选中的图片仍处于选中状态
        filter = new IntentFilter("data.broadcast.action");
        registerReceiver(broadcastReceiver, filter);
        bitmap = BitmapFactory.decodeResource(getResources(),
                ResUtil.getDrawableID("plugin_camera_no_pictures"));
        init();
        initListener();
        // 这个函数主要用来控制预览和完成按钮的状态
        isShowOkBt();
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            mContext.unregisterReceiver(this);
            // TODO Auto-generated method stub
            gridImageAdapter.notifyDataSetChanged();// 通过gridImageAdapter更新
        }
    };

    // 预览按钮的监听
    private class PreviewListener implements OnClickListener {
        public void onClick(View v) {
            if (BimpUtil.tempSelectBitmap.size() > 0) {
                intent.putExtra("position", "1");
                intent.setClass(ComAlbumActivity.this, ComGalleryActivity.class);
                startActivity(intent);
            }
        }

    }

    // 完成按钮的监听
    private class AlbumSendListener implements OnClickListener {
        public void onClick(View v) {
            overridePendingTransition(R.anim.activity_translate_in,
                    R.anim.activity_translate_out);
            intent.setClass(mContext, ComPostTopicActivity.class);
            startActivity(intent);
            unregisterReceiver(broadcastReceiver);      // 注销广播
            finish();
        }

    }

    // 返回按钮监听，返回到相册页面
    private class BackListener implements OnClickListener {
        public void onClick(View v) {
            intent.setClass(ComAlbumActivity.this, ComImageFileActivtiy.class);
            startActivity(intent);
        }
    }

    // 取消按钮的监听
    private class CancelListener implements OnClickListener {
        public void onClick(View v) {
            BimpUtil.tempSelectBitmap.clear();
            intent.setClass(mContext, ComPostTopicActivity.class);
            startActivity(intent);
            unregisterReceiver(broadcastReceiver);
        }
    }

    // 初始化，给一些对象赋值
    private void init() {
        helper = ComAlbumHelper.getHelper();
        helper.init(getApplicationContext());

        contentList = helper.getImagesBucketList(false);
        dataList = new ArrayList<ImageItem>();
        for (int i = 0; i < contentList.size(); i++) {
            dataList.addAll(contentList.get(i).imageList);
        }

        back = (TextView) findViewById(ResUtil.getWidgetID("back"));
        cancel = (TextView) findViewById(ResUtil.getWidgetID("cancel"));
        cancel.setOnClickListener(new CancelListener());
        back.setOnClickListener(new BackListener());
        preview = (TextView) findViewById(ResUtil.getWidgetID("preview"));
        preview.setOnClickListener(new PreviewListener());
        intent = getIntent();
        // Bundle bundle = intent.getExtras();
        gridView = (GridView) findViewById(ResUtil.getWidgetID("myGrid"));
        gridImageAdapter = new ComAlbumGridViewAdapter(this, dataList,
                BimpUtil.tempSelectBitmap);
        gridView.setAdapter(gridImageAdapter);
        tv = (TextView) findViewById(ResUtil.getWidgetID("myText"));
        gridView.setEmptyView(tv);
        okButton = (TextView) findViewById(ResUtil.getWidgetID("ok_button"));
        okButton.setText(ResUtil.getString("finish") + "("
                + BimpUtil.tempSelectBitmap.size() + "/" + 9 + ")");
    }

    private void initListener() {

        gridImageAdapter
                .setOnItemClickListener(new ComAlbumGridViewAdapter.OnItemClickListener() {

                    @Override
                    public void onItemClick(final ToggleButton toggleButton,
                                            int position, boolean isChecked, Button chooseBt) {
                        if (BimpUtil.tempSelectBitmap.size() >= 9) {
                            toggleButton.setChecked(false);
                            chooseBt.setVisibility(View.GONE);
                            if (!removeOneData(dataList.get(position))) {
                                Toast.makeText(ComAlbumActivity.this,
                                        ResUtil.getString("only_choose_num"), Toast.LENGTH_SHORT)
                                        .show();
                            }
                            return;
                        }
                        if (isChecked) {
                            chooseBt.setVisibility(View.VISIBLE);
                            BimpUtil.tempSelectBitmap.add(dataList.get(position));
                            okButton.setText(ResUtil.getString("finish") + "("
                                    + BimpUtil.tempSelectBitmap.size() + "/"
                                    + 9 + ")");
                        } else {
                            BimpUtil.tempSelectBitmap.remove(dataList.get(position));
                            chooseBt.setVisibility(View.GONE);
                            okButton.setText(ResUtil.getString("finish") + "("
                                    + BimpUtil.tempSelectBitmap.size() + "/"
                                    + 9 + ")");
                        }
                        isShowOkBt();
                    }
                });

        okButton.setOnClickListener(new AlbumSendListener());

    }

    private boolean removeOneData(ImageItem imageItem) {
        if (BimpUtil.tempSelectBitmap.contains(imageItem)) {
            BimpUtil.tempSelectBitmap.remove(imageItem);
            okButton.setText(ResUtil.getString("finish") + "("
                    + BimpUtil.tempSelectBitmap.size() + "/" + 9 + ")");
            return true;
        }
        return false;
    }

    public void isShowOkBt() {
        if (BimpUtil.tempSelectBitmap.size() > 0) {
            okButton.setText(ResUtil.getString("finish") + "("
                    + BimpUtil.tempSelectBitmap.size() + "/" + 9 + ")");
            preview.setPressed(true);
            okButton.setPressed(true);
            preview.setClickable(true);
            okButton.setClickable(true);
            okButton.setTextColor(Color.WHITE);
            preview.setTextColor(Color.WHITE);
        } else {
            okButton.setText(ResUtil.getString("finish") + "("
                    + BimpUtil.tempSelectBitmap.size() + "/" + 9 + ")");
            preview.setPressed(false);
            preview.setClickable(false);
            okButton.setPressed(false);
            okButton.setClickable(false);
            okButton.setTextColor(Color.parseColor("#E1E0DE"));
            preview.setTextColor(Color.parseColor("#E1E0DE"));
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            unregisterReceiver(broadcastReceiver);
            intent.setClass(ComAlbumActivity.this, ComImageFileActivtiy.class);
            startActivity(intent);
        }

        return false;
    }

    @Override
    protected void onRestart() {
        isShowOkBt();
        super.onRestart();
    }


    @Override
    protected void onPause() {
        unregisterReceiver(broadcastReceiver);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(broadcastReceiver, filter);
    }
}
