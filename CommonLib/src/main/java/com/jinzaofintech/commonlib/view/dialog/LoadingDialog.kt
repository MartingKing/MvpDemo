package com.jinzaofintech.commonlib.view.dialog

import android.app.Dialog
import android.content.Context
import com.jinzaofintech.commonlib.R

/**
 * Author : zhoujiulong
 * Email : 754667445@qq.com
 * Time : 2017/12/15
 * 描述 :
 */

class LoadingDialog private constructor(private val mContext: Context) : Dialog(mContext, R.style.CommonToast) {

    companion object {

        fun showLoading(context: Context): LoadingDialog {
            val loadingDialog = LoadingDialog(context)
            loadingDialog.show()
            loadingDialog.setCanceledOnTouchOutside(false)
            loadingDialog.setCanceledOnTouchOutside(false)
            loadingDialog.setCancelable(false)
            return loadingDialog
        }
    }

    override fun show() {
        super.show()
        setContentView(R.layout.dialog_loading)
    }

}

































