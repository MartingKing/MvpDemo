package com.jinzaofintech.rebate.presenter;

import com.jinzaofintech.commonlib.utils.ViewModule;
import com.jinzaofintech.rebate.bean.AccountInfo;


/**
 * account page
 */

public interface AccountPresenter {

    interface View extends ViewModule {
        void accountinfo(AccountInfo accountInfo);
    }

    interface Presenter {
        void getAccountInfo();
    }
}
