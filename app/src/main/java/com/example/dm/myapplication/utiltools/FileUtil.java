package com.example.dm.myapplication.utiltools;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

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
}
