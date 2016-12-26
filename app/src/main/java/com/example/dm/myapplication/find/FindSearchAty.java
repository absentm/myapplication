package com.example.dm.myapplication.find;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.dm.myapplication.R;
import com.example.dm.myapplication.beans.SearchBeans.SearchBean;
import com.example.dm.myapplication.beans.SearchBeans.SearchResultsBean;
import com.example.dm.myapplication.utiltools.HttpUtil;
import com.example.dm.myapplication.utiltools.SystemUtils;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.util.List;


/**
 * FindSearchAty
 * Created by dm on 16-11-10.
 */
public class FindSearchAty extends Activity
        implements View.OnClickListener,
        FindSearchAdapter.OnRecyclerViewItemClickListener {

    private ImageButton mTitleBackIBtn;
    private EditText mSearchContentEt;
    private ImageButton mSearchIbtn;

    private RecyclerView mRecyclerView;
    private SearchBean mSearchBean;
    private List<SearchResultsBean> mDatas;
    private String mSearchContentStr;
    private FindSearchAdapter mFindSearchAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private MaterialDialog mMaterialDialog;

    private boolean isConnect;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String gankJsonStr = msg.obj.toString();
            Log.d("FindGankAty", " Json: " + gankJsonStr);

            Gson gson = new Gson();
            mSearchBean = gson.fromJson(gankJsonStr, SearchBean.class);
            if (!mSearchBean.getResults().isEmpty() && mSearchBean != null) {
                mDatas = mSearchBean.getResults();

                mFindSearchAdapter = new FindSearchAdapter(FindSearchAty.this, mDatas);
                mLayoutManager = new LinearLayoutManager(FindSearchAty.this,
                        LinearLayoutManager.VERTICAL, false);
                mRecyclerView.setAdapter(mFindSearchAdapter);
                mRecyclerView.setLayoutManager(mLayoutManager);
                mRecyclerView.setItemAnimator(new DefaultItemAnimator());//默认动画
                mRecyclerView.setHasFixedSize(true);//效率最高
                mFindSearchAdapter.setOnItemClickListener(FindSearchAty.this);
                mMaterialDialog.dismiss();
            } else {
                mMaterialDialog.dismiss();
                Toast.makeText(FindSearchAty.this,
                        "数据加载出错！",
                        Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_serch_lay);

        initView();
        setUpListener();
    }

    private void initView() {
        isConnect = SystemUtils.checkNetworkConnection(FindSearchAty.this);
        mTitleBackIBtn = (ImageButton) findViewById(R.id.find_search_back_ibtn);
        mSearchContentEt = (EditText) findViewById(R.id.find_search_et);
        mSearchIbtn = (ImageButton) findViewById(R.id.find_search_ibtn);
        mRecyclerView = (RecyclerView) findViewById(R.id.find_search_recyclerview);

        if (!isConnect) {
            SystemUtils.noNetworkAlert(FindSearchAty.this);
        }
    }

    private void setUpListener() {
        mTitleBackIBtn.setOnClickListener(FindSearchAty.this);
        mSearchIbtn.setOnClickListener(FindSearchAty.this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.find_search_back_ibtn:
                FindSearchAty.this.finish();
                break;
            case R.id.find_search_ibtn:
                mSearchContentStr = mSearchContentEt.getText().toString().trim();
                if (!mSearchContentStr.isEmpty()) {
                    mMaterialDialog = new MaterialDialog.Builder(FindSearchAty.this)
                            .content("Please waiting...")
                            .contentGravity(GravityEnum.CENTER)
                            .progress(true, 0)
                            .progressIndeterminateStyle(true)
                            .show();
                    mMaterialDialog.setCancelable(false);

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                String gankDatasJsonStr = HttpUtil.getSearchJsonStr(mSearchContentStr);
                                Log.d("FindSearchAty", gankDatasJsonStr);
                                if (!gankDatasJsonStr.equals("")) {
                                    Message message = mHandler.obtainMessage();
                                    message.obj = gankDatasJsonStr;
                                    mHandler.sendMessage(message);
                                } else {
                                    Log.d("FindSearchAty", "出错");
                                    mMaterialDialog.dismiss();
                                }
                            } catch (UnsupportedEncodingException e) {
                                Log.d("FindSearchAty", e.getMessage());
                            }
                        }
                    }).start();

                } else {
                    Toast.makeText(FindSearchAty.this,
                            "请输入您的搜索内容 !!!",
                            Toast.LENGTH_LONG).show();
                }

                break;
        }
    }

    @Override
    public void onItemClick(View view, SearchResultsBean data) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable("gankItemInfos", data);
        intent.setClass(FindSearchAty.this, FindGankDetailAty.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
