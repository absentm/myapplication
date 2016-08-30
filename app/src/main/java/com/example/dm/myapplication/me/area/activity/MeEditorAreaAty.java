package com.example.dm.myapplication.me.area.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.example.dm.myapplication.R;
import com.example.dm.myapplication.beans.AppUser;
import com.example.dm.myapplication.customviews.kankan.wheel.widget.OnWheelChangedListener;
import com.example.dm.myapplication.customviews.kankan.wheel.widget.WheelView;
import com.example.dm.myapplication.customviews.kankan.wheel.widget.adapters.ArrayWheelAdapter;
import com.example.dm.myapplication.utiltools.AMapUtils;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by dm on 16-4-15.
 * 所在地编辑
 */
public class MeEditorAreaAty extends BaseActivity implements View.OnClickListener, OnWheelChangedListener, AMapLocationListener {
    private static final String LOG = "LOG";

    // 初始化顶部栏显示
    private ImageView titleLeftImv;
    private RelativeLayout currentAreaRout;
    private TextView currentAreaTv;
    private RelativeLayout fullAreaRout;
    private LinearLayout wheelLout;
    private TextView fullAreaTv;

    private WheelView mViewProvince;
    private WheelView mViewCity;
    private WheelView mViewDistrict;
    private TextView mConfirmTv;

