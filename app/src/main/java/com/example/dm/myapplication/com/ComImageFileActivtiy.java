package com.example.dm.myapplication.com;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;
import android.widget.TextView;

import com.example.dm.myapplication.R;
import com.example.dm.myapplication.customviews.com.adapter.ComFolderAdapter;
import com.example.dm.myapplication.utiltools.ActivityCollector;
import com.example.dm.myapplication.utiltools.BimpUtil;
import com.example.dm.myapplication.utiltools.ResUtil;

/**
 * Created by dm on 16-4-26.
 */
public class ComImageFileActivtiy extends Activity {

    private ComFolderAdapter mComFolderAdapter;
    private TextView bt_cancel;
    private Context mContext;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.com_post_plugin_camera_image_file);

        ActivityCollector.addActivity(this);

        mContext = this;
        bt_cancel = (TextView) findViewById(ResUtil.getWidgetID("cancel"));
        bt_cancel.setOnClickListener(new CancelListener());
        GridView gridView = (GridView) findViewById(ResUtil.getWidgetID("fileGridView"));
        TextView textView = (TextView) findViewById(ResUtil.getWidgetID("headerTitle"));
        textView.setText(ResUtil.getString("photo"));
        mComFolderAdapter = new ComFolderAdapter(this);
        gridView.setAdapter(mComFolderAdapter);
    }

    private class CancelListener implements OnClickListener {// 取消按钮的监听

        public void onClick(View v) {
            //清空选择的图片
            BimpUtil.tempSelectBitmap.clear();
            Intent intent = new Intent();
            intent.setClass(mContext, ComPostTopicActivity.class);
            startActivity(intent);
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent();
            intent.setClass(mContext, ComPostTopicActivity.class);
            startActivity(intent);
        }

        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}