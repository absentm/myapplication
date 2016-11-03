package com.example.dm.myapplication.find;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.dm.myapplication.R;
import com.example.dm.myapplication.beans.MusicEntity;
import com.example.dm.myapplication.utiltools.FileUtil;

import java.util.List;

import me.yokeyword.indexablerv.IndexableAdapter;
import me.yokeyword.indexablerv.IndexableLayout;

/**
 * FindIndexMusicAty
 * Created by dm on 16-11-2.
 */
public class FindIndexMusicAty extends Activity implements View.OnClickListener,
        IndexableAdapter.OnItemContentClickListener<MusicEntity>,
        IndexableAdapter.OnItemTitleClickListener {
    private ImageButton titleBackIBtn;
    private ProgressBar mProgressBar;

    private IndexableLayout mIndexableLayout;
    private FindIndexMusicAdapter mFindIndexMusicAdapter;
    private List<MusicEntity> mDatas;

    private FindMusicPlayService musicService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_index_music_lay);
        mDatas = FileUtil.getLocalIndexMusics(FindIndexMusicAty.this.getContentResolver());

        initView();
        setUpListener();
        eventDeal();
    }

    private void initView() {
        titleBackIBtn = (ImageButton) findViewById(R.id.title_music_left_imv);
        mProgressBar = (ProgressBar) findViewById(R.id.find_index_music_pbar);
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
//        mIndexableLayout.setOverlayStyle_MaterialDesign(R.color.colorAccent);
        // 快速排序。  排序规则设置为：只按首字母  （默认全拼音排序）  效率很高，是默认的10倍左右。  按需开启～
        mIndexableLayout.setFastCompare(true);

    }

    private void setUpListener() {
        titleBackIBtn.setOnClickListener(FindIndexMusicAty.this);
        mFindIndexMusicAdapter.setOnItemContentClickListener(FindIndexMusicAty.this);
        mFindIndexMusicAdapter.setOnItemTitleClickListener(FindIndexMusicAty.this);
    }

    private void eventDeal() {

//        // set Listener
//        mFindIndexMusicAdapter.setOnItemContentClickListener(new IndexableAdapter.OnItemContentClickListener<MusicEntity>() {
//            @Override
//            public void onItemClick(View v, int originalPosition, int currentPosition, MusicEntity entity) {
//                if (originalPosition >= 0) {
//                    Toast.makeText(FindIndexMusicAty.this, "选中:" +
//                            entity.getTitle() + "  当前位置:" + currentPosition +
//                            "  原始所在数组位置:" + originalPosition, Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(FindIndexMusicAty.this,
//                            "选中Header:" + entity.getTitle() + "  当前位置:" + currentPosition, Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//
//        mFindIndexMusicAdapter.setOnItemTitleClickListener(new IndexableAdapter.OnItemTitleClickListener() {
//            @Override
//            public void onItemClick(View v, int currentPosition, String indexTitle) {
//                Toast.makeText(FindIndexMusicAty.this,
//                        "选中:" + indexTitle + "  当前位置:" + currentPosition,
//                        Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        // 添加 HeaderView DefaultHeaderAdapter接收一个IndexableAdapter, 使其布局以及点击事件和IndexableAdapter一致
//        // 如果想自定义布局,点击事件, 可传入 new IndexableHeaderAdapter


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_music_left_imv:
                FindIndexMusicAty.this.finish();
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
            Toast.makeText(FindIndexMusicAty.this, "选中:" +
                    entity.getTitle() + "  当前位置:" + currentPosition +
                    "  原始所在数组位置:" + originalPosition, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.putExtra("url", entity.getUrl());
            intent.putExtra("title", entity.getTitle());
            intent.putExtra("artist", entity.getArtist());
            intent.putExtra("album", entity.getAlbum());
            intent.putExtra("album_id", entity.getAlbum_id());
            intent.setClass(FindIndexMusicAty.this, FindMusicPlayService.class);
            startService(intent);
            bindService(intent, sc, BIND_AUTO_CREATE);
        } else {
            Toast.makeText(FindIndexMusicAty.this,
                    "选中Header:" + entity.getTitle() + "  当前位置:" + currentPosition, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * item 标题点击事件
     *
     * @param v
     * @param currentPosition
     * @param indexTitle
     */
    @Override
    public void onItemClick(View v, int currentPosition, String indexTitle) {
        Toast.makeText(FindIndexMusicAty.this,
                "选中:" + indexTitle + "  当前位置:" + currentPosition,
                Toast.LENGTH_SHORT).show();
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
}
