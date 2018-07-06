package com.jinzaofintech.commonlib.utils;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Created by zengwendi on 2018/1/3.
 */

public class TextUtils {
    /**
     * 字符串换行处理
     *
     * @param text
     */
    public static String textWrap(String text) {
        return text.replace("\\n", "\n");
    }

    /**
     * 获取整数部分
     *
     * @param newNum
     * @return
     */
    public static String getZhengshu(String newNum) {
        if (android.text.TextUtils.isEmpty(newNum)) return "";
        if (newNum.contains("."))
            return (String) newNum.subSequence(0, newNum.indexOf("."));
        else return newNum;
    }

    /**
     * 格式化金额并返回元或者万元
     *
     * @param money
     * @return
     */
    public static String formatMoney(BigDecimal money) {
        StringBuilder builder = new StringBuilder();
        if (money == null) {
            return builder.toString();
        }
        if (money.doubleValue() >= 10000) {
            builder.append(money.doubleValue() / 10000).append("万元");
        } else {
            builder.append(money).append("元");
        }
        return builder.toString();
    }

    /**
     * 获取小数部分
     *
     * @param newNum
     * @return
     */
    public static String getXiaoshu(String newNum) {
        if (isEmpty(newNum))
            return "";
        else if (newNum.contains("."))
            return newNum.substring(newNum.indexOf("."), newNum.length());
        else return "";
    }

    /**
     * 电话号码中间五位用星号表示
     *
     * @param s
     * @return
     */
    public static String setPhoneNoText(String s) {
        if (isEmpty(s)) {
            return "";
        } else if (s.length() < 11) {
            return s;
        } else {
            return s.substring(0, 3) + "*****" + s.substring(8, s.length());
        }
    }

    /**
     * @param amount 金额
     * @return 对服务器返回金额做非空处理
     */
    public static String getAmout(String amount) {
        if (isEmpty(amount)) {
            return "0";
        } else {
            return amount;
        }
    }

    /**
     * 带自定义默认值
     *
     * @param amount  1
     * @param defaule 1
     * @return
     */
    public static String getDefaultText(String amount, String defaule) {
        if (isEmpty(amount)) {
            return defaule;
        } else {
            return amount;
        }
    }

    /**
     * 保留两位小数
     *
     * @param d 原始shuju
     * @return
     */
    public static String getTwoPoint(double d) {
        return String.format("%.2f", d);
    }


    /**
     * 获取资源文件中的string
     *
     * @param context
     * @param id      资源id
     * @return
     */
    public static String getStringText(Context context, int id) {
        return context.getResources().getString(id);
    }

    public static String getStringTextAppend(Context context, int id, String str) {
        return context.getResources().getString(id) + str;
    }

    public static String getTwoStrAppendText(String str1, String str2) {
        return str1 + str2;
    }

    public static String getThreeAppendText(String str1, String str2, String str3) {
        return str1 + str2 + str3;
    }

    public static boolean isEmpty(String s) {
        return s == null || s.equals("");
    }

    /**
     * 设置输入框输入的小数位数
     *
     * @param editText EditText
     * @param length   小数长度
     */
    public static void setEditTextInputDotLength(final EditText editText, final int length) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String str = editText.getText().toString().trim();
                if (!android.text.TextUtils.isEmpty(str)) {
                    int dotIndex = str.indexOf(".");
                    if (dotIndex >= 0) {//用户输入的是小数
                        int dotAfterSize = str.length() - 1 - dotIndex;
                        if (dotAfterSize > length) {//用户输入的金额小数点后超过两位
                            editText.setText(str.substring(0, dotIndex + length + 1));
                            editText.setSelection(editText.getText().length());
                        }
                    }
                }
            }
        });
    }

    /**
     * 验证手机号格式正确性  没有下面的严谨
     */
    public static boolean isMobileNO(String mobiles) {
    /*
    移动：134、135、136、137、138、139、150、151、147(TD)、157(TD)、158、159、178、187、188
    联通：130、131、132、152、155、156、176、185、186
    电信：133、153、177、180、189、（1349卫通）
    总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
    */
        String telRegex = "[1][345789]\\d{9}";//"[1]"代表第1位为数字1，"[4578]"代表第二位可以为3、4、5、7、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobiles)) {
            return false;
        } else {
            return mobiles.matches(telRegex);
        }
    }

    /**
     * 大陆号码或香港号码均可
     */
    public static boolean isPhoneLegal(String str) throws PatternSyntaxException {
        return isChinaPhoneLegal2(str) || isHKPhoneLegal(str);
    }

    /**
     * 大陆手机号码11位数，匹配格式：前三位固定格式+后8位任意数
     * 此方法中前三位格式有：
     * 13+任意数
     * 14+任意数
     * 15+
     * 17+
     * 18+
     * 19+
     */
    public static boolean isChinaPhoneLegal2(String str) throws PatternSyntaxException {
        String regExp = "^((13[0-9])|(14[0-9])|(15[0-9])|(17[0-9])|(18[0-9])|(19[0-9]))\\d{8}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }
    //较上面的方法  更严谨的正则
    public static boolean isChinaPhoneLegal(String str)
            throws PatternSyntaxException {
        String regExp = "^((13[0-9])|(15[^4])|(166)|(17[0-8])|(18[0-9])|(19[8-9])|(147,145))\\d{8}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * 香港手机号码8位数，5|6|8|9开头+7位任意数
     */
    public static boolean isHKPhoneLegal(String str) throws PatternSyntaxException {
        String regExp = "^(5|6|8|9)\\d{7}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }

}














