package com.example.dm.myapplication.find;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.dm.myapplication.R;
import com.example.dm.myapplication.utiltools.FileUtil;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * FindPhotoDetailAty: 图片详情
 * Created by dm on 16-9-2.
 */
public class FindPhotoDetailAty extends Activity {
    private final static String LOG = "FindPhotoDetailAty";

    private ImageView mImageView;
    private PhotoViewAttacher photoViewAttacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_photo_show);

        initView();
        eventDeal();
    }

    private void initView() {
        mImageView = (ImageView) findViewById(R.id.find_photo_imv);
        ImageView titlteImv = (ImageView) findViewById(R.id.title_imv);
        titlteImv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FindPhotoDetailAty.this.finish();
            }
        });
    }

    private void eventDeal() {
        Bundle bundle = new Bundle();
        bundle = getIntent().getExtras();

        String imageUrl = bundle.getString("photoUrl");

        // load images
        Glide.with(FindPhotoDetailAty.this)
                .load(imageUrl)
                .into(mImageView);

        photoViewAttacher = new PhotoViewAttacher(mImageView);
        photoViewAttacher.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final AlertDialog dialog = new AlertDialog.Builder(FindPhotoDetailAty.this)
                        .setCancelable(false)
                        .setMessage("保存到手机?")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Bitmap bitmap = photoViewAttacher.getVisibleRectangleBitmap();
                                String savePath = FileUtil.saveBitmapToJpg(FindPhotoDetailAty.this, bitmap);
                                Toast.makeText(FindPhotoDetailAty.this, "save -> " +
                                        savePath, Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        }).create();
                dialog.show();

                return true;
            }
        });
    }

}
