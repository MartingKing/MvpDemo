package com.jinzaofintech.rebate.ui.activity;

import android.widget.TextView;

import com.jinzaofintech.commonlib.http.utils.NetWorkState;
import com.jinzaofintech.commonlib.ui.base.BaseActivity;
import com.jinzaofintech.rebate.R;
import com.jinzaofintech.rebate.bean.MessageInfo;
import com.jinzaofintech.rebate.bean.MyConstants;
import com.jinzaofintech.rebate.presenter.impl.MyMsgPresenterImpl;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/6/7.
 * message center ui
 */

public class MyMessageActivity extends BaseActivity {

    @BindView(R.id.tv_msg_title)
    TextView mTvMsgTitle;
    @BindView(R.id.tv_msg_content)
    TextView mTvMsgContent;
    @BindView(R.id.tv_msg_time)
    TextView mTvMsgTime;
    private MessageInfo mDatas;

    @Override
    protected void initPresenter() {
        mPresenter = new MyMsgPresenterImpl();
    }

    @Override
    protected void initData() {
        mDatas = (MessageInfo) getIntent().getSerializableExtra(MyConstants.MY_MESSAGE);
        setActionBar("我的消息");
    }

    @Override
    protected void loadData() {
        setState(NetWorkState.STATE_SUCCESS);
    }

    @Override
    public int getLayoutId() {
        return R.layout.act_layout_my_msg;
    }

    @Override
    protected void initView() {
        mTvMsgTitle.setText(mDatas.getTitle());
        mTvMsgContent.setText(mDatas.getContent());
        mTvMsgTime.setText(mDatas.getCreated_at());
    }

}
