package com.davidkuper.anyview.listview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.view.View;
import com.davidkuper.R;

/**
 * Created by malingyi on 2017/3/2.
 */

public class SecondHeadView extends View {
    private Bitmap endBitmap;

    public SecondHeadView(Context context, AttributeSet attrs,
                                        int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public SecondHeadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SecondHeadView(Context context) {
        super(context);
        init();
    }

    private void init() {
        endBitmap = Bitmap.createBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.pull_end_image_frame_05));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureWidth(widthMeasureSpec)*endBitmap.getHeight()/endBitmap.getWidth());
    }

    private int measureWidth(int widthMeasureSpec){
        int result = 0;
        int size = MeasureSpec.getSize(widthMeasureSpec);
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        }else {
            result = endBitmap.getWidth();
            if (mode == MeasureSpec.AT_MOST) {
                result = Math.min(result, size);
            }
        }
        return result;
    }
}
