package com.jinzaofintech.commonlib.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Base64;

import com.jinzaofintech.commonlib.app.BaseApplication;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by 0169670 on 2016/11/14.
 * SharedPreferences 存储工具类
 * 除登录信息外其它地方的 key 放到主项目的 SPConstant 类中
 */
public class SPUtils {
    /**
     * 文件名
     */
    public static final String SHAREPREFERENCES_FILENAME = "vc_sp";
    /**
     * 用户key
     */
    public static final String USER_KEY = "user_key";
    /**
     * 安全域名key
     */
    public static final String SAFE_DOMAINS_KEY = "safe_domains_key";
    /**
     * 是否进入引导页
     */
    public static final String IS_BOOT_PAGE = "IS_BOOT_PAGE";

    /**
     * 默认模式，只允许应用本身访问数据
     */
    public static final int MODE = Context.MODE_PRIVATE;


    private SPUtils() {
    }


    /**
     * 获取 ShredPreferences
     *
     * @return SharedPreferences
     */
    public static SharedPreferences getSharedPreferences() {
        return BaseApplication.getInstance().getSharedPreferences(SHAREPREFERENCES_FILENAME, MODE);
    }

    /**
     * 保存String
     *
     * @param key   String
     * @param value String
     */
    public static void setStringValue(String key, String value) {
        Editor editor = getSharedPreferences().edit();
        editor.putString(key, value);
        editor.commit();
    }

    /**
     * 获得String类型
     *
     * @param key String
     * @return String
     */
    public static String getStringValue(String key, String defValue) {
        return getSharedPreferences().getString(key, defValue);
    }

    /**
     * 存储boolean类型数据
     *
     * @param key   String
     * @param value boolean
     */
    public static void setBooleanValue(String key, boolean value) {
        Editor editor = getSharedPreferences().edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    /**
     * 获得Boolean类型数据
     *
     * @param key      String
     * @param defValue boolean
     * @return boolean
     */
    public static boolean getBooleanValue(String key, boolean defValue) {
        return getSharedPreferences().getBoolean(key, defValue);
    }

    /**
     * 保存int类型
     *
     * @param key   String
     * @param value int
     */
    public static void setIntValue(String key, int value) {
        Editor editor = getSharedPreferences().edit();
        editor.putInt(key, value);
        editor.commit();
    }

    /**
     * 获得int类型
     *
     * @param key      String
     * @param defValue int
     * @return int
     */
    public static int getIntValue(String key, int defValue) {
        return getSharedPreferences().getInt(key, defValue);
    }

    /**
     * SharedPreferences 对象数据初始化
     *
     * @param key    String
     * @param object Object
     */
    public static void setObjectValue(String key, Object object) {
        String objectBase64 = "";
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            objectBase64 = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Editor editor = getSharedPreferences().edit();
        editor.putString(key, objectBase64);
        editor.commit();
    }

    /**
     * SharedPreferencesȡ 获取对象
     *
     * @param key String
     * @return Object
     */
    public static Object getObjectValue(String key) {
        Object object = null;
        try {
            String objectBase64 = getSharedPreferences().getString(key, "");
            byte[] base64Bytes = Base64.decode(objectBase64.getBytes(), Base64.DEFAULT);
            if (base64Bytes != null && base64Bytes.length > 0) {
                ByteArrayInputStream bais = new ByteArrayInputStream(base64Bytes);
                ObjectInputStream ois = new ObjectInputStream(bais);
                object = ois.readObject();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return object;
    }

    /**
     * 清空数据
     */
    public static void clearSharePreference() {
        getSharedPreferences().edit().clear().commit();
    }

    /**
     * 删除指定 SharePreferencs
     *
     * @param key String
     */
    public static void clearSharePreferencrForName(String key) {
        getSharedPreferences().edit().remove(key).commit();
    }

    /**
     * 清除用户数据
     */
    public static void clearUserPreferencr() {
        clearSharePreferencrForName(USER_KEY);
    }
}
