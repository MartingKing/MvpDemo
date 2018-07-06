package com.jinzaofintech.rebate.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jinzaofintech.commonlib.app.ActivityManager;
import com.jinzaofintech.commonlib.http.utils.NetWorkState;
import com.jinzaofintech.commonlib.ui.base.BaseActivity;
import com.jinzaofintech.commonlib.utils.TextUtils;
import com.jinzaofintech.commonlib.utils.ToastUtils;
import com.jinzaofintech.commonlib.view.ClearableEditText;
import com.jinzaofintech.rebate.R;
import com.jinzaofintech.rebate.app.AppActivityManager;
import com.jinzaofintech.rebate.bean.MyConstants;
import com.jinzaofintech.rebate.presenter.BindAlipayPresenter;
import com.jinzaofintech.rebate.presenter.impl.BindAlipayPresenterImpl;
import com.jinzaofintech.rebate.utils.DialogUtil;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/6/11.
 * change zhifubao account
 */

public class ChangeZFBAccountActivity extends BaseActivity<BindAlipayPresenterImpl> implements BindAlipayPresenter.View, TextWatcher {

    @BindView(R.id.edtAccount)
    ClearableEditText mEdtAccount;
    @BindView(R.id.edtPwd)
    EditText mEdtPwd;
    @BindView(R.id.tvLoiginBtn)
    TextView mTvLoiginBtn;
    private String where, restamount;

    @Override
    protected void initPresenter() {
        mPresenter = new BindAlipayPresenterImpl();
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        if (intent.hasExtra(MyConstants.FROM_WHERE)) {
            where = intent.getStringExtra(MyConstants.FROM_WHERE);
        }
        if (intent.hasExtra(MyConstants.REST_AMOUNT)) {
            restamount = intent.getStringExtra(MyConstants.REST_AMOUNT);
        }
        setActionBar("修改支付宝");
    }

    @Override
    protected void loadData() {
        setState(NetWorkState.STATE_SUCCESS);
    }

    @Override
    public int getLayoutId() {
        return R.layout.act_layout_change_zfb;
    }

    private String account, realname;

    @Override
    protected void initView() {
        mTvLoiginBtn.setEnabled(false);
        //the first page content
        mEdtPwd.addTextChangedListener(this);
        mTvLoiginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                account = mEdtAccount.getText().toString().trim();
                realname = mEdtPwd.getText().toString().trim();
                if (TextUtils.isEmpty(account)) {
                    ToastUtils.showShortToast("账户不能为空");
                    return;
                }
                if (TextUtils.isEmpty(realname)) {
                    ToastUtils.showShortToast("实名不能为空");
                    return;
                }
                mPresenter.doBind(account, realname);
            }
        });
    }

    @Override
    public void bindInfo() {
        if (where.equals("account_widthdraw") || where.equals("home_widthdraw")) {
            AppActivityManager.finishAllActivity();
            startActivity(new Intent(ChangeZFBAccountActivity.this, WidthdrawActivity.class)
                    .putExtra(MyConstants.ALI_ACCOUNT, account)
                    .putExtra(MyConstants.ALI_REALNAME, realname)
                    .putExtra(MyConstants.REST_AMOUNT, restamount)
                    .putExtra(MyConstants.FROM_WHERE, "bindzhifubao"));
        } else {
            new DialogUtil(this, false, R.style.dialog, "支付宝账户改绑成功。", new DialogUtil.OnCloseListener() {
                @Override
                public void onClick(Dialog dialog, boolean confirm) {
                    if (confirm) {
                        dialog.dismiss();
                    }
                }
            }).show();
            finish();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() > 0) {
            mTvLoiginBtn.setEnabled(true);
            mTvLoiginBtn.setBackgroundResource(R.drawable.shape_bg_blue);
        } else {
            mTvLoiginBtn.setEnabled(false);
            mTvLoiginBtn.setBackgroundResource(R.drawable.shape_bg_gray);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
