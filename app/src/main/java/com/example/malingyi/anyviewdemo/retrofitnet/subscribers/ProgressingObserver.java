package com.example.malingyi.anyviewdemo.retrofitnet.subscribers;

import android.util.Log;
import android.widget.Toast;

import com.davidkuper.anyview.utils.GsonUtil;
import com.example.malingyi.anyviewdemo.AnyViewApplication;
import com.example.malingyi.anyviewdemo.utils.NetMotinorUtil;
import com.example.malingyi.anyviewdemo.retrofitnet.HttpResult;
import com.example.malingyi.anyviewdemo.utils.MineActivityManager;
import com.example.malingyi.anyviewdemo.retrofitnet.progress.ProgressCancelListener;
import com.example.malingyi.anyviewdemo.retrofitnet.progress.ProgressDialogHandler;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import java.net.ConnectException;
import java.net.SocketTimeoutException;

/**
 * 用于在Http请求开始时，自动显示一个ProgressDialog
 * 在Http请求结束是，关闭ProgressDialog
 * 调用者自己对请求数据进行处理
 * Created by malingyi on 2017/3/1.
 */
public class ProgressingObserver<T> implements Observer<HttpResult<T>>, ProgressCancelListener {

    private ObserverListener<T> mCallBack;
    private ProgressDialogHandler mProgressDialogHandler;
    private Disposable disposable;

    public ProgressingObserver(ObserverListener<T> mCallBack) {
        this.mCallBack = mCallBack;
        mProgressDialogHandler = new ProgressDialogHandler(this, true);
    }

    private void showProgressDialog(){
        if (mProgressDialogHandler != null) {
            mProgressDialogHandler.obtainMessage(ProgressDialogHandler.SHOW_PROGRESS_DIALOG).sendToTarget();
        }
    }

    private void dismissProgressDialog(){
        if (mProgressDialogHandler != null) {
            mProgressDialogHandler.obtainMessage(ProgressDialogHandler.DISMISS_PROGRESS_DIALOG).sendToTarget();
            mProgressDialogHandler = null;
        }
    }

    /**
     * 订阅开始时调用
     * 显示ProgressDialog
     */
    @Override
    public void onSubscribe(Disposable d) {
        showProgressDialog();
        disposable = d;
        if (!NetMotinorUtil.isNetworkConnected(AnyViewApplication.getApp())) {
            makeToast("网络未连接，请检查！");
            if (disposable != null && !disposable.isDisposed()){
                disposable.dispose();
            }
        }
    }

    /**
     * 完成，隐藏ProgressDialog
     */
    @Override
    public void onComplete() {
        dismissProgressDialog();
    }

    /**
     * 对错误进行统一处理
     * 隐藏ProgressDialog
     * @param e
     */
    @Override
    public void onError(Throwable e) {
        if (e instanceof SocketTimeoutException) {
            makeToast("网络中断，请检查您的网络状态");
        } else if (e instanceof ConnectException) {
            makeToast("网络中断，请检查您的网络状态");
        } else {
            makeToast("error:" + e.getMessage());
        }
        dismissProgressDialog();
        if (mCallBack != null){
            mCallBack.onError(e);
        }

    }

    private void makeToast(String msg){
        Toast.makeText(MineActivityManager.getInstance().getCurrentActivity(), msg, Toast.LENGTH_SHORT).show();
    }



    /**
     * 将onNext方法中的返回结果交给业务层自己处理
     *
     * @param httpResult 创建Subscriber时的泛型类型
     */
    @Override
    public void onNext(HttpResult<T> httpResult) {
        if (httpResult.isSuccess()){
            Log.e("ProgressingObserver",
                "onNext() ——> " + GsonUtil.getInstance().toJson(httpResult));
            if (mCallBack != null){
                mCallBack.onSuccess(httpResult.getData());
            }
        }else {
            if (mCallBack != null){
                mCallBack.onFailure("请求失败！");
            }
        }

    }

    /**
     * 取消ProgressDialog的时候，取消对observable的订阅，同时也取消了http请求
     */
    @Override
    public void onCancelProgress() {
        if (!disposable.isDisposed()){
            disposable.dispose();
        }
    }
}
