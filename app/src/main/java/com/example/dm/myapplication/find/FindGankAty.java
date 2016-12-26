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
import android.widget.Button;
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
 * FindGankAty
 * Created by dm on 16-11-10.
 */
public class FindGankAty extends Activity implements View.OnClickListener,
        FindSearchAdapter.OnRecyclerViewItemClickListener {
    private ImageButton mTitleBackIBtn;
    private Button mGankClassBtn;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<SearchResultsBean> mDatas;
    private FindSearchAdapter mFindSearchAdapter;

    private boolean isConnect;
    private String mSelectedStr;
    private SearchBean mSearchBean;
    private MaterialDialog mMaterialDialog;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String gankJsonStr = msg.obj.toString();
            Log.d("FindGankAty", " Json: " + gankJsonStr);

            Gson gson = new Gson();
            mSearchBean = gson.fromJson(gankJsonStr, SearchBean.class);
            if (!mSearchBean.getResults().isEmpty() && mSearchBean != null) {
                mDatas = mSearchBean.getResults();

                mFindSearchAdapter = new FindSearchAdapter(FindGankAty.this, mDatas);
                mLayoutManager = new LinearLayoutManager(FindGankAty.this,
                        LinearLayoutManager.VERTICAL, false);
                mRecyclerView.setAdapter(mFindSearchAdapter);
                mRecyclerView.setLayoutManager(mLayoutManager);
                mRecyclerView.setItemAnimator(new DefaultItemAnimator());//默认动画
                mRecyclerView.setHasFixedSize(true);//效率最高
                mFindSearchAdapter.setOnItemClickListener(FindGankAty.this);
                mMaterialDialog.dismiss();
            } else {
                mMaterialDialog.dismiss();
                Toast.makeText(FindGankAty.this,
                        "数据加载出错！",
                        Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_gank_lay);

        initView();
        setUpListener();
    }

    private void initView() {
        isConnect = SystemUtils.checkNetworkConnection(FindGankAty.this);
        mTitleBackIBtn = (ImageButton) findViewById(R.id.title_gank_back_ibtn);
        mGankClassBtn = (Button) findViewById(R.id.title_gank_class_tv);
        mRecyclerView = (RecyclerView) findViewById(R.id.find_gank_recyclerview);
        mMaterialDialog = new MaterialDialog.Builder(FindGankAty.this)
                .content("Please waiting...")
                .contentGravity(GravityEnum.CENTER)
                .progress(true, 0)
                .progressIndeterminateStyle(true)
                .show();
        mMaterialDialog.setCancelable(false);

        if (isConnect) {
            switchSelectedDatas("all");
        } else {
            mMaterialDialog.dismiss();
            SystemUtils.noNetworkAlert(FindGankAty.this);
        }
    }

    private void setUpListener() {
        mTitleBackIBtn.setOnClickListener(FindGankAty.this);
        mGankClassBtn.setOnClickListener(FindGankAty.this);
    }

    private void switchSelectedDatas(final String selectedStr) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String gankDatasJsonStr = HttpUtil.getGankJsonStr(selectedStr);
                    if (!gankDatasJsonStr.equals("")) {
                        Message message = mHandler.obtainMessage();
                        message.obj = gankDatasJsonStr;
                        mHandler.sendMessage(message);
                    } else {
                        Log.d("FindGankAty", "出错");
                    }
                } catch (UnsupportedEncodingException e) {
                    Log.d("FindGankAty", e.getMessage());
                }
            }
        }).start();
    }

    @Override
    public void onItemClick(View view, SearchResultsBean data) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable("gankItemInfos", data);
        intent.setClass(FindGankAty.this, FindGankDetailAty.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_gank_back_ibtn:
                FindGankAty.this.finish();
                break;
            case R.id.title_gank_class_tv:
                new MaterialDialog
                        .Builder(FindGankAty.this).title("干货列表")
                        .items(R.array.gank_values)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog,
                                                    View itemView,
                                                    int position,
                                                    CharSequence text) {
                                mSelectedStr = (String) text;
                                mGankClassBtn.setText(mSelectedStr);
                                mMaterialDialog.show();

                                if (isConnect) {
                                    switchSelectedDatas(mSelectedStr);
                                } else {
                                    mMaterialDialog.dismiss();
                                    SystemUtils.noNetworkAlert(FindGankAty.this);
                                }
                            }
                        }).show();
                break;
        }
    }
}
