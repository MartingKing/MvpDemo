package com.jinzaofintech.rebate.presenter.impl;

import com.jinzaofintech.commonlib.bean.ApiRes;
import com.jinzaofintech.commonlib.http.Callback;
import com.jinzaofintech.commonlib.http.utils.HttpOnNextListener;
import com.jinzaofintech.commonlib.presenter.base.BasePresenter;
import com.jinzaofintech.rebate.bean.SettingInfo;
import com.jinzaofintech.rebate.http.AppApiManager;
import com.jinzaofintech.rebate.presenter.SettingPresenter;

/**
 * setting
 */

public class SettingPresenterImpl extends BasePresenter<SettingPresenter.View> implements SettingPresenter.Presenter {

    @Override
    public void logout() {
        HttpOnNextListener<Object> listener = new HttpOnNextListener<Object>() {
            @Override
            public void onNext(Object msg) {
                mView.exitApp(msg.toString());
            }
        };
        listener.setShowToast(true).setErrorPage(false);
        invoke(AppApiManager.getInstence().getService().logout(), new Callback<ApiRes<Object>>(listener));
    }

    @Override
    public void getSetingInfo() {
        HttpOnNextListener<SettingInfo> listener = new HttpOnNextListener<SettingInfo>() {
            @Override
            public void onNext(SettingInfo info) {
                mView.settingInfo(info);
            }
        };
        listener.setShowToast(true).setErrorPage(false);
        invoke(AppApiManager.getInstence().getService().setting(), new Callback<ApiRes<SettingInfo>>(listener));
    }
}
