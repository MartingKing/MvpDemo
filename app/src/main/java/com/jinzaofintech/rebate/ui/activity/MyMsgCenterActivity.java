package com.jinzaofintech.rebate.ui.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jinzaofintech.commonlib.http.utils.NetWorkState;
import com.jinzaofintech.commonlib.ui.base.BaseActivity;
import com.jinzaofintech.rebate.R;
import com.jinzaofintech.rebate.adapter.MyMsgAdapter;
import com.jinzaofintech.rebate.bean.MessageInfo;
import com.jinzaofintech.rebate.bean.MyConstants;
import com.jinzaofintech.rebate.presenter.MyMsgPresenter;
import com.jinzaofintech.rebate.presenter.impl.MyMsgPresenterImpl;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/6/7.
 * message center ui
 */

public class MyMsgCenterActivity extends BaseActivity<MyMsgPresenterImpl> implements MyMsgPresenter.View {

    MyMsgAdapter mMyMsgAdapter;
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerview;
    private List<MessageInfo> mDatas;

    @Override
    protected void initPresenter() {
        mPresenter = new MyMsgPresenterImpl();
    }

    @Override
    protected void initData() {
        setActionBar("我的消息");
        mMyMsgAdapter = new MyMsgAdapter(R.layout.item_msg_center, new ArrayList<MessageInfo>());
    }

    @Override
    protected void loadData() {
        mPresenter.getMsg();
        setState(NetWorkState.STATE_SUCCESS);
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_recyclerview;
    }

    @Override
    protected void initView() {
        mRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerview.setAdapter(mMyMsgAdapter);
        mMyMsgAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(MyMsgCenterActivity.this, MyMessageActivity.class);
                intent.putExtra(MyConstants.MY_MESSAGE, mDatas.get(position));
                startActivity(intent);
            }
        });
    }

    @Override
    public void myMessage(List<MessageInfo> msginfo) {
        mDatas = msginfo;
        if (msginfo != null && msginfo.size() > 0) {
            mMyMsgAdapter.setNewData(msginfo);
        } else {
            TextView emptyView = new TextView(this);
            emptyView.setGravity(Gravity.CENTER);
            emptyView.setText("暂无数据");
            mMyMsgAdapter.setEmptyView(emptyView);
        }
    }
}
