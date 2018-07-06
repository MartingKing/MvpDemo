package com.jinzaofintech.rebate.presenter.impl

import com.jinzaofintech.commonlib.http.Callback
import com.jinzaofintech.commonlib.http.utils.HttpOnNextListener
import com.jinzaofintech.commonlib.presenter.base.BasePresenter
import com.jinzaofintech.rebate.bean.RebateResult
import com.jinzaofintech.rebate.http.AppApiManager
import com.jinzaofintech.rebate.presenter.RebateWebViewPresenter

/**
 * Created by zengwendi on 2018/6/5.
 */

class RebateWebViewPresenterImpl : BasePresenter<RebateWebViewPresenter.View>(), RebateWebViewPresenter.Presenter {


    override fun postMoney(data: String, id: String) {
        val listener = object : HttpOnNextListener<RebateResult>() {
            override fun onNext(rebateResult: RebateResult) {
                mView.onSuccess(rebateResult)
            }

            override fun onError(e: Throwable?) {
                mView.onSuccess(RebateResult("", "", 4, 1))
            }
        }
        listener.setShowToast(true).setErrorPage(false).isLoadingDialog = false
        invoke(AppApiManager.getInstence().service.postMoney(data, id), Callback(listener))
    }
}
