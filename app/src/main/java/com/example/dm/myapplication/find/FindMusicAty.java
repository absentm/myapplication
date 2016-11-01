package com.example.dm.myapplication.find;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.dm.myapplication.R;
import com.example.dm.myapplication.beans.MusicBean;
import com.example.dm.myapplication.utiltools.FileUtil;

import java.util.List;

/**
 * FindMusicAty
 * Created by dm on 16-11-1.
 */
public class FindMusicAty extends Activity implements FindMusicAdapter.OnMusicItemClickListener,
        View.OnClickListener {

    private ImageButton titleBackIBtn;
    private ImageView mMusicPlayingImv;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<MusicBean> mDatas;
    private FindMusicAdapter mFindMusicAdapter;

    private MusicBean mMusicBean;
    private MaterialDialog mMaterialDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_music_lay);

        initView();
        setUpListener();
    }

    private void initView() {
        titleBackIBtn = (ImageButton) findViewById(R.id.title_music_left_imv);
        mMusicPlayingImv = (ImageView) findViewById(R.id.item_music_playing_imv);
        mRecyclerView = (RecyclerView) findViewById(R.id.find_musics_recyclerview);
        mDatas = FileUtil.getLocalMusics(FindMusicAty.this.getContentResolver());

        mFindMusicAdapter = new FindMusicAdapter(FindMusicAty.this, mDatas);
        mLayoutManager = new LinearLayoutManager(FindMusicAty.this,
                LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setAdapter(mFindMusicAdapter);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());//默认动画
        mRecyclerView.setHasFixedSize(true);//效率最高

    }

    private void setUpListener() {
        titleBackIBtn.setOnClickListener(FindMusicAty.this);
        mFindMusicAdapter.setOnItemClickListener(FindMusicAty.this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_music_left_imv:
                FindMusicAty.this.finish();
                break;
        }
    }

    @Override
    public void onMusicItemClick(View view, MusicBean data) {
        mMusicPlayingImv.setVisibility(View.VISIBLE);
        Toast.makeText(FindMusicAty.this,
                data.getTitle() + " " + data.getArtist(),
                Toast.LENGTH_SHORT).show();

    }
}
