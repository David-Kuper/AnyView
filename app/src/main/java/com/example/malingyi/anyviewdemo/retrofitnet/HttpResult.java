package com.example.malingyi.anyviewdemo.retrofitnet;

import java.io.Serializable;

/**
 * Created by malingyi on 2017/2/28.
 */

public class HttpResult<T> implements Serializable {


    private String success;

    private T object;

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getSuccess() {
        return success;
    }

    public T getData() {
        return object;
    }

    public void setData(T data) {
        this.object = data;
    }

    public boolean isSuccess() {
        return success.equals("0");
    }
}
