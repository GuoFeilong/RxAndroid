package com.rxandroid.ui.customview;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.rxandroid.R;

/**
 * Created by jsion on 16/3/7.
 */
public class EarView extends View {
    /**
     * 默认宽高
     */
    private static final int DE_WIDTH = 50;
    private static final int DE_HEIGHT = 20;

    /**
     * 填充的画笔
     */
    private Paint solidePaint;

    /**
     * 文字画笔
     */
    private TextPaint textPaint;

    /**
     * 填充,描边,文字颜色
     */
    private int solideColor, textColor;

    /**
     * 设置的文字内容
     */
    private String earText;

    /**
     * 文字大小
     */
    private int earTextSize;

    /**
     * 默认右边耳朵显示
     */
    private boolean isRight;
    private boolean isLeft;
    private boolean isBoth;
    private Resources resources;

    private int width;
    private int height;

    private int rectWidth, rectHeight, ovalRectWidth, ovalRectHeight;

    private int radio;
    private RectF solideRectF, strokeRectF, solideOvalRectF, strokeOvalRectF;


    public EarView(Context context) {
        this(context, null);
    }

    public EarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.earView, defStyleAttr, 0);
        resources = getResources();
        for (int i = 0; i < typedArray.getIndexCount(); i++) {
            int attr = typedArray.getIndex(i);
            switch (attr) {
                case R.styleable.earView_earLeft:
                    isLeft = typedArray.getBoolean(attr, false);
                    isRight = false;
                    isBoth = false;
                    break;
                case R.styleable.earView_earRight:
                    isLeft = false;
                    isBoth = false;
                    isRight = typedArray.getBoolean(attr, false);
                    break;
                case R.styleable.earView_earBoth:
                    isLeft = false;
                    isRight = false;
                    isBoth = typedArray.getBoolean(attr, false);
                    break;
                case R.styleable.earView_earSolideColor:
                    solideColor = typedArray.getColor(attr, Color.BLUE);
                    break;

                case R.styleable.earView_earTextColor:
                    textColor = typedArray.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.earView_earText:
                    earText = typedArray.getString(attr);
                    break;
                case R.styleable.earView_earTextSize:
                    earTextSize = typedArray.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 11, resources.getDisplayMetrics()));
                    break;


            }
        }
        typedArray.recycle();
        init();

    }

    private void init() {
        solidePaint = new Paint();
        solidePaint.setColor(solideColor);
        solidePaint.setAntiAlias(true);
        solidePaint.setStrokeCap(Paint.Cap.ROUND);
        solidePaint.setStrokeJoin(Paint.Join.ROUND);
        solidePaint.setStyle(Paint.Style.FILL_AND_STROKE);


        textPaint = new TextPaint();
        textPaint.setTextSize(earTextSize);
        textPaint.setColor(textColor);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setAntiAlias(true);
        textPaint.setStrokeCap(Paint.Cap.ROUND);
        textPaint.setStrokeJoin(Paint.Join.ROUND);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int widthSize, heightSize;
        if (widthMode == MeasureSpec.AT_MOST || widthMode == MeasureSpec.UNSPECIFIED) {
            widthSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DE_WIDTH, resources.getDisplayMetrics());
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY);
        }

        if (heightMode == MeasureSpec.AT_MOST || heightMode == MeasureSpec.UNSPECIFIED) {
            heightSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DE_HEIGHT, resources.getDisplayMetrics());
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        width = getWidth() - getPaddingRight() - getPaddingLeft();
        height = getHeight() - getPaddingTop() - getPaddingBottom();

        rectWidth = (width / 3) * 2;
        ovalRectWidth = width - rectWidth;

        rectHeight = height;
        ovalRectHeight = height;

        /**
         * 左边矩形范围
         */
        strokeRectF = new RectF(0, 0, rectWidth + ovalRectWidth / 2, rectHeight);

        /**
         * 右边矩形范围
         */
        strokeOvalRectF = new RectF(rectWidth, 0, width, ovalRectHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        setBackgroundColor(Color.RED);

        canvas.save();
        canvas.translate(-width / 2 + 30, 0);
        canvas.drawArc(new RectF(0, 0, width, height), -90, 180, true, solidePaint);
        canvas.restore();

    }
}
