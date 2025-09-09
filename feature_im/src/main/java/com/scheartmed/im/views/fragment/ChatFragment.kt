package com.scheartmed.im.views.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.ViewTreeObserver
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.core.basemvvm.BaseApplication
import com.core.commonsdk.base.BaseFragment
import com.core.commonsdk.utils.FileUtils
import com.core.commonsdk.utils.MyLogger
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.config.SelectMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnExternalPreviewEventListener
import com.luck.picture.lib.interfaces.OnResultCallbackListener
import com.luck.picture.lib.style.PictureSelectorStyle
import com.luck.picture.lib.style.TitleBarStyle
import com.netease.nimlib.sdk.NIMClient
import com.netease.nimlib.sdk.msg.MsgService
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum
import com.netease.nimlib.sdk.v2.auth.V2NIMLoginService
import com.netease.nimlib.sdk.v2.conversation.enums.V2NIMConversationType
import com.netease.nimlib.sdk.v2.message.V2NIMMessage
import com.netease.nimlib.sdk.v2.message.V2NIMMessageRevokeNotification
import com.netease.nimlib.sdk.v2.message.V2NIMMessageService
import com.netease.nimlib.sdk.v2.message.V2NIMTeamMessageReadReceipt
import com.netease.nimlib.sdk.v2.message.attachment.V2NIMMessageAudioAttachment
import com.netease.nimlib.sdk.v2.message.attachment.V2NIMMessageFileAttachment
import com.netease.nimlib.sdk.v2.message.attachment.V2NIMMessageImageAttachment
import com.netease.nimlib.sdk.v2.message.attachment.V2NIMMessageVideoAttachment
import com.netease.nimlib.sdk.v2.message.enums.V2NIMMessageType
import com.netease.nimlib.sdk.v2.message.params.V2NIMMessageRevokeParams
import com.netease.nimlib.sdk.v2.message.result.V2NIMMessageListResult
import com.netease.nimlib.sdk.v2.user.V2NIMUserService
import com.netease.nimlib.sdk.v2.utils.V2NIMConversationIdUtil
import com.orhanobut.logger.Logger
import com.scheartmed.im.R
import com.scheartmed.im.data.MessageItem
import com.scheartmed.im.data.model.ChatSession
import com.scheartmed.im.databinding.FragmentChatBinding
import com.scheartmed.im.event.BottomBarEvent
import com.scheartmed.im.ext.retrySendMessageDialog
import com.scheartmed.im.listener.CustomMessageListener
import com.scheartmed.im.utils.AudioPlayer
import com.scheartmed.im.utils.ChatMsgHandler
import com.scheartmed.im.utils.FilePickerAndSender
import com.scheartmed.im.utils.GlideEngine
import com.scheartmed.im.utils.ImageFileCompressEngine
import com.scheartmed.im.viewmodels.ChatViewModel
import com.scheartmed.im.views.activity.WebViewActivity
import com.scheartmed.im.views.adapter.ChatAdapter
import com.scheartmed.im.widget.ChatContextMenu
import com.scheartmed.im.widget.ChatContextMenu.OnTextClickListener
import com.scheartmed.im.widget.ChatUiHelper
import com.scheartmed.im.widget.RelativePopupWindow
import com.scheartmed.im.widget.morelayout.MoreLayoutItemBean
import org.greenrobot.eventbus.EventBus
import java.io.File


/**
 * @author: villa_mou
 * @date: 09-09:07
 * @version V1.0 <描述当前版本功能>
 * @desc
 */
class ChatFragment : BaseFragment<ChatViewModel>() {

    companion object {
        fun newInstance(teamId: String): ChatFragment {
            val fragment = ChatFragment()
            val args = Bundle()
            args.putString("teamId", teamId)
            fragment.setArguments(args)
            return fragment
        }
    }

