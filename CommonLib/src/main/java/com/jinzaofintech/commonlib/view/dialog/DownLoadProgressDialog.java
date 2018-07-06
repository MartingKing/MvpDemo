package com.jinzaofintech.commonlib.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.jinzaofintech.commonlib.R;
import com.jinzaofintech.commonlib.view.SimpleProgressView;

/**
 * Author : zhoujiulong
 * Email : 754667445@qq.com
 * Time : 2018/02/06
 * 描述 : 带下载进度的弹窗
 */

public class DownLoadProgressDialog extends Dialog {

    public static DownLoadProgressDialog build(Context context) {
        return new DownLoadProgressDialog(context);
    }

    private String mTitle;
    private int mProgress = 0;
    private boolean mIsBackDismiss = true;

    private TextView mTvTitle;
    private SimpleProgressView mSimpleProgressView;
    private TextView mTvProgress;

    private DownLoadProgressDialog(@NonNull Context context) {
        super(context, R.style.LibCommonDialog);
    }

    public DownLoadProgressDialog setTitle(String title) {
        if (title == null) return this;
        mTitle = title;
        if (mTvTitle != null) mTvTitle.setText(title);
        return this;
    }

    public DownLoadProgressDialog setProgress(int progress) {
        if (progress < 0) {
            mProgress = progress;
        } else if (progress > 100) {
            mProgress = 100;
        } else {
            mProgress = progress;
        }
        if (mSimpleProgressView != null) {
            mSimpleProgressView.setProgress(mProgress);
        }
        if (mTvProgress != null) {
            mTvProgress.setText(String.valueOf(mProgress + "%"));
        }
        return this;
    }

    public DownLoadProgressDialog setBackDismiss(boolean isBackDismiss) {
        mIsBackDismiss = isBackDismiss;
        return this;
    }

    @Override
    public void onBackPressed() {
        if (mIsBackDismiss) {
            super.onBackPressed();
        }
    }

    @Override
    public void show() {
        super.show();
        Window win = getWindow();
        if (win != null) {
            win.getDecorView().setPadding(0, 0, 0, 0);
            WindowManager.LayoutParams lp = win.getAttributes();
            lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.gravity = Gravity.CENTER;
            win.setAttributes(lp);
            setCancelable(true);
            setCanceledOnTouchOutside(false);
        }
        setContentView(R.layout.dialog_download_progressview);

        mTvTitle = findViewById(R.id.tv_title);
        mSimpleProgressView = findViewById(R.id.progressview);
        mTvProgress = findViewById(R.id.tv_progress);

        if (!TextUtils.isEmpty(mTitle)) mTvTitle.setText(mTitle);
        mSimpleProgressView.setProgress(mProgress);
        mTvProgress.setText(String.valueOf(mProgress + "%"));
    }
}





















