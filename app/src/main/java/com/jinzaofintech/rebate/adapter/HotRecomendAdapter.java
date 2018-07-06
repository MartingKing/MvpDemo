package com.jinzaofintech.rebate.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jinzaofintech.rebate.R;
import com.jinzaofintech.rebate.bean.JpGood;

import java.util.List;

/**
 * Created by Administrator on 2018/5/22.
 * the popular recommend adpter
 * just show two
 */

public class HotRecomendAdapter extends BaseQuickAdapter<JpGood, BaseViewHolder> {


    public HotRecomendAdapter(int layoutResId, @Nullable List<JpGood> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, JpGood item) {
        holder.setText(R.id.tv_platform, item.getTitle());
        holder.setText(R.id.tv_name, item.getName());
        holder.setText(R.id.tv_year_profit, item.getApr());
        holder.setText(R.id.tv_firt_invest, item.getFirst_investment());
        holder.setText(R.id.tv_second_invest, item.getSecond_investment());
        ImageView imageView = holder.getView(R.id.iv_platform);
        Glide.with(imageView.getContext()).load(item.getIcon()).into(imageView);
    }
}
