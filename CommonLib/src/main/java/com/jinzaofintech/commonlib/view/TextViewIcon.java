package com.jinzaofintech.commonlib.view;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.jinzaofintech.commonlib.app.BaseApplication;

/**
 * Created by zengwendi on 2017/3/3.
 * 矢量图
 */

public class TextViewIcon extends AppCompatTextView {
    public TextViewIcon(Context context) {
        super(context);
        init(context);
    }


    public TextViewIcon(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TextViewIcon(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setTypeface(BaseApplication.getInstance().getIconfont(context));
    }

}
