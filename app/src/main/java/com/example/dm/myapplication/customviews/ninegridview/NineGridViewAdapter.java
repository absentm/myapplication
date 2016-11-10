package com.example.dm.myapplication.customviews.ninegridview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dm.myapplication.R;

/**
 * Created by dm on 16-4-8.
 * 九宫格GridView适配器
 */
public class NineGridViewAdapter extends BaseAdapter {
    private Context mContext;

    public String[] img_text = {"云笔记", "How old", "干货", "览图",
            "扫一扫", "视频", "天气", "周边", "音乐",};

    public int[] imgs = {R.drawable.ic_mode_edit_grey600_36dp,
            R.drawable.ic_accessibility_grey600_36dp,
            R.drawable.ic_settings_system_daydream_grey600_36dp,
            R.drawable.ic_insert_photo_grey600_36dp,
            R.drawable.ic_open_with_grey600_36dp,
            R.drawable.ic_video_collection_grey600_36dp,
            R.drawable.ic_wb_sunny_grey600_36dp,
            R.drawable.ic_map_grey600_36dp,
            R.drawable.ic_queue_music_grey600_36dp};

    public NineGridViewAdapter(Context mContext) {
        super();
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return img_text.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.find_grid_item, parent, false);
        }

        TextView textView = NineBaseViewHolder.get(convertView, R.id.tv_item);
        ImageView imageView = NineBaseViewHolder.get(convertView, R.id.iv_item);
        imageView.setBackgroundResource(imgs[position]);
        textView.setText(img_text[position]);

        return convertView;
    }


}
