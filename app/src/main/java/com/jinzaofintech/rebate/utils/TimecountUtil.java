package com.jinzaofintech.rebate.utils;

import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import com.jinzaofintech.commonlib.utils.ContextUtils;
import com.jinzaofintech.commonlib.utils.TextUtils;
import com.jinzaofintech.rebate.R;

/**
 * Created by Administrator on 2018/6/15.
 * 获取验证码倒计时工具类
 */

public class TimecountUtil extends CountDownTimer {
    /**
     * @param millisInFuture    The number of millis in the future from the call
     * to {@link #start()} until the countdown is done and {@link #onFinish()}
     * is called.
     * @param countDownInterval The interval along the way to receive
     * {@link #onTick(long)} callbacks.
     */
    private TextView mView;

    public TimecountUtil(long millisInFuture, long countDownInterval, TextView view) {
        super(millisInFuture, countDownInterval);
        this.mView = view;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        if (mView != null) {
            mView.setClickable(false);
            mView.setText(TextUtils.getThreeAppendText("", String.valueOf(millisUntilFinished / 1000), " s"));
            mView.setTextColor(ContextCompat.getColor(ContextUtils.getContext(), R.color.color999));
        }
    }

    @Override
    public void onFinish() {
        if (mView != null) {
            mView.setText("重新获取");
            mView.setTextColor(ContextCompat.getColor(ContextUtils.getContext(), R.color.red));
            mView.setClickable(true);
        }
    }
}
