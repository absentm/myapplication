package com.example.dm.myapplication.find;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.dm.myapplication.R;
import com.example.dm.myapplication.beans.WeatherBeans.IndexBean;
import com.example.dm.myapplication.beans.WeatherBeans.WeatherDataBean;
import com.example.dm.myapplication.beans.WeatherBeans.WeatherInfo;
import com.example.dm.myapplication.customviews.WeatherChartView;
import com.example.dm.myapplication.utiltools.DateUtil;
import com.example.dm.myapplication.utiltools.HttpUtil;
import com.example.dm.myapplication.utiltools.StringUtils;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * FindWeatherAty: 天气
 * Created by dm on 16-9-3.
 */
public class FindWeatherAty extends Activity implements SwipeRefreshLayout.OnRefreshListener {
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

    private WeatherInfo mWeatherDatas;
    private List<Map<String, String>> tmpMap = new ArrayList<>();

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String weatherJsonStr = msg.obj.toString();
            Log.d(LOG, " Json: " + weatherJsonStr);

            Gson gson = new Gson();
            mWeatherDatas = gson.fromJson(weatherJsonStr, WeatherInfo.class);
            if (mWeatherDatas.getError() == 0) {
                fillWeatherDatas(mWeatherDatas);
            } else {
                Toast.makeText(FindWeatherAty.this,
                        "数据加载出错！",
                        Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_weather_layout);

        initViews();
        EventDeal();
    }

    private void initViews() {
        titleLeftImv = (ImageView) findViewById(R.id.title_left_imv);
        titleCityTv = (TextView) findViewById(R.id.title_center_tv);
        titleRightImv = (ImageView) findViewById(R.id.title_right_imv);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.find_weather_SRfLout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent,
                R.color.colorPrimary,
                R.color.teal);

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


