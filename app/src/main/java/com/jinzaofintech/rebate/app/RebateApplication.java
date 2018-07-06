package com.jinzaofintech.rebate.app;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.support.multidex.MultiDex;

import com.goat.imageloader.loader.ImageLoader;
import com.jinzaofintech.commonlib.BuildConfig;
import com.jinzaofintech.commonlib.app.BaseApplication;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.beta.UpgradeInfo;
import com.tencent.bugly.beta.interfaces.BetaPatchListener;
import com.tencent.bugly.beta.upgrade.UpgradeListener;
import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

/**
 * Created by Administrator on 2018/5/31.
 * the application init
 */

public class RebateApplication extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        initTinker();
        UMShareAPI.get(this);
        ImageLoader.init(this);
        PlatformConfig.setWeixin(com.jinzaofintech.commonlib.BuildConfig.WEIXIN_KEY, com.jinzaofintech.commonlib.BuildConfig.WEIXIN_SECRET);
        PlatformConfig.setQQZone(com.jinzaofintech.commonlib.BuildConfig.QQ_KEY, com.jinzaofintech.commonlib.BuildConfig.QQ_SECRET);
        Config.DEBUG= BuildConfig.DEBUG_MODE;
    }
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
        // 安装tinker
        Beta.installTinker();
    }

    @Override
    public void registerActivityLifecycleCallbacks(ActivityLifecycleCallbacks callback) {
        super.registerActivityLifecycleCallbacks(callback);
    }

    private void initTinker() {
        Beta.enableHotfix = true;
        // 设置是否自动下载补丁，默认为true
        Beta.canAutoDownloadPatch = true;
        //在开发测试阶段，可以在初始化Bugly之前通过以下接口把调试设备设置成“开发设备”。
        CrashReport.setIsDevelopmentDevice(getApplicationContext(), BuildConfig.DEBUG);
        //自动检查更新开关
        Beta.autoCheckUpgrade = true;
        Beta.upgradeCheckPeriod = 60 * 1000;
        //APP启动1s后初始化SDK，避免影响APP启动速度;
        Beta.initDelay = 1000;
        // 设置是否自动合成补丁，默认为true
        Beta.canAutoPatch = true;
        // 设置是否提示用户重启，默认为false
        Beta.canNotifyUserRestart = false;
        //设置sd卡的Download为更新资源存储目录
        Beta.storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        //设置Wifi下自动下载
        Beta.autoDownloadOnWifi = false;
        // 补丁回调接口
        Beta.betaPatchListener = new BetaPatchListener() {
            /**
             * 补丁下载地址
             * @param patchFile
             */
            @Override
            public void onPatchReceived(String patchFile) {

            }

            /**
             * 下载进度
             * @param savedLength
             * @param totalLength
             */
            @Override
            public void onDownloadReceived(long savedLength, long totalLength) {
//                Toast.makeText(getApplicationContext(),
//                        String.format(Locale.getDefault(), "%s %d%%",
//                                Beta.strNotificationDownloading,
//                                (int) (totalLength == 0 ? 0 : savedLength * 100 / totalLength)),
//                        Toast.LENGTH_SHORT).show();
            }

            /**
             * 补丁下载成功
             * @param msg
             */
            @Override
            public void onDownloadSuccess(String msg) {
                Beta.applyDownloadedPatch();
            }

            /**
             * 补丁下载失败
             * @param msg
             */
            @Override
            public void onDownloadFailure(String msg) {

            }

            /**
             * 补丁应用成功
             * @param msg
             */
            @Override
            public void onApplySuccess(String msg) {
                restartApp();
            }

            /**
             * 补丁应用失败
             * @param msg
             */
            @Override
            public void onApplyFailure(String msg) {

            }

            @Override
            public void onPatchRollback() {

            }
        };
         /*在application中初始化时设置监听，监听策略的收取*/
        Beta.upgradeListener = new UpgradeListener() {
            @Override
            public void onUpgrade(int ret, UpgradeInfo strategy, boolean isManual, boolean isSilence) {
                if (strategy != null) {
                    //有更新
                    Beta.downloadPatch();
                }
            }
        };

        // 设置开发设备，默认为false，上传补丁如果下发范围指定为“开发设备”，需要调用此接口来标识开发设备
        Bugly.setIsDevelopmentDevice(getApplicationContext(), BuildConfig.DEBUG_MODE);
        Bugly.init(this, BuildConfig.BUGLY_ID, BuildConfig.DEBUG_MODE);
    }

    private void restartApp() {
        Intent intent = getPackageManager().getLaunchIntentForPackage(getPackageName());
        PendingIntent restartIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager mgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        assert mgr != null;
        mgr.set(AlarmManager.RTC, System.currentTimeMillis(), restartIntent); // 1秒钟后重启应用
        System.exit(0);
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        ImageLoader.trimMemory(level);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        Beta.unInit();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        ImageLoader.clearAllMemoryCaches();
    }
}
