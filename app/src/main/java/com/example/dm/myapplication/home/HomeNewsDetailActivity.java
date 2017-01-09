package com.example.dm.myapplication.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.RelativeLayout;

import com.example.dm.myapplication.R;
import com.example.dm.myapplication.beans.News;
import com.example.dm.myapplication.utiltools.SystemUtils;
import com.like.LikeButton;
import com.like.OnLikeListener;

/**
 * HomeNewsDetailActivity
 * Created by DUAN on 2016/5/24.
 */
public class HomeNewsDetailActivity extends Activity {
    private RelativeLayout mBackRout;
    private LikeButton mFavImv;
    private WebView mWebView;

    private boolean isFavourite = false;
    private News news;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_news_detail);

        initView();
        eventDeal();
    }

    private void initView() {
        mBackRout = (RelativeLayout) findViewById(R.id.home_detail_back_rout);
        mFavImv = (LikeButton) findViewById(R.id.home_detail_title_right_imv);

        mWebView = (WebView) findViewById(R.id.webview);
        setWebView(mWebView);

        news = (News) getIntent().getSerializableExtra("news");
        new HomeLoadNewsDetailTask(mWebView).execute(news.getId());
        isFavourite = DailyNewsDB.getInstance(this).isFavourite(news);

        if (isFavourite) {
//            mFavImv.setImageResource(R.drawable.fav_active);
            mFavImv.setLiked(true);
        }
    }

    private void eventDeal() {
        mBackRout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeNewsDetailActivity.this.finish();
            }
        });

//        mFavImv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!isFavourite) {
//                    DailyNewsDB.getInstance(getApplicationContext()).saveFavourite(news);
//                    mFavImv.setImageResource(R.drawable.fav_active);
//                    isFavourite = true;
//                } else {
//                    DailyNewsDB.getInstance(getApplicationContext()).deleteFavourite(news);
//                    mFavImv.setImageResource(R.drawable.fav_normal);
//                    isFavourite = false;
//                }
//            }
//        });

        mFavImv.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                DailyNewsDB.getInstance(getApplicationContext()).saveFavourite(news);
                mFavImv.setLiked(true);
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                DailyNewsDB.getInstance(getApplicationContext()).deleteFavourite(news);
                mFavImv.setLiked(false);
            }
        });


    }

    private void setWebView(WebView mWebView) {
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setVerticalScrollBarEnabled(false);
        mWebView.setHorizontalScrollBarEnabled(false);
    }

    public static void startActivity(Context context, News news) {
        if (SystemUtils.checkNetworkConnection(context)) {
            Intent i = new Intent(context, HomeNewsDetailActivity.class);
            i.putExtra("news", news);
            context.startActivity(i);
        } else {
            SystemUtils.noNetworkAlert(context);
        }
    }
}