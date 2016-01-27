package com.rxandroid.ui;

import android.os.Bundle;
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

import com.rxandroid.R;
import com.rxandroid.base.BaseActivity;
import com.rxandroid.entity.AppInfoEntity;
import com.rxandroid.presenter.impl.AppInfoPresenterImpl;
import com.rxandroid.tools.T;
import com.rxandroid.view.AppInfoView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;


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

    TextView textView;

    private AppInfoPresenterImpl mAppInfoPresenter;
    private ArrayList<AppInfoEntity> mAppInfos;
    private AppInfoAdapter mAppInfoAdapter;
    private int mCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
        initEvent();
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
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
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
        if (mAppInfoPresenter!=null){
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
        }
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


}
