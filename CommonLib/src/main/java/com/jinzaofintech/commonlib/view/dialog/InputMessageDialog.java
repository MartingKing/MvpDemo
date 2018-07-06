package com.jinzaofintech.commonlib.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.jinzaofintech.commonlib.R;
import com.jinzaofintech.commonlib.view.ClearableEditText;

/**
 * Author : zhoujiulong
 * Email : 754667445@qq.com
 * Time : 2017/12/22
 * 描述 : 输入文本 Dialog
 */

public class InputMessageDialog extends Dialog implements View.OnClickListener {

    private String mTitleStr;
    private String mMsgStr;
    private String mLeftTextStr;
    private String mRightTextStr;
    private int mInputType = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL;
    private int mInputLength = -1;

    private ClearableEditText mCleInput;

    public static InputMessageDialog build(Context context) {
        return new InputMessageDialog(context);
    }

    private InputMessageDialog(@NonNull Context context) {
        super(context, R.style.LibCommonDialog);
    }

    public InputMessageDialog setTitle(String title) {
        mTitleStr = title;
        return this;
    }

    public InputMessageDialog setMessage (String msg) {
        mMsgStr = msg;
        return this;
    }

    public InputMessageDialog setLeftText(String leftStr) {
        mLeftTextStr = leftStr;
        return this;
    }

    public InputMessageDialog setRightText(String rightStr) {
        mRightTextStr = rightStr;
        return this;
    }

    public InputMessageDialog setInputType(int inputType) {
        mInputType = inputType;
        return this;
    }

    public InputMessageDialog setMaxLength(int maxLength) {
        mInputLength = maxLength;
        return this;
    }

    @Override
    public void show() {
        super.show();
        Window win = getWindow();
        if (win != null) {
            win.getDecorView().setPadding(0, 0, 0, 0);
            WindowManager.LayoutParams lp = win.getAttributes();
            lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.height = WindowManager.LayoutParams.MATCH_PARENT;
            win.setAttributes(lp);
            setCancelable(true);
            setCanceledOnTouchOutside(false);
        }

        setContentView(R.layout.dialog_input_message);

        TextView tvTitle = findViewById(R.id.tv_title);
        if (!TextUtils.isEmpty(mTitleStr)) tvTitle.setText(mTitleStr);
        TextView tvMsg = findViewById(R.id.tv_message);
        if (!TextUtils.isEmpty(mMsgStr)) {
            tvMsg.setVisibility(View.VISIBLE);
            tvMsg.setText(mMsgStr);
        } else {
            tvMsg.setVisibility(View.GONE);
        }

        mCleInput = findViewById(R.id.cet_input);
        mCleInput.setInputType(mInputType);
        if (mInputLength != -1) {
            mCleInput.setFilters(new InputFilter[]{new InputFilter.LengthFilter(mInputLength)});
        }

        TextView tvLeft = findViewById(R.id.tv_left);
        if (!TextUtils.isEmpty(mLeftTextStr)) tvLeft.setText(mLeftTextStr);
        tvLeft.setOnClickListener(this);

        TextView tvRight = findViewById(R.id.tv_right);
        if (!TextUtils.isEmpty(mRightTextStr)) tvRight.setText(mRightTextStr);
        tvRight.setOnClickListener(this);

        //弹出键盘
        mCleInput.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager m = (InputMethodManager) mCleInput.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (m != null) {
                    m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        }, 300);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_right) {
            if (mOnClickListener != null) {
                mOnClickListener.click(mCleInput.getText().toString().trim());
            }
        } else if (id==R.id.tv_left) {
            dismiss();
        }
    }

    private OnClickListener mOnClickListener;

    public InputMessageDialog setOnClickListener(OnClickListener listener) {
        mOnClickListener = listener;
        return this;
    }

    public interface OnClickListener {
        void click(String inputStr);
    }
}





















