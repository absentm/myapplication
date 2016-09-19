package com.example.dm.myapplication.utiltools;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.example.dm.myapplication.utiltools.StringUtils.DEFAULT_DATETIME_PATTERN;

/**
 * Created by dm on 16-4-26.
 * 时间工具类
 */
public class DateUtil {
    public static final String ZHUHU_API_BEGIN_DATESTR = "20150101";

    /**
     * 获取当前系统时间， 24小时制---"HH"
     *
     * @return 当前系统时间
     */
    public static String getCurrentTimeStr() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        Date date = new Date(System.currentTimeMillis());

        return simpleDateFormat.format(date);
    }

    /**
     * 获取当前系统时间， 24小时制---"HH"
     *
     * @return 当前系统时间
     */
    public static long getCurrentTimeMills() {
        return System.currentTimeMillis();
    }

    /**
     * @param datePattenStr 日期格式
     * @return 日期字符串
     */
    public static String getCurrentTimeStr(String datePattenStr) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(datePattenStr, Locale.CHINA);
        Date date = new Date(System.currentTimeMillis());

        return simpleDateFormat.format(date);
    }


    /**
     * 获取随机日期
     *
     * @param beginDate 起始日期，格式为：yyyyMMdd
     * @param endDate   结束日期，格式为：yyyyMMdd
     * @return Date
     */
    public static Date randomDate(String beginDate, String endDate) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
            Date start = format.parse(beginDate);// 构造开始日期
            Date end = format.parse(endDate);// 构造结束日期

            // getTime()表示返回自 1970 年 1 月 1 日 00:00:00 GMT 以来此 Date 对象表示的毫秒数。
            if (start.getTime() >= end.getTime()) {
                return null;
            }

            long date = random(start.getTime(), end.getTime());

            return new Date(date);
        } catch (Exception e) {
            Log.i("LOG", e.getMessage());
        }

        return null;
    }

    public static long random(long begin, long end) {
        long rtn = begin + (long) (Math.random() * (end - begin));

        // 如果返回的是开始时间和结束时间，则递归调用本函数查找随机值
        if (rtn == begin || rtn == end) {
            return random(begin, end);
        }

        return rtn;
    }

    /**
     * 获取随机日期字符串，格式：yyyyMMdd
     *
     * @return String
     */
    public static String randomDateStr() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd", Locale.CHINA);
        Date date = randomDate(ZHUHU_API_BEGIN_DATESTR, getCurrentTimeStr("yyyyMMdd"));

        return simpleDateFormat.format(date);
    }

    /**
     * 日期类型字符串转Date日期类
     *
     * @param timeStr 日期类型字符串: 2016-04-26 15:50:23
     * @return Date
     */
    public static Date string2Date(String timeStr) {
        SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat(DEFAULT_DATETIME_PATTERN);
        Date date = null;
        try {
            date = simpleDateFormat.parse(timeStr);
        } catch (ParseException e) {
            Log.i("LOG", e.getMessage());
        }

        return date;
    }

    /**
     * 判断是否为夜晚
     *
     * @return boolean
     */
    public static boolean isNight() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH");
        String hour = sdf.format(new Date());
        int k = Integer.parseInt(hour);
        if ((k >= 0 && k < 6) || (k >= 18 && k < 24)) {
            return true;
        } else {
            return false;
        }
    }
}
