package com.jinzaofintech.commonlib.utils

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import java.io.UnsupportedEncodingException
import java.net.URLDecoder

/**
 * Created by zengwendi on 2017/12/6.
 * kotlinuiils
 */
/**
 * kotlin三目运算
 */
fun <T> select(isTure: Boolean, param: () -> T, param1: () -> T) = if (isTure) param() else param1()

fun <T> select(isTure: Boolean, param: T, param1: T): T = if (isTure) param else param1


/**
 * 跳转activity
 * @param bundle 参数
 * 不传去默认值
 */
inline fun <reified T> Activity.startActivity(bundle: Bundle = Bundle()) {
    var intent = Intent(this, T::class.java);
    intent.putExtras(bundle)
    this.startActivity(android.content.Intent())
}

//String 或者方法获取当中的url中的参数
fun String.getQueryParams(): Map<String, String> {
    try {
        val params = HashMap<String, String>()
        val urlParts = this.split("\\?".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        if (urlParts.size > 1) {
            val query = urlParts[1]
            for (param in query.split("&".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()) {
                val pair = param.split("=".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val key = URLDecoder.decode(pair[0], "UTF-8")
                var value = ""
                if (pair.size > 1) {
                    value = URLDecoder.decode(pair[1], "UTF-8")
                }
                if (!TextUtils.isEmpty(value)) {
                    params.put(key, value)
                }
            }
        }
        return params
    } catch (ex: UnsupportedEncodingException) {
        throw AssertionError(ex)
    }
}