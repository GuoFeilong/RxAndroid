package com.rxandroid.presenter.impl;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;

import com.rxandroid.entity.AppInfoEntity;
import com.rxandroid.presenter.AppInfoPresenter;
import com.rxandroid.view.AppInfoView;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * Created by jsion on 16/1/26.
 */
public class AppInfoPresenterImpl implements AppInfoPresenter {
    private AppInfoView appInfoView;
    private Context context;
    private Observable ob;
    private Subscriber subscriber;


    public AppInfoPresenterImpl(AppInfoView appInfoView, Context context) {
        this.appInfoView = appInfoView;
        this.context = context;
    }


    @Override
    public void getAppInfo(int type) {
        initRXJavaAppInfo(type).subscribeOn(Schedulers.io())// 获取数据在io线程,
                .observeOn(AndroidSchedulers.mainThread())//转换到UI线程.模拟线程切换
                .subscribe(new Subscriber<AppInfoEntity>() {
                    @Override
                    public void onCompleted() {
                        appInfoView.stopRefresh();
                        appInfoView.bindCompeled();
                    }

                    @Override
                    public void onError(Throwable e) {
                        appInfoView.stopRefresh4Error();
                    }

                    @Override
                    public void onNext(AppInfoEntity appInfoEntity) {
                        appInfoView.bindAppInfo(appInfoEntity);
                    }

                });
    }

    @Override
    public void getAppTakeInfo(int count) {
        initRXJavaAppInfo(-1).take(count)
                .subscribeOn(Schedulers.io())// 获取数据在io线程,
                .observeOn(AndroidSchedulers.mainThread())//转换到UI线程.模拟线程切换
                .subscribe(new Subscriber<AppInfoEntity>() {
                    @Override
                    public void onCompleted() {
                        appInfoView.stopRefresh();
                        appInfoView.bindCompeled();
                    }

                    @Override
                    public void onError(Throwable e) {
                        appInfoView.stopRefresh4Error();
                    }

                    @Override
                    public void onNext(AppInfoEntity appInfoEntity) {
                        appInfoView.bindAppInfo(appInfoEntity);
                    }

                });
    }

    @Override
    public void getAppTakeLastInfo(int count) {
        // 筛选最后几个的时候模拟环境耗时太大所以这里也不走耗时方法
        initRXJavaAppInfo(0).takeLast(count)
                .subscribeOn(Schedulers.io())// 获取数据在io线程,
                .observeOn(AndroidSchedulers.mainThread())//转换到UI线程.模拟线程切换
                .subscribe(new Subscriber<AppInfoEntity>() {
                    @Override
                    public void onCompleted() {
                        appInfoView.stopRefresh();
                        appInfoView.bindCompeled();
                    }

                    @Override
                    public void onError(Throwable e) {
                        appInfoView.stopRefresh4Error();
                    }

                    @Override
                    public void onNext(AppInfoEntity appInfoEntity) {
                        appInfoView.bindAppInfo(appInfoEntity);
                    }

                });

    }

    @Override
    public void getAppFilterInfo() {
        initRXJavaAppInfo(-1).filter(new Func1<AppInfoEntity, Boolean>() {
            @Override
            public Boolean call(AppInfoEntity appInfoEntity) {
                return appInfoEntity.getAppName().startsWith("c");
            }
        })
                .subscribeOn(Schedulers.io())// 获取数据在io线程,
                .observeOn(AndroidSchedulers.mainThread())//转换到UI线程.模拟线程切换
                .subscribe(new Subscriber<AppInfoEntity>() {
                    @Override
                    public void onCompleted() {
                        appInfoView.stopRefresh();
                        appInfoView.bindCompeled();
                    }

                    @Override
                    public void onError(Throwable e) {
                        appInfoView.stopRefresh4Error();
                    }

                    @Override
                    public void onNext(AppInfoEntity appInfoEntity) {
                        appInfoView.bindAppInfo(appInfoEntity);
                    }

                });
    }

    @Override
    public void scanAppInfo() {
        initRXJavaAppInfo(-1).scan(new Func2<AppInfoEntity, AppInfoEntity, AppInfoEntity>() {
            @Override
            public AppInfoEntity call(AppInfoEntity appInfoEntity, AppInfoEntity appInfoEntity2) {

                // 拿到两个app名字长度进行比较,按照长度排序
                if (appInfoEntity.getAppName().length() > appInfoEntity2.getAppName().length()) {
                    return appInfoEntity;
                } else {
                    return appInfoEntity2;
                }

            }
        })
                .distinct()//去重复
                .subscribeOn(Schedulers.io())// 获取数据在io线程,
                .observeOn(AndroidSchedulers.mainThread())//转换到UI线程.模拟线程切换
                .subscribe(subscriber);
    }

    @Override
    public void unSubscriberRX() {
        if (ob != null && subscriber != null) {
            ob.unsafeSubscribe(subscriber);
        }
    }


    /**
     * RXjava获取应用列表
     */
    private Observable<AppInfoEntity> initRXJavaAppInfo(final int type) {
        ob = Observable.create(new Observable.OnSubscribe<AppInfoEntity>() {
            @Override
            public void call(Subscriber<? super AppInfoEntity> subscriber) {

                ArrayList<AppInfoEntity> appList = new ArrayList<AppInfoEntity>();
                List<PackageInfo> packages = context.getPackageManager().getInstalledPackages(0);

                for (int i = 0; i < packages.size(); i++) {
                    PackageInfo packageInfo = packages.get(i);
                    if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                        // 系统应用
                    } else {
                        // 客户应用
                        AppInfoEntity tmpInfo = new AppInfoEntity();
                        tmpInfo.appName = packageInfo.applicationInfo.loadLabel(context.getPackageManager()).toString();
                        tmpInfo.packageName = packageInfo.packageName;
                        tmpInfo.versionName = packageInfo.versionName;
                        tmpInfo.versionCode = packageInfo.versionCode;
                        tmpInfo.appIcon = packageInfo.applicationInfo.loadIcon(context.getPackageManager());
                        appList.add(tmpInfo);

                        if (subscriber.isUnsubscribed()) {
                            return;
                        } else {
                            if (type == -1) {
                                // for debug 用来模拟网络环境加载延时.
                                try {
                                    Thread.sleep(50);
                                    subscriber.onNext(tmpInfo);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                subscriber.onNext(tmpInfo);
                            }

                        }
                    }
                }
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onCompleted();
                }
            }
        });

        subscriber = new Subscriber<AppInfoEntity>() {
            @Override
            public void onCompleted() {
                appInfoView.stopRefresh();
                appInfoView.bindCompeled();
            }

            @Override
            public void onError(Throwable e) {
                appInfoView.stopRefresh4Error();
            }

            @Override
            public void onNext(AppInfoEntity appInfoEntity) {
                appInfoView.bindAppInfo(appInfoEntity);
            }

        };

        return ob;
    }
}
