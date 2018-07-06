package com.jinzaofintech.rebate.ui.activity;

import com.jinzaofintech.commonlib.http.utils.NetWorkState;
import com.jinzaofintech.commonlib.ui.base.BaseActivity;
import com.jinzaofintech.rebate.R;

/**
 * Created by Administrator on 2018/6/7.
 * about us ui
 */

public class AboutusActivity extends BaseActivity {

    @Override
    protected void initPresenter() {

    }

    @Override
    protected void initData() {
        setActionBar("关于我们");
    }

    @Override
    protected void loadData() {
        setState(NetWorkState.STATE_SUCCESS);
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_recyclerview;
    }

    @Override
    protected void initView() {

    }
}
