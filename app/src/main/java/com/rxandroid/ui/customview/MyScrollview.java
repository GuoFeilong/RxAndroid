package com.rxandroid.ui.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

import com.rxandroid.tools.logger.Logger;

/**
 * Created by jsion on 16/1/27.
 */
public class MyScrollview extends ScrollView {

    public MyScrollview(Context context) {
        this(context, null);
    }

    public MyScrollview(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyScrollview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

    }

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                int sY = getScrollY();
                int h = getHeight();
                int scrollViewMeasuredHeight = getChildAt(0).getMeasuredHeight();
                if (sY == 0) {
                    Logger.e("滑动到了顶端 view.getScrollY()=" + sY);
                }
                if ((sY + h) == scrollViewMeasuredHeight) {
                    Logger.e("滑动到了底部 scrollY=" + sY + ">>>>height=" + h + ">>>>>>scrollViewMeasuredHeight=" + scrollViewMeasuredHeight);
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onTouchEvent(ev);

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }
}
