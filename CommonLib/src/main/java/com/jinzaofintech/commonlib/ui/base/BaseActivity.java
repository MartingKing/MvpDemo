package com.jinzaofintech.commonlib.ui.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.FrameLayout;

import com.jinzaofintech.commonlib.R;
import com.jinzaofintech.commonlib.app.ActivityManager;
import com.jinzaofintech.commonlib.app.BaseApplication;
import com.jinzaofintech.commonlib.app.Constants;
import com.jinzaofintech.commonlib.bean.Event;
import com.jinzaofintech.commonlib.http.Callback;
import com.jinzaofintech.commonlib.http.HttpUtils;
import com.jinzaofintech.commonlib.http.utils.NetWorkState;
import com.jinzaofintech.commonlib.presenter.base.BasePresenter;
import com.jinzaofintech.commonlib.utils.EventBusUtil;
import com.jinzaofintech.commonlib.utils.LogUtils;
import com.jinzaofintech.commonlib.utils.ToastUtils;
import com.jinzaofintech.commonlib.utils.ViewModule;
import com.jinzaofintech.commonlib.view.LoadingPage;
import com.jinzaofintech.commonlib.view.dialog.LoadingDialog;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by zengwendi on 2017/8/7.
 * BaseActivity抽象类默认提供4个抽象方法
 * ①initPresenter()Presenter初始化
 * ②initData()初始化数据
 * ③reLoadData()加载数据数据
 * ④getLayoutId()网络请求成功加载布局
 * ⑤initView()加载状态成功之后设置数据
 * 继承SimpleFragment已经实现了RxBus的订阅与销毁
 * 通过泛型定义了BasePresenter
 * 实现了ViewModulew 能够显示Toast,dialog和bindSubscription绑定RxJava 通过生命周期对Rx进行销毁防止内存泄露；
 * 已经存在Toolbar默认状态为gone通过setTitle和setBack自动变成VISIBLE
 */
