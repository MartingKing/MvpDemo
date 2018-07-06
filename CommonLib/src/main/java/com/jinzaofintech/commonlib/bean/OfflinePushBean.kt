package com.jinzaofintech.commonlib.bean

/**
 * Created by Administrator on 2018/1/29.
 * 离线通知实体类
 */

data class PushInfo(
		val display_type: String, //notification  推送消息类型
		val extra: Extra,
		val msg_id: String, //uuapugn151720757171510  推送消息的id
		val body: Body,
		val random_min: Int //0
)
//自定义参数
data class Extra(
		val weic: String //"mainActivity"  key为url-->value为mainActivity
)

data class Body(
		val after_open: String, //go_app  点击推送消息打开应用后的行为描述 go_qpp(启动应用) go_custom(自定义行为) go_activity(打开指定页面) go_url(打开指定链接)
		val play_lights: String, //false 是否亮灯（手机消息呼吸灯）
		val ticker: String, //惺惺惜惺惺
		val play_vibrate: String, //false
		val custom: String, //自定义行为的value
		val activity: String,//activity的全路径
		val url: String, //链接地址
		val text: String, //阿德法阿萨德  内容
		val title: String, //惺惺惜惺惺   标题
		val play_sound: Boolean //true   是否播放声音
)