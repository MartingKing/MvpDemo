package com.jinzaofintech.commonlib.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.jinzaofintech.commonlib.R;

/**
 * Author : zhoujiulong
 * Email : 754667445@qq.com
 * Time : 2018/02/06
 * 描述 : 只有背景的进度条
 */

public class SimpleProgressView extends View {

    private int mProgress = 0;
    private int mWidth;
    private int mHeight;
    private Paint mPaint;
    private int mProgressColor = Color.BLACK;

    public SimpleProgressView(Context context) {
        this(context, null);
    }

    public SimpleProgressView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimpleProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SimpleProgressView);
            mProgressColor = ta.getColor(R.styleable.SimpleProgressView_lib_progress_color, Color.BLACK);
            ta.recycle();
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int progressWidth = (mWidth * mProgress) / 100;
        mPaint.setColor(mProgressColor);
        @SuppressLint("DrawAllocation")
        Rect rect = new Rect(0, 0, progressWidth, mHeight);
        canvas.drawRect(rect, mPaint);
    }

    public void setProgress(int progress) {
        if (progress < 0) {
            mProgress = progress;
        } else if (progress > 100) {
            mProgress = 100;
        } else {
            mProgress = progress;
        }
        invalidate();
    }
}

















