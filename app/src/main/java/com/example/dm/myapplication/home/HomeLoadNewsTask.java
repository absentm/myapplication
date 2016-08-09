package com.example.dm.myapplication.home;

import android.os.AsyncTask;
import android.util.Log;

import com.example.dm.myapplication.beans.News;
import com.example.dm.myapplication.customviews.com.adapter.NewsAdapter;
import com.example.dm.myapplication.utiltools.HttpUtil;
import com.example.dm.myapplication.utiltools.JsonHelper;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

/**
 * Created by DUAN on 2016/5/24.
 *
 */
public class HomeLoadNewsTask extends AsyncTask<Void, Void, List<News>> {
    private NewsAdapter adapter;
    private onFinishListener listener;

    public HomeLoadNewsTask(NewsAdapter adapter) {
        super();
        this.adapter = adapter;
    }

    public HomeLoadNewsTask(NewsAdapter adapter, onFinishListener listener) {
        super();
        this.adapter = adapter;
        this.listener = listener;
    }

    @Override
    protected List<News> doInBackground(Void... params) {
        List<News> newsList = null;
        try {
            newsList = JsonHelper.parseJsonToList(HttpUtil.getLastNewsList());
        } catch (IOException | JSONException e) {
            Log.i("TAG", e.getMessage());
        } finally {
            return newsList;
        }
    }

    @Override
    protected void onPostExecute(List<News> newsList) {
        adapter.refreshNewsList(newsList);
        if (listener != null) {
            listener.afterTaskFinish();
        }

    }

    public interface onFinishListener {
        public void afterTaskFinish();
    }
}
