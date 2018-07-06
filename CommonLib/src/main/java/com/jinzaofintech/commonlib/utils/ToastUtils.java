package com.jinzaofintech.commonlib.utils;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.jinzaofintech.commonlib.R;
import com.jinzaofintech.commonlib.view.TextViewIcon;

/**
 * <pre>
 *     author: Blankj
 *     time  : 2017/7/18
 *     desc  : 吐司相关工具类
 * </pre>
 */
public class ToastUtils {

    private static Toast mToast;

    public static final void showShortToast(CharSequence c) {
        if (mToast == null) {
            mToast = Toast.makeText(ContextUtils.getContext(), c, Toast.LENGTH_SHORT);
            mToast.setGravity(Gravity.CENTER, 0, 0);
        } else {
            mToast.setText(c);
        }
        mToast.show();
    }

    public static final void showLongToast(CharSequence c) {
        if (mToast == null) {
            mToast = Toast.makeText(ContextUtils.getContext(), c, Toast.LENGTH_LONG);
            mToast.setGravity(Gravity.CENTER, 0, 0);
        } else {
            mToast.setText(c);
        }
        mToast.show();
    }

    /**
     * 当前网络状况不佳，请稍后重试！
     */
    public static final void showNetErrorToast() {
        if (mToast == null) {
            mToast = Toast.makeText(ContextUtils.getContext(), "当前网络状况不佳，请稍后重试！", Toast.LENGTH_SHORT);
            mToast.setGravity(Gravity.CENTER, 0, 0);
        } else {
            mToast.setText("当前网络状况不佳，请稍后重试！");
        }
        mToast.show();
    }

    /**
     * 没有更多的数据
     */
    public static final void showNoneDataToast() {
        if (mToast == null) {
            mToast = Toast.makeText(ContextUtils.getContext(), "没有更多的数据", Toast.LENGTH_SHORT);
            mToast.setGravity(Gravity.CENTER, 0, 0);
        } else {
            mToast.setText("没有更多的数据");
        }
        mToast.show();
    }

    /**
     * 数据加载错误，请稍后重试；
     */
    public static final void showErrorToast() {
        if (mToast == null) {
            mToast = Toast.makeText(ContextUtils.getContext(), "数据加载错误，请稍后重试", Toast.LENGTH_SHORT);
            mToast.setGravity(Gravity.CENTER, 0, 0);
        } else {
            mToast.setText("数据加载错误，请稍后重试");
        }
        mToast.show();
    }

    /**
     * 服务器异常
     */
    public static final void showErrorServer() {
        if (mToast == null) {
            mToast = Toast.makeText(ContextUtils.getContext(), "服务器异常，请稍后重试", Toast.LENGTH_SHORT);
            mToast.setGravity(Gravity.CENTER, 0, 0);
        } else {
            mToast.setText("服务器异常，请稍后重试");
        }
        mToast.show();
    }

    /**
     * 成功提示
     */
    public static final void showSuccess(String msg) {
        View v = LayoutInflater.from(ContextUtils.getContext()).inflate(com.jinzaofintech.commonlib.R.layout.toast_common, null);
        Toast toast = new Toast(ContextUtils.getContext());
        TextViewIcon tvi = v.findViewById(R.id.tvi);
        tvi.setText("\ue608");
        TextView tvMsg = v.findViewById(R.id.tv_msg);
        tvMsg.setText(msg);
        tvMsg.setVisibility(android.text.TextUtils.isEmpty(msg) ? View.GONE : View.VISIBLE);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setView(v);
        toast.show();
    }

    /**
     * 失败提示
     */
    public static final void showFaild(String msg) {
        View v = LayoutInflater.from(ContextUtils.getContext()).inflate(com.jinzaofintech.commonlib.R.layout.toast_common, null);
        Toast toast = new Toast(ContextUtils.getContext());
        TextViewIcon tvi = v.findViewById(R.id.tvi);
        tvi.setText("\ue60b");
        TextView tvMsg = v.findViewById(R.id.tv_msg);
        tvMsg.setText(msg);
        tvMsg.setVisibility(android.text.TextUtils.isEmpty(msg) ? View.GONE : View.VISIBLE);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setView(v);
        toast.show();
    }

    /**
     * 警告提示
     */
    public static final void showWarning(String msg) {
        View v = LayoutInflater.from(ContextUtils.getContext()).inflate(com.jinzaofintech.commonlib.R.layout.toast_common, null);
        Toast toast = new Toast(ContextUtils.getContext());
        TextViewIcon tvi = v.findViewById(R.id.tvi);
        tvi.setText("\ue632");
        TextView tvMsg = v.findViewById(R.id.tv_msg);
        tvMsg.setText(msg);
        tvMsg.setVisibility(android.text.TextUtils.isEmpty(msg) ? View.GONE : View.VISIBLE);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setView(v);
        toast.show();
    }
}