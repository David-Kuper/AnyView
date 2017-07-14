package com.example.malingyi.anyviewdemo.utils;

/**
 * Created by malingyi on 2017/3/1.
 */

import android.app.Activity;
import android.content.Context;
import com.example.malingyi.anyviewdemo.AnyViewApplication;
import java.lang.ref.WeakReference;

/**
 * 维护当前的Activity实例
 */
public final class MineActivityManager {
    private static MineActivityManager sInstance = new MineActivityManager();
    private WeakReference<Activity> sCurrentActivityWeakRef;


    private MineActivityManager() {

    }

    public static MineActivityManager getInstance() {
        return sInstance;
    }

    public Context getCurrentActivity() {
        if (sCurrentActivityWeakRef != null) {
            Activity currentActivity = sCurrentActivityWeakRef.get();
            return currentActivity;
        }
        return AnyViewApplication.getApp();
    }

    public void setCurrentActivity(Activity activity) {
        sCurrentActivityWeakRef = new WeakReference<Activity>(activity);
    }
}
