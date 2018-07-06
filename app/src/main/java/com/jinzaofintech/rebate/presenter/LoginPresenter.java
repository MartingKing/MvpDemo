package com.jinzaofintech.rebate.presenter;

import com.jinzaofintech.commonlib.bean.User;
import com.jinzaofintech.commonlib.utils.ViewModule;


/**
 * login
 */

public interface LoginPresenter {

    interface View extends ViewModule {
        void loginSuccess(User user);
    }

    interface Presenter {
        void login(String phone, String pwd);
    }
}
