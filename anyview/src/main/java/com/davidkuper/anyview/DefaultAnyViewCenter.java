package com.davidkuper.anyview;

import android.support.annotation.NonNull;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by malingyi on 2017/3/28.
 */

public class DefaultAnyViewCenter implements IAnyViewCenter {
  private HashMap<Integer, AnyViewBind> anyViewBindMap = new HashMap<>();

  private static class DefaultAnyViewCenterHolder {
    private static final DefaultAnyViewCenter instance = new DefaultAnyViewCenter();
  }

  public static DefaultAnyViewCenter getInstance() {
    return DefaultAnyViewCenterHolder.instance;
  }

  public void registerAnyView(@NonNull Integer type, @NonNull Class viewClazz,
      @NonNull Class beanClazz) {
    registerAnyView(type, viewClazz, beanClazz, 0);
  }

  public void registerAnyView(@NonNull Integer type, @NonNull Class viewClazz,
      @NonNull Class beanClazz, int preNum) {
    AnyViewBind anyViewBind = getAnyViewBind(type);
    if (anyViewBind == null) {
      anyViewBind = new AnyViewBind();
    }
    anyViewBind.beanClazz = beanClazz;
    anyViewBind.viewClazz = viewClazz;
    anyViewBind.type = type;
    anyViewBind.preNum = preNum;
    registerAnyView(type, anyViewBind);
  }

  @Override public void registerAnyView(@NonNull Integer type, @NonNull AnyViewBind anyViewBind) {
    //如果type和anyViewBind存在，并且已经绑定了
    if (anyViewBindMap.containsKey(type) && anyViewBindMap.get(type) != null) {
      throw new IllegalArgumentException("注册失败！type已经存在与其对应的绑定，如需重新注册，请删除原有关系！");
    }
    //或者已经存在该绑定的anyViewBind
    Collection<AnyViewBind> anyViewBinds = anyViewBindMap.values();
    for (AnyViewBind item : anyViewBinds) {
         if (anyViewBind.equals(item)){
           throw new IllegalArgumentException("注册失败！anyViewBind已经存在与其对应的绑定，如需重新注册，请删除原有关系！");
         }
    }
    //如果type和anyViewBind都不存在，或者它们存在但是没有绑定
    anyViewBindMap.put(type, anyViewBind);
  }

  @Override public AnyViewBind getAnyViewBind(@NonNull Integer type) {
    AnyViewBind anyViewBind = anyViewBindMap.get(type);
    return anyViewBind;
  }

  @Override public AnyViewBind removeAnyView(@NonNull Integer type) {
    if (anyViewBindMap.containsKey(type)) {
      AnyViewBind anyViewBind = anyViewBindMap.remove(type);
      return anyViewBind;
    }
    return null;
  }

  @Override public Integer getAnyViewCount() {
    Set set = anyViewBindMap.keySet();
    return set.size();
  }
}
