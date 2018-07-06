package com.jinzaofintech.rebate.ui.activity;

import android.widget.LinearLayout;
import android.widget.TextView;

import com.jinzaofintech.commonlib.http.utils.NetWorkState;
import com.jinzaofintech.commonlib.ui.base.BaseActivity;
import com.jinzaofintech.commonlib.utils.TextUtils;
import com.jinzaofintech.rebate.R;
import com.jinzaofintech.rebate.bean.InviteFriendsInfo;
import com.jinzaofintech.rebate.presenter.InvitingFriendsPresenter;
import com.jinzaofintech.rebate.presenter.impl.InvitingFriendsPresenterImpl;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/6/7.
 * prize invited ui
 */

public class RewardInvatationActivity extends BaseActivity<InvitingFriendsPresenterImpl> implements InvitingFriendsPresenter.View {

    @BindView(R.id.tv_amount)
    TextView mTvAmount;
    @BindView(R.id.tv_numberof_friend)
    TextView mTvFriendNo;
    @BindView(R.id.tv_invited_number)
    TextView mTvInvitedNo;
    @BindView(R.id.ll_total_reward)
    LinearLayout mLlTotalReward;
    @BindView(R.id.ll_total_invite)
    LinearLayout mLlTotalInvite;
    @BindView(R.id.tvLoiginBtn)
    TextView mTvLoiginBtn;

    @Override
    protected void initPresenter() {
        mPresenter = new InvitingFriendsPresenterImpl();
    }

    @Override
    protected void initData() {
        setActionBar("邀请好友");
    }

    @Override
    protected void loadData() {
        mPresenter.getRewardInvation();
        setState(NetWorkState.STATE_SUCCESS);
    }

    @Override
    public int getLayoutId() {
        return R.layout.act_layout_invite;
    }

    @Override
    protected void initView() {

    }

    @Override
    public void invitingFriendinfo(InviteFriendsInfo inviteFriendsInfo) {
        mTvAmount.setText(TextUtils.getDefaultText(inviteFriendsInfo.getReward() + "", "0"));
        mTvFriendNo.setText(TextUtils.getDefaultText(inviteFriendsInfo.getYyfriend() + "", "0"));
        mTvInvitedNo.setText(TextUtils.getDefaultText(inviteFriendsInfo.getCount() + "", "0"));
    }
}
