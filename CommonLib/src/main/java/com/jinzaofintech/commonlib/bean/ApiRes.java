package com.jinzaofintech.commonlib.bean;

/**
 * Created by zengwendi on 2017/9/27.
 */
public class ApiRes<T> {

    private String message;
    private T data;
    private int status_code;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getStatus_code() {
        return status_code;
    }

    public void setStatus_code(int status_code) {
        this.status_code = status_code;
    }

    @Override
    public String toString() {
        return "ApiRes{" +
                "message='" + message + '\'' +
                ", data=" + data +
                ", status_code=" + status_code +
                '}';
    }
}
