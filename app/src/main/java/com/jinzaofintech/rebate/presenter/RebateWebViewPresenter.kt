package com.jinzaofintech.rebate.presenter

import com.jinzaofintech.commonlib.utils.ViewModule
import com.jinzaofintech.rebate.bean.RebateResult

/**
 * Created by zengwendi on 2018/6/5.
 */
interface RebateWebViewPresenter {
    interface View : ViewModule {
        fun onSuccess(rebateResult: RebateResult)
    }

    interface Presenter {
        fun postMoney(data: String, id: String)
    }
}