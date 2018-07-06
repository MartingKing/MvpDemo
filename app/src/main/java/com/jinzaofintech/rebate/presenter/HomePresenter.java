package com.jinzaofintech.rebate.presenter;

import com.jinzaofintech.commonlib.utils.ViewModule;
import com.jinzaofintech.rebate.bean.HomeDataResponse;
import com.jinzaofintech.rebate.bean.RebateCash;


/**
 * 首页
 */

public interface HomePresenter {

    interface View extends ViewModule {
        void requestHomeData(HomeDataResponse homeDataResponse);

        void requestCash(RebateCash cash);

        void downloadHotPicSuccess(String msg);
    }


    interface Presenter {
        void downloadHotPic(String path, String imageName);

        void getHomeData();

        void getCash(String id);
    }
}
