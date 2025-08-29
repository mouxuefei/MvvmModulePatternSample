package com.scheartmed.lib_im.listener

import com.netease.nimlib.sdk.v2.message.V2NIMClearHistoryNotification
import com.netease.nimlib.sdk.v2.message.V2NIMMessage
import com.netease.nimlib.sdk.v2.message.V2NIMMessageDeletedNotification
import com.netease.nimlib.sdk.v2.message.V2NIMMessageListener
import com.netease.nimlib.sdk.v2.message.V2NIMMessagePinNotification
import com.netease.nimlib.sdk.v2.message.V2NIMMessageQuickCommentNotification
import com.netease.nimlib.sdk.v2.message.V2NIMMessageRevokeNotification
import com.netease.nimlib.sdk.v2.message.V2NIMP2PMessageReadReceipt
import com.netease.nimlib.sdk.v2.message.V2NIMTeamMessageReadReceipt

/**
 * @FileName: CustomMessageListener.java
 * @author: villa_mou
 * @date: 08-17:42
 * @version V1.0 <描述当前版本功能>
 * @desc
 */
abstract  class CustomMessageListener: V2NIMMessageListener {
    override fun onReceiveMessages(messages: MutableList<V2NIMMessage>) {
    }

    override fun onReceiveP2PMessageReadReceipts(readReceipts: MutableList<V2NIMP2PMessageReadReceipt>) {

    }

    override fun onReceiveTeamMessageReadReceipts(readReceipts: MutableList<V2NIMTeamMessageReadReceipt>) {
    }

    override fun onMessageRevokeNotifications(revokeNotifications: MutableList<V2NIMMessageRevokeNotification>) {
    }

    override fun onMessagePinNotification(pinNotification: V2NIMMessagePinNotification) {
    }

    override fun onMessageQuickCommentNotification(quickCommentNotification: V2NIMMessageQuickCommentNotification) {
    }

    override fun onMessageDeletedNotifications(messageDeletedNotifications: MutableList<V2NIMMessageDeletedNotification>) {
    }

    override fun onClearHistoryNotifications(clearHistoryNotifications: MutableList<V2NIMClearHistoryNotification>) {
    }

    override fun onSendMessage(message: V2NIMMessage) {
    }

    override fun onReceiveMessagesModified(messages: MutableList<V2NIMMessage>) {
    }
}