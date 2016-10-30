package com.example.dm.myapplication.find;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.dm.myapplication.R;
import com.example.dm.myapplication.beans.VideoBeans.VideoResultsBean;
import com.example.dm.myapplication.beans.VideoBeans.VideosBean;
import com.example.dm.myapplication.utiltools.HttpUtil;
import com.example.dm.myapplication.utiltools.SystemUtils;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * FindVideoAty
 * Created by dm on 16-10-29.
 */
public class FindVideoAty extends Activity implements
        FindVideoAdapter.OnVideoItemClickListener,
        View.OnClickListener {
    private ImageButton titleBackImv;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<VideoResultsBean> mDatas;
    private FindVideoAdapter mFindVideoAdapter;

    private boolean isConnect;
    private VideosBean mVideosBean;
    private MaterialDialog mMaterialDialog;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String videoJsonStr = msg.obj.toString();
            Log.d("FindVideoAty", " Json: " + videoJsonStr);

            Gson gson = new Gson();
            mVideosBean = gson.fromJson(videoJsonStr, VideosBean.class);
            if (!mVideosBean.getResults().isEmpty() && mVideosBean != null) {
                mDatas = mVideosBean.getResults();

                mFindVideoAdapter = new FindVideoAdapter(FindVideoAty.this, mDatas);
                mLayoutManager = new LinearLayoutManager(FindVideoAty.this,
                        LinearLayoutManager.VERTICAL, false);
                mRecyclerView.setAdapter(mFindVideoAdapter);
                mRecyclerView.setLayoutManager(mLayoutManager);
                mRecyclerView.setItemAnimator(new DefaultItemAnimator());//默认动画
                mRecyclerView.setHasFixedSize(true);//效率最高
                mFindVideoAdapter.setOnItemClickListener(FindVideoAty.this);

                mMaterialDialog.dismiss();
            } else {
                mMaterialDialog.dismiss();
                Toast.makeText(FindVideoAty.this,
                        "数据加载出错！",
                        Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_video_lay);

        initView();
    }

    private void initView() {
        isConnect = SystemUtils.checkNetworkConnection(FindVideoAty.this);
        titleBackImv = (ImageButton) findViewById(R.id.title_video_left_imv);
        mRecyclerView = (RecyclerView) findViewById(R.id.find_videos_recyclerview);
        titleBackImv.setOnClickListener(FindVideoAty.this);

        if (isConnect) {
            mMaterialDialog = new MaterialDialog.Builder(FindVideoAty.this)
                    .content("Please waiting...")
                    .progress(true, 0)
                    .progressIndeterminateStyle(false)
                    .cancelable(false)
                    .show();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String videoDatas = HttpUtil.getVideoJsonStr();
                        if (!videoDatas.equals("")) {
                            Message message = mHandler.obtainMessage();
                            message.obj = videoDatas;
                            mHandler.sendMessage(message);
                        }
                    } catch (UnsupportedEncodingException e) {
                        Log.d("FindVideoAty", e.getMessage());
                    }
                }
            }).start();
        } else {
            SystemUtils.noNetworkAlert(FindVideoAty.this);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_video_left_imv:
                FindVideoAty.this.finish();
                break;
        }
    }

    @Override
    public void onVideoItemClick(View view, VideoResultsBean data) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable("videoItemInfos", data);
        intent.setClass(FindVideoAty.this, FindVideoDetailAty.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
