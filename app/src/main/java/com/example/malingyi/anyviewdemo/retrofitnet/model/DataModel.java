package com.example.malingyi.anyviewdemo.retrofitnet.model;

import android.util.Log;
import com.davidkuper.anyview.AnyProcess;
import com.davidkuper.anyview.anybean.AnyBean;
import com.davidkuper.anyview.utils.GsonUtil;
import com.example.malingyi.anyviewdemo.retrofitnet.ServiceGenerator;
import com.example.malingyi.anyviewdemo.retrofitnet.api.DataService;
import com.example.malingyi.anyviewdemo.retrofitnet.callback.CallBackR;
import com.example.malingyi.anyviewdemo.retrofitnet.subscribers.ObserverListener;
import com.example.malingyi.anyviewdemo.retrofitnet.subscribers.ProgressingObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import java.util.List;

/**
 * Created by malingyi on 2017/3/1.
 */

public class DataModel {
  DataService hotelService;
  public DataModel() {
    hotelService = ServiceGenerator.getHotelServiceInstance();
  }

  public <T> void getHotelList(String url, final CallBackR<T> callBack) {
    hotelService.getHotelListData(url)
        .subscribeOn(Schedulers.io())
        .unsubscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new ProgressingObserver<List<AnyBean>>(new ObserverListener<List<AnyBean>>() {

          @Override public void onSuccess(List<AnyBean> anyBeanList) {
            if (anyBeanList != null && anyBeanList.size() > 0) {
              AnyProcess.parseBeanList(anyBeanList, new AnyProcess.CallBack() {
                @Override public void parseSuccess(List<AnyBean> list) {
                  Log.e("DataModel","parseSuccess( )" + "   "+Thread.currentThread());
                  Log.e("DataModel", GsonUtil.getInstance().toJson(list));
                  if (callBack != null) {
                    callBack.onSuccess((T) list);
                  }
                }

                @Override public void parseError(String error) {
                  if (callBack != null){
                    callBack.onError(error);
                  }
                }
              });
            }
          }

          @Override public void onFailure(String msg) {
            if (callBack != null) {
              callBack.onFailure(msg);
            }
          }

          @Override public void onError(Throwable throwable) {
            if (callBack != null) {
              callBack.onError(throwable.getMessage());
            }
          }
        }));
  }
}
