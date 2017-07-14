package com.example.malingyi.anyviewdemo.retrofitnet.subscribers;


/**
 * Created by malingyi on 2017/3/1.
 */
public interface ObserverListener<T> {
    void onSuccess(T data);
    void onFailure(String msg);
    void onError(Throwable throwable);
}
