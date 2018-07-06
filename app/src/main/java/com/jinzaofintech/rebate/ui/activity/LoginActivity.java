package com.jinzaofintech.rebate.ui.activity;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jinzaofintech.commonlib.app.ActivityManager;
import com.jinzaofintech.commonlib.bean.User;
import com.jinzaofintech.commonlib.http.utils.NetWorkState;
import com.jinzaofintech.commonlib.ui.base.BaseActivity;
import com.jinzaofintech.commonlib.utils.TextUtils;
import com.jinzaofintech.commonlib.utils.ToastUtils;
import com.jinzaofintech.commonlib.utils.Utils;
import com.jinzaofintech.commonlib.view.ClearableEditText;
import com.jinzaofintech.commonlib.view.TextViewIcon;
import com.jinzaofintech.rebate.R;
import com.jinzaofintech.rebate.app.AppActivityManager;
import com.jinzaofintech.rebate.bean.MyConstants;
import com.jinzaofintech.rebate.presenter.LoginPresenter;
import com.jinzaofintech.rebate.presenter.impl.LoginPresenterImpl;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/6/7.
 * login ui
 */

public class LoginActivity extends BaseActivity<LoginPresenterImpl> implements LoginPresenter.View {

    @BindView(R.id.edtAccount)
    ClearableEditText mEdtAccount;
    @BindView(R.id.edtPwd)
    EditText mEdtPwd;
    @BindView(R.id.icEye)
    TextViewIcon mIcEye;
    @BindView(R.id.tvLoiginBtn)
    TextView mTvLoiginBtn;
    @BindView(R.id.tvForgetPwd)
    TextView mTvForgetPwd;
    @BindView(R.id.tvRegisterBtn)
    TextView mTvRegisterBtn;

    private boolean isFromHome;
    private String phone, pwd;

    @Override
    protected void initPresenter() {
        mPresenter = new LoginPresenterImpl();
    }

    @Override
    protected void initData() {
        setTitle("登录");
        isFromHome = getIntent().getBooleanExtra(MyConstants.HOME2LOGIN, false);
    }

    @Override
    protected void loadData() {
        setState(NetWorkState.STATE_SUCCESS);
    }

    @Override
    public int getLayoutId() {
        return R.layout.act_layout_login;
    }

    @Override
    protected void initView() {
        onListenerBack(new OnBackClickListener() {
            @Override
            public void onBackClicked() {
                if (isFromHome) {
                    EventBus.getDefault().post(MyConstants.OTHERS2HOME);
                } else {
                    finish();
                }
            }
        });
        mTvRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
        mTvLoiginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phone = mEdtAccount.getText().toString().trim();
                pwd = mEdtPwd.getText().toString().trim();
                if (TextUtils.isEmpty(phone)) {
                    ToastUtils.showShortToast("手机号不能为空");
                    return;
                }
                if (TextUtils.isEmpty(pwd)) {
                    ToastUtils.showShortToast("密码不能为空");
                    return;
                }
                mPresenter.login(phone, pwd);
            }
        });
        mTvForgetPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ForgetPwdActivity.class));
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (isFromHome) {
            //返回首页
            EventBus.getDefault().post(MyConstants.OTHERS2HOME);
        }
    }

    @Override
    public void loginSuccess(User user) {
        user.setPhone(phone);
        user.setPassword(pwd);
        Utils.setUserInfo(user);
        EventBus.getDefault().post(MyConstants.LOGIN2ACCOUNT);
        AppActivityManager.finishAllActivity();
    }
}
