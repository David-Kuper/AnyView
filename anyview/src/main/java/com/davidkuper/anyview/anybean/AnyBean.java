package com.davidkuper.anyview.anybean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by malingyi on 2017/3/1.
 */

public final class AnyBean<T> implements Parcelable {
     public int anyViewType; //anyView的类别对应于AnyViewType.type
     public T data;  //具体的AnyBean数据类

     public AnyBean(){}

     protected AnyBean(Parcel in) {
          anyViewType = in.readInt();
     }

     public static final Creator<AnyBean> CREATOR = new Creator<AnyBean>() {
          @Override
          public AnyBean createFromParcel(Parcel in) {
               return new AnyBean(in);
          }

          @Override
          public AnyBean[] newArray(int size) {
               return new AnyBean[size];
          }
     };

     @Override
     public int describeContents() {
          return 0;
     }

     @Override
     public void writeToParcel(Parcel dest, int flags) {
          dest.writeInt(anyViewType);
     }
}
