package com.example.malingyi.anyviewdemo.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by malingyi on 2017/3/1.
 */

public final class NetMotinorUtil {
    private static NetWorkReceiver netWorkReceiver ;

    private NetMotinorUtil() {
    }
    public static NetWorkReceiver getNetWorkReceiver(){
        if(netWorkReceiver == null){
            netWorkReceiver = new NetWorkReceiver();
        }
        return netWorkReceiver;
    }
    /**
     * 判断当前网络是否是wifi网络
     * @param
     * @return boolean
     */
    public static boolean isWifi(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null
                && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }
    /**
     * 判断当前网络是否是3G网络
     * @param
     * @return boolean
     */
    public static boolean is3G(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null
                && activeNetInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
            return true;
        }
        return false;
    }
    /**
     * 判断是否有网络链接
     * @param
     * @return
     */
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager
                    .getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 判断当前手机的网络是否可用.
     * @param context 上下文
     * @return boolean 是否连上网络
     */
    public static boolean isMobileConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mMobileNetworkInfo = mConnectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (mMobileNetworkInfo != null) {
                if( mMobileNetworkInfo.isAvailable()){
                    return mMobileNetworkInfo.isConnected();
                }else{
                    return false;
                }
            }
        }
        return false;
    }

}
