package com.davidkuper.anyview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by malingyi on 2017/3/1.
 */

public abstract class IViewGroup<T extends View, D extends Object> extends FrameLayout {
    protected T containerView;
    protected Context context;

    public IViewGroup(Context context) {
        this(context, null);
    }

    public IViewGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        containerView = initView(context);
        if (containerView != null) {
            addView(containerView);
        }
    }

    /**
     * CardView的构造,以及初始化（布局设置、属性设置等）
     * @param context
     * @return
     */
    protected abstract T initView(Context context);

    /**
     * 提供给CardView的接口，可以重新操作CardView
     * @param containerView
     */
    protected abstract void fillView(T containerView);
}
