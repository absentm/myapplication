package com.example.dm.myapplication.find;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.dm.myapplication.R;
import com.example.dm.myapplication.beans.MusicEntity;
import com.example.dm.myapplication.customviews.MarqueeTextView;
import com.example.dm.myapplication.utiltools.FileUtil;

import java.util.ArrayList;
import java.util.List;

import me.yokeyword.indexablerv.IndexableAdapter;
import me.yokeyword.indexablerv.IndexableLayout;

import static com.example.dm.myapplication.find.FindMusicPlayService.mMediaPlayer;

/**
 * FindIndexMusicAty
 * Created by dm on 16-11-2.
 */
public class FindIndexMusicAty extends Activity implements View.OnClickListener,
        IndexableAdapter.OnItemContentClickListener<MusicEntity> {

    private List<FindIndexMusicAdapter.ContentVH> listViewHolder = new ArrayList<>();

    private ImageButton titleBackIBtn;
    private ProgressBar mProgressBar;
    private ImageView mCurrMusicAlbumImv;
    private MarqueeTextView mCurrMusicTitleTv;
    private TextView mCurrMusicArtistTv;
    private ImageButton mCurrMusicPlayOrPauseIBtn;

    private IndexableLayout mIndexableLayout;
    private FindIndexMusicAdapter mFindIndexMusicAdapter;
    private List<MusicEntity> mDatas;

    private FindMusicPlayService musicService;

    public static String currMusicTitle;
    public static String currMusicArtist;
    public static long currMusicAlbum_id;

    public static String lastMusicTitle;
    public static String lastMusicArtist;
    public static long lastMusicAlbum_id;

    private int isFirstCreateFlag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_index_music_lay);
        mDatas = FileUtil.getLocalIndexMusics(FindIndexMusicAty.this.getContentResolver());

        initView();
        setUpListener();
        eventDeal();

        if (isFirstCreateFlag != 0) {
            getLastMusicInfos();
        }
    }

    private void initView() {
        titleBackIBtn = (ImageButton) findViewById(R.id.title_music_left_imv);
        mProgressBar = (ProgressBar) findViewById(R.id.find_index_music_pbar);
        mCurrMusicAlbumImv = (ImageView) findViewById(R.id.find_music_album_imv);
        mCurrMusicTitleTv = (MarqueeTextView) findViewById(R.id.item_music_title_tv);
        mCurrMusicTitleTv.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        mCurrMusicTitleTv.setSingleLine(true);
        mCurrMusicArtistTv = (TextView) findViewById(R.id.item_music_artist_tv);
        mCurrMusicPlayOrPauseIBtn = (ImageButton) findViewById(R.id.find_music_play_imv);

        mIndexableLayout = (IndexableLayout) findViewById(R.id.find_music_indexableLayout);
        mProgressBar.setVisibility(View.VISIBLE);

        mFindIndexMusicAdapter = new FindIndexMusicAdapter(this);
        mIndexableLayout.setAdapter(mFindIndexMusicAdapter);
        mFindIndexMusicAdapter.setDatas(mDatas, new IndexableAdapter.IndexCallback<MusicEntity>() {
            @Override
            public void onFinished(List<MusicEntity> datas) {
                mProgressBar.setVisibility(View.GONE);
            }
        });
        // mIndexableLayout.setOverlayStyle_MaterialDesign(R.color.colorAccent);
        // 快速排序。  排序规则设置为：只按首字母  （默认全拼音排序）  效率很高，是默认的10倍左右。  按需开启～
        mIndexableLayout.setFastCompare(true);

    }

    private void setUpListener() {
        titleBackIBtn.setOnClickListener(FindIndexMusicAty.this);
        mCurrMusicPlayOrPauseIBtn.setOnClickListener(FindIndexMusicAty.this);
        mFindIndexMusicAdapter.setOnItemContentClickListener(FindIndexMusicAty.this);
    }

    private void eventDeal() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_music_left_imv:
                FindIndexMusicAty.this.finish();
                break;
            case R.id.find_music_play_imv:
                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.pause();
                    mCurrMusicPlayOrPauseIBtn
                            .setImageResource(R.drawable.ic_play_circle_outline_black_24dp);
                } else if (isFirstCreateFlag != 0) {
                    mMediaPlayer.start();
                    mCurrMusicPlayOrPauseIBtn
                            .setImageResource(R.drawable.ic_pause_circle_outline_black_24dp);
                }
                break;
        }
    }

    /**
     * item内容 点击事件
     *
     * @param v
     * @param originalPosition
     * @param currentPosition
     * @param entity
     */
    @Override
    public void onItemClick(View v, int originalPosition, int currentPosition, MusicEntity entity) {
        if (originalPosition >= 0) {
            int textColorNorm = Color.parseColor("#b3000000");
            int textColorChange = Color.parseColor("#008080");

            // 复位其他被点选过的Item，重置为正常
            for (int i = 0; i < listViewHolder.size(); i++) {
                listViewHolder.get(i).musicPlayingImv.setVisibility(View.GONE);
                listViewHolder.get(i).musicArtistTv.setTextColor(textColorNorm);
                listViewHolder.get(i).musicTitleTv.setTextColor(textColorNorm);
                listViewHolder.get(i).musicTimeTv.setTextColor(textColorNorm);
            }

            // item view的获取方法1
//            FindIndexMusicAdapter.ContentVH contentVH = (FindIndexMusicAdapter.ContentVH)
//                    mIndexableLayout.getRecyclerView().getChildViewHolder(v);
            // item view的获取方法2
            FindIndexMusicAdapter.ContentVH contentVH = (FindIndexMusicAdapter.ContentVH) v.getTag();
            contentVH.musicPlayingImv.setVisibility(View.VISIBLE);
            contentVH.musicArtistTv.setTextColor(textColorChange);
            contentVH.musicTitleTv.setTextColor(textColorChange);
            contentVH.musicTimeTv.setTextColor(textColorChange);
            listViewHolder.clear();
            listViewHolder.add(contentVH);

            currMusicTitle = entity.getTitle();
            currMusicArtist = entity.getArtist();
            currMusicAlbum_id = entity.getAlbum_id();
            Glide.with(FindIndexMusicAty.this)
                    .load(getCoverUri(FindIndexMusicAty.this, currMusicAlbum_id))
                    .placeholder(R.drawable.app_icon)
                    .error(R.drawable.app_icon)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(mCurrMusicAlbumImv);

            mCurrMusicTitleTv.setText(currMusicTitle);
            mCurrMusicArtistTv.setText(currMusicArtist);
            mCurrMusicPlayOrPauseIBtn.setImageResource(R.drawable.ic_pause_circle_outline_black_24dp);

            saveLastMusicInfos();

            Intent intent = new Intent();
            intent.putExtra("url", entity.getUrl());
            intent.putExtra("title", entity.getTitle());
            intent.putExtra("artist", entity.getArtist());
            intent.putExtra("album", entity.getAlbum());
            intent.putExtra("album_id", entity.getAlbum_id());
            intent.setClass(FindIndexMusicAty.this, FindMusicPlayService.class);
            startService(intent);
            bindService(intent, sc, BIND_AUTO_CREATE);

            isFirstCreateFlag = 1;
        } else {
            Toast.makeText(FindIndexMusicAty.this,
                    "选中Header:" + entity.getTitle() + "  当前位置:" + currentPosition, Toast.LENGTH_SHORT).show();
        }
    }

    private ServiceConnection sc = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            musicService = ((FindMusicPlayService.MyBinder) iBinder).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            musicService = null;
        }
    };

    /**
     * 查询专辑封面图片uri
     */
    private String getCoverUri(Context context, long albumId) {
        String uri = null;
        Cursor cursor = context.getContentResolver().query(
                Uri.parse("content://media/external/audio/albums/" + albumId),
                new String[]{"album_art"}, null, null, null);
        if (cursor != null) {
            cursor.moveToNext();
            uri = cursor.getString(0);
            cursor.close();
        }

        return uri;
    }

    private void saveLastMusicInfos() {
        SharedPreferences sharedPreferences = getSharedPreferences("lastMusicInfos", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("lastMusicTitle", currMusicTitle);
        editor.putString("lastMusicArtist", currMusicArtist);
        editor.putLong("lastMusicAlbum_id", currMusicAlbum_id);
        editor.apply();

        Log.i("music", "currMusicTitle: " + currMusicTitle);
    }

    private void getLastMusicInfos() {
        SharedPreferences sharedPreferences = getSharedPreferences("lastMusicInfos", MODE_PRIVATE);
        lastMusicTitle = sharedPreferences.getString("lastMusicTitle", "传奇");
        lastMusicArtist = sharedPreferences.getString("lastMusicArtist", "王菲");
        lastMusicAlbum_id = sharedPreferences.getLong("lastMusicAlbum_id", 236);

        Log.i("music", "lastMusicTitle: " + lastMusicTitle);

        if (lastMusicTitle != null && lastMusicArtist != null && lastMusicAlbum_id != 0) {
            Glide.with(FindIndexMusicAty.this)
                    .load(getCoverUri(FindIndexMusicAty.this, lastMusicAlbum_id))
                    .placeholder(R.drawable.app_icon)
                    .error(R.drawable.app_icon)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(mCurrMusicAlbumImv);

            mCurrMusicTitleTv.setText(lastMusicTitle);
            mCurrMusicArtistTv.setText(lastMusicArtist);
            if (mMediaPlayer.isPlaying()) {
                mCurrMusicPlayOrPauseIBtn.setImageResource(R.drawable.ic_pause_circle_outline_black_24dp);
            } else {
                mCurrMusicPlayOrPauseIBtn.setImageResource(R.drawable.ic_play_circle_outline_black_24dp);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        getLastMusicInfos();
        Log.i("music", "onStart >>>>> get");
    }

    @Override
    protected void onResume() {
        super.onResume();
        getLastMusicInfos();
        Log.i("music", "onResume >>>>> get");
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveLastMusicInfos();
        Log.i("music", "onPause >>>>> save");
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveLastMusicInfos();
        Log.i("music", "onStop >>>>> save");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveLastMusicInfos();
        Log.i("music", "onDestroy >>>>> save");
    }
}
