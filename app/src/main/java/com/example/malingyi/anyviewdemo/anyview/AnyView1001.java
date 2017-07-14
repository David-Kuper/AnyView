package com.example.malingyi.anyviewdemo.anyview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.davidkuper.anyview.anybean.AnyBean;
import com.davidkuper.anyview.view.SuperAnyView;
import com.example.malingyi.anyviewdemo.R;
import com.example.malingyi.anyviewdemo.anyview.bean.AnyBean1001;

/**
 * Created by malingyi on 2017/3/28.
 */
public class AnyView1001 extends SuperAnyView<AnyBean<AnyBean1001>> {
  TextView name;
  AnyBean1001 anyBean1001;
  public AnyView1001(Context context) {
    this(context,null);
  }

  public AnyView1001(Context context, AttributeSet attrs) {
    this(context, attrs,0);
  }

  public AnyView1001(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override protected int getLayoutId() {
    return R.layout.anyview1001;
  }

  @Override protected void setData(AnyBean<AnyBean1001> anyBean) {
    if (anyBean == null){
      throw new NullPointerException("data is null");
    }
    if (anyBean.data == null){
      throw new NullPointerException("anyBean.data is null");
    }
    anyBean1001 = anyBean.data;
    name.setText(anyBean1001.name);
  }

  @Override protected void findView(ViewGroup view) {
    name = (TextView) view.findViewById(R.id.tv_anyview_1001);
  }
}
