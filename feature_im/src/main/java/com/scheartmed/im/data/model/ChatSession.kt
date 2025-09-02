package com.scheartmed.im.data.model

import com.netease.nimlib.sdk.v2.conversation.enums.V2NIMConversationType
import com.netease.nimlib.sdk.v2.user.V2NIMUser

class ChatSession {
    var conversationType: V2NIMConversationType? = null
    var conversationId: String? = null
    var myInfo: V2NIMUser? = null
    var chatInfo: V2NIMUser? = null
}
