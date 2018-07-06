package com.jinzaofintech.commonlib.utils;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

/**
 * Created by zengwendi on 2018/1/29.
 * App工具类
 *
 * @author zengwendi
 * @date 2018/1/29.
 */

public class AppUtils {


    /**
     * 判断应用是否已经启动
     *
     * @param context     一个context
     * @param packageName 要判断应用的包名
     * @return boolean
     */
    public static boolean isAppAlive(Context context, String packageName) {
        ActivityManager activityManager =
                (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> processInfos
                = activityManager.getRunningAppProcesses();
        for (int i = 0; i < processInfos.size(); i++) {
            if (processInfos.get(i).processName.equals(packageName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断MainActivity是否活动
     *
     * @param context      一个context
     * @param activityName 要判断Activity
     * @return boolean
     */
    public static boolean isMainActivityAlive(Context context, String activityName) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(100);
        for (ActivityManager.RunningTaskInfo info : list) {
            // 注意这里的 topActivity 包含 packageName和className，可以打印出来看看
            if (info.topActivity.toString().equals(activityName) || info.baseActivity.toString().equals(activityName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 检测某Activity是否在当前Task的栈顶
     *
     * @param context      一个context
     * @param activityName 要判断Activity
     * @return boolean
     */
    public static boolean isTopActivity(Context context, String activityName) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(1);
        String cmpNameTemp = null;
        if (runningTaskInfos != null) {
            cmpNameTemp = runningTaskInfos.get(0).topActivity.toString();
        }
        if (cmpNameTemp == null) {
            return false;
        }
        return cmpNameTemp.equals(activityName);
    }

    /**
     * 获取app 包名
     *
     * @return 包名
     */
    public static String getPackageName() {
        try {
            String pkName = ContextUtils.getContext().getPackageName();
            return pkName;
        } catch (Exception e) {
        }
        return null;
    }
}
