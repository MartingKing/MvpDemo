package com.jinzaofintech.rebate.presenter.impl;

import com.jinzaofintech.commonlib.bean.ApiRes;
import com.jinzaofintech.commonlib.http.Callback;
import com.jinzaofintech.commonlib.http.utils.HttpOnNextListener;
import com.jinzaofintech.commonlib.presenter.base.BasePresenter;
import com.jinzaofintech.rebate.bean.SmsCodeReponse;
import com.jinzaofintech.rebate.http.AppApiManager;
import com.jinzaofintech.rebate.presenter.ForgetPwdPresenter;

/**
 * 忘记密码
 */

public class ForgetPwdPresenterImpl extends BasePresenter<ForgetPwdPresenter.View> implements ForgetPwdPresenter.Presenter {

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
    public void doRequest(String vfkey, String cfcode, String password) {
        HttpOnNextListener<Object> listener = new HttpOnNextListener<Object>() {
            @Override
            public void onNext(Object info) {
                mView.setNewPwdSuccess(info);
            }
        };
        listener.setShowToast(true).setErrorPage(false);
        invoke(AppApiManager.getInstence().getService().forgetPwd(vfkey, cfcode, password), new Callback<ApiRes<Object>>(listener));
    }
}
