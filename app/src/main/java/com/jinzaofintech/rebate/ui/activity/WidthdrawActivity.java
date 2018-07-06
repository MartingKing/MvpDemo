package com.jinzaofintech.rebate.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jinzaofintech.commonlib.http.utils.NetWorkState;
import com.jinzaofintech.commonlib.ui.base.BaseActivity;
import com.jinzaofintech.commonlib.utils.TextUtils;
import com.jinzaofintech.commonlib.utils.ToastUtils;
import com.jinzaofintech.rebate.R;
import com.jinzaofintech.rebate.bean.AccountInfo;
import com.jinzaofintech.rebate.bean.MyConstants;
import com.jinzaofintech.rebate.presenter.WidthdrawPresenter;
import com.jinzaofintech.rebate.presenter.impl.WidthdrawPresenterImpl;
import com.jinzaofintech.rebate.utils.DialogUtil;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/6/7.
 */

public class WidthdrawActivity extends BaseActivity<WidthdrawPresenterImpl> implements WidthdrawPresenter.View {

    @BindView(R.id.tv_widthdrwa_money)
    EditText mTvWMoney;
    @BindView(R.id.tvLoiginBtn)
    TextView mTvLoiginBtn;
    @BindView(R.id.tv_account_email)
    TextView mTvAccountEmail;
    @BindView(R.id.tv_amount)
    TextView mTvAmount;
    @BindView(R.id.tv_change_account)
    TextView mTvChange;

    private String account = "";
    private String realname = "";
    private String restamount = "";
    private String where = "";

    @Override
    protected void initPresenter() {
        mPresenter = new WidthdrawPresenterImpl();
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        if (intent.hasExtra(MyConstants.FROM_WHERE)) {
            where = intent.getStringExtra(MyConstants.FROM_WHERE);
        }
        if (where.equals("account_widthdraw")) {
            AccountInfo mAccountInfo = (AccountInfo) getIntent().getSerializableExtra(MyConstants.ACCOUNT_INFO);
            account = mAccountInfo.getAlipay();
            realname = mAccountInfo.getAliname();
            restamount = mAccountInfo.getTotal();
        }
        if (where.equals("bindzhifubao") || where.equals("home_frag")) {
            account = getIntent().getStringExtra(MyConstants.ALI_ACCOUNT);
            realname = getIntent().getStringExtra(MyConstants.ALI_REALNAME);
            restamount = getIntent().getStringExtra(MyConstants.REST_AMOUNT);
        }
        setActionBar("提现");
    }

    @Override
    protected void loadData() {
        setState(NetWorkState.STATE_SUCCESS);
    }

    @Override
    public int getLayoutId() {
        return R.layout.act_layout_widthdraw_step;
    }

    @Override
    protected void initView() {
        mTvChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WidthdrawActivity.this, ChangeZFBAccountActivity.class));
            }
        });
        //the second page content
        mTvAmount.setText(restamount);
        mTvAccountEmail.setText(account);
        mTvLoiginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String money = mTvWMoney.getText().toString().trim();
                if (TextUtils.isEmpty(money)) {
                    ToastUtils.showShortToast("提现金额不能为空");
                    return;
                }
                if (Double.valueOf(money) < 1) {
                    ToastUtils.showShortToast("提现金额不能小于1元");
                    return;
                }
                if (Double.valueOf(money) > Double.valueOf(restamount)) {
                    ToastUtils.showShortToast("提现金额不能大于账户余额");
                    return;
                }
                mPresenter.doRequest(money, account, realname);
            }
        });
    }

    @Override
    public void widthdraw(String msg) {
        new DialogUtil(this, false, R.style.dialog, "申请提现成功，1到3个工作日内到账。", new DialogUtil.OnCloseListener() {
            @Override
            public void onClick(Dialog dialog, boolean confirm) {
                if (confirm) {
                    dialog.dismiss();
                }
            }
        }).show();
    }

}
