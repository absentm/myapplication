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

import com.afollestad.materialdialogs.MaterialDialog;
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

import java.util.List;

import cn.bmob.v3.BmobUser;


/**
 * Created by dm on 16-3-29.
 * 第三个页面
 */
public class ThirdFragment extends Fragment implements View.OnClickListener {
    private ImageButton titleSearchIbtn;
    private NineGridView nineGridView;
    private View view;
    private MaterialDialog mMaterialDialog;
    private List<String> mTitles;
    private List<String> mImages;

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
        titleSearchIbtn = (ImageButton) view.findViewById(R.id.title_find_search_ibtn);
        titleSearchIbtn.setOnClickListener(ThirdFragment.this);

        nineGridView = (NineGridView) view.findViewById(R.id.find_nine_gridview);
        nineGridView.setAdapter(new NineGridViewAdapter(getActivity()));

        Banner banner = (Banner) view.findViewById(R.id.banner);
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
        banner.setBannerAnimation(Transformer.DepthPage);
        banner.isAutoPlay(true);
        banner.setDelayTime(2000);
        banner.setIndicatorGravity(BannerConfig.CENTER);
        banner.setBannerTitles(mTitles);
        banner.setImageLoader(new GlideImageLoader());
        banner.setImages(mImages);
        banner.start();
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
}
