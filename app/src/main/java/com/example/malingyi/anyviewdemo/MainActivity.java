package com.example.malingyi.anyviewdemo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import com.davidkuper.anyview.adapter.AnyAdapter;
import com.davidkuper.anyview.anybean.AnyBean;
import com.davidkuper.anyview.listview.DefaultAnyListView;
import com.davidkuper.anyview.utils.GsonUtil;
import com.example.malingyi.anyviewdemo.retrofitnet.callback.CallBackR;
import com.example.malingyi.anyviewdemo.retrofitnet.model.DataModel;
import com.example.malingyi.anyviewdemo.utils.MineActivityManager;
import java.util.List;

public class MainActivity extends Activity {
  private static final String TAG = "MainActivity";

  DefaultAnyListView mListView;
  AnyAdapter mAnyAdapter;
  DataModel mDataModel;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    MineActivityManager.getInstance().setCurrentActivity(this);
    init();
    onRefreshTop();
  }

  private void init() {
    mListView = (DefaultAnyListView) findViewById(R.id.lisview);
    mAnyAdapter = new AnyAdapter(MainActivity.this);
    mListView.setAdapter(mAnyAdapter);

    mDataModel = new DataModel();
    mListView.setHeaderDividersEnabled(false);
    mListView.setFooterDividersEnabled(false);
    mListView.setOnMeiTuanRefreshListener(new DefaultAnyListView.OnMeiTuanRefreshListener() {
      @Override public void onRefresh() {
        onRefreshTop();
      }
    });
    mListView.setOnMeituanLoadingListener(new DefaultAnyListView.OnMeiTuanLoadingListener() {
      @Override public void onLoadMore() {
        loadMore();
      }
    });
  }


  private void onRefreshTop(){
    mDataModel.getHotelList(getResources().getString(R.string.getDataList),
        new CallBackR<List<AnyBean>>() {
          @Override public void onSuccess(List<AnyBean> data) {
            if (data != null) {
              mAnyAdapter.setData(data);
            }
            Log.e(TAG, GsonUtil.toJsonString(data));
          }

          @Override public void onFailure(String msg) {
            Log.e(TAG, msg);
            mListView.setOnRefreshComplete();
          }

          @Override public void onError(String error) {
            Log.e(TAG, error);
            mListView.setOnRefreshComplete();
          }
        });
  }
  private void loadMore() {
    mDataModel.getHotelList(getResources().getString(R.string.getDataList),
        new CallBackR<List<AnyBean>>() {
          @Override public void onSuccess(List<AnyBean> data) {
            if (data != null) {
              mAnyAdapter.addData(data);
            }
            mListView.setOnLoadMoreComplete();
          }

          @Override public void onFailure(String msg) {
            Log.e(TAG, msg);
            mListView.setOnLoadMoreComplete();
          }

          @Override public void onError(String error) {
            Log.e(TAG, error);
            mListView.setOnLoadMoreComplete();
          }
        });
  }
}
