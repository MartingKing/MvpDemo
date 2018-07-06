package com.jinzaofintech.commonlib.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.jinzaofintech.commonlib.R;
import com.jinzaofintech.commonlib.view.PickerViewTwoStr;

import java.util.List;

/**
 * Author : zhoujiulong
 * Email : 754667445@qq.com
 * Time : 2017/12/20
 * 描述 : 选择控件，可配置左右两种文字大小
 */

public class ChoiceListTwoStrDialog extends Dialog implements View.OnClickListener {

    private String mTitle;
    private List<PickerViewTwoStr.Item> mDatas;
    private PickerViewTwoStr mPickerViewTwo;

    public static ChoiceListTwoStrDialog build(Context context, List<PickerViewTwoStr.Item> datas) {
        return new ChoiceListTwoStrDialog(context, datas);
    }

    private ChoiceListTwoStrDialog(@NonNull Context context, List<PickerViewTwoStr.Item> datas) {
        super(context, R.style.LibCommonDialog);
        mDatas = datas;
    }

    public ChoiceListTwoStrDialog setTitle(String title) {
        mTitle = title;
        return this;
    }

    @Override
    public void show() {
        super.show();
        Window win = getWindow();
        if (win != null) {
            win.getDecorView().setPadding(0, 0, 0, 0);
            WindowManager.LayoutParams lp = win.getAttributes();
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.gravity = Gravity.BOTTOM;
            win.setAttributes(lp);
            win.setWindowAnimations(R.style.lib_dialog_bottom_inout_anim);
            setCancelable(true);
            setCanceledOnTouchOutside(true);
        }

        setContentView(R.layout.dialog_choice_list_two_str);
        TextView title = findViewById(R.id.tv_title);
        if (mTitle != null) title.setText(mTitle);
        mPickerViewTwo = findViewById(R.id.pickerview);

        mPickerViewTwo.setData(mDatas);
        findViewById(R.id.tv_cancel).setOnClickListener(this);
        findViewById(R.id.tv_sure).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        dismiss();
        if (id == R.id.tv_sure) {//确定
            if (mListener != null) {
                mListener.choice(mPickerViewTwo.getData().get(mPickerViewTwo.getSelected()));
            }
        }
    }

    private OnChoiceListener mListener;

    public ChoiceListTwoStrDialog setOnChoiceListener(OnChoiceListener listener) {
        mListener = listener;
        return this;
    }

    public interface OnChoiceListener {
        void choice(PickerViewTwoStr.Item item);
    }
}


























