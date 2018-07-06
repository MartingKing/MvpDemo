package com.jinzaofintech.commonlib.http;

import com.jinzaofintech.commonlib.app.BaseApplication;
import com.jinzaofintech.commonlib.utils.Utils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by zengwendi on 2016/12/13.
 * 添加token等头部参数
 */

public class HeaderInterceptord implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request oldRequest = chain.request();
        Request newRequest = addParam(oldRequest);
        return chain.proceed(newRequest);
    }

    /**
     * 添加公共参数
     *
     * @param oldRequest
     * @return
     */
    private Request addParam(Request oldRequest) {
        Request.Builder builder = oldRequest.newBuilder()
                .header("REBATE_UA", Utils.getAppUserAgent());
        if (BaseApplication.getInstance().getUser() != null) {
            builder.header("Authorization", "Bearer " + BaseApplication.getInstance().getUser().getAccess_token());
        }
        return builder.build();
    }
}
