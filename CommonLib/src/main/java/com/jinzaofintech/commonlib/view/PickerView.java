package com.jinzaofintech.commonlib.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Paint.Style;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.jinzaofintech.commonlib.R;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 滚动选择器
 *
 * @author chenjing
 */
public class PickerView extends View {

    public static final String TAG = "PickerView";
    /**
     * text之间间距和minTextSize之比
     */
    public static final float MARGIN_ALPHA = 2.8f;
    /**
     * 自动回滚到中间的速度
     */
    public static final float SPEED = 2;

    private List<Item> mDataList = new ArrayList<>();
    /**
     * 选中的位置，这个位置是mDataList的中心位置，一直不变
     */
    private int mCurrentSelected;
    private Paint mPaint;

    private float mMaxTextSize = 60;
    private float mMinTextSize = 30;

    private float mMaxTextAlpha = 255;
    private float mMinTextAlpha = 120;

    private int mColorText = Color.parseColor("#000000");

    private int mViewHeight;
    private int mViewWidth;

    private float mLastDownY;
    /**
     * 滑动的距离
     */
    private float mMoveLen = 0;
    private boolean isInit = false;
    private onSelectListener mSelectListener;
    private Timer timer;
    private MyTimerTask mTask;

    private Handler updateHandler = new InnerHandler(this);

    private static class InnerHandler extends Handler {

        private SoftReference<PickerView> mPickerViewSoftReference;

        InnerHandler(PickerView pickerView) {
            mPickerViewSoftReference = new SoftReference<>(pickerView);
        }

        @Override
        public void handleMessage(Message msg) {
            PickerView pickerView = mPickerViewSoftReference.get();
            if (pickerView != null) {
                if (Math.abs(pickerView.mMoveLen) < SPEED) {
                    pickerView.mMoveLen = 0;
                    if (pickerView.mTask != null) {
                        pickerView.mTask.cancel();
                        pickerView.mTask = null;
                        pickerView.performSelect();
                    }
                } else
                    // 这里mMoveLen / Math.abs(mMoveLen)是为了保有mMoveLen的正负号，以实现上滚或下滚
                    pickerView.mMoveLen = pickerView.mMoveLen - pickerView.mMoveLen / Math.abs(pickerView.mMoveLen) * SPEED;
                pickerView.invalidate();
            }
        }
    }

    public PickerView(Context context) {
        this(context, null);
    }

    public PickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public void setOnSelectListener(onSelectListener listener) {
        mSelectListener = listener;
    }

    private void performSelect() {
        if (mSelectListener != null)
            mSelectListener.onSelect(PickerView.this, mDataList.get(mCurrentSelected));
    }

    public void setData(List<Item> datas) {
        mDataList = datas;
        mInitData = new ArrayList<>(datas);
        mCurrentSelected = datas.size() / 2;
        invalidate();
    }

    public List<Item> getData() {
        return mDataList == null ? new ArrayList<Item>() : mDataList;
    }

    private List<Item> mInitData;

    public void setSelected(int selected) {
        mCurrentSelected = selected;
        mDataList = new ArrayList<>(mInitData);
        if (mCurrentSelected < mDataList.size() / 3) {
            mCurrentSelected += 1;
            mDataList.add(0, mDataList.remove(mDataList.size() - 1));
        }
        if (mCurrentSelected > mDataList.size() * 2 / 3) {
            mCurrentSelected -= 1;
            mDataList.add(mDataList.remove(0));
        }
        invalidate();
    }

    public int getSelected() {
        return mCurrentSelected;
    }

    private void moveHeadToTail() {
        Item head = mDataList.get(0);
        mDataList.remove(0);
        mDataList.add(head);
    }

    private void moveTailToHead() {
        Item tail = mDataList.get(mDataList.size() - 1);
        mDataList.remove(mDataList.size() - 1);
        mDataList.add(0, tail);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mViewHeight = getMeasuredHeight();
        mViewWidth = getMeasuredWidth();
        isInit = true;
        invalidate();
    }

