package com.scheartmed.lib_im.data.model

/**
 * @author: villa_mou
 * @date: 2022-12-07:10
 * @version V1.0
 * @desc <描述当前版本功能>
 */
object TIMMessageType {
    val TIMTextElem = "TIMTextElem"
    val TIMSoundElem = "TIMSoundElem"
    val TIMImageElem = "TIMImageElem"
}

val TIMMessageTypes = mapOf(
    TIMMessageType.TIMTextElem to MessageCustomType.TEXT,
    TIMMessageType.TIMImageElem to MessageCustomType.IMAGE,
    TIMMessageType.TIMSoundElem to MessageCustomType.SOUND
)

enum class MsgSendStatus {
    SENDING,  //发送中
    FAILED,  //失败
    SUCCESS //已发送
}

object MessageCustomType {

    //  医生诊断
    val CARD_DOCTOR_DIAGNOSED = "CARD_DOCTOR_DIAGNOSED"

    //  检验检查
    val CARD_INSPECTION_CHECK = "CARD_INSPECTION_CHECK"

    // 处方单
    val CARD_PRESCRIPTION = "CARD_PRESCRIPTION"

    // 医生卡片
    val RECOMMEND_DOCTOR = "RECOMMEND_DOCTOR"

    // 须知-卡片
    val NOTICE_DRUG = "NOTICE_DRUG"

    // 系统文本消息
    val SYSTEM_TEXT = "SYSTEM_TEXT"

    // 系统可点击文本消息
    val SYSTEM_TEXT_ACTION = "SYSTEM_TEXT_ACTION"

    // 系统推荐消息
    val SYSTEM_TEXT_CENTER = "SYSTEM_TEXT_CENTER"

    // 系统资料完善
    val SYSTEM_CARD_DATA_PERFECT = "SYSTEM_CARD_DATA_PERFECT"

    // 普通文本信息
    val TEXT = "Text"

    // 普通音频信息
    val SOUND = "SOUND"

    // 图片消息
    val IMAGE = "Image"

    // 视频消息
    val VIDEO = "VIDEO"

    // 视频消息提示
    val VIDEO_TIP = "VIDEO_TIP"

    //  视频-系统消息
    val ROOM_SYSTEM = "ROOM_SYSTEM"

    // 电话消息
    val PHONE = "PHONE"

    // 电话消息提示
    val PHONE_TIP = "PHONE_TIP"

    // 患教资料
    val PATIENT_EDUCATION_DATA = "PATIENT_EDUCATION_DATA"

    //  术前问题
    val PREOPERATIVE_PROBLEM = "PREOPERATIVE_PROBLEM"

    // 复诊开方
    val CARD_FU_PRESCRIPTION = "CARD_FU_PRESCRIPTION"

    // 历史处方
    val CARD_PRESCRIPTION_HISTORY = "CARD_PRESCRIPTION_HISTORY"

// 复诊上传凭证

    val SUBSEQUENT_VISIT_CERTIFICATE_UPLOAD = "SUBSEQUENT_VISIT_CERTIFICATE_UPLOAD"

// ------ 状态

    // 状态发生变化推动的消息
    val ITEM_STATUS_CHANGE = "ITEM_STATUS_CHANGE"

    // 表单状态变更
    val FORM_STATUS_CHANGE = "FORM_STATUS_CHANGE"
}