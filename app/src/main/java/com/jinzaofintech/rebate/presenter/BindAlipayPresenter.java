package com.jinzaofintech.rebate.presenter;

import com.jinzaofintech.commonlib.utils.ViewModule;


/**
 * BindAlipay
 */

public interface BindAlipayPresenter {

    interface View extends ViewModule {
        void bindInfo();
    }

    interface Presenter {
        void doBind(String alipay, String name);
    }
}
