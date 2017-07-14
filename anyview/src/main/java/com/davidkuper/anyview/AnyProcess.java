package com.davidkuper.anyview;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.Log;
import com.davidkuper.anyview.anybean.AnyBean;
import com.davidkuper.anyview.utils.GsonUtil;
import com.google.gson.JsonSyntaxException;
import com.google.gson.internal.LinkedTreeMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by malingyi on 2017/3/1.
 */
public final class AnyProcess {
  private static final int THREAD_POOL_NUM = 3;

  private AnyProcess() {

  }

  private static final ExecutorService THREAD_POOL = Executors.newFixedThreadPool(THREAD_POOL_NUM);

  /**
   * 解析List<AnyBean<T>>，只能解析AnyBean<T>的T这一层，如果T里面还有嵌套泛型就需要自己解析
   */
  public static void parseBeanList(final List<AnyBean> anyBeanList,
      @NonNull final CallBack callBack) {
    THREAD_POOL.submit(new Runnable() {
      @Override public void run() {
        for (AnyBean anyBean : anyBeanList) {
          AnyViewBind anyViewBind =
              DefaultAnyViewCenter.getInstance().getAnyViewBind(anyBean.anyViewType);
          Log.e("AnyProcess", anyViewBind.toString());
          Log.e("AnyProcess", anyBean.data.toString());
          Class clazz = anyViewBind.beanClazz;
          if (anyBean.data instanceof LinkedTreeMap) {
            try {
              anyBean.data = GsonUtil.fromJsonMap((LinkedTreeMap) anyBean.data, clazz);
            } catch (JsonSyntaxException e) {
              Log.e("AnyProcess", "JsonSyntaxException!");
              callBack.parseError(e.getMessage());
              return;
            }
          } else if (anyBean.data != null) {//不是LinkedTreeMap，说明已经是具体的Bean对象了。
            Log.e("AnyProcess", anyBean.data.getClass().getName());
          } else {
            //为null时，不解析
            Log.e("AnyProcess", "null object!");
          }
        }
        if (Looper.myLooper() != Looper.getMainLooper()) {
          Handler handler = new Handler(Looper.getMainLooper());
          handler.post(new Runnable() {
            @Override public void run() {
              callBack.parseSuccess(anyBeanList);
            }
          });
        } else {
          callBack.parseSuccess(anyBeanList);
        }
      }
    });
  }

  public interface CallBack {
    void parseSuccess(List<AnyBean> list);

    void parseError(String error);
  }
}
