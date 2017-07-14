package com.example.malingyi.anyviewdemo.anyview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.TextView;
import com.davidkuper.anyview.anybean.AnyBean;
import com.davidkuper.anyview.view.SuperAnyView;
import com.example.malingyi.anyviewdemo.R;
import com.example.malingyi.anyviewdemo.anyview.bean.AnyBean1002;

/**
 * Created by malingyi on 2017/3/28.
 */

public class AnyView1002 extends SuperAnyView<AnyBean<AnyBean1002>> {

  private TextView name;
  private AnyBean1002 anyBean1002;

  public AnyView1002(Context context) {
    super(context);
  }

  public AnyView1002(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public AnyView1002(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override protected int getLayoutId() {
    return R.layout.anyview1002;
  }

  @Override protected void setData(AnyBean<AnyBean1002> anyBean) {
    if (anyBean == null){
      throw new NullPointerException("anyBean is null");
    }
    if (anyBean.data == null){
      throw new NullPointerException("anyBean.data is null");
    }
    anyBean1002 = anyBean.data;
    name.setText(anyBean1002.name);
  }

  @Override protected void findView(ViewGroup view) {
    name = (TextView) view.findViewById(R.id.tv_anyview_1002);
  }
}
