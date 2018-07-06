package com.jinzaofintech.rebate.view

import android.app.Dialog
import android.content.Context
import android.view.View
import android.view.WindowManager
import com.jinzaofintech.rebate.R
import kotlinx.android.synthetic.main.dialog_rebate_result.view.*

/**
 * Created by zengwendi on 2018/6/15.
 * 返现结果
 * @type 弹窗类型 1 查询失败 2金额不足
 */

class RebateResultDialog(context: Context, var content: String = "", private var type: Int, private var function: () -> Unit) : Dialog(context, R.style.LibCommonDialog) {

    private val view by lazy {
        View.inflate(context, R.layout.dialog_rebate_result, null)
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
        setShowLoad(type)


    }

    private fun setShowLoad(type: Int) {
        if (type == 1) {
            view.llPromptCon.visibility = View.GONE
            view.llAgainCon.visibility = View.VISIBLE
            view.tvMessage.text = content
            view.tviClose.setOnClickListener {
                dismiss()
            }
            view.tvAgainBtn.setOnClickListener {
                dismiss()
                function()
            }
        } else {
            view.llPromptCon.visibility = View.VISIBLE
            view.llAgainCon.visibility = View.GONE
            view.tvPrompt.text = content
            view.confirm.setOnClickListener {
                dismiss()
            }
        }
    }

}