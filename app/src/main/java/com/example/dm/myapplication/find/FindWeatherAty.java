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

import com.afollestad.materialdialogs.MaterialDialog;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.bumptech.glide.Glide;
import com.example.dm.myapplication.R;
import com.example.dm.myapplication.beans.WeatherBeans.IndexBean;
import com.example.dm.myapplication.beans.WeatherBeans.WeatherDataBean;
import com.example.dm.myapplication.beans.WeatherBeans.WeatherInfo;
import com.example.dm.myapplication.customviews.WeatherChartView;
import com.example.dm.myapplication.utiltools.AMapUtils;
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
public class FindWeatherAty extends Activity implements AMapLocationListener {
    private final static String LOG = "FindWeatherAty";
    private ImageView titleLeftImv;
    private TextView titleCityTv;

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
    private List<IndexBean> index;
    private List<WeatherDataBean> weather_data;

    private String mCityName = "郑州";     // 默认郑州

    private AMapLocationClient aMapLocationClient = null;
    private AMapLocationClientOption aMapLocationClientOption = null;

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

            mSwipeRefreshLayout.setRefreshing(false);
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

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.find_weather_SRfLout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent,
                R.color.colorPrimary,
                R.color.teal);

        // 进入页面mSwipeRefreshLayout自动刷新并填充数据
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
                refresh2GetDatas(mCityName);
            }
        });

        // 设置高德地图
        aMapLocationClient = new AMapLocationClient(FindWeatherAty.this);
        aMapLocationClientOption = new AMapLocationClientOption();
        aMapLocationClientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        aMapLocationClientOption.setOnceLocation(true);
        aMapLocationClient.setLocationListener(this);

        aMapLocationClientOption.setNeedAddress(true);
        aMapLocationClientOption.setInterval(1000);
        aMapLocationClient.setLocationOption(aMapLocationClientOption);
        aMapLocationClient.startLocation();
        mLocationHandler.sendEmptyMessage(AMapUtils.MSG_LOCATION_START);
    }

    private void EventDeal() {
        titleLeftImv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FindWeatherAty.this.finish();
            }
        });

        mDressRout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialDialog.Builder(FindWeatherAty.this)
                        .title("穿衣提醒")
                        .content(index.get(0).getDes())
                        .positiveText("OK")
                        .positiveColorRes(R.color.teal)
                        .show();
            }
        });

        mCarRout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialDialog.Builder(FindWeatherAty.this)
                        .title("洗车提醒")
                        .content(index.get(1).getDes())
                        .positiveText("OK")
                        .positiveColorRes(R.color.teal)
                        .show();
            }
        });

        mTripRout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialDialog.Builder(FindWeatherAty.this)
                        .title("旅行提醒")
                        .content(index.get(2).getDes())
                        .positiveText("OK")
                        .positiveColorRes(R.color.teal)
                        .show();
            }
        });

        mColdlRout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialDialog.Builder(FindWeatherAty.this)
                        .title("感冒提醒")
                        .content(index.get(3).getDes())
                        .positiveText("OK")
                        .positiveColorRes(R.color.teal)
                        .show();
            }
        });

        mSportRout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialDialog.Builder(FindWeatherAty.this)
                        .title("运动提醒")
                        .content(index.get(4).getDes())
                        .positiveText("OK")
                        .positiveColorRes(R.color.teal)
                        .show();
            }
        });

        mSunRout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialDialog.Builder(FindWeatherAty.this)
                        .title("紫外线提醒")
                        .content(index.get(5).getDes())
                        .positiveText("OK")
                        .positiveColorRes(R.color.teal)
                        .show();
            }
        });

        // 下拉刷新事件
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh2GetDatas(mCityName);
            }
        });

        titleCityTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialDialog.Builder(FindWeatherAty.this)
                        .title("选择城市")
                        .items(R.array.city_values)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                                mCityName = (String) text;

                                // 修改城市后自动刷新并填充数据
                                refresh2GetDatas(mCityName);
                            }
                        }).show();
            }
        });
    }

    /**
     * 激活刷新填充数据
     *
     * @param cityStr
     */
    private void refresh2GetDatas(final String cityStr) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String weatherDatas = HttpUtil.getWeatherJson(cityStr);
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
        weather_data = weatherInfos.getResults().get(0).getWeather_data();

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
        index = weatherInfos.getResults().get(0).getIndex();

        mDressTv.setText(new StringBuilder().append("穿衣 - ").append(index.get(0).getZs()).toString());
        mDressTipTv.setText(index.get(0).getDes());

        mCarTv.setText(new StringBuilder().append("洗车 - ").append(index.get(1).getZs()).toString());
        mCarTipTv.setText(index.get(1).getDes());

        mTripTv.setText(new StringBuilder().append("旅行 - ").append(index.get(2).getZs()).toString());
        mTripTipTv.setText(index.get(2).getDes());

        mColdlTv.setText(new StringBuilder().append("感冒 - ").append(index.get(3).getZs()).toString());
        mColdlTipTv.setText(index.get(3).getDes());

        mSportTv.setText(new StringBuilder().append("运动 - ").append(index.get(4).getZs()).toString());
        mSportTipTv.setText(index.get(4).getDes());

        mSunTv.setText(new StringBuilder().append("紫外线 - ").append(index.get(5).getZs()).toString());
        mSunTipTv.setText(index.get(5).getDes());

        // 获取天气气温折线图
        Log.d(LOG, "tmpMap.size() >>>> " + tmpMap.size());
        int[] higherTmp = new int[tmpMap.size()];
        int[] lowerTmp = new int[tmpMap.size()];

        for (int i = 0; i < tmpMap.size(); i++) {
            higherTmp[i] = StringUtils.string2Int(tmpMap.get(i).get("high"));
            lowerTmp[i] = StringUtils.string2Int(tmpMap.get(i).get("low"));
            Log.d(LOG, "i >>>> " + i);
        }

        mWeatherChartView.setTempDay(higherTmp);
        mWeatherChartView.setTempNight(lowerTmp);
        mWeatherChartView.invalidate();

        tmpMap.clear();
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
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (null != aMapLocation) {
            Message message = mLocationHandler.obtainMessage();
            message.obj = aMapLocation;
            message.what = AMapUtils.MSG_LOCATION_FINISH;
            mLocationHandler.sendMessage(message);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != aMapLocationClient) {
            aMapLocationClient.onDestroy();
            aMapLocationClient = null;
            aMapLocationClientOption = null;
        }
    }

    Handler mLocationHandler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            switch (msg.what) {
                case AMapUtils.MSG_LOCATION_START:
                    Toast.makeText(FindWeatherAty.this, "正在定位中.....",
                            Toast.LENGTH_SHORT).show();
                    break;
                case AMapUtils.MSG_LOCATION_FINISH:
                    AMapLocation aMapLocation = (AMapLocation) msg.obj;
                    String result = AMapUtils.getLocationCityStr(aMapLocation);

                    if (result.startsWith("定位失败")) {
                        Log.i(LOG, "result = " + result);
                        Toast.makeText(FindWeatherAty.this, "定位失败, 请稍后再试.....",
                                Toast.LENGTH_SHORT).show();
                        titleCityTv.setText("定位失败，请手动添加");
                    } else {
                        Log.i(LOG, "result = " + result);
                        titleCityTv.setText(result);

                        mCityName = result;
                        // 修改城市后自动刷新并填充数据
                        refresh2GetDatas(mCityName);
                    }

                    break;
                default:
                    break;
            }
        }
    };
}
