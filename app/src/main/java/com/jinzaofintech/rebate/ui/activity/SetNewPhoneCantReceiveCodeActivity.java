package com.jinzaofintech.rebate.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jinzaofintech.commonlib.app.ActivityManager;
import com.jinzaofintech.commonlib.http.utils.NetWorkState;
import com.jinzaofintech.commonlib.ui.base.BaseActivity;
import com.jinzaofintech.commonlib.utils.SpannableStringUtils;
import com.jinzaofintech.commonlib.utils.TextUtils;
import com.jinzaofintech.commonlib.utils.ToastUtils;
import com.jinzaofintech.commonlib.view.ClearableEditText;
import com.jinzaofintech.rebate.R;
import com.jinzaofintech.rebate.bean.SmsCodeReponse;
import com.jinzaofintech.rebate.presenter.ChangePhonePresenter2;
import com.jinzaofintech.rebate.presenter.impl.ChangePhonePresenter2Impl;
import com.jinzaofintech.rebate.utils.DialogUtil;
import com.jinzaofintech.rebate.utils.TimecountUtil;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/6/11.
 */

public class SetNewPhoneCantReceiveCodeActivity extends BaseActivity<ChangePhonePresenter2Impl> implements ChangePhonePresenter2.View {
    @BindView(R.id.phoneHint)
    TextView mPhoneHint;
    @BindView(R.id.tv_loginpwd)
    ClearableEditText mTvLoginpwd;
    @BindView(R.id.tv_newphone)
    EditText mTvNewphone;
    @BindView(R.id.tv_newcode)
    ClearableEditText mTvNewcode;
    @BindView(R.id.tvGetCode)
    TextView mTvGetCode;
    @BindView(R.id.tv_makesure)
    TextView mTvMakesure;

    private SmsCodeReponse mSmsCodeReponse;
    private TimecountUtil time;

    @Override
    protected void initPresenter() {
        mPresenter = new ChangePhonePresenter2Impl();
    }

    @Override
    protected void initData() {
        setActionBar("修改手机号");
    }

    @Override
    protected void loadData() {
        setState(NetWorkState.STATE_SUCCESS);
    }

    @Override
    public int getLayoutId() {
        return R.layout.act_layout_set_new_phone_cant;
    }

    @Override
    protected void initView() {
        time = new TimecountUtil(60000, 1000, mTvGetCode);
        mTvGetCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newPhone = mTvNewphone.getText().toString().trim();
                if (TextUtils.isEmpty(newPhone)) {
                    ToastUtils.showShortToast("请输入手机号");
                    return;
                }
                mPresenter.getSmsCode(newPhone);
                time.start();
                mPhoneHint.setVisibility(View.VISIBLE);
                mPhoneHint.setText(SpannableStringUtils
                        .getBuilder("验证码已发送至")
                        .append(newPhone).setForegroundColor(ContextCompat.getColor(SetNewPhoneCantReceiveCodeActivity.this, R.color.red))
                        .append("，请注意查收。")
                        .create());
            }
        });
        mTvMakesure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String loginPwd = mTvLoginpwd.getText().toString().trim();
                String vfcode = mTvNewcode.getText().toString().trim();
                if (TextUtils.isEmpty(loginPwd)) {
                    ToastUtils.showShortToast("登录密码不能为空");
                    return;
                }
                if (TextUtils.isEmpty(mTvNewphone.getText().toString().trim())) {
                    ToastUtils.showShortToast("新手机号码不能为空");
                    return;
                }
                if (TextUtils.isEmpty(vfcode)) {
                    ToastUtils.showShortToast("手机验证码不能为空");
                    return;
                }
                if (!vfcode.equals(mSmsCodeReponse.getCode())) {
                    ToastUtils.showShortToast("验证码错误");
                    return;
                }
                mPresenter.getPhone(loginPwd, mSmsCodeReponse.getKey(), vfcode);
            }
        });
    }

    @Override
    public void smsSendSuccess(SmsCodeReponse smsCodeReponse) {
        mSmsCodeReponse = smsCodeReponse;
    }

    @Override
    public void changeMobile(Object msg) {
        new DialogUtil(this, false, R.style.dialog, "手机号改绑成功。", new DialogUtil.OnCloseListener() {
            @Override
            public void onClick(Dialog dialog, boolean confirm) {
                if (confirm) {
                    dialog.dismiss();
                    ActivityManager.getActivityManager().finishActivity(ChangePhoneActivity.class, SetNewPhoneCantReceiveCodeActivity.class);
                    startActivity(new Intent(SetNewPhoneCantReceiveCodeActivity.this, SettingActivity.class));
                }
            }
        }).show();
    }
}
