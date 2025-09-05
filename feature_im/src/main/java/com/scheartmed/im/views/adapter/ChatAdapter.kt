package com.scheartmed.im.views.adapter


import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseDelegateMultiAdapter
import com.chad.library.adapter.base.delegate.BaseMultiTypeDelegate
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.netease.nimlib.sdk.NIMClient
import com.netease.nimlib.sdk.uinfo.UserService
import com.netease.nimlib.sdk.v2.V2NIMFailureCallback
import com.netease.nimlib.sdk.v2.V2NIMSuccessCallback
import com.netease.nimlib.sdk.v2.conversation.enums.V2NIMConversationType
import com.netease.nimlib.sdk.v2.message.V2NIMMessage
import com.netease.nimlib.sdk.v2.message.V2NIMMessageService
import com.netease.nimlib.sdk.v2.message.V2NIMTeamMessageReadReceiptDetail
import com.netease.nimlib.sdk.v2.message.attachment.V2NIMMessageAudioAttachment
import com.netease.nimlib.sdk.v2.message.attachment.V2NIMMessageFileAttachment
import com.netease.nimlib.sdk.v2.message.attachment.V2NIMMessageImageAttachment
import com.netease.nimlib.sdk.v2.message.attachment.V2NIMMessageNotificationAttachment
import com.netease.nimlib.sdk.v2.message.attachment.V2NIMMessageVideoAttachment
import com.netease.nimlib.sdk.v2.message.enums.V2NIMMessageSendingState
import com.netease.nimlib.sdk.v2.message.enums.V2NIMMessageType
import com.orhanobut.logger.Logger
import com.scheartmed.im.R
import com.scheartmed.im.data.MessageItem
import com.scheartmed.im.utils.ChatImageLoader
import com.scheartmed.im.utils.DateTimeUtil
import com.scheartmed.im.utils.FileUtils
import com.scheartmed.im.widget.emoji.EmojiUtils.*


