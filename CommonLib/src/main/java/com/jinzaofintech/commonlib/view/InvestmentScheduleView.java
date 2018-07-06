package com.jinzaofintech.commonlib.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.jinzaofintech.commonlib.R;

import java.math.RoundingMode;
import java.text.NumberFormat;

/**
 * Author : zhoujiulong
 * Email : 754667445@qq.com
 * Time : 2017/12/14
 * 描述 : 投资进度表
 */

public class InvestmentScheduleView extends View {

    //竖向分割成多少段
    private int mYDivideSize = 4;
    //横向分割间隔
    private int mXDivideValue = 1;
    //竖向最小值
    private double mYMinValue = 10D;
    //竖向最大值
    private double mYMaxValue = 13D;
    //横向最小值
    private double mXMinValue = 1D;
    //横向最大值
    private double mXMaxValue = 13D;
    //竖向单位
    private String mYCompany = "%";
    //横向单位
    private String mXCompany = "周";
    //控件宽度
    private int mWidth;
    //控件高度
    private int mHeight;

    //当前Y方向值
    private double mYCurrentValue = 12D;
    //当前X方向值
    private double mXCurrentValue = 10D;
    //是否显示提示
    private boolean mNeedShowMsg = true;
    //是否显示内容，默认不显示
    private boolean mNeedShow = false;

    public InvestmentScheduleView(Context context) {
        this(context, null);
    }

