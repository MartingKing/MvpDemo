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
import com.jinzaofintech.commonlib.view.PickerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Author : zhoujiulong
 * Email : 754667445@qq.com
 * Time : 2017/12/20
 * 描述 : 三项选择弹窗
 */

public class ThreeOptionsDialog extends Dialog implements View.OnClickListener, PickerView.onSelectListener {

    private List<PickerView.Item> mOptionOneDatas;
    private HashMap<PickerView.Item, List<PickerView.Item>> mOptionTwoDatas;
    private HashMap<PickerView.Item, List<PickerView.Item>> mOptionThreeDatas;
    private String mTitle;
    private OnChoiceListener mOnChoiceListener;

    private PickerView mPickerViewOne;
    private PickerView mPickerViewTwo;
    private PickerView mPickerViewThree;

    /**
     * @param context   Context
     * @param provinces 第一项 不能为空
     * @param citys     第二项 如 (省份 ：城市集合) 可为空
     * @param countys   第三项 如（城市 ：县城或区域集合）可为空
     */
    public static ThreeOptionsDialog build(Context context, List<PickerView.Item> provinces, HashMap<PickerView.Item, List<PickerView.Item>> citys, HashMap<PickerView.Item, List<PickerView.Item>> countys) {
        return new ThreeOptionsDialog(context, provinces, citys, countys);
    }

    private ThreeOptionsDialog(@NonNull Context context, List<PickerView.Item> provinces, HashMap<PickerView.Item, List<PickerView.Item>> citys, HashMap<PickerView.Item, List<PickerView.Item>> countys) {
        super(context, R.style.LibCommonDialog);
        mOptionOneDatas = provinces;
        if (citys != null && citys.size() > 0) {
            mOptionTwoDatas = citys;
        }
        if (countys != null && countys.size() > 0) {
            mOptionThreeDatas = countys;
        }
    }

    public ThreeOptionsDialog setTitle(String title) {
        mTitle = title;
        return this;
    }

    public ThreeOptionsDialog setOnChoiceListener(OnChoiceListener listener) {
        mOnChoiceListener = listener;
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

        initView();
        setData();
        initListener();
    }

    private void initView() {
        setContentView(R.layout.dialog_three_options);
        TextView title = findViewById(R.id.tv_title);
        if (mTitle != null) title.setText(mTitle);
        mPickerViewOne = findViewById(R.id.picker_one);
        mPickerViewTwo = findViewById(R.id.picker_two);
        mPickerViewThree = findViewById(R.id.picker_three);
        if (mOptionOneDatas == null || mOptionOneDatas.size() == 0) {
            mPickerViewOne.setVisibility(View.GONE);
        }
        if (mOptionTwoDatas == null || mOptionTwoDatas.size() == 0) {
            mPickerViewTwo.setVisibility(View.GONE);
        }
        if (mOptionThreeDatas == null || mOptionThreeDatas.size() == 0) {
            mPickerViewThree.setVisibility(View.GONE);
        }
    }

    private void setData() {
        if (mOptionOneDatas != null && mOptionOneDatas.size() > 0) {
            mPickerViewOne.setData(mOptionOneDatas);
            setCityShowData(mPickerViewOne.getData().get(mPickerViewOne.getSelected()));
        }
    }

    private void setCityShowData(PickerView.Item item) {
        if (mOptionTwoDatas != null && mOptionTwoDatas.size() > 0) {
            List<PickerView.Item> currentCitys = mOptionTwoDatas.get(item);
            if (currentCitys == null) currentCitys = new ArrayList<>();
            if (currentCitys.size() == 0) currentCitys.add(new PickerView.Item("", ""));
            mPickerViewTwo.setData(currentCitys);
            setCountyShowData(mPickerViewTwo.getData().get(mPickerViewTwo.getSelected()));
        }
    }

    private void setCountyShowData(PickerView.Item item) {
        if (mOptionThreeDatas != null && mOptionThreeDatas.size() > 0) {
            List<PickerView.Item> currentCountys = mOptionThreeDatas.get(item);
            if (currentCountys == null) currentCountys = new ArrayList<>();
            if (currentCountys.size() == 0) currentCountys.add(new PickerView.Item("", ""));
            mPickerViewThree.setData(currentCountys);
        }
    }

    private void initListener() {
        findViewById(R.id.tv_cancel).setOnClickListener(this);
        findViewById(R.id.tv_sure).setOnClickListener(this);
        if (mOptionOneDatas != null && mOptionOneDatas.size() > 0) {
            mPickerViewOne.setOnSelectListener(this);
        }
        if (mOptionTwoDatas != null && mOptionTwoDatas.size() > 0) {
            mPickerViewTwo.setOnSelectListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        dismiss();
        if (id == R.id.tv_sure) {//确定
            if (mOnChoiceListener != null) {
                PickerView.Item optionOne = null;
                PickerView.Item optionTwo = null;
                PickerView.Item optionThree = null;
                if (mOptionOneDatas != null && mOptionOneDatas.size() > 0) {
                    optionOne = mPickerViewOne.getData().get(mPickerViewOne.getSelected());
                }
                if (mOptionTwoDatas != null && mOptionTwoDatas.size() > 0) {
                    optionTwo = mPickerViewTwo.getData().get(mPickerViewTwo.getSelected());
                }
                if (mOptionThreeDatas != null && mOptionThreeDatas.size() > 0) {
                    optionThree = mPickerViewThree.getData().get(mPickerViewThree.getSelected());
                }
                mOnChoiceListener.choice(optionOne, optionTwo, optionThree);
            }
        }
    }

    @Override
    public void onSelect(PickerView view, PickerView.Item item) {
        int id = view.getId();
        if (id == R.id.picker_one) {
            setCityShowData(item);
        } else if (id == R.id.picker_two) {
            setCountyShowData(item);
        }
    }

    public interface OnChoiceListener {
        void choice(PickerView.Item optionOne, PickerView.Item optionTwo, PickerView.Item optionThree);
    }
}



























