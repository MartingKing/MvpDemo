package com.jinzaofintech.rebate.ui.activity;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.jinzaofintech.commonlib.http.utils.NetWorkState;
import com.jinzaofintech.commonlib.ui.base.BaseActivity;
import com.jinzaofintech.commonlib.utils.TextUtils;
import com.jinzaofintech.rebate.R;
import com.jinzaofintech.rebate.bean.MyConstants;
import com.jinzaofintech.rebate.bean.SettingInfo;
import com.jinzaofintech.rebate.presenter.PhonePresenter;
import com.jinzaofintech.rebate.presenter.impl.PhonePresenterImpl;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/6/11.
 * change the bound phone number
 */

public class ChangePhoneActivity extends BaseActivity<PhonePresenterImpl> implements PhonePresenter.View {

    @BindView(R.id.tv_phone)
    TextView mTvPhone;
    @BindView(R.id.tv_can_accept)
    TextView mTvCanAccept;
    @BindView(R.id.tv_cant_accept)
    TextView mTvCantAccept;
    private String phone;

    @Override
    protected void initPresenter() {
        mPresenter = new PhonePresenterImpl();
    }

    @Override
    protected void initData() {
        setActionBar("修改手机号");
    }

    @Override
    protected void loadData() {
        mPresenter.getPhone();
        setState(NetWorkState.STATE_SUCCESS);
    }

    @Override
    public int getLayoutId() {
        return R.layout.act_layout_change_phone;
    }

    @Override
    protected void initView() {
        mTvCanAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChangePhoneActivity.this, SetNewPhoneCantReceiveCodeActivity.class));
            }
        });
        mTvCantAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChangePhoneActivity.this, SetNewPhoneCanReceiveCodeActivity.class)
                        .putExtra(MyConstants.PHONENUM, phone));
            }
        });
    }

    @Override
    public void changeMobile(SettingInfo settingInfo) {
        phone = TextUtils.getDefaultText(settingInfo.getPhone(), "");
        mTvPhone.setText(phone);
    }
}
