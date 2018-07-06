package com.jinzaofintech.commonlib.ui.base;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.githang.statusbar.StatusBarCompat;
import com.jinzaofintech.commonlib.R;
import com.jinzaofintech.commonlib.app.ActivityManager;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by zengwendi on 2017/9/21.
 * 所有activity继承的简单activity实现了所有的activity生命周期方法
 * 简单的activity可以做一些初始化的工作
 */

public class SimpleActivity extends AppCompatActivity {
    public View mToolbar;
    private OnBackClickListener mOnBackClickListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        ActivityManager.getActivityManager().addActivity(this);
        setStatusBarColor(false);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //友盟统计的开始
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //友盟统计结束
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityManager.getActivityManager().removeActivity(this);
    }

    /**
     * 设置状态栏颜色默认为白色
     * 如果需要更改不要调用super
     */
    public void setStatusBarColor(boolean lightStatusBar) {
        StatusBarCompat.setStatusBarColor(this, ContextCompat.getColor(this, R.color.colorPrimary));
    }

    /**
     * 设置标题跟返回键
     */
    public void setActionBar(String titleName) {
        setBack();
        setTitle(titleName);
    }

    /**
     * 设置返回键 返回键的描述
     */
    public void setBack() {
        if (mToolbar == null) {
            mToolbar = findViewById(R.id.toolbar);
        }
        if (mToolbar != null && mToolbar.getVisibility() != View.VISIBLE) {
            mToolbar.setVisibility(View.VISIBLE);
        }
        View back = findViewById(R.id.back);
        if (back != null) {
            back.setVisibility(View.VISIBLE);
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onLeftClick();
                }
            });
        }
    }

    /**
     * 带回调的左上角返回按钮
     * @param onBackClickListener
     */
    public void onListenerBack(OnBackClickListener onBackClickListener) {
        this.mOnBackClickListener = onBackClickListener;
        if (mToolbar == null) {
            mToolbar = findViewById(R.id.toolbar);
        }
        if (mToolbar != null && mToolbar.getVisibility() != View.VISIBLE) {
            mToolbar.setVisibility(View.VISIBLE);
        }
        View back = findViewById(R.id.back);
        if (back != null) {
            back.setVisibility(View.VISIBLE);
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnBackClickListener.onBackClicked();
                }
            });
        }

    }

    /**
     * 返回按钮点击事件
     */
    protected void onLeftClick() {
        finish();
    }

    /**
     * 设置标题
     */
    public void setTitle(String titleName) {
        if (mToolbar == null) {
            mToolbar = findViewById(R.id.toolbar);
        }
        if (mToolbar != null && mToolbar.getVisibility() != View.VISIBLE) {
            mToolbar.setVisibility(View.VISIBLE);
        }
        TextView title = findViewById(R.id.toolbar_title);
        if (title != null) title.setText(titleName);
    }

    public interface OnBackClickListener {
        void onBackClicked();
    }
}
