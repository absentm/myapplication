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
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.dm.myapplication.R;
import com.example.dm.myapplication.beans.AppUser;
import com.example.dm.myapplication.beans.NotesBean;
import com.example.dm.myapplication.main.LoginActivity;
import com.example.dm.myapplication.utiltools.SystemUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

import static com.example.dm.myapplication.R.id.title_add_ibtn;

/**
 * FindNotesAty
 * Created by dm on 16-9-23.
 */
public class FindNotesAty extends Activity implements
        FindNotesAdapter.OnRecyclerViewItemClickListener,
        FindNotesAdapter.OnRecyclerViewItemLongClickListener,
        View.OnClickListener {

    private static final String TAG = "FindNotesAty";
    private static final int REQUEST_CODE_ADD_1 = 1;
    private ImageButton titleBackImv;
    private ImageButton mAddNoteIbtn;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private List<NotesBean> mDatas = new ArrayList<>();
    private FindNotesAdapter mFindNotesAdapter;

    private boolean isConnect;
    private AppUser mAppUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_notes_layout);

        initView();
        setUpListener();
    }

    private void initView() {
        isConnect = SystemUtils.checkNetworkConnection(FindNotesAty.this);

        mAppUser = BmobUser.getCurrentUser(AppUser.class);
        if (mAppUser == null) {
            startActivity(new Intent(this, LoginActivity.class));
        }

        Log.i(TAG, "mAppUser.getUsername() >> " + mAppUser.getUsername());

        titleBackImv = (ImageButton) findViewById(R.id.notes_back_imv);
        mAddNoteIbtn = (ImageButton) findViewById(title_add_ibtn);

        mRecyclerView = (RecyclerView) findViewById(R.id.find_notes_recyclerview);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.find_notes_swiperefreshlayout);
        mSwipeRefreshLayout.setColorSchemeResources(
                R.color.colorAccent,
                R.color.colorPrimary,
                R.color.teal);

        mFindNotesAdapter = new FindNotesAdapter(FindNotesAty.this, mDatas);
        //垂直的，listView的布局方式
        // LinearLayoutManager第三个参数设置为true, 反转显示列表, 从底部出现
        mLayoutManager = new LinearLayoutManager(FindNotesAty.this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setAdapter(mFindNotesAdapter);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());//默认动画
        mRecyclerView.setHasFixedSize(true);//效率最高

        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
            }
        });

        if (isConnect) {
            generateDatas();
        } else {
            mSwipeRefreshLayout.postOnAnimationDelayed(new Runnable() {
                @Override
                public void run() {
                    mSwipeRefreshLayout.setRefreshing(false);
                    SystemUtils.noNetworkAlert(FindNotesAty.this);
                }
            }, 3000);
        }

    }

    private void setUpListener() {
        mFindNotesAdapter.setOnItemClickListener(this);
        mFindNotesAdapter.setOnItemLongClickListener(this);
        titleBackImv.setOnClickListener(this);
        mAddNoteIbtn.setOnClickListener(this);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (isConnect = SystemUtils.checkNetworkConnection(FindNotesAty.this)) {
                    generateDatas();
                } else {
                    SystemUtils.noNetworkAlert(FindNotesAty.this);
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }
        });
    }

    private void generateDatas() {
        BmobQuery<NotesBean> query1 = new BmobQuery<>();
        query1.addWhereLessThanOrEqualTo("createdAt", new BmobDate(new Date()));

        BmobQuery<NotesBean> query2 = new BmobQuery<>();
        query2.addWhereEqualTo("userNameStr", mAppUser.getUsername());

        List<BmobQuery<NotesBean>> andQuerys = new ArrayList<>();
        andQuerys.add(query1);
        andQuerys.add(query2);

        BmobQuery<NotesBean> notesInfoBmobQuery = new BmobQuery<>();
        notesInfoBmobQuery.and(andQuerys);
        notesInfoBmobQuery.order("-createdAt");  // 按时间降序排列
        // 设定查询缓存策略-CACHE_ELSE_NETWORK: 先从缓存读取数据, 如果没有, 再从网络获取.
        notesInfoBmobQuery.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);
        notesInfoBmobQuery.setMaxCacheAge(TimeUnit.DAYS.toMillis(7));    //此表示缓存一天
        notesInfoBmobQuery.findObjects(new FindListener<NotesBean>() {
            @Override
            public void done(List<NotesBean> list, BmobException e) {
                if (!list.isEmpty() && (list.size() != mDatas.size())) {
                    for (NotesBean notesBean : list) {
                        mDatas.add(notesBean);
                    }

                    if (!mDatas.isEmpty()) {
                        mFindNotesAdapter.notifyDataSetChanged();
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                } else if (list.isEmpty()) {
                    Toast.makeText(FindNotesAty.this, "暂无数据", Toast.LENGTH_SHORT).show();
                    mSwipeRefreshLayout.setRefreshing(false);
                } else if (list.size() == mDatas.size()) {
                    Toast.makeText(FindNotesAty.this, "没有更多内容了", Toast.LENGTH_SHORT).show();
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }
        });

    }

    @Override
    public void onItemClick(View view, NotesBean data) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable("noteInfos", data);
        intent.setClass(FindNotesAty.this, FindNoteDetailAty.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onItemLongClick(View view, final NotesBean data) {
        final MaterialDialog materialDialog = new MaterialDialog.Builder(FindNotesAty.this)
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.notes_back_imv:
                FindNotesAty.this.finish();
                break;
            case R.id.title_add_ibtn:
                startActivityForResult(new Intent(FindNotesAty.this, FindAddNoteAty.class),
                        REQUEST_CODE_ADD_1);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null && resultCode == -1) {
            NotesBean notesBean = (NotesBean) data.getSerializableExtra("newNoteInfos");

            switch (requestCode) {
                case REQUEST_CODE_ADD_1:
                    if (mDatas.isEmpty()) {
                        mDatas.add(notesBean);
                        mFindNotesAdapter = new FindNotesAdapter(this, mDatas);
                        mRecyclerView.setAdapter(mFindNotesAdapter);
//                        mFindNotesAdapter.notifyDataSetChanged();
                    } else {
                        mFindNotesAdapter.addDataInTop(notesBean);
                    }

                    break;
            }
        }
    }

}
