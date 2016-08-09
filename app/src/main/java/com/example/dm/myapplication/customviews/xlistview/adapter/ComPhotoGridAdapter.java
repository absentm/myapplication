package com.example.dm.myapplication.customviews.xlistview.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.dm.myapplication.R;
import com.example.dm.myapplication.utiltools.HttpUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by dm on 16-4-24.
 * 发布图片Gridview适配器
 */
public class ComPhotoGridAdapter extends BaseAdapter {
    private List<String> mUI;

    private LayoutInflater mLayoutInflater;

    public ComPhotoGridAdapter(List<String> ui, Context context) {
        mLayoutInflater = LayoutInflater.from(context);
        this.mUI = ui;
    }

    @Override
    public int getCount() {
        return mUI == null ? 0 : mUI.size();
    }

    @Override
    public String getItem(int position) {
        return mUI.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyGridViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new MyGridViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.com_user_img_item,
                    parent, false);
            viewHolder.imageView = (ImageView) convertView
                    .findViewById(R.id.iv_user_img);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (MyGridViewHolder) convertView.getTag();
        }
        String url = getItem(position);
        ImageLoader.getInstance().displayImage(url, viewHolder.imageView, HttpUtil.DefaultOptions);

        return convertView;
    }

    private static class MyGridViewHolder {
        ImageView imageView;
    }
}

