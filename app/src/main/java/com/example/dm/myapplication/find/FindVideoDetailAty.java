package com.example.dm.myapplication.find;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.dm.myapplication.R;
import com.example.dm.myapplication.beans.VideoBeans.VideoResultsBean;
import com.example.dm.myapplication.utiltools.DateUtil;

import java.text.ParseException;

/**
 * FindVideoDetailAty
 * Created by dm on 16-10-30.
 */
public class FindVideoDetailAty extends Activity implements View.OnClickListener {
    private ImageButton titleBackIBtn;

    private TextView mVideoDescTv;
    private TextView mVideoPublishedAtTv;
    private TextView mVideoCreateAtTv;
    private TextView mVideoSourceTv;
    private TextView mVideoWhoTv;

    private VideoResultsBean mVideoResultsBean;
    private String mVideoItemDescStr;
    private String mVideoItemPublishAtStr;
    private String mVideoItemCreateAtStr;
    private String mVideoItemSourceStr;
    private String mVideoItemWhoStr;
    private String mVideoItemUrlStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_video_item_detal);

        initView();
        setUpListener();

        try {
            fillVideoDatas();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        titleBackIBtn = (ImageButton) findViewById(R.id.video_detail_back_imv);
        mVideoDescTv = (TextView) findViewById(R.id.video_detail_desc_tv);
        mVideoPublishedAtTv = (TextView) findViewById(R.id.video_detail_publish_time_tv);
        mVideoCreateAtTv = (TextView) findViewById(R.id.video_detail_create_time_tv);
        mVideoSourceTv = (TextView) findViewById(R.id.video_detail_source_tv);
        mVideoWhoTv = (TextView) findViewById(R.id.video_detail_who_tv);
    }

    private void setUpListener() {
        titleBackIBtn.setOnClickListener(FindVideoDetailAty.this);
    }

    private void fillVideoDatas() throws ParseException {
        Intent intent = getIntent();
        mVideoResultsBean = (VideoResultsBean) intent.getSerializableExtra("videoItemInfos");


        mVideoItemUrlStr = mVideoResultsBean.getUrl();
        Log.i("TAG", "mVideoItemUrlStr： " + mVideoItemUrlStr);

        mVideoItemDescStr = "『" + mVideoResultsBean.getDesc() + "』";
        mVideoItemPublishAtStr = "PublishedAt： " +
                DateUtil.utc2LocalTime(mVideoResultsBean.getPublishedAt());
        mVideoItemCreateAtStr = "CreatedAt： " +
                DateUtil.utc2LocalTime(mVideoResultsBean.getCreatedAt());
        mVideoItemSourceStr = "Source from： " + mVideoResultsBean.getSource();
        mVideoItemWhoStr = "Shared by： " + mVideoResultsBean.getWho();

        mVideoDescTv.setText(mVideoItemDescStr);
        mVideoPublishedAtTv.setText(mVideoItemPublishAtStr);
        mVideoCreateAtTv.setText(mVideoItemCreateAtStr);
        mVideoSourceTv.setText(mVideoItemSourceStr);
        mVideoWhoTv.setText(mVideoItemWhoStr);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.video_detail_back_imv:
                FindVideoDetailAty.this.finish();
                break;
            default:
                break;
        }
    }
}
