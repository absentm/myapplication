package com.example.dm.myapplication.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.example.dm.myapplication.R;
import com.example.dm.myapplication.customviews.ninegridview.NineGridView;
import com.example.dm.myapplication.customviews.ninegridview.NineGridViewAdapter;
import com.example.dm.myapplication.find.FindHowOldAct;
import com.example.dm.myapplication.find.FindMapAroundAty;
import com.example.dm.myapplication.find.FindMeiziAty;
import com.example.dm.myapplication.find.FindNotesAty;
import com.example.dm.myapplication.find.FindWeatherAty;
import com.example.dm.myapplication.find.zxing.activity.CaptureActivity;


/**
 * Created by dm on 16-3-29.
 * 第三个页面
 */
public class ThirdFragment extends Fragment {
    private NineGridView nineGridView;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fg3, container, false);

        initView();     // 初始化界面控件
        dealEvents();   // 事件处理: gridview item的点击事件
        return view;
    }

    /**
     * 初始化界面控件
     */
    private void initView() {
        nineGridView = (NineGridView) view.findViewById(R.id.find_nine_gridview);
        nineGridView.setAdapter(new NineGridViewAdapter(getActivity()));

    }

    /**
     * 事件处理: gridview item的点击事件
     */
    private void dealEvents() {
        nineGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        startActivity(new Intent(getActivity(), FindNotesAty.class));
                        break;
                    case 1:
                        startActivity(new Intent(getActivity(), FindHowOldAct.class));
                        break;
                    case 2:

                        break;
                    case 3:
                        startActivity(new Intent(getActivity(), FindMeiziAty.class));
                        break;
                    case 4:
                        startActivity(new Intent(getActivity(), CaptureActivity.class));
                        break;
                    case 5:
                        break;
                    case 6:
                        startActivity(new Intent(getActivity(), FindWeatherAty.class));
                        break;
                    case 7:
                        startActivity(new Intent(getActivity(), FindMapAroundAty.class));
                        break;
                    case 8:
                        break;

                }
            }
        });
    }

}