class ChatAdapter(
    context: Context, data: MutableList<MessageItem>, val type: V2NIMConversationType
) : BaseDelegateMultiAdapter<MessageItem, BaseViewHolder>(data) {

    private val MSG_TEXT_L = 100
    private val MSG_IMG_L = 101
    private val MSG_AUDIO_L = 102
    private val MSG_VIDEO_L = 103
    private val MSG_FILE_L = 104
    private val MSG_NOTIFICATION_L = 105
    private val MSG_TIPS_L = 106
    private val MSG_CUSTOM_1_L = 107
    private val MSG_CUSTOM_2_L = 108


    private val MSG_EMPTY = 300
    private val MSG_TIME = 400


    private val MSG_TEXT_R = 200
    private val MSG_IMG_R = 201
    private val MSG_AUDIO_R = 202
    private val MSG_VIDEO_R = 203
    private val MSG_FILE_R = 204
    private val MSG_NOTIFICATION_R = 205
    private val MSG_TIPS_R = 206
    private val MSG_CUSTOM_1_R = 207
    private val MSG_CUSTOM_2_R = 208

    init {
        setMultiTypeDelegate(object : BaseMultiTypeDelegate<MessageItem>() {
            override fun getItemType(data: List<MessageItem>, position: Int): Int {
                val entity = data[position]
                return if (entity is MessageItem.SdkMessage) {
                    val isSend = entity.message.isSelf
                    getMsgViewType(isSend, entity.message)
                } else {
                    MSG_TIME
                }

            }
        })

        getMultiTypeDelegate()?.addItemType(MSG_TEXT_R, R.layout.item_send_text)
            ?.addItemType(MSG_IMG_R, R.layout.item_send_image)
            ?.addItemType(MSG_AUDIO_R, R.layout.item_send_audio)
            ?.addItemType(MSG_VIDEO_R, R.layout.item_send_video)
            ?.addItemType(MSG_FILE_R, R.layout.item_send_file)
            ?.addItemType(MSG_NOTIFICATION_R, R.layout.item_notication)
            ?.addItemType(MSG_TIPS_R, R.layout.item_tips)
            ?.addItemType(MSG_CUSTOM_1_R, R.layout.item_notication)

            ?.addItemType(MSG_TEXT_L, R.layout.item_receive_text)
            ?.addItemType(MSG_IMG_L, R.layout.item_receive_image)
            ?.addItemType(MSG_VIDEO_L, R.layout.item_receive_video)
            ?.addItemType(MSG_AUDIO_L, R.layout.item_receive_audio)
            ?.addItemType(MSG_FILE_L, R.layout.item_receive_file)
            ?.addItemType(MSG_NOTIFICATION_L, R.layout.item_notication)
            ?.addItemType(MSG_TIPS_L, R.layout.item_tips)
            ?.addItemType(MSG_CUSTOM_1_L, R.layout.item_notication)

            ?.addItemType(MSG_EMPTY, R.layout.item_empty)?.addItemType(MSG_TIME, R.layout.item_time)
    }

    private fun getMsgViewType(isSend: Boolean, msg: V2NIMMessage): Int {
        val type = msg.messageType
        when (type) {
            V2NIMMessageType.V2NIM_MESSAGE_TYPE_TEXT -> {
                return if (isSend) MSG_TEXT_R else MSG_TEXT_L
            }

            V2NIMMessageType.V2NIM_MESSAGE_TYPE_IMAGE -> {
                return if (isSend) MSG_IMG_R else MSG_IMG_L
            }

            V2NIMMessageType.V2NIM_MESSAGE_TYPE_AUDIO -> {
                return if (isSend) MSG_AUDIO_R else MSG_AUDIO_L
            }

            V2NIMMessageType.V2NIM_MESSAGE_TYPE_VIDEO -> {
                return if (isSend) MSG_VIDEO_R else MSG_VIDEO_L
            }

            V2NIMMessageType.V2NIM_MESSAGE_TYPE_FILE -> {
                return if (isSend) MSG_FILE_R else MSG_FILE_L
            }

            V2NIMMessageType.V2NIM_MESSAGE_TYPE_NOTIFICATION -> {
                return if (isSend) MSG_NOTIFICATION_R else MSG_NOTIFICATION_L
            }

            V2NIMMessageType.V2NIM_MESSAGE_TYPE_TIPS -> {
                return if (isSend) MSG_TIPS_R else MSG_TIPS_L
            }

            V2NIMMessageType.V2NIM_MESSAGE_TYPE_CUSTOM -> {
                val attachment = msg.attachment
                val raw = attachment.raw
                Logger.d("IM", "自定义消息内容: $raw")
                return if (isSend) MSG_CUSTOM_1_R else MSG_CUSTOM_1_L
            }

            else -> {
                return MSG_EMPTY
            }
        }
    }

    override fun convert(holder: BaseViewHolder, item: MessageItem) {
        if (item is MessageItem.SdkMessage) {
            setSendStatus(holder, item.message)
            setContent(holder, item.message)
            setUserIcon(holder, item.message)
            setReadStatus(holder, item.message)
        } else if (item is MessageItem.TimeDivider) {
            setTimeVisible(holder, item)
        }

    }

    private fun setReadStatus(holder: BaseViewHolder, message: V2NIMMessage) {
        val tvRead = holder.getViewOrNull<TextView>(R.id.chat_item_tv_read)

        val v2MessageService = NIMClient.getService(
            V2NIMMessageService::class.java
        )
        if (!message.isSelf || message.messageType == V2NIMMessageType.V2NIM_MESSAGE_TYPE_NOTIFICATION || message.messageType == V2NIMMessageType.V2NIM_MESSAGE_TYPE_TIPS) {
            return
        }
        if (tvRead != null && tvRead.text == "已读") {
            return
        }
        v2MessageService.getTeamMessageReceiptDetail(message,
            null,
            {
                tvRead?.let { tv ->
                    //TODO:
                    Logger.e("readReceipt=" + it.readReceipt.readCount)
                    tv.text = if (it.readReceipt.readCount > 0) "已读" else "未读"
                }
            },
            {
                Logger.e("readReceipt error=" + it.desc)
            })
    }

    /**
     * 设置用户头像
     */
    private fun setUserIcon(holder: BaseViewHolder, item: V2NIMMessage) {
        val userInfo = NIMClient.getService(UserService::class.java).getUserInfo(item.senderId)
        val tvName = holder.getViewOrNull<TextView>(R.id.tvName)
        val tvAvatar = holder.getViewOrNull<ImageView>(R.id.chat_item_header)
        tvName?.let {
            it.text = userInfo?.name ?: ""
            it.visibility =
                if (type == V2NIMConversationType.V2NIM_CONVERSATION_TYPE_P2P) View.GONE else View.VISIBLE
        }
        tvAvatar?.apply {
            userInfo?.avatar?.let { url ->
                ChatImageLoader.loadCircleImage(context, url, this, item.isSelf)
            }
        }
    }

    /**
     * 时间是否显示
     */
    private fun setTimeVisible(helper: BaseViewHolder, item: MessageItem.TimeDivider) {
        helper.setText(R.id.item_tv_time, item.time.let { DateTimeUtil.getTimeFormatText(it) })
    }

    /**
     * 发送状态，loading,error
     */
    private fun setSendStatus(helper: BaseViewHolder, item: V2NIMMessage) {
        val isSend = item.isSelf
        if (isSend) {
            val progressVisible = isSending(item)
            val failVisible = isSendFail(item)
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
     * 是否在发送中
     */
    private fun isSending(message: V2NIMMessage): Boolean {
        return message.isSelf && message.sendingState == V2NIMMessageSendingState.V2NIM_MESSAGE_SENDING_STATE_SENDING
    }

    /**
     * 是否发送失败
     */
    private fun isSendFail(message: V2NIMMessage): Boolean {
        return message.isSelf && message.sendingState == V2NIMMessageSendingState.V2NIM_MESSAGE_SENDING_STATE_FAILED
    }


    /**
     * 内容
     */
    private fun setContent(helper: BaseViewHolder, item: V2NIMMessage) {
        when (helper.itemViewType) {
            MSG_TEXT_R, MSG_TEXT_L -> {
                setTextType(item, helper)
            }

            MSG_IMG_L, MSG_IMG_R -> {
                setImageType(item, helper)
            }

            MSG_VIDEO_L, MSG_VIDEO_R -> {
                setVideoType(item, helper)
            }

            MSG_AUDIO_L, MSG_AUDIO_R -> {
                setSoundType(item, helper)
            }

            MSG_FILE_L, MSG_FILE_R -> {
                setFileType(item, helper)
            }

            MSG_NOTIFICATION_L, MSG_NOTIFICATION_R -> {
                //TODO:
                setNotificationType(item, helper)
            }

            MSG_TIPS_L, MSG_TIPS_R -> {
                //TODO:
            }

            MSG_CUSTOM_1_L, MSG_CUSTOM_1_R -> {
                //TODO:
            }

            else -> {
            }
        }
    }

    private fun setNotificationType(item: V2NIMMessage, helper: BaseViewHolder) {
        val v2NIMMessageNotificationAttachment =
            item.attachment as V2NIMMessageNotificationAttachment

        val tvNotification = helper.getView<TextView>(R.id.tvNotification)
        tvNotification.text = "通知"
    }

    private fun setFileType(item: V2NIMMessage, helper: BaseViewHolder) {
        val ivFileType = helper.getViewOrNull<ImageView>(R.id.rc_msg_iv_file_type_image)
        val v2NIMMessageFileAttachment = item.attachment as V2NIMMessageFileAttachment
        when (FileUtils.getExtensionName(v2NIMMessageFileAttachment.name)) {
            "doc", "docx" -> ivFileType?.setImageResource(R.drawable.icon_file_word)
            "ppt", "pptx" -> ivFileType?.setImageResource(R.drawable.icon_file_ppt)
            "xls", "xlsx" -> ivFileType?.setImageResource(R.drawable.icon_file_excel)
            "pdf" -> ivFileType?.setImageResource(R.drawable.icon_file_pdf)
            else -> ivFileType?.setImageResource(R.drawable.icon_file_other)
        }
    }

    private fun setVideoType(item: V2NIMMessage, helper: BaseViewHolder) {
        val ivVideo = helper.getViewOrNull<ImageView>(R.id.ivVideoCover)
        ivVideo?.let {
            val attachment = item.attachment as V2NIMMessageVideoAttachment
            Logger.e("attachment.url=" + attachment.url)
            Glide.with(context).asBitmap().load(attachment.url) // http/https 视频地址
                .centerCrop().frame(1000 * 1000) // 指定取 1 秒处的帧 (单位微秒)
                .into(ivVideo)
        }
    }


    private fun setSoundType(item: V2NIMMessage, helper: BaseViewHolder) {
        val tvDuration = helper.getViewOrNull<TextView>(R.id.tvDuration)
        val attachment = item.attachment as? V2NIMMessageAudioAttachment
        tvDuration?.let {
            val second = attachment?.duration
            tvDuration.text = DateTimeUtil.formatSecondsTo00(second ?: 0)
        }
    }


    private fun setImageType(
        item: V2NIMMessage, helper: BaseViewHolder
    ) {
        val imageAttachment = item.attachment as? V2NIMMessageImageAttachment
        imageAttachment?.let { it ->
            if (it.name?.contains(".gif", true) == true) {
                val imageUrl = it.path.takeIf { !it.isNullOrEmpty() } ?: it.url
                val maxWidth =
                    helper.getView<View>(R.id.bivPic).resources.displayMetrics.widthPixels / 2 // 最大宽度
                ChatImageLoader.loadGif(
                    context, imageUrl, helper.getView(R.id.bivPic), maxWidth
                )
                return
            }
            // 本地 url
            val imageUrl = it.path.takeIf { !it.isNullOrEmpty() } ?: it.url
            val maxWidth =
                helper.getView<View>(R.id.bivPic).resources.displayMetrics.widthPixels / 2 // 最大宽度
            ChatImageLoader.loadImage(
                context, imageUrl, helper.getView(R.id.bivPic), maxWidth, 8
            )
        }
    }

    private fun setTextType(
        item: V2NIMMessage, helper: BaseViewHolder
    ) {
        val view = helper.getView<TextView>(R.id.chat_item_content_text)
        val text2Emoji = text2Emoji(
            context, item.text, view.textSize
        )
        helper.setText(R.id.chat_item_content_text, text2Emoji)
    }

}
