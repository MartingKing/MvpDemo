package com.jinzaofintech.rebate.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jinzaofintech.rebate.R;
import com.jinzaofintech.rebate.bean.MessageInfo;

import java.util.List;

/**
 * Created by Administrator on 2018/5/22.
 * my message adapter
 * message center
 */

public class MyMsgAdapter extends BaseQuickAdapter<MessageInfo, BaseViewHolder> {


    public MyMsgAdapter(int layoutResId, @Nullable List<MessageInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, MessageInfo item) {
        holder.setText(R.id.tv_title, item.getTitle());
        holder.setText(R.id.tv_time, item.getCreated_at());
        holder.setText(R.id.tv_content, item.getContent());
    }
}
