package com.example.dm.myapplication.me;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.dm.myapplication.R;
import com.example.dm.myapplication.beans.News;
import com.example.dm.myapplication.customviews.com.adapter.NewsAdapter;
import com.example.dm.myapplication.home.DailyNewsDB;
import com.example.dm.myapplication.home.HomeNewsDetailActivity;

import java.util.List;

/**
 * Created by DUAN on 2016/5/24.
 */
public class MeFavouritrActivity extends Activity implements AdapterView.OnItemClickListener {
    private ImageView mBackImv;
    private NewsAdapter adapter;
    private List<News> favouriteList;
    private ListView lvFavourite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.me_favourite);
        mBackImv = (ImageView) findViewById(R.id.title_imv);
        lvFavourite = (ListView) findViewById(R.id.lv_fav);
        favouriteList = DailyNewsDB.getInstance(this).loadFavourite();
        adapter = new NewsAdapter(this, R.layout.home_listview_item, favouriteList);
        lvFavourite.setAdapter(adapter);
        lvFavourite.setOnItemClickListener(this);

        mBackImv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MeFavouritrActivity.this.finish();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        HomeNewsDetailActivity.startActivity(this, adapter.getItem(position));
    }
}
