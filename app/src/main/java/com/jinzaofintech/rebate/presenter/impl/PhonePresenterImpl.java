package com.jinzaofintech.rebate.presenter.impl;

import com.jinzaofintech.commonlib.bean.ApiRes;
import com.jinzaofintech.commonlib.http.Callback;
import com.jinzaofintech.commonlib.http.utils.HttpOnNextListener;
import com.jinzaofintech.commonlib.presenter.base.BasePresenter;
import com.jinzaofintech.rebate.bean.AccountInfo;
import com.jinzaofintech.rebate.bean.SettingInfo;
import com.jinzaofintech.rebate.http.AppApiManager;
import com.jinzaofintech.rebate.presenter.AccountPresenter;
import com.jinzaofintech.rebate.presenter.PhonePresenter;

/**
 * account page
 */

public class PhonePresenterImpl extends BasePresenter<PhonePresenter.View> implements PhonePresenter.Presenter {

    @Override
    public void getPhone() {
        HttpOnNextListener<SettingInfo> listener = new HttpOnNextListener<SettingInfo>() {
            @Override
            public void onNext(SettingInfo info) {
                mView.changeMobile(info);
            }
        };
        listener.setShowToast(true).setErrorPage(false);
        invoke(AppApiManager.getInstence().getService().changePhone(), new Callback<ApiRes<SettingInfo>>(listener));
    }
}
