package com.jinzaofintech.rebate.ui.activity;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jinzaofintech.commonlib.http.utils.NetWorkState;
import com.jinzaofintech.commonlib.ui.base.BaseActivity;
import com.jinzaofintech.commonlib.utils.LogUtils;
import com.jinzaofintech.commonlib.utils.SpannableStringUtils;
import com.jinzaofintech.commonlib.utils.TextUtils;
import com.jinzaofintech.commonlib.utils.ToastUtils;
import com.jinzaofintech.commonlib.view.ClearableEditText;
import com.jinzaofintech.rebate.R;
import com.jinzaofintech.rebate.bean.MyConstants;
import com.jinzaofintech.rebate.bean.RegistResponse;
import com.jinzaofintech.rebate.bean.SmsCodeReponse;
import com.jinzaofintech.rebate.presenter.RegisterPresenter;
import com.jinzaofintech.rebate.presenter.impl.RegisterPresenterImpl;
import com.jinzaofintech.rebate.utils.TimecountUtil;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/6/7.
 * the register page step 2
 */

public class Register2Activity extends BaseActivity<RegisterPresenterImpl> implements RegisterPresenter.View, View.OnClickListener, TextWatcher {

    @BindView(R.id.phoneHint)
    TextView mPhoneHint;
    @BindView(R.id.tv_verifycode)
    ClearableEditText mTvVerifycode;
    @BindView(R.id.tv_phone_friend)
    ClearableEditText mTvFriendPhone;
    @BindView(R.id.tvGetCode)
    TextView mTvGetCode;
    @BindView(R.id.tv_verfycode_err)
    TextView mTvVerfycodeErr;
    @BindView(R.id.tv_pwd)
    EditText mTvPwd;
    @BindView(R.id.tv_pwd_set_err)
    TextView mTvPwdSetErr;
    @BindView(R.id.tv_haveinvite)
    TextView mTvHaveinvite;
    @BindView(R.id.tv_makesure)
    TextView mTvMakesure;
    @BindView(R.id.friend_divider)
    View mDivider;
    @BindView(R.id.iv_icon)
    AppCompatImageView mIcon;

    private String phone;
    private TimecountUtil time;

    private SmsCodeReponse mSmsCodeReponse;

    @Override
    protected void initPresenter() {
        mPresenter = new RegisterPresenterImpl();
    }

    @Override
    protected void initData() {
        setActionBar("注册");
        phone = getIntent().getStringExtra(MyConstants.PHONENUM);
    }

    @Override
    protected void loadData() {
        mPresenter.getSmsCode(phone);
        setState(NetWorkState.STATE_SUCCESS);
    }

    @Override
    public int getLayoutId() {
        return R.layout.act_layout_regist_step2;
    }

    @Override
    protected void initView() {
        time = new TimecountUtil(60000, 1000, mTvGetCode);
        time.start();
        mPhoneHint.setText(SpannableStringUtils
                .getBuilder("验证码已发送至")
                .append(phone).setForegroundColor(ContextCompat.getColor(this, R.color.red))
                .append("，请注意查收。")
                .create());

        mTvGetCode.setOnClickListener(this);
        mTvHaveinvite.setOnClickListener(this);
        mTvMakesure.setOnClickListener(this);
        mTvVerifycode.addTextChangedListener(this);
        mTvPwd.addTextChangedListener(this);
    }

    @Override
    public void smsSendSuccess(SmsCodeReponse smsCodeReponse) {
        mPhoneHint.setVisibility(View.VISIBLE);
        mSmsCodeReponse = smsCodeReponse;
        LogUtils.e(smsCodeReponse);
    }

    @Override
    public void registSuccess(RegistResponse response) {
        LogUtils.e("register>>>" + response);
        startActivity(new Intent(this, RegistSuccessActivity.class)
                .putExtra(MyConstants.PHONENUM, phone)
                .putExtra(MyConstants.PASSWORD, mTvPwd.getText().toString().trim()));
    }

    private int checkStatus = 0;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvGetCode:
                time.start();
                mPresenter.getSmsCode(phone);
                break;
            case R.id.tv_haveinvite:
                if (checkStatus == 0) {
                    mIcon.setBackgroundResource(R.mipmap.icon_open);
                    mTvFriendPhone.setVisibility(View.VISIBLE);
                    checkStatus = 1;
                } else {
                    mIcon.setBackgroundResource(R.mipmap.icon_close);
                    mTvFriendPhone.setVisibility(View.GONE);
                    checkStatus = 0;
                }

                break;
            case R.id.tv_makesure:
                String vfCode = mTvVerifycode.getText().toString().trim();
                String pwd = mTvPwd.getText().toString().trim();
                String friendPhone = mTvFriendPhone.getText().toString().trim();

                if (TextUtils.isEmpty(vfCode)) {
                    ToastUtils.showShortToast("验证码不能为空");
                    return;
                }
                if (mSmsCodeReponse != null && !TextUtils.getDefaultText(mSmsCodeReponse.getCode(), "").equals(vfCode)) {
                    mTvVerfycodeErr.setVisibility(View.VISIBLE);
                    return;
                }

                if (TextUtils.isEmpty(pwd)) {
                    ToastUtils.showShortToast("登录密码不能为空");
                    return;
                }
                if (pwd.length() < 6 || pwd.length() > 18) {
                    mTvPwdSetErr.setVisibility(View.VISIBLE);
                    return;
                }
                if (mSmsCodeReponse != null) {
                    mPresenter.doRegist(vfCode, mSmsCodeReponse.getKey(), pwd, friendPhone);
                } else {
                    ToastUtils.showShortToast("数据请求出错");
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        mTvVerfycodeErr.setVisibility(View.GONE);
        mTvPwdSetErr.setVisibility(View.GONE);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
