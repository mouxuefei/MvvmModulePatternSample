package com.scheartmed.im.views.activity

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
import com.core.commonsdk.base.BaseActivity
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.config.SelectMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnResultCallbackListener
import com.core.basemvvm.BaseApplication
import com.netease.nimlib.sdk.NIMClient
import com.netease.nimlib.sdk.v2.auth.V2NIMLoginService
import com.netease.nimlib.sdk.v2.conversation.enums.V2NIMConversationType
import com.netease.nimlib.sdk.v2.message.V2NIMMessage
import com.netease.nimlib.sdk.v2.message.V2NIMMessageService
import com.netease.nimlib.sdk.v2.message.attachment.V2NIMMessageAudioAttachment
import com.netease.nimlib.sdk.v2.message.attachment.V2NIMMessageFileAttachment
import com.netease.nimlib.sdk.v2.message.attachment.V2NIMMessageImageAttachment
import com.netease.nimlib.sdk.v2.message.attachment.V2NIMMessageVideoAttachment
import com.netease.nimlib.sdk.v2.message.enums.V2NIMMessageType
import com.netease.nimlib.sdk.v2.message.result.V2NIMMessageListResult
import com.netease.nimlib.sdk.v2.user.V2NIMUserService
import com.orhanobut.logger.Logger
import com.scheartmed.im.R
import com.scheartmed.im.data.MessageItem
import com.scheartmed.im.data.model.ChatSession
import com.scheartmed.im.databinding.ActivityChatGroupBinding
import com.scheartmed.im.ext.retrySendMessageDialog
import com.scheartmed.im.listener.CustomMessageListener
import com.scheartmed.im.utils.ChatMsgHandler
import com.scheartmed.im.utils.FilePickerAndSender
import com.scheartmed.im.utils.FileUtils
import com.scheartmed.im.utils.GlideEngine
import com.scheartmed.im.utils.ImageFileCompressEngine
import com.scheartmed.im.utils.MediaManager
import com.scheartmed.im.viewmodels.ChatGroupViewModel
import com.scheartmed.im.viewmodels.ChatViewModel
import com.scheartmed.im.views.adapter.ChatAdapter
import com.scheartmed.im.views.fragment.ChatFragment
import com.scheartmed.im.views.fragment.ChatP2PFragment
import com.scheartmed.im.widget.ChatContextMenu
import com.scheartmed.im.widget.ChatUiHelper
import com.scheartmed.im.widget.RelativePopupWindow
import com.scheartmed.im.widget.morelayout.MoreLayoutItemBean
import java.io.File

/**
 * @FileName: ChatGroupActivity.java
 * @author: villa_mou
 * @date: 08-13:45
 * @version V1.0 <描述当前版本功能>
 * @desc
 */
class ChatGroupActivity : BaseActivity<ChatGroupViewModel>() {
    override val binding: ActivityChatGroupBinding by lazy {
        ActivityChatGroupBinding.inflate(layoutInflater)
    }
    override fun providerVMClass(): Class<ChatGroupViewModel> = ChatGroupViewModel::class.java

    override fun initView() {
        initTitleBar()

    }

    override fun initData() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragContainer, ChatFragment.newInstance("47810892234"))
            .commit()
    }

    private fun initTitleBar() {
        val chatId = intent.extras?.getString("account")
        binding.titleBar.setTitle(chatId ?: "")
    }

}