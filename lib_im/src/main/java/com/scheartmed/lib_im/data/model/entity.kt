package com.scheartmed.lib_im.data.model

import kotlin.collections.ArrayList

/**
 * @author: villa_mou
 * @date: 2022-12-05:13
 * @version V1.0
 * @desc <描述当前版本功能>
 */
data class ChatConfigEntity(


    /**
     * 总条数
     */
    var allNumber: Long? = null,

    /**
     * 大标题
     */
    val conversationTitle: String? = null,

    /**
     * 医生头像url
     */
    val doctorIconUrl: String? = null,

    /**
     * 医生id
     */
    val doctorId: Long? = null,

    /**
     * 就诊卡id
     */
    val ecardId: String? = null,

    /**
     * 商品/服务状态
     * Created :1.0 已创建
     * ToBeDelivered :2.0 待发货
     * ToBeReceipt :3.0 待收货
     * ToBeReceiveIm :4.0 待接诊
     * InConsultationIm :5.0 咨询中
     * Over :6.0 已完成
     * Received :7.0 已收货
     * ToBeExecute :8.0 待执行
     * Paused :9.0 已暂停
     * Executed :10.0 已执行
     * Expired :11.0 已失效
     * NotReceiveIm :12.0 未接诊
     * ToPickedUp :13.0 待自取
     * ReportIsOut :14.0 报告已出
     * ReportNotOut :15.0 报告未出
     * Cancelled :16.0 已取消
     */

    /**
     *  Prescription :2.0 处方
     *  Drug :3.0 药品
     *  ConsultationBuy :4.0 复诊配药
     *  OnlineAsk :5.0 在线咨询
     *  OnlineFreeClinic :6.0 在线义诊
     *  ReportInterpretationAsk :7.0 报告解读
     *  UseDrugAsk :8.0 用药咨询
     *  NursingAsk :9.0 护理咨询
     *  FollowUpAsk :10.0 随诊会话
     *  Inspection :11.0 检验检查
     */

    /**
     * 患者头像url
     */
    val patientIconUrl: String? = null,

    /**
     * 患者用户ID
     */
    val patientUserId: String? = null,

    /**
     * 放置栏集合
     * diagnose :edy-im-xiazhenduan 下诊断
     *  prescription :im-chufang 开处方
     *  examine :edy-im-jianyanjiancha 检验检查
     *  delayed :null 延时
     *  appeal :null 申诉
     *  finishInterrogation :null 结束问诊
     *  phone :null 电话
     *  video :im-shipinwenzhen 视频
     *  image :im-tupian 图片
     *  registering :edy-im-guahao 挂号
     *  businessCard :edy-im-mingpian 名片
     *  article :edy-im-wenzhang 文章
     *  servicePackage :edy-im-fuwubao 服务包
     *  followUpVisit :edy-IM-suifang 随访
     *  hospitalizedID :edy-im-ruyuanzheng 入院证
     *  appointment :edy-fuzhenyuyue 复诊预约
     *  recommend :im-tuijianyisheng 推荐医生
     *  electronicCase :edy-im-dianzibingli-new 电子病例
     *  noticePush :im-shensu 须知推送
     *  refund :im-tuikuan 退款申请 refund
     */
    val placeList: List<IconFontEntity>? = null,

    /**
     * (1, "图文问诊"),
     * (2, "电话问诊"),
     * (3, "视频问诊"),
     * (4, "图文咨询"),
     * (5, "电话咨询"),
     * (6, "视频咨询");
     */

    /**
     * 快捷工具栏集合
     *  diagnose :edy-im-xiazhenduan 下诊断
     *  prescription :im-chufang 开处方
     *  examine :edy-im-jianyanjiancha 检验检查
     *  finishInterrogation :null 结束问诊
     *  phone :null 电话
     *  video :im-shipinwenzhen 视频
     *  image :im-tupian 图片
     *  registering :edy-im-guahao 挂号
     *  businessCard :edy-im-mingpian 名片
     *  article :edy-im-wenzhang 文章
     *  servicePackage :edy-im-fuwubao 服务包
     *  followUpVisit :edy-IM-suifang 随访
     *  hospitalizedID :edy-im-ruyuanzheng 入院证
     *  appointment :edy-fuzhenyuyue 复诊预约
     *  recommend :im-tuijianyisheng 推荐医生
     *  electronicCase :edy-im-dianzibingli-new 电子病例
     *  noticePush :im-shensu 须知推送
     *  refund :im-tuikuan 退款申请
     */
    val shortcutList: List<IconFontEntity>? = null,

    /**
     * 订单的状态  待支付:1  待接诊:4  咨询中:5  已完成: 6  已退款   已过期
     *
     */

    /**
     * 结束时间
     */
    var stopTime: String? = null,

    /**
     * 已使用条数
     */
    var useNumber: Int? = null,
    var formCode: String? = null
)

