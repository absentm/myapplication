package com.example.dm.myapplication.find;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.example.dm.myapplication.R;
import com.example.dm.myapplication.find.FindMeizhiImageAdapter.OnRecyclerViewItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * FindMeiziAty: 览图
 * link1: http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2015/0327/2647.html
 * link2: http://www.voidcn.com/blog/u012842664/article/p-4910468.html
 * Created by dm on 16-9-2.
 */
public class FindMeiziAty extends AppCompatActivity
        implements FindImageUrlLoader.Callback, OnRefreshListener, OnLoadMoreListener {
    private static final String TAG = "FindMeiziAty";
    private static final int DEFAULT_IMG_COUNT = 10;    //默认每页加载10张图
    private ImageButton titleImv;
    private FloatingActionButton fab;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private FindMeizhiImageAdapter mFindMeizhiImageAdapter;
    private FindImageUrlLoader imgUrlLoader;

    private SwipeToLoadLayout mSwipeToLoadLayout;

    private List<String> imgUrlList = new ArrayList<>();

    private int mImgCount = DEFAULT_IMG_COUNT;  //每页加载数
    private int nextPage = 1;
    private int[] lastPos = new int[2];

    private Animation animationIn;  //进入动画
    private Animation animationOut; //退出动画


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_picture_layout);

        initAnimation();
        initView();
        eventsDeal();

    }

    private void initAnimation() {
        animationIn = AnimationUtils.loadAnimation(this, R.anim.scale_fade_in);
        animationOut = AnimationUtils.loadAnimation(this, R.anim.scale_fade_out);
    }

    private void initView() {
        titleImv = (ImageButton) findViewById(R.id.title_imv);
        fab = (FloatingActionButton) findViewById(R.id.find_fab);
        mLayoutManager = new LinearLayoutManager(FindMeiziAty.this,
                LinearLayoutManager.VERTICAL, false);
        mRecyclerView = (RecyclerView) findViewById(R.id.swipe_target);
    }

    private void eventsDeal() {
        titleImv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FindMeiziAty.this.finish();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //返回顶部
                mLayoutManager.smoothScrollToPosition(mRecyclerView, null, 0);
            }
        });

        mRecyclerView.setLayoutManager(mLayoutManager);
//        mRecyclerView.setHasFixedSize(true);
        mFindMeizhiImageAdapter = new FindMeizhiImageAdapter(this, imgUrlList);
        //上拉加载更多
        mRecyclerView.setAdapter(mFindMeizhiImageAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //上滑比并且按钮不可见 显示按钮
                if (dy < 0 && fab.getVisibility() == View.GONE) {
                    fab.startAnimation(animationIn);
                    fab.setVisibility(View.VISIBLE);
                }
                //下滑并且按钮可见 隐藏按钮
                else if (dy > 0 && fab.getVisibility() == View.VISIBLE) {
                    fab.startAnimation(animationOut);
                    fab.setVisibility(View.GONE);
                }
            }
        });

        // item 点击事件
        mFindMeizhiImageAdapter.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, String data) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("photoUrl", data);
                intent.setClass(FindMeiziAty.this, FindPhotoDetailAty.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        imgUrlLoader = new FindImageUrlLoader(FindMeiziAty.this, mImgCount, FindMeiziAty.this);
        imgUrlLoader.loadImageUrl(nextPage);

    }

    @Override
    public void addData(List<String> imgDataList) {
        for (int i = 0; i < imgDataList.size(); i++) {
            imgUrlList.add(imgDataList.get(i));
        }

        mFindMeizhiImageAdapter.notifyDataSetChanged();
        nextPage++;

        mSwipeToLoadLayout.setRefreshing(false);
    }

    /**
     * 刷新并重置当前页数
     */
    @Override
    public void onRefresh() {
        nextPage = 1;//将页数重置为1
        imgUrlList.clear();//清空原来url数据
        imgUrlLoader.loadImageUrl(nextPage);//重置url数据
    }

    @Override
    public void onLoadMore() {
        mSwipeToLoadLayout.setRefreshing(true);
        imgUrlLoader.loadImageUrl(nextPage);
    }
}
