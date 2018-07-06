package com.jinzaofintech.commonlib.presenter.base;


import android.util.Log;

import com.jinzaofintech.commonlib.app.BaseApplication;
import com.jinzaofintech.commonlib.bean.ApiRes;
import com.jinzaofintech.commonlib.bean.User;
import com.jinzaofintech.commonlib.http.ApiManager;
import com.jinzaofintech.commonlib.http.Callback;
import com.jinzaofintech.commonlib.http.HttpUtils;
import com.jinzaofintech.commonlib.http.utils.HttpOnNextListener;
import com.jinzaofintech.commonlib.http.utils.NetWorkState;
import com.jinzaofintech.commonlib.utils.AesUtils;
import com.jinzaofintech.commonlib.utils.LogUtils;
import com.jinzaofintech.commonlib.utils.TextUtils;
import com.jinzaofintech.commonlib.utils.Utils;
import com.jinzaofintech.commonlib.utils.ViewModule;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by zengwendi on 2017/5/10.
 */

public class BasePresenter<T extends ViewModule> {
    protected T mView;//指的是界面，也就是BaseFragment或者BaseActivity

    private Callback callback;

    public void attachView(ViewModule viewModule) {
        this.mView = (T) viewModule;
    }

    protected <T> void invoke(Observable<T> observable, Callback<T> callback) {
        this.callback = callback;
        HttpUtils.invoke(mView, observable, callback);
    }

    /**
     * 给子类检查返回集合是否为空
     *
     * @param list
     */
    public void checkState(List list) {
        if (list.size() == 0) {
            if (mView instanceof ViewModule)
                ((ViewModule) mView).setState(NetWorkState.STATE_EMPTY);
            return;
        }
    }

    public void detachView() {
        if (mView != null)
            mView = null;
        if (callback != null) {
            callback.detachView();
        }

    }

    /**
     * 提供方法取消网络请求进行取消订阅
     */
    public void disposeCallback() {
        if (callback != null && callback.getDisposable() != null && !callback.getDisposable().isDisposed()) {
            callback.getDisposable().dispose();
        }
    }

    //自动登录
    public void autoLogin() {
        final User userold = BaseApplication.getInstance().getUser();
        LogUtils.e("userold=="+userold);
        if (userold == null) {
            return;
        }
        final String phone = userold.getPhone();
//        final String pwd = AesUtils.decrypt(AesUtils.KEY, userold.getPassword());
        final String pwd = userold.getPassword();
        Log.e("DHD", "autoLogin: phone==" + phone + "\n autoLogin:pwd==" + pwd);
        if (!TextUtils.isEmpty(phone) && !TextUtils.isEmpty(pwd)) {
            HttpOnNextListener<User> listener = new HttpOnNextListener<User>() {
                @Override
                public void onNext(User user) {
                    user.setPhone(phone);
                    user.setPassword(pwd);
                    Utils.setUserInfo(user);
                }
            };
            listener.setLoadingDialog(true);
            listener.setErrorPage(false);
            listener.setShowToast(false);
            invoke(ApiManager.getInstence().getService().login(phone, pwd), new Callback<ApiRes<User>>(listener));
        }
    }
}
