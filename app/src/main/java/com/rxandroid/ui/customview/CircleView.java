package com.rxandroid.ui.customview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.BounceInterpolator;

import com.rxandroid.tools.logger.Logger;

/**
 * Created by jsion on 16/2/17.
 */
public class CircleView extends View {
    private static final int DE_VIEW_SIZE = 150;
    private static final int DE_CIRCLE_SIZE = (int) (DE_VIEW_SIZE * .8F) / 2;
    private static final int DE_P_WIDTH = 15;
    private static final int COMMPADING = 5;
    private Paint circlePaint;
    private int width;
    private int height;
    private int deCircleSize;
    public CircleView(Context context) {
        this(context, null);
    }

    public CircleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setBackgroundColor(Color.CYAN);
        initRes();
    }

    private void initRes() {
        circlePaint = new Paint();
        circlePaint.setColor(Color.RED);
        circlePaint.setAntiAlias(true);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setStrokeCap(Paint.Cap.ROUND);
        circlePaint.setStrokeJoin(Paint.Join.ROUND);
        circlePaint.setStrokeWidth(DE_P_WIDTH);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 获取测量模式

        int wMspec = MeasureSpec.getMode(widthMeasureSpec);
        int hMspec = MeasureSpec.getMode(heightMeasureSpec);


        int widthSize;
        int heightSize;

        // 如果测量模式不确定,走默认的值,修改测量模式为确定的值

        if (wMspec == MeasureSpec.UNSPECIFIED || wMspec == MeasureSpec.AT_MOST) {
            widthSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DE_CIRCLE_SIZE, getResources().getDisplayMetrics());
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY);
            deCircleSize = DE_CIRCLE_SIZE;
        }else {
            deCircleSize = getWidth()/2-DE_P_WIDTH-COMMPADING;
        }

        if (hMspec == MeasureSpec.UNSPECIFIED || hMspec == MeasureSpec.AT_MOST) {
            heightSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DE_CIRCLE_SIZE, getResources().getDisplayMetrics());
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize, heightMeasureSpec);
            deCircleSize = DE_CIRCLE_SIZE;
        }else {
            deCircleSize = getHeight()/2-DE_P_WIDTH-COMMPADING;
        }



        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        width = getWidth();
        height = getHeight();
        startCircleAnim();

        Logger.d("onLayout>>>W=" + width + ">>>H=" + height);
    }

    private void startCircleAnim() {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0f, 1f);
        valueAnimator.setDuration(2000);
        valueAnimator.setInterpolator(new BounceInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                postInvalidate();
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawCircle(canvas);
    }

    private void drawCircle(Canvas canvas) {
        canvas.drawCircle(width / 2, height / 2, deCircleSize, circlePaint);
    }
}

