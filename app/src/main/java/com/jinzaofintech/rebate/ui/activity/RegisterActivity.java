package com.jinzaofintech.rebate.ui.activity;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jinzaofintech.commonlib.http.utils.NetWorkState;
import com.jinzaofintech.commonlib.ui.base.BaseActivity;
import com.jinzaofintech.commonlib.utils.TextUtils;
import com.jinzaofintech.commonlib.utils.ToastUtils;
import com.jinzaofintech.commonlib.view.ClearableEditText;
import com.jinzaofintech.commonlib.view.TextViewIcon;
import com.jinzaofintech.rebate.R;
import com.jinzaofintech.rebate.bean.MyConstants;
import com.jinzaofintech.rebate.presenter.impl.RegisterPresenterImpl;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/6/7.
 * register ui
 */

public class RegisterActivity extends BaseActivity implements TextWatcher {

    @BindView(R.id.tv_phone)
    ClearableEditText mTvPhone;
    @BindView(R.id.tv_phone_format_err)
    TextView mTvPhoneFormatErr;
    @BindView(R.id.tv_nextstep)
    TextView mTvNextstep;
    @BindView(R.id.ll_center)
    LinearLayout mLlCenter;
    @BindView(R.id.tv_select_icon)
    TextViewIcon mTvSelectIcon;
    @BindView(R.id.tv_xieyi)
    TextView mTvXieyi;
    @BindView(R.id.ll_accept)
    LinearLayout mLlAccept;
    @BindView(R.id.tv_tologin)
    LinearLayout mTvTologin;
    private int checkStatus = 0;

    @Override
    protected void initPresenter() {
        mPresenter = new RegisterPresenterImpl();
    }

    @Override
    protected void initData() {
        setActionBar("注册");
    }

    @Override
    protected void loadData() {
        setState(NetWorkState.STATE_SUCCESS);
    }

    @Override
    public int getLayoutId() {
        return R.layout.act_layout_regist_step1;
    }

    @Override
    protected void initView() {
        mTvSelectIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkStatus == 0) {
                    mTvSelectIcon.setText(getText(R.string.status_unselect));
                    mTvSelectIcon.setTextColor(ContextCompat.getColor(RegisterActivity.this, R.color.colordc));
                    checkStatus = 1;
                } else {
                    mTvSelectIcon.setText(getText(R.string.icon_selected));
                    mTvSelectIcon.setTextColor(ContextCompat.getColor(RegisterActivity.this, R.color.colorFEC043));
                    checkStatus = 0;
                }
            }
        });
        mTvPhone.addTextChangedListener(this);
        mTvNextstep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = mTvPhone.getText().toString().trim();
                if (TextUtils.isEmpty(phone)) {
                    ToastUtils.showShortToast("手机号不能为空");
                    return;
                }
                if (!TextUtils.isPhoneLegal(phone)) {
                    mTvPhoneFormatErr.setVisibility(View.VISIBLE);
                    return;
                }
                if (checkStatus == 1) {
                    ToastUtils.showShortToast("请先阅读并同意协议");
                    return;
                }
                startActivity(new Intent(RegisterActivity.this, Register2Activity.class).putExtra(MyConstants.PHONENUM, phone));
            }
        });
        mTvTologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkStatus == 1) {
                    ToastUtils.showShortToast("请先阅读并同意协议");
                    return;
                }
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        mTvPhoneFormatErr.setVisibility(View.GONE);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
