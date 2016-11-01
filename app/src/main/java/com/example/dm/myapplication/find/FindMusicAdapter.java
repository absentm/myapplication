package com.example.dm.myapplication.find;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dm.myapplication.R;
import com.example.dm.myapplication.beans.MusicBean;

import java.util.List;

/**
 * FindVideoAdapter
 * Created by dm on 16-10-29.
 */

public class FindMusicAdapter extends RecyclerView.Adapter<FindMusicAdapter.ItemMusicHolder>
        implements View.OnClickListener {
    private Context context;
    private List<MusicBean> mDatas;

    public FindMusicAdapter(Context context, List<MusicBean> datas) {
        this.context = context;
        mDatas = datas;
    }

    // 设置item点击事件的回调函数
    private FindMusicAdapter.OnMusicItemClickListener mOnItemClickListener = null;

    // define interface
    public interface OnMusicItemClickListener {
        void onMusicItemClick(View view, MusicBean data);
    }

    @Override
    public FindMusicAdapter.ItemMusicHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.find_music_item, parent, false);
        FindMusicAdapter.ItemMusicHolder viewHolder = new FindMusicAdapter.ItemMusicHolder(view);
        //将创建的View注册点击事件
        view.setOnClickListener(this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(FindMusicAdapter.ItemMusicHolder holder, int position) {
        holder.musicTitleTv.setText(mDatas.get(position).getTitle());
        holder.musicArtistTv.setText(mDatas.get(position).getArtist() + " - ");
        holder.musicTimeTv.setText("[ " + formatTime(mDatas.get(position).getDuration()) + " ]");
        holder.musicPlayingImv.setVisibility(View.GONE);
        // 将数据保存在itemView的Tag中，以便点击时进行获取
        holder.itemView.setTag(mDatas.get(position));
    }

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    @Override
    public void onClick(View view) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            mOnItemClickListener.onMusicItemClick(view, (MusicBean) view.getTag());
        }
    }

    public void setOnItemClickListener(FindMusicAdapter.OnMusicItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public static class ItemMusicHolder extends RecyclerView.ViewHolder {
        ImageView musicPlayingImv;
        TextView musicTitleTv;
        TextView musicArtistTv;
        TextView musicTimeTv;

        public ItemMusicHolder(View view) {
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

    /**
     * 查询专辑封面图片uri
     */
    private static String getCoverUri(Context context, long albumId) {
        String uri = null;
        Cursor cursor = context.getContentResolver().query(
                Uri.parse("content://media/external/audio/albums/" + albumId),
                new String[]{"album_art"}, null, null, null);
        if (cursor != null) {
            cursor.moveToNext();
            uri = cursor.getString(0);
            cursor.close();
        }

        return uri;
    }
}