    private void EventDeal() {
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

        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(LOG, "-=-=-=-=>begin");
                try {
                    String weatherDatas = HttpUtil.getWeatherJson("郑州");
                    if (!weatherDatas.equals("")) {
                        Message message = mHandler.obtainMessage();
                        message.obj = weatherDatas;
                        mHandler.sendMessage(message);
                    }
                } catch (UnsupportedEncodingException e) {
                    Log.d(LOG, e.getMessage());
                }
            }
        }).start();

    }


    /**
     * 填充数据
     *
     * @param weatherInfos WeatherInfos对象
     */
    private void fillWeatherDatas(WeatherInfo weatherInfos) {
        boolean isNight = DateUtil.isNight();

        titleCityTv.setText(weatherInfos.getResults().get(0).getCurrentCity());
        mCurDateTv.setText(weatherInfos.getDate() + " 发布");
        mCurPm25Tv.setText("PM2.5  " + weatherInfos.getResults().get(0).getPm25() + "μg/m³");

        // 获取4天的天气数据
        List<WeatherDataBean> weather_data =
                weatherInfos.getResults().get(0).getWeather_data();

        mCurTempTv.setText(parseTempData(weather_data.get(0).getDate()));
        mCurWeaDesTv.setText(weather_data.get(0).getWeather());
        mCurWeaWindTv.setText(weather_data.get(0).getWind());

        Map<String, String> map1 = parseHighAndLowTmp(weather_data.get(0).getTemperature());
        mTodayWeaHighTmpTv.setText(map1.get("high") + "℃");
        mTodayWeaLowTmpTv.setText(map1.get("low") + "℃");
        tmpMap.add(map1);
        mTodayWeaDesTv.setText(weather_data.get(0).getWeather());

        Map<String, String> map2 = parseHighAndLowTmp(weather_data.get(1).getTemperature());
        mTomorrowWeaHighTmpTv.setText(map2.get("high") + "℃");
        mTomorrowWeaLowTmpTv.setText(map2.get("low") + "℃");
        tmpMap.add(map2);
        mTomorrowWeaDesTv.setText(weather_data.get(1).getWeather());

        mData1WeaDesTv.setText(weather_data.get(0).getWeather());
        mData1WeaWinsTv.setText(weather_data.get(0).getWind());

        mData2TimeTv.setText(weather_data.get(1).getDate());
        mData2WeaDesTv.setText(weather_data.get(1).getWeather());
        mData2WeaWinsTv.setText(weather_data.get(1).getWind());

        mData3TimeTv.setText(weather_data.get(2).getDate());
        mData3WeaDesTv.setText(weather_data.get(2).getWeather());
        mData3WeaWinsTv.setText(weather_data.get(2).getWind());
        Map<String, String> map3 = parseHighAndLowTmp(weather_data.get(2).getTemperature());
        tmpMap.add(map3);

        mData4TimeTv.setText(weather_data.get(3).getDate());
        mData4WeaDesTv.setText(weather_data.get(3).getWeather());
        mData4WeaWinsTv.setText(weather_data.get(3).getWind());
        Map<String, String> map4 = parseHighAndLowTmp(weather_data.get(3).getTemperature());
        tmpMap.add(map4);

        if (isNight) {
            loadWeatherPng(weather_data.get(0).getNightPictureUrl(),
                    mTodayWeaImv);
            loadWeatherPng(weather_data.get(1).getNightPictureUrl(),
                    mTomorrowWeaImv);

            loadWeatherPng(weather_data.get(0).getNightPictureUrl(),
                    mData1Imv);
            loadWeatherPng(weather_data.get(1).getNightPictureUrl(),
                    mData2Imv);
            loadWeatherPng(weather_data.get(2).getNightPictureUrl(),
                    mData3Imv);
            loadWeatherPng(weather_data.get(3).getNightPictureUrl(),
                    mData4Imv);
        } else {
            loadWeatherPng(weather_data.get(0).getDayPictureUrl(),
                    mTodayWeaImv);
            loadWeatherPng(weather_data.get(1).getDayPictureUrl(),
                    mTomorrowWeaImv);

            loadWeatherPng(weather_data.get(0).getDayPictureUrl(),
                    mData1Imv);
            loadWeatherPng(weather_data.get(1).getDayPictureUrl(),
                    mData2Imv);
            loadWeatherPng(weather_data.get(2).getDayPictureUrl(),
                    mData3Imv);
            loadWeatherPng(weather_data.get(3).getDayPictureUrl(),
                    mData4Imv);
        }

        // 获取生活指数数据
        List<IndexBean> index =
                weatherInfos.getResults().get(0).getIndex();

        mDressTv.setText(mDressTv.getText().toString() + " - ".toString() + index.get(0).getZs());
        mDressTipTv.setText(index.get(0).getDes());

        mCarTv.setText(mCarTv.getText().toString() + " - ".toString() + index.get(1).getZs());
        mCarTipTv.setText(index.get(1).getDes());

        mTripTv.setText(mTripTv.getText().toString() + " - ".toString() + index.get(2).getZs());
        mTripTipTv.setText(index.get(2).getDes());

        mColdlTv.setText(mColdlTv.getText().toString() + " - ".toString() + index.get(3).getZs());
        mColdlTipTv.setText(index.get(3).getDes());

        mSportTv.setText(mSportTv.getText().toString() + " - ".toString() + index.get(4).getZs());
        mSportTipTv.setText(index.get(4).getDes());

        mSunTv.setText(mSunTv.getText().toString() + " - ".toString() + index.get(5).getZs());
        mSunTipTv.setText(index.get(5).getDes());

        // 获取天气气温折线图
        int[] higherTmp = new int[4];
        int[] lowerTmp = new int[4];
        for (int i = 0; i < tmpMap.size(); i++) {
            higherTmp[i] = StringUtils.string2Int(tmpMap.get(i).get("high"));
            lowerTmp[i] = StringUtils.string2Int(tmpMap.get(i).get("low"));
        }

        mWeatherChartView.setTempDay(higherTmp);
        mWeatherChartView.setTempNight(lowerTmp);
        mWeatherChartView.invalidate();
    }

    private void loadWeatherPng(String url, ImageView imageView) {
        Glide.with(FindWeatherAty.this)
                .load(url)
                .error(R.drawable.ic_weather_nice)
                .into(imageView);
    }

    /**
     * 解析实时气温数据
     *
     * @param tmpStr
     * @return
     */
    private String parseTempData(String tmpStr) {
        String[] tempStr1 = tmpStr.split("：");
        Log.d(LOG, "tempStr1[1] = " + tempStr1[1]);

        String tempStr2 = tempStr1[1];

        String[] tempStr3 = tempStr2.split("\\)");
        Log.d(LOG, "tempStr1[1] = " + tempStr3[0]);

        return tempStr3[0];
    }

    /**
     * @param tmpStr
     * @return
     */
    private Map<String, String> parseHighAndLowTmp(String tmpStr) {
        Map<String, String> map = new HashMap<>();
        String[] tempStr = tmpStr.split("~");

        Log.d(LOG, "Higher = " + tempStr[0].trim());

        String[] tempStr1 = tempStr[1].trim().split("℃");

        Log.d(LOG, "Lower = " + tempStr1[0].trim());

        map.put("high", tempStr[0].trim());
        map.put("low", tempStr1[0].trim());

        return map;
    }

    @Override
    public void onRefresh() {
        //模拟加载网络数据，这里设置3秒，正好能看到3色进度条
        new Handler().postDelayed(new Runnable() {
            public void run() {
                //显示或隐藏刷新进度条
                mSwipeRefreshLayout.setRefreshing(false);
                // 更新数据数据
                fillWeatherDatas(mWeatherDatas);
            }
        }, 3000);
    }

}
