package com.jinzaofintech.commonlib.view.dialog

import android.app.Dialog
import android.content.Context
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import com.jinzaofintech.commonlib.R
import com.jinzaofintech.commonlib.utils.ScreenUtils
import kotlinx.android.synthetic.main.dialog_common.*


/**
 * Author : zhoujiulong
 * Email : 754667445@qq.com
 * Time : 2017/12/15
 * 描述 : 通用的 Dialog
 */
class CommonDialog private constructor(private val mContext: Context, private val mTitle: String, private val mCenterMsg: String, private val mListener: CommonDialogListener?)
    : Dialog(mContext, R.style.LibCommonDialog), View.OnClickListener {

    private var mLeftText = context.resources.getString(R.string.lib_cancel)
    private var mRightText = context.resources.getString(R.string.lib_sure)
    private var mTitleGravity: Int = Gravity.CENTER
    private var mIsClickDismiss = true
    private var mDescGravity: Int = Gravity.CENTER

    companion object {

        /**
         * 显示 Dialog
         *
         * @param listener CommonDialogListener 点击回调
         */
        fun build(context: Context, title: String, centerMsg: String, listener: CommonDialogListener): CommonDialog {
            return CommonDialog(context, title, centerMsg, listener)
        }
    }

    /**
     * 设置按钮文字
     */
    fun setText(leftText: String, rightText: String): CommonDialog {
        mLeftText = leftText
        mRightText = rightText
        return this
    }

    fun setTtitleGravity(gravity: Int): CommonDialog {
        mTitleGravity = gravity
        return this
    }


    /**
     * 设置dialog点击按钮不关闭
     */
    fun setOnClickDismiss(isClickDismiss:Boolean): CommonDialog {
        mIsClickDismiss = isClickDismiss
        return this
    }

    fun setDescGravity (gravity: Int): CommonDialog {
        mDescGravity = gravity
        return this
    }

    override fun show() {
        super.show()
        setContentView(R.layout.dialog_common)
        val window = window
        if (window != null) {
            val params = window.attributes
            params.width = (ScreenUtils.getScreenWidth(mContext) * 0.8).toInt()
            window.attributes = params
        }

        val tvTitle = tv_title
        val tvDesc = tv_desc
        val tvLeft = tv_left
        val tvRight = tv_right

        tvLeft.setOnClickListener(this)
        tvRight.setOnClickListener(this)

        // 设置txt数据，如果为空，说明不需要显示，则设置不可见
        tvTitle.text = mTitle
        tvTitle.visibility = if (TextUtils.isEmpty(mTitle)) View.GONE else View.VISIBLE
        tvTitle.gravity = mTitleGravity
        tvDesc.text = mCenterMsg

        tvLeft.text = mLeftText
        tvLeft.visibility = if (TextUtils.isEmpty(mLeftText)) View.GONE else View.VISIBLE

        tvRight.text = mRightText
        tvRight.visibility = if (TextUtils.isEmpty(mRightText)) View.GONE else View.VISIBLE
        view_center_line.visibility = if (TextUtils.isEmpty(mRightText)) View.GONE else View.VISIBLE
    }

    override fun onClick(v: View) {
        dismiss()
        val id = v.id
        if (id == R.id.tv_right) {//右侧按钮
            mListener?.rightClick()
        } else if (id == R.id.tv_left) {//左侧按钮
            mListener?.leftClick()
        }
    }

    /**
     * Dialog 点击回调
     */
    interface CommonDialogListener {
        fun leftClick()

        fun rightClick()
    }

}
