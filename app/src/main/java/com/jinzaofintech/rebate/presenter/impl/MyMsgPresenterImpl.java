package com.jinzaofintech.rebate.presenter.impl;

import com.jinzaofintech.commonlib.bean.ApiRes;
import com.jinzaofintech.commonlib.http.Callback;
import com.jinzaofintech.commonlib.http.utils.HttpOnNextListener;
import com.jinzaofintech.commonlib.presenter.base.BasePresenter;
import com.jinzaofintech.rebate.bean.MessageInfo;
import com.jinzaofintech.rebate.http.AppApiManager;
import com.jinzaofintech.rebate.presenter.MyMsgPresenter;

import java.util.List;

/**
 * FundermenterDetailPresenterImpl
 */

public class MyMsgPresenterImpl extends BasePresenter<MyMsgPresenter.View> implements MyMsgPresenter.Presenter {


    @Override
    public void getMsg() {
        HttpOnNextListener<List<MessageInfo>> listener = new HttpOnNextListener<List<MessageInfo>>() {
            @Override
            public void onNext(List<MessageInfo> info) {
                mView.myMessage(info);
            }
        };
        listener.setShowToast(true).setErrorPage(false);
        invoke(AppApiManager.getInstence().getService().msgCenter(), new Callback<ApiRes<List<MessageInfo>>>(listener));
    }
}
