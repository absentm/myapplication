package com.example.dm.myapplication.find;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dm.myapplication.R;
import com.example.dm.myapplication.beans.NotesBean;

import java.util.ArrayList;
import java.util.List;

/**
 * FindNotesAdapter
 * Created by dm on 16-9-24.
 */

public class FindNotesAdapter
        extends RecyclerView.Adapter<FindNotesAdapter.ItemNoteHolder>
        implements View.OnClickListener, View.OnLongClickListener {

    private Context context;
    private List<NotesBean> mDatas;

    public FindNotesAdapter(Context context, List<NotesBean> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
    }

    // 设置item点击事件的回调函数
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    // define interface
    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, NotesBean data);
    }

    // 设置item点击事件的回调函数
    private OnRecyclerViewItemLongClickListener mOnItemLongClickListener = null;

    // define interface
    public interface OnRecyclerViewItemLongClickListener {
        void onItemLongClick(View view, NotesBean data);
    }


    @Override
    public FindNotesAdapter.ItemNoteHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.find_note_item, parent, false);
        FindNotesAdapter.ItemNoteHolder vh = new FindNotesAdapter.ItemNoteHolder(view);
        //将创建的View注册点击事件
        view.setOnClickListener(this);
        view.setOnLongClickListener(this);
        return vh;
    }

    @Override
    public void onBindViewHolder(FindNotesAdapter.ItemNoteHolder holder, int position) {
        // 时间轴竖线的layout
        holder.noteTitleTv.setText(mDatas.get(position).getNoteTitle());
        holder.noteContentTv.setText(mDatas.get(position).getNoteContent());
        holder.noteTimeTv.setText(mDatas.get(position).getNoteTime());

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
            mOnItemClickListener.onItemClick(view, (NotesBean) view.getTag());
        }
    }

    @Override
    public boolean onLongClick(View view) {
        if (mOnItemLongClickListener != null) {
            mOnItemLongClickListener.onItemLongClick(view, (NotesBean) view.getTag());
        }

        return false;
    }

    public void setOnItemClickListener(FindNotesAdapter.OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public void setOnItemLongClickListener(FindNotesAdapter.OnRecyclerViewItemLongClickListener listener) {
        this.mOnItemLongClickListener = listener;
    }

    public void addDataInTop(NotesBean notesBean) {
        if (mDatas == null) {
            mDatas = new ArrayList<>();
        }

        mDatas.add(0, notesBean);
        notifyDataSetChanged();
    }

    public static class ItemNoteHolder extends RecyclerView.ViewHolder {
        TextView noteTitleTv;
        TextView noteContentTv;
        TextView noteTimeTv;

        public ItemNoteHolder(View view) {
            super(view);
            noteTitleTv = (TextView) view.findViewById(R.id.item_notes_title_tv);
            noteContentTv = (TextView) view.findViewById(R.id.item_notes_content_tv);
            noteTimeTv = (TextView) view.findViewById(R.id.item_notes_time_tv);
        }
    }

}
