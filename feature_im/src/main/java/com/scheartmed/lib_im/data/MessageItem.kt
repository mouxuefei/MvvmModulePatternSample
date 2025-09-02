package com.scheartmed.lib_im.data

import com.netease.nimlib.sdk.v2.message.V2NIMMessage

sealed class MessageItem {
    data class SdkMessage(val message: V2NIMMessage) : MessageItem()
    data class TimeDivider(val time: Long) : MessageItem()
}