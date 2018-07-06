package com.jinzaofintech.rebate.presenter;

import com.jinzaofintech.commonlib.utils.ViewModule;
import com.jinzaofintech.rebate.bean.MessageInfo;

import java.util.List;


/**
 * FundermenterDetailPresenter
 */

public interface MyMsgPresenter {

    interface View extends ViewModule {
        void myMessage(List<MessageInfo> msginfo);
    }

    interface Presenter {
        void getMsg();
    }
}
