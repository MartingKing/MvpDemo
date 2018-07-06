package com.jinzaofintech.rebate.presenter;

import com.jinzaofintech.commonlib.utils.ViewModule;
import com.jinzaofintech.rebate.bean.RegistResponse;
import com.jinzaofintech.rebate.bean.SmsCodeReponse;


/**
 * 注册
 */

public interface RegisterPresenter {

    interface View extends ViewModule {
        void smsSendSuccess(SmsCodeReponse smsCodeReponse);

        void registSuccess(RegistResponse registResponse);
    }

    interface Presenter {
        void getSmsCode(String phone);

        void doRegist(String vfcode, String vfkey, String pwd, String fdphone);
    }
}
