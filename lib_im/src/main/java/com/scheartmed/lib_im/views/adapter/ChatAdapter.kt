package com.scheartmed.lib_im.views.adapter

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.chad.library.adapter.base.BaseDelegateMultiAdapter
import com.chad.library.adapter.base.delegate.BaseMultiTypeDelegate
import com.chad.library.adapter.base.viewholder.BaseViewHolder


import com.google.gson.GsonBuilder
import com.google.gson.internal.LinkedTreeMap
import com.scheartmed.lib_im.R
import com.scheartmed.lib_im.data.model.ChatConfigEntity
import com.scheartmed.lib_im.data.model.MessageCustomType
import com.scheartmed.lib_im.data.model.MessageListEntity
import com.scheartmed.lib_im.data.model.MsgSendStatus
import com.scheartmed.lib_im.data.model.SystemTextActionEntity
import com.scheartmed.lib_im.data.model.SystemTextListEntity
import com.scheartmed.lib_im.utils.DateTimeUtil
import com.scheartmed.lib_im.utils.GlideUtils

class ChatAdapter(context: Context, data: MutableList<MessageListEntity>) :
    BaseDelegateMultiAdapter<MessageListEntity, BaseViewHolder>(data) {
    /**
     * 我的用户id
     */
    private val myUserId = "xxx"
    private var mChatConfigData: ChatConfigEntity? = null
    private var mSystemActionListener: SystemActionListener? = null
    val mParseMapGson = GsonBuilder().enableComplexMapKeySerialization().create()

    init {
        setMultiTypeDelegate(object : BaseMultiTypeDelegate<MessageListEntity>() {
            override fun getItemType(data: List<MessageListEntity>, position: Int): Int {
                val entity = data[position]
                val isSend = entity.from == myUserId
                val type = entity.msgType?.let { getMessageItemType(isSend, it) }
                return type ?: TYPE_EMPTY
            }
        })
        getMultiTypeDelegate()?.addItemType(TYPE_SEND_TEXT, LAYOUT_SEND_TEXT)
            ?.addItemType(TYPE_RECEIVE_TEXT, LAYOUT_RECEIVE_TEXT)
            ?.addItemType(TYPE_SEND_IMAGE, LAYOUT_SEND_IMAGE)
            ?.addItemType(TYPE_RECEIVE_IMAGE, LAYOUT_RECEIVE_IMAGE)
            ?.addItemType(TYPE_SEND_AUDIO, LAYOUT_SEND_AUDIO)
            ?.addItemType(TYPE_RECEIVE_AUDIO, LAYOUT_RECEIVE_AUDIO)
            ?.addItemType(TYPE_EMPTY, LAYOUT_EMPTY)
            ?.addItemType(TYPE_SEND_CARD_CONDITION_DATA, LAYOUT_SEND_CARD_CONDITION_DATA)
            ?.addItemType(TYPE_SEND_CARD_DOCTOR_DIAGNOSED, LAYOUT_SEND_CARD_DOCTOR_DIAGNOSED)
            ?.addItemType(TYPE_RECEIVE_CARD_DOCTOR_DIAGNOSED, LAYOUT_RECEIVE_CARD_DOCTOR_DIAGNOSED)
            ?.addItemType(TYPE_SEND_CARD_INSPECTION_CHECK, LAYOUT_SEND_CARD_INSPECTION_CHECK)
            ?.addItemType(TYPE_RECEIVE_CARD_INSPECTION_CHECK, LAYOUT_RECEIVE_CARD_INSPECTION_CHECK)
            ?.addItemType(TYPE_SEND_CARD_PRESCRIPTION, LAYOUT_SEND_CARD_PRESCRIPTION)
            ?.addItemType(TYPE_RECEIVE_CARD_PRESCRIPTION, LAYOUT_RECEIVE_CARD_PRESCRIPTION)
            ?.addItemType(TYPE_SEND_RECOMMEND_DOCTOR, LAYOUT_SEND_RECOMMEND_DOCTOR)
            ?.addItemType(TYPE_RECEIVE_RECOMMEND_DOCTOR, LAYOUT_RECEIVE_RECOMMEND_DOCTOR)
            ?.addItemType(TYPE_SEND_NOTICE_DRUG, LAYOUT_SEND_NOTICE_DRUG)
            ?.addItemType(TYPE_RECEIVE_NOTICE_DRUG, LAYOUT_RECEIVE_NOTICE_DRUG)
            ?.addItemType(TYPE_RECEIVE_SYSTEM_TEXT, LAYOUT_RECEIVE_SYSTEM_TEXT)
            ?.addItemType(TYPE_RECEIVE_SYSTEM_TEXT_ACTION, LAYOUT_RECEIVE_SYSTEM_TEXT_ACTION)
            ?.addItemType(TYPE_RECEIVE_SYSTEM_TEXT_CENTER, LAYOUT_RECEIVE_SYSTEM_TEXT_CENTER)
            ?.addItemType(TYPE_SEND_SYSTEM_CARD_DATA_PERFECT, LAYOUT_SEND_SYSTEM_CARD_DATA_PERFECT)
            ?.addItemType(
                TYPE_RECEIVE_SYSTEM_CARD_DATA_PERFECT, LAYOUT_RECEIVE_SYSTEM_CARD_DATA_PERFECT
            )?.addItemType(TYPE_SEND_VIDEO, LAYOUT_SEND_VIDEO)
            ?.addItemType(TYPE_RECEIVE_VIDEO, LAYOUT_RECEIVE_VIDEO)
            ?.addItemType(TYPE_SEND_VIDEO_TIP, LAYOUT_SEND_VIDEO_TIP)
            ?.addItemType(TYPE_SEND_ROOM_SYSTEM, LAYOUT_SEND_ROOM_SYSTEM)
            ?.addItemType(TYPE_RECEIVE_ROOM_SYSTEM, LAYOUT_RECEIVE_ROOM_SYSTEM)
            ?.addItemType(TYPE_SEND_PHONE, LAYOUT_SEND_PHONE)
            ?.addItemType(TYPE_RECEIVE_PHONE, LAYOUT_RECEIVE_PHONE)
            ?.addItemType(TYPE_PHONE_TIP, LAYOUT_SEND_PHONE_TIP)
            ?.addItemType(TYPE_SEND_PATIENT_EDUCATION_DATA, LAYOUT_SEND_PATIENT_EDUCATION_DATA)
            ?.addItemType(
                TYPE_RECEIVE_PATIENT_EDUCATION_DATA, LAYOUT_RECEIVE_PATIENT_EDUCATION_DATA
            )?.addItemType(TYPE_SEND_PREOPERATIVE_PROBLEM, LAYOUT_SEND_PREOPERATIVE_PROBLEM)
            ?.addItemType(TYPE_RECEIVE_PREOPERATIVE_PROBLEM, LAYOUT_RECEIVE_PREOPERATIVE_PROBLEM)
            ?.addItemType(TYPE_SEND_CARD_FU_PRESCRIPTION, LAYOUT_SEND_CARD_FU_PRESCRIPTION)
            ?.addItemType(TYPE_RECEIVE_CARD_FU_PRESCRIPTION, LAYOUT_RECEIVE_CARD_FU_PRESCRIPTION)
            ?.addItemType(
                TYPE_SEND_CARD_PRESCRIPTION_HISTORY, LAYOUT_SEND_CARD_PRESCRIPTION_HISTORY
            )?.addItemType(
                TYPE_RECEIVE_CARD_PRESCRIPTION_HISTORY, LAYOUT_RECEIVE_CARD_PRESCRIPTION_HISTORY
            )?.addItemType(
                TYPE_SEND_SUBSEQUENT_VISIT_CERTIFICATE_UPLOAD,
                LAYOUT_SEND_SUBSEQUENT_VISIT_CERTIFICATE_UPLOAD
            )?.addItemType(
                TYPE_RECEIVE_SUBSEQUENT_VISIT_CERTIFICATE_UPLOAD,
                LAYOUT_RECEIVE_SUBSEQUENT_VISIT_CERTIFICATE_UPLOAD
            )?.addItemType(TYPE_SEND_ITEM_STATUS_CHANGE, LAYOUT_SEND_ITEM_STATUS_CHANGE)
            ?.addItemType(TYPE_RECEIVE_ITEM_STATUS_CHANGE, LAYOUT_RECEIVE_ITEM_STATUS_CHANGE)
            ?.addItemType(TYPE_SEND_FORM_STATUS_CHANGE, LAYOUT_SEND_FORM_STATUS_CHANGE)
            ?.addItemType(TYPE_RECEIVE_FORM_STATUS_CHANGE, LAYOUT_RECEIVE_FORM_STATUS_CHANGE)
    }

    override fun convert(holder: BaseViewHolder, item: MessageListEntity) {
        setStatus(holder, item)
        setContent(holder, item)
        setTimeVisible(holder, item)
        setUserIcon(holder, item)
    }

    /**
     * 设置用户头像
     */
    private fun setUserIcon(holder: BaseViewHolder, item: MessageListEntity) {
        val isSend = item.from == myUserId
        val url = if (isSend) mChatConfigData?.doctorIconUrl else mChatConfigData?.patientIconUrl
        url?.let {
            val iv = holder.getViewOrNull<ImageView>(R.id.chat_item_header)
            GlideUtils.loadCircleImage(
                context, url, iv
            )
        }
    }

    /**
     * 时间是否显示
     * @param helper
     * @param item
     */
    private fun setTimeVisible(helper: BaseViewHolder, item: MessageListEntity) {
        helper.setGone(R.id.item_tv_time, item.isShowTime == false)
        if (item.isShowTime == true) {
            helper.setText(R.id.item_tv_time, item.time?.let { DateTimeUtil.getTimeFormatText(it) })
        }
    }

    /**
     * 发送状态，loading,error
     */
    private fun setStatus(helper: BaseViewHolder, item: MessageListEntity) {
        val sentStatus = item.sentStatus
        val isSend = item.from == myUserId
        if (isSend) {
            var progressVisible = false
            var failVisible = false
            when {
                sentStatus === MsgSendStatus.SENDING -> progressVisible = true
                sentStatus === MsgSendStatus.FAILED -> failVisible = true
            }
            val progressView = helper.getViewOrNull<View>(R.id.chat_item_progress)
            val failView = helper.getViewOrNull<View>(R.id.chat_item_fail)
            progressView?.let {
                it.visibility = if (progressVisible) View.VISIBLE else View.GONE
            }
            failView?.let {
                it.visibility = if (failVisible) View.VISIBLE else View.GONE
            }

        }
    }

    /**
     * 内容
     */
    private fun setContent(helper: BaseViewHolder, item: MessageListEntity) {
        when (item.msgType) {
            MessageCustomType.TEXT -> {
                setTextType(item, helper)
            }

            MessageCustomType.IMAGE -> {
                setImageType(item, helper)
            }

            MessageCustomType.SOUND -> {
                setSoundType(item, helper)
            }

            MessageCustomType.SYSTEM_TEXT_CENTER -> {
                setSystemCenterText(item, helper)
            }

            MessageCustomType.SYSTEM_TEXT_ACTION -> {
                setSystemActionText(item, helper)
            }

            MessageCustomType.CARD_DOCTOR_DIAGNOSED -> {
                setDoctorDiagnosed(item, helper)
            }

            MessageCustomType.CARD_PRESCRIPTION -> {
                setDoctorPrescription(item, helper)
            }

            MessageCustomType.CARD_PRESCRIPTION_HISTORY -> {
                setDoctorHistoryPrescription(item, helper)
            }

            MessageCustomType.CARD_FU_PRESCRIPTION -> {
                setFuPrescription(item, helper)
            }

            MessageCustomType.SUBSEQUENT_VISIT_CERTIFICATE_UPLOAD -> {
                setFuCertificateUpload(item, helper)
            }

            MessageCustomType.PHONE_TIP -> {
                setPhoneTip(item, helper)
            }

            MessageCustomType.VIDEO_TIP -> {
                setVideoTip(item, helper)
            }

            MessageCustomType.PATIENT_EDUCATION_DATA -> {
                setEducationData(item, helper)
            }

            MessageCustomType.VIDEO -> {
                setVideo(item, helper)
            }

            MessageCustomType.PHONE -> {
                setPhone(item, helper)
            }

            MessageCustomType.CARD_INSPECTION_CHECK -> {
                setInspectionCheck(item, helper)
            }

            else -> {
            }
        }
    }

    private fun setInspectionCheck(item: MessageListEntity, helper: BaseViewHolder) {
        helper.getViewOrNull<TextView>(R.id.item_card_check_name)?.let {
            it.text = item.payLoad?.data?.get(0)?.msgContent?.Data?.diag
        }
        helper.getViewOrNull<TextView>(R.id.item_card_check_project)?.let {
            it.text = item.payLoad?.data?.get(0)?.msgContent?.Data?.inspection
        }
    }

    private fun setPhone(item: MessageListEntity, helper: BaseViewHolder) {
        val isSend = item.from == myUserId
        helper.setBackgroundResource(
            R.id.chat_item_content_icon,
            if (isSend) R.mipmap.ic_list_phone else R.mipmap.ic_list_receive_phone
        )
        val callDuration = item.payLoad?.data?.get(0)?.msgContent?.Data?.callDuration
        val formatSecondsTo00 = DateTimeUtil.formatSecondsTo00(callDuration?.toInt() ?: 0)
        helper.setText(
            R.id.chat_item_content_text, "通话时长 $formatSecondsTo00"
        )
    }

    private fun setVideo(item: MessageListEntity, helper: BaseViewHolder) {
        val isSend = item.from == myUserId
        helper.setBackgroundResource(
            R.id.chat_item_content_icon,
            if (isSend) R.mipmap.ic_list_video else R.mipmap.ic_list_recerive_video
        )
        val callDuration = item.payLoad?.data?.get(0)?.msgContent?.Data?.callDuration
        val status = item.payLoad?.data?.get(0)?.msgContent?.Data?.status
        val formatSecondsTo00 = DateTimeUtil.formatSecondsTo00(callDuration?.toInt() ?: 0)
        helper.setText(
            R.id.chat_item_content_text,
            if (status == "1") "未接通" else "通话时长 $formatSecondsTo00"
        )
    }

    private fun setEducationData(item: MessageListEntity, helper: BaseViewHolder) {
        helper.getViewOrNull<TextView>(R.id.item_patient_education_title)?.let {
            it.text = item.payLoad?.data?.get(0)?.msgContent?.Data?.title
        }
        helper.getViewOrNull<ImageView>(R.id.item_patient_education_icon)?.let {
            GlideUtils.loadChatImage(
                context, item.payLoad?.data?.get(0)?.msgContent?.Data?.frontCoverUrl, it
            )
        }
    }

    private fun setVideoTip(item: MessageListEntity, helper: BaseViewHolder) {
        helper.getViewOrNull<TextView>(R.id.item_phone_tip_title)?.let {
            it.text = "在线咨询-视频"
        }
        helper.getViewOrNull<TextView>(R.id.item_phone_tip_call)?.let {
            it.text = "立即视频"
        }
        helper.getViewOrNull<TextView>(R.id.item_phone_tip_time)?.let {
            it.text = "${item.payLoad?.data?.get(0)?.msgContent?.Data?.desiredDate} ${
                item.payLoad?.data?.get(0)?.msgContent?.Data?.desiredSlot
            }"
        }
    }

    private fun setPhoneTip(item: MessageListEntity, helper: BaseViewHolder) {
        helper.getViewOrNull<TextView>(R.id.item_phone_tip_title)?.let {
            it.text = "在线咨询-电话"
        }
        helper.getViewOrNull<TextView>(R.id.item_phone_tip_call)?.let {
            it.text = "立即电话"
        }
        helper.getViewOrNull<TextView>(R.id.item_phone_tip_time)?.let {
            it.text = "${item.payLoad?.data?.get(0)?.msgContent?.Data?.desiredDate} ${
                item.payLoad?.data?.get(0)?.msgContent?.Data?.desiredSlot
            }"
        }
    }

    private fun setFuCertificateUpload(item: MessageListEntity, helper: BaseViewHolder) {
        val tvContent = helper.getViewOrNull<TextView>(R.id.item_fzpz_content)
        val tvDesc = helper.getViewOrNull<TextView>(R.id.item_fzpz_desc)
        tvDesc?.let {
            it.text = item.payLoad?.data?.get(0)?.msgContent?.Data?.content as String
        }
        tvContent?.let {
            it.text = item.payLoad?.data?.get(0)?.msgContent?.Data?.contentTwo
        }
    }

    private fun setFuPrescription(item: MessageListEntity, helper: BaseViewHolder) {
        val diag = item.payLoad?.data?.get(0)?.msgContent?.Data?.diag
        val desc = item.payLoad?.data?.get(0)?.msgContent?.Data?.desc
        val drugName = item.payLoad?.data?.get(0)?.msgContent?.Data?.drugName
        val patientName = item.payLoad?.data?.get(0)?.msgContent?.Data?.patientName
        val gender = item.payLoad?.data?.get(0)?.msgContent?.Data?.gender
        val age = item.payLoad?.data?.get(0)?.msgContent?.Data?.age
        val tvWords = helper.getViewOrNull<TextView>(R.id.item_card_fu_words)
        val tvCard = helper.getViewOrNull<TextView>(R.id.item_card_fu_card)
        val tvDrag = helper.getViewOrNull<TextView>(R.id.item_card_fu_drag)
        val tvRecord = helper.getViewOrNull<TextView>(R.id.item_card_fu_record)
        tvWords?.let {
            it.text = desc
        }
        tvCard?.let {
            it.text = "$patientName $gender $age"
        }
        tvDrag?.let {
            it.text = diag
        }
        tvRecord?.let {
            it.text = drugName
        }
    }

    private fun setDoctorHistoryPrescription(item: MessageListEntity, helper: BaseViewHolder) {
        val rp = item.payLoad?.data?.get(0)?.msgContent?.Data?.drugNameStrs
        val result = item.payLoad?.data?.get(0)?.msgContent?.Data?.diag
        val tvName = helper.getViewOrNull<TextView>(R.id.item_card_pre_name)
        val tvRp = helper.getViewOrNull<TextView>(R.id.item_card_pre_rp)
        tvName?.let {
            it.text = result
        }
        tvRp?.let {
            it.text = rp
        }
    }

    private fun setDoctorPrescription(item: MessageListEntity, helper: BaseViewHolder) {
        val rp = item.payLoad?.data?.get(0)?.msgContent?.Data?.rp
        val result = item.payLoad?.data?.get(0)?.msgContent?.Data?.result
        val tvName = helper.getViewOrNull<TextView>(R.id.item_card_pre_name)
        val tvRp = helper.getViewOrNull<TextView>(R.id.item_card_pre_rp)
        tvName?.let {
            it.text = result
        }
        tvRp?.let {
            it.text = rp
        }
    }

    private fun setDoctorDiagnosed(item: MessageListEntity, helper: BaseViewHolder) {
        val time = item.payLoad?.data?.get(0)?.msgContent?.Data?.time
        val result = item.payLoad?.data?.get(0)?.msgContent?.Data?.result
        val tvName = helper.getViewOrNull<TextView>(R.id.item_card_diagnosed_name)
        val tvTime = helper.getViewOrNull<TextView>(R.id.item_card_diagnosed_time)
        tvName?.let {
            it.text = result
        }
        tvTime?.let {
            it.text = time
        }
    }


    private fun setSystemActionText(item: MessageListEntity, helper: BaseViewHolder) {
        item.payLoad?.data?.get(0)?.msgContent?.Data?.content?.let {
            val map = it as LinkedTreeMap<*, *>
            val messageJson = mParseMapGson.toJson(map)
            val systemTextActionEntity = mParseMapGson.fromJson<SystemTextActionEntity>(
                messageJson, SystemTextActionEntity::class.java
            ) //再根据类型转成bean对象
            val container = helper.getView<LinearLayout>(R.id.item_system_text_action_container)
            if (systemTextActionEntity.center == true) {
                container.gravity = Gravity.CENTER
            }
            val content = helper.getView<LinearLayout>(R.id.item_system_text_action_content)
            content.removeAllViews()
            systemTextActionEntity.list?.map { subItem ->
                val textView = TextView(context)
                textView.text = subItem.text
                textView.textSize = 12f
                if (subItem.highlight == true) {
                    textView.setTextColor(
                        Color.parseColor(subItem.color ?: "#0077FF")
                    )
                    textView.tag = subItem.href
                    textView.setOnClickListener {
                        mSystemActionListener?.clickView(item, subItem)
                    }
                } else {
                    textView.setTextColor(
                        Color.parseColor("#989EB4")
                    )
                }
                content.addView(textView)
            }

        }
    }


    private fun setSoundType(item: MessageListEntity, helper: BaseViewHolder) {
        val tvDuration = helper.getViewOrNull<TextView>(R.id.tvDuration)
        tvDuration?.let {
            val second = item.payLoad?.data?.get(0)?.msgContent?.Second
            tvDuration.text = DateTimeUtil.formatSecondsTo00(second ?: 0)
        }
    }

    private fun setSystemCenterText(item: MessageListEntity, helper: BaseViewHolder) {
        val tvTitle = helper.getViewOrNull<TextView>(R.id.item_system_text_title)
        tvTitle?.let {
            it.text = item.payLoad?.data?.get(0)?.msgContent?.Data?.content as String
        }
    }


    private fun setImageType(
        item: MessageListEntity, helper: BaseViewHolder
    ) {
        val imageItem = item.payLoad?.data?.get(0)?.msgContent?.ImageInfoArray?.get(1)
        val url = imageItem?.URL
        GlideUtils.loadChatImageRadius(
            context, url, 15, helper.getView(R.id.bivPic)
        )
    }

    private fun setTextType(
        item: MessageListEntity, helper: BaseViewHolder
    ) {
        val text = item.payLoad?.data?.get(0)?.msgContent?.Text
        helper.setText(R.id.chat_item_content_text, text)
    }

    fun setUserInfo(data: ChatConfigEntity) {
        mChatConfigData = data
        notifyDataSetChanged()
    }

    fun setSystemActionListener(listener: SystemActionListener) {
        mSystemActionListener = listener
    }
}

interface SystemActionListener {
    fun clickView(item: MessageListEntity, child: SystemTextListEntity)
}