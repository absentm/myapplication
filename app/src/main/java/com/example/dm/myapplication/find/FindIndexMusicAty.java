package com.example.dm.myapplication.find;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.dm.myapplication.R;
import com.example.dm.myapplication.beans.MusicEntity;
import com.example.dm.myapplication.customviews.MarqueeTextView;
import com.example.dm.myapplication.utiltools.FileUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.yokeyword.indexablerv.IndexableAdapter;
import me.yokeyword.indexablerv.IndexableLayout;

import static com.example.dm.myapplication.R.id.find_music_next_ibtn;
import static com.example.dm.myapplication.find.FindIndexMusicAdapter.formatTime;
import static com.example.dm.myapplication.find.FindMusicPlayService.mMediaPlayer;
import static com.example.dm.myapplication.utiltools.StringUtils.generateFileSize;

/**
 * FindIndexMusicAty
 * Created by dm on 16-11-2.
 */
public class FindIndexMusicAty extends Activity implements View.OnClickListener,
        IndexableAdapter.OnItemContentClickListener<MusicEntity>,
        SeekBar.OnSeekBarChangeListener {

    private List<FindIndexMusicAdapter.ContentVH> listViewHolder = new ArrayList<>();

    private ImageButton titleBackIBtn;
    private ProgressBar mProgressBar;

    private ImageView mCurrMusicAlbumImv;
    private MarqueeTextView mCurrMusicTitleTv;
    private TextView mCurrMusicArtistTv;
    private ImageButton mCurrMusicPlayOrPauseIBtn;
    private ImageButton mCurrMusicDetailIBtn;
    private ImageButton mCurrMusicNextIBtn;
    private SeekBar mSeekBar;

    private IndexableLayout mIndexableLayout;
    private FindIndexMusicAdapter mFindIndexMusicAdapter;
    private List<MusicEntity> mDatas;

    private FindMusicPlayService musicService;

    public static long lastMusicId;
    public static String lastMusicUrl;
    public static String lastMusicAlbum;
    public static String lastMusicTitle;
    public static String lastMusicArtist;
    public static long lastMusicAlbum_id;
    public static long lastMusicSize;
    public static long lastMusicDurition;
    public static int position = 0;

    private boolean isFirstPlay;
    private boolean isPause;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_index_music_lay);

        initView();
        setUpListener();
    }

    private void initView() {
        mDatas = FileUtil.getLocalIndexMusics(FindIndexMusicAty.this.getContentResolver());

        titleBackIBtn = (ImageButton) findViewById(R.id.title_music_left_imv);
        mProgressBar = (ProgressBar) findViewById(R.id.find_index_music_pbar);

        mCurrMusicAlbumImv = (ImageView) findViewById(R.id.find_music_album_imv);
        mCurrMusicTitleTv = (MarqueeTextView) findViewById(R.id.item_music_title_tv);
        mCurrMusicTitleTv.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        mCurrMusicTitleTv.setSingleLine(true);
        mCurrMusicArtistTv = (TextView) findViewById(R.id.item_music_artist_tv);
        mCurrMusicPlayOrPauseIBtn = (ImageButton) findViewById(R.id.find_music_play_ibtn);
        mCurrMusicDetailIBtn = (ImageButton) findViewById(R.id.find_music_detail_ibtn);
        mCurrMusicNextIBtn = (ImageButton) findViewById(find_music_next_ibtn);

        mSeekBar = (SeekBar) findViewById(R.id.find_music_seekbar);
        mSeekBar.setProgress(mMediaPlayer.getCurrentPosition());
        mSeekBar.setMax(mMediaPlayer.getDuration());

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

        initFirstRunEvent();
    }

    private void setUpListener() {
        titleBackIBtn.setOnClickListener(FindIndexMusicAty.this);
        mCurrMusicPlayOrPauseIBtn.setOnClickListener(FindIndexMusicAty.this);
        mCurrMusicDetailIBtn.setOnClickListener(FindIndexMusicAty.this);
        mCurrMusicNextIBtn.setOnClickListener(FindIndexMusicAty.this);
        mFindIndexMusicAdapter.setOnItemContentClickListener(FindIndexMusicAty.this);
    }

    private void initFirstRunEvent() {
        SharedPreferences sharedPreferences =
                FindIndexMusicAty.this.getSharedPreferences("shareInfo", MODE_PRIVATE);
        isFirstPlay = sharedPreferences.getBoolean("isFirstRun", true);

        if (mDatas.isEmpty()) {
            Toast.makeText(FindIndexMusicAty.this,
                    "本地暂无音乐，先去下载吧！", Toast.LENGTH_SHORT).show();
        } else {
            if (isFirstPlay) {   // 第一次播放初始化
                Glide.with(FindIndexMusicAty.this)
                        .load(getCoverUri(FindIndexMusicAty.this, mDatas.get(0).getAlbum_id()))
                        .placeholder(R.drawable.app_icon)
                        .error(R.drawable.app_icon)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(mCurrMusicAlbumImv);
                mCurrMusicTitleTv.setText(mDatas.get(0).getTitle());
                mCurrMusicArtistTv.setText(mDatas.get(0).getArtist());
            } else {
                getLastMusicInfos();
                // 更新当前改变样式的Item, 防止点选item状态被复用回收，标记并重置为正常
                for (int i = 0; i < mDatas.size(); i++) {
                    mDatas.get(i).setSelected(false);
                }
                mDatas.get(position).setSelected(true);
                mFindIndexMusicAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_music_left_imv:
                FindIndexMusicAty.this.finish();
                break;
            case R.id.find_music_play_ibtn:
                if (isFirstPlay) {
                    // 更新当前改变样式的Item, 防止点选item状态被复用回收，标记并重置为正常
                    for (int i = 0; i < mDatas.size(); i++) {
                        mDatas.get(i).setSelected(false);
                    }
                    mDatas.get(0).setSelected(true);
                    mFindIndexMusicAdapter.notifyDataSetChanged();

                    Intent intent = new Intent();
                    intent.putExtra("url", mDatas.get(0).getUrl());
                    intent.putExtra("title", mDatas.get(0).getTitle());
                    intent.putExtra("artist", mDatas.get(0).getArtist());
                    intent.putExtra("album", mDatas.get(0).getAlbum());
                    intent.putExtra("album_id", mDatas.get(0).getAlbum_id());
                    intent.setClass(FindIndexMusicAty.this, FindMusicPlayService.class);
                    startService(intent);
                    bindService(intent, sc, BIND_AUTO_CREATE);

                    mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mediaPlayer) {
                            playNextMusic();
                        }
                    });

                    mCurrMusicPlayOrPauseIBtn.setImageResource(
                            R.drawable.ic_pause_circle_outline_black_24dp);

                    // 保存第一次播放信息
                    lastMusicId = mDatas.get(0).getId();
                    lastMusicTitle = mDatas.get(0).getTitle();
                    lastMusicArtist = mDatas.get(0).getArtist();
                    lastMusicAlbum_id = mDatas.get(0).getAlbum_id();
                    lastMusicUrl = mDatas.get(0).getUrl();
                    lastMusicAlbum = mDatas.get(0).getAlbum();
                    lastMusicSize = mDatas.get(0).getSize();
                    lastMusicDurition = mDatas.get(0).getDuration();

                    saveLastMusicInfos();
                    setFirstPlayFalse();
                    isFirstPlay = false;
                    position = 0;
                } else {
                    if (mMediaPlayer.isPlaying()) {
                        mMediaPlayer.pause();
                        mCurrMusicPlayOrPauseIBtn
                                .setImageResource(R.drawable.ic_play_circle_outline_black_24dp);
                        isPause = true;
                    } else if (isPause) {
                        mMediaPlayer.start();
                        mCurrMusicPlayOrPauseIBtn
                                .setImageResource(R.drawable.ic_pause_circle_outline_black_24dp);
                        isPause = false;
                    } else {
                        // 更新当前改变样式的Item, 防止点选item状态被复用回收，标记并重置为正常
                        for (int i = 0; i < mDatas.size(); i++) {
                            mDatas.get(i).setSelected(false);
                            if (mDatas.get(i).getId() == lastMusicId) {
                                mDatas.get(i).setSelected(true);
                            }
                        }
                        mFindIndexMusicAdapter.notifyDataSetChanged();

                        Intent intent = new Intent();
                        intent.putExtra("url", lastMusicUrl);
                        intent.putExtra("title", lastMusicTitle);
                        intent.putExtra("artist", lastMusicArtist);
                        intent.putExtra("album", lastMusicAlbum);
                        intent.putExtra("album_id", lastMusicAlbum_id);
                        intent.setClass(FindIndexMusicAty.this, FindMusicPlayService.class);
                        startService(intent);
                        bindService(intent, sc, BIND_AUTO_CREATE);
                        mCurrMusicPlayOrPauseIBtn
                                .setImageResource(R.drawable.ic_pause_circle_outline_black_24dp);

                        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mediaPlayer) {
                                playNextMusic();
                            }
                        });
                    }
                }
                break;
            case R.id.find_music_detail_ibtn:
                getCurrentMusicInfos();
                break;
            case find_music_next_ibtn:
                playNextMusic();
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
        // 更新当前改变样式的Item, 防止点选item状态被复用回收，标记并重置为正常
        for (int i = 0; i < mDatas.size(); i++) {
            mDatas.get(i).setSelected(false);
        }
        entity.setSelected(true);
        mFindIndexMusicAdapter.notifyDataSetChanged();

        // 更新数据
        lastMusicId = entity.getId();
        lastMusicTitle = entity.getTitle();
        lastMusicArtist = entity.getArtist();
        lastMusicAlbum_id = entity.getAlbum_id();
        lastMusicUrl = entity.getUrl();
        lastMusicAlbum = entity.getAlbum();
        lastMusicSize = entity.getSize();
        lastMusicDurition = entity.getDuration();

        // 更新状态栏
        Glide.with(FindIndexMusicAty.this)
                .load(getCoverUri(FindIndexMusicAty.this, lastMusicAlbum_id))
                .placeholder(R.drawable.app_icon)
                .error(R.drawable.app_icon)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(mCurrMusicAlbumImv);
        mCurrMusicTitleTv.setText(lastMusicTitle);
        mCurrMusicArtistTv.setText(lastMusicArtist);
        mCurrMusicPlayOrPauseIBtn.setImageResource(R.drawable.ic_pause_circle_outline_black_24dp);

        // 保存点击item后的音乐状态信息
        position = originalPosition;
        saveLastMusicInfos();
        setFirstPlayFalse();

        // 启动音乐播放
        Intent intent = new Intent();
        intent.putExtra("url", entity.getUrl());
        intent.putExtra("title", entity.getTitle());
        intent.putExtra("artist", entity.getArtist());
        intent.putExtra("album", entity.getAlbum());
        intent.putExtra("album_id", entity.getAlbum_id());
        intent.setClass(FindIndexMusicAty.this, FindMusicPlayService.class);
        startService(intent);
        bindService(intent, sc, BIND_AUTO_CREATE);

        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                playNextMusic();
            }
        });
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

    /**
     * 保存最后一次播放记录
     */
    private void saveLastMusicInfos() {
        SharedPreferences sharedPreferences = getSharedPreferences("lastMusicInfos", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong("lastMusicId", lastMusicId);
        editor.putString("lastMusicUrl", lastMusicUrl);
        editor.putString("lastMusicAlbum", lastMusicAlbum);
        editor.putString("lastMusicTitle", lastMusicTitle);
        editor.putString("lastMusicArtist", lastMusicArtist);
        editor.putLong("lastMusicAlbum_id", lastMusicAlbum_id);
        editor.putLong("lastMusicSize", lastMusicSize);
        editor.putLong("lastMusicDurition", lastMusicDurition);
        editor.putInt("position", position);
        editor.apply();

        Log.i("music", "currMusicTitle: " + lastMusicTitle);
    }

    /**
     * 获取最后一次播放记录
     */
    private void getLastMusicInfos() {
        SharedPreferences sharedPreferences = getSharedPreferences("lastMusicInfos", MODE_PRIVATE);
        lastMusicId = sharedPreferences.getLong("lastMusicId", mDatas.get(0).getId());
        lastMusicTitle = sharedPreferences.getString("lastMusicTitle", mDatas.get(0).getTitle());
        lastMusicArtist = sharedPreferences.getString("lastMusicArtist", mDatas.get(0).getArtist());
        lastMusicAlbum_id = sharedPreferences.getLong("lastMusicAlbum_id", mDatas.get(0).getAlbum_id());
        lastMusicUrl = sharedPreferences.getString("lastMusicUrl", mDatas.get(0).getUrl());
        lastMusicAlbum = sharedPreferences.getString("lastMusicAlbum", mDatas.get(0).getAlbum());
        position = sharedPreferences.getInt("position", 0);

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

    /**
     * 第一次运行状态记录
     */
    private void setFirstPlayFalse() {
        SharedPreferences sharedPreferences = getSharedPreferences("shareInfo", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isFirstRun", false);
        editor.apply();
    }

    /**
     * 获取当前状态栏上的音乐详情
     */
    private void getCurrentMusicInfos() {
        SharedPreferences sharedPreferences = getSharedPreferences("lastMusicInfos", MODE_PRIVATE);
        lastMusicId = sharedPreferences.getLong("lastMusicId", mDatas.get(0).getId());
        lastMusicTitle = sharedPreferences.getString("lastMusicTitle", mDatas.get(0).getTitle());
        lastMusicArtist = sharedPreferences.getString("lastMusicArtist", mDatas.get(0).getArtist());
        lastMusicAlbum_id = sharedPreferences.getLong("lastMusicAlbum_id", mDatas.get(0).getAlbum_id());
        lastMusicUrl = sharedPreferences.getString("lastMusicUrl", mDatas.get(0).getUrl());
        lastMusicAlbum = sharedPreferences.getString("lastMusicAlbum", mDatas.get(0).getAlbum());
        lastMusicSize = sharedPreferences.getLong("lastMusicSize", mDatas.get(0).getSize());
        lastMusicDurition = sharedPreferences.getLong("lastMusicDurition", mDatas.get(0).getDuration());

        new MaterialDialog.Builder(FindIndexMusicAty.this)
                .title(lastMusicTitle)
                .content("歌手： " + lastMusicArtist + "\n\n"
                        + "专辑： " + lastMusicAlbum + "\n\n"
                        + "时长： " + formatTime(lastMusicDurition) + "\n\n"
                        + "大小： " + generateFileSize(lastMusicSize))
                .show();
    }

    /**
     * 播放下一曲
     */
    private void playNextMusic() {
        if (position == mDatas.size()) {
            position = 0;
        } else {
            ++position;
        }

        // 保存一下当前播放的音乐信息
        lastMusicId = mDatas.get(position).getId();
        lastMusicUrl = mDatas.get(position).getUrl();
        lastMusicAlbum = mDatas.get(position).getAlbum();
        lastMusicTitle = mDatas.get(position).getTitle();
        lastMusicArtist = mDatas.get(position).getArtist();
        lastMusicAlbum_id = mDatas.get(position).getAlbum_id();
        lastMusicSize = mDatas.get(position).getSize();
        lastMusicDurition = mDatas.get(position).getDuration();
        saveLastMusicInfos();

        // 更新列表被点选item的样式，防止点选item状态被复用回收，标记并重置为正常, 更新状态栏
        for (int i = 0; i < mDatas.size(); i++) {
            mDatas.get(i).setSelected(false);
        }
        mDatas.get(position).setSelected(true);
        mFindIndexMusicAdapter.notifyDataSetChanged();
//        mIndexableLayout.getRecyclerView().scrollToPosition(position);

        // 播放
        try {
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(mDatas.get(position).getUrl());
            mMediaPlayer.prepare();

            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.pause();
            } else {
                mMediaPlayer.start();
            }

            // 取数据更新一下当前的播放状态栏
            getLastMusicInfos();
        } catch (IOException e) {
            Log.i("music", e.getMessage());
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        getLastMusicInfos();
        // 更新当前改变样式的Item, 防止点选item状态被复用回收，标记并重置为正常
        for (int i = 0; i < mDatas.size(); i++) {
            mDatas.get(i).setSelected(false);
        }
        mDatas.get(position).setSelected(true);
        mFindIndexMusicAdapter.notifyDataSetChanged();
        Log.i("music", "onStart >>>>> get");
    }

    @Override
    protected void onResume() {
        super.onResume();
        getLastMusicInfos();
        // 更新当前改变样式的Item, 防止点选item状态被复用回收，标记并重置为正常
        for (int i = 0; i < mDatas.size(); i++) {
            mDatas.get(i).setSelected(false);
        }
        mDatas.get(position).setSelected(true);
        mFindIndexMusicAdapter.notifyDataSetChanged();
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
