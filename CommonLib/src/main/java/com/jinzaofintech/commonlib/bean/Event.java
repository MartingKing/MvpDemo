package com.jinzaofintech.commonlib.bean;

/**
 * @author zengwendi
 * @date 2018/2/1
 * 事件，EventBus订阅发布。
 */
public class Event<T> {
    private int code;
    private T data;

    public Event(int code) {
        this.code = code;
    }

    public Event(int code, T data) {
        this.code = code;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static final class EventCode {
        public static final int A = 0x11;
        public static final int B = 0x12;
        public static final int C = 0x13;
    }
}
