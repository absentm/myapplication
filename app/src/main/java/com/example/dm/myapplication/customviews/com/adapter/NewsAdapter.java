package com.example.dm.myapplication.customviews.com.adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.dm.myapplication.R;
import com.example.dm.myapplication.beans.News;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by DUAN on 2016/5/15.
 */
public class NewsAdapter extends ArrayAdapter<News> {
    private LayoutInflater mInflater;
    private int resource;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.app_icon)
            .showImageOnFail(R.drawable.app_icon)
            .showImageForEmptyUri(R.drawable.app_icon)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .considerExifParams(true)
            .build();

    public NewsAdapter(Context context, int resource) {
        super(context, resource);
        this.mInflater = LayoutInflater.from(context);
        this.resource = resource;
    }

    public NewsAdapter(Context context, int resource, List<News> objects) {
        super(context, resource, objects);
        mInflater = LayoutInflater.from(context);
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(resource, null);
            holder = new ViewHolder();
            holder.newsImage = (ImageView) convertView.findViewById(R.id.news_image);
            holder.newsTitle = (TextView) convertView.findViewById(R.id.news_title);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        News news = getItem(position);
        holder.newsTitle.setText(news.getTitle());
        Glide.with(parent.getContext())
                .load(news.getImage())
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.newsImage);

        return convertView;

    }

    class ViewHolder {
        ImageView newsImage;
        TextView newsTitle;
    }

    public void refreshNewsList(List<News> newsList) {
        clear();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            addAll(newsList);
        }
        notifyDataSetChanged();
    }
}
