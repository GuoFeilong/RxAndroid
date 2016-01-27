package com.rxandroid.view;

import com.rxandroid.entity.AppInfoEntity;

public interface AppInfoView {

    void bindAppInfo(AppInfoEntity appInfoEntity);

    void bindCompeled();

    void stopRefresh();

    void stopRefresh4Error();

    void showLoading();

}
