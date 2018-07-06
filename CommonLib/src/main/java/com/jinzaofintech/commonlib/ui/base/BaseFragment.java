package com.jinzaofintech.commonlib.ui.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.jinzaofintech.commonlib.R;
import com.jinzaofintech.commonlib.app.ActivityManager;
import com.jinzaofintech.commonlib.app.Constants;
import com.jinzaofintech.commonlib.http.Callback;
import com.jinzaofintech.commonlib.http.HttpUtils;
import com.jinzaofintech.commonlib.http.utils.NetWorkState;
import com.jinzaofintech.commonlib.presenter.base.BasePresenter;
import com.jinzaofintech.commonlib.utils.EventBusUtil;
import com.jinzaofintech.commonlib.utils.ToastUtils;
import com.jinzaofintech.commonlib.utils.ViewModule;
import com.jinzaofintech.commonlib.view.LoadingPage;
import com.jinzaofintech.commonlib.view.dialog.LoadingDialog;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by zengwendi on 2017/8/7.
 * BaseFragment抽象类默认提供4个抽象方法
 * ①initPresenter()Presenter初始化
 * ②initData()初始化数据
 * ③reLoadData()加载数据数据
 * ④getLayoutId()网络请求成功加载布局
 * ⑤initView()加载状态成功之后设置数据
 * 继承SimpleFragment已经实现了RxBus的订阅与销毁
 * 通过泛型定义了BasePresenter
 * 实现了ViewModulew 能够显示Toast,dialog和bindSubscription绑定RxJava 通过生命周期对Rx进行销毁防止内存泄露
 * 已经存在Toolbar默认状态为gone通过setTitle和setBack自动变成VISIBLE
 */

public abstract class BaseFragment<T extends BasePresenter> extends SimpleFragment implements ViewModule {

    protected T mPresenter;
    protected boolean isFirst = true; //只加载一次界面
    protected Unbinder mBind;
    public LoadingPage mLoadingPage;
    private CompositeDisposable mCompositeDisposable;
    private boolean mIsLazy = true;     // 是否开启懒加载只针对viewpager有效
    private boolean isPrepared = false; //是否加载数据
    private FrameLayout mFlBase;
    private static final String RX_TAG_LOGIN_FRAGMET = "rx_tag_login_fragmet";
    private LoadingDialog mLoadingDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isRegisterEventBus()) {
            EventBusUtil.register(this);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setContentView(inflater, container);
        initData();
        isPrepared = true;
        onVisible();
        loadBaseData();
        return mRootView;
    }

    protected void setContentView(LayoutInflater inflater, @Nullable ViewGroup container) {
        mRootView = inflater.inflate(R.layout.base_default_con, container, false);
        if (mLoadingPage == null) {
            mLoadingPage = new LoadingPage(getContext()) {
                @Override
                protected void initView() {
                    if (isFirst) {
                        mBind = ButterKnife.bind(BaseFragment.this, contentView);
                        BaseFragment.this.initView();
                        isFirst = false;
                    }
                }

                @Override
                protected void reLoadData() {
                    BaseFragment.this.loadData();
                }

                @Override
                protected int getLayoutId() {
                    return BaseFragment.this.getLayoutId();
                }
            };
        }
        mFlBase = mRootView.findViewById(R.id.fl_base);
        mFlBase.addView(mLoadingPage);
    }

    protected void removeLodingPage() {
        if (mFlBase != null)
            mFlBase.removeView(mLoadingPage);
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
     * 设置Toast
     */
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
            mLoadingDialog = LoadingDialog.Companion.showLoading(getContext());
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

    @Override
    public NetWorkState getState() {
        if (mLoadingPage != null) {
            return mLoadingPage.state;
        } else {
            return NetWorkState.STATE_SUCCESS;
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
        //移除loadingview会导致承载butterknife的页面销毁，这里加个判断
        if (mLoadingPage != null && mLoadingPage.isActivated() && mBind != null) {
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
        //comtab和viewpager联动的情况，需要移除loadingview里面的contentview
        removeLodingPage();
    }

    /**
     * 普通fragment不能调用
     * 是否开启懒加载在initData()方法中调用openLazy()即可
     * 只针对viewpage有效
     */
    public void openLazy() {
        mIsLazy = false;
    }

    /**
     * 在这里实现Fragment数据的缓加载.
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {//fragment可见
            mIsLazy = true;
            onVisible();
        } else {//fragment不可见
            mIsLazy = false;
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    /**
     * 显示时加载数据,需要这样的使用
     * 注意声明 isPrepared，先初始化
     * 生命周期会先执行 setUserVisibleHint 再执行onActivityCreated
     * 在 onActivityCreated 之后第一次显示加载数据，只加载一次
     */
    protected void onVisible() {
        if (isFirst) {
            initPresenter();
            if (mPresenter != null) {
                mPresenter.attachView(this);
            }
        }
        loadBaseData();//根据获取的数据来调用showView()切换界面
    }

    //条件全部满足加载数据
    private void loadBaseData() {
        if (!mIsLazy || !isPrepared || !isFirst) {
            return;
        }
        loadData();
    }

    @Subscribe(tags = {@Tag(RX_TAG_LOGIN_FRAGMET)})
    public void loginSuccess() {//登录成功重新请求
        HttpUtils.invokeLoseTokenCallback();
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
            loginActivity = Class.forName("com.weiclicai.vc.ui.activity.common.LoginActivity");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (loginActivity != null && !ActivityManager.getActivityManager().isActivity(loginActivity)) {//判断loginActivity不为null并且没有打开登录
            Intent intent = new Intent();
            intent.setClassName(getContext(), loginActivity.getCanonicalName());
            intent.putExtra("rx_tag_story", RX_TAG_LOGIN_FRAGMET);
            startActivity(intent);
        } else {

        }
    }

    /**
     * 重新登陆跳转登陆
     */
    @Override
    public void againLogin() {
        Intent intent = new Intent();
        intent.putExtra(Constants.IS_BACK_HOME, true);
        intent.setClassName(getContext(), Constants.LOGIN_CLASSNAME);
        startActivity(intent);
        getActivity().finish();
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
}
