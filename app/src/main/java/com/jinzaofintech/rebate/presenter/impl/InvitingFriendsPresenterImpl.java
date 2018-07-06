package com.jinzaofintech.rebate.presenter.impl;

import com.jinzaofintech.commonlib.bean.ApiRes;
import com.jinzaofintech.commonlib.http.Callback;
import com.jinzaofintech.commonlib.http.utils.HttpOnNextListener;
import com.jinzaofintech.commonlib.presenter.base.BasePresenter;
import com.jinzaofintech.rebate.bean.InviteFriendsInfo;
import com.jinzaofintech.rebate.http.AppApiManager;
import com.jinzaofintech.rebate.presenter.InvitingFriendsPresenter;

/**
 * prize invited ui
 */

public class InvitingFriendsPresenterImpl extends BasePresenter<InvitingFriendsPresenter.View> implements InvitingFriendsPresenter.Presenter {

    @Override
    public void getRewardInvation() {
        HttpOnNextListener<InviteFriendsInfo> listener = new HttpOnNextListener<InviteFriendsInfo>() {
            @Override
            public void onNext(InviteFriendsInfo inviteFriendsInfo) {
                mView.invitingFriendinfo(inviteFriendsInfo);
            }
        };
        listener.setShowToast(true).setErrorPage(false);
        invoke(AppApiManager.getInstence().getService().rewardInvatation(), new Callback<ApiRes<InviteFriendsInfo>>(listener));
    }
}
