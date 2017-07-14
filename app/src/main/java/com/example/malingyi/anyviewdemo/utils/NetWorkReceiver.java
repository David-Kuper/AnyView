package com.example.malingyi.anyviewdemo.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * Created by malingyi on 2017/3/1.
 */
public class NetWorkReceiver extends BroadcastReceiver {
    public NetWorkReceiver(){

    }
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        String action = intent.getAction();

        if(action.equals(ConnectivityManager.CONNECTIVITY_ACTION)){
            ConnectivityManager condition = (ConnectivityManager)context.getSystemService(
                    Context.CONNECTIVITY_SERVICE);
            if(condition == null){
                Toast.makeText(context, "网络连接不存在！", Toast.LENGTH_LONG).show();
                return ;
            }
            NetworkInfo networkInfo = condition.getActiveNetworkInfo();
            NetworkInfo wifiInfo = condition.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            NetworkInfo nGInfo = condition.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (networkInfo != null && networkInfo.isAvailable() ) {
                if (nGInfo != null && nGInfo.isConnected()) {
                    Toast.makeText(context, "3G网络", Toast.LENGTH_LONG).show();
                } else if(wifiInfo != null && wifiInfo.isConnected()){
                    Toast.makeText(context, "WIFI网络："+wifiInfo.getExtraInfo()+wifiInfo.getDetailedState(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context, "未知网络！", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(context, "当前无网络，请检查，显示数据为本地缓存！", Toast.LENGTH_LONG).show();
            }
        }
    }

}
