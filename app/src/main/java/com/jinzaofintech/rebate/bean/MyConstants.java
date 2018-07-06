package com.jinzaofintech.rebate.bean;

import android.os.Environment;

/**
 * Created by Administrator on 2018/6/13.
 * the parameters that needs pass to another page
 */

public class MyConstants {
    //首页点击我的没登录直接跳转的登录页面
    public static String HOME2LOGIN = "home2login";
    //其他页面跳转home fragment
    public static String OTHERS2HOME = "ohters2home";
    //注册成功后自动登录  然后页面跳转
    public static String LOGIN2ACCOUNT = "login2account";
    //手机号
    public static String PHONENUM = "phone_number";
    //密码
    public static String PASSWORD = "password";
    //首页爆款图片
    public static final String APP_IMAGE_DIR = Environment.getExternalStorageDirectory() + "/rebate/";
    //账户余额
    public static String ACCOUNT_INFO = "acccount_info";
    //我的消息
    public static String MY_MESSAGE = "message";
    //页面跳转来源
    public static String FROM_WHERE = "where";
    //支付宝账户和实名
    public static String ALI_ACCOUNT = "ali_account";
    public static String ALI_REALNAME = "ali_name";
    //账户余额
    public static String REST_AMOUNT = "rest_amount";

}