    override fun providerVMClass(): Class<ChatViewModel> = ChatViewModel::class.java
    private var mAdapter: ChatAdapter? = null
    private var mChatUiHelper: ChatUiHelper? = null
    private lateinit var mChatHandler: ChatMsgHandler
    private lateinit var mChatSession: ChatSession
    private lateinit var mMsgList: ArrayList<MessageItem>
    private var isLoadingMessageList = false
    private var hasMore = true // 是否还有更多历史消息
    private lateinit var filePickerSender: FilePickerAndSender
    private val mHandler by lazy { Handler(Looper.getMainLooper()) }
    override val binding: FragmentChatBinding by lazy {
        FragmentChatBinding.inflate(layoutInflater)
    }

    private var messageListener: CustomMessageListener = object : CustomMessageListener() {
        // 判断是否需要添加时间消息
        private fun shouldAddTimeMessage(imMessage: V2NIMMessage, lastMsg: MessageItem?): Boolean {
            return imMessage.conversationType == V2NIMConversationType.V2NIM_CONVERSATION_TYPE_TEAM && V2NIMConversationIdUtil.conversationTargetId(
                imMessage.conversationId
            ) == V2NIMConversationIdUtil.conversationTargetId(mChatSession.conversationId) && (lastMsg == null || (lastMsg is MessageItem.SdkMessage && imMessage.createTime - lastMsg.message.createTime > ChatMsgHandler.TEN_MINUTE))
        }

        override fun onReceiveMessages(messages: MutableList<V2NIMMessage>) {
            Logger.e("onReceiveMessages==" + messages.size)
            val lastMsg = mMsgList.lastOrNull()
            if (shouldAddTimeMessage(messages[0], lastMsg)) {
                mMsgList.add(mChatHandler.createTimeMessage(messages[0]))
            }
            val newMessages = messages.filter {
                it.conversationType == V2NIMConversationType.V2NIM_CONVERSATION_TYPE_TEAM && V2NIMConversationIdUtil.conversationTargetId(
                    it.conversationId
                ) == V2NIMConversationIdUtil.conversationTargetId(mChatSession.conversationId)
            }
            newMessages.forEach {
                mMsgList.add(MessageItem.SdkMessage(it))
            }
            if (newMessages.isNotEmpty()) {
                mAdapter?.notifyDataSetChanged()
                binding.rvChatList.layoutManager?.scrollToPosition(mMsgList.size - 1)
            }

            val notMyMessages = messages.filter {
                Logger.e("senderId==" + it.senderId + ",isSelf=" + it.isSelf)
                filterReceiptMessages(it)
            }
            NIMClient.getService(V2NIMMessageService::class.java)
                .sendTeamMessageReceipts(notMyMessages, {
                    Logger.e("发送已读回执成功1")
                }) {
                    Logger.e("发送已读回执失败1" + it.desc)
                }
        }

        override fun onReceiveTeamMessageReadReceipts(readReceipts: MutableList<V2NIMTeamMessageReadReceipt>) {
            super.onReceiveTeamMessageReadReceipts(readReceipts)
            if (readReceipts.isNotEmpty()) {
                //TODO:
                mAdapter?.notifyDataSetChanged()
            }
        }


        /**
         * 本端发送消息状态回调 来源： 发送消息， 插入消息
         */
        override fun onSendMessage(message: V2NIMMessage) {
            Logger.e("监听-" + message.sendingState)
            mHandler.post {
                val index = mMsgList.indexOfFirst {
                    it is MessageItem.SdkMessage && it.message.messageId == message.messageId
                }
                if (index >= 0) {
                    mMsgList[index] = MessageItem.SdkMessage(message)
                    mAdapter?.notifyItemChanged(index)
                }
            }
        }

        override fun onMessageRevokeNotifications(revokeNotifications: MutableList<V2NIMMessageRevokeNotification>) {
            super.onMessageRevokeNotifications(revokeNotifications)
            if (revokeNotifications.isNotEmpty()) {
                revokeNotifications.forEach { notification ->
                    val revokedMessageId = notification.messageRefer.messageServerId
                    val senderId = notification.messageRefer.senderId
                    val myAccountId = mChatSession.myInfo?.accountId

                    MyLogger.getLogger().e("onMessageRevokeNotifications-$revokedMessageId")

                    mMsgList.indexOfFirst {
                        it is MessageItem.SdkMessage && it.message.messageServerId == revokedMessageId && senderId != myAccountId
                    }.takeIf { it >= 0 }?.let { index ->
                        val tipsMessage =
                            mChatHandler.createTipsMessage("对方撤回了一条消息")
                        NIMClient.getService(V2NIMMessageService::class.java)
                            .insertMessageToLocal(
                                tipsMessage,
                                mChatSession.conversationId,
                                notification.revokeAccountId,
                                notification.messageRefer.createTime,
                                {
                                    mMsgList[index] = MessageItem.SdkMessage(tipsMessage)
                                    mAdapter?.notifyItemChanged(index)
                                },
                                { error ->
                                    MyLogger.getLogger()
                                        .e("Failed to insert tips message: ${error.desc}")
                                }
                            )
                    }
                }
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //解决崩溃
        filePickerSender = context?.let { FilePickerAndSender(this@ChatFragment, it) }!!
    }

    override fun initView() {
        initRecyclerView()
        initListener()
        initChatUi()
    }

    override fun initData() {
        createChatSession()
        loadMessage()
    }

    override fun onResume() {
        super.onResume()
        setChattingAccount()
    }

    /**
     *  进入聊天界面，建议放在 onResume 中，表示来自 account 的消息无需进行消息提醒。
     */
    private fun setChattingAccount() {
        val teamId = getTeamId()
        NIMClient.getService(MsgService::class.java)
            .setChattingAccount(teamId, SessionTypeEnum.Team);
    }


    private fun createChatSession() {
        val teamId = getTeamId()
        val myAccountId = NIMClient.getService(V2NIMLoginService::class.java).loginUser
        Logger.e("login myAccountId==" + myAccountId)
        val userService = NIMClient.getService(V2NIMUserService::class.java)
        val myUsrInfo = userService.getUserInfo(myAccountId).data
        mChatSession = ChatSession()
        mChatSession.myInfo = myUsrInfo
        mChatSession.conversationType = V2NIMConversationType.V2NIM_CONVERSATION_TYPE_TEAM
        mChatSession.conversationId = V2NIMConversationIdUtil.teamConversationId(teamId)
        mChatHandler = ChatMsgHandler(context)

    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initChatUi() {
        mChatUiHelper = ChatUiHelper.with(activity)
        val data = arrayListOf<MoreLayoutItemBean>()
        data.add(MoreLayoutItemBean("im_photo", R.mipmap.edy_im_tupian))
        data.add(MoreLayoutItemBean("im_camera", R.mipmap.edy_im_xiazhenduan))
        data.add(MoreLayoutItemBean("im_video", R.mipmap.edy_im_shipinwenzhen))
        data.add(MoreLayoutItemBean("im_file", R.mipmap.edy_im_wenzhang))
        mChatUiHelper?.bindContentLayout(binding.llContent)
            ?.bindToSendButton(binding.chatInputContainer.btnSend)
            ?.bindEditText(binding.chatInputContainer.etContent)
            ?.bindBottomLayout(binding.bottomLayout)?.bindEmojiLayout(binding.layoutExpress)
            ?.bindAddLayout(binding.llAdd.rootAddPanel)
            ?.bindToAddButton(binding.chatInputContainer.ivAdd)
            ?.bindToEmojiButton(binding.chatInputContainer.ivEmo)
            ?.bindAudioBtn(binding.chatInputContainer.btnAudio)
            ?.bindAudioIv(binding.chatInputContainer.ivAudioIcon)
            ?.bindMoreLayoutData(
                data
            ) { adapter, view, position ->
                val item = adapter.getItem(position) as MoreLayoutItemBean
                when (item.key) {
                    "im_photo" -> {
                        onPressSelectPhoto()
                    }

                    "im_video" -> {
                        onPressSelectVideo()
                    }

                    "im_camera" -> {
                        onPressTakePhoto()
                    }

                    "im_file" -> {
                        onPressSelectFile()
                    }
                }
            }

        mChatUiHelper?.attachKeyboardListener()

        //底部布局弹出,聊天列表上滑到最后一位
        binding.rvChatList.addOnLayoutChangeListener(View.OnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
            if (bottom < oldBottom) {
                binding.rvChatList.post(Runnable {
                    mAdapter?.let {
                        if (it.itemCount > 0) {
                            binding.rvChatList.scrollToPosition(it.itemCount - 1)
                        }
                    }
                })
            }
        })
        //点击空白区域关闭键盘
        binding.rvChatList.setOnTouchListener(View.OnTouchListener { _, _ ->
            mChatUiHelper?.hideBottomLayout(false)
            mChatUiHelper?.hideSoftInput()
            binding.chatInputContainer.etContent.clearFocus()
            false
        })
        //录音结束回调,发送录音
        binding.chatInputContainer.btnAudio.setOnFinishedRecordListener { audioPath, time ->
            val file = File(audioPath)
            if (file.exists()) {
                val newMessage = mChatHandler.createAudioMessage(audioPath, time)
                sendMessage(newMessage)
            }
        }
        binding.chatInputContainer.etContent.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                EventBus.getDefault().post(BottomBarEvent(false))
            } else {
                Logger.e("isShowBottomLayout==" + mChatUiHelper?.isShowBottomLayout)
                binding.chatInputContainer.etContent.postDelayed({
                    if (binding.bottomLayout.visibility == View.VISIBLE) {
                        return@postDelayed
                    }
                    EventBus.getDefault().post(BottomBarEvent(true))
                }, 100)
            }

        }
    }

