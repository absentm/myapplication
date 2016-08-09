package com.example.dm.myapplication.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.dm.myapplication.R;
import com.example.dm.myapplication.customviews.ninegridview.NineGridView;
import com.example.dm.myapplication.customviews.ninegridview.NineGridViewAdapter;
import com.example.dm.myapplication.find.FindHowOldAct;


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
                Toast.makeText(getActivity(), "点击了第" + position + "个Item.", Toast.LENGTH_SHORT).show();
                switch (position) {
                    case 0:

                        break;
                    case 1:
                        startActivity(new Intent(getActivity(), FindHowOldAct.class));
                        break;
                    case 2:

                        break;
                    case 3:

                        break;
                    case 4:

                        break;

                }
            }
        });
    }

}
