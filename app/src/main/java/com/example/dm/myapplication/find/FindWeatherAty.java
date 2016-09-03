package com.example.dm.myapplication.find;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.dm.myapplication.R;
import com.example.dm.myapplication.customviews.WeatherChartView;

/**
 * FindWeatherAty: 天气
 * Created by dm on 16-9-3.
 */
public class FindWeatherAty extends Activity {
    private final static String LOG = "FindWeatherAty";
    private ImageView titleLeftImv;
    private TextView titleCityTv;
    private ImageView titleRightImv;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private TextView mCurDateTv;
    private TextView mCurTempTv;
    private TextView mCurPm25Tv;
    private TextView mCurWeaDesTv;
    private TextView mCurWeaWindTv;

    private ImageView mTodayWeaImv;
    private TextView mTodayWeaHighTmpTv;
    private TextView mTodayWeaLowTmpTv;
    private TextView mTodayWeaDesTv;

    private ImageView mTomorrowWeaImv;
    private TextView mTomorrowWeaHighTmpTv;
    private TextView mTomorrowWeaLowTmpTv;
    private TextView mTomorrowWeaDesTv;

    private ImageView mData1Imv;
    private TextView mData1WeaDesTv;
    private TextView mData1WeaWinsTv;

    private TextView mData2TimeTv;
    private ImageView mData2Imv;
    private TextView mData2WeaDesTv;
    private TextView mData2WeaWinsTv;

    private TextView mData3TimeTv;
    private ImageView mData3Imv;
    private TextView mData3WeaDesTv;
    private TextView mData3WeaWinsTv;

    private TextView mData4TimeTv;
    private ImageView mData4Imv;
    private TextView mData4WeaDesTv;
    private TextView mData4WeaWinsTv;

    private WeatherChartView mWeatherChartView;

    private RelativeLayout mDressRout;
    private RelativeLayout mCarRout;
    private RelativeLayout mTripRout;
    private RelativeLayout mColdlRout;
    private RelativeLayout mSportRout;
    private RelativeLayout mSunRout;

    private TextView mDressTv;
    private TextView mDressTipTv;

    private TextView mCarTv;
    private TextView mCarTipTv;

    private TextView mTripTv;
    private TextView mTripTipTv;

    private TextView mColdlTv;
    private TextView mColdlTipTv;

    private TextView mSportTv;
    private TextView mSportTipTv;

    private TextView mSunTv;
    private TextView mSunTipTv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_weather_layout);

        initViews();
        initSwipView();
        TitleEventDeal();
    }


    private void TitleEventDeal() {
        titleLeftImv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FindWeatherAty.this.finish();
            }
        });

        titleRightImv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void initSwipView() {
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent,
                R.color.colorPrimary,
                R.color.teal);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

            }
        });


    }

    private void initViews() {
        titleLeftImv = (ImageView) findViewById(R.id.title_left_imv);
        titleCityTv = (TextView) findViewById(R.id.title_center_tv);
        titleRightImv = (ImageView) findViewById(R.id.title_right_imv);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.find_weather_SRfLout);

        mCurDateTv = (TextView) findViewById(R.id.find_today_date);
        mCurTempTv = (TextView) findViewById(R.id.find_weather_temp);
        mCurPm25Tv = (TextView) findViewById(R.id.find_weather_pm25_tv);
        mCurWeaDesTv = (TextView) findViewById(R.id.find_weather_describe);
        mCurWeaWindTv = (TextView) findViewById(R.id.find_weather_wind);

        mTodayWeaImv = (ImageView) findViewById(R.id.find_weather_today_imv);
        mTodayWeaHighTmpTv = (TextView) findViewById(R.id.find_weather_today_hightmp);
        mTodayWeaLowTmpTv = (TextView) findViewById(R.id.find_weather_today_lowtmp);
        mTodayWeaDesTv = (TextView) findViewById(R.id.find_weather_today_wind_tv);

        mTomorrowWeaImv = (ImageView) findViewById(R.id.find_weather_tomorrow_imv);
        mTomorrowWeaHighTmpTv = (TextView) findViewById(R.id.find_weather_tomorrow_hightmp);
        mTomorrowWeaLowTmpTv = (TextView) findViewById(R.id.find_weather_tomorrow_lowtmp);
        mTomorrowWeaDesTv = (TextView) findViewById(R.id.find_weather_tomorrow_wind_tv);

        mData1Imv = (ImageView) findViewById(R.id.find_data1_imv);
        mData1WeaDesTv = (TextView) findViewById(R.id.find_data1_tmp_tv);
        mData1WeaWinsTv = (TextView) findViewById(R.id.find_data1_wind_tv);

        mData2TimeTv = (TextView) findViewById(R.id.find_data2_title_tv);
        mData2Imv = (ImageView) findViewById(R.id.find_data2_imv);
        mData2WeaDesTv = (TextView) findViewById(R.id.find_data2_tmp_tv);
        mData2WeaWinsTv = (TextView) findViewById(R.id.find_data2_wind_tv);

        mData3TimeTv = (TextView) findViewById(R.id.find_data3_title_tv);
        mData3Imv = (ImageView) findViewById(R.id.find_data3_imv);
        mData3WeaDesTv = (TextView) findViewById(R.id.find_data3_tmp_tv);
        mData3WeaWinsTv = (TextView) findViewById(R.id.find_data3_wind_tv);

        mData4TimeTv = (TextView) findViewById(R.id.find_data4_title_tv);
        mData4Imv = (ImageView) findViewById(R.id.find_data4_imv);
        mData4WeaDesTv = (TextView) findViewById(R.id.find_data4_tmp_tv);
        mData4WeaWinsTv = (TextView) findViewById(R.id.find_data4_wind_tv);

        mWeatherChartView = (WeatherChartView) findViewById(R.id.find_weather_weather_chart);

        mDressRout = (RelativeLayout) findViewById(R.id.find_dress_rout);
        mCarRout = (RelativeLayout) findViewById(R.id.find_car_rout);
        mTripRout = (RelativeLayout) findViewById(R.id.find_trip_rout);
        mColdlRout = (RelativeLayout) findViewById(R.id.find_coldl_rout);
        mSportRout = (RelativeLayout) findViewById(R.id.find_sport_rout);
        mSunRout = (RelativeLayout) findViewById(R.id.find_ziwaixian_rout);

        mDressTv = (TextView) findViewById(R.id.find_dress_tv);
        mDressTipTv = (TextView) findViewById(R.id.find_dress_tip_tv);

        mCarTv = (TextView) findViewById(R.id.find_car_tv);
        mCarTipTv = (TextView) findViewById(R.id.find_car_tip_tv);

        mTripTv = (TextView) findViewById(R.id.find_trip_tv);
        mTripTipTv = (TextView) findViewById(R.id.find_trip_tip_tv);

        mColdlTv = (TextView) findViewById(R.id.find_coldl_tv);
        mColdlTipTv = (TextView) findViewById(R.id.find_coldl_tip_tv);

        mSportTv = (TextView) findViewById(R.id.find_sport_tv);
        mSportTipTv = (TextView) findViewById(R.id.find_sport_tip_tv);

        mSunTv = (TextView) findViewById(R.id.find_ziwaixian_tv);
        mSunTipTv = (TextView) findViewById(R.id.find_ziwaixian_tip_tv);

    }
}
