package com.example.dm.myapplication.me;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.dm.myapplication.R;
import com.example.dm.myapplication.beans.AppUser;
import com.example.dm.myapplication.utiltools.HttpUtil;
import com.example.dm.myapplication.utiltools.StringUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;

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
        AppUser appUser = BmobUser.getCurrentUser(AppUser.class);
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
                    String savePath = StringUtils.saveBitmapToJpg(MeAvatarShowerAty.this, bitmap);
                    Toast.makeText(MeAvatarShowerAty.this, "头像保存至: " +
                            savePath, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
}