    private void init(Context context, AttributeSet attrs) {

        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.PickerView);
            mMaxTextSize = ta.getDimensionPixelSize(R.styleable.PickerView_lib_picker_text_size, 60);
            mMinTextSize = ta.getDimensionPixelSize(R.styleable.PickerView_lib_picker_min_text_size, 30);
            mColorText = ta.getColor(R.styleable.PickerView_lib_picker_text_color, mColorText);
            ta.recycle();
        }

        timer = new Timer();
        mDataList = new ArrayList<>();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Style.FILL);
        mPaint.setTextAlign(Align.CENTER);
        mPaint.setColor(mColorText);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 根据index绘制view
        if (isInit)
            drawData(canvas);
    }

    private void drawData(Canvas canvas) {
        // 先绘制选中的text再往上往下绘制其余的text
        float scale = parabola(mViewHeight / 4.0f, mMoveLen);
        float size = (mMaxTextSize - mMinTextSize) * scale + mMinTextSize;
        mPaint.setTextSize(size);
        mPaint.setAlpha((int) ((mMaxTextAlpha - mMinTextAlpha) * scale + mMinTextAlpha));
        // text居中绘制，注意baseline的计算才能达到居中，y值是text中心坐标
        float x = (float) (mViewWidth / 2.0);
        float y = (float) (mViewHeight / 2.0 + mMoveLen);
        FontMetricsInt fmi = mPaint.getFontMetricsInt();
        float baseline = (float) (y - (fmi.bottom / 2.0 + fmi.top / 2.0));

        if (mDataList != null) {
            if (mDataList.size() > mCurrentSelected)
                canvas.drawText(mDataList.get(mCurrentSelected).str, x, baseline, mPaint);
            // 绘制上方data
            for (int i = 1; (mCurrentSelected - i) >= 0; i++) {
                drawOtherText(canvas, i, -1);
            }
            // 绘制下方data
            for (int i = 1; (mCurrentSelected + i) < mDataList.size(); i++) {
                drawOtherText(canvas, i, 1);
            }
        }
    }

    /**
     * @param position 距离mCurrentSelected的差值
     * @param type     1表示向下绘制，-1表示向上绘制
     */
    private void drawOtherText(Canvas canvas, int position, int type) {
        float d = MARGIN_ALPHA * mMinTextSize * position + type * mMoveLen;
        float scale = parabola(mViewHeight / 4.0f, d);
        float size = (mMaxTextSize - mMinTextSize) * scale + mMinTextSize;
        mPaint.setTextSize(size);
        mPaint.setAlpha((int) ((mMaxTextAlpha - mMinTextAlpha) * scale + mMinTextAlpha));
        float y = (float) (mViewHeight / 2.0 + type * d);
        FontMetricsInt fmi = mPaint.getFontMetricsInt();
        float baseline = (float) (y - (fmi.bottom / 2.0 + fmi.top / 2.0));
        canvas.drawText(mDataList.get(mCurrentSelected + type * position).str, (float) (mViewWidth / 2.0), baseline, mPaint);
    }

    /**
     * 抛物线
     *
     * @param zero 零点坐标
     * @param x    偏移量
     * @return scale
     */
    private float parabola(float zero, float x) {
        float f = (float) (1 - Math.pow(x / zero, 2));
        return f < 0 ? 0 : f;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                doDown(event);
                break;
            case MotionEvent.ACTION_MOVE:
                doMove(event);
                break;
            case MotionEvent.ACTION_UP:
                doUp(event);
                break;
        }
        return true;
    }

    private void doDown(MotionEvent event) {
        if (mTask != null) {
            mTask.cancel();
            mTask = null;
        }
        mLastDownY = event.getY();
    }

    private void doMove(MotionEvent event) {

        mMoveLen += (event.getY() - mLastDownY);

        if (mMoveLen > MARGIN_ALPHA * mMinTextSize / 2) {
            // 往下滑超过离开距离
            moveTailToHead();
            mMoveLen = mMoveLen - MARGIN_ALPHA * mMinTextSize;
        } else if (mMoveLen < -MARGIN_ALPHA * mMinTextSize / 2) {
            // 往上滑超过离开距离
            moveHeadToTail();
            mMoveLen = mMoveLen + MARGIN_ALPHA * mMinTextSize;
        }

        mLastDownY = event.getY();
        invalidate();
    }

    private void doUp(MotionEvent event) {
        // 抬起手后mCurrentSelected的位置由当前位置move到中间选中位置
        if (Math.abs(mMoveLen) < 0.0001) {
            mMoveLen = 0;
            return;
        }
        if (mTask != null) {
            mTask.cancel();
            mTask = null;
        }
        mTask = new MyTimerTask(updateHandler);
        timer.schedule(mTask, 0, 10);
    }

    class MyTimerTask extends TimerTask {
        Handler handler;

        public MyTimerTask(Handler handler) {
            this.handler = handler;
        }

        @Override
        public void run() {
            this.handler.sendMessage(this.handler.obtainMessage());
        }

    }

    public interface onSelectListener {
        void onSelect(PickerView view, Item text);
    }

    public void destory() {
        if (timer != null) timer.cancel();
        if (updateHandler != null) updateHandler.removeCallbacksAndMessages(null);
        timer = null;
        updateHandler = null;
    }

    public static class Item {
        public String str;
        public String id;

        public Item(String id, String str) {
            this.id = id;
            this.str = str;
        }
    }
}
