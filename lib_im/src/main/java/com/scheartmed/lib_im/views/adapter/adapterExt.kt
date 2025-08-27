package com.scheartmed.lib_im.views.adapter

import com.netease.nimlib.sdk.v2.message.V2NIMMessage
import com.scheartmed.lib_im.R
import com.scheartmed.lib_im.data.model.MessageCustomType


val TYPE_SEND_TEXT = 1
val TYPE_RECEIVE_TEXT = 2
val TYPE_SEND_IMAGE = 3
val TYPE_RECEIVE_IMAGE = 4
val TYPE_SEND_AUDIO = 5
val TYPE_RECEIVE_AUDIO = 6
val TYPE_SEND_CARD_CONDITION_DATA = 7
val TYPE_RECEIVE_CARD_CONDITION_DATA = 8
val TYPE_SEND_CARD_DOCTOR_DIAGNOSED = 9
val TYPE_RECEIVE_CARD_DOCTOR_DIAGNOSED = 10
val TYPE_SEND_CARD_INSPECTION_CHECK = 11
val TYPE_RECEIVE_CARD_INSPECTION_CHECK = 12
val TYPE_SEND_CARD_PRESCRIPTION = 13
val TYPE_RECEIVE_CARD_PRESCRIPTION = 14
val TYPE_SEND_RECOMMEND_DOCTOR = 15
val TYPE_RECEIVE_RECOMMEND_DOCTOR = 16
val TYPE_SEND_NOTICE_DRUG = 17
val TYPE_RECEIVE_NOTICE_DRUG = 18
val TYPE_RECEIVE_SYSTEM_TEXT = 20
val TYPE_RECEIVE_SYSTEM_TEXT_ACTION = 22
val TYPE_RECEIVE_SYSTEM_TEXT_CENTER = 24
val TYPE_SEND_SYSTEM_CARD_DATA_PERFECT = 25
val TYPE_RECEIVE_SYSTEM_CARD_DATA_PERFECT = 26
val TYPE_SEND_VIDEO = 27
val TYPE_RECEIVE_VIDEO = 28
val TYPE_SEND_VIDEO_TIP = 29
val TYPE_SEND_ROOM_SYSTEM = 31
val TYPE_RECEIVE_ROOM_SYSTEM = 32
val TYPE_SEND_PHONE = 33
val TYPE_RECEIVE_PHONE = 34
val TYPE_PHONE_TIP = 35
val TYPE_SEND_PATIENT_EDUCATION_DATA = 37
val TYPE_RECEIVE_PATIENT_EDUCATION_DATA = 38
val TYPE_SEND_PREOPERATIVE_PROBLEM = 39
val TYPE_RECEIVE_PREOPERATIVE_PROBLEM = 40
val TYPE_SEND_CARD_FU_PRESCRIPTION = 41
val TYPE_RECEIVE_CARD_FU_PRESCRIPTION = 42
val TYPE_SEND_CARD_PRESCRIPTION_HISTORY = 43
val TYPE_RECEIVE_CARD_PRESCRIPTION_HISTORY = 44
val TYPE_SEND_SUBSEQUENT_VISIT_CERTIFICATE_UPLOAD = 45
val TYPE_RECEIVE_SUBSEQUENT_VISIT_CERTIFICATE_UPLOAD = 46
val TYPE_SEND_ITEM_STATUS_CHANGE = 47
val TYPE_RECEIVE_ITEM_STATUS_CHANGE = 48
val TYPE_SEND_FORM_STATUS_CHANGE = 49
val TYPE_RECEIVE_FORM_STATUS_CHANGE = 50

val TYPE_EMPTY = 100

val LAYOUT_SEND_TEXT = R.layout.item_text_send
val LAYOUT_RECEIVE_TEXT = R.layout.item_text_receive
val LAYOUT_SEND_IMAGE = R.layout.item_image_send
val LAYOUT_RECEIVE_IMAGE = R.layout.item_image_receive
val LAYOUT_RECEIVE_AUDIO = R.layout.item_audio_receive
val LAYOUT_SEND_AUDIO = R.layout.item_audio_send
val LAYOUT_SEND_CARD_CONDITION_DATA = R.layout.item_empty
val LAYOUT_SEND_CARD_DOCTOR_DIAGNOSED = R.layout.item_send_card_doctor_diagnosed
val LAYOUT_RECEIVE_CARD_DOCTOR_DIAGNOSED = R.layout.item_empty
val LAYOUT_SEND_CARD_INSPECTION_CHECK = R.layout.item_send_card_inspection_check
val LAYOUT_RECEIVE_CARD_INSPECTION_CHECK = R.layout.item_empty
val LAYOUT_SEND_CARD_PRESCRIPTION = R.layout.item_send_card_prescription
val LAYOUT_RECEIVE_CARD_PRESCRIPTION = R.layout.item_empty

