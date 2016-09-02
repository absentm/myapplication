package com.example.dm.myapplication.main;

import android.app.Application;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;


/**
 * Created by dm on 16-4-23.
 * basicApplication
 */
public class BasicApplication extends Application {
    private static final String TAG = "MyApplication";

    private RequestQueue requestQueue;
    private static BasicApplication applicationInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        initImageLoader(getApplicationContext());
        applicationInstance = this;
        requestQueue = Volley.newRequestQueue(getApplicationContext());
    }

    public static synchronized BasicApplication getInstance() {
        return applicationInstance;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addRequest(Request<T> request) {
        request.setTag(TAG);
        requestQueue.add(request);
    }

    public void cancleRequest() {
        requestQueue.cancelAll(TAG);
    }

    /**
     * 自定义ImageLoader配置
     *
     * @param context context
     */
    public void initImageLoader(Context context) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .diskCacheExtraOptions(1500, 3000, null)
                .threadPoolSize(3)      //线程池内加载的数量
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .diskCacheSize(50 * 1024 * 1024) // 50 Mb
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .writeDebugLogs() // Remove for release app
                .build();

        ImageLoader.getInstance().init(config);   // Initialize ImageLoader with configuration.
    }
}
