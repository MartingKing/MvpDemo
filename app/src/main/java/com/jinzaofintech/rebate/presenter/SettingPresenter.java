package com.jinzaofintech.rebate.presenter;

import com.jinzaofintech.commonlib.utils.ViewModule;
import com.jinzaofintech.rebate.bean.SettingInfo;


/**
 * setting
 */

public interface SettingPresenter {

    interface View extends ViewModule {
        void exitApp(String msg);

        void settingInfo(SettingInfo info);
    }

    interface Presenter {
        void logout();

        void getSetingInfo();
    }
}
