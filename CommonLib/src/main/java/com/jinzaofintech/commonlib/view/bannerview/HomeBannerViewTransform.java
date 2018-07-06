package com.jinzaofintech.commonlib.view.bannerview;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * Author : zhoujiulong
 * Email : 754667445@qq.com
 * Time : 2017/12/14
 * 描述 :
 */

public class HomeBannerViewTransform implements ViewPager.PageTransformer {

    final float SCALE_MAX = 0.8f;

    @Override
    public void transformPage(View page, float position) {
        float scale;
        if (position <= 0) {
            scale = (1 - SCALE_MAX) * position + 1;
        } else if (position <= 1) {
            scale = (SCALE_MAX - 1) * position + 1;
        } else {
            scale = (SCALE_MAX - 1) * (position - 1) + 1 - (1 - SCALE_MAX);
        }
        page.setPivotX(page.getWidth() / 2);
        page.setPivotY(page.getHeight() / 2);
        page.setScaleY(scale);
    }
}
