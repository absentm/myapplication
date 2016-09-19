package com.example.dm.myapplication.customviews.xlistview.adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.dm.myapplication.R;
import com.example.dm.myapplication.beans.ComUserPostInfo;
import com.example.dm.myapplication.com.ComImagePagerActivity;
import com.example.dm.myapplication.customviews.ninegridview.NineGridView;
import com.example.dm.myapplication.utiltools.HttpUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dm on 16-4-24.
 * 发布内容适配器
 */
public class ComAppAdapter extends BaseAdapter {

    private List<ComUserPostInfo> mList;
    private Context mContext;

    public ComAppAdapter(Context _context) {
        this.mContext = _context;
    }

    public void setData(List<ComUserPostInfo> _list) {
        this.mList = _list;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public ComUserPostInfo getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.com_grid_item, parent, false);

            holder.userHeadImageImv = (ImageView) convertView.findViewById(R.id.user_img_sImv);
            holder.userNickNameTv = (TextView) convertView.findViewById(R.id.user_name_tv);
            holder.userTimeTv = (TextView) convertView.findViewById(R.id.user_time_tv);
            holder.userContentTv = (TextView) convertView.findViewById(R.id.user_content_tv);
            holder.gridView = (NineGridView) convertView.findViewById(R.id.gridView);

            holder.userRepostNumTv = (TextView) convertView.findViewById(R.id.repost_counter_tv);
            holder.userCommentNumTv = (TextView) convertView.findViewById(R.id.comment_counter_tv);
            holder.userLikeNumTv = (TextView) convertView.findViewById(R.id.like_counter_tv);

            holder.userRepostLout = (LinearLayout) convertView.findViewById(R.id.user_repost_llout);
            holder.userCommentLout = (LinearLayout) convertView.findViewById(R.id.user_comment_llout);
            holder.userLikeLout = (LinearLayout) convertView.findViewById(R.id.user_like_llout);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ComUserPostInfo mComUserPostInfo = getItem(position);
        if (mList != null && mList.size() > 0) {
            ImageLoader.getInstance().displayImage(mComUserPostInfo.getUserHeadImgUrl(),
                    holder.userHeadImageImv, HttpUtil.DefaultOptions);
            holder.userNickNameTv.setText(mComUserPostInfo.getUserNickNameStr());
            holder.userTimeTv.setText(mComUserPostInfo.getUserTimeStr());
            holder.userContentTv.setText(mComUserPostInfo.getUserContentStr());

            holder.userRepostNumTv.setText(String.valueOf(mComUserPostInfo.getUserRepostCounter()));
            holder.userCommentNumTv.setText(String.valueOf(mComUserPostInfo.getUserCommentCounter()));
            holder.userLikeNumTv.setText(String.valueOf(mComUserPostInfo.getUserLikeCounter()));

            final List<String> userImgsArrayList = mComUserPostInfo.getUserImageUrlList();
            if (userImgsArrayList == null || userImgsArrayList.size() == 0) {
                holder.gridView.setVisibility(View.GONE);
            } else {
                holder.gridView.setVisibility(View.VISIBLE);
                holder.gridView.setAdapter(
                        new ComPhotoGridAdapter(mComUserPostInfo.getUserImageUrlList(), mContext));
            }

            holder.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    imageBrower(position, userImgsArrayList);
                }
            });

            holder.gridView.setOnTouchInvalidPositionListener(
                    new NineGridView.OnTouchInvalidPositionListener() {
                        @Override
                        public boolean onTouchInvalidPosition(int motionEvent) {
                            return false;
                        }
                    });
        }

        return convertView;
    }

    /**
     * 打开图片查看器
     *
     * @param position          图片在九宫格中的位置
     * @param userImgsArrayList 图片列表
     */
    private void imageBrower(int position, List<String> userImgsArrayList) {
        Intent intent = new Intent(mContext, ComImagePagerActivity.class);

        intent.putExtra(ComImagePagerActivity.EXTRA_IMAGE_URLS, (java.io.Serializable) userImgsArrayList);
        intent.putExtra(ComImagePagerActivity.EXTRA_IMAGE_INDEX, position);

        mContext.startActivity(intent);
    }

    public class ViewHolder {
        public ImageView userHeadImageImv;   // 头像
        public TextView userNickNameTv;   // 昵称
        public TextView userTimeTv;  // 发布时间
        public TextView userContentTv; // 发布内容

        public NineGridView gridView;  // 图片集

        public TextView userRepostNumTv; // 转发数目
        public TextView userCommentNumTv;  // 评论数
        public TextView userLikeNumTv; // 点赞数

        public LinearLayout userRepostLout;   // 转发键
        public LinearLayout userCommentLout;   // 评论键
        public LinearLayout userLikeLout;  // 点赞键
    }

    public void addDataInTop(ComUserPostInfo comUserPostInfo) {
        if (mList == null) {
            mList = new ArrayList<>();
        } else if (mList.isEmpty()) {
            mList.add(comUserPostInfo);
        } else {
            mList.add(0, comUserPostInfo);
        }

        notifyDataSetChanged();
    }

}
