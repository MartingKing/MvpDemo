package com.jinzaofintech.rebate.presenter.impl;

import com.jinzaofintech.commonlib.bean.ApiRes;
import com.jinzaofintech.commonlib.http.Callback;
import com.jinzaofintech.commonlib.http.utils.HttpOnNextListener;
import com.jinzaofintech.commonlib.presenter.base.BasePresenter;
import com.jinzaofintech.rebate.bean.AccountInfo;
import com.jinzaofintech.rebate.http.AppApiManager;
import com.jinzaofintech.rebate.presenter.AccountPresenter;

/**
 * account page
 */

public class AccountPresenterImpl extends BasePresenter<AccountPresenter.View> implements AccountPresenter.Presenter {

    @Override
    public void getAccountInfo() {
        HttpOnNextListener<AccountInfo> listener = new HttpOnNextListener<AccountInfo>() {
            @Override
            public void onNext(AccountInfo info) {
                mView.accountinfo(info);
            }
        };
        listener.setShowToast(true).setErrorPage(false);
        invoke(AppApiManager.getInstence().getService().getAccountInfo(), new Callback<ApiRes<AccountInfo>>(listener));
    }
}