    private fun initListener() {
        binding.chatInputContainer.btnSend.setOnClickListener {
            mChatUiHelper?.hideSoftInput()
            val newMessage =
                mChatHandler.createTextMessage(binding.chatInputContainer.etContent.text.toString())
            sendMessage(newMessage)
            binding.chatInputContainer.etContent.setText("")
        }
        NIMClient.getService(V2NIMMessageService::class.java).addMessageListener(messageListener)
    }

    fun onNewIntent(intent: Intent) {
        //TODO:处理新消息

//        anchorMessage =
//            intent.getSerializableExtra(RouterConstant.KEY_MESSAGE_INFO) as IMMessageInfo?
//        if (anchorMessage == null) {
//            val message = intent.getSerializableExtra(RouterConstant.KEY_MESSAGE) as V2NIMMessage?
//            if (message != null) {
//                anchorMessage = IMMessageInfo(message)
//            }
//        }
//        loadAnchorMessage()
    }

    private fun getTeamId(): String {
        val arguments = arguments
        var teamId = ""
        arguments?.let {
            teamId = it.getString("teamId") ?: ""
        }
        return teamId
    }


    private fun loadMessage() {
        val anchorMessage = when {
            mMsgList.isEmpty() -> null
            mMsgList[0] is MessageItem.TimeDivider -> (mMsgList.getOrNull(1) as? MessageItem.SdkMessage)?.message
            else -> (mMsgList[0] as? MessageItem.SdkMessage)?.message
        }
        isLoadingMessageList = true
        Logger.e(
            "loadMessage anchorMessage==" + (anchorMessage?.messageId
                ?: "null") + ",mChatSession.conversationId=" + mChatSession.conversationId
        )
        mChatHandler.loadMessage(anchorMessage, mChatSession.conversationId, {
            handleMsg(it)
            isLoadingMessageList = false
        }, {
            Logger.e("loadMessage error==" + it.desc)
            isLoadingMessageList = false
        })
    }

