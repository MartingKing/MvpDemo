package com.jinzaofintech.rebate.presenter.impl;

import com.jinzaofintech.commonlib.bean.ApiRes;
import com.jinzaofintech.commonlib.http.Callback;
import com.jinzaofintech.commonlib.http.utils.HttpOnNextListener;
import com.jinzaofintech.commonlib.presenter.base.BasePresenter;
import com.jinzaofintech.rebate.http.AppApiManager;
import com.jinzaofintech.rebate.presenter.BindAlipayPresenter;

/**
 * BindAlipay
 */

public class BindAlipayPresenterImpl extends BasePresenter<BindAlipayPresenter.View> implements BindAlipayPresenter.Presenter {


    @Override
    public void doBind(String alipay, String name) {
        HttpOnNextListener<Object> listener = new HttpOnNextListener<Object>() {
            @Override
            public void onNext(Object msg) {
                mView.bindInfo();
            }
        };
        listener.setShowToast(true).setErrorPage(false);
        invoke(AppApiManager.getInstence().getService().bindAlipay(alipay, name), new Callback<ApiRes<Object>>(listener));
    }
}