data class IconFontEntity(
    val label: String? = null,
    val value: String? = null,
    val icon: String? = null,
    val active: Boolean? = null,
)


data class MessageListEntity(
    var ID: String? = null,
    val groupId: String? = null,
    var from: String? = null,
    val type: String? = null,
    var time: Long? = null,
    var payLoad: MessagePayload? = null,
    //自定义字段，是否显示时间
    var isShowTime: Boolean? = null,
    //自定义字段，发送状态
    var sentStatus: MsgSendStatus? = null,
    //自定义字段，msgType
    var msgType: String? = null
)

data class MessagePayload(
    var data: List<MessageDatum>? = null
)

data class MessageDatum(
    val msgType: String? = null,
    var msgContent: MsgContent? = null,
)

data class MsgContent(
    var Download_Flag: Long? = null,

    var Second: Int? = null,

    var Size: Long? = null,

    var UUID: String? = null,

    var Url: String? = null,
    /**
     * 文本消息的文本
     */
    var Text: String? = null,
    var ImageInfoArray: ArrayList<ImageInfoArray>? = null,
    var Ext: String? = null,

    var Desc: String? = null,

    var Data: MessageContentDataEntity? = null,

    var Sound: String? = null,

    )

data class ImageInfoArray(
    val Type: Long? = null,

    val Size: Long? = null,

    val Height: Long? = null,

    val Width: Long? = null,

    val URL: String
)

data class MessageContentDataEntity(
    val patientName: String? = null,
    val messageType: EnumObj? = null,
    val businessData: String? = null,
    val recipients: List<String>? = null,
    val href: String? = null,
    val labels: List<String>? = null,
    val desc: String? = null,
    val content: Any? = null,
    val contentTwo: String? = null,
    val result: String? = null,
    val rp: String? = null,
    val status: String? = null,
    val time: String? = null,
    val drugNameStrs: String? = null,
    val diag: String? = null,
    val gender: String? = null,
    val age: Int? = null,
    val drugName: String? = null,
    val desiredDate: String? = null,
    val desiredSlot: String? = null,
    val title: String? = null,
    val frontCoverUrl: String? = null,
    val callDuration: String? = null,
    val inspection: String? = null,
    val originalId: String? = null,
    val preId: String? = null,
    val id: String? = null,
)


data class SystemTextActionEntity(
    val center: Boolean? = null,
    val list: List<SystemTextListEntity>? = null
)

data class SystemTextListEntity(
    val highlight: Boolean? = null,
    val color: String? = null,
    val text: String? = null,
    val href: String
)

data class RoomIdEntity(
    val roomId: String? = null,
    val userDefineRecordId: String? = null,
    val closeRoomDuration: String? = null,
)


data class VideoTimeEntity(
    val time: String? = null,
)

data class MedicalRecordsEntity(val aBoolean: Boolean? = null)


data class PrescriptionBusinessDataEntity(
    val preId: String,
    val opIdList: List<String>,
    val orderId: String,
    val opImgUrlList: List<String>
)


data class FormEntity(
    val form: Form,
    val formItems: List<FormItem>,
    val result: Result
)

data class Form(
    val code: String,
    val name: String,
    val description: String
)

data class FormItem(
    val id: String,
    val type: EnumObj,
    val layout: String,
    val title: String,
    val data: String,
    val required: Boolean,
)

data class Result(
    val data: Map<String, String>,
    val completeFlag: Boolean
)

class NameObj {
    var value: String? = null
    var name: String? = null
}

data class EnumObj(
    val value: Int? = null,
    val desc: String? = null
)
