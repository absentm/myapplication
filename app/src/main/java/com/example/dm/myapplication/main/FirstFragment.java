package com.example.dm.myapplication.main;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.dm.myapplication.R;
import com.example.dm.myapplication.customviews.com.adapter.NewsAdapter;
import com.example.dm.myapplication.home.HomeLoadNewsTask;
import com.example.dm.myapplication.home.HomeNewsDetailActivity;
import com.example.dm.myapplication.utiltools.SystemUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;


/**
 * Created by dm on 16-3-29.
 * 第一个页面
 */
public class FirstFragment extends Fragment {
    private View view;
    private RelativeLayout mRelativeLayout;

    private ListView homeListView;
    private SwipeRefreshLayout refreshLayout;
    private NewsAdapter adapter;
    private boolean isConnected;

    private long[] mHits = new long[2];//存储时间的数组

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fg1, container, false);
        initViews();
        eventDeals();

        return view;
    }

    private void initViews() {
        isConnected = SystemUtils.checkNetworkConnection(getActivity());

        mRelativeLayout = (RelativeLayout) view.findViewById(R.id.home_title_rout);
        TextView titleTextTv = (TextView) view.findViewById(R.id.home_title_tv);
        ImageView titleRightImv = (ImageView) view.findViewById(R.id.title_right_imv);
        titleTextTv.setText(getTime());

        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.home_refreshLayout);
        refreshLayout.setColorSchemeResources(
                R.color.swipeRefreshLayout,
                R.color.swipeRefreshLayout,
                R.color.swipeRefreshLayout,
                R.color.swipeRefreshLayout);

        homeListView = (ListView) view.findViewById(R.id.home_listView);
        adapter = new NewsAdapter(getActivity(), R.layout.home_listview_item);
        homeListView.setAdapter(adapter);

        if (isConnected) new HomeLoadNewsTask(adapter).execute();
        else SystemUtils.noNetworkAlert(getActivity());

        // 双击事件，回顶部栏
        mRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //实现数组的移位操作，点击一次，左移一位，末尾补上当前开机时间（cpu的时间）
                System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
                mHits[mHits.length - 1] = SystemClock.uptimeMillis();

                //双击事件的时间间隔500ms
                if (mHits[0] >= (SystemClock.uptimeMillis() - 500)) {
                    Log.i("TAG", "I am here!");

//                    mScrollView.scrollTo(0, 0);  // 滑动置顶部
                    homeListView.setSelection(0);
                    homeListView.setSelectionAfterHeaderView();
                    homeListView.smoothScrollToPosition(0);
                }
            }
        });
    }

    private void eventDeals() {
        homeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HomeNewsDetailActivity.startActivity(getActivity(), adapter.getItem(position));
            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (isConnected) {
                    new HomeLoadNewsTask(adapter, new HomeLoadNewsTask.onFinishListener() {
                        @Override
                        public void afterTaskFinish() {
                            refreshLayout.setRefreshing(false);
//                Toast.makeText(MainActivity.this, "Refresh success", Toast.LENGTH_SHORT).show();
                        }
                    }).execute();
                } else {
                    SystemUtils.noNetworkAlert(getActivity());
                    refreshLayout.setRefreshing(false);
                }
            }
        });
    }

    public String getTime() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat(getString(R.string.date_format));
        return format.format(c.getTime());
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        isConnected = SystemUtils.checkNetworkConnection(getActivity());
        if (!isConnected) {
            refreshLayout.setRefreshing(false);
        }
    }

}
