package com.jinzaofintech.rebate.presenter;

import com.jinzaofintech.commonlib.utils.ViewModule;
import com.jinzaofintech.rebate.bean.FundermenterInfo;

import java.util.List;


/**
 * FundermenterDetailPresenter
 */

public interface FundermenterDetailPresenter {

    interface View extends ViewModule {
        void fundermenterInfo(List<FundermenterInfo> fundermenterInfo);
    }

    interface Presenter {
        void getFundermenterInfo();
    }
}
