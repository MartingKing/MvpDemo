package com.jinzaofintech.rebate.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jinzaofintech.commonlib.http.utils.NetWorkState;
import com.jinzaofintech.commonlib.ui.base.BaseActivity;
import com.jinzaofintech.commonlib.utils.TextUtils;
import com.jinzaofintech.commonlib.utils.Utils;
import com.jinzaofintech.rebate.R;
import com.jinzaofintech.rebate.app.AppActivityManager;
import com.jinzaofintech.rebate.bean.MyConstants;
import com.jinzaofintech.rebate.bean.SettingInfo;
import com.jinzaofintech.rebate.presenter.SettingPresenter;
import com.jinzaofintech.rebate.presenter.impl.SettingPresenterImpl;
import com.jinzaofintech.rebate.utils.DialogUtil;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/6/7.
 */

public class SettingActivity extends BaseActivity<SettingPresenterImpl> implements SettingPresenter.View, View.OnClickListener {

    @BindView(R.id.rl_zhifubao)
    RelativeLayout mRlZhifubao;
    @BindView(R.id.tv_p)
    TextView mTvP;
    @BindView(R.id.tv_phone_num)
    TextView mTvPhoneNum;
    @BindView(R.id.rl_phone)
    RelativeLayout mRlPhone;
    @BindView(R.id.rl_login)
    RelativeLayout mRlLogin;
    @BindView(R.id.tvLogout)
    TextView mTvLogout;
    @BindView(R.id.tv_ali_account)
    TextView mTvAccount;
    @BindView(R.id.tv_tobind)
    TextView mTvTobind;

    @Override
    protected void initPresenter() {
        mPresenter = new SettingPresenterImpl();
    }

    @Override
    protected void initData() {
        setActionBar("设置");
    }

    @Override
    protected void loadData() {
        setState(NetWorkState.STATE_SUCCESS);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.getSetingInfo();
    }

    @Override
    public int getLayoutId() {
        return R.layout.act_layout_setting;
    }

    @Override
    protected void initView() {
        mRlZhifubao.setOnClickListener(this);
        mRlPhone.setOnClickListener(this);
        mRlLogin.setOnClickListener(this);
        mTvLogout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_zhifubao:
                startActivity(new Intent(this, ChangeZFBAccountActivity.class));
                break;
            case R.id.rl_phone:
                startActivity(new Intent(this, ChangePhoneActivity.class));
                break;
            case R.id.rl_login:
                startActivity(new Intent(this, ChangePWDActivity.class));
                break;
            case R.id.tvLogout:
                new DialogUtil(this, true, R.style.dialog, "是否确认退出？", new DialogUtil.OnCloseListener() {
                    @Override
                    public void onClick(Dialog dialog, boolean confirm) {
                        if (confirm) {
                            dialog.dismiss();
                            mPresenter.logout();
                        } else {
                            dialog.dismiss();
                        }
                    }
                }).show();
                break;
            default:
                break;
        }
    }

    @Override
    public void exitApp(String msg) {
        Utils.clearCacheAndUserInfo();
        EventBus.getDefault().post(MyConstants.OTHERS2HOME);
        finish();
    }

    @Override
    public void settingInfo(SettingInfo info) {
        if (TextUtils.isEmpty(info.getAlipay())) {
            mTvTobind.setText("去绑定");
            mTvTobind.setTextColor(ContextCompat.getColor(this, R.color.colorFF703F));
        } else {
            mTvTobind.setText("修改");
            mTvTobind.setTextColor(ContextCompat.getColor(this, R.color.color999));
        }
        mTvAccount.setText(TextUtils.getDefaultText(info.getAlipay(), ""));
        mTvPhoneNum.setText(TextUtils.getDefaultText(info.getPhone(), ""));

    }
}
