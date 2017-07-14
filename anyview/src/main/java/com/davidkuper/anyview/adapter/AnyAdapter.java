package com.davidkuper.anyview.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.davidkuper.anyview.AnyViewBind;
import com.davidkuper.anyview.DefaultAnyViewCenter;
import com.davidkuper.anyview.anybean.AnyBean;
import com.davidkuper.anyview.view.SuperAnyView;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

/**
 * Created by malingyi on 2017/3/1.
 */

public class AnyAdapter extends BaseAdapter {
  private final Object object = new Object();
  private static final String TAG = "AnyAdapter";
  private Context context;
  private List<AnyBean> list = new ArrayList<>();
  private AnyAdapterListener anyAdapterListener;
  private LinkedHashMap<Class, List<SuperAnyView>> preViewListMap = new LinkedHashMap<>();

  public AnyAdapter(Context context, List<AnyBean> list) {
    this.context = context;
    this.list.clear();
    this.list.addAll(list);
  }

  public AnyAdapter(Context context) {
    this.context = context;
  }

  public void setData(List<AnyBean> list) {
    synchronized (object){
      this.list.clear();
      this.list.addAll(list);
      notifyDataSetChanged();
    }
  }

  public void addData(List<AnyBean> list) {
    synchronized (object){
      this.list.addAll(list);
      notifyDataSetChanged();
    }
  }

  public void addData(AnyBean cardBean) {
    synchronized (object){
      this.list.add(cardBean);
      notifyDataSetChanged();
    }
  }

  @Override public int getCount() {
    return list.size();
  }

  @Override public boolean hasStableIds() {
    return super.hasStableIds();
  }

  @Override public Object getItem(int i) {
    return list.get(i);
  }

  @Override public long getItemId(int i) {
    return i;
  }

  //TODO 重要知识点

  /**
   * 该方法在getView之前调用，从缓存中寻到对应的View
   */
  @Override public int getItemViewType(int position) {
    return list.get(position).anyViewType;
  }

  //TODO

  /**
   * 该方法只在setAdapter的时候调用
   */
  @Override public int getViewTypeCount() {
    return DefaultAnyViewCenter.getInstance().getAnyViewCount();
  }

  /**
   * 预加载
   */
  public void setPreViewListMap(List<AnyBean> list) {
    if (list == null) {
      return;
    }
    AnyBean cardBean;
    List<SuperAnyView> viewList;
    for (int i = CONSTANTS_ONE; i < list.size(); i++) {
      cardBean = list.get(i);
      AnyViewBind anyViewBind = DefaultAnyViewCenter.getInstance().getAnyViewBind(cardBean.anyViewType);
      if (preViewListMap.containsKey(anyViewBind.viewClazz)) {
        viewList = preViewListMap.get(anyViewBind.viewClazz);
        if (viewList.size() < anyViewBind.preNum) {
          //反射注册一个View
          SuperAnyView cardView = getAnyView(anyViewBind.viewClazz.getName(), context);
          if (cardView != null) {
            viewList.add(cardView);
          }
        }
      } else if (anyViewBind.preNum > CONSTANTS_ONE) {
        viewList = new ArrayList<>();
        SuperAnyView cardView = getAnyView(anyViewBind.viewClazz.getName(), context);
        if (cardView != null) {
          viewList.add(cardView);
        }
        preViewListMap.put(anyViewBind.viewClazz, viewList);
      }
    }
  }

  /**
   *
   * @param className
   * @param context
   * @return
   */
  private SuperAnyView getAnyView(String className, Context context) {
    //反射注册一个View
    Object cardView = null;
    try {
      Class clazz = Class.forName(className);
      cardView = clazz.getConstructor(Context.class).newInstance(context);
    } catch (NoSuchMethodException e) {
      e.printStackTrace();
    } catch (InstantiationException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
    return (SuperAnyView) cardView;
  }

  private static final int CONSTANTS_ONE = 0;

  /**
   * 工厂方法，根据CardBean的viewType对应解析出CardView然后注入数据，最后返回CardView
   */
  @Override public View getView(final int position, View view, ViewGroup viewGroup) {
    final AnyBean item = (AnyBean) getItem(position);
    final AnyViewBind anyViewBind =
        DefaultAnyViewCenter.getInstance().getAnyViewBind(item.anyViewType);
    // 如果当前View是BaseCardView系列，并且不为null，则直接填充数据
    if (view instanceof SuperAnyView) {
      ((SuperAnyView<AnyBean>) view).fillViewWithData(item);
    } else if (view == null) { //如果当前View为空，则重新获取一个
      //从预加载的列表中获取View
      List<SuperAnyView> views = preViewListMap.get(anyViewBind.viewClazz);
      if (views != null && views.size() > CONSTANTS_ONE) {
        view = (View) preViewListMap.get(anyViewBind.viewClazz).get(CONSTANTS_ONE);
        views.remove(CONSTANTS_ONE);
      } else {
        //预加载列表中没有，则重新inflate一个
        view = (SuperAnyView) getAnyView(anyViewBind.viewClazz.getName(), context);
      }

      if (view != null) {
        ((SuperAnyView<AnyBean>) view).fillViewWithData(item);
      }
    } else {
      //其他View类型
      try {
        throw new Exception("cardAdapter can only process those view which extends BaseCardView!");
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    final View finalView = view;
    view.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (anyAdapterListener != null && position < list.size()) {
          anyAdapterListener.onViewPosition(finalView, position, list.get(position));
        }
      }
    });
    return view;
  }

  public void setAnyAdapterListener(AnyAdapterListener anyAdapterListener) {
    this.anyAdapterListener = anyAdapterListener;
  }

  public interface AnyAdapterListener {
    void onViewPosition(View view, int position, AnyBean cardBean);
  }
}
