package com.jinzaofintech.rebate.presenter.impl;

import com.jinzaofintech.commonlib.bean.ApiRes;
import com.jinzaofintech.commonlib.bean.User;
import com.jinzaofintech.commonlib.http.Callback;
import com.jinzaofintech.commonlib.http.utils.HttpOnNextListener;
import com.jinzaofintech.commonlib.presenter.base.BasePresenter;
import com.jinzaofintech.rebate.http.AppApiManager;
import com.jinzaofintech.rebate.presenter.LoginPresenter;

/**
 * 登录
 */

public class LoginPresenterImpl extends BasePresenter<LoginPresenter.View> implements LoginPresenter.Presenter {

    @Override
    public void login(String phone, String pwd) {
        HttpOnNextListener<User> listener = new HttpOnNextListener<User>() {
            @Override
            public void onNext(User user) {
                mView.loginSuccess(user);
            }
        };
        listener.setShowToast(true).setErrorPage(false);
        invoke(AppApiManager.getInstence().getService().login(phone, pwd), new Callback<ApiRes<User>>(listener));
    }
}
