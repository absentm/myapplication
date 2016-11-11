package com.example.dm.myapplication.find;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.dm.myapplication.R;
import com.example.dm.myapplication.utiltools.SystemUtils;

import static com.example.dm.myapplication.R.id.find_search_back_ibtn;
import static com.example.dm.myapplication.R.id.find_search_ibtn;


/**
 * FindSearchAty
 * Created by dm on 16-11-10.
 */
public class FindSearchAty extends Activity
        implements View.OnClickListener {

    private ImageButton mTitleBackIBtn;
    private EditText mSearchContentEt;
    private ImageButton mSearchIbtn;

    private RecyclerView mRecyclerView;
    private String mSearchContentStr;

    private boolean isConnect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_serch_lay);

        initView();
        setUpListener();
    }

    private void initView() {
        isConnect = SystemUtils.checkNetworkConnection(FindSearchAty.this);
        mTitleBackIBtn = (ImageButton) findViewById(find_search_back_ibtn);
        mSearchContentEt = (EditText) findViewById(R.id.find_search_et);
        mSearchIbtn = (ImageButton) findViewById(R.id.find_search_ibtn);
        mRecyclerView = (RecyclerView) findViewById(R.id.find_search_recyclerview);

        if (isConnect) {

        } else {
            SystemUtils.noNetworkAlert(FindSearchAty.this);
        }
    }

    private void setUpListener() {
        mTitleBackIBtn.setOnClickListener(FindSearchAty.this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case find_search_back_ibtn:
                FindSearchAty.this.finish();
                break;
            case find_search_ibtn:
                mSearchContentStr = mSearchContentEt.getText().toString().trim();
                break;
        }
    }
}
