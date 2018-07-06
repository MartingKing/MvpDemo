package com.jinzaofintech.rebate.ui.activity;

import com.jinzaofintech.commonlib.http.utils.NetWorkState;
import com.jinzaofintech.commonlib.ui.base.BaseActivity;
import com.jinzaofintech.rebate.R;

/**
 * Created by Administrator on 2018/6/11.
 * change password
 */

public class ChangePWDActivity extends BaseActivity {


    @Override
    protected void initPresenter() {

    }

    @Override
    protected void initData() {
        setActionBar("修改密码");
    }

    @Override
    protected void loadData() {
        setState(NetWorkState.STATE_SUCCESS);
    }

    @Override
    public int getLayoutId() {
        return R.layout.act_layout_change_pwd;
    }

    @Override
    protected void initView() {

    }

}
