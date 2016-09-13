package com.example.dm.myapplication.find;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dm.myapplication.R;
import com.example.dm.myapplication.find.zxing.activity.CaptureActivity;
import com.example.dm.myapplication.utiltools.SystemUtils;

/**
 * FindScanResultAty
 * Created by dm on 16-9-13.
 */
public class FindScanResultAty extends Activity implements View.OnClickListener {
    private ImageView mTitleBackImv;
    private TextView mScanResultTv;
    private Button mResultCopyBtn;
    private Button mResultSearchBtn;
    private String mScanResultStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_result);

        initView();
    }

    private void initView() {
        mTitleBackImv = (ImageView) findViewById(R.id.find_scan_result_title_left_imv);
        mScanResultTv = (TextView) findViewById(R.id.find_scan_result_content_tv);
        mResultCopyBtn = (Button) findViewById(R.id.find_scan_copy_btn);
        mResultSearchBtn = (Button) findViewById(R.id.find_scan_search_btn);

        mScanResultStr = getIntent().getStringExtra(CaptureActivity.SCAN_RESULT);
        int type = getIntent().getIntExtra(CaptureActivity.SCAN_TYPE, 0);

        // 二维码
        if (type == 0) {
            mScanResultTv.setText(mScanResultStr);
        } else { // 条形码
            mScanResultTv.setText(String.format("条形码：%1$s", mScanResultStr));
        }

        mTitleBackImv.setOnClickListener(FindScanResultAty.this);
        mResultCopyBtn.setOnClickListener(FindScanResultAty.this);
        mResultSearchBtn.setOnClickListener(FindScanResultAty.this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.find_scan_result_title_left_imv:
                FindScanResultAty.this.finish();
                break;
            case R.id.find_scan_copy_btn:
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(
                        Context.CLIPBOARD_SERVICE);
                // 将文本复制到剪贴板
                clipboardManager.setPrimaryClip(ClipData.newPlainText("data", mScanResultStr));
                SystemUtils.showHandlerToast(FindScanResultAty.this, "已复制到剪贴板");
                break;
            case R.id.find_scan_search_btn:
                // TODO: 自定义浏览器webview
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                if (intent.resolveActivity(getPackageManager()) != null) {
                    Uri uri = Uri.parse("http://www.baidu.com/s?wd=" + mScanResultStr);
                    intent.setData(uri);
                    startActivity(intent);
                    FindScanResultAty.this.finish();
                    overridePendingTransition(0, 0);
                } else {
                    SystemUtils.showHandlerToast(FindScanResultAty.this, "您的设备没有提供内置浏览器");
                }
                break;
        }
    }
}
