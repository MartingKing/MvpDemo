package com.jinzaofintech.rebate.presenter;

import com.jinzaofintech.commonlib.utils.ViewModule;
import com.jinzaofintech.rebate.bean.SmsCodeReponse;


/**
 * forget password
 */

public interface ForgetPwdPresenter {

    interface View extends ViewModule {
        void setNewPwdSuccess(Object info);

        void smsSendSuccess(SmsCodeReponse smsCodeReponse);
    }

    interface Presenter {
        void getSmsCode(String phone);

        void doRequest(String vfkey, String cfcode, String password);
    }
}
