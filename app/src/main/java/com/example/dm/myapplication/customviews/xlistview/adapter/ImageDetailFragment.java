package com.example.dm.myapplication.customviews.xlistview.adapter;


import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.dm.myapplication.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

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
    private ProgressBar progressBar;

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

        progressBar = (ProgressBar) v.findViewById(R.id.loading);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ImageLoader.getInstance().displayImage(mImageUrl, mImageView, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                String message = null;
                switch (failReason.getType()) {
                    case IO_ERROR:
                        message = "下载错误";
                        break;
                    case DECODING_ERROR:
                        message = "图片无法显示";
                        break;
                    case NETWORK_DENIED:
                        message = "网络有问题，无法下载";
                        break;
                    case OUT_OF_MEMORY:
                        message = "图片太大无法显示";
                        break;
                    case UNKNOWN:
                        message = "未知的错误";
                        break;
                }
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                progressBar.setVisibility(View.GONE);
                mAttacher.update();

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE) {
            String dialogItemStr = data.getStringExtra(ImageDealDialog.RESPONSE);
            Log.i(TAG, dialogItemStr);

            switch (dialogItemStr) {
                case "保存到手机":
                    String imgPath = saveImageViewToAlbum(getActivity(), mImageView, ".jpg", "AbsentM", "forum_");
                    Log.i(TAG, "IIIIIII= " + imgPath);
                    Toast.makeText(getActivity(), "照片已保存（手机相册 -> AbsentM）", Toast.LENGTH_LONG).show();

                    break;
                case "分享给好友":
                    Toast.makeText(getActivity(), "Waitting...", Toast.LENGTH_LONG).show();
                    break;
                case "收藏":
                    Toast.makeText(getActivity(), "Waitting...", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }

    /**
     * 将ImageView控件中的照片保存到系统相册中
     *
     * @param context           上下文环境
     * @param imageView         ImageView实例
     * @param fileFormatName    保存图片的格式
     * @param saveDirectiryName 保存图片的目录
     * @param filePreName       保存图片的前缀名称
     * @return 保存的路径
     */
    private String saveImageViewToAlbum(Context context, ImageView imageView, String fileFormatName, String saveDirectiryName, String filePreName) {
        BitmapDrawable bmpDrawable = (BitmapDrawable) imageView.getDrawable();
        Bitmap bmp = bmpDrawable.getBitmap();

        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
            Log.i(TAG, "SD *****>> SD卡不存在");
        } else {
            Log.i(TAG, "SD *****>> SD卡 存在");
        }

        // 创建图片保存目录
        File faceImgDir = new File(Environment.getExternalStorageDirectory(), saveDirectiryName);
        if (!faceImgDir.exists()) {
            faceImgDir.mkdir();
        }

        // 以系统时间命名文件
        String faceImgName = filePreName + String.valueOf(System.currentTimeMillis()) + fileFormatName;
        File file = new File(faceImgDir, faceImgName);

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);

            if (fileFormatName.equals(".jpg")) {
                bmp.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            } else if (fileFormatName.equals(".png")) {
                bmp.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            }

            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 保存后要扫描一下文件，及时更新到系统目录（一定要加绝对路径，这样才能更新）
        MediaScannerConnection.scanFile(context,
                new String[]{Environment.getExternalStorageDirectory() + File.separator + saveDirectiryName + File.separator + faceImgName}, null, null);

        return (Environment.getExternalStorageDirectory() + File.separator + saveDirectiryName + File.separator + faceImgName);
    }

    /**
     * 直接保存到系统相册（红米2）
     *
     * @param context 上下文环境
     * @param bitMap  bitmap对象
     */
    private void saveImageToSysAlbum(Context context, Bitmap bitMap) {
        String imgUrl;
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
            Log.i(TAG, "SD *****>> SD卡不存在");
        } else {
            Log.i(TAG, "SD *****>> SD卡 存在");
        }

        if (bitMap != null) {
            try {
                ContentResolver cr = context.getContentResolver();
                imgUrl = MediaStore.Images.Media.insertImage(cr, bitMap, String.valueOf(System.currentTimeMillis()), "");

                Log.i(TAG, "saveImageToSysAlbum + " + imgUrl);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Log.i(TAG, "?????? = " + Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM).getPath());

        MediaScannerConnection.scanFile(context,
                new String[]{Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DCIM).getPath() + "/"}, null, null);
    }


    /**
     * 保存到指定目录，但能立即更新到系统相册中（红米2）
     *
     * @param context    上下文环境
     * @param faceBitmap 位图资源
     * @return 保存图片的路径
     */
    private String saveBitmapToJpg(Context context, Bitmap faceBitmap) {
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
            Log.i(TAG, "SD *****>> SD卡不存在");
        } else {
            Log.i(TAG, "SD *****>> SD卡 存在");
        }

        // 创建图片保存目录
        File faceImgDir = new File(Environment.getExternalStorageDirectory(), "AbsentM");
        if (!faceImgDir.exists()) {
            faceImgDir.mkdir();
        }

        // 以系统时间命名文件
        String faceImgName = "forum-" + String.valueOf(System.currentTimeMillis()) + ".jpg";
        File file = new File(faceImgDir, faceImgName);

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            faceBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 保存后要扫描一下文件，及时更新到系统目录（一定要加绝对路径，这样才能更新）
        MediaScannerConnection.scanFile(context,
                new String[]{Environment.getExternalStorageDirectory() + File.separator + "AbsentM" + File.separator + faceImgName}, null, null);

        return (Environment.getExternalStorageDirectory() + File.separator + "AbsentM" + File.separator + faceImgName);
    }

}