    private AMapLocationClient aMapLocationClient = null;
    private AMapLocationClientOption aMapLocationClientOption = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.me_editor_area_layout);

        initViews();
        setUpListener();
        setUpdata();
    }


    /**
     * 初始化界面控件
     */
    private void initViews() {
        // 标题栏控件
        titleLeftImv = (ImageView) findViewById(R.id.title_imv);
        currentAreaRout = (RelativeLayout) findViewById(R.id.me_editor__current_area_rout);
        currentAreaTv = (TextView) findViewById(R.id.me_editor_current_area_tv);
        fullAreaRout = (RelativeLayout) findViewById(R.id.me_editor_full_area_rout);
        fullAreaTv = (TextView) findViewById(R.id.me_editor_full_area_tv);
        wheelLout = (LinearLayout) findViewById(R.id.me_editor_area_wheel);

        mViewProvince = (WheelView) findViewById(R.id.me_editor_province_wheel);
        mViewCity = (WheelView) findViewById(R.id.me_editor_city_wheel);
        mViewDistrict = (WheelView) findViewById(R.id.me_editor_district_wheel);
        mConfirmTv = (TextView) findViewById(R.id.me_editor_area_finish_tv);

        AppUser appUser = BmobUser.getCurrentUser(AppUser.class);
        if (appUser != null) {
            fullAreaTv.setText(appUser.getUserArea());
        } else {
            fullAreaTv.setText("查找位置, 同城交友");
        }
    }

    /**
     * 设置监听
     */
    private void setUpListener() {
        titleLeftImv.setOnClickListener(this);
        currentAreaRout.setOnClickListener(this);

        fullAreaRout.setOnClickListener(this);
        // 添加change事件
        mViewProvince.addChangingListener(this);
        // 添加change事件
        mViewCity.addChangingListener(this);
        // 添加change事件
        mViewDistrict.addChangingListener(this);
        // 添加onclick事件
        mConfirmTv.setOnClickListener(this);

        // 设置高德地图
        aMapLocationClient = new AMapLocationClient(this.getApplicationContext());
        aMapLocationClientOption = new AMapLocationClientOption();
        aMapLocationClientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        aMapLocationClientOption.setOnceLocation(true);
        aMapLocationClient.setLocationListener(this);
    }

    /**
     * 加载初始化控件数据
     */
    private void setUpdata() {
        initProvinceDatas();
        mViewProvince.setViewAdapter(new ArrayWheelAdapter<>(MeEditorAreaAty.this, mProvinceDatas));
        // 设置可见条目数量
        mViewProvince.setVisibleItems(7);
        mViewCity.setVisibleItems(7);
        mViewDistrict.setVisibleItems(7);
        updateCities();
        updateAreas();
    }

    /**
     * 根据当前的省，更新市WheelView的信息
     */
    private void updateCities() {
        int currentProvince = mViewProvince.getCurrentItem();
        mCurrentProviceName = mProvinceDatas[currentProvince];
        String[] cities = mCitisDatasMap.get(mCurrentProviceName);
        if (cities == null) {
            cities = new String[]{""};
        }
        mViewCity.setViewAdapter(new ArrayWheelAdapter<>(this, cities));
        mViewCity.setCurrentItem(0);
        updateAreas();
    }


    /**
     * 根据当前的市，更新区WheelView的信息
     */
    private void updateAreas() {
        int currentCity = mViewCity.getCurrentItem();
        mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[currentCity];
        String[] areas = mDistrictDatasMap.get(mCurrentCityName);

        if (areas == null) {
            areas = new String[]{""};
        }
        mViewDistrict.setViewAdapter(new ArrayWheelAdapter<String>(this, areas));
        mViewDistrict.setCurrentItem(0);
    }


    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        // TODO Auto-generated method stub
        if (wheel == mViewProvince) {
            updateCities();
        } else if (wheel == mViewCity) {
            updateAreas();
        } else if (wheel == mViewDistrict) {
            mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[newValue];
            mCurrentZipCode = mZipcodeDatasMap.get(mCurrentDistrictName);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_imv:
                MeEditorAreaAty.this.finish();
                break;
            case R.id.me_editor__current_area_rout:
                aMapLocationClientOption.setNeedAddress(true);
                aMapLocationClientOption.setInterval(1000);
                aMapLocationClient.setLocationOption(aMapLocationClientOption);
                aMapLocationClient.startLocation();
                mHandler.sendEmptyMessage(AMapUtils.MSG_LOCATION_START);
                break;
            case R.id.me_editor_full_area_rout:
                wheelLout.setVisibility(View.VISIBLE);
                break;
            case R.id.me_editor_area_finish_tv:
                String areaStr = mCurrentProviceName + " " + mCurrentCityName + " " + mCurrentDistrictName;
                fullAreaTv.setText(areaStr);
                uploadAreaData(areaStr);
                break;
            default:
                break;
        }
    }

    /**
     * 上传职业job信息
     *
     * @param areaStr job名称
     */
    private void uploadAreaData(String areaStr) {
        AppUser newAppUser = new AppUser();
        newAppUser.setUserArea(areaStr);
        AppUser currentAppUser = BmobUser.getCurrentUser(AppUser.class);
        newAppUser.update(currentAppUser.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Toast.makeText(MeEditorAreaAty.this, "修改成功, 数据已上传!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MeEditorAreaAty.this, "修改失败! " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        Intent intent1 = new Intent();
        intent1.putExtra("MeEditorAreaAty.areaStr", areaStr);
        MeEditorAreaAty.this.setResult(RESULT_OK, intent1);
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (null != aMapLocation) {
            Message message = mHandler.obtainMessage();
            message.obj = aMapLocation;
            message.what = AMapUtils.MSG_LOCATION_FINISH;
            mHandler.sendMessage(message);
        }

    }

    Handler mHandler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            switch (msg.what) {
                case AMapUtils.MSG_LOCATION_START:
                    currentAreaTv.setText("正在定位......");
                    break;
                case AMapUtils.MSG_LOCATION_FINISH:
                    AMapLocation aMapLocation = (AMapLocation) msg.obj;
                    String result = AMapUtils.getLocationStr(aMapLocation);

                    if (result.startsWith("定位失败")) {
                        Log.i(LOG, "result = " + result);
                        Toast.makeText(MeEditorAreaAty.this, "定位失败, 请稍后再试.....",
                                Toast.LENGTH_SHORT).show();
                        currentAreaTv.setText("使用当前位置");
                    } else {
                        Log.i(LOG, "result = " + result);
                        currentAreaTv.setText(result);
                        fullAreaTv.setText(result);
                        uploadAreaData(result);
                    }

                    break;
                default:
                    break;
            }
        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != aMapLocationClient) {
            aMapLocationClient.onDestroy();
            aMapLocationClient = null;
            aMapLocationClientOption = null;
        }
    }
}
