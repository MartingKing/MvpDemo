package com.jinzaofintech.rebate.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.jinzaofintech.commonlib.app.ActivityManager;
import com.jinzaofintech.commonlib.http.utils.NetWorkState;
import com.jinzaofintech.commonlib.ui.base.BaseActivity;
import com.jinzaofintech.commonlib.utils.SpannableStringUtils;
import com.jinzaofintech.commonlib.utils.TextUtils;
import com.jinzaofintech.commonlib.utils.ToastUtils;
import com.jinzaofintech.commonlib.view.ClearableEditText;
import com.jinzaofintech.rebate.R;
import com.jinzaofintech.rebate.bean.MyConstants;
import com.jinzaofintech.rebate.bean.SmsCodeReponse;
import com.jinzaofintech.rebate.presenter.ChangePhonePresenter;
import com.jinzaofintech.rebate.presenter.impl.ChangePhonePresenterImpl;
import com.jinzaofintech.rebate.utils.DialogUtil;
import com.jinzaofintech.rebate.utils.TimecountUtil;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/6/11.
 */

public class SetNewPhoneCanReceiveCodeActivity extends BaseActivity<ChangePhonePresenterImpl> implements ChangePhonePresenter.View {


    @BindView(R.id.phoneHint)
    TextView mPhoneHint;
    @BindView(R.id.tv_oldcode)
    ClearableEditText mTvOldcode;
    @BindView(R.id.tv_verifycode)
    TextView mTvVerifycode;
    @BindView(R.id.tv_newphone)
    ClearableEditText mTvNewphone;
    @BindView(R.id.tv_newcolde)
    ClearableEditText mTvNewcolde;
    @BindView(R.id.tvGetCode)
    TextView mTvGetCode;
    @BindView(R.id.tv_makesure)
    TextView mTvMakesure;
    private String phoneNo;
    private TimecountUtil time;
    private SmsCodeReponse mSmsCodeReponseOld;
    private SmsCodeReponse mSmsCodeReponseNew;
    private boolean isFirstRequestSmsCode = true;

    @Override
    protected void initPresenter() {
        mPresenter = new ChangePhonePresenterImpl();
    }

    @Override
    protected void initData() {
        phoneNo = getIntent().getStringExtra(MyConstants.PHONENUM);
        setActionBar("修改手机号");
    }

    @Override
    protected void loadData() {
        mPresenter.getSmsCode(phoneNo);
        setState(NetWorkState.STATE_SUCCESS);
    }

    @Override
    public int getLayoutId() {
        return R.layout.act_layout_set_new_phone;
    }

    @Override
    protected void initView() {
        time = new TimecountUtil(60000, 1000, mTvVerifycode);
        time.start();
        mPhoneHint.setText(SpannableStringUtils
                .getBuilder("验证码已发送至")
                .append(phoneNo).setForegroundColor(ContextCompat.getColor(SetNewPhoneCanReceiveCodeActivity.this, R.color.red))
                .append("，请注意查收。")
                .create());
        mTvVerifycode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time.start();
            }
        });

        mTvGetCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newPhone = mTvNewphone.getText().toString().trim();
                time = new TimecountUtil(60000, 1000, mTvGetCode);
                if (TextUtils.isEmpty(newPhone)) {
                    ToastUtils.showShortToast("手机号不能为空");
                    return;
                }
                mPresenter.getSmsCode(newPhone);
                time.start();
                mPhoneHint.setText(SpannableStringUtils
                        .getBuilder("验证码已发送至")
                        .append(newPhone).setForegroundColor(ContextCompat.getColor(SetNewPhoneCanReceiveCodeActivity.this, R.color.red))
                        .append("，请注意查收。")
                        .create());
            }
        });

        mTvMakesure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldCode = mTvOldcode.getText().toString().trim();
                String newcode = mTvNewcolde.getText().toString().trim();
                if (TextUtils.isEmpty(oldCode)) {
                    ToastUtils.showShortToast("旧手机号验证码不能为空");
                    return;
                }
                if (TextUtils.isEmpty(newcode)) {
                    ToastUtils.showShortToast("新手机号验证码不能为空");
                    return;
                }
                mPresenter.getPhone(mSmsCodeReponseOld.getKey(), oldCode, mSmsCodeReponseNew.getKey(), newcode);
            }
        });
    }

    @Override
    public void smsSendSuccess(SmsCodeReponse smsCodeReponse) {
        if (isFirstRequestSmsCode) {
            mSmsCodeReponseOld = smsCodeReponse;
            isFirstRequestSmsCode = false;
        }
        mSmsCodeReponseNew = smsCodeReponse;

    }

    @Override
    public void changeMobile(Object msg) {
        new DialogUtil(this, false, R.style.dialog, "手机号改绑成功。", new DialogUtil.OnCloseListener() {
            @Override
            public void onClick(Dialog dialog, boolean confirm) {
                if (confirm) {
                    dialog.dismiss();
                    ActivityManager.getActivityManager().finishActivity(ChangePhoneActivity.class, SetNewPhoneCanReceiveCodeActivity.class);
                    startActivity(new Intent(SetNewPhoneCanReceiveCodeActivity.this, SettingActivity.class));
                }
            }
        }).show();
    }

}
