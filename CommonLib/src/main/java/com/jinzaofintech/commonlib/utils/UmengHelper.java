package com.jinzaofintech.commonlib.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Handler;

import com.jinzaofintech.commonlib.BuildConfig;
import com.jinzaofintech.commonlib.R;
import com.jinzaofintech.commonlib.app.BaseApplication;
import com.jinzaofintech.commonlib.app.Constants;
import com.meituan.android.walle.WalleChannelReader;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.MsgConstant;
import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.common.inter.ITagManager;
import com.umeng.message.entity.UMessage;
import com.umeng.message.tag.TagManager;

/**
 * 友盟推送类
 */
public class UmengHelper {

    private static final String TAG = "UmengHelper";
    public static final String UPDATE_STATUS_ACTION = "com.umeng.message.example.action.UPDATE_STATUS";
    private static UmengHelper mUmengHelper;
    private static String USER_ALIAS_VALUE = "userId_";
    public static String LOGING_TAG = "log_on";
    public static String BID_TAG = "bid";
    public static String mDeviceToken;

    public static UmengHelper getInstance() {
        if (mUmengHelper == null) {
            mUmengHelper = new UmengHelper();
        }
        return mUmengHelper;
    }

    public void init() {
        PushAgent mPushAgent = PushAgent.getInstance(ContextUtils.getContext());
        //注册推送服务，每次调用register方法都会回调该接口
        UMConfigure.setLogEnabled(true);
        mPushAgent.setNotificationPlaySound(MsgConstant.NOTIFICATION_PLAY_SDK_ENABLE);
        mPushAgent.register(new IUmengRegisterCallback() {
            @Override
            public void onSuccess(String deviceToken) {
                mDeviceToken = deviceToken;
                //注册成功会返回device token
                LogUtils.e("onSuccess: deviceToken----" + deviceToken);
                ContextUtils.getContext().sendBroadcast(new Intent(UPDATE_STATUS_ACTION));
            }

            @Override
            public void onFailure(String s, String s1) {
                LogUtils.e("onFailure: ---" + s + "     s1--" + s1);
                ContextUtils.getContext().sendBroadcast(new Intent(UPDATE_STATUS_ACTION));
            }
        });
        String channel = WalleChannelReader.getChannel(ContextUtils.getContext());
        UMConfigure.init(ContextUtils.getContext(), BuildConfig.UMENG_APPKEY, channel, UMConfigure.DEVICE_TYPE_PHONE, BuildConfig.UMENG_MESSAGE_SECRET);

        setMessageHandler();
        setNotificationClickHandler();
    }

    public static void startPushAgent() {
        PushAgent.getInstance(ContextUtils.getContext()).onAppStart();
    }

    /**
     * 设置用户别名
     */
    public static void setUserAlias(long user_id) {
        //设置用户id和device_token的一对多的映射关系：
        PushAgent.getInstance(ContextUtils.getContext()).addAlias(USER_ALIAS_VALUE + user_id, Constants.APP_SHEME, new UTrack.ICallBack() {
            @Override
            public void onMessage(boolean isSuccess, String message) {
            }
        });
    }

    public static void delUserAlias() {
        long userid = BaseApplication.getInstance().getUser() == null ? 0 : BaseApplication.getInstance().getUser().getUser_id();
        PushAgent.getInstance(ContextUtils.getContext()).deleteAlias(USER_ALIAS_VALUE + userid, Constants.APP_SHEME, new UTrack.ICallBack() {
            @Override
            public void onMessage(boolean b, String s) {
            }
        });
    }

    /**
     * 设置用户标签
     */
    public static void setUserTag(String... var2) {
        PushAgent.getInstance(ContextUtils.getContext()).getTagManager().addTags(new TagManager.TCallBack() {

            @Override
            public void onMessage(final boolean isSuccess, final ITagManager.Result result) {
            }
        }, var2);
    }

    public static void delUserTag() {
        PushAgent.getInstance(ContextUtils.getContext()).getTagManager().deleteTags(new TagManager.TCallBack() {
            @Override
            public void onMessage(boolean b, ITagManager.Result result) {
            }
        }, LOGING_TAG, BID_TAG);
    }

    public void setMessageHandler() {
        UmengMessageHandler messageHandler = new UmengMessageHandler() {
            @Override
            public void dealWithCustomMessage(final Context context, final UMessage msg) {
                LogUtils.e("dealWithCustomAction msg=" + msg.toString() + ", msg.custom=" + msg.text);
                new Handler(context.getMainLooper()).post(new Runnable() {

                    @Override
                    public void run() {
                        // 对自定义消息的处理方式，点击或者忽略
                        boolean isClickOrDismissed = true;
                        if (isClickOrDismissed) {
                            //自定义消息的点击统计
                            UTrack.getInstance(ContextUtils.getContext()).trackMsgClick(msg);
                        } else {
                            //自定义消息的忽略统计
                            UTrack.getInstance(ContextUtils.getContext()).trackMsgDismissed(msg);
                        }
                    }
                });
            }

            @Override
            public void dealWithNotificationMessage(Context context, UMessage uMessage) {
                super.dealWithNotificationMessage(context, uMessage);
            }


            @Override
            public Notification getNotification(Context context, UMessage msg) {
                LogUtils.e("dealWithCustomAction msg=" + msg.toString() + ", msg.custom=" + msg.text);
                if (Build.VERSION.SDK_INT >= 26) {
                    NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    NotificationChannel channel = new NotificationChannel("channel_id", "channel_name", NotificationManager.IMPORTANCE_HIGH);
                    if (manager != null) {
                        manager.createNotificationChannel(channel);
                    }
                    Notification.Builder builder = new Notification.Builder(context, "channel_id");
                     builder.setSmallIcon(R.mipmap.ic_launcher)
                            .setWhen(System.currentTimeMillis())
                            .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                            .setContentTitle(msg.title)
                            .setContentText(msg.text)
                            .setAutoCancel(true);
                    return builder.build();
                } else {
                    Notification.Builder builder = new Notification.Builder(context);
                    builder.setSmallIcon(R.mipmap.ic_launcher)
                            .setWhen(System.currentTimeMillis())
                            .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                            .setContentTitle(msg.title)
                            .setContentText(msg.text)
                            .setAutoCancel(true);
                    return builder.build();
                }
            }
        };
        PushAgent.getInstance(ContextUtils.getContext()).setMessageHandler(messageHandler);
    }


    /**
     * 自定义行为的回调处理，参考文档：高级功能-通知的展示及提醒-自定义通知打开动作
     * UmengNotificationClickHandler是在BroadcastReceiver中被调用，故
     * 如果需启动Activity，需添加Intent.FLAG_ACTIVITY_NEW_TASK
     */
    public void setNotificationClickHandler() {
        /**
         * 该Handler是在BroadcastReceiver中被调用，故
         * 如果需启动Activity，需添加Intent.FLAG_ACTIVITY_NEW_TASK
         * */
        UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler() {
            @Override
            public void dealWithCustomAction(Context context, UMessage msg) {

            }

            @Override
            public void launchApp(Context context, UMessage msg) {
                if (msg.extra != null) {
                    String url = msg.extra.get(Constants.APP_SHEME);
                    if (url != null) {//跳转到过度页
//                        Intent intent = new Intent();
//                        intent.putExtra(Constants.SHEME_URL, url);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        intent.setClassName(context, Constants.MIDDLEACTIVITY_CLASSNAME);
//                        context.startActivity(intent);
                    }
                }
            }

            @Override
            public void openUrl(Context context, UMessage msg) {
                LogUtils.e(TAG, "openUrl msg=" + msg.toString() + ", msg.custom=" + msg.custom);
                super.openUrl(context, msg);
            }

            @Override
            public void openActivity(Context context, UMessage msg) {
                LogUtils.e(TAG, "openActivity msg=" + msg.toString() + ", msg.custom=" + msg.custom);
            }
        };
        PushAgent.getInstance(ContextUtils.getContext()).setNotificationClickHandler(notificationClickHandler);
    }


}
