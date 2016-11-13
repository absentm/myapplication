package com.example.dm.myapplication.find;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.example.dm.myapplication.R;
import com.example.dm.myapplication.beans.SearchBeans.SearchResultsBean;
import com.example.dm.myapplication.customviews.MarqueeTextView;

/**
 * FindGankDetailAty
 * Created by dm on 16-11-11.
 */
public class FindGankDetailAty extends Activity implements View.OnClickListener {
    private ImageButton titlBackIbtn;
    private MarqueeTextView mMarqueeTextView;
    private ImageButton mShareIbtn;

    private NumberProgressBar mNumberProgressBar;
    private WebView mWebView;

    private SearchResultsBean mGankResultBean;
    private String mGankUrlStr;
    private String mGankDescStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_gank_detail_lay);

        initView();
        setUpListener();
        fillGankDatas();
    }

    private void initView() {
        titlBackIbtn = (ImageButton) findViewById(R.id.title_gank_detail_back_ibtn);
        mMarqueeTextView = (MarqueeTextView) findViewById(R.id.title_detail_gank_class_tv);
        mMarqueeTextView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        mMarqueeTextView.setSingleLine(true);
        mShareIbtn = (ImageButton) findViewById(R.id.title_detail_gank_share_ibtn);
        mNumberProgressBar = (NumberProgressBar) findViewById(R.id.find_gank_detail_numpbar);
        mWebView = (WebView) findViewById(R.id.find_gank_detail_wv);
    }

    private void setUpListener() {
        titlBackIbtn.setOnClickListener(FindGankDetailAty.this);
        mShareIbtn.setOnClickListener(FindGankDetailAty.this);
    }

    private void fillGankDatas() {
        Intent intent = getIntent();
        mGankResultBean = (SearchResultsBean) intent.getSerializableExtra("gankItemInfos");
        mGankDescStr = mGankResultBean.getDesc();
        mMarqueeTextView.setText(mGankDescStr);

        mGankUrlStr = mGankResultBean.getUrl();
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setSupportZoom(false);
        webSettings.setPluginState(WebSettings.PluginState.ON);

        mWebView.setWebChromeClient(new FindGankDetailAty.MyWebChromeClient());
        mWebView.setWebViewClient(new FindGankDetailAty.MyWebViewClient());
        mWebView.loadUrl(mGankUrlStr);
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
            mMarqueeTextView.setText(title);
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_gank_detail_back_ibtn:
                FindGankDetailAty.this.finish();
                break;
            case R.id.title_detail_gank_share_ibtn:
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT,
                        "来自[AbsentM-干货分享]: " + mGankDescStr + " " + mGankUrlStr);
                shareIntent.setType("text/plain");
                //设置分享列表的标题，并且每次都显示分享列表
                startActivity(Intent.createChooser(shareIntent, "分享到"));
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (mWebView.canGoBack()) {
                        mWebView.goBack();
                    } else {
                        finish();
                    }
                    return true;
            }
        }
        return super.onKeyDown(keyCode, event);
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