//没有
val LAYOUT_SEND_RECOMMEND_DOCTOR = R.layout.item_empty

//没有
val LAYOUT_RECEIVE_RECOMMEND_DOCTOR = R.layout.item_empty

//没有
val LAYOUT_SEND_NOTICE_DRUG = R.layout.item_empty

//没有
val LAYOUT_RECEIVE_NOTICE_DRUG = R.layout.item_empty
val LAYOUT_RECEIVE_SYSTEM_TEXT = R.layout.item_system_text
val LAYOUT_RECEIVE_SYSTEM_TEXT_ACTION = R.layout.item_system_text_action
val LAYOUT_RECEIVE_SYSTEM_TEXT_CENTER = R.layout.item_system_text_center

//没有
val LAYOUT_SEND_SYSTEM_CARD_DATA_PERFECT = R.layout.item_empty

//没有
val LAYOUT_RECEIVE_SYSTEM_CARD_DATA_PERFECT = R.layout.item_empty

val LAYOUT_SEND_VIDEO = R.layout.item_send_video_and_phone

val LAYOUT_RECEIVE_VIDEO = R.layout.item_receive_video_and_phone

val LAYOUT_SEND_VIDEO_TIP = R.layout.item_phone_and_video_tip

//没有
val LAYOUT_SEND_ROOM_SYSTEM = R.layout.item_empty

//没有
val LAYOUT_RECEIVE_ROOM_SYSTEM = R.layout.item_empty

val LAYOUT_SEND_PHONE = R.layout.item_send_video_and_phone

val LAYOUT_RECEIVE_PHONE = R.layout.item_receive_video_and_phone

val LAYOUT_SEND_PHONE_TIP = R.layout.item_phone_and_video_tip

val LAYOUT_SEND_PATIENT_EDUCATION_DATA = R.layout.item_send_patient_education_data

//没有
val LAYOUT_RECEIVE_PATIENT_EDUCATION_DATA = R.layout.item_empty

//没有
val LAYOUT_SEND_PREOPERATIVE_PROBLEM = R.layout.item_empty

//没有
val LAYOUT_RECEIVE_PREOPERATIVE_PROBLEM = R.layout.item_empty

//没有
val LAYOUT_SEND_CARD_FU_PRESCRIPTION = R.layout.item_empty
val LAYOUT_RECEIVE_CARD_FU_PRESCRIPTION = R.layout.item_receive_card_fu_prescription

//没有
val LAYOUT_SEND_CARD_PRESCRIPTION_HISTORY = R.layout.item_empty
val LAYOUT_RECEIVE_CARD_PRESCRIPTION_HISTORY = R.layout.item_receive_card_history_prescription
val LAYOUT_SEND_SUBSEQUENT_VISIT_CERTIFICATE_UPLOAD =
    R.layout.item_send_subsequent_visit_certificate_upload

//没有
val LAYOUT_RECEIVE_SUBSEQUENT_VISIT_CERTIFICATE_UPLOAD = R.layout.item_empty

//没有
val LAYOUT_SEND_ITEM_STATUS_CHANGE = R.layout.item_empty

//没有
val LAYOUT_RECEIVE_ITEM_STATUS_CHANGE = R.layout.item_empty

//没有
val LAYOUT_SEND_FORM_STATUS_CHANGE = R.layout.item_empty

//没有
val LAYOUT_RECEIVE_FORM_STATUS_CHANGE = R.layout.item_empty
val LAYOUT_EMPTY = R.layout.item_empty

fun getMessageItemType(isSend: Boolean, msgType: String): Int {
    when (msgType) {
        MessageCustomType.TEXT -> {
            return if (isSend) TYPE_SEND_TEXT else TYPE_RECEIVE_TEXT
        }
        MessageCustomType.IMAGE -> {
            return if (isSend) TYPE_SEND_IMAGE else TYPE_RECEIVE_IMAGE
        }
        MessageCustomType.SOUND -> {
            return if (isSend) TYPE_SEND_AUDIO else TYPE_RECEIVE_AUDIO
        }

        //医生诊断
        MessageCustomType.CARD_DOCTOR_DIAGNOSED -> {
            return if (isSend) TYPE_SEND_CARD_DOCTOR_DIAGNOSED else TYPE_RECEIVE_CARD_DOCTOR_DIAGNOSED
        }
        //检验检查
        MessageCustomType.CARD_INSPECTION_CHECK -> {
            return if (isSend) TYPE_SEND_CARD_INSPECTION_CHECK else TYPE_RECEIVE_CARD_INSPECTION_CHECK
        }
        //处方单
        MessageCustomType.CARD_PRESCRIPTION -> {
            return if (isSend) TYPE_SEND_CARD_PRESCRIPTION else TYPE_RECEIVE_CARD_PRESCRIPTION
        }
        //医生卡片
        MessageCustomType.RECOMMEND_DOCTOR -> {
            return if (isSend) TYPE_SEND_RECOMMEND_DOCTOR else TYPE_RECEIVE_RECOMMEND_DOCTOR
        }
        //须知-卡片
        MessageCustomType.NOTICE_DRUG -> {
            return if (isSend) TYPE_SEND_NOTICE_DRUG else TYPE_RECEIVE_NOTICE_DRUG
        }
        //系统文本消息
        MessageCustomType.SYSTEM_TEXT -> {
            return TYPE_RECEIVE_SYSTEM_TEXT
        }
        //系统可点击文本消息
        MessageCustomType.SYSTEM_TEXT_ACTION -> {
            return TYPE_RECEIVE_SYSTEM_TEXT_ACTION
        }
        //系统推荐消息
        MessageCustomType.SYSTEM_TEXT_CENTER -> {
            return TYPE_RECEIVE_SYSTEM_TEXT_CENTER
        }
        //系统资料完善
        MessageCustomType.SYSTEM_CARD_DATA_PERFECT -> {
            return if (isSend) TYPE_SEND_SYSTEM_CARD_DATA_PERFECT else TYPE_RECEIVE_SYSTEM_CARD_DATA_PERFECT
        }
        //视频消息
        MessageCustomType.VIDEO -> {
            return if (isSend) TYPE_SEND_VIDEO else TYPE_RECEIVE_VIDEO
        }
        //视频消息提示
        MessageCustomType.VIDEO_TIP -> {
            return TYPE_SEND_VIDEO_TIP
        }
        //视频-系统消息
        MessageCustomType.ROOM_SYSTEM -> {
            return if (isSend) TYPE_SEND_ROOM_SYSTEM else TYPE_RECEIVE_ROOM_SYSTEM
        }
        // 电话消息
        MessageCustomType.PHONE -> {
            return if (isSend) TYPE_SEND_PHONE else TYPE_RECEIVE_PHONE
        }
        // 电话消息提示
        MessageCustomType.PHONE_TIP -> {
            return TYPE_PHONE_TIP
        }
        // 患教资料
        MessageCustomType.PATIENT_EDUCATION_DATA -> {
            return if (isSend) TYPE_SEND_PATIENT_EDUCATION_DATA else TYPE_RECEIVE_PATIENT_EDUCATION_DATA
        }
        // 术前问题
        MessageCustomType.PREOPERATIVE_PROBLEM -> {
            return if (isSend) TYPE_SEND_PREOPERATIVE_PROBLEM else TYPE_RECEIVE_PREOPERATIVE_PROBLEM
        }
        // 复诊开方
        MessageCustomType.CARD_FU_PRESCRIPTION -> {
            return if (isSend) TYPE_SEND_CARD_FU_PRESCRIPTION else TYPE_RECEIVE_CARD_FU_PRESCRIPTION
        }
        // 历史处方
        MessageCustomType.CARD_PRESCRIPTION_HISTORY -> {
            return if (isSend) TYPE_SEND_CARD_PRESCRIPTION_HISTORY else TYPE_RECEIVE_CARD_PRESCRIPTION_HISTORY
        }
        // 复诊上传凭证
        MessageCustomType.SUBSEQUENT_VISIT_CERTIFICATE_UPLOAD -> {
            return if (isSend) TYPE_SEND_SUBSEQUENT_VISIT_CERTIFICATE_UPLOAD else TYPE_RECEIVE_SUBSEQUENT_VISIT_CERTIFICATE_UPLOAD
        }
        // 状态发生变化推动的消息
        MessageCustomType.ITEM_STATUS_CHANGE -> {
            return if (isSend) TYPE_SEND_ITEM_STATUS_CHANGE else TYPE_RECEIVE_ITEM_STATUS_CHANGE
        }
        // 表单状态变更
        MessageCustomType.FORM_STATUS_CHANGE -> {
            return if (isSend) TYPE_SEND_FORM_STATUS_CHANGE else TYPE_RECEIVE_FORM_STATUS_CHANGE
        }
    }
    return TYPE_EMPTY
}