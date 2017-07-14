package com.davidkuper.anyview.listview;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.davidkuper.R;
import com.davidkuper.anyview.utils.DensityUtil;

/**
 * Created by malingyi on 2017/3/2.
 */

public class BottomLoadingView extends RelativeLayout {
    RotateAnimation rotateAnimation;
    ImageView loadingImg;
    private static final int LAYOUT_WIDTH_HEIGHT = 50;
    public BottomLoadingView(Context context) {
        super(context);
        init(context);
    }

    public BottomLoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BottomLoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        setGravity(CENTER_HORIZONTAL);
        setBackgroundColor(Color.WHITE);

        loadingImg = new ImageView(context);
        loadingImg.setImageResource(R.mipmap.loading);
        RelativeLayout.LayoutParams params = new LayoutParams(
            DensityUtil.dp2px(context,LAYOUT_WIDTH_HEIGHT), DensityUtil.dp2px(context,LAYOUT_WIDTH_HEIGHT));

        addView(loadingImg,params);
    }

    public void startAnim(){
        if (rotateAnimation == null){
            rotateAnimation = (RotateAnimation) AnimationUtils.loadAnimation(getContext(), R.anim.loading_animation);
        }
        loadingImg.startAnimation(rotateAnimation);
    }

    public void stopAnim(){
        if (rotateAnimation == null){
            throw new NullPointerException("rotateAnimation is null");
        }
        rotateAnimation.cancel();
        rotateAnimation.reset();
    }
}
