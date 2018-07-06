package com.jinzaofintech.rebate.app;

import android.app.Activity;


import com.jinzaofintech.commonlib.app.ActivityManager;
import com.jinzaofintech.rebate.MainActivity;

import java.util.Stack;

/**
 * Created by zengwendi on 2017/12/27.
 * appActivity管理工具类
 */

public class AppActivityManager {

    /**
     * 除去首页结束所以activity
     */
    public static  void finishAllActivity(){
        Stack<Activity> stack = ActivityManager.getActivityManager().getActivityStack();
        if (stack==null || stack.size()==0) return;
        for (int i = stack.size()-1; i >= 0; i--) {
            Activity activity = stack.get(i);
            if (!(activity instanceof MainActivity)){
                activity.finish();
            }
        }
    }
}
