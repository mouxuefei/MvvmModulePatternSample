package com.scheartmed.lib_im.views.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestOptions
import com.fortunes.commonsdk.base.BaseActivity
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.config.SelectMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnResultCallbackListener
import com.mou.basemvvm.BaseApplication
import com.netease.nimlib.sdk.NIMClient
import com.netease.nimlib.sdk.v2.auth.V2NIMLoginService
import com.netease.nimlib.sdk.v2.conversation.enums.V2NIMConversationType
import com.netease.nimlib.sdk.v2.message.V2NIMMessage
import com.netease.nimlib.sdk.v2.message.V2NIMMessageService
import com.netease.nimlib.sdk.v2.message.V2NIMP2PMessageReadReceipt
import com.netease.nimlib.sdk.v2.message.attachment.V2NIMMessageAudioAttachment
import com.netease.nimlib.sdk.v2.message.attachment.V2NIMMessageFileAttachment
import com.netease.nimlib.sdk.v2.message.attachment.V2NIMMessageImageAttachment
import com.netease.nimlib.sdk.v2.message.attachment.V2NIMMessageVideoAttachment
import com.netease.nimlib.sdk.v2.message.enums.V2NIMMessageType
import com.netease.nimlib.sdk.v2.message.result.V2NIMMessageListResult
import com.netease.nimlib.sdk.v2.user.V2NIMUserService
import com.netease.nimlib.sdk.v2.utils.V2NIMConversationIdUtil
import com.orhanobut.logger.Logger
import com.scheartmed.lib_im.R
import com.scheartmed.lib_im.data.MessageItem
import com.scheartmed.lib_im.data.model.ChatSession
import com.scheartmed.lib_im.databinding.ActivityChatP2pBinding
import com.scheartmed.lib_im.ext.retrySendMessageDialog
import com.scheartmed.lib_im.listener.CustomMessageListener
import com.scheartmed.lib_im.utils.ChatMsgHandler
import com.scheartmed.lib_im.utils.ChatMsgHandler.TEN_MINUTE
import com.scheartmed.lib_im.utils.FilePickerAndSender
import com.scheartmed.lib_im.utils.FileUtils
import com.scheartmed.lib_im.utils.GlideEngine
import com.scheartmed.lib_im.utils.ImageFileCompressEngine
import com.scheartmed.lib_im.utils.MediaManager
import com.scheartmed.lib_im.viewmodels.ChatP2PViewModel
import com.scheartmed.lib_im.views.adapter.ChatAdapter
import com.scheartmed.lib_im.widget.ChatContextMenu
import com.scheartmed.lib_im.widget.ChatUiHelper
import com.scheartmed.lib_im.widget.RelativePopupWindow
import com.scheartmed.lib_im.widget.morelayout.MoreLayoutItemBean
import java.io.File


/**
 * @FileName: ChatP2PActivity.java
 * @author: villa_mou
 * @date: 08-13:45
 * @version V1.0 <描述当前版本功能>
 * @desc
 */
class ChatP2PActivity : BaseActivity<ChatP2PViewModel>() {
    override val binding: ActivityChatP2pBinding by lazy {
        ActivityChatP2pBinding.inflate(layoutInflater)
    }

    override fun providerVMClass(): Class<ChatP2PViewModel> = ChatP2PViewModel::class.java



    override fun initView() {
        initTitleBar()

    }

    override fun initData() {

    }

    private fun initTitleBar() {
        val chatId = intent.extras?.getString("account")
        binding.titleBar.setTitle(chatId ?: "")
    }

}