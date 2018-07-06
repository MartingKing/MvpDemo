package com.jinzaofintech.commonlib.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.jinzaofintech.commonlib.R;

/**
 * Author : zhoujiulong
 * Email : 754667445@qq.com
 * Time : 2017/12/14
 * 描述 :
 */

public class ProgressView extends View {

    private int mWidth;
    private int mHeight;
    private Paint mPaint;

    private int mPercent = 50;
    private int mPercentLineColor = Color.parseColor("#FFC546");
    private int mPercentRightLineColor = Color.parseColor("#FFD7D7");
    private int mBgColorOne = Color.parseColor("#FBB528");
    private int mBgColorTwo = Color.parseColor("#FFC914");
    private int mTextColor = Color.WHITE;

    public ProgressView(Context context) {
        this(context, null);
    }

    public ProgressView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ProgressView);
            mPercent = ta.getInt(R.styleable.ProgressView_lib_progress, 0);
            mPercentLineColor = ta.getColor(R.styleable.ProgressView_lib_line_color, mPercentLineColor);
            mPercentRightLineColor = ta.getColor(R.styleable.ProgressView_lib_line_right_color, mPercentRightLineColor);
            mBgColorOne = ta.getColor(R.styleable.ProgressView_lib_bg_one_color, mBgColorOne);
            mBgColorTwo = ta.getColor(R.styleable.ProgressView_lib_bg_two_color, mBgColorTwo);
            mTextColor = ta.getColor(R.styleable.ProgressView_lib_text_color, mTextColor);
            ta.recycle();
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //计算中心点，如果 percent 是 0 中心点 x = height, 如果 percent 是 100 中心点 x = width - h
        int currentPercentX = mWidth * mPercent / 100 + (50 - mPercent) * mHeight / 50;
        int currentPercentY = mHeight / 2;

        //绘制进度条
        mPaint.setColor(mPercentLineColor);
        Rect rect = new Rect(0, currentPercentY - mHeight / 14, currentPercentX, currentPercentY + mHeight / 14);
        canvas.drawRect(rect, mPaint);
        mPaint.setColor(mPercentRightLineColor);
        Rect rectRight = new Rect(currentPercentX, currentPercentY - mHeight / 14, mWidth, currentPercentY + mHeight / 14);
        canvas.drawRect(rectRight, mPaint);

        //绘制背景
        Path path = new Path();
        path.moveTo(currentPercentX - mHeight, mHeight);
        path.lineTo(currentPercentX + mHeight / 2, mHeight);
        path.quadTo(currentPercentX + mHeight, mHeight, currentPercentX + mHeight, currentPercentY);
        path.lineTo(currentPercentX + mHeight, 0);
        path.lineTo(currentPercentX - mHeight / 2, 0);
        path.quadTo(currentPercentX - mHeight, 0, currentPercentX - mHeight, currentPercentY);
        path.close();
        LinearGradient linearGradient = new LinearGradient(currentPercentX - mHeight, 0, currentPercentX + mHeight, currentPercentY, mBgColorOne, mBgColorTwo, Shader.TileMode.CLAMP);
        mPaint.setShader(linearGradient);
        canvas.drawPath(path, mPaint);

        //绘制文字
        mPaint.setColor(mTextColor);
        mPaint.setShader(null);
        mPaint.setTextSize(mHeight * 0.72F);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setTextAlign(Paint.Align.CENTER);
        Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
        float top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
        float bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
        canvas.drawText(mPercent + "%", currentPercentX, currentPercentY - top / 2 - bottom / 2, mPaint);
    }

    public void setProgress(int percent) {
        if (percent < 0) {
            mPercent = 0;
        } else if (percent > 100) {
            mPercent = 100;
        } else {
            mPercent = percent;
        }
        invalidate();
    }

}





























