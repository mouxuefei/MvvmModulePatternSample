package com.scheartmed.lib_im.views.adapter


import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseDelegateMultiAdapter
import com.chad.library.adapter.base.delegate.BaseMultiTypeDelegate
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.netease.nimlib.sdk.NIMClient
import com.netease.nimlib.sdk.v2.message.V2NIMMessage
import com.netease.nimlib.sdk.v2.message.V2NIMMessageService
import com.netease.nimlib.sdk.v2.message.attachment.V2NIMMessageAudioAttachment
import com.netease.nimlib.sdk.v2.message.attachment.V2NIMMessageImageAttachment
import com.netease.nimlib.sdk.v2.message.attachment.V2NIMMessageVideoAttachment
import com.netease.nimlib.sdk.v2.message.enums.V2NIMMessageSendingState
import com.netease.nimlib.sdk.v2.message.enums.V2NIMMessageType
import com.orhanobut.logger.Logger
import com.scheartmed.lib_im.R
import com.scheartmed.lib_im.R.id.chat_item_content_text
import com.scheartmed.lib_im.data.MessageItem
import com.scheartmed.lib_im.utils.ChatImageLoader
import com.scheartmed.lib_im.utils.DateTimeUtil
import com.scheartmed.lib_im.widget.emoji.EmojiUtils
import com.scheartmed.lib_im.widget.emoji.EmojiUtils.*


class ChatAdapter(context: Context, data: MutableList<MessageItem>) :
    BaseDelegateMultiAdapter<MessageItem, BaseViewHolder>(data) {

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

        getMultiTypeDelegate()?.addItemType(MSG_TEXT_R, R.layout.item_text_send)
            ?.addItemType(MSG_IMG_R, R.layout.item_image_send)
            ?.addItemType(MSG_AUDIO_R, R.layout.item_audio_send)
            ?.addItemType(MSG_VIDEO_R, R.layout.item_video_send)
            ?.addItemType(MSG_FILE_R, R.layout.item_file_send)
            ?.addItemType(MSG_NOTIFICATION_R, R.layout.item_notication)
            ?.addItemType(MSG_TIPS_R, R.layout.item_tips)
            ?.addItemType(MSG_CUSTOM_1_R, R.layout.item_notication)

            ?.addItemType(MSG_TEXT_L, R.layout.item_text_receive)
            ?.addItemType(MSG_IMG_L, R.layout.item_image_receive)
            ?.addItemType(MSG_VIDEO_L, R.layout.item_video_receive)
            ?.addItemType(MSG_AUDIO_L, R.layout.item_audio_receive)
            ?.addItemType(MSG_FILE_L, R.layout.item_file_receive)
            ?.addItemType(MSG_NOTIFICATION_L, R.layout.item_notication)
            ?.addItemType(MSG_TIPS_L, R.layout.item_tips)
            ?.addItemType(MSG_CUSTOM_1_L, R.layout.item_notication)
            ?.addItemType(MSG_EMPTY, R.layout.item_empty)

            ?.addItemType(MSG_TIME, R.layout.item_time)
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
        tvRead?.let {
            val isPeerRead: Boolean =
                NIMClient.getService(V2NIMMessageService::class.java).isPeerRead(message)

            Logger.e("isPeerRead" + isPeerRead)
            it.text = if (isPeerRead) "已读" else "未读"
        }
    }

    /**
     * 设置用户头像
     */
    private fun setUserIcon(holder: BaseViewHolder, item: V2NIMMessage) {
        //TODO:
//        url?.let {
//            val iv = holder.getViewOrNull<ImageView>(R.id.chat_item_header)
//            GlideUtils.loadCircleImage(
//                context, url, iv
//            )
//        }
    }

    /**
     * 时间是否显示
     * @param helper
     * @param item
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
                //TODO:
            }

            MSG_NOTIFICATION_L, MSG_NOTIFICATION_R -> {
                //TODO:
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

    private fun setVideoType(item: V2NIMMessage, helper: BaseViewHolder) {
        val ivVideo = helper.getViewOrNull<ImageView>(R.id.ivVideoCover)
        ivVideo?.let {
            val attachment = item.attachment as V2NIMMessageVideoAttachment
            Glide.with(context)
                .asBitmap()
                .load(attachment.url) // http/https 视频地址
                .centerCrop()
                .frame(1000 * 1000) // 指定取 1 秒处的帧 (单位微秒)
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
            Logger.e("image type thumbUrl:${it.path} , url:${it.url}, name ${it.name}")
            if (it.name?.contains(".gif", true) == true || it.name?.contains(
                    ".GIF", true
                ) == true
            ) {
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
        val view = helper.getView<TextView>(chat_item_content_text)
        val text2Emoji = text2Emoji(
            context, item.text,
            view.textSize
        )
        helper.setText(chat_item_content_text, text2Emoji)
    }

}
