package com.jinzaofintech.commonlib.ui.common;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jinzaofintech.commonlib.bean.PushInfo;
import com.umeng.message.UmengNotifyClickActivity;

import org.android.agoo.common.AgooConstants;

/**
 * Created by Administrator on 2017/11/23 0023.
 * 小米弹窗功能
 * 小米对后台进程做了诸多限制。若使用一键清理，应用的channel进程被清除，将接收不到推送。
 * 为了增加推送的送达率，可选择接入小米托管弹窗功能。通知将由小米系统托管弹出，
 * 点击通知栏将跳转到指定的Activity。该Activity需继承自UmengNotifyClickActivity，
 * 同时实现父类的onMessage方法，对该方法的intent参数进一步解析即可，该方法异步调用，不阻塞主线程
 */

public class MipushDialogActivity extends UmengNotifyClickActivity {

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String body = (String) msg.obj;
            Gson gson = new GsonBuilder().create();
            PushInfo info = gson.fromJson(body, PushInfo.class);
            String url = info.getExtra().getWeic();
            if (url != null) {//跳转到过度页
//                Intent intent = new Intent();
//                intent.putExtra(Constants.SHEME_URL, url);
//                intent.setClassName(MipushDialogActivity.this, Constants.MIDDLEACTIVITY_CLASSNAME);
//                startActivity(intent);
//                overridePendingTransition(0, 0);
            }
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }


    @Override
    public void onMessage(Intent intent) {
        super.onMessage(intent);  //此方法必须调用，否则无法统计打开数
        String body = intent.getStringExtra(AgooConstants.MESSAGE_BODY);
        Message message = Message.obtain();
        message.obj = body;
        handler.sendMessage(message);
    }
}
