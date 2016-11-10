package com.example.dm.myapplication.find;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.dm.myapplication.R;
import com.example.dm.myapplication.utiltools.SystemUtils;

/**
 * FindGankAty
 * Created by dm on 16-11-10.
 */
public class FindGankAty extends Activity implements View.OnClickListener {
    private ImageButton mTitleBackIBtn;
    private Button mGankClassBtn;
    private RecyclerView mRecyclerView;

    private boolean isConnect;
    private String mSelectedStr;

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
        mRecyclerView = (RecyclerView) findViewById(R.id.find_search_recyclerview);

        if (isConnect) {

        } else {
            SystemUtils.noNetworkAlert(FindGankAty.this);
        }
    }

    private void setUpListener() {
        mTitleBackIBtn.setOnClickListener(FindGankAty.this);
        mGankClassBtn.setOnClickListener(FindGankAty.this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_gank_back_ibtn:
                FindGankAty.this.finish();
                break;
            case R.id.title_gank_class_tv:
                new MaterialDialog.Builder(FindGankAty.this)
                        .title("干货列表")
                        .items(R.array.gank_values)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog,
                                                    View itemView,
                                                    int position,
                                                    CharSequence text) {
                                mSelectedStr = (String) text;
                                Toast.makeText(FindGankAty.this, mSelectedStr,
                                        Toast.LENGTH_SHORT).show();
                            }
                        }).show();
        }
    }
}
