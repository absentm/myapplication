package com.example.dm.myapplication.find;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dm.myapplication.R;
import com.example.dm.myapplication.beans.MusicEntity;

import me.yokeyword.indexablerv.IndexableAdapter;

/**
 * FindIndexMusicAdapter
 * Created by dm on 16-11-2.
 */

public class FindIndexMusicAdapter extends IndexableAdapter<MusicEntity> {
    private LayoutInflater mInflater;

    public FindIndexMusicAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateTitleViewHolder(ViewGroup parent) {
        View view = mInflater.inflate(R.layout.item_index_music, parent, false);
        return new IndexVH(view);
    }

    @Override
    public RecyclerView.ViewHolder onCreateContentViewHolder(ViewGroup parent) {
        View view = mInflater.inflate(R.layout.find_music_item, parent, false);
        return new ContentVH(view);
    }

    @Override
    public void onBindTitleViewHolder(RecyclerView.ViewHolder holder, String indexTitle) {
        IndexVH vh = (IndexVH) holder;
        vh.tv.setText(indexTitle);
    }

    @Override
    public void onBindContentViewHolder(RecyclerView.ViewHolder holder, MusicEntity entity) {
        ContentVH vh = (ContentVH) holder;
        vh.musicTitleTv.setText(entity.getTitle());
        vh.musicArtistTv.setText(entity.getArtist() + " - ");
        vh.musicTimeTv.setText("[ " + formatTime(entity.getDuration()) + " ]");
        vh.musicPlayingImv.setVisibility(View.GONE);
        // 将数据保存在itemView的Tag中，以便点击时进行获取
    }

    private class IndexVH extends RecyclerView.ViewHolder {
        TextView tv;

        public IndexVH(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.tv_index);
        }
    }

    private class ContentVH extends RecyclerView.ViewHolder {
        ImageView musicPlayingImv;
        TextView musicTitleTv;
        TextView musicArtistTv;
        TextView musicTimeTv;

        public ContentVH(View view) {
            super(view);
            musicPlayingImv = (ImageView) view.findViewById(R.id.item_music_playing_imv);
            musicTitleTv = (TextView) view.findViewById(R.id.item_music_title_tv);
            musicArtistTv = (TextView) view.findViewById(R.id.item_music_artist_tv);
            musicTimeTv = (TextView) view.findViewById(R.id.item_music_time_tv);
        }
    }

    //将歌曲的时间转换为分秒的制度
    private static String formatTime(Long time) {
        String min = time / (1000 * 60) + "";
        String sec = time % (1000 * 60) + "";

        if (min.length() < 2)
            min = "0" + min;
        switch (sec.length()) {
            case 4:
                sec = "0" + sec;
                break;
            case 3:
                sec = "00" + sec;
                break;
            case 2:
                sec = "000" + sec;
                break;
            case 1:
                sec = "0000" + sec;
                break;
        }

        return min + ":" + sec.trim().substring(0, 2);
    }
}
