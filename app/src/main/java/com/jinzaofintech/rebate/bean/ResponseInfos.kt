package com.jinzaofintech.rebate.bean

import java.io.Serializable

/**
 * Created by Administrator on 2018/6/13.
 * 所有接口返回数据实体类
 */

//获取验证码
data class SmsCodeReponse(
        val key: String,//后台获取缓存的key 请求用户注册接口 时需带上
        val code: String,//验证码
        val expired_at: String//验证码过期时间

)

//注册
data class RegistResponse(
        val access_token: String,
        val token_type: String,
        val expires_in: String//验证码过期时间
)

//首页
data class HomeDataResponse(
        val user_id: Int, //1
        val is_login: Int, //是否登录1表示已经登录2表示未登录
        val banlance: String,//用户账户余额 登录会显示 未登录不会显示
        val hotgood: Hotgood,
        val banner: List<Banner>,
        val jp_good: List<JpGood>,
        val alipay: String,
        val aliname: String,
        val status: Int,
        val notice: List<Notice>
)

//首页
data class Notice(
        val notice: String
)

//首页
data class JpGood(
        val id: Int, //1
        val title: String,
        val type_id: Int, //1
        val apr: String,
        val first_investment: String, //1
        val second_investment: String,
        val status: Int, //1
        val icon: String,
        val name: String
)

//首页banner
data class Banner(
        val url: String
)

//首页爆品
data class Hotgood(
        val img: String,
        val id: Int
)

//我的
data class AccountInfo(
        val user_id: Int, //1
        val status: Int, //1
        val total: String,
        val alipay: String,
        val aliname: String,
        val notification_count: Int, //1
        val withdraw: Double //0
) : Serializable

//资金明细
data class FundermenterInfo(
        val id: Int, //1
        val user_id: Int, //1
        val title: String,
        val sum: String,
        val sign: String,
        val created_at: String
)

//消息中心
data class MessageInfo(
        val user_id: Int, //1
        val id: Int, //1
        val title: String,
        val content: String,
        val status: Int, //1
        val created_at: String
) : Serializable

//invite friends
data class InviteFriendsInfo(
        val yyfriend: Double, //0
        val count: Double, //0
        val reward: Double //0
)

//setting
data class SettingInfo(
        val alipay: String, //null
        val phone: String,
        val id: Int, //1
        val title: String,
        val status: Int //1
)

/**
 * web实体类
 */
data class CashBean(
        /**
         * 匹配url 正则表达式
         */
        val pattern: String, //https://passport.tuandai.com/2login
        /**
         * js代码
         */
        val js: String,
        /**
         * 是否显示页面
         */
        val is_show: Int
) : Serializable

/**
 * 返现接口
 */
data class RebateCash(
        /**
         * 匹配url 数组
         */
        val urls: ArrayList<CashBean>

) : Serializable {
    var productId: String = ""
}


/**
 * 返现结果回调
 */
data class RebateResult(
        val content: String,
        val money: String,
        var status: Int, //1
        val is_close: Int //1
)
