package com.jinzaofintech.rebate.presenter.impl;

import com.jinzaofintech.commonlib.bean.ApiRes;
import com.jinzaofintech.commonlib.http.Callback;
import com.jinzaofintech.commonlib.http.utils.HttpOnNextListener;
import com.jinzaofintech.commonlib.presenter.base.BasePresenter;
import com.jinzaofintech.rebate.http.AppApiManager;
import com.jinzaofintech.rebate.presenter.WidthdrawPresenter;

/**
 * Widthdraw page
 */

public class WidthdrawPresenterImpl extends BasePresenter<WidthdrawPresenter.View> implements WidthdrawPresenter.Presenter {


    @Override
    public void doRequest(String money, String account, String aliname) {
        HttpOnNextListener<Object> listener = new HttpOnNextListener<Object>() {
            @Override
            public void onNext(Object info) {
                mView.widthdraw(info.toString());
            }
        };
        listener.setShowToast(true).setErrorPage(false);
        invoke(AppApiManager.getInstence().getService().widthdraw(money, account, aliname), new Callback<ApiRes<Object>>(listener));
    }
}
