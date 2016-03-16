package com.rxandroid.ui.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.rxandroid.R;
import com.rxandroid.tools.logger.Logger;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class ChangeColorImageView extends ImageView {
    private Paint mPaint;
    private int iconColor;
    private Subscriber<Integer> subscriber;
    private Observable<Integer> observable;

    public ChangeColorImageView(Context context) {
        this(context, null);
    }

    public ChangeColorImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChangeColorImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ChangeColorImageView, defStyleAttr, 0);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.ChangeColorImageView_iconColor:
                    iconColor = a.getColor(attr, getResources().getColor(R.color.trans));
                    break;
            }
        }
        a.recycle();
        init();
    }


    private void init() {
        mPaint = new Paint();
        mPaint.setColor(iconColor);
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //获取当前控件的drawable
        if (iconColor == getResources().getColor(R.color.trans)) {
            return;
        } else {
            Drawable drawable = getBackground();

            if (drawable == null) {
                return;
            }
            if (getHeight() == 0 || getWidth() == 0) {
                return;
            }
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            int saveFlags = Canvas.MATRIX_SAVE_FLAG | Canvas.CLIP_SAVE_FLAG | Canvas.HAS_ALPHA_LAYER_SAVE_FLAG | Canvas.FULL_COLOR_LAYER_SAVE_FLAG | Canvas.CLIP_TO_LAYER_SAVE_FLAG;
            canvas.saveLayer(0, 0, getWidth(), getHeight(), null, saveFlags);
            canvas.drawRect(0, 0, getWidth(), getHeight(), mPaint);

            /**
             * 画笔的模式,
             */
            PorterDuffXfermode xfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_ATOP);
            mPaint.setXfermode(xfermode);
            float scaleWidth = ((float) getWidth()) / bitmap.getWidth();
            float scaleHeight = ((float) getHeight()) / bitmap.getHeight();

            Matrix matrix = new Matrix();
            matrix.postScale(scaleWidth, scaleHeight);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            canvas.drawBitmap(bitmap, 0, 0, mPaint);
            canvas.restore();

            if (subscriber != null && !subscriber.isUnsubscribed()) {
                observable.unsafeSubscribe(subscriber);
            }

        }

    }

    /**
     * 设置icon的颜色
     *
     * @param color
     */
    public void setShadowColor(int color) {
        mPaint.setColor(color);
        invalidate();
    }

    /**
     * 设置icon的颜色
     *
     * @param color
     */
    public void setShadowColorP(int color) {
        mPaint.setColor(color);
        postInvalidate();
    }

    public void setColors(final Integer[] colors) {
        Observable.interval(2, 2, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onCompleted() {
                        Logger.d("间隔>>subscriber>>>onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.d("间隔>>>onError>>>" + e.toString());
                    }

                    @Override
                    public void onNext(Long aLong) {
                        int i = aLong.intValue();
                        if (i == colors.length - 1) {
                            this.unsubscribe();
                            onCompleted();
                        }

                        setShadowColorP(colors[i]);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Logger.e("间隔>>>onNext>>>" + colors[i] + "====" + simpleDateFormat.format(new Date()));
                    }
                });


//        observable = Observable.create(new Observable.OnSubscribe<Integer>() {
//            @Override
//            public void call(Subscriber<? super Integer> subscriber) {
//                if (subscriber.isUnsubscribed()) {
//                    return;
//                } else {
//                    for (int i = 0; i < colors.length; i++) {
//
//                        try {
//
//                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                            subscriber.onNext(colors[i]);
//                            Logger.i("线程-----颜色:" + colors[i] + ">>>>" + simpleDateFormat.format(new Date()));
////                            Thread.sleep(2000);
//
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//
//
//                    }
//
//                }
//
//                if (!subscriber.isUnsubscribed()) {
//                    subscriber.onCompleted();
//                }
//            }
//        });
//
//        subscriber = new Subscriber<Integer>() {
//            @Override
//            public void onCompleted() {
//                Logger.d("subscriber>>>onCompleted");
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                Logger.d("onError>>>" + e.toString());
//            }
//
//            @Override
//            public void onNext(Integer integer) {
//                setShadowColorP(integer);
//                Logger.e("onNext>>>" + integer);
//            }
//        };
//
//
//        observable.subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(subscriber);

    }
}
