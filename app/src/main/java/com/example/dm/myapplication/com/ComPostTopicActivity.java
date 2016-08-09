package com.example.dm.myapplication.com;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dm.myapplication.R;
import com.example.dm.myapplication.beans.ImageItem;
import com.example.dm.myapplication.customviews.CustomAvatarDialog;
import com.example.dm.myapplication.utiltools.ActivityCollector;
import com.example.dm.myapplication.utiltools.BimpUtil;
import com.example.dm.myapplication.utiltools.FileUtil;
import com.example.dm.myapplication.utiltools.ResUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by dm on 16-4-26.
 * 发表话题
 */
public class ComPostTopicActivity extends Activity {
    private static final int TAKE_PICTURE = 0x000001;

    private TextView mTitleCancleTv;
    private TextView mTitlePostTv;
    private EditText mPostComtentEt;

    private GridView mPostPhotoGridView;
    private GridAdapter adapter;

    public static Bitmap bimap;
    private View parentView;
    private CustomAvatarDialog mCustomAvatarDialog;

    private String filePath;
    private String fileName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ResUtil.init(this);
        bimap = BitmapFactory.decodeResource(getResources(),
                R.drawable.icon_addpic_unfocused);
        parentView = getLayoutInflater().inflate(R.layout.com_post_content_layout, null);
        setContentView(parentView);

