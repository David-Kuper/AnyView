package com.example.malingyi.anyviewdemo;

import android.app.Activity;
import android.os.Bundle;
import com.davidkuper.anyview.DefaultAnyViewCenter;
import com.example.malingyi.anyviewdemo.anyview.AnyView1001;
import com.example.malingyi.anyviewdemo.anyview.AnyView1002;
import com.example.malingyi.anyviewdemo.anyview.AnyView1003;
import com.example.malingyi.anyviewdemo.anyview.AnyView1004;
import com.example.malingyi.anyviewdemo.anyview.AnyView1005;
import com.example.malingyi.anyviewdemo.anyview.bean.AnyBean1001;
import com.example.malingyi.anyviewdemo.anyview.bean.AnyBean1002;
import com.example.malingyi.anyviewdemo.anyview.bean.AnyBean1003;
import com.example.malingyi.anyviewdemo.anyview.bean.AnyBean1004;
import com.example.malingyi.anyviewdemo.anyview.bean.AnyBean1005;
import com.example.malingyi.anyviewdemo.utils.MineActivityManager;

/**
 * Created by jubin on 16/6/21.
 */
public class AnyViewApplication extends android.app.Application {

    private static AnyViewApplication roadMapApp;
    @Override
    public void onCreate() {
        super.onCreate();
        roadMapApp = this;
        registerAnyView();
        /**
         * 注册Activity生命周期回调监听
         */
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {
                MineActivityManager.getInstance().setCurrentActivity(activity);
            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }

    private void registerAnyView() {
        DefaultAnyViewCenter.getInstance().registerAnyView(0, AnyView1001.class, AnyBean1001.class);
        DefaultAnyViewCenter.getInstance().registerAnyView(1, AnyView1002.class, AnyBean1002.class);
        DefaultAnyViewCenter.getInstance().registerAnyView(2, AnyView1003.class, AnyBean1003.class);
        DefaultAnyViewCenter.getInstance().registerAnyView(3, AnyView1004.class, AnyBean1004.class);
        DefaultAnyViewCenter.getInstance().registerAnyView(4, AnyView1005.class, AnyBean1005.class);
    }
    public static AnyViewApplication getApp(){
        return roadMapApp;
    }

}
