package com.example.dm.myapplication.main;

import android.app.Application;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;


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

}
