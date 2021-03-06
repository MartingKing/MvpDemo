package com.jinzaofintech.rebate.presenter.impl;

import com.jinzaofintech.commonlib.bean.ApiRes;
import com.jinzaofintech.commonlib.http.Callback;
import com.jinzaofintech.commonlib.http.utils.HttpOnNextListener;
import com.jinzaofintech.commonlib.presenter.base.BasePresenter;
import com.jinzaofintech.rebate.bean.SmsCodeReponse;
import com.jinzaofintech.rebate.http.AppApiManager;
import com.jinzaofintech.rebate.presenter.ChangePhonePresenter;

/**
 * account page
 */

public class ChangePhonePresenterImpl extends BasePresenter<ChangePhonePresenter.View> implements ChangePhonePresenter.Presenter {

    @Override
    public void getSmsCode(String phone) {
        HttpOnNextListener<SmsCodeReponse> listener = new HttpOnNextListener<SmsCodeReponse>() {
            @Override
            public void onNext(SmsCodeReponse smsCodeReponse) {
                mView.smsSendSuccess(smsCodeReponse);
            }
        };
        listener.setShowToast(true).setErrorPage(false);
        invoke(AppApiManager.getInstence().getService().getSmsCode(phone), new Callback<ApiRes<SmsCodeReponse>>(listener));
    }

    @Override
    public void getPhone(String keyold, String vfcode, String keynew, String codenew) {
        HttpOnNextListener<Object> listener = new HttpOnNextListener<Object>() {
            @Override
            public void onNext(Object info) {
                mView.changeMobile(info.toString());
            }
        };
        listener.setShowToast(true).setErrorPage(false);
        invoke(AppApiManager.getInstence().getService().changePhoByOldPhone(keyold, vfcode, keynew, codenew), new Callback<ApiRes<Object>>(listener));
    }
}
