package com.jinzaofintech.commonlib.utils;

import com.jinzaofintech.commonlib.http.Callback;
import com.jinzaofintech.commonlib.http.utils.NetWorkState;

import io.reactivex.disposables.Disposable;

/**
 * Created by zengwendi on 2017/9/25.
 * 通用viewModule
 */

public interface ViewModule {
    /**
     * 显示toast
     *
     * @param sequence
     */
    void showToast(CharSequence sequence);

    /**
     * 显示loading
     */
    void showLoading();

    /**
     * 隐藏loading
     */
    void hideLoading();

    /**
     * 控制显示状态
     *
     * @param state
     */
    void setState(NetWorkState state);

    /**
     * 获取当前状态
     *
     * @return
     */
    NetWorkState getState();

    /**
     * 绑定Disposable用于销毁
     *
     * @param disposable
     */
    void bindSubscription(Disposable disposable);

    /**
     * View的错误回调主要用在token过期重新登录
     */
    void onHtppError(Callback callback);

    /**
     * 重新跳转登陆界面
     */
    void againLogin();
}
