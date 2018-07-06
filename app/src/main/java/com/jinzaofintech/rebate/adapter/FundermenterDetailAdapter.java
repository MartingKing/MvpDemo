package com.jinzaofintech.rebate.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jinzaofintech.rebate.R;
import com.jinzaofintech.rebate.bean.FundermenterInfo;

import java.util.List;

/**
 * Created by Administrator on 2018/5/22.
 * the financial details adapter
 */

public class FundermenterDetailAdapter extends BaseQuickAdapter<FundermenterInfo, BaseViewHolder> {


    public FundermenterDetailAdapter(int layoutResId, @Nullable List<FundermenterInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, FundermenterInfo item) {
        helper.setText(R.id.tv_type, item.getTitle());
        helper.setText(R.id.tv_time, item.getCreated_at());
        helper.setText(R.id.tv_amount, item.getSum());
    }
}
