package com.jinzaofintech.rebate.presenter;

import com.jinzaofintech.commonlib.utils.ViewModule;
import com.jinzaofintech.rebate.bean.SettingInfo;
import com.jinzaofintech.rebate.bean.SmsCodeReponse;


/**
 * account page
 */

public interface ChangePhonePresenter {

    interface View extends ViewModule {
        void smsSendSuccess(SmsCodeReponse smsCodeReponse);

        void changeMobile(Object msg);
    }

    interface Presenter {
        void getSmsCode(String phone);

        void getPhone(String keyold, String vfcode, String keynew, String codenew);
    }
}
