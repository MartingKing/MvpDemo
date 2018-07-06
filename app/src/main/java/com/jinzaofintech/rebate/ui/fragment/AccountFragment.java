package com.jinzaofintech.rebate.ui.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jinzaofintech.commonlib.http.utils.NetWorkState;
import com.jinzaofintech.commonlib.ui.base.BaseFragment;
import com.jinzaofintech.commonlib.utils.TextUtils;
import com.jinzaofintech.rebate.R;
import com.jinzaofintech.rebate.bean.AccountInfo;
import com.jinzaofintech.rebate.bean.MyConstants;
import com.jinzaofintech.rebate.presenter.AccountPresenter;
import com.jinzaofintech.rebate.presenter.impl.AccountPresenterImpl;
import com.jinzaofintech.rebate.ui.activity.AboutusActivity;
import com.jinzaofintech.rebate.ui.activity.ChangeZFBAccountActivity;
import com.jinzaofintech.rebate.ui.activity.FundermenterDetailActivity;
import com.jinzaofintech.rebate.ui.activity.MyMsgCenterActivity;
import com.jinzaofintech.rebate.ui.activity.RewardInvatationActivity;
import com.jinzaofintech.rebate.ui.activity.SettingActivity;
import com.jinzaofintech.rebate.ui.activity.WidthdrawActivity;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/6/4.
 */

public class AccountFragment extends BaseFragment<AccountPresenterImpl> implements AccountPresenter.View, View.OnClickListener {

    @BindView(R.id.tv_widthdrwa)
    TextView mTvWidthdrwa;
    @BindView(R.id.tv_fundation_detail)
    TextView mTvFundationDetail;
    @BindView(R.id.rl_fundation_detail)
    RelativeLayout mRlFundationDetail;
    @BindView(R.id.tv_reward_invatation)
    TextView mTvRewardInvatation;
    @BindView(R.id.rl_reward_invatation)
    RelativeLayout mRlRewardInvatation;
    @BindView(R.id.tv_msg)
    TextView mTvMsg;
    @BindView(R.id.rl_my_msg)
    RelativeLayout mRlMyMsg;
    @BindView(R.id.tv_set)
    TextView mTvSet;
    @BindView(R.id.rl_set)
    RelativeLayout mRlSet;
    @BindView(R.id.tv_abount_us)
    TextView mTvAbountUs;
    @BindView(R.id.rl_abount_us)
    RelativeLayout mRlAbountUs;
    @BindView(R.id.tv_widthdrwa_aready)
    TextView mTvWidthdrwaAready;
    @BindView(R.id.my_assets)
    TextView mMyAssets;
    private AccountInfo mAccountInfo;
    private Boolean isFirst = true;

    @Override
    protected void initPresenter() {
        mPresenter = new AccountPresenterImpl();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void loadData() {
        mPresenter.getAccountInfo();
        setState(NetWorkState.STATE_SUCCESS);
    }

    @Override
    public int getLayoutId() {
        return R.layout.frag_account;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isFirst) {
            mPresenter.getAccountInfo();
        }
    }

    @Override
    protected void initView() {
        mTvWidthdrwa.setOnClickListener(this);
        mRlFundationDetail.setOnClickListener(this);
        mRlRewardInvatation.setOnClickListener(this);
        mRlMyMsg.setOnClickListener(this);
        mRlSet.setOnClickListener(this);
        mRlAbountUs.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_widthdrwa:
                if (mAccountInfo.getStatus() == 1) {
                    startActivity(new Intent(getContext(), WidthdrawActivity.class)
                            .putExtra(MyConstants.ACCOUNT_INFO, mAccountInfo)
                            .putExtra(MyConstants.FROM_WHERE, "account_widthdraw"));
                } else {
                    startActivity(new Intent(getContext(), ChangeZFBAccountActivity.class)
                            .putExtra(MyConstants.FROM_WHERE, "account_widthdraw")
                            .putExtra(MyConstants.REST_AMOUNT, mAccountInfo.getTotal()));
                }
                break;
            case R.id.rl_fundation_detail:
                startActivity(new Intent(getContext(), FundermenterDetailActivity.class));
                break;
            case R.id.rl_reward_invatation:
                startActivity(new Intent(getContext(), RewardInvatationActivity.class));
                break;
            case R.id.rl_my_msg:
                startActivity(new Intent(getContext(), MyMsgCenterActivity.class));
                break;
            case R.id.rl_set:
                startActivity(new Intent(getContext(), SettingActivity.class));
                break;
            case R.id.rl_abount_us:
                startActivity(new Intent(getContext(), AboutusActivity.class));
                break;
            default:
                break;
        }
    }

    @Override
    public void accountinfo(AccountInfo accountInfo) {
        isFirst = false;
        if (accountInfo != null) {
            mAccountInfo = accountInfo;
            String accountRest = TextUtils.getDefaultText(accountInfo.getTotal(), "0");
            mMyAssets.setText(accountRest);
            mTvWidthdrwaAready.setText(TextUtils.getTwoStrAppendText("已提现金额:", accountInfo.getWithdraw() + ""));
            mTvMsg.setText(TextUtils.getDefaultText(accountInfo.getNotification_count() + "", "0"));
        }
    }
}
