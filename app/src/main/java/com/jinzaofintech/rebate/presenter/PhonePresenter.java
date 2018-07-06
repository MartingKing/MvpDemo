package com.jinzaofintech.rebate.presenter;

import com.jinzaofintech.commonlib.utils.ViewModule;
import com.jinzaofintech.rebate.bean.AccountInfo;
import com.jinzaofintech.rebate.bean.SettingInfo;


/**
 * account page
 */

public interface PhonePresenter {

    interface View extends ViewModule {
        void changeMobile(SettingInfo settingInfo);
    }

    interface Presenter {
        void getPhone();
    }
}
