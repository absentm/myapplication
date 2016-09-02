package com.example.dm.myapplication.find;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.dm.myapplication.main.BasicApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


/**
 * FindImageUrlLoader：图片数据提取器
 * Created by dm on 16-9-2.
 */
public class FindImageUrlLoader {
    private static final String TAG = "FindImageUrlLoader";
    //数据类型： 福利 | Android | iOS | 休息视频 | 拓展资源 | 前端 | all
    //目前仅支持福利 剩下的日后扩充
    private static final String TYPE_FL = "福利";
    //分类数据: http://gank.avosapps.com/api/data/数据类型/请求个数/第几页
    private static final String DATA_URL = "http://gank.io/api/data/%s/%d/%d";
    private static final int DEFAULT_COUNT = 10;//默认每页10个
    private static final int DEFAULT_TIMEOUT = 5000;//默认超时请求

    private List<String> imageUrlList;//用于存放图片url
    private int count = DEFAULT_COUNT;//每页数量
    private static int currentPage = 1;//当前页数
    private String dataType = TYPE_FL;//数据格式

    private Context context;
    private Callback callback;//回调函数

    public FindImageUrlLoader(Context context, Callback callback) {
        this(context, DEFAULT_COUNT, callback);

    }

    public FindImageUrlLoader(Context context, int count, Callback callback) {
        imageUrlList = new ArrayList<>();
        this.count = count;
        this.context = context;
        this.callback = callback;

        try {
            dataType = URLEncoder.encode(dataType, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获得第page页图片url * @param page 页数
     */
    public void loadImageUrl(int page) {

        String dataUrl = String.format(DATA_URL, dataType, count, page);
        StringRequest request = new StringRequest(dataUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject obj = new JSONObject(response);
                    JSONArray array = obj.getJSONArray("results");
                    imageUrlList.clear();
                    for (int i = 0; i < array.length(); i++) {
                        String url = array.getJSONObject(i).getString("url");//获得图片url
                        imageUrlList.add(url);
                    }
                    callback.addData(imageUrlList);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //加载出错
                Log.e(TAG, "error:" + error.getMessage());
                Toast.makeText(context, "加载出错", Toast.LENGTH_SHORT).show();
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(DEFAULT_TIMEOUT, 1, 1.0f));//设置请求超时
        BasicApplication.getInstance().addRequest(request);//将消息添加到消息队列
    }


    /**
     * 获取下一页内容
     */
    public void nextPage() {
        currentPage += 1;
        loadImageUrl(currentPage);
    }

    /**
     * 回调函数 * 向数据集中添加新增的数据
     */
    public interface Callback {
        void addData(List<String> list);
    }
}