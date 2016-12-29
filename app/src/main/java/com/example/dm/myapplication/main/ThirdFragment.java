package com.example.dm.myapplication.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.dm.myapplication.R;
import com.example.dm.myapplication.beans.AppUser;
import com.example.dm.myapplication.customviews.ninegridview.NineGridView;
import com.example.dm.myapplication.customviews.ninegridview.NineGridViewAdapter;
import com.example.dm.myapplication.find.FindGankAty;
import com.example.dm.myapplication.find.FindHowOldAct;
import com.example.dm.myapplication.find.FindIndexMusicAty;
import com.example.dm.myapplication.find.FindMapAroundAty;
import com.example.dm.myapplication.find.FindMeiziAty;
import com.example.dm.myapplication.find.FindNotesAty;
import com.example.dm.myapplication.find.FindSearchAty;
import com.example.dm.myapplication.find.FindVideoAty;
import com.example.dm.myapplication.find.FindWeatherAty;
import com.example.dm.myapplication.find.zxing.activity.CaptureActivity;
import com.example.dm.myapplication.utiltools.GlideImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerClickListener;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;


/**
 * Created by dm on 16-3-29.
 * 第三个页面
 */
public class ThirdFragment extends Fragment
        implements View.OnClickListener, OnBannerClickListener {

    private ImageButton titleSearchIbtn;
    private NineGridView nineGridView;
    private View view;
    private List<String> mTitles = new ArrayList<>();
    private List<Integer> mImages = new ArrayList<>();
    private Banner mBanner;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fg3, container, false);

        initView();     // 初始化界面控件
        dealEvents();   // 事件处理: gridview item的点击事件
        return view;
    }

    /**
     * 初始化界面控件
     */
    private void initView() {
        mImages.add(R.drawable.find_1);
        mImages.add(R.drawable.find_2);
        mImages.add(R.drawable.find_8);
        mImages.add(R.drawable.find_4);
        mImages.add(R.drawable.find_5);
        mImages.add(R.drawable.find_6);
        mImages.add(R.drawable.find_7);

        mTitles.add("既见公子，云胡不喜？");
        mTitles.add("我只想静静地，做个美男子。");
        mTitles.add("曾经的曾经，只剩期望...");
        mTitles.add("你那么美，爱我如何？");
        mTitles.add("夜，夜，夜...");
        mTitles.add("孩子，快点睡吧，明天还要抢票回家呢。");
        mTitles.add("天边美丽的火烧云啊，我的生活放荡，像条狗，像条流浪狗...");

        titleSearchIbtn = (ImageButton) view.findViewById(R.id.title_find_search_ibtn);
        titleSearchIbtn.setOnClickListener(ThirdFragment.this);

        nineGridView = (NineGridView) view.findViewById(R.id.find_nine_gridview);
        nineGridView.setAdapter(new NineGridViewAdapter(getActivity()));

        mBanner = (Banner) view.findViewById(R.id.banner);
        mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
        mBanner.setBannerAnimation(Transformer.Default);
        mBanner.isAutoPlay(true);
        mBanner.setDelayTime(3000);
        mBanner.setIndicatorGravity(BannerConfig.CENTER);
        mBanner.setBannerTitles(mTitles);
        mBanner.setImageLoader(new GlideImageLoader());
        mBanner.setImages(mImages);
        mBanner.start();
        mBanner.setOnBannerClickListener(this);
    }

    /**
     * 事件处理: gridview item的点击事件
     */
    private void dealEvents() {
        final AppUser currentAppUser = BmobUser.getCurrentUser(AppUser.class);

        nineGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        if (currentAppUser != null) {
                            startActivity(new Intent(getActivity(), FindNotesAty.class));
                        } else {
                            Toast.makeText(getActivity(),
                                    "请先登录！",
                                    Toast.LENGTH_SHORT).show();
                        }

                        break;
                    case 1:
                        startActivity(new Intent(getActivity(), FindHowOldAct.class));
                        break;
                    case 2:
                        startActivity(new Intent(getActivity(), FindGankAty.class));
                        break;
                    case 3:
                        startActivity(new Intent(getActivity(), FindMeiziAty.class));
                        break;
                    case 4:
                        startActivity(new Intent(getActivity(), CaptureActivity.class));
                        break;
                    case 5:
                        startActivity(new Intent(getActivity(), FindVideoAty.class));
                        break;
                    case 6:
                        startActivity(new Intent(getActivity(), FindWeatherAty.class));
                        break;
                    case 7:
                        startActivity(new Intent(getActivity(), FindMapAroundAty.class));
                        break;
                    case 8:
                        startActivity(new Intent(getActivity(), FindIndexMusicAty.class));
                        break;
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_find_search_ibtn:
                startActivity(new Intent(getActivity(), FindSearchAty.class));
                break;

        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mBanner.startAutoPlay();
    }

    @Override
    public void onStop() {
        super.onStop();
        mBanner.stopAutoPlay();
    }

    @Override
    public void OnBannerClick(int position) {
        switch (position) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
                Toast.makeText(getActivity(),
                        "别点了，我只想试试...",
                        Toast.LENGTH_SHORT).show();
                break;
        }

    }
}
