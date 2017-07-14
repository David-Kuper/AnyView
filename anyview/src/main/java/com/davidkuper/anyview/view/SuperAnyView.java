package com.davidkuper.anyview.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.davidkuper.anyview.IViewGroup;

/**
 * Created by malingyi on 2017/3/1.
 */

public abstract class SuperAnyView<D> extends IViewGroup<FrameLayout, D> {
  FrameLayout anyViewT;  //anyView外层容器
  protected View view;
  public static final int CARD_PADDING = 5;
  public static final int CARD_ELEVATION = 5;
  public static final int CARD_MARGIN = 10;

  public SuperAnyView(Context context) {
    super(context);
  }

  public SuperAnyView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public SuperAnyView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  /**
   * 通过数据填充当前View
   */
  public void fillViewWithData(D anyBean) {
    if (null != anyBean) {
      setData(anyBean);
      fillView(containerView);
    }
  }

  @Override protected FrameLayout initView(Context context) {
    this.context = context;
    view = LayoutInflater.from(context).inflate(getLayoutId(), null, false);
    if (view != null) {
      if (view instanceof ViewGroup){
        findView((ViewGroup) view);
      }
      if (anyViewT == null) {
        anyViewT = getAnyViewT(context);
      }
      anyViewT.addView(view);
    }
    return anyViewT;
  }

  private FrameLayout getAnyViewT(Context context) {
    if (anyViewT == null) {
      anyViewT = new FrameLayout(context);
      FrameLayout.LayoutParams params =
          new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
              ViewGroup.LayoutParams.WRAP_CONTENT);
      params.setMargins(CARD_PADDING, CARD_MARGIN, CARD_PADDING, CARD_MARGIN);
      anyViewT.setLayoutParams(params);
    }
    return anyViewT;
  }

  /**
   * 子类可以选择覆盖这个接口，对containerView进行操作，进行视图的修改
   */
  @Override protected void fillView(FrameLayout containerView) {

  }

  /**
   * 获取子类View的LayoutID
   */
  protected abstract int getLayoutId();

  /**
   * 将数据装填至View
   */
  protected abstract void setData(D anyBean);

  /**
   * 提供findView的接口
   */
  protected abstract void findView(ViewGroup view);
}
