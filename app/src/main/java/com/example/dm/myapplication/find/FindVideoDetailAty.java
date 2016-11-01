package com.example.dm.myapplication.find;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.example.dm.myapplication.R;
import com.example.dm.myapplication.beans.VideoBeans.VideoResultsBean;
import com.example.dm.myapplication.utiltools.DateUtil;

import java.text.ParseException;

/**
 * FindVideoDetailAty
 * Created by dm on 16-10-30.
 */
public class FindVideoDetailAty extends Activity implements View.OnClickListener {
    private ImageButton titleBackIBtn;

    private WebView mWebView;
    private TextView mVideoDescTv;
    private TextView mVideoPublishedAtTv;
    private TextView mVideoCreateAtTv;
    private TextView mVideoSourceTv;
    private TextView mVideoWhoTv;
    private NumberProgressBar mNumberProgressBar;

    private VideoResultsBean mVideoResultsBean;
    private String mVideoItemDescStr;
    private String mVideoItemPublishAtStr;
    private String mVideoItemCreateAtStr;
    private String mVideoItemSourceStr;
    private String mVideoItemWhoStr;
    private String mVideoItemUrlStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_video_item_detal);

        initView();
        setUpListener();

        try {
            fillVideoDatas();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        titleBackIBtn = (ImageButton) findViewById(R.id.video_detail_back_imv);
        mWebView = (WebView) findViewById(R.id.video_item_webview);
        mNumberProgressBar = (NumberProgressBar) findViewById(R.id.progressbar);
        mVideoDescTv = (TextView) findViewById(R.id.video_detail_desc_tv);
        mVideoPublishedAtTv = (TextView) findViewById(R.id.video_detail_publish_time_tv);
        mVideoCreateAtTv = (TextView) findViewById(R.id.video_detail_create_time_tv);
        mVideoSourceTv = (TextView) findViewById(R.id.video_detail_source_tv);
        mVideoWhoTv = (TextView) findViewById(R.id.video_detail_who_tv);
    }

    private void setUpListener() {
        titleBackIBtn.setOnClickListener(FindVideoDetailAty.this);
    }

    private void fillVideoDatas() throws ParseException {
        Intent intent = getIntent();
        mVideoResultsBean = (VideoResultsBean) intent.getSerializableExtra("videoItemInfos");


        mVideoItemUrlStr = mVideoResultsBean.getUrl();
        Log.i("TAG", "mVideoItemUrlStr： " + mVideoItemUrlStr);

        mVideoItemDescStr = "『" + mVideoResultsBean.getDesc() + "』";
        mVideoItemPublishAtStr = "PublishedAt： " +
                DateUtil.utc2LocalTime(mVideoResultsBean.getPublishedAt());
        mVideoItemCreateAtStr = "CreatedAt： " +
                DateUtil.utc2LocalTime(mVideoResultsBean.getCreatedAt());
        mVideoItemSourceStr = "Source from： " + mVideoResultsBean.getSource();
        mVideoItemWhoStr = "Shared by： " + mVideoResultsBean.getWho();

        mVideoDescTv.setText(mVideoItemDescStr);
        mVideoPublishedAtTv.setText(mVideoItemPublishAtStr);
        mVideoCreateAtTv.setText(mVideoItemCreateAtStr);
        mVideoSourceTv.setText(mVideoItemSourceStr);
        mVideoWhoTv.setText(mVideoItemWhoStr);

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setSupportZoom(false);
        webSettings.setPluginState(WebSettings.PluginState.ON);

        mWebView.setBackgroundColor(255); // 设置背景色
        mWebView.getBackground().setAlpha(249); // 设置填充透明度 范围：0-255
        mWebView.setWebChromeClient(new MyWebChromeClient());
        mWebView.setWebViewClient(new MyWebViewClient());
        mWebView.loadUrl(mVideoItemUrlStr);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.video_detail_back_imv:
                FindVideoDetailAty.this.finish();
                break;
            default:
                break;
        }
    }

    private class MyWebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            mNumberProgressBar.setProgress(newProgress);
            if (newProgress == 100) {
                mNumberProgressBar.setVisibility(View.GONE);
            } else {
                mNumberProgressBar.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
        }
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url != null) {
                view.loadUrl(url);
            }
            return true;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mWebView != null) {
            mWebView.destroy();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mWebView != null) {
            mWebView.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mWebView != null) {
            mWebView.onResume();
        }
    }
}
