package com.jinzaofintech.commonlib.utils;

import com.jinzaofintech.commonlib.bean.Event;

import org.greenrobot.eventbus.EventBus;

/**
 * @author zengwendi
 * @date 2018/2/1
 * 将EventBus封装一层.
 */
public class EventBusUtil {

    public static void register(Object subscriber) {
        EventBus.getDefault().register(subscriber);
    }

    public static void unregister(Object subscriber) {
        EventBus.getDefault().unregister(subscriber);
    }

    public static void sendEvent(Event event) {
        EventBus.getDefault().post(event);
    }

    public static void sendStickyEvent(Event event) {
        EventBus.getDefault().postSticky(event);
    }

    // 其他
}
