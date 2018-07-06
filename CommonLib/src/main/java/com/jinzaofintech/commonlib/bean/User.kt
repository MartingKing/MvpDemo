package com.jinzaofintech.commonlib.bean

import java.io.Serializable

/**
 *  注册后服务器返回的数据
 */

data class User(
        var message: String,
        var status_code: Int,
        var access_token: String,
        var token_type: String,
        var expires_in: String,
        val user_id: Long, //用户的id
        var phone: String,//手机号
        var password: String//密码
) : Serializable {
    override fun toString(): String {
        return "User(message='$message', status_code=$status_code, access_token='$access_token', token_type='$token_type', expires_in='$expires_in', user_id=$user_id, phone='$phone', password='$password')"
    }
}