    private fun handleMsg(it: V2NIMMessageListResult) {
        val messages = it.messages
        val notMyMessages = messages.filter {
            filterReceiptMessages(it)
        }
        Logger.e("notMyMessages ==" + notMyMessages.size)
        //已读回执
        NIMClient.getService(V2NIMMessageService::class.java)
            .sendTeamMessageReceipts(notMyMessages, {
                MyLogger.getLogger().e("发送已读回执成功")
            }) {
                MyLogger.getLogger().e("发送已读回执失败" + it.desc)
            }
        messages.reverse()
        val anchorMessage = it.anchorMessage
        if (messages.isEmpty() || messages.size < ChatMsgHandler.ONE_QUERY_LIMIT) {
            hasMore = false
        }
        var scroll = false
        // 如果原本没有，为第一次加载，需要在加载完成后移动到最后一项
        if (mMsgList.isEmpty()) {
            scroll = true
        }
        if (messages.isNotEmpty()) {
            val dealLoadMessage = mChatHandler.dealLoadMessage(messages, anchorMessage)
            mMsgList.addAll(0, dealLoadMessage)
            mAdapter?.notifyItemRangeInserted(0, dealLoadMessage.size)
            if (scroll) {
                binding.rvChatList.layoutManager?.scrollToPosition(mMsgList.size - 1)
            } else {
                val lm = binding.rvChatList.layoutManager as LinearLayoutManager
                val anchorPos = lm.findFirstVisibleItemPosition()
                val anchorView = lm.findViewByPosition(anchorPos)
                val anchorTop = anchorView?.let { lm.getDecoratedTop(it) } ?: 0
                // 3) 等布局完成后再恢复
                binding.rvChatList.doAfterNextLayout {
                    lm.scrollToPositionWithOffset(anchorPos + dealLoadMessage.size, anchorTop)
                }
            }
        }

    }

    private fun filterReceiptMessages(it: V2NIMMessage) =
        (!it.isSelf && it.messageType != V2NIMMessageType.V2NIM_MESSAGE_TYPE_NOTIFICATION && it.messageType != V2NIMMessageType.V2NIM_MESSAGE_TYPE_TIPS)

    private fun RecyclerView.doAfterNextLayout(action: () -> Unit) {
        viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                viewTreeObserver.removeOnPreDrawListener(this)
                action()
                return true
            }
        })
    }


    private fun onPressSelectFile() {
        filePickerSender.pickOfficeOrPdfFile { picked ->
            val newMessage = mChatHandler.createFileMessage(
                picked.file?.absolutePath,
            )
            sendMessage(newMessage)
        }
    }

    private fun onPressTakePhoto() {
        PictureSelector.create(this).openCamera(SelectMimeType.ofImage())
            .setCompressEngine(ImageFileCompressEngine())
            .forResult(object : OnResultCallbackListener<LocalMedia?> {
                override fun onResult(result: ArrayList<LocalMedia?>) {
                    if (result.isNotEmpty()) {
                        val localMedia = result[0]
                        val path = when {
                            localMedia?.isCompressed == true -> localMedia.compressPath
                            else -> localMedia?.path ?: ""
                        }
                        val newMessage = mChatHandler.createImageMessage(
                            path, localMedia?.width ?: 0, localMedia?.height ?: 0
                        )
                        sendMessage(newMessage)
                    }

                }

                override fun onCancel() {}
            })
    }

    private fun onPressSelectVideo() {
        //TODO：发送完需要删除缓存视频
        PictureSelector.create(this).openGallery(SelectMimeType.ofVideo()).setMaxSelectNum(1)
            .setImageEngine(GlideEngine.createGlideEngine())
            .forResult(object : OnResultCallbackListener<LocalMedia?> {
                override fun onResult(result: ArrayList<LocalMedia?>) {
                    if (result.isNotEmpty()) {
                        result.forEach {
                            val videoFile = FileUtils.copyVideoToCache(
                                BaseApplication.instance(), Uri.parse(it?.path)
                            )
                            videoFile?.let { it1 ->
                                val newMessage = mChatHandler.createVideoMessage(
                                    videoFile.absolutePath,
                                    it?.duration?.toInt() ?: 0,
                                )
                                sendMessage(newMessage)
                            }

                        }
                    }
                }

                override fun onCancel() {}
            })
    }

    private fun onPressSelectPhoto() {
        PictureSelector.create(this).openGallery(SelectMimeType.ofImage())
            .setCompressEngine(ImageFileCompressEngine())
            .setImageEngine(GlideEngine.createGlideEngine()).setMaxSelectNum(9)
            .forResult(object : OnResultCallbackListener<LocalMedia?> {
                override fun onResult(result: ArrayList<LocalMedia?>) {
                    if (result.isNotEmpty()) {
                        result.forEach {
                            val path = when {
                                it?.isCompressed == true -> it.compressPath
                                else -> it?.path ?: ""
                            }
                            val newMessage = mChatHandler.createImageMessage(
                                path, it?.width ?: 0, it?.height ?: 0
                            )
                            sendMessage(newMessage)
                        }

                    }
                }

                override fun onCancel() {}
            })
    }

    private fun sendMessage(newMessage: V2NIMMessage, isRetry: Boolean = false) {
        mChatHandler.sendMsg(newMessage, mChatSession.conversationId, {

        }, {

        }, {

        })
        if (!isRetry) {
            sendMessageSuccess(newMessage)
        }
    }


    /**
     * 发送消息成功
     */
    private fun sendMessageSuccess(newMessage: V2NIMMessage) {
        if (mMsgList.isEmpty()) {
            mMsgList.add(mChatHandler.createTimeMessage(newMessage))
        }
        if (mMsgList.isNotEmpty()) {
            val item = mMsgList[mMsgList.size - 1]
            if (item is MessageItem.SdkMessage && newMessage.createTime - item.message.createTime > ChatMsgHandler.TEN_MINUTE) {
                mMsgList.add(mChatHandler.createTimeMessage(newMessage))
            }
        }
        mMsgList.add(MessageItem.SdkMessage(newMessage))
        mAdapter?.notifyItemInserted(mMsgList.size);
        binding.rvChatList.layoutManager?.scrollToPosition(mMsgList.size - 1)
    }


    private fun initRecyclerView() {
        mMsgList = arrayListOf()
        mAdapter = context?.let {
            ChatAdapter(it, mMsgList, V2NIMConversationType.V2NIM_CONVERSATION_TYPE_TEAM).apply {
                addChildClickViewIds(
                    R.id.chat_item_header,
                    R.id.chat_item_layout_content,
                    R.id.chat_item_fail,
                )
                addChildLongClickViewIds(R.id.chat_item_layout_content)
            }
        }
        binding.rvChatList.adapter = mAdapter
        mAdapter?.setOnItemChildClickListener { adapter, view, position ->
            val item = adapter.getItem(position) as MessageItem
            dealAdapterChildItemClick(view, item, position)
        }
        mAdapter?.setOnItemChildLongClickListener { adapter, view, position ->
            val item = adapter.getItem(position) as MessageItem
            if (item is MessageItem.SdkMessage) {
                dealAdapterChildItemLoongClick(view, item, position)
            }
            false
        }

        //🌟解决加载数据没法滚动到底部的问题
        binding.rvChatList.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                val canScroll =
                    binding.rvChatList.computeVerticalScrollRange() > binding.rvChatList.computeVerticalScrollExtent()
                val layoutManager = binding.rvChatList.layoutManager as LinearLayoutManager
                if (canScroll && !layoutManager.stackFromEnd) {
                    with(layoutManager) { stackFromEnd = true }
                    return
                }
                if (!canScroll && layoutManager.stackFromEnd) {
                    layoutManager.stackFromEnd = false
                }
            }
        })

        // 下拉到顶部，加载更多
        binding.rvChatList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = binding.rvChatList.layoutManager as LinearLayoutManager
                val firstVisible = layoutManager.findFirstVisibleItemPosition()
                if (!hasMore || isLoadingMessageList || firstVisible != 0) {
                    return
                }
                loadMessage()
            }

        })

    }

    private fun dealAdapterChildItemLoongClick(
        view: View, item: MessageItem.SdkMessage, position: Int
    ) {
        if (!isNormalMessage(item.message)) {
            return
        }
        val chatContextMenu = ChatContextMenu(view.context)
        chatContextMenu.showOnAnchor(
            view,
            RelativePopupWindow.VerticalPosition.ABOVE,
            RelativePopupWindow.HorizontalPosition.CENTER
        )
        chatContextMenu.setListener(object : OnTextClickListener {
            override fun onCopy() {

            }

            override fun onRevoke() {
                onRevokeMessage(item, position)
            }

        })
    }

    private fun onRevokeMessage(item: MessageItem.SdkMessage, position: Int) {
        val v2MessageService = NIMClient.getService(V2NIMMessageService::class.java)
        val revokeMessage = item.message
        val revokeParams = V2NIMMessageRevokeParams.V2NIMMessageRevokeParamsBuilder.builder()
//            .withEnv("路由抄送地址")
            .withExtension("扩展信息").withPushContent("推送文案")
//            .withPushPayload("推送数据")
            .build()
        v2MessageService.revokeMessage(revokeMessage, revokeParams, {
            MyLogger.getLogger().e("撤回成功")
            val v2NIMMessage = mChatHandler.createTipsMessage("你撤回了一条消息")
            NIMClient.getService(
                V2NIMMessageService::class.java
            ).insertMessageToLocal(v2NIMMessage,
                mChatSession.conversationId,
                item.message.senderId,
                item.message.createTime,
                {
                    mMsgList[position] = MessageItem.SdkMessage(v2NIMMessage)
                    mAdapter?.notifyItemChanged(position)
                },
                {

                })
        }, {
            MyLogger.getLogger().e("撤回失败" + it.desc + ",code=" + it.code)
        });

    }

    /**
     * 是否是用户自己发送的消息类型
     */
    private fun isNormalMessage(message: V2NIMMessage): Boolean {
        return message.messageType == V2NIMMessageType.V2NIM_MESSAGE_TYPE_AUDIO || message.messageType == V2NIMMessageType.V2NIM_MESSAGE_TYPE_TEXT || message.messageType == V2NIMMessageType.V2NIM_MESSAGE_TYPE_IMAGE || message.messageType == V2NIMMessageType.V2NIM_MESSAGE_TYPE_VIDEO || message.messageType == V2NIMMessageType.V2NIM_MESSAGE_TYPE_FILE
    }

    /**
     * 处理点击item事件
     */
    private fun dealAdapterChildItemClick(
        view: View, item: MessageItem, position: Int
    ) {
        if (item is MessageItem.SdkMessage) {
            when (view.id) {
                //头像
                R.id.chat_item_header -> {
                    //TODO:
                }
                //失败按钮
                R.id.chat_item_fail -> {
                    val appCompatActivity = activity as AppCompatActivity
                    //TODO：
                    appCompatActivity.retrySendMessageDialog {
                        sendMessage(item.message, true)
                    }
                }
                //内容
                R.id.chat_item_layout_content -> {
                    when (item.message.messageType) {
                        V2NIMMessageType.V2NIM_MESSAGE_TYPE_IMAGE -> {
                            onPressShowImage(view, item, position)
                        }

                        V2NIMMessageType.V2NIM_MESSAGE_TYPE_AUDIO -> {
                            onPressShowAudio(item, view, position)
                        }

                        V2NIMMessageType.V2NIM_MESSAGE_TYPE_VIDEO -> {
                            onPressShowVideo(item)
                        }

                        V2NIMMessageType.V2NIM_MESSAGE_TYPE_FILE -> {
                            onPressShowFile(item)
                        }

                        else -> {
                        }
                    }
                }
            }
        }

    }

    private fun onPressShowFile(item: MessageItem.SdkMessage) {
        //TODO:
        val v2NIMMessageFileAttachment = item.message.attachment as V2NIMMessageFileAttachment
        Logger.e("file url " + v2NIMMessageFileAttachment.url)
        val intent = Intent(context, WebViewActivity::class.java)
        intent.putExtra("url", v2NIMMessageFileAttachment.url)
        startActivity(intent)
    }

    private fun onPressShowVideo(item: MessageItem.SdkMessage) {
        var position = 0
        val videoList = ArrayList<LocalMedia>()
        mMsgList.filterIsInstance<MessageItem.SdkMessage>()
            .filter { it.message.messageType == V2NIMMessageType.V2NIM_MESSAGE_TYPE_VIDEO }.map {
                val videoAttachment = it.message.attachment as? V2NIMMessageVideoAttachment
                val videoUrl =
                    videoAttachment?.path.takeIf { !it.isNullOrEmpty() } ?: videoAttachment?.url
                videoUrl?.let { url ->
                    LocalMedia().apply {
                        this.path = url
                        this.mimeType = PictureMimeType.MIME_TYPE_VIDEO
                        videoList.add(this)
                    }
                }

                if (item.message.messageId == it.message.messageId) {
                    position = videoList.size - 1
                }
            }
        showVideoOrImage(position, videoList)
    }


    /**
     * 点击音频，播放
     */
    private fun onPressShowAudio(msg: MessageItem.SdkMessage, view: View, position: Int) {
        AudioPlayer.release()
        val ivAudioPlay = view.findViewById<ImageView>(R.id.ivAudio)
        val audioUrl = (msg.message.attachment as V2NIMMessageAudioAttachment).url
        AudioPlayer.play(audioUrl, onStart = {
            ivAudioPlay?.let {
                val drawableRes =
                    if (msg.message.isSelf) R.drawable.voice_white else R.drawable.voice_black
                context?.let { it1 ->
                    Glide.with(it1).asGif().load(drawableRes).into(it)
                };
            }
        }, onComplete = {
            context?.let {
                Glide.with(it)
                    .load(if (msg.message.isSelf) R.mipmap.ic_audio_animation_right else R.mipmap.ic_audio_animation_left)
                    .into(ivAudioPlay!!)
            }
        }, onError = {
            context?.let {
                Glide.with(it)
                    .load(if (msg.message.isSelf) R.mipmap.ic_audio_animation_right else R.mipmap.ic_audio_animation_left)
                    .into(ivAudioPlay!!)
            }
        })

    }

    private fun onPressShowImage(view: View, msg: MessageItem.SdkMessage, position: Int) {
//        val pathList = ArrayList<String>()
        val photoList = ArrayList<LocalMedia>()
        var position = 0
        mMsgList.forEach {
            if (it is MessageItem.SdkMessage) {
                if (it.message.messageType == V2NIMMessageType.V2NIM_MESSAGE_TYPE_IMAGE) {
                    val imageAttachment = it.message.attachment as? V2NIMMessageImageAttachment
                    val imageUrl =
                        imageAttachment?.path.takeIf { !it.isNullOrEmpty() } ?: imageAttachment?.url
                    LocalMedia().apply {
                        this.path = imageUrl
                        this.mimeType = PictureMimeType.MIME_TYPE_IMAGE
                        photoList.add(this)
                    }
                }
                if (it.message.messageId == msg.message.messageId) {
                    position = photoList.size - 1
                }

            }
        }
//        val intent = Intent(context, PhotoViewerActivity::class.java).apply {
//            putStringArrayListExtra(PhotoViewerActivity.EXTRA_IMAGE_URLS, ArrayList(pathList))
//            putExtra(PhotoViewerActivity.EXTRA_POSITION, position)
//        }
//        startActivity(intent)

        showVideoOrImage(position, photoList)
    }

    private fun showVideoOrImage(
        position: Int, photoList: ArrayList<LocalMedia>
    ) {
        val style = PictureSelectorStyle().apply {
            titleBarStyle = TitleBarStyle().apply {
                isHideTitleBar = true
            }
        }

        PictureSelector.create(this).openPreview().setImageEngine(GlideEngine.createGlideEngine())
            .isVideoPauseResumePlay(true).setSelectorUIStyle(style)
            .setExternalPreviewEventListener(object : OnExternalPreviewEventListener {
                override fun onPreviewDelete(position: Int) {}
                override fun onLongPressDownload(context: Context?, media: LocalMedia?): Boolean {
                    return false
                }
            }).startActivityPreview(position, false, photoList)
    }


    override fun onDestroy() {
        super.onDestroy()
        NIMClient.getService(V2NIMMessageService::class.java).removeMessageListener(messageListener)
    }
}