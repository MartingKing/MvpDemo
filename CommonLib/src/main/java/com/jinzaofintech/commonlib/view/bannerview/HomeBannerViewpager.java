package com.jinzaofintech.commonlib.view.bannerview;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

import com.jinzaofintech.commonlib.R;

/**
 * Author : zhoujiulong
 * Email : 754667445@qq.com
 * Time : 2017/12/15
 * 描述 :
 */

public class HomeBannerViewpager extends ViewPager {

    private boolean mScrollFinish = true;
    private CountDownTimer mCountDownTimer;
    private int mLeftAndRightMargin = 0;

    public HomeBannerViewpager(@NonNull Context context) {
        this(context, null);
    }

    public HomeBannerViewpager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.HomeBannerViewpager);
            mLeftAndRightMargin = ta.getDimensionPixelSize(R.styleable.HomeBannerViewpager_lib_homebannerviewpager_leftright_padding, 0);
            ta.recycle();
        }
        init();
    }

    private void init() {
        setClipToPadding(false);
        setOffscreenPageLimit(3);
        setPadding(mLeftAndRightMargin, 0, mLeftAndRightMargin, 0);
        setPageTransformer(false, new HomeBannerViewTransform());
        setOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == 0) {//滑动完成
                    mScrollFinish = true;
                } else {
                    mScrollFinish = false;
                }
            }
        });
    }

    public void setMyAdapter(HomeBannerViewAdapter myAdapter) {
        setAdapter(myAdapter);
        setCurrentItem(myAdapter.getData().size() * 20);
    }

    public void start() {
        if (getAdapter() == null || getAdapter().getCount() <= 0) return;
        if (mCountDownTimer != null) mCountDownTimer.cancel();
        mCountDownTimer = new CountDownTimer(Long.MAX_VALUE, 3000) {
            @Override
            public void onTick(long l) {
                if (mScrollFinish) {
                    setCurrentItem(getCurrentItem() + 1);
                }
            }

            @Override
            public void onFinish() {
                mCountDownTimer = null;
            }
        };
        mCountDownTimer.start();
    }

    public void stop() {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
            mCountDownTimer = null;
        }
    }

}




















