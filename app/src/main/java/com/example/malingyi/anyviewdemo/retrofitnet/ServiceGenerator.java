package com.example.malingyi.anyviewdemo.retrofitnet;

import com.example.malingyi.anyviewdemo.BuildConfig;
import com.example.malingyi.anyviewdemo.retrofitnet.api.DataService;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by malingyi on 2017/2/28.
 */

public final class ServiceGenerator {
    private static Retrofit sRetrofit;
    private static final int DEFAULT_TIMEOUT = 5;
    private ServiceGenerator() {
    }
    /**
     * 静态初始化Gson和Retrofit
     */
    static {
        Gson gson = new Gson();
        OkHttpClient.Builder okHttpBuilder = new OkHttpClient.Builder();
        okHttpBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        okHttpBuilder.readTimeout(DEFAULT_TIMEOUT,TimeUnit.SECONDS);
        //添加日志拦截器
        NetworkInterceptor networkIntercepter = new NetworkInterceptor();
        if (BuildConfig.DEBUG){
            okHttpBuilder.addInterceptor(networkIntercepter );
            okHttpBuilder.addNetworkInterceptor(networkIntercepter);
        }

        sRetrofit = new Retrofit.Builder()
                .client(okHttpBuilder.build())
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }
    private static class ServiceSingleTonHolder {
        private static final DataService INSTANCE = sRetrofit.create(DataService.class);
    }

    public static DataService getHotelServiceInstance() {
        return ServiceSingleTonHolder.INSTANCE;
    }

}
