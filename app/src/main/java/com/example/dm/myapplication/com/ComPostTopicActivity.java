package com.example.dm.myapplication.com;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.dm.myapplication.R;
import com.example.dm.myapplication.beans.AppUser;
import com.example.dm.myapplication.beans.ComUserPostInfo;
import com.example.dm.myapplication.utiltools.DateUtil;
import com.example.dm.myapplication.utiltools.SystemUtils;
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

    private MaterialDialog mMaterialDialog;

    private RecyclerView mRecyclerView;
    private ChooseAdapter mAdapter;

    private List<String> urlsList = new ArrayList<>();

    private boolean isConnected;

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
        isConnected = SystemUtils.checkNetworkConnection(ComPostTopicActivity.this);

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
                    if (isConnected) {


                        mMaterialDialog = new MaterialDialog.Builder(ComPostTopicActivity.this)
                                .content("Please waiting...")
                                .progress(true, 0)
                                .progressIndeterminateStyle(false)
                                .show();
                        mMaterialDialog.setCancelable(false);

                        // print local images' paths log, and save image paths
                        List<String> stringArrayList = new ArrayList<>();
                        for (PhotoEntry photoEntry : mAdapter.getData()) {
                            stringArrayList.add(photoEntry.getPath());
                        }

                        if (!stringArrayList.isEmpty()) {
                            // ArrayList<String> to String[], ensure upload to bmob
                            String[] bombImagesPaths = new String[stringArrayList.size()];
                            bombImagesPaths = stringArrayList.toArray(bombImagesPaths);

                            // upload local image to bmob, return bmob image links
                            final String[] finalBombImagesPaths = bombImagesPaths; // temp var
                            BmobFile.uploadBatch(finalBombImagesPaths, new UploadBatchListener() {
                                @Override
                                public void onSuccess(List<BmobFile> files, List<String> urls) {
                                    if (urls.size() == finalBombImagesPaths.length) {
                                        urlsList = urls;
                                        // post topic
                                        AppUser appUser = BmobUser.getCurrentUser(AppUser.class);
                                        String currentTime = DateUtil.getCurrentTimeStr();
                                        long currentTimeMills = DateUtil.getCurrentTimeMills();
                                        final ComUserPostInfo comUserPostInfo = new ComUserPostInfo();

                                        if (appUser != null) {
                                            comUserPostInfo.setUserNameStr(appUser.getUsername());
                                            comUserPostInfo.setUserHeadImgUrl(appUser.getUserAvatarUrl());
                                            comUserPostInfo.setUserNickNameStr(appUser.getUserNickName());
                                            comUserPostInfo.setUserTimeStr(currentTime);
                                            comUserPostInfo.setUserTimeMills(currentTimeMills);
                                            comUserPostInfo.setUserContentStr(mPostContentEt.getText().toString());
                                            comUserPostInfo.setUserImageUrlList(urlsList);
                                            comUserPostInfo.setUserRepostCounter(0);
                                            comUserPostInfo.setUserCommentCounter(0);
                                            comUserPostInfo.setUserLikeCounter(0);

                                            new Thread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    comUserPostInfo.save(new SaveListener<String>() {
                                                        @Override
                                                        public void done(String s, BmobException e) {
                                                            if (e == null) {
                                                                mMaterialDialog.dismiss();
                                                                Intent intent = new Intent();
                                                                intent.putExtra("newPostData", comUserPostInfo);
                                                                ComPostTopicActivity.this.setResult(RESULT_OK, intent);
                                                                ComPostTopicActivity.this.finish();
                                                            } else {
                                                                mMaterialDialog.dismiss();
                                                                Toast.makeText(ComPostTopicActivity.this,
                                                                        "Error! " + e.getMessage(),
                                                                        Toast.LENGTH_LONG).show();
                                                            }
                                                        }
                                                    });
                                                }
                                            }).start();
                                        } else {
                                            mMaterialDialog.dismiss();
                                            Toast.makeText(ComPostTopicActivity.this,
                                                    "Errors, please wait1!",
                                                    Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }

                                @Override
                                public void onProgress(int curIndex, int curPercent, int total, int totalPercent) {
                                }

                                @Override
                                public void onError(int statuscode, String errormsg) {
                                    mMaterialDialog.dismiss();
                                    Toast.makeText(ComPostTopicActivity.this,
                                            "ERROR >>> statuscode: " + statuscode +
                                                    "errormsg: " + errormsg,
                                            Toast.LENGTH_LONG).show();
                                }
                            });
                        } else {
                            // post topic
                            AppUser appUser = BmobUser.getCurrentUser(AppUser.class);
                            String currentTime = DateUtil.getCurrentTimeStr();
                            long currentTimeMills = DateUtil.getCurrentTimeMills();
                            final ComUserPostInfo comUserPostInfo = new ComUserPostInfo();

                            if (appUser != null) {
                                comUserPostInfo.setUserNameStr(appUser.getUsername());
                                comUserPostInfo.setUserHeadImgUrl(appUser.getUserAvatarUrl());
                                comUserPostInfo.setUserNickNameStr(appUser.getUserNickName());
                                comUserPostInfo.setUserTimeStr(currentTime);
                                comUserPostInfo.setUserTimeMills(currentTimeMills);
                                comUserPostInfo.setUserContentStr(mPostContentEt.getText().toString());
                                comUserPostInfo.setUserImageUrlList(urlsList);
                                comUserPostInfo.setUserRepostCounter(0);
                                comUserPostInfo.setUserCommentCounter(0);
                                comUserPostInfo.setUserLikeCounter(0);

                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        comUserPostInfo.save(new SaveListener<String>() {
                                            @Override
                                            public void done(String s, BmobException e) {
                                                if (e == null) {
                                                    mMaterialDialog.dismiss();
                                                    Intent intent = new Intent();
                                                    intent.putExtra("newPostData", comUserPostInfo);
                                                    ComPostTopicActivity.this.setResult(RESULT_OK, intent);
                                                    ComPostTopicActivity.this.finish();
                                                } else {
                                                    mMaterialDialog.dismiss();
                                                    Toast.makeText(ComPostTopicActivity.this,
                                                            "Error! " + e.getMessage(),
                                                            Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });
                                    }
                                }).start();
                            } else {
                                mMaterialDialog.dismiss();
                                Toast.makeText(ComPostTopicActivity.this,
                                        "Errors, please wait1!",
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    } else {
                        SystemUtils.noNetworkAlert(ComPostTopicActivity.this);
                    }
                } else {
                    Toast.makeText(ComPostTopicActivity.this,
                            "发表内容不能为空！",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
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
