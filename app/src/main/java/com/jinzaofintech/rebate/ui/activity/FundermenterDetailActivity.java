package com.jinzaofintech.rebate.ui.activity;

import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.widget.TextView;

import com.jinzaofintech.commonlib.http.utils.NetWorkState;
import com.jinzaofintech.commonlib.ui.base.BaseActivity;
import com.jinzaofintech.rebate.R;
import com.jinzaofintech.rebate.adapter.FundermenterDetailAdapter;
import com.jinzaofintech.rebate.bean.FundermenterInfo;
import com.jinzaofintech.rebate.presenter.FundermenterDetailPresenter;
import com.jinzaofintech.rebate.presenter.impl.FundermenterDetailPresenterImpl;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/6/7.
 */

public class FundermenterDetailActivity extends BaseActivity<FundermenterDetailPresenterImpl> implements FundermenterDetailPresenter.View {
    FundermenterDetailAdapter mFundermenterDetailAdapter;
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerview;

    @Override
    protected void initPresenter() {
        mPresenter = new FundermenterDetailPresenterImpl();
    }

    @Override
    protected void initData() {
        setActionBar("资金明细");
        mFundermenterDetailAdapter = new FundermenterDetailAdapter(R.layout.item_fundmenter_detail, new ArrayList<FundermenterInfo>());
    }

    @Override
    protected void loadData() {
        mPresenter.getFundermenterInfo();
        setState(NetWorkState.STATE_SUCCESS);
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_recyclerview;
    }

    @Override
    protected void initView() {
        mRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerview.addItemDecoration(new DividerItemDecoration(this, 1));
        mRecyclerview.setAdapter(mFundermenterDetailAdapter);
    }

    @Override
    public void fundermenterInfo(List<FundermenterInfo> fundermenterInfo) {
        if (fundermenterInfo != null && fundermenterInfo.size() > 0) {
            mFundermenterDetailAdapter.setNewData(fundermenterInfo);
        } else {
            TextView emptyView = new TextView(this);
            emptyView.setGravity(Gravity.CENTER);
            emptyView.setText("暂无数据");
            mFundermenterDetailAdapter.setEmptyView(emptyView);
        }
    }
}
