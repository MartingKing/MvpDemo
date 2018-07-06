package com.jinzaofintech.commonlib.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

/**
 * Created by Administrator on 2018/1/10.
 */

public class QRcodeDialog extends Dialog {

    private Context context;
    private int resId;

    public QRcodeDialog(Context context, int resLayout) {
        this(context, 0, 0);
    }

    public QRcodeDialog(Context context, int themeResId, int resLayout) {
        super(context, themeResId);
        this.context = context;
        this.resId = resLayout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(resId);
    }

}
