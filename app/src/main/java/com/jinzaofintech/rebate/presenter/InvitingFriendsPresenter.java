package com.jinzaofintech.rebate.presenter;

import com.jinzaofintech.commonlib.utils.ViewModule;
import com.jinzaofintech.rebate.bean.InviteFriendsInfo;


/**
 * prize invited ui
 */

public interface InvitingFriendsPresenter {

    interface View extends ViewModule {
        void invitingFriendinfo(InviteFriendsInfo inviteFriendsInfo);
    }

    interface Presenter {
        void getRewardInvation();
    }
}
