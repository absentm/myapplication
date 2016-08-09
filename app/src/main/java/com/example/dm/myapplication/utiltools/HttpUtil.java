package com.example.dm.myapplication.utiltools;

import com.example.dm.myapplication.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by dm on 16-4-22.
 * http工具类
 */
public class HttpUtil {
    public static String NEWSLIST_LATEST = "http://news-at.zhihu.com/api/4/news/latest";
    public static String STORY_VIEW = "http://daily.zhihu.com/story/";
    public static String NEWSDETAIL = "http://news-at.zhihu.com/api/4/news/";

    public static DisplayImageOptions DefaultOptions =
            new DisplayImageOptions.Builder()
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
//                    .showImageOnLoading(R.drawable.app_icon) //加载图片时的图片
                    .showImageForEmptyUri(R.drawable.app_icon) //没有图片资源时的默认图片
                    .showImageOnFail(R.drawable.app_icon) //加载失败时的图片
                    .considerExifParams(true) //启用EXIF和JPEG图像格式
                    .build();

    public static String get(String urlAddr) throws IOException {
        HttpURLConnection con = null;
        try {
            URL url = new URL(urlAddr);
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", "Mozilla/5.0");

            if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                return response.toString();
            } else {
                throw new IOException("Network Error - response code: " + con.getResponseCode());
            }
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }
    }

    public static String getLastNewsList() throws IOException {
        return get(NEWSLIST_LATEST);
    }

    public static String getNewsDetail(int id) throws IOException {
        return get(NEWSDETAIL + id);
    }

}
