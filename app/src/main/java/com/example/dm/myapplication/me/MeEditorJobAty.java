package com.example.dm.myapplication.me;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.dm.myapplication.R;
import com.example.dm.myapplication.beans.AppUser;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by dm on 16-4-14.
 * 职业编辑
 */
public class MeEditorJobAty extends Activity implements View.OnClickListener {
    private static final String LOG = "LOG";
    private static final int RESULT_CODE = 2;

    private ImageView titleLeftImv;

    private RelativeLayout jobRout1;
    private ImageView jobImv1;

    private RelativeLayout jobRout2;
    private ImageView jobImv2;

    private RelativeLayout jobRout3;
    private ImageView jobImv3;

    private RelativeLayout jobRout4;
    private ImageView jobImv4;

    private RelativeLayout jobRout5;
    private ImageView jobImv5;

    private RelativeLayout jobRout6;
    private ImageView jobImv6;

    private RelativeLayout jobRout7;
    private ImageView jobImv7;

    private RelativeLayout jobRout8;
    private ImageView jobImv8;

    private RelativeLayout jobRout9;
    private ImageView jobImv9;

    private RelativeLayout jobRout10;
    private ImageView jobImv10;

    private RelativeLayout jobRout11;
    private ImageView jobImv11;

    private String jobStr;
    private String getJobValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.me_editor_job_layout);

        initViews();
        eventsDeal();
    }

    private void initViews() {
        titleLeftImv = (ImageView) findViewById(R.id.title_imv);

        jobRout1 = (RelativeLayout) findViewById(R.id.job_item_1_rout);
        jobImv1 = (ImageView) findViewById(R.id.job_item_1_imv);

        jobRout2 = (RelativeLayout) findViewById(R.id.job_item_2_rout);
        jobImv2 = (ImageView) findViewById(R.id.job_item_2_imv);

        jobRout3 = (RelativeLayout) findViewById(R.id.job_item_3_rout);
        jobImv3 = (ImageView) findViewById(R.id.job_item_3_imv);

        jobRout4 = (RelativeLayout) findViewById(R.id.job_item_4_rout);
        jobImv4 = (ImageView) findViewById(R.id.job_item_4_imv);

        jobRout5 = (RelativeLayout) findViewById(R.id.job_item_5_rout);
        jobImv5 = (ImageView) findViewById(R.id.job_item_5_imv);

        jobRout6 = (RelativeLayout) findViewById(R.id.job_item_6_rout);
        jobImv6 = (ImageView) findViewById(R.id.job_item_6_imv);

        jobRout7 = (RelativeLayout) findViewById(R.id.job_item_7_rout);
        jobImv7 = (ImageView) findViewById(R.id.job_item_7_imv);

        jobRout8 = (RelativeLayout) findViewById(R.id.job_item_8_rout);
        jobImv8 = (ImageView) findViewById(R.id.job_item_8_imv);

        jobRout9 = (RelativeLayout) findViewById(R.id.job_item_9_rout);
        jobImv9 = (ImageView) findViewById(R.id.job_item_9_imv);

        jobRout10 = (RelativeLayout) findViewById(R.id.job_item_10_rout);
        jobImv10 = (ImageView) findViewById(R.id.job_item_10_imv);

        jobRout11 = (RelativeLayout) findViewById(R.id.job_item_11_rout);
        jobImv11 = (ImageView) findViewById(R.id.job_item_11_imv);

        final Intent intent = getIntent();
        getJobValue = intent.getStringExtra("MeInfoDetailActivity.jobStr");

        switch (getJobValue) {
            case "计算机/互联网/通信":
                jobImv1.setVisibility(View.VISIBLE);
                break;
            case "医疗/护理/制药":
                jobImv2.setVisibility(View.VISIBLE);
                break;
            case "金融/银行/投资/保险":
                jobImv3.setVisibility(View.VISIBLE);
                break;
            case "商业/服务业/个体经营":
                jobImv4.setVisibility(View.VISIBLE);
                break;
            case "文化/广告/传媒":
                jobImv5.setVisibility(View.VISIBLE);
                break;
            case "娱乐/艺术/表演":
                jobImv6.setVisibility(View.VISIBLE);
                break;
            case "律师/法务":
                jobImv7.setVisibility(View.VISIBLE);
                break;
            case "教育/培训":
                jobImv8.setVisibility(View.VISIBLE);
                break;
            case "公务员/行政/事业单位":
                jobImv9.setVisibility(View.VISIBLE);
                break;
            case "学生":
                jobImv10.setVisibility(View.VISIBLE);
                break;
            case "其他职业":
                jobImv11.setVisibility(View.VISIBLE);
                break;
        }
    }

    /**
     * 点击事件处理
     */
    private void eventsDeal() {
        titleLeftImv.setOnClickListener(MeEditorJobAty.this);
        jobRout1.setOnClickListener(MeEditorJobAty.this);
        jobRout2.setOnClickListener(MeEditorJobAty.this);
        jobRout3.setOnClickListener(MeEditorJobAty.this);
        jobRout4.setOnClickListener(MeEditorJobAty.this);
        jobRout5.setOnClickListener(MeEditorJobAty.this);
        jobRout6.setOnClickListener(MeEditorJobAty.this);
        jobRout7.setOnClickListener(MeEditorJobAty.this);
        jobRout8.setOnClickListener(MeEditorJobAty.this);
        jobRout9.setOnClickListener(MeEditorJobAty.this);
        jobRout10.setOnClickListener(MeEditorJobAty.this);
        jobRout11.setOnClickListener(MeEditorJobAty.this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_imv:
                MeEditorJobAty.this.finish();
                break;
            case R.id.job_item_1_rout:
                jobStr = "计算机/互联网/通信";
                jobImv1.setVisibility(View.VISIBLE);
                uploadJobData(jobStr);
                break;
            case R.id.job_item_2_rout:
                jobStr = "医疗/护理/制药";
                jobImv2.setVisibility(View.VISIBLE);
                uploadJobData(jobStr);
                break;
            case R.id.job_item_3_rout:
                jobStr = "金融/银行/投资/保险";
                jobImv3.setVisibility(View.VISIBLE);
                uploadJobData(jobStr);
                break;
            case R.id.job_item_4_rout:
                jobStr = "商业/服务业/个体经营";
                jobImv4.setVisibility(View.VISIBLE);
                uploadJobData(jobStr);
                break;
            case R.id.job_item_5_rout:
                jobStr = "文化/广告/传媒";
                jobImv5.setVisibility(View.VISIBLE);
                uploadJobData(jobStr);
                break;
            case R.id.job_item_6_rout:
                jobStr = "娱乐/艺术/表演";
                jobImv6.setVisibility(View.VISIBLE);
                uploadJobData(jobStr);
                break;
            case R.id.job_item_7_rout:
                jobStr = "律师/法务";
                jobImv7.setVisibility(View.VISIBLE);
                uploadJobData(jobStr);
                break;
            case R.id.job_item_8_rout:
                jobStr = "教育/培训";
                jobImv8.setVisibility(View.VISIBLE);
                uploadJobData(jobStr);
                break;
            case R.id.job_item_9_rout:
                jobStr = "公务员/行政/事业单位";
                jobImv9.setVisibility(View.VISIBLE);
                uploadJobData(jobStr);
                break;
            case R.id.job_item_10_rout:
                jobStr = "学生";
                jobImv10.setVisibility(View.VISIBLE);
                uploadJobData(jobStr);
                break;
            case R.id.job_item_11_rout:
                jobStr = "其他职业";
                jobImv11.setVisibility(View.VISIBLE);
                uploadJobData(jobStr);
                break;
        }
    }

    /**
     * 上传职业job信息
     *
     * @param jobStr job名称
     */
    private void uploadJobData(String jobStr) {
        AppUser newAppUser = new AppUser();
        newAppUser.setUserJob(jobStr);
        AppUser currentAppUser = BmobUser.getCurrentUser(MeEditorJobAty.this, AppUser.class);
        newAppUser.update(MeEditorJobAty.this, currentAppUser.getObjectId(), new UpdateListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(MeEditorJobAty.this, "修改成功!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int i, String s) {
                Log.i(LOG, "i = " + i + ", s = " + s);
            }
        });

        Intent intent1 = new Intent();
        intent1.putExtra("MeEditorJobAty.jobStr", jobStr);
        MeEditorJobAty.this.setResult(RESULT_CODE, intent1);
        MeEditorJobAty.this.finish();
    }
}
