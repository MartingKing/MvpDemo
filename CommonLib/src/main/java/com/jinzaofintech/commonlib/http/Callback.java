package com.jinzaofintech.commonlib.http;


import com.jinzaofintech.commonlib.app.BaseApplication;
import com.jinzaofintech.commonlib.bean.ApiRes;
import com.jinzaofintech.commonlib.bean.User;
import com.jinzaofintech.commonlib.http.utils.HttpException;
import com.jinzaofintech.commonlib.http.utils.HttpOnNextListener;
import com.jinzaofintech.commonlib.http.utils.NetWorkState;
import com.jinzaofintech.commonlib.utils.AesUtils;
import com.jinzaofintech.commonlib.utils.NetworkUtils;
import com.jinzaofintech.commonlib.utils.TextUtils;
import com.jinzaofintech.commonlib.utils.ToastUtils;
import com.jinzaofintech.commonlib.utils.Utils;
import com.jinzaofintech.commonlib.utils.ViewModule;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by zengwendi on 2017/5/10.
 * 网络请求回调
 */

public class Callback<T> implements Observer<T> {

    private ViewModule mViewModule;

    private HttpOnNextListener mListener;

    private Observable<T> mObservable;

    private Disposable mDisposable;

    public Disposable getDisposable() {
        return mDisposable;
    }

    public ViewModule getViewModule() {
        return mViewModule;
    }

    public HttpOnNextListener getListener() {
        return mListener;
    }

    public Callback(HttpOnNextListener listener) {
        mListener = listener;
    }

    public void setViewModule(ViewModule module) {
        this.mViewModule = module;
    }


    public Observable<T> getObservable() {
        return mObservable;
    }

    public void setObservable(Observable<T> observable) {
        mObservable = observable;
    }

    public void detachView() {
        if (mViewModule != null) {
            mViewModule = null;
        }
        if (mListener != null) {
            mListener = null;
        }
        if (mObservable != null) {
            mObservable = null;
        }
    }

    @Override
    public void onSubscribe(Disposable d) {
        if (mListener != null) {
            mListener.onStart();
        }
        mDisposable = d;
        if (mViewModule != null) {
            mViewModule.bindSubscription(d);
        }
        if (mViewModule != null && mListener != null && mListener.isLoadingDialog()) {//是否显示Loading
            mViewModule.showLoading();
        }
    }

    @Override
    public void onNext(T value) {
        if (value instanceof ApiRes) {
            onResponse((ApiRes<T>) value);
        } else {
            mListener.onError(new HttpException(HttpException.ErrorType.RETURN_ERROR));
            if (mListener.isShowToast() && mViewModule != null) {
                mViewModule.showToast("数据异常");
            }
            if (mListener.isErrorPage() && mViewModule != null) {
                mViewModule.setState(NetWorkState.STATE_ERROR);
            }
        }
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        onfail(e, "");
        if (mListener != null) {
            mListener.onComplete();
        }
        if (mViewModule != null && mListener != null && mListener.isLoadingDialog()) {//是否隐藏Loading
            mViewModule.hideLoading();
        }
    }

    @Override
    public void onComplete() {
        if (mListener != null) {
            mListener.onComplete();
        }
        if (mViewModule != null && mListener != null && mListener.isLoadingDialog()) {//是否隐藏Loading
            mViewModule.hideLoading();
        }
    }

    /**
     * 统一处理成功回掉
     */
    public void onResponse(ApiRes<T> res) {
        if (res == null) {//未知错误
            onfail(new Throwable(), res.getMessage());
            mListener.onError(new HttpException(HttpException.ErrorType.ERROR));
            return;
        }
        if (mListener.isCache()) {//缓存数据直接返回结果
            mListener.onCacheNext(res.getData());
            return;
        }
        String code = String.valueOf(res.getStatus_code());
        if (code.startsWith("4")) {//服务器返回的错误
            if (mViewModule != null && mListener.isShowToast()) {//服务端返回错误并且显示toast直接显示
                mViewModule.showToast(res.getMessage());
            }
            if (mViewModule != null && mListener.isErrorPage()) {//是否显示错误界面
                mViewModule.setState(NetWorkState.STATE_ERROR);
            }
            mListener.onError(new HttpException(HttpException.ErrorType.RETURN_ERROR));
            return;
        }
        if (res.getStatus_code() == 401) {//token过期
            mListener.onTokenError();
            mViewModule.againLogin();
            reLogin();
            return;
        }

        if (code.startsWith("2")) {//正常返回
            if (mViewModule != null && mListener.isErrorPage())
                mViewModule.setState(NetWorkState.STATE_SUCCESS);
            mListener.onNext(res.getData());  //网络数据
        }
    }

    public void onfail(Throwable e, String err) {
        String toastText;
        if (!NetworkUtils.isAvailableByPing()) {
            mListener.onError(new HttpException(HttpException.ErrorType.NETWORK_ERROR));
            toastText = "你连接的网络有问题，请检查网络连接状态";
        } else if (e instanceof retrofit2.HttpException) {
            mListener.onError(new HttpException(HttpException.ErrorType.ERROR));
            // TODO: 2018/6/21 调试阶段先弹出服务器返回的msg
            //toastText = "服务器异常"
            if (!TextUtils.isEmpty(err)) {
                toastText = err;
            } else {
                toastText = e.getMessage();
            }
        } else {
            mListener.onError(new HttpException(HttpException.ErrorType.ERROR));
            toastText = "数据异常";
        }
        if (mListener.isShowToast() && mViewModule != null) {
            mViewModule.showToast(toastText);
        }
        if (mListener.isErrorPage() && mViewModule != null) {
            mViewModule.setState(NetWorkState.STATE_ERROR);
        }
    }

    /**
     * 重新登陆
     */
    private void reLogin() {
        final User userold = BaseApplication.getInstance().getUser();
        if (userold == null) {
            if (mViewModule != null) {
                mViewModule.onHtppError(Callback.this);
            }
            return;
        }
        final String phone = userold.getPhone();
        final String pwd = AesUtils.decrypt(AesUtils.KEY, userold.getPassword());
        ApiManager.getInstence().getService().login(phone, pwd).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<ApiRes<User>>() {
            @Override
            public void onSubscribe(Disposable d) {
                mViewModule.bindSubscription(d);
            }

            @Override
            public void onNext(ApiRes<User> userApiRes) {
                if (String.valueOf(userApiRes.getStatus_code()).startsWith("2")) {
                    User user = userApiRes.getData();
                    user.setPhone(phone);
                    user.setPassword(userold.getPassword());
                    Utils.setUserInfo(user);
                    HttpUtils.invokeLoseTokenCallback();
                } else {
                    if (mViewModule != null) {
                        mViewModule.onHtppError(Callback.this);
                    }
                }

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                if (mViewModule != null) {
                    mViewModule.onHtppError(Callback.this);
                }
            }

            @Override
            public void onComplete() {

            }
        });
    }
}