        ActivityCollector.addActivity(this);
        initView();
        evensDeal();
    }


    private void initView() {
        mTitleCancleTv = (TextView) findViewById(R.id.title_tv);
        mTitlePostTv = (TextView) findViewById(R.id.title_right_tv);
        mPostComtentEt = (EditText) findViewById(R.id.com_post_comtent_et);
        mPostPhotoGridView = (GridView) findViewById(R.id.com_post_photo_gdv);
    }

    private void evensDeal() {
        // 取消按钮的事件处理
        mTitleCancleTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BimpUtil.tempSelectBitmap.clear();
                ActivityCollector.finishAll();
            }
        });

        // 发表按钮的事件处理
        mTitlePostTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        photoGridDeal();

    }

    private void photoGridDeal() {
        mPostPhotoGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        adapter = new GridAdapter(this);
        adapter.update();
        mPostPhotoGridView.setAdapter(adapter);
        mPostPhotoGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {// 查看某个照片

            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int arg2, long arg3) {
                if (arg2 == BimpUtil.tempSelectBitmap.size()) {
                    mCustomAvatarDialog = new CustomAvatarDialog(ComPostTopicActivity.this,
                            onAddPhotoClickListener);
                    mCustomAvatarDialog.show();
                } else {
                    Toast.makeText(ComPostTopicActivity.this,
                            BimpUtil.tempSelectBitmap.get(arg2).imagePath,
                            Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(ComPostTopicActivity.this,
                            ComPhotoShowActivity.class);
                    intent.putExtra("position", "1");
                    intent.putExtra("ID", arg2);
                    startActivity(intent);
                }
            }
        });
    }

    private View.OnClickListener onAddPhotoClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.me_editor_avatar_takephoto_btn:
                    mCustomAvatarDialog.dismiss();
                    captureImageEvents();
                    break;
                case R.id.me_editor_avatar_album_btn:
                    mCustomAvatarDialog.dismiss();
                    selectAlbumEvents();
                    break;
                default:
                    break;
            }
        }
    };


    private void captureImageEvents() {
        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED)) {
            Intent takePhotoIntent = new Intent("android.media.action.IMAGE_CAPTURE");

            fileName = "forum_capture_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
            File fileDir = new File(Environment.getExternalStorageDirectory(), "AbsentM");
            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }

            filePath = Environment.getExternalStorageDirectory() + File.separator + "AbsentM"
                    + File.separator + fileName;
            takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                    Uri.fromFile(new File(fileDir, fileName)));
            takePhotoIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
            startActivityForResult(takePhotoIntent, TAKE_PICTURE);

        } else {
            Toast.makeText(ComPostTopicActivity.this, "没有内存卡不能拍照", Toast.LENGTH_LONG).show();
        }
    }

    private void selectAlbumEvents() {
        startActivity(new Intent(ComPostTopicActivity.this, ComAlbumActivity.class));
        overridePendingTransition(
                R.anim.activity_translate_in,
                R.anim.activity_translate_out);
    }

    public class GridAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        private int selectedPosition = -1;
        private boolean shape;

        public boolean isShape() {
            return shape;
        }

        public void setShape(boolean shape) {
            this.shape = shape;
        }

        public GridAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        public void update() {
            loading();
        }

        public int getCount() {
            if (BimpUtil.tempSelectBitmap.size() == 9) {
                return 9;
            }
            return (BimpUtil.tempSelectBitmap.size() + 1);
        }

        public Object getItem(int arg0) {
            return null;
        }

        public long getItemId(int arg0) {
            return 0;
        }

        public void setSelectedPosition(int position) {
            selectedPosition = position;
        }

        public int getSelectedPosition() {
            return selectedPosition;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.com_post_photo_grid_item,
                        parent, false);
                holder = new ViewHolder();
                holder.image = (ImageView) convertView
                        .findViewById(R.id.com_item_grid_image);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if (position == BimpUtil.tempSelectBitmap.size()) {
                holder.image.setImageBitmap(BitmapFactory.decodeResource(
                        getResources(), R.drawable.icon_addpic_unfocused));
                if (position == 9) {
                    holder.image.setVisibility(View.GONE);
                }
            } else {
                holder.image.setImageBitmap(BimpUtil.tempSelectBitmap.get(position)
                        .getBitmap());
            }

            return convertView;
        }

        public class ViewHolder {
            public ImageView image;
        }

        Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        adapter.notifyDataSetChanged();
                        break;
                }
                super.handleMessage(msg);
            }
        };

        public void loading() {
            new Thread(new Runnable() {
                public void run() {
                    while (true) {
                        if (BimpUtil.max == BimpUtil.tempSelectBitmap.size()) {
                            Message message = new Message();
                            message.what = 1;
                            handler.sendMessage(message);
                            break;
                        } else {
                            BimpUtil.max += 1;
                            Message message = new Message();
                            message.what = 1;
                            handler.sendMessage(message);
                        }
                    }
                }
            }).start();
        }
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PICTURE:
                Bitmap bitmap = null;
                if (resultCode == RESULT_OK) {
                    bitmap = FileUtil.getScaledBitmap(filePath, 600);
                    Log.i("LOG", "====== resultCode: " + resultCode);

                    try {
                        FileOutputStream fileOutputStream = new FileOutputStream(filePath);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);

                        fileOutputStream.flush();
                        fileOutputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    MediaScannerConnection.scanFile(getApplicationContext(),
                            new String[]{Environment.getExternalStorageDirectory() + File.separator
                                    + "AbsentM" + File.separator + fileName}, null, null);
                }

                if (BimpUtil.tempSelectBitmap.size() < 9 && resultCode == RESULT_OK) {
                    System.out.println("添加照片");
                    ImageItem takePhoto = new ImageItem();
                    takePhoto.setBitmap(bitmap);
                    takePhoto.setImagePath(filePath);
                    takePhoto.setImageName(fileName);
                    BimpUtil.tempSelectBitmap.add(takePhoto);
                    System.out.println("里面有多少张图片" + BimpUtil.tempSelectBitmap.size());
                }

                break;
        }
    }

    /**
     * 物理按键返回键的逻辑控制
     *
     * @param keyCode 键值
     * @param event   事件
     * @return bool
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            BimpUtil.tempSelectBitmap.clear();
            ActivityCollector.finishAll();
        }

        return true;
    }

    protected void onRestart() {
        adapter.update();
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}
