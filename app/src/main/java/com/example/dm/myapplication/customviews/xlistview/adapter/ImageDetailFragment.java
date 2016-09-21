package com.example.dm.myapplication.customviews.xlistview.adapter;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.dm.myapplication.R;
import com.example.dm.myapplication.utiltools.FileUtil;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by dm on 16-4-24.
 * 单张图片显示Fragment
 */
public class ImageDetailFragment extends Fragment {
    private static final String TAG = "ImageDetailFragment";
    public static final int REQUEST_CODE = 0X110;
    public static final String DIALOG = "dialog";

    private String mImageUrl;
    private ImageView mImageView;

    private PhotoViewAttacher mAttacher;

    public static ImageDetailFragment newInstance(String imageUrl) {
        final ImageDetailFragment f = new ImageDetailFragment();

        final Bundle args = new Bundle();
        args.putSerializable("url", imageUrl);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mImageUrl = getArguments() != null ? (String) getArguments().getSerializable("url") : null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.image_detail_fragment, container, false);
        mImageView = (ImageView) v.findViewById(R.id.image);
        mAttacher = new PhotoViewAttacher(mImageView);

        // 轻点照片，返回消失
        mAttacher.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float v, float v1) {
                getActivity().finish();
            }

            @Override
            public void onOutsidePhotoTap() {
                getActivity().finish();
            }
        });

        // 图片的长按事件监听
        mAttacher.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ImageDealDialog imageDealDialog = new ImageDealDialog();
                imageDealDialog.setTargetFragment(ImageDetailFragment.this, REQUEST_CODE);
                imageDealDialog.show(getFragmentManager(), DIALOG);

                return false;
            }
        });

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Glide.with(ImageDetailFragment.this)
                .load(mImageUrl)
                .error(R.drawable.app_icon)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(mImageView);
        mAttacher.update();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE) {
            String dialogItemStr = data.getStringExtra(ImageDealDialog.RESPONSE);
            Log.i(TAG, dialogItemStr);

            switch (dialogItemStr) {
                case "保存到手机":
                    Bitmap bitmap = mAttacher.getVisibleRectangleBitmap();
                    FileUtil.saveBitmapToJpg(getActivity(), bitmap);
                    Toast.makeText(getActivity(), "照片已保存（手机相册 -> AbsentM）", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }

}


