package com.example.dm.myapplication.find;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.dm.myapplication.R;

import java.util.List;

/**
 * FindMeizhiImageAdapter
 * <p/>
 * Created by dm on 16-9-2.
 */
public class FindMeizhiImageAdapter extends
        RecyclerView.Adapter<FindMeizhiImageAdapter.ImageViewHolder> implements View.OnClickListener {

    private static final String TAG = "FindMeizhiImageAdapter";

    private Context context;
    private List<String> imgUrlList;

    public FindMeizhiImageAdapter(Context context, List<String> imgUrlList) {
        this.context = context;
        this.imgUrlList = imgUrlList;
    }

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;


    //define interface
    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, String data);
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.find_image_item, null);
        ImageViewHolder vh = new ImageViewHolder(view);
        //将创建的View注册点击事件
        view.setOnClickListener(this);
        return vh;
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        // load images
        Glide.with(context)
                .load(imgUrlList.get(position))
                .crossFade()
                .into(holder.imageView);

//        ImageLoader慢
//        ImageLoader.getInstance().displayImage(imgUrlList.get(position),
//                holder.imageView, HttpUtil.DefaultOptions);

        //将数据保存在itemView的Tag中，以便点击时进行获取
        holder.itemView.setTag(imgUrlList.get(position));
    }

    @Override
    public int getItemCount() {
        return imgUrlList.size();
    }

    class ImageViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        public ImageViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.iv_item_img);
        }
    }

    @Override
    public void onClick(View view) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            mOnItemClickListener.onItemClick(view, (String) view.getTag());
        }
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}
