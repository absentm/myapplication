package com.example.dm.myapplication.utiltools;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * CompressPhotoUtils
 * Created by dm on 16-9-20.
 */
public class CompressPhotoUtils {
    private List<String> fileList = new ArrayList<>();
//    private ProgressDialog progressDialog;

    public void CompressPhoto(Context context, List<String> list, CompressCallBack callBack) {
        CompressTask task = new CompressTask(context, list, callBack);
        task.execute();
    }

    class CompressTask extends AsyncTask<Void, Integer, Integer> {
        private Context context;
        private List<String> list;
        private CompressCallBack callBack;

        CompressTask(Context context, List<String> list, CompressCallBack callBack) {
            this.context = context;
            this.list = list;
            this.callBack = callBack;
        }

        /**
         * 运行在UI线程中，在调用doInBackground()之前执行
         */
        @Override
        protected void onPreExecute() {
//            progressDialog = ProgressDialog.show(context, null, "处理中...");
        }

        /**
         * 后台运行的方法，可以运行非UI线程，可以执行耗时的方法
         */
        @Override
        protected Integer doInBackground(Void... params) {
            for (int i = 0; i < list.size(); i++) {
                Bitmap bitmap = getBitmap(list.get(i));
                String path = SaveBitmap(bitmap, i);
                fileList.add(path);
            }
            return null;
        }

        /**
         * 运行在ui线程中，在doInBackground()执行完毕后执行
         */
        @Override
        protected void onPostExecute(Integer integer) {
//            progressDialog.dismiss();
            callBack.success(fileList);
        }

        /**
         * 在publishProgress()被调用以后执行，publishProgress()用于更新进度
         */
        @Override
        protected void onProgressUpdate(Integer... values) {
        }
    }

    /**
     * 从sd卡获取压缩图片bitmap
     */
    public static Bitmap getBitmap(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        float hh = 1280f;
        float ww = 720f;
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        newOpts.inSampleSize = be;// 设置缩放比例
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return bitmap;
    }

    /**
     * 保存bitmap到内存卡
     */
    public static String SaveBitmap(Bitmap bmp, int num) {
        File file = new File(AbsentMConstants.TEMP_IMAGES_UPLOAD_DIR);
        String path = null;
        if (!file.exists()) file.mkdirs();

        try {
            path = file.getPath() + "/" + System.currentTimeMillis() + "-" + num + ".jpg";
            FileOutputStream fileOutputStream = new FileOutputStream(path);
            bmp.compress(Bitmap.CompressFormat.JPEG, 80, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return path;
    }

    public interface CompressCallBack {
        void success(List<String> list);
    }
}
