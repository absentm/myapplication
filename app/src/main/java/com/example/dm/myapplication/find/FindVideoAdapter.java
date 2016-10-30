package com.example.dm.myapplication.find;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dm.myapplication.R;
import com.example.dm.myapplication.beans.VideoBeans.VideoResultsBean;
import com.example.dm.myapplication.utiltools.DateUtil;

import java.text.ParseException;
import java.util.List;

/**
 * FindVideoAdapter
 * Created by dm on 16-10-29.
 */

public class FindVideoAdapter extends RecyclerView.Adapter<FindVideoAdapter.ItemVideoHolder>
        implements View.OnClickListener {
    private Context context;
    private List<VideoResultsBean> mDatas;

    public FindVideoAdapter(Context context, List<VideoResultsBean> datas) {
        this.context = context;
        mDatas = datas;
    }

    // 设置item点击事件的回调函数
    private FindVideoAdapter.OnVideoItemClickListener mOnItemClickListener = null;

    // define interface
    public interface OnVideoItemClickListener {
        void onVideoItemClick(View view, VideoResultsBean data);
    }

    @Override
    public FindVideoAdapter.ItemVideoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.find_video_item, parent, false);
        FindVideoAdapter.ItemVideoHolder viewHolder = new FindVideoAdapter.ItemVideoHolder(view);
        //将创建的View注册点击事件
        view.setOnClickListener(this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(FindVideoAdapter.ItemVideoHolder holder, int position) {
        holder.videoDescTv.setText(mDatas.get(position).getDesc());
        holder.videoWhoTv.setText(new StringBuilder().append("by ").append(mDatas.get(position).getWho()).toString());
        try {
            holder.videoTimeTv.setText(DateUtil.utc2LocalTime(mDatas.get(position).getPublishedAt()));
        } catch (ParseException e) {
            Log.i("FindVideoAty", e.getMessage());
        }

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
            mOnItemClickListener.onVideoItemClick(view, (VideoResultsBean) view.getTag());
        }
    }

    public void setOnItemClickListener(FindVideoAdapter.OnVideoItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public static class ItemVideoHolder extends RecyclerView.ViewHolder {
        TextView videoDescTv;
        TextView videoWhoTv;
        TextView videoTimeTv;

        public ItemVideoHolder(View view) {
            super(view);
            videoDescTv = (TextView) view.findViewById(R.id.item_video_desc_tv);
            videoWhoTv = (TextView) view.findViewById(R.id.item_video_who_tv);
            videoTimeTv = (TextView) view.findViewById(R.id.item_video_time_tv);
        }
    }
}
