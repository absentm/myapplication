package com.example.dm.myapplication.utiltools;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
            e.printStackTrace();
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
}
