package com.example.dm.myapplication.com;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.dm.myapplication.beans.ImageItem;
import com.example.dm.myapplication.customviews.com.adapter.ComAlbumGridViewAdapter;
import com.example.dm.myapplication.utiltools.ActivityCollector;
import com.example.dm.myapplication.utiltools.BimpUtil;
import com.example.dm.myapplication.utiltools.ResUtil;

import java.util.ArrayList;

/**
 * Created by dm on 16-4-26.
 */
public class ComShowAllPhotoActivity extends Activity {
    private GridView gridView;
    private ProgressBar progressBar;
    private ComAlbumGridViewAdapter gridImageAdapter;
    // 完成按钮
    private TextView okButton;
    // 预览按钮
    private TextView preview;
    // 返回按钮
    private TextView back;
    // 取消按钮
    private TextView cancel;
    // 标题
    private TextView headTitle;
    private Intent intent;
    private Context mContext;
    public static ArrayList<ImageItem> dataList = new ArrayList<ImageItem>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(ResUtil.getLayoutID("com_post_plugin_camera_show_all_photo"));

        ActivityCollector.addActivity(this);

        mContext = this;
        back = (TextView) findViewById(ResUtil.getWidgetID("showallphoto_back"));
        cancel = (TextView) findViewById(ResUtil.getWidgetID("showallphoto_cancel"));
        preview = (TextView) findViewById(ResUtil.getWidgetID("showallphoto_preview"));
        okButton = (TextView) findViewById(ResUtil.getWidgetID("showallphoto_ok_button"));
        headTitle = (TextView) findViewById(ResUtil.getWidgetID("showallphoto_headtitle"));
        this.intent = getIntent();
        String folderName = intent.getStringExtra("folderName");
        if (folderName.length() > 8) {
            folderName = folderName.substring(0, 9) + "...";
        }
        headTitle.setText(folderName);
        cancel.setOnClickListener(new CancelListener());
        back.setOnClickListener(new BackListener(intent));
        preview.setOnClickListener(new PreviewListener());
        init();
        initListener();
        isShowOkBt();
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            mContext.unregisterReceiver(this);
            gridImageAdapter.notifyDataSetChanged();
        }
    };

    private class PreviewListener implements View.OnClickListener {
        public void onClick(View v) {
            if (BimpUtil.tempSelectBitmap.size() > 0) {
                intent.putExtra("position", "2");
                unregisterReceiver(broadcastReceiver);

                intent.setClass(ComShowAllPhotoActivity.this, ComGalleryActivity.class);
                startActivity(intent);
            }
        }

    }

    private class BackListener implements View.OnClickListener {// 返回按钮监听
        Intent intent;

        public BackListener(Intent intent) {
            this.intent = intent;
        }

        public void onClick(View v) {
            unregisterReceiver(broadcastReceiver);
            intent.setClass(ComShowAllPhotoActivity.this, ComImageFileActivtiy.class);
            startActivity(intent);
        }

    }

    private class CancelListener implements View.OnClickListener {// 取消按钮的监听

        public void onClick(View v) {
            //清空选择的图片
            BimpUtil.tempSelectBitmap.clear();
            unregisterReceiver(broadcastReceiver);

            intent.setClass(mContext, ComPostTopicActivity.class);
            startActivity(intent);
        }
    }

    private void init() {
        IntentFilter filter = new IntentFilter("data.broadcast.action");
        registerReceiver(broadcastReceiver, filter);
        progressBar = (ProgressBar) findViewById(ResUtil.getWidgetID("showallphoto_progressbar"));
        progressBar.setVisibility(View.GONE);
        gridView = (GridView) findViewById(ResUtil.getWidgetID("showallphoto_myGrid"));
        gridImageAdapter = new ComAlbumGridViewAdapter(this, dataList,
                BimpUtil.tempSelectBitmap);
        gridView.setAdapter(gridImageAdapter);
        okButton = (TextView) findViewById(ResUtil.getWidgetID("showallphoto_ok_button"));
    }

    private void initListener() {

        gridImageAdapter
                .setOnItemClickListener(new ComAlbumGridViewAdapter.OnItemClickListener() {
                    public void onItemClick(final ToggleButton toggleButton,
                                            int position, boolean isChecked,
                                            Button button) {
                        if (BimpUtil.tempSelectBitmap.size() >= 9 && isChecked) {
                            button.setVisibility(View.GONE);
                            toggleButton.setChecked(false);
                            Toast.makeText(ComShowAllPhotoActivity.this, ResUtil.getString("only_choose_num"), Toast.LENGTH_SHORT)
                                    .show();
                            return;
                        }

                        if (isChecked) {
                            button.setVisibility(View.VISIBLE);
                            BimpUtil.tempSelectBitmap.add(dataList.get(position));
                            okButton.setText(ResUtil.getString("finish") + "(" + BimpUtil.tempSelectBitmap.size()
                                    + "/" + 9 + ")");
                        } else {
                            button.setVisibility(View.GONE);
                            BimpUtil.tempSelectBitmap.remove(dataList.get(position));
                            okButton.setText(ResUtil.getString("finish") + "(" + BimpUtil.tempSelectBitmap.size() + "/" + 9 + ")");
                        }
                        isShowOkBt();
                    }
                });

        okButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                okButton.setClickable(false);
//				if (PublicWay.photoService != null) {
//					PublicWay.selectedDataList.addAll(BimpUtil.tempSelectBitmap);
//					BimpUtil.tempSelectBitmap.clear();
//					PublicWay.photoService.onActivityResult(0, -2,
//							intent);
//				}
                unregisterReceiver(broadcastReceiver);
                intent.setClass(mContext, ComPostTopicActivity.class);
                startActivity(intent);
                // Intent intent = new Intent();
                // Bundle bundle = new Bundle();
                // bundle.putStringArrayList("selectedDataList",
                // selectedDataList);
                // intent.putExtras(bundle);
                // intent.setClass(ShowAllPhoto.this, UploadPhoto.class);
                // startActivity(intent);
                finish();

            }
        });

    }

    public void isShowOkBt() {
        if (BimpUtil.tempSelectBitmap.size() > 0) {
            okButton.setText(ResUtil.getString("finish") + "(" + BimpUtil.tempSelectBitmap.size() + "/" + 9 + ")");
            preview.setPressed(true);
            okButton.setPressed(true);
            preview.setClickable(true);
            okButton.setClickable(true);
            okButton.setTextColor(Color.WHITE);
            preview.setTextColor(Color.WHITE);
        } else {
            okButton.setText(ResUtil.getString("finish") + "(" + BimpUtil.tempSelectBitmap.size() + "/" + 9 + ")");
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
            this.finish();
            intent.setClass(ComShowAllPhotoActivity.this, ComImageFileActivtiy.class);
            startActivity(intent);
        }

        return false;

    }

    @Override
    protected void onRestart() {
        // TODO Auto-generated method stub
        isShowOkBt();
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}
