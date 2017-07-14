package com.example.malingyi.anyviewdemo.retrofitnet.callback;


/**
 * Created by malingyi on 2017/3/1.
 */

public interface CallBackR<T> {
    void onSuccess(T data);
    void onFailure(String msg);
    void onError(String message);
}
