package com.example.malingyi.anyviewdemo.retrofitnet.api;

import com.davidkuper.anyview.anybean.AnyBean;
import com.example.malingyi.anyviewdemo.retrofitnet.HttpResult;
import io.reactivex.Observable;
import java.util.List;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by malingyi on 2017/3/1.
 */

public interface DataService {

  @GET Observable<HttpResult<List<AnyBean>>> getHotelListData(@Url String url);
}
