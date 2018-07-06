package com.jinzaofintech.rebate.http.service;

import com.jinzaofintech.commonlib.BuildConfig;
import com.jinzaofintech.commonlib.bean.ApiRes;
import com.jinzaofintech.commonlib.bean.User;
import com.jinzaofintech.rebate.bean.AccountInfo;
import com.jinzaofintech.rebate.bean.FundermenterInfo;
import com.jinzaofintech.rebate.bean.HomeDataResponse;
import com.jinzaofintech.rebate.bean.InviteFriendsInfo;
import com.jinzaofintech.rebate.bean.MessageInfo;
import com.jinzaofintech.rebate.bean.RebateCash;
import com.jinzaofintech.rebate.bean.RebateResult;
import com.jinzaofintech.rebate.bean.RegistResponse;
import com.jinzaofintech.rebate.bean.SettingInfo;
import com.jinzaofintech.rebate.bean.SmsCodeReponse;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;


/**
 * 数据接口请求
 */

public interface AppService {
    String HOST = BuildConfig.BASE_URL;
    String CACHETIME = "2592000";

    /**
     * @param vfcode        verify code
     * @param vfkey         the appened key
     * @param pwd           userr password
     * @param recomendphone phone number
     * @return
     */
    @FormUrlEncoded
    @POST("users")
    Observable<ApiRes<RegistResponse>> regist1(@Field("verification_code") String vfcode,
                                               @Field("verification_key") String vfkey,
                                               @Field("password") String pwd,
                                               @Field("fdphone") String recomendphone);

    /**
     * login data request
     *
     * @param phone    phone
     * @param password password
     * @return login data
     */
    @FormUrlEncoded
    @POST("authorizations")
    Observable<ApiRes<User>> login(@Field("phone") String phone, @Field("password") String password);

    /**
     * @param phone phone
     * @return sms code
     */
    @FormUrlEncoded
    @POST("verificationCodes")
    Observable<ApiRes<SmsCodeReponse>> getSmsCode(@Field("phone") String phone);

    /**
     * @param key  verification key
     * @param code verify code
     * @param pwd  password
     * @return change password data
     */
    @FormUrlEncoded
    @POST("forgotPassword")
    Observable<ApiRes<Object>> forgetPwd(@Field("verification_key") String key, @Field("verification_code") String code, @Field("password") String pwd);

    /**
     * home page request
     *
     * @return home page data
     */
    @GET("index")
    Observable<ApiRes<HomeDataResponse>> getHomeFragData();

    /**
     * download image by url
     *
     * @param fileUrl
     * @return
     */
    @Streaming
    @GET
    Call<ResponseBody> downloadLatestFeature(@Url String fileUrl);

    /**
     * log out
     *
     * @return exit app
     */
    @DELETE("authorizations/current")
    Observable<ApiRes<Object>> logout();

    /**
     * account info
     *
     * @return my account
     */
    @GET("me")
    Observable<ApiRes<AccountInfo>> getAccountInfo();

    /**
     * fundermention detail
     *
     * @return
     */
    @GET("uflow")
    Observable<ApiRes<List<FundermenterInfo>>> getFunderDetail();

    /**
     * @param alipay  ali account
     * @param aliname ali real name
     * @return
     */
    @FormUrlEncoded
    @POST("comfirmwithdraw")
    Observable<ApiRes<Object>> widthdraw(@Field("money") String money, @Field("alipay") String alipay, @Field("aliname") String aliname);

    /**
     * message center info
     *
     * @return msg
     */
    @GET("unotice")
    Observable<ApiRes<List<MessageInfo>>> msgCenter();

    /**
     * inviting friend page
     *
     * @return
     */
    @GET("awardshare")
    Observable<ApiRes<InviteFriendsInfo>> rewardInvatation();

    /**
     * 设置
     *
     * @return
     */
    @GET("setting")
    Observable<ApiRes<SettingInfo>> setting();

    /**
     * 绑定支付宝
     *
     * @param alipay
     * @param aliname
     * @return
     */
    @FormUrlEncoded
    @POST("setalipay")
    Observable<ApiRes<Object>> bindAlipay(@Field("alipay") String alipay, @Field("aliname") String aliname);

    /**
     * 改绑手机
     *
     * @return
     */
    @GET("updatephone")
    Observable<ApiRes<SettingInfo>> changePhone();

    /**
     * @param key1  用用户原手机 调用验证码接口 返回的的 key
     * @param code1 用户原手机接收到的验证码
     * @param key2  用新手机调用验证码接口返回的key
     * @param code2 新手机接收到的验证码
     * @return 原手机能接收短信修改手机
     */
    @FormUrlEncoded
    @POST("updatephonev1")
    Observable<ApiRes<Object>> changePhoByOldPhone(@Field("key1") String key1,
                                                   @Field("code1") String code1,
                                                   @Field("key2") String key2,
                                                   @Field("code2") String code2);

    /**
     * @param password 原登录密码
     * @param key      新手机号调用短信验证码返回的key
     * @param code     用户短信验证码
     * @return 原手机不能接收短信修改手机
     */
    @FormUrlEncoded
    @POST("updatephonev2")
    Observable<ApiRes<Object>> changePhoByPwd(@Field("password") String password,
                                              @Field("key") String key,
                                              @Field("code") String code);

    /**
     * 爆品返现
     *
     * @return
     */
    @GET("cash")
    Observable<ApiRes<RebateCash>> getCash(@Query("id") String id);

    /**
     * 爆品返现
     *
     * @return
     */
    @POST("getmoney")
    @FormUrlEncoded
    Observable<ApiRes<RebateResult>> postMoney(@Field("data") String data, @Field("data") String id);
}




