    public InvestmentScheduleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public InvestmentScheduleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        mTextSize = mWidth * 3 / 100;
        //底部文字距离底部1倍文字大小，距离顶部1.5倍文字大小，底部文字距离表格0.8倍文字大小
        //左边右边距离边距5.5%宽度，横向第一个坐标距离左边边距(最小值0 15.5 其他 8)，5.5%到15.5%之间绘制Y方向数值
        mBottomMargin = mTextSize;
        mTopMargin = mTextSize;
        mBottomTextTopMargin = mTextSize * 8 / 10;
        mLeftFormMargin = mXMinValue == 0 ? mWidth * 155 / 1000 : mWidth / 10;
        mLeftMargin = mWidth * 55 / 1000;
        mRightMargin = mLeftMargin;
        calculationDivideSize();
    }

    //计算表横竖向每格的宽度
    private void calculationDivideSize() {
        int temp = mXDivideValue > 3 ? 0 : 1;
        mFormXDivideWidth = (int) ((mWidth - mLeftFormMargin - mRightMargin) / (mXMaxValue + (mXDivideValue + temp) / 2));
        mFormYDivideWidth = (mHeight - mBottomMargin - mTextSize - mBottomTextTopMargin - mTopMargin) / mYDivideSize;
    }

    //底部的留空
    private int mBottomMargin;
    //顶部的留空
    private int mTopMargin;
    //底部文字上部的留空
    private int mBottomTextTopMargin;
    //真正绘制的表格的左部留空
    private int mLeftFormMargin;
    //左部的留空
    private int mLeftMargin;
    //右部的留空
    private int mRightMargin;
    //表格横向每格宽度
    private int mFormXDivideWidth;
    //表格竖向每格宽度
    private int mFormYDivideWidth;
    //字体大小
    private int mTextSize;
    //画笔
    private Paint mPaint;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!mNeedShow) return;

        //绘制Y方向的线条和左侧单位
        int startY = mTopMargin;
        mPaint.setColor(Color.WHITE);
        mPaint.setTextSize(mTextSize);
        mPaint.setStrokeWidth(1);
        int formLineEndX = mWidth - mRightMargin;
        String stry;
        for (int i = 1; i <= mYDivideSize; i++) {
            //绘制线
            startY += mFormYDivideWidth;
            canvas.drawLine(mLeftMargin, startY, formLineEndX, startY, mPaint);
            //绘制左侧单位
            if (i == 1) {
                stry = formatValue(mYMaxValue) + mYCompany;
            } else if (i == mYDivideSize) {
                stry = formatValue(mYMinValue) + mYCompany;
            } else {
                stry = formatValue((mYMaxValue - (mYMaxValue - mYMinValue) * (i - 1) / (mYDivideSize - 1))) + mYCompany;
            }
            canvas.drawText(stry, mLeftMargin, startY - mTextSize / 3, mPaint);
        }

        //绘制底部文字
        startY += mBottomTextTopMargin + mTextSize;
        mPaint.setTextSize(mTextSize);
        String str;
        int startX = mLeftFormMargin;
        str = formatValue(mXMinValue);
        canvas.drawText(str, startX + mFormXDivideWidth, startY, mPaint);
        for (int i = mXDivideValue; i < mXMaxValue - mXDivideValue / 2; i += mXDivideValue) {
            str = formatValue(i);
            startX += mFormXDivideWidth * mXDivideValue;
            canvas.drawText(str, startX, startY, mPaint);
        }
        str = formatValue(mXMaxValue) + mXCompany;
        startX += mFormXDivideWidth * mXDivideValue;
        canvas.drawText(str, startX, startY, mPaint);

        //绘制进度和提示信息
        float startPointX = (float) (mLeftFormMargin + mFormXDivideWidth * mXMinValue + mTextSize / 4);
        int endX = (int) (mXCurrentValue * mFormXDivideWidth + mLeftFormMargin + mTextSize / 4);
        int formHeight = mHeight - mTopMargin - mBottomTextTopMargin - mTextSize - mBottomMargin - mFormYDivideWidth;
        double valueHeight = (mYCurrentValue - mYMinValue) / (mYMaxValue - mYMinValue) * formHeight;
        int endY = (int) (formHeight - valueHeight + mTopMargin + mFormYDivideWidth);
        //绘制进度线条
        mPaint.setColor(Color.WHITE);
        mPaint.setStrokeWidth(3);
        canvas.drawLine(startPointX, mTopMargin + formHeight + mFormYDivideWidth, endX, endY, mPaint);
        //绘制终点圆环
        mPaint.setColor(Color.WHITE);
        canvas.drawCircle(endX, endY, mTextSize / 3, mPaint);
        mPaint.setColor(Color.parseColor("#FFD8D7"));
        canvas.drawCircle(endX, endY, mTextSize / 4, mPaint);
        //绘制起点的圆环
        mPaint.setColor(Color.WHITE);
        canvas.drawCircle(startPointX, mTopMargin + formHeight + mFormYDivideWidth, mTextSize / 3, mPaint);
        mPaint.setColor(Color.parseColor("#FFD8D7"));
        canvas.drawCircle(startPointX, mTopMargin + formHeight + mFormYDivideWidth, mTextSize / 4, mPaint);

        //如果需要弹提示
        if (mNeedShowMsg) {
            //图片大小是文字的 3.5 倍
            float picWidth = mTextSize * 3.7F;
            Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.ic_msg_toast);
            float scale = 1;
            if (bitmap.getWidth() > 0) {
                scale = picWidth / bitmap.getWidth();
            }
            Matrix matrix = new Matrix();
            matrix.setScale(scale, scale);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            int bitmapWidth = bitmap.getWidth();
            int bitmapHeight = bitmap.getHeight();
            float bgLeft = endX - bitmapWidth / 2;
            float bgTop = endY - mTextSize * 2 / 3 - bitmapHeight;
            canvas.drawBitmap(bitmap, bgLeft, bgTop, mPaint);

            //绘制当前收益
            Rect rect = new Rect();
            mPaint.setTextSize(mTextSize);
            mPaint.setColor(Color.parseColor("#FE6046"));
            String strDraw = formatValue(mYCurrentValue) + "%";
            mPaint.getTextBounds(strDraw, 0, strDraw.length(), rect);
            int strWidth = rect.width();
            int strHeight = rect.height();
            int strStartX = endX - strWidth / 2;
            float strStartY = bgTop + (bitmapHeight * 5 / 7 - strHeight) / 2 + strHeight;
            canvas.drawText(strDraw, strStartX, strStartY, mPaint);
        }
    }

    private String formatValue(double value) {
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMaximumFractionDigits(2);
        nf.setRoundingMode(RoundingMode.HALF_UP);
        nf.setGroupingUsed(false);
        return nf.format(value);
    }

    /**
     * 设置当前值
     *
     * @param xValue 横向值
     * @param yValue 竖向值
     */
    public void setCurrentValue(double xValue, double yValue) {
        mXCurrentValue = xValue;
        mYCurrentValue = yValue;
        mNeedShow = true;
        invalidate();
    }

    /**
     * 竖向单位
     */
    public void setYCompany(String YCompany) {
        mYCompany = YCompany;
        mNeedShow = true;
        invalidate();
    }

    /**
     * 竖向分割成多少段
     */
    public void setYDivideSize(int YDivideSize) {
        mYDivideSize = YDivideSize;
        mNeedShow = true;
        calculationDivideSize();
        invalidate();
    }

    /**
     * 竖向最小值
     */
    public void setYMinValue(double YMinValue) {
        mYMinValue = YMinValue;
        calculationDivideSize();
        invalidate();
    }

    /**
     * 竖向最大值
     */
    public void setYMaxValue(double YMaxValue) {
        mYMaxValue = YMaxValue;
        mNeedShow = true;
        calculationDivideSize();
        invalidate();
    }

    /**
     * 横向单位
     */
    public void setXCompany(String XCompany) {
        mXCompany = XCompany;
        mNeedShow = true;
        invalidate();
    }

    /**
     * 横向最小值
     */
    public void setXMinValue(double XMinValue) {
        mXMinValue = XMinValue;
        mNeedShow = true;
        calculationDivideSize();
        invalidate();
    }

    /**
     * 横向最大值
     */
    public void setXMaxValue(double XMaxValue) {
        mXMaxValue = XMaxValue;
        mNeedShow = true;
        calculationDivideSize();
        invalidate();
    }

    /**
     * 横向分割每段间隔
     */
    public void setXDivideValue(int XDivideValue) {
        mXDivideValue = XDivideValue;
        mNeedShow = true;
        calculationDivideSize();
        invalidate();
    }


    /**
     * 是否显示弹窗信息
     */
    public void setNeedShowMsg(boolean needShowMsg) {
        mNeedShowMsg = needShowMsg;
        mNeedShow = true;
        invalidate();
    }
}

























