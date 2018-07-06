package com.jinzaofintech.commonlib.http.service;

import com.jinzaofintech.commonlib.BuildConfig;
import com.jinzaofintech.commonlib.bean.ApiRes;
import com.jinzaofintech.commonlib.bean.User;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by zengwendi on 2017/12/22.
 */

public interface Service {
    String HOST = BuildConfig.BASE_URL;

    /**
     * @param phone   手机号
     * @param password 密码
     */
    @FormUrlEncoded
    @POST("authorizations")
    Observable<ApiRes<User>> login(@Field("phone") String phone, @Field("password") String password);


}




































