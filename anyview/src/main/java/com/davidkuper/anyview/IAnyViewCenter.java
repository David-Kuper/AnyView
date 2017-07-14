package com.davidkuper.anyview;

import android.support.annotation.NonNull;

/**
 * Created by malingyi on 2017/3/28.
 */

public interface IAnyViewCenter{
  void registerAnyView(@NonNull Integer type,@NonNull AnyViewBind anyViewBind);
  AnyViewBind getAnyViewBind(@NonNull Integer type);
  AnyViewBind removeAnyView(@NonNull Integer type);
  Integer getAnyViewCount();
}
