package com.jinzaofintech.rebate.presenter;

import com.jinzaofintech.commonlib.utils.ViewModule;
import com.jinzaofintech.rebate.bean.HomeDataResponse;


/**
 * Widthdraw
 */

public interface WidthdrawPresenter {

    interface View extends ViewModule {
        void widthdraw(String msg);
    }

    interface Presenter {
        void doRequest(String money, String account, String aliname);
    }
}
