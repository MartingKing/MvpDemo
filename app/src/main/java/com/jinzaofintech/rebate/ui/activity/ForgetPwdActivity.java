package com.jinzaofintech.rebate.ui.activity;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.jinzaofintech.rebate.presenter.ForgetPwdPresenter;
import com.jinzaofintech.rebate.presenter.impl.ForgetPwdPresenterImpl;
import com.jinzaofintech.rebate.utils.TimecountUtil;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/6/15.
 * forget password
 */

public class ForgetPwdActivity extends BaseActivity<ForgetPwdPresenterImpl> implements ForgetPwdPresenter.View, TextWatcher {

    @BindView(R.id.phoneHint)
    TextView mPhoneHint;
    @BindView(R.id.edtPhone)
    ClearableEditText mEdtPhone;
    @BindView(R.id.edtcCode)
    ClearableEditText mEdtcCode;
    @BindView(R.id.tvGetCode)
    TextView mTvGetCode;
    @BindView(R.id.edtPwd)
    EditText mEdtPwd;
    @BindView(R.id.tv_makesure)
    TextView mTvMakesure;
    @BindView(R.id.tv_verfycode_err)
    TextView mVfCodeErr;
    @BindView(R.id.tv_pwd_set_err)
    TextView mPwderr;

    private TimecountUtil mTimecountUtil;
    private SmsCodeReponse mSmsCodeReponse;

    @Override
    protected void initPresenter() {
        mPresenter = new ForgetPwdPresenterImpl();
    }

    @Override
    protected void initData() {
        setActionBar("忘记密码");
    }

    @Override
    protected void loadData() {
        setState(NetWorkState.STATE_SUCCESS);
    }

    @Override
    public int getLayoutId() {
        return R.layout.act_layout_forget_pwd;
    }

    @Override
    protected void initView() {
        mTimecountUtil = new TimecountUtil(60000, 1000, mTvGetCode);
        mVfCodeErr.addTextChangedListener(this);
        mTvMakesure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String vfcode = mEdtcCode.getText().toString().trim();
                String pwd = mEdtPwd.getText().toString().trim();
                if (TextUtils.isEmpty(pwd)) {
                    ToastUtils.showShortToast("新密码不能为空");
                    return;
                }
                if (pwd.length() < 6) {
                    mPwderr.setVisibility(View.VISIBLE);
                    return;
                }
                if (!mSmsCodeReponse.getCode().equals(vfcode)) {
                    mVfCodeErr.setVisibility(View.VISIBLE);
                    return;
                }
                if (mSmsCodeReponse != null) {
                    mPresenter.doRequest(mSmsCodeReponse.getKey(), vfcode, pwd);
                } else {
                    ToastUtils.showShortToast("数据请求出错");
                }
            }
        });
        mTvGetCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = mEdtPhone.getText().toString().trim();
                if (TextUtils.isEmpty(phone)) {
                    ToastUtils.showShortToast("手机号不能为空");
                    return;
                }
                mPhoneHint.setVisibility(View.VISIBLE);
                mPhoneHint.setText(SpannableStringUtils
                        .getBuilder("验证码已发送至")
                        .append(phone).setForegroundColor(ContextCompat.getColor(ForgetPwdActivity.this, R.color.red))
                        .append("，请注意查收。")
                        .create());
                mTimecountUtil.start();
                mPresenter.getSmsCode(phone);
            }
        });
    }

    @Override
    public void setNewPwdSuccess(Object info) {
        ActivityManager.getActivityManager().finishActivity(ForgetPwdActivity.class, LoginActivity.class);
        startActivity(new Intent(ForgetPwdActivity.this, LoginActivity.class));
    }

    @Override
    public void smsSendSuccess(SmsCodeReponse smsCodeReponse) {
        mSmsCodeReponse = smsCodeReponse;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        mVfCodeErr.setVisibility(View.GONE);
        mPwderr.setVisibility(View.GONE);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
