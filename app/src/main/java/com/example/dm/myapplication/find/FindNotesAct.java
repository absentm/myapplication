package com.example.dm.myapplication.find;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.dm.myapplication.R;
import com.example.dm.myapplication.beans.NotesBean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * FindNotesAct
 * Created by dm on 16-9-23.
 */
public class FindNotesAct extends Activity {
    private static final String TAG = "FindNotesAct";
    private static final int REQUEST_CODE_ADD_1 = 1;
    private ImageButton titleBackImv;
    private ImageButton mAddNoteIbtn;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private List<NotesBean> mDatas = new ArrayList<>();
    private FindNotesAdapter mFindNotesAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_notes_layout);

        initView();
        fillDatas();
        eventDeals();
    }

    private void initView() {
        titleBackImv = (ImageButton) findViewById(R.id.notes_back_imv);
        mAddNoteIbtn = (ImageButton) findViewById(R.id.title_add_ibtn);

        mRecyclerView = (RecyclerView) findViewById(R.id.find_notes_recyclerview);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.find_notes_swiperefreshlayout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent,
                R.color.colorPrimary,
                R.color.teal);
    }

    private void fillDatas() {
        BmobQuery<NotesBean> notesInfoBmobQuery = new BmobQuery<>();
        notesInfoBmobQuery.addWhereLessThanOrEqualTo("createdAt", new BmobDate(new Date()));
        notesInfoBmobQuery.order("-createdAt");  // 按时间降序排列
        // 设定查询缓存策略-CACHE_ELSE_NETWORK: 先从缓存读取数据, 如果没有, 再从网络获取.
        notesInfoBmobQuery.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);
        notesInfoBmobQuery.setMaxCacheAge(TimeUnit.DAYS.toMillis(7));    //此表示缓存一天
        notesInfoBmobQuery.findObjects(new FindListener<NotesBean>() {
            @Override
            public void done(List<NotesBean> list, BmobException e) {
                for (NotesBean notesBean : list) {
                    mDatas.add(notesBean);
                }
            }
        });

    }

    private void eventDeals() {
        // 将数据按照时间排序
        mFindNotesAdapter = new FindNotesAdapter(FindNotesAct.this, mDatas);

        //垂直的，listView的布局方式
        // LinearLayoutManager第三个参数设置为true, 反转显示列表, 从底部出现
        mLayoutManager = new LinearLayoutManager(FindNotesAct.this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setAdapter(mFindNotesAdapter);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());//默认动画
        mRecyclerView.setHasFixedSize(true);//效率最高

        titleBackImv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FindNotesAct.this.finish();
            }
        });

        // item 点击事件
        mFindNotesAdapter.setOnItemClickListener(new FindNotesAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, NotesBean data) {
                Log.i(TAG, "data >> " + data.toString());
                Log.i(TAG, "data >> " + data.getNoteTitle());
                Log.i(TAG, "data >> " + data.getNoteContent());
                Log.i(TAG, "data >> " + data.getNoteTime());

                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("noteInfos", data);
                intent.setClass(FindNotesAct.this, FindNoteDetailAty.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        // item 点击事件
        mFindNotesAdapter.setOnItemLongClickListener(new FindNotesAdapter.OnRecyclerViewItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, final NotesBean data) {
                Log.i(TAG, "data >> " + data.toString());
                Log.i(TAG, "data >> " + data.getNoteTitle());
                Log.i(TAG, "data >> " + data.getNoteContent());
                Log.i(TAG, "data >> " + data.getNoteTime());

                final MaterialDialog materialDialog = new MaterialDialog.Builder(FindNotesAct.this)
                        .title("确定删除？")
                        .negativeText("确定")
                        .negativeColorRes(R.color.teal)
                        .positiveText("取消")
                        .positiveColorRes(R.color.teal)
                        .show();

                View negativeBtn = materialDialog.getActionButton(DialogAction.NEGATIVE);
                View positiveBtn = materialDialog.getActionButton(DialogAction.POSITIVE);
                negativeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mDatas.remove(data);
                        mFindNotesAdapter.notifyDataSetChanged();
                        materialDialog.dismiss();
                    }
                });

                positiveBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        materialDialog.cancel();
                    }
                });
            }
        });

        mAddNoteIbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FindNotesAct.this, FindAddNoteAty.class));
                startActivityForResult(new Intent(FindNotesAct.this, FindAddNoteAty.class),
                        REQUEST_CODE_ADD_1);
            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mDatas.clear();
                fillDatas();
                mFindNotesAdapter.notifyDataSetChanged();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null && resultCode == -1) {
            NotesBean notesBean = (NotesBean) data.getSerializableExtra("newNoteInfos");
            Log.d("SecondFragment", "postData >>> " + notesBean);

            switch (requestCode) {
                case REQUEST_CODE_ADD_1:
//                    mNoDataTv.setVisibility(View.GONE)
                    if (mDatas.isEmpty()) {
                        mDatas.add(notesBean);
                        mRecyclerView.setAdapter(mFindNotesAdapter);
                    } else {
                        mFindNotesAdapter.addDataInTop(notesBean);
                    }

                    break;
            }
        }
    }
}
