package com.jinzaofintech.commonlib.app;

import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;
import android.support.multidex.MultiDex;

import com.jinzaofintech.commonlib.BuildConfig;
import com.jinzaofintech.commonlib.R;
import com.jinzaofintech.commonlib.bean.User;
import com.jinzaofintech.commonlib.utils.ContextUtils;
import com.jinzaofintech.commonlib.utils.SPUtils;
import com.jinzaofintech.commonlib.utils.UmengHelper;
import com.jinzaofintech.commonlib.view.smartrefresh.NormalRefreshHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreater;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import org.android.agoo.huawei.HuaWeiRegister;
import org.android.agoo.xiaomi.MiPushRegistar;

/**
 * Created by Administrator on 2017/11/27 0027.
 */

public class BaseApplication extends Application {
    public Typeface iconfont;
    public static BaseApplication mInstance;
    private User mUser;

    static {
        //初始化刷新控件 SmartRefreshLayout 构建器
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreater(new DefaultRefreshHeaderCreater() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(android.R.color.white, R.color.tv_normal);//全局设置主题颜色
                layout.setHeaderHeight(60);
                layout.setFooterHeight(40);
                layout.setDragRate(0.8F);
                layout.setEnableLoadmoreWhenContentNotFull(false);
                return new NormalRefreshHeader(context);
            }
        });
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        ContextUtils.init(this);
        initYouMengShare();
    }

    private void initYouMengShare() {
        UmengHelper.getInstance().init();
        MiPushRegistar.register(this, BuildConfig.XIAOMI_KEY, BuildConfig.XIAOMI_SECRET);//小米通道
        HuaWeiRegister.register(this);//华为通道
    }


    public Typeface getIconfont(Context context) {
        if (iconfont != null) {
            return iconfont;
        } else {
            iconfont = Typeface.createFromAsset(context.getAssets(), "iconfont.ttf");
        }
        return iconfont;
    }

    /**
     * 获取类实例对象
     *
     * @return MyApplication
     */
    public static BaseApplication getInstance() {
        if (mInstance != null && mInstance instanceof BaseApplication) {
            return mInstance;
        } else {
            mInstance = new BaseApplication();
            return mInstance;
        }
    }

    /**
     * 获取User
     *
     * @return
     */
    public User getUser() {
        if (mUser != null) {
            return mUser;
        } else {
            mUser = (User) SPUtils.getObjectValue(SPUtils.USER_KEY);
        }
        return mUser;
    }

    public void setUser(User user) {
        mUser = user;
    }
}
