public abstract class BaseActivity<T extends BasePresenter> extends SimpleActivity implements ViewModule {
    protected T mPresenter;
    private static final String RX_TAG_LOGIN_ACTIVITY = "rx_tag_login_activity";
    protected Unbinder mBind;
    public LoadingPage mLoadingPage;
    private CompositeDisposable mCompositeDisposable;
    private LoadingDialog mLoadingDialog;
    /**
     * 是否需要登录
     */
    private boolean isNeedLogin = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isRegisterEventBus()) {
            EventBusUtil.register(this);
        }
        initPresenter();
        if (mPresenter != null) mPresenter.attachView(this);
        setContentView();
        initData();
        jumpInOrloadData();
    }

    /**
     * 跳转登录或加载数据
     */
    private void jumpInOrloadData() {
        if (isNeedLogin) {
            if (BaseApplication.getInstance().getUser() == null) {
                Intent intent = new Intent();
                intent.setClassName(this, Constants.LOGIN_CLASSNAME);
                LogUtils.e(this.getComponentName().getClassName());
                intent.putExtra(Constants.AGAIN_LOAD_KEY, this.getComponentName().getClassName());
                startActivity(intent);
            } else {
                loadData();
            }
        } else {
            loadData();
        }
    }

    /**
     * 设置布局文件,如果不使用 Loading 页面和 ToolBar
     * 子类重写该方法即可，同时设置
     */
    protected void setContentView() {
        setContentView(R.layout.base_default_con);
        if (mLoadingPage == null) {
            mLoadingPage = new LoadingPage(this) {
                @Override
                protected void initView() {
                    mBind = ButterKnife.bind(BaseActivity.this, contentView);
                    BaseActivity.this.initView();
                }

                @Override
                protected void reLoadData() {
                    BaseActivity.this.reLoadData();
                }

                @Override
                protected int getLayoutId() {
                    return BaseActivity.this.getLayoutId();
                }
            };
        }
        FrameLayout mFlBase = findViewById(R.id.fl_base);
        mFlBase.addView(mLoadingPage);
    }

    /**
     * ①
     * Presenter初始化
     */
    protected abstract void initPresenter();

    /**
     * ②
     * 初始化数据接收intent的数据
     * 设置ActionBar
     */
    protected abstract void initData();

    /**
     * ③
     * 根据网络获取的数据返回状态，每一个子类的获取网络返回的都不一样，所以要交给子类去完成
     * * 如果是静态页面不需要网络请求的在子类的loadData方法中添加以下2行即可
     * mLoadingPage.state =NetWorkState.STATE_SUCCESS;
     * mLoadingPage.showPage();
     * 或者调用setState(NetWorkState.STATE_SUCCESS)
     */
    protected abstract void loadData();

    /**
     * ④
     * 网络请求成功在去加载布局
     */
    public abstract int getLayoutId();

    /**
     * ⑤
     * 子类关于View的操作(如setAdapter)都必须在这里面，会因为页面状态不为成功，而binding还没创建就引用而导致空指针。
     * reLoadData()和initView只执行一次，如果有一些请求需要二次的不要放到loadData()里面。
     */
    protected abstract void initView();

    /**
     * 界面异常，点击重新获取数据
     */
    public void reLoadData() {
        loadData();
    }


    @Override
    public void showToast(CharSequence sequence) {
        ToastUtils.showShortToast(sequence);
    }

    /**
     * 显示加载的dialog
     */
    @Override
    public void showLoading() {
        if (mLoadingDialog == null) {
            mLoadingDialog = LoadingDialog.Companion.showLoading(this);
        }
        mLoadingDialog.show();

    }

    /**
     * 隐藏dialog
     */
    @Override
    public void hideLoading() {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
    }


    @Override
    public void setState(NetWorkState state) {
        if (mLoadingPage != null) {
            mLoadingPage.state = state;
            mLoadingPage.showPage();
        }
    }

    /**
     * 是否需要EventBus用订阅
     * 需要的话重新该方法并返回true
     *
     * @return
     */
    protected boolean isRegisterEventBus() {
        return false;
    }

    @Override
    public NetWorkState getState() {
        if (mLoadingPage != null) {
            return mLoadingPage.state;
        } else {
            return null;
        }
    }

    /**
     * 用于添加rx的监听的在onDestroy中记得关闭不然会内存泄漏。
     */
    @Override
    public void bindSubscription(Disposable disposable) {
        if (this.mCompositeDisposable == null) {
            this.mCompositeDisposable = new CompositeDisposable();
        }
        this.mCompositeDisposable.add(disposable);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mBind != null) {
            mBind.unbind();
        }
        if (this.mCompositeDisposable != null && !mCompositeDisposable.isDisposed()) {
            this.mCompositeDisposable.dispose();
        }
        if (mPresenter != null) {
            mPresenter.detachView();
        }
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
        if (isRegisterEventBus()) {
            EventBusUtil.unregister(this);
        }
        HttpUtils.cleanLoseTokenCallback();
    }

    /**
     * 网络错误回调
     *
     * @param callback 错误请求
     */
    @Override
    public void onHtppError(Callback callback) {
        for (Callback call : HttpUtils.mCallBacks) {
            if (call.getViewModule() != null && call.getListener() != null) {//
                if (call.getListener().isErrorPage()) {
                    call.getViewModule().setState(NetWorkState.STATE_ERROR);
                }
                if (call.getListener().isLoadingDialog()) {
                    call.getViewModule().hideLoading();
                }
            }
        }
        Class<?> loginActivity = null;
        try {
            loginActivity = Class.forName(Constants.LOGIN_CLASSNAME);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (loginActivity != null && !ActivityManager.getActivityManager().isActivity(loginActivity)) {//判断loginActivity不为null并且没有打开登录
            Intent intent = new Intent();
            intent.setClassName(this, Constants.LOGIN_CLASSNAME);
            intent.putExtra("rx_tag_story", RX_TAG_LOGIN_ACTIVITY);
            startActivity(intent);
        } else {

        }
    }

    // 广播接受者是否显示通知
    public BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(getString(R.string.login_success))) {
                for (Callback call : HttpUtils.mCallBacks) {
                    if (call.getViewModule() != null && call.getListener() != null) {//
                        if (call.getListener().isErrorPage()) {
                            call.getViewModule().setState(NetWorkState.STATE_LOADING);
                        }
                        if (call.getListener().isLoadingDialog()) {
                            call.getViewModule().showLoading();
                        }
                    }
                }
                HttpUtils.invokeLoseTokenCallback();
                abortBroadcast();
            }
        }
    };

    // 注册广播
    public void registerBoradcastReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(getString(R.string.login_success));
        this.registerReceiver(mBroadcastReceiver, filter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerBoradcastReceiver();//注册广播
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.unregisterReceiver(mBroadcastReceiver);//注销广播
    }

    /**
     * 重新登陆跳转登陆
     */
    @Override
    public void againLogin() {
        Intent intent = new Intent();
        intent.putExtra(Constants.IS_BACK_HOME, true);
        intent.setClassName(this, Constants.LOGIN_CLASSNAME);
        startActivity(intent);
        finish();
    }


    /**
     * 登陆成功之后加载数据
     *
     * @param event 判断是不是本页
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onStickyEventBusload(Event event) {
        if (event != null && event.getCode() == Event.EventCode.C && event.getData().equals(this.getComponentName().getClassName())) {
            loadData();
        }
    }

    /**
     * 设置页面是否需要登陆再请求数据加载数据
     * 改方法在initData中调用
     *
     * @param needLogin true需要登陆再去加载数据，否则直接加载数据
     */
    public void setNeedLogin(boolean needLogin) {
        isNeedLogin = needLogin;
    }
}
