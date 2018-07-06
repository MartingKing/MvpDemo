package com.jinzaofintech.commonlib.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.jinzaofintech.commonlib.utils.ScreenUtils;

/**
 * Author : zhoujiulong
 * Email : 754667445@qq.com
 * Time : 2018/03/05
 * 描述 : 设置投资进度控件
 */

public class SetInvestmentQutaView extends View implements GestureDetector.OnGestureListener {

    private int mPercentWidth;
    private int mBeginX;
    private int mPercentCenterY;
    private int mTextCenterY;
    //滑动的距离
    private int mScrollX = 0;
    private int mStartNumber = 1000;

    private Paint mPaint;
    private int mColor = Color.parseColor("#FF6046");

    public SetInvestmentQutaView(Context context) {
        this(context, null);
    }

    public SetInvestmentQutaView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SetInvestmentQutaView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initData();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mPercentWidth = ScreenUtils.getScreenWidth(getContext()) / 40;
        mBeginX = w / 2;
        mPercentCenterY = h / 2 - 30;
        mTextCenterY = h / 2 + 30;
    }

    private void initData() {
        mPaint = new Paint();
        mPaint.setColor(mColor);
        mPaint.setAntiAlias(true);
        mGestureDetector = new GestureDetector(getContext(), this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //如果手动设置了投资额度，计算移动距离
        if (mSetInvestmentQuta >= mStartNumber) {
            mScrollX = ((mSetInvestmentQuta - mStartNumber) / 1000) * mPercentWidth;
            mSetInvestmentQuta = -1;
        }
        //绘制进度横线
        int lineStartX = mBeginX - mScrollX;
        int lineEndX = lineStartX + mPercentWidth * (50 - mStartNumber / 1000);
        //中间左侧的绘制红色进度横线
        if (lineStartX < mBeginX) {
            mPaint.setColor(mColor);
            canvas.drawRect(lineStartX, mPercentCenterY - 1.5F, mBeginX, mPercentCenterY + 1.5F, mPaint);
        }
        //中间右侧的绘制灰色的进度横线
        mPaint.setColor(Color.parseColor("#F5F5F5"));
        canvas.drawRect(mBeginX, mPercentCenterY - 1.5F, lineEndX, mPercentCenterY + 1.5F, mPaint);
        //绘制进度刻度和进度文字
        for (int i = mStartNumber / 1000; i <= 50; i++) {
            if (i == mStartNumber / 1000 || i % 10 == 0) {
                //绘制进度刻度
                int start = lineStartX + mPercentWidth * (i - mStartNumber / 1000);
                if (start < mBeginX) {
                    mPaint.setColor(mColor);
                } else {
                    mPaint.setColor(Color.parseColor("#AAAAAA"));
                }
                canvas.drawRect(start - 1.5F, mPercentCenterY - 9, start + 1.5F, mPercentCenterY + 9, mPaint);
                //绘制进度文字
                mPaint.setTextSize(36);
                mPaint.setTextAlign(Paint.Align.CENTER);
                if (start > mBeginX) {
                    mPaint.setColor(Color.parseColor("#666666"));
                } else {
                    mPaint.setColor(mColor);
                }
                canvas.drawText(String.valueOf(i * 1000), start, mTextCenterY, mPaint);
            }
        }
        //绘制中间刻度
        mPaint.setColor(mColor);
        canvas.drawRect(mBeginX - 2.5F, mPercentCenterY - 15, mBeginX + 2.5F, mPercentCenterY + 15, mPaint);
    }

    private GestureDetector mGestureDetector;
    private boolean mIsDown = false;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mIsDown = true;
                break;
            case MotionEvent.ACTION_UP:
                mIsDown = false;
                break;
        }
        mGestureDetector.onTouchEvent(event);
        return true;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        mScrollX += distanceX;
        if (mScrollX < 0) {
            mScrollX = 0;
        }
        if (mScrollX > mPercentWidth * (50 - mStartNumber / 1000)) {
            mScrollX = mPercentWidth * (50 - mStartNumber / 1000);
        }
        invalidate();
        noticyDataChange();
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        fling(velocityX);
        return true;
    }

    private void fling(final float velocityX) {
        if (mIsDown) return;
        if (velocityX < 0 && velocityX < -300) {//向左滑动
            mScrollX += mPercentWidth;
            if (mScrollX > mPercentWidth * (50 - mStartNumber / 1000)) {
                mScrollX = mPercentWidth * (50 - mStartNumber / 1000);
            }
            invalidate();
            noticyDataChange();
            if (mScrollX < mPercentWidth * (50 - mStartNumber / 1000)) {
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        fling(velocityX + 300);
                    }
                }, 15);
            }
        } else if (velocityX > 0 && velocityX > 300) {//向右滑动
            mScrollX -= mPercentWidth;
            if (mScrollX < 0) {
                mScrollX = 0;
            }
            invalidate();
            noticyDataChange();
            if (mScrollX > 0) {
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        fling(velocityX - 300);
                    }
                }, 15);
            }
        }
    }

    private void noticyDataChange() {
        if (mOnInvestmentQutaChangeListener != null) {
            mOnInvestmentQutaChangeListener.onInvestmentQutaChange(((mScrollX + mPercentWidth / 10) / mPercentWidth) * 1000 + mStartNumber);
        }
    }

    private OnInvestmentQutaChangeListener mOnInvestmentQutaChangeListener;

    public void setOnInvestmentQutaChangeListener(OnInvestmentQutaChangeListener listener) {
        mOnInvestmentQutaChangeListener = listener;
    }

    public interface OnInvestmentQutaChangeListener {
        void onInvestmentQutaChange(int investmentQuta);
    }

    public int getInvestmentQuta() {
        return ((mScrollX + mPercentWidth / 10) / mPercentWidth) * 1000 + mStartNumber;
    }

    public boolean isScroll() {
        return mIsDown;
    }

    /**
     * 初始化时设置的投资额度
     */
    private int mSetInvestmentQuta = -1;

    public void setInvestmentQuta(int investmentQuta) {
        if (investmentQuta >= mStartNumber && investmentQuta <= 50000) {
            mSetInvestmentQuta = investmentQuta;
            postInvalidate();
        }
    }

}




















