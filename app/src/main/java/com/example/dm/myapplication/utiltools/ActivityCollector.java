package com.example.dm.myapplication.utiltools;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dm on 16-4-29.
 * activity 控制器
 */
public class ActivityCollector {
    public static List<Activity> activities = new ArrayList<Activity>();

    public static void addActivity(Activity activity) {
        activities.add(activity);
    }

    public static void removeActivity(Activity activity) {
        activities.remove(activity);
    }

    public static void finishAll() {
        for (Activity activity : activities) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }

}
