package com.rxandroid.presenter;

/**
 * Created by jsion on 16/1/26.
 */
public interface AppInfoPresenter {
    /**
     * RxJAVA取所有数据
     */
    void getAppInfo(int type);

    /**
     * RxJAVA筛选取前几条数据
     * @param count
     */
    void getAppTakeInfo(int count);

    /**
     * RXjava筛选取最后几条数据
     * @param count
     */
    void getAppTakeLastInfo(int count);

    /**
     * RXjava筛选条件比如不是字母的所有app 或者以那个字母开头等各种条件
     */
    void getAppFilterInfo();

    /**
     * 来自网络文档RxJava的scan()函数可以看做是一个累加器函数。
     * scan()函数对原始Observable发射的每一项数据都应用一个函数，
     * 它将函数的结果填充回可观测序列，等待和下一次发射的数据一起使用。
     */
    void scanAppInfo();

    void unSubscriberRX();
}
