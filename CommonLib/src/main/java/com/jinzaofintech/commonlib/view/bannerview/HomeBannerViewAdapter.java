package com.jinzaofintech.commonlib.view.bannerview;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jinzaofintech.commonlib.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Author : zhoujiulong
 * Email : 754667445@qq.com
 * Time : 2017/12/14
 * 描述 :
 */

public abstract class HomeBannerViewAdapter<T> extends PagerAdapter {

    private Context mContext;
    private List<View> mViewList = new ArrayList<>();
    private List<T> mData;

    public HomeBannerViewAdapter(Context context, List<T> data) {
        this.mContext = context;
        mData = data;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view;
        if (mViewList.size() > 0) {
            view = mViewList.remove(mViewList.size() - 1);
        } else {
            view = View.inflate(mContext, getResourceId(), null);
        }
        ImageView imageView = view.findViewById(R.id.iv);
        int positionInData = position % mData.size();
        loadImage(imageView, mContext, mData.get(positionInData));
        view.setTag(positionInData);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = (int) view.getTag();
                onItemClick(mContext, pos, mData.get(pos));
            }
        });
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        mViewList.add(view);
        container.removeView(view);
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        //当返回为true的时候，就将根据当前的position得到的view展示出来
        return view == object;
    }

    public List<T> getData() {
        return mData == null ? new ArrayList<T>() : mData;
    }

    public abstract int getResourceId ();

    public abstract void loadImage(ImageView imageView, Context context, T data);

    public abstract void onItemClick(Context context, int position, T data);
}
