package com.jinzaofintech.rebate.presenter.impl;

import com.jinzaofintech.commonlib.bean.ApiRes;
import com.jinzaofintech.commonlib.bean.User;
import com.jinzaofintech.commonlib.http.Callback;
import com.jinzaofintech.commonlib.http.utils.HttpOnNextListener;
import com.jinzaofintech.commonlib.presenter.base.BasePresenter;
import com.jinzaofintech.rebate.bean.RegistResponse;
import com.jinzaofintech.rebate.bean.SmsCodeReponse;
import com.jinzaofintech.rebate.http.AppApiManager;
import com.jinzaofintech.rebate.presenter.RegisterPresenter;

/**
 * 注册
 */

public class RegisterPresenterImpl extends BasePresenter<RegisterPresenter.View> implements RegisterPresenter.Presenter {

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
    public void doRegist(String vfcode, String vfkey, String pwd, String fdphone) {
        HttpOnNextListener<RegistResponse> listener = new HttpOnNextListener<RegistResponse>() {
            @Override
            public void onNext(RegistResponse response) {
                mView.registSuccess(response);
            }
        };
        listener.setShowToast(true).setErrorPage(false);
        invoke(AppApiManager.getInstence().getService().regist1(vfcode, vfkey, pwd, fdphone), new Callback<ApiRes<RegistResponse>>(listener));
    }
}
