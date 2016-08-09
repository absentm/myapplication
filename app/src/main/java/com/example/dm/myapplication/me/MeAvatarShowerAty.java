package com.example.dm.myapplication.me;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.dm.myapplication.R;
import com.example.dm.myapplication.beans.AppUser;
import com.example.dm.myapplication.utiltools.HttpUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import cn.bmob.v3.BmobUser;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by dm on 16-4-22.
 * 图像裁剪
 */
public class MeAvatarShowerAty extends Activity {
    private ImageView avatarImv;
    private PhotoViewAttacher photoViewAttacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.me_avatar_shower_layout);

        initView();
        eventDeal();
    }


    private void initView() {
        avatarImv = (ImageView) findViewById(R.id.me_show_avatar_imv);
        ImageView titlteImv = (ImageView) findViewById(R.id.title_imv);
        titlteImv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MeAvatarShowerAty.this.finish();
            }
        });
    }

    private void eventDeal() {
        AppUser appUser = BmobUser.getCurrentUser(MeAvatarShowerAty.this, AppUser.class);
        String avatarUrl = appUser.getUserAvatarUrl();
        ImageSize targetSize = new ImageSize(300, 300);
        Bitmap bitmap = ImageLoader.getInstance().loadImageSync(avatarUrl, targetSize,
                HttpUtil.DefaultOptions);
        avatarImv.setImageBitmap(bitmap);

        photoViewAttacher = new PhotoViewAttacher(avatarImv);

        photoViewAttacher.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String[] choices = {"保存至本地"};
                //包含多个选项的对话框
                AlertDialog dialog = new AlertDialog.Builder(MeAvatarShowerAty.this)
                        .setItems(choices, onselect).create();
                dialog.show();
                return true;
            }
        });
    }

    /**
     * 选项的事件监听器
     */
    DialogInterface.OnClickListener onselect = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case 0:
                    Bitmap bitmap = photoViewAttacher.getVisibleRectangleBitmap();
                    String savePath = saveBitmapToJpg(MeAvatarShowerAty.this, bitmap);
                    Toast.makeText(MeAvatarShowerAty.this, "头像保存至: " +
                            savePath, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

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

}
