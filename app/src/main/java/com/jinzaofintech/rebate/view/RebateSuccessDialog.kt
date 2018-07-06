package com.jinzaofintech.rebate.view

import android.app.Dialog
import android.content.Context
import android.text.Html
import android.view.View
import android.view.WindowManager
import com.jinzaofintech.rebate.R
import kotlinx.android.synthetic.main.dialog_rebate_success.view.*

/**
 * Created by zengwendi on 2018/6/15.
 * 返现成功dialog
 */
class RebateSuccessDialog(context: Context, var content: String = "", function: () -> Unit) : Dialog(context, R.style.LibCommonDialog) {

    private val view by lazy {
        View.inflate(context, R.layout.dialog_rebate_success, null)
    }

    override fun show() {
        super.show()
        val win = window
        if (win != null) {
            win.decorView.setPadding(0, 0, 0, 0)
            val lp = win.attributes
            lp.width = context.resources.getDimensionPixelOffset(R.dimen.dip540)
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT
            win.attributes = lp
            setCancelable(false)
            setCanceledOnTouchOutside(false)
        }

        setContentView(view)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            view.tvContent.text = Html.fromHtml(content, Html.FROM_HTML_MODE_COMPACT)
        } else {
            view.tvContent.text = Html.fromHtml(content)
        }
        view.confirm.setOnClickListener { dismiss() }

    }


}