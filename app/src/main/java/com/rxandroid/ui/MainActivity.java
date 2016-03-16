package com.rxandroid.ui;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.rxandroid.R;
import com.rxandroid.base.BaseActivity;
import com.rxandroid.entity.AppInfoEntity;
import com.rxandroid.presenter.impl.AppInfoPresenterImpl;
import com.rxandroid.tools.T;
import com.rxandroid.tools.logger.Logger;
import com.rxandroid.ui.customview.ChangeColorImageView;
import com.rxandroid.view.AppInfoView;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


public class MainActivity extends BaseActivity implements AppInfoView, SwipeRefreshLayout.OnRefreshListener, TextWatcher {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.sfl_load_data)
    SwipeRefreshLayout mRefreshLayout;
    @Bind(R.id.rcv_app_list)
    RecyclerView mAppList;
    @Bind(R.id.et_input_count)
    EditText mInputCount;
    @Bind(R.id.btn_take)
    Button mTake;
    @Bind(R.id.btn_take_last)
    Button mTakeLast;
    @Bind(R.id.btn_filter)
    Button mFilter;
    @Bind(R.id.btn_scan)
    Button mScan;
    @Bind(R.id.iv_test)
    ImageView imageView;
    @Bind(R.id.iv_test1)
    ImageView imageView1;
    @Bind(R.id.iv_test2)
    ImageView imageView2;
    @Bind(R.id.iv_test3)
    ImageView imageView3;
    @Bind(R.id.civ_change)
    ChangeColorImageView changeColorImageView;
    @Bind(R.id.civ_1)
    ChangeColorImageView changeColorImageView1;
    @Bind(R.id.civ_2)
    ChangeColorImageView changeColorImageView2;
    @Bind(R.id.civ_3)
    ChangeColorImageView changeColorImageView3;
    @Bind(R.id.civ_4)
    ChangeColorImageView changeColorImageView4;
    @Bind(R.id.civ_5)
    ChangeColorImageView changeColorImageView5;
    @Bind(R.id.civ_6)
    ChangeColorImageView changeColorImageView6;
    private AppInfoPresenterImpl mAppInfoPresenter;
    private ArrayList<AppInfoEntity> mAppInfos;
    private AppInfoAdapter mAppInfoAdapter;
    private int mCount;

    private Integer[] colors;
    private Integer[] colors2;

    public int test;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // for test
        MainActivity mainActivity = new MainActivity();
        mainActivity.test = 3;

        initView();
        initData();
        initEvent();
        String test = getNum("love23next234csdn3423javaeye");
        Logger.e("Test>>>" + test+">>>"+mainActivity.test);
    }

    /**
     * love23next234csdn3423javaeye
     *
     * @param source
     * @return
     */
    private String getNum(String source) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < source.length(); i++) {
            if (Character.isDigit(source.charAt(i))) {
                stringBuffer.append(source.charAt(i));
            } else {

                String temp = stringBuffer.toString();
                if (temp.length() - 1 >= 0) {
                    String c = temp.charAt(temp.length() - 1) + "";
                    if (!c.equals(" ")) {
                        stringBuffer.append(" ");
                    }
                }
            }
        }

        return stringBuffer.toString();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void initData() {
        mAppInfos = new ArrayList<>();
        mAppInfoPresenter = new AppInfoPresenterImpl(this, this);
        if (!TextUtils.isEmpty(mInputCount.getText().toString())) {
            mCount = Integer.parseInt(mInputCount.getText().toString());
        }

        colors = new Integer[]{getResources().getColor(R.color.color_orange),
                getResources().getColor(R.color.color_pink),
                getResources().getColor(R.color.color_purple),
                getResources().getColor(R.color.colorPrimary),
                getResources().getColor(R.color.common_text_color)};

        colors2 = new Integer[]{R.color.color_orange,
                R.color.color_pink,
                R.color.color_purple,
                R.color.colorPrimary,
                R.color.common_text_color};

    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        Glide.with(MainActivity.this)
                .load("http://nuuneoi.com/uploads/source/playstore/cover.jpg")
                .placeholder(R.mipmap.bg_de)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .crossFade()
                .into(imageView);

        Glide.with(MainActivity.this)
                .load("http://nuuneoi.com/uploads/source/playstore/cover.jpg")
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.mipmap.bg_de)
                .transform(new GlideRoundTransform(this, 20))
                .into(imageView1);

