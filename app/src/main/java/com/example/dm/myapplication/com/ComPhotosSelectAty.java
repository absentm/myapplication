package com.example.dm.myapplication.com;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dm.myapplication.R;
import com.litao.android.lib.BaseGalleryActivity;
import com.litao.android.lib.Configuration;
import com.litao.android.lib.entity.PhotoEntry;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * ComPhotosSelectAty: 选择多张图片
 * Created by dm on 16-8-30.
 */
public class ComPhotosSelectAty extends BaseGalleryActivity implements View.OnClickListener {
    private ImageView titleBackImv;
    private TextView titleTextTv;

    private TextView mTextViewOpenAlbum;
    private TextView mTextViewSelectedCount;
    private TextView mTextViewSend;

    private List<PhotoEntry> mSelectedPhotos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_photos_lay);
        EventBus.getDefault().register(this);
        initView();
        attachFragment(R.id.gallery_root);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    private void initView() {
        titleBackImv = (ImageView) findViewById(R.id.title_imv);
        titleTextTv = (TextView) findViewById(R.id.title_text_tv);

        titleBackImv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ComPhotosSelectAty.this.finish();
            }
        });

        mTextViewOpenAlbum = (TextView) findViewById(R.id.album);
        mTextViewSelectedCount = (TextView) findViewById(R.id.selected_count);
        mTextViewSend = (TextView) findViewById(R.id.send_photos);

        mTextViewOpenAlbum.setOnClickListener(this);
        mTextViewSelectedCount.setOnClickListener(this);
        mTextViewSend.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.album:
                openAlbum();
                break;
            case R.id.send_photos:
                sendPhotos();
                break;
        }
    }

    /**
     * @return
     */
    @Override
    public Configuration getConfiguration() {
        //default configuration
        Configuration cfg = new Configuration.Builder()
                .hasCamera(true)
                .hasShade(true)
                .hasPreview(true)
                .setSpaceSize(4)
                .setPhotoMaxWidth(100)
                .setCheckBoxColor(0xFF3F51B5)
                .setDialogHeight(Configuration.DIALOG_HALF)
                .setDialogMode(Configuration.DIALOG_GRID)
                .setMaximum(9)
                .setTip(null)
                .setAblumsTitle(null)
                .build();
        return cfg;
    }

    @Override
    public List<PhotoEntry> getSelectPhotos() {
        return mSelectedPhotos;
    }

    @Override
    public void onSelectedCountChanged(int count) {
        mTextViewSelectedCount.setVisibility(count > 0 ? View.VISIBLE : View.INVISIBLE);
        mTextViewSelectedCount.setText(String.valueOf(count));
    }

    @Override
    public void onAlbumChanged(String name) {
//        getSupportActionBar().setSubtitle(name);
        titleTextTv.setText(name);
    }

    @Override
    public void onTakePhoto(PhotoEntry entry) {
        EventBus.getDefault().post(entry);
        this.finish();
    }

    @Override
    public void onChoosePhotos(List<PhotoEntry> entries) {
        EventBus.getDefault().post(new EventEntry(entries, EventEntry.RECEIVED_PHOTOS_ID));
        finish();
    }

    /**
     * @param entry
     */
    @Override
    public void onPhotoClick(PhotoEntry entry) {

    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void photosMessageEvent(EventEntry entry) {
        if (entry.id == EventEntry.SELECTED_PHOTOS_ID) {
            mSelectedPhotos = entry.photos;
        }
    }
}