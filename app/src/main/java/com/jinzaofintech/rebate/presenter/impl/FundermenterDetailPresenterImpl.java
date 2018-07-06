package com.jinzaofintech.rebate.presenter.impl;

import com.jinzaofintech.commonlib.bean.ApiRes;
import com.jinzaofintech.commonlib.http.Callback;
import com.jinzaofintech.commonlib.http.utils.HttpOnNextListener;
import com.jinzaofintech.commonlib.presenter.base.BasePresenter;
import com.jinzaofintech.rebate.bean.FundermenterInfo;
import com.jinzaofintech.rebate.http.AppApiManager;
import com.jinzaofintech.rebate.presenter.FundermenterDetailPresenter;

import java.util.List;

/**
 * FundermenterDetailPresenterImpl
 */

public class FundermenterDetailPresenterImpl extends BasePresenter<FundermenterDetailPresenter.View> implements FundermenterDetailPresenter.Presenter {


    @Override
    public void getFundermenterInfo() {
        HttpOnNextListener<List<FundermenterInfo>> listener = new HttpOnNextListener<List<FundermenterInfo>>() {
            @Override
            public void onNext(List<FundermenterInfo> info) {
                mView.fundermenterInfo(info);
            }
        };
        listener.setShowToast(true).setErrorPage(false);
        invoke(AppApiManager.getInstence().getService().getFunderDetail(), new Callback<ApiRes<List<FundermenterInfo>>>(listener));
    }
}
