package com.example.dm.myapplication.com;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dm.myapplication.R;
import com.example.dm.myapplication.beans.AppUser;
import com.example.dm.myapplication.beans.ComUserPostInfo;
import com.example.dm.myapplication.utiltools.DateUtil;
import com.litao.android.lib.Utils.GridSpacingItemDecoration;
import com.litao.android.lib.entity.PhotoEntry;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadBatchListener;

/**
 * ComPostTopicActivity
 * Created by dm on 16-8-30.
 */
public class ComPostTopicActivity extends Activity implements ChooseAdapter.OnItmeClickListener {
    private Button titleLeftTv;
    private Button titleRightTv;
    private EditText mPostContentEt;

    private RecyclerView mRecyclerView;
    private ChooseAdapter mAdapter;

    private List<String> urlsList = new ArrayList<>();
    private int flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.com_post_content_layout);

        initBaseViews();

        EventBus.getDefault().register(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mAdapter = new ChooseAdapter(this);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(4, 4, true));
    }

    private void initBaseViews() {
        titleLeftTv = (Button) findViewById(R.id.title_tv);
        titleRightTv = (Button) findViewById(R.id.title_right_tv);
        mPostContentEt = (EditText) findViewById(R.id.com_post_comtent_et);

        // cancle button click
        titleLeftTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ComPostTopicActivity.this.finish();
            }
        });

        // post button click
        titleRightTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mPostContentEt.getText().toString().equals("") ||
                        !mAdapter.getData().isEmpty()) {
                    postInfos();
                } else {
                    Toast.makeText(ComPostTopicActivity.this,
                            "发表内容不能为空！",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * 发表新内容
     */
    private void postInfos() {
        // print local images' paths log, and save image paths
        List<String> stringArrayList = new ArrayList<>();
        for (PhotoEntry photoEntry : mAdapter.getData()) {
            Log.i("LOG", "photoEntry.getPath() >> " + photoEntry.getPath());
            stringArrayList.add(photoEntry.getPath());
        }

        if (!stringArrayList.isEmpty()) {
            // ArrayList<String> to String[], ensure upload to bmob
            String[] bombImagesPaths = new String[stringArrayList.size()];
            bombImagesPaths = stringArrayList.toArray(bombImagesPaths);

            // upload local image to bmob, return bmob image links
            final String[] finalBombImagesPaths = bombImagesPaths; // temp var
            BmobFile.uploadBatch(bombImagesPaths, new UploadBatchListener() {
                @Override
                public void onSuccess(List<BmobFile> files, List<String> urls) {
                    if (urls.size() == finalBombImagesPaths.length) {
                        Log.i("LOG", "URLS >>> " + urls.toString());
                        urlsList = urls;
                        Log.i("LOG", "urlsList in upLoad >>> " + urlsList.toString());

                        generatePostInfos();
                    }
                }

                @Override
                public void onProgress(int i, int i1, int i2, int i3) {
                }

                @Override
                public void onError(int statuscode, String errormsg) {
                    Log.i("LOG", "ERROR >>> statuscode: " + statuscode +
                            "errormsg: " + errormsg);
                    Toast.makeText(ComPostTopicActivity.this,
                            "ERROR >>> statuscode: " + statuscode +
                                    "errormsg: " + errormsg,
                            Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            generatePostInfos();
        }
    }

    /**
     * 生成新发表内容数据
     */
    private void generatePostInfos() {
        // post topic
        AppUser appUser = BmobUser.getCurrentUser(AppUser.class);
        String currentTime = DateUtil.getCurrentTimeStr();
        ComUserPostInfo comUserPostInfo = new ComUserPostInfo();

        if (appUser != null) {
            Log.i("LOG", "urlsList in if >>> " + urlsList.toString());

            comUserPostInfo.setUserNameStr(appUser.getUsername());
            comUserPostInfo.setUserHeadImgUrl(appUser.getUserAvatarUrl());
            comUserPostInfo.setUserNickNameStr(appUser.getUserNickName());
            comUserPostInfo.setUserTimeStr(currentTime);
            comUserPostInfo.setUserContentStr(mPostContentEt.getText().toString());
            comUserPostInfo.setUserImageUrlList(urlsList);
            comUserPostInfo.setUserRepostCounter(0);
            comUserPostInfo.setUserCommentCounter(0);
            comUserPostInfo.setUserLikeCounter(0);

            // sava all post contents
            comUserPostInfo.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    if (e == null) {
                        flag = 1;
                    } else {
                        Toast.makeText(ComPostTopicActivity.this,
                                "Error! " + e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(ComPostTopicActivity.this,
                    "Errors, please wait!",
                    Toast.LENGTH_SHORT).show();
        }

        if (1 == flag) {
            Intent intent = new Intent();
            intent.putExtra("newPostData", comUserPostInfo);
            ComPostTopicActivity.this.setResult(RESULT_OK, intent);

            Toast.makeText(ComPostTopicActivity.this,
                    "发表成功！",
                    Toast.LENGTH_SHORT).show();
            ComPostTopicActivity.this.finish();
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onItemClicked(int position) {
        if (position == mAdapter.getItemCount() - 1) {
            startActivity(new Intent(ComPostTopicActivity.this, ComPhotosSelectAty.class));
            EventBus.getDefault().postSticky(new EventEntry(mAdapter.getData(),
                    EventEntry.SELECTED_PHOTOS_ID));
        } else {
            Toast.makeText(ComPostTopicActivity.this, "pos: " + position, Toast.LENGTH_SHORT).show();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void photosMessageEvent(EventEntry entries) {
        if (entries.id == EventEntry.RECEIVED_PHOTOS_ID) {
            mAdapter.reloadList(entries.photos);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void photoMessageEvent(PhotoEntry entry) {
        mAdapter.appendPhoto(entry);
    }

}
