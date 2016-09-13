package com.example.dm.myapplication.utiltools;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by dm on 16-4-22.
 * 文件操作工具类
 */
public class FileUtil {
    public static String SDPATH = Environment.getExternalStorageDirectory()
            + "/AbsentM/";


    /**
     * 将Bitmap 图片保存到本地路径，并返回路径
     *
     * @param c        context
     * @param fileName 文件名称
     * @param bitmap   图片
     * @return string
     */
    public static String saveFile(Context c, String fileName, Bitmap bitmap) {
        return saveFile(c, "", fileName, bitmap);
    }

    public static String saveFile(Context c, String filePath, String fileName, Bitmap bitmap) {
        byte[] bytes = bitmapToBytes(bitmap);
        return saveFile(c, filePath, fileName, bytes);
    }

    public static byte[] bitmapToBytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return baos.toByteArray();
    }

    public static String saveFile(Context c, String filePath, String fileName, byte[] bytes) {
        String fileFullName = "";
        FileOutputStream fos = null;
        String dateFolder = new SimpleDateFormat("yyyyMMdd", Locale.CHINA)
                .format(new Date());
        try {
            String suffix = "";
            if (filePath == null || filePath.trim().length() == 0) {
                filePath = Environment.getExternalStorageDirectory() + "/AbsentM/" + dateFolder + "/";
            }
            File file = new File(filePath);
            if (!file.exists()) {
                file.mkdirs();
            }
            File fullFile = new File(filePath, fileName + suffix);
            fileFullName = fullFile.getPath();
            fos = new FileOutputStream(new File(filePath, fileName + suffix));
            fos.write(bytes);
        } catch (Exception e) {
            fileFullName = "";
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    fileFullName = "";
                }
            }
        }
        return fileFullName;
    }

    public static Bitmap getScaledBitmap(String filePath, int dstWidth) {
        BitmapFactory.Options localOptions = new BitmapFactory.Options();
        localOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, localOptions);
        int originWidth = localOptions.outWidth;
        int originHeight = localOptions.outHeight;

        localOptions.inSampleSize = originWidth > originHeight ? originWidth / dstWidth
                : originHeight / dstWidth;
        localOptions.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, localOptions);
    }

    public static void saveBitmap(Bitmap bm, String picName) {
        try {
            if (!isFileExist("")) {
                File tempf = createSDDir("");
            }
            File f = new File(SDPATH, picName + ".JPEG");
            if (f.exists()) {
                f.delete();
            }
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static File createSDDir(String dirName) throws IOException {
        File dir = new File(SDPATH + dirName);
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {

            System.out.println("createSDDir:" + dir.getAbsolutePath());
            System.out.println("createSDDir:" + dir.mkdir());
        }
        return dir;
    }

    public static boolean isFileExist(String fileName) {
        File file = new File(SDPATH + fileName);
        file.isFile();
        return file.exists();
    }

    public static void delFile(String fileName) {
        File file = new File(SDPATH + fileName);
        if (file.isFile()) {
            file.delete();
        }
        file.exists();
    }

    public static void deleteDir() {
        File dir = new File(SDPATH);
        if (dir == null || !dir.exists() || !dir.isDirectory())
            return;

        for (File file : dir.listFiles()) {
            if (file.isFile())
                file.delete();
            else if (file.isDirectory())
                deleteDir();
        }
        dir.delete();
    }

    public static boolean fileIsExists(String path) {
        try {
            File f = new File(path);
            if (!f.exists()) {
                return false;
            }
        } catch (Exception e) {

            return false;
        }
        return true;
    }

    /**
     * 保存到指定目录，但能立即更新到系统相册中（红米2）
     *
     * @param context    上下文环境
     * @param faceBitmap 位图资源
     * @return 保存图片的路径
     */
    public static String saveBitmapToJpg(Context context, Bitmap faceBitmap) {
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
            Log.i("LOG", "SD *****>> SD卡不存在");
        } else {
            Log.i("LOG", "SD *****>> SD卡 存在");
        }

        // 创建图片保存目录
        File faceImgDir = new File(Environment.getExternalStorageDirectory(), "AbsentM");
        if (!faceImgDir.exists()) {
            faceImgDir.mkdir();
        }

        // 以系统时间命名文件
        String faceImgName = "absentm-" + String.valueOf(System.currentTimeMillis()) + ".jpg";
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
                new String[]{Environment.getExternalStorageDirectory() +
                        File.separator + "AbsentM" + File.separator + faceImgName},
                null, null);

        return (Environment.getExternalStorageDirectory() + File.separator +
                "AbsentM" + File.separator + faceImgName);
    }

    /**
     * ImageView 转换为 Bimap
     *
     * @param context
     * @param imageView
     * @return
     */
    public static Bitmap imageView2Bitmap(Context context, ImageView imageView) {
        Drawable drawable = imageView.getDrawable();
        BitmapDrawable bd = (BitmapDrawable) drawable;
        Bitmap bitmap = bd.getBitmap();

        return bitmap;
    }
}