//        Glide.with(MainActivity.this)
//                .load("http://nuuneoi.com/uploads/source/playstore/cover.jpg")
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .placeholder(R.mipmap.bg_de)
//                .transform(new GlideCircleTransform(this))
//                .into(imageView2);


        Glide.with(MainActivity.this)
                .load("http://nuuneoi.com/uploads/source/playstore/cover.jpg")
                .asBitmap().centerCrop()
                .placeholder(R.mipmap.bg_de)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(new BitmapImageViewTarget(imageView) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(MainActivity.this.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        imageView2.setImageDrawable(circularBitmapDrawable);
                    }
                });

        Glide.with(MainActivity.this)
//                .load("http://ww2.sinaimg.cn/bmiddle/dc66b248jw1eznsz2nn2xg209607aqv5.gif")
                .load("http://s1.dwstatic.com/group1/M00/3E/BD/767a2ffb59b04d8ddba07a86650601bf.gif")
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.mipmap.bg_de)
                .transform(new GlideRoundTransform(this, 10))
                .into(imageView3);
    }

    @Override
    protected void initEvent() {
        setSupportActionBar(toolbar);
        mAppList.setHasFixedSize(true);
        mAppList.setLayoutManager(new LinearLayoutManager(this));
        mAppList.setItemAnimator(new DefaultItemAnimator());

        mAppInfoAdapter = new AppInfoAdapter();
        mAppInfoAdapter.setOnItemClickLitener(new OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                mAppInfoAdapter.addAppInfo(mAppInfos.get(position), position);
                T.show(MainActivity.this, "复制" + (position + 1) + "个元素", 0);
            }

            @Override
            public void onItemLongClick(View view, int position) {
                mAppInfoAdapter.removeData(position);
                T.show(MainActivity.this, "删除第" + (position + 1) + "个元素", 0);
            }
        });
        mAppList.setAdapter(mAppInfoAdapter);

        mRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary),
                getResources().getColor(android.R.color.holo_green_light),
                getResources().getColor(android.R.color.holo_orange_light),
                getResources().getColor(R.color.colorAccent));

        mRefreshLayout.setOnRefreshListener(this);
        mTake.setOnClickListener(this);
        mTakeLast.setOnClickListener(this);
        mFilter.setOnClickListener(this);
        mScan.setOnClickListener(this);

        mInputCount.addTextChangedListener(this);
        changeColorImageView.setOnClickListener(this);
        changeColorImageView1.setOnClickListener(this);
        changeColorImageView2.setOnClickListener(this);
        changeColorImageView3.setOnClickListener(this);
        changeColorImageView4.setOnClickListener(this);
        changeColorImageView5.setOnClickListener(this);
        changeColorImageView6.setOnClickListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (!TextUtils.isEmpty(s.toString())) {
            mCount = Integer.parseInt(s.toString());
        }
    }

    public interface OnItemClickLitener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    /**
     * 应用列表的适配器
     */
    class AppInfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private OnItemClickLitener mOnItemClickListener;

        public void setOnItemClickLitener(OnItemClickLitener onItemClickLitener) {
            this.mOnItemClickListener = onItemClickLitener;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            AppInfoVH appInfoVH = new AppInfoVH(LayoutInflater
                    .from(MainActivity.this)
                    .inflate(R.layout.item_appinfo, parent, false));
            return appInfoVH;
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
            AppInfoEntity currentAppInfo = mAppInfos.get(position);
            AppInfoVH appInfoVH = (AppInfoVH) holder;
            appInfoVH.appIcon.setImageDrawable(currentAppInfo.getAppIcon());
            appInfoVH.appName.setText(currentAppInfo.getAppName());
            appInfoVH.appPackName.setText(currentAppInfo.getPackageName());
            appInfoVH.appVersionName.setText(currentAppInfo.getVersionName());
            appInfoVH.appVersionCode.setText(currentAppInfo.getVersionCode() + "");


            // 如果设置了回调，则设置点击事件
            if (mOnItemClickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = holder.getLayoutPosition();
                        mOnItemClickListener.onItemClick(holder.itemView, pos);
                    }
                });

                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        int pos = holder.getLayoutPosition();
                        mOnItemClickListener.onItemLongClick(holder.itemView, pos);
                        return false;
                    }
                });
            }

        }

        @Override
        public int getItemCount() {
            return mAppInfos.size();
        }

        public void addAppInfo(AppInfoEntity appinfo, int position) {
            mAppInfos.add(appinfo);
            notifyItemInserted(position);
        }

        public void removeData(int position) {
            mAppInfos.remove(position);
            notifyItemRemoved(position);
        }

        class AppInfoVH extends RecyclerView.ViewHolder {
            @Bind(R.id.tv_item_appname)
            TextView appName;
            @Bind(R.id.tv_item_package_name)
            TextView appPackName;
            @Bind(R.id.tv_item_version_name)
            TextView appVersionName;
            @Bind(R.id.tv_item_version_code)
            TextView appVersionCode;
            @Bind(R.id.iv_app_icon)
            ImageView appIcon;

            public AppInfoVH(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mAppInfoPresenter != null) {
            mAppInfoPresenter.unSubscriberRX();
        }
    }

    @Override
    public void onClick(View v) {
        mAppInfos.clear();
        mAppInfoAdapter.notifyDataSetChanged();
        switch (v.getId()) {
            case R.id.btn_take:
                if (TextUtils.isEmpty(mInputCount.getText())) {
                    T.show(this, "请输入要TAKE的数量", 0);
                } else {
                    mAppInfoPresenter.getAppTakeInfo(mCount);
                }
                break;
            case R.id.btn_take_last:
                if (TextUtils.isEmpty(mInputCount.getText())) {
                    T.show(this, "请输入要TAKE_Last的数量", 0);
                } else {
                    mAppInfoPresenter.getAppTakeLastInfo(mCount);
                }
                break;
            case R.id.btn_filter:
                mAppInfoPresenter.getAppFilterInfo();
                break;
            case R.id.btn_scan:
                mAppInfoPresenter.scanAppInfo();
                break;
            case R.id.civ_1:
                changeColorImageView1.setShadowColor(getResources().getColor(R.color.dialog_text_color));
                changeActivity(SunBabyLoadingActivity.class);
                break;
            case R.id.civ_2:
                changeColorImageView2.setShadowColor(getResources().getColor(R.color.common_line));
                //rx等待十秒谈土司
                Observable.timer(10, TimeUnit.SECONDS)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<Long>() {
                            @Override
                            public void call(Long aLong) {
                                T.show(MainActivity.this, "====" + aLong, 0);
                            }
                        });
                break;
            case R.id.civ_3:
//                changeColorImageView3.setShadowColor(getResources().getColor(R.color.common_red_color));
                changeColorImageView3.setColors(colors);
                changeActivity(GlideTestActivity.class);
                break;
            case R.id.civ_4:
//                changeColorImageView4.setShadowColor(getResources().getColor(R.color.color_orange));
                changeColorImageView4.setColors(colors);

                break;
            case R.id.civ_5:
//                changeColorImageView5.setShadowColor(getResources().getColor(R.color.shake_color));
                changeColorImageView5.setColors(colors);

                break;
            case R.id.civ_6:
//                changeColorImageView6.setShadowColor(getResources().getColor(R.color.cardview_dark_background));
                changeColorImageView6.setColors(colors);

                break;
            case R.id.civ_change:
                changeColorImageView.setColors(colors);
//                changeColorsForver(colors);
//                changeColorsHandler(colors2);

                break;
        }
    }


    private void changeColorsHandler(final Integer[] colors) {

//        for (int i = 0; i < colors.length; i++) {
//            changeColorImageView.setShadowColor(getResources().getColor(colors[i]));
//        }


        Observable.interval(2, 2, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onCompleted() {
                        T.show(MainActivity.this, "onCompleted", 0);
                    }

                    @Override
                    public void onError(Throwable e) {
                        T.show(MainActivity.this, "onError>>>" + e.toString(), 0);

                    }

                    @Override
                    public void onNext(Long aLong) {
                        int i = aLong.intValue();
                        if (i == colors.length - 1) {
                            this.unsubscribe();
                            onCompleted();
                        }
                        changeColorImageView.setShadowColor(getResources().getColor(colors[i]));

                        T.show(MainActivity.this, "onNext>>>" + colors[i], 0);

                    }
                });
    }


    private <T> void changeActivity(Class<T> tClass) {
        Intent intent = new Intent(this, tClass);
        startActivity(intent);
    }

    /**
     * 循环设置颜色
     *
     * @param colors
     */
    private void changeColorsForver(final Integer[] colors) {
        Subscriber<Integer> subscriber = new Subscriber<Integer>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Integer integer) {
                changeColorImageView.setShadowColorP(integer);
                Logger.e("TAG___Main>>>>>" + integer);
            }
        };

        Observable<Integer> observable = Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                if (subscriber.isUnsubscribed()) {
                    return;
                } else {
                    for (int i = 0; i < colors.length; i++) {
                        try {
                            subscriber.onNext(colors[i]);
                            Logger.i("TAG_Main>>>>线程-----颜色:" + colors[i]);
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                }

                if (!subscriber.isUnsubscribed()) {
                    subscriber.onCompleted();
                }
            }
        });

        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        changeColorImageView.setShadowColorP(integer);
                        Logger.e("call>>>>>" + integer);
                    }
                });
    }

    @Override
    public void bindAppInfo(AppInfoEntity appInfoEntity) {
        mAppInfoAdapter.addAppInfo(appInfoEntity, mAppList.getChildCount());
    }

    @Override
    public void bindCompeled() {
        T.show(this, getResources().getString(R.string.rx_load_complete), 0);
    }


    @Override
    public void stopRefresh() {
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void stopRefresh4Error() {
        mRefreshLayout.setRefreshing(false);
        T.show(this, getResources().getString(R.string.rx_error), 0);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void onRefresh() {
        mAppInfos.clear();
        mAppInfoAdapter.notifyDataSetChanged();
        if (mAppInfoPresenter != null) {
            mAppInfoPresenter.unSubscriberRX();
            mAppInfoPresenter.getAppInfo(0);
        }
    }

    /**
     * Glide圆角处理
     */
    public class GlideRoundTransform extends BitmapTransformation {
        private float radius = 0f;

        public GlideRoundTransform(Context context) {
            this(context, 4);
        }

        public GlideRoundTransform(Context context, int dp) {
            super(context);
            this.radius = Resources.getSystem().getDisplayMetrics().density * dp;
        }

        @Override
        protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
            return roundCrop(pool, toTransform);
        }

        private Bitmap roundCrop(BitmapPool pool, Bitmap source) {
            if (source == null) return null;
            Bitmap result = pool.get(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
            if (result == null) {
                result = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
            }
            Canvas canvas = new Canvas(result);
            Paint paint = new Paint();
            paint.setShader(new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
            paint.setAntiAlias(true);
            RectF rectF = new RectF(0f, 0f, source.getWidth(), source.getHeight());
            canvas.drawRoundRect(rectF, radius, radius, paint);
            return result;
        }

        @Override
        public String getId() {
            return getClass().getName() + Math.round(radius);
        }
    }

    public class GlideCircleTransform extends BitmapTransformation {
        public GlideCircleTransform(Context context) {
            super(context);
        }

        protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
            return circleCrop(pool, toTransform);
        }

        private Bitmap circleCrop(BitmapPool pool, Bitmap source) {
            if (source == null) return null;
            int size = Math.min(source.getWidth(), source.getHeight());
            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;
            // TODO this could be acquired from the pool too
            Bitmap squared = Bitmap.createBitmap(source, x, y, size, size);
            Bitmap result = pool.get(size, size, Bitmap.Config.ARGB_8888);
            if (result == null) {
                result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
            }
            Canvas canvas = new Canvas(result);
            Paint paint = new Paint();
            paint.setShader(new BitmapShader(squared, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
            paint.setAntiAlias(true);
            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);
            Logger.e(x + "===" + y + "====" + r + "---" + result.getWidth() + "---" + result.getHeight());
            return result;
        }

        @Override
        public String getId() {
            return getClass().getName();
        }
    }

}
