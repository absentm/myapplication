package com.example.dm.myapplication.home;

import android.os.AsyncTask;
import android.webkit.WebView;

import com.example.dm.myapplication.beans.HomeNewsDetailBeans;
import com.example.dm.myapplication.utiltools.HttpUtil;
import com.example.dm.myapplication.utiltools.JsonHelper;

import org.json.JSONException;

import java.io.IOException;

/**
 * Created by DUAN on 2016/5/24.
 */
public class HomeLoadNewsDetailTask extends AsyncTask<Integer, Void, HomeNewsDetailBeans> {
    private WebView mWebView;

    public HomeLoadNewsDetailTask(WebView mWebView) {
        this.mWebView = mWebView;
    }

    @Override
    protected HomeNewsDetailBeans doInBackground(Integer... params) {
        HomeNewsDetailBeans mHomeNewsDetailBeans = null;
        try {
            mHomeNewsDetailBeans = JsonHelper.parseJsonToDetail(HttpUtil.getNewsDetail(params[0]));
        } catch (IOException | JSONException e) {

        } finally {
            return mHomeNewsDetailBeans;
        }
    }

    @Override
    protected void onPostExecute(HomeNewsDetailBeans mHomeNewsDetailBeans) {
        String headerImage;
        if (mHomeNewsDetailBeans.getImage() == null || mHomeNewsDetailBeans.getImage() == "") {
            headerImage = "file:///android_asset/news_detail_header_image.jpg";

        } else {
            headerImage = mHomeNewsDetailBeans.getImage();
        }
        StringBuilder sb = new StringBuilder();
        sb.append("<div class=\"img-wrap\">")
                .append("<h1 class=\"headline-title\">")
                .append(mHomeNewsDetailBeans.getTitle()).append("</h1>")
                .append("<span class=\"img-source\">")
                .append(mHomeNewsDetailBeans.getImage_source()).append("</span>")
                .append("<img src=\"").append(headerImage)
                .append("\" alt=\"\">")
                .append("<div class=\"img-mask\"></div>");
        String mNewsContent = "<link rel=\"stylesheet\" type=\"text/css\" href=\"news_content_style.css\"/>"
                + "<link rel=\"stylesheet\" type=\"text/css\" href=\"news_header_style.css\"/>"
                + mHomeNewsDetailBeans.getBody().replace("<div class=\"img-place-holder\">", sb.toString());
        mWebView.loadDataWithBaseURL("file:///android_asset/", mNewsContent, "text/html", "UTF-8", null);
    }
}
