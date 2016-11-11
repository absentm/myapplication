package com.example.dm.myapplication.find;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dm.myapplication.R;
import com.example.dm.myapplication.beans.SearchBeans.SearchResultsBean;

import java.util.List;

/**
 * FindSearchAdapter
 * Created by dm on 16-11-10.
 */
public class FindSearchAdapter
        extends RecyclerView.Adapter<FindSearchAdapter.ItemHolder>
        implements View.OnClickListener {

    private Context context;
    private List<SearchResultsBean> mDatas;

    public FindSearchAdapter(Context context, List<SearchResultsBean> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
    }

    // 设置item点击事件的回调函数
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    // define interface
    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, SearchResultsBean data);
    }

    @Override
    public FindSearchAdapter.ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.find_search_item, parent, false);
        FindSearchAdapter.ItemHolder vh = new FindSearchAdapter.ItemHolder(view);
        //将创建的View注册点击事件
        view.setOnClickListener(this);
        return vh;
    }

    @Override
    public void onBindViewHolder(FindSearchAdapter.ItemHolder holder, int position) {
//        String typeAndWhoStr = "<font color=\"#666666\"> ( "
//                + mDatas.get(position).getType()
//                + " via." + mDatas.get(position).getWho() + ")  </font>";

        int textColor = Color.parseColor("#4d000000");

        String textStr = mDatas.get(position).getDesc() + " ("
                + " via. " + mDatas.get(position).getWho() + ")";
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(textStr);
//        spannableStringBuilder.setSpan(new ForegroundColorSpan(textColor),
//                textStr.lastIndexOf("("), textStr.lastIndexOf(")") + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableStringBuilder.setSpan(new TextAppearanceSpan(context, R.style.text_span_style),
                textStr.lastIndexOf("("), textStr.lastIndexOf(")") + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        holder.searchResultContentTv.setText(spannableStringBuilder);

//        holder.searchResultContentTv.setText(mDatas.get(position).getDesc() + " ("
//                + mDatas.get(position).getType()
//                + " via." + mDatas.get(position).getWho() + ")");

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
            mOnItemClickListener.onItemClick(view, (SearchResultsBean) view.getTag());
        }
    }

    public void setOnItemClickListener(FindSearchAdapter.OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public static class ItemHolder extends RecyclerView.ViewHolder {
        TextView searchResultContentTv;

        public ItemHolder(View view) {
            super(view);
            searchResultContentTv = (TextView) view.findViewById(R.id.item_search_content_tv);
        }
    }

}
