package com.jinzaofintech.rebate.ui.activity;

import android.view.View;
import android.widget.TextView;

import com.jinzaofintech.commonlib.app.ActivityManager;
import com.jinzaofintech.commonlib.app.BaseApplication;
import com.jinzaofintech.commonlib.bean.User;
import com.jinzaofintech.commonlib.http.utils.NetWorkState;
import com.jinzaofintech.commonlib.ui.base.BaseActivity;
import com.jinzaofintech.commonlib.utils.Utils;
import com.jinzaofintech.rebate.R;
import com.jinzaofintech.rebate.app.AppActivityManager;
import com.jinzaofintech.rebate.bean.MyConstants;
import com.jinzaofintech.rebate.presenter.LoginPresenter;
import com.jinzaofintech.rebate.presenter.impl.LoginPresenterImpl;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/6/15.
 * register success ui
 */

public class RegistSuccessActivity extends BaseActivity<LoginPresenterImpl> implements LoginPresenter.View {

    @BindView(R.id.tv_amount)
    TextView mTvAmount;
    @BindView(R.id.tv_makesure)
    TextView mTvMakesure;
    private String userPhone, pwd;

    @Override
    protected void initPresenter() {
        mPresenter = new LoginPresenterImpl();
    }

    @Override
    protected void initData() {
        setActionBar("注册");
        userPhone = getIntent().getStringExtra(MyConstants.PHONENUM);
        pwd = getIntent().getStringExtra(MyConstants.PASSWORD);
    }

    @Override
    protected void loadData() {
        setState(NetWorkState.STATE_SUCCESS);
    }

    @Override
    public int getLayoutId() {
        return R.layout.act_layout_regist_successed;
    }

    @Override
    protected void initView() {
        mTvMakesure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.login(userPhone, pwd);
            }
        });
    }

    @Override
    public void loginSuccess(User user) {
        //注册成功 请求登录 然后跳转页面
        user.setPhone(userPhone);
        user.setPassword(pwd);
        Utils.setUserInfo(user);
        EventBus.getDefault().post(MyConstants.LOGIN2ACCOUNT);
        AppActivityManager.finishAllActivity();
    }
}
