package com.scheartmed.lib_im.views

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fortunes.commonsdk.base.BaseActivity
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.config.SelectMimeType
import com.luck.picture.lib.engine.CompressFileEngine
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnResultCallbackListener
import com.netease.nimlib.sdk.NIMClient
import com.netease.nimlib.sdk.v2.auth.V2NIMLoginService
import com.netease.nimlib.sdk.v2.conversation.enums.V2NIMConversationType
import com.netease.nimlib.sdk.v2.message.V2NIMMessage
import com.netease.nimlib.sdk.v2.message.V2NIMMessageService
import com.netease.nimlib.sdk.v2.message.attachment.V2NIMMessageImageAttachment
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
import com.scheartmed.lib_im.photoviewerlibrary.PhotoViewer
import com.scheartmed.lib_im.utils.ChatMsgHandler
import com.scheartmed.lib_im.utils.ChatMsgHandler.TEN_MINUTE
import com.scheartmed.lib_im.utils.FilePickerAndSender
import com.scheartmed.lib_im.utils.ImageFileCompressEngine
import com.scheartmed.lib_im.viewmodels.ChatP2PViewModel
import com.scheartmed.lib_im.views.adapter.ChatAdapter
import com.scheartmed.lib_im.widget.ChatUiHelper
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
    private var mAdapter: ChatAdapter? = null
    private var mChatUiHelper: ChatUiHelper? = null
    private lateinit var mChatHandler: ChatMsgHandler
    private lateinit var mChatSession: ChatSession
    private lateinit var mMsgList: ArrayList<MessageItem>
    private var isLoadingMessageList = false
    private var hasMore = true // 是否还有更多历史消息
    private val filePickerSender: FilePickerAndSender by lazy {
        FilePickerAndSender(this@ChatP2PActivity, this@ChatP2PActivity)
    }

    private val mHandler by lazy { Handler(Looper.getMainLooper()) }

    private var messageListener: CustomMessageListener = object : CustomMessageListener() {

        // 判断是否需要添加时间消息
        private fun shouldAddTimeMessage(imMessage: V2NIMMessage, lastMsg: MessageItem?): Boolean {
            return imMessage.conversationType == V2NIMConversationType.V2NIM_CONVERSATION_TYPE_P2P && imMessage.senderId == mChatSession.chatInfo?.accountId && (lastMsg == null || (lastMsg is MessageItem.SdkMessage && imMessage.createTime - lastMsg.message.createTime > TEN_MINUTE))
        }

        override fun onReceiveMessages(messages: MutableList<V2NIMMessage>) {
            val lastMsg = mMsgList.lastOrNull()
            if (shouldAddTimeMessage(messages[0], lastMsg)) {
                mMsgList.add(mChatHandler.createTimeMessage(messages[0]))
            }
            val newMessages = messages.filter {
                it.conversationType == V2NIMConversationType.V2NIM_CONVERSATION_TYPE_P2P && it.senderId == mChatSession.chatInfo?.accountId
            }
            newMessages.forEach {
                mMsgList.add(MessageItem.SdkMessage(it))
            }
            if (newMessages.isNotEmpty()) {
                mAdapter?.notifyDataSetChanged()
                binding.rvChatList.layoutManager?.scrollToPosition(mMsgList.size - 1)
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

        override fun onReceiveMessagesModified(messages: MutableList<V2NIMMessage>) {
        }


    }


    override fun initView() {
        initTitleBar()
        initRv()
        initListener()
        initChatUi()
    }

    override fun initData() {
        createChatSession()
        loadMessage()
    }

    private fun initTitleBar() {
        binding.titleBar.setTitle("test002")
    }


    private fun loadMessage() {
        val anchorMessage = when {
            mMsgList.isEmpty() -> null
            mMsgList[0] is MessageItem.TimeDivider -> (mMsgList.getOrNull(1) as? MessageItem.SdkMessage)?.message
            else -> (mMsgList[0] as? MessageItem.SdkMessage)?.message
        }
        isLoadingMessageList = true
        mChatHandler.loadMessage(anchorMessage, mChatSession.conversationId, {
            handleMsg(it)
            isLoadingMessageList = false
        }, {
            isLoadingMessageList = false
        })
    }

    private fun handleMsg(it: V2NIMMessageListResult) {
        val messages = it.messages
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

    private fun RecyclerView.doAfterNextLayout(action: () -> Unit) {
        viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                viewTreeObserver.removeOnPreDrawListener(this)
                action()
                return true
            }
        })
    }

    private fun createChatSession() {
        val chatId = "test002"
        val myAccountId = NIMClient.getService(V2NIMLoginService::class.java).loginUser
        Logger.e("myAccountId=a${myAccountId}")
        val userService = NIMClient.getService(V2NIMUserService::class.java)
        val myUsrInfo = userService.getUserInfo(myAccountId).data
        val chatUserInfo = userService.getUserInfo(chatId).data

        mChatSession = ChatSession()
        mChatSession.chatInfo = chatUserInfo
        mChatSession.myInfo = myUsrInfo
        mChatSession.conversationType = V2NIMConversationType.V2NIM_CONVERSATION_TYPE_P2P
        mChatSession.conversationId = V2NIMConversationIdUtil.p2pConversationId(chatId)
        mChatHandler = ChatMsgHandler(this, mChatSession)

    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initChatUi() {
        mChatUiHelper = ChatUiHelper.with(this)
        val data = arrayListOf<MoreLayoutItemBean>()
        data.add(MoreLayoutItemBean("im_photo", R.mipmap.edy_im_tupian))
        data.add(MoreLayoutItemBean("im_camera", R.mipmap.edy_im_xiazhenduan))
        data.add(MoreLayoutItemBean("im_video", R.mipmap.edy_im_shipinwenzhen))
        data.add(MoreLayoutItemBean("im_file", R.mipmap.edy_im_wenzhang))
        data.add(MoreLayoutItemBean("im_chufang", R.mipmap.edy_im_chufang))
        mChatUiHelper?.bindContentLayout(binding.llContent)
            ?.bindToSendButton(binding.chatInputContainer.btnSend)
            ?.bindEditText(binding.chatInputContainer.etContent)
            ?.bindBottomLayout(binding.bottomLayout)?.bindEmojiLayout(binding.layoutExpress)
            ?.bindAddLayout(binding.llAdd.rootAddPanel)
            ?.bindToAddButton(binding.chatInputContainer.ivAdd)
            ?.bindToEmojiButton(binding.chatInputContainer.ivEmo)
            ?.bindAudioBtn(binding.chatInputContainer.btnAudio)
            ?.bindAudioIv(binding.chatInputContainer.ivAudioIcon)?.bindMoreLayoutData(
                data
            ) { adapter, view, position ->
                val item = adapter.getItem(position) as MoreLayoutItemBean
                when (item.key) {
                    "im_photo" -> {
                        PictureSelector.create(this).openSystemGallery(SelectMimeType.ofImage())
                            .setCompressEngine(ImageFileCompressEngine())
                            .forSystemResult(object : OnResultCallbackListener<LocalMedia> {
                                override fun onResult(result: ArrayList<LocalMedia>) {
                                    if (result.size > 9) {
                                        return
                                    }
                                    if (result.isNotEmpty()) {
                                        result.forEach {
                                            Logger.e("compressPath=" + it.compressPath)
                                            Logger.e("path=" + it.path)
                                            val path = when {
                                                it.isCompressed -> it.compressPath
                                                else -> it.path
                                            }
                                            val newMessage = mChatHandler.createImageMessage(
                                                path, it.width, it.height
                                            )
                                            mChatHandler.sendMsg(
                                                newMessage,
                                                mChatSession.conversationId,
                                                null,
                                                null,
                                                null
                                            )
                                            sendMessageSuccess(newMessage)
                                        }

                                    }
                                }

                                override fun onCancel() {}
                            })
                    }

                    "im_camera" -> {
                        PictureSelector.create(this).openSystemGallery(SelectMimeType.ofVideo())
                            .forSystemResult(object : OnResultCallbackListener<LocalMedia?> {
                                override fun onResult(result: ArrayList<LocalMedia?>) {}
                                override fun onCancel() {}
                            })
                    }

                    "im_video" -> {
                        PictureSelector.create(this).openCamera(SelectMimeType.ofImage())
                            .forResult(object : OnResultCallbackListener<LocalMedia?> {
                                override fun onResult(result: ArrayList<LocalMedia?>) {}
                                override fun onCancel() {}
                            })
                    }

                    "im_file" -> {
                        filePickerSender.pickOfficeOrPdfFile { picked ->
                            // 可选：显示文件信息
                            Log.d("villavilla", "选中文件: ${picked.name}, size: ${picked.size}")
                            // 发送逻辑在 pickFileAndSend 内部已经调用
                        }
                    }

                    "im_chufang" -> {
                        // 检验检查
                    }
                }
            }
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
        //录音结束回调
        binding.chatInputContainer.btnAudio.setOnFinishedRecordListener { audioPath, time ->
            val file = File(audioPath)
//            if (file.exists()) {
//                mConversationId?.let { mPresenter.sendAudioMessage(audioPath, time, it) }
//            }
        }
    }

    private fun initListener() {
        //发送文本
        binding.chatInputContainer.btnSend.setOnClickListener {
            mChatUiHelper?.hideSoftInput()
            val newMessage =
                mChatHandler.createTextMessage(binding.chatInputContainer.etContent.text.toString())
            mChatHandler.sendMsg(newMessage, mChatSession.conversationId, null, null, null)
            sendMessageSuccess(newMessage)
            binding.chatInputContainer.etContent.setText("")
        }
        NIMClient.getService(V2NIMMessageService::class.java).addMessageListener(messageListener)
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
            if (item is MessageItem.SdkMessage && newMessage.createTime - item.message.createTime > TEN_MINUTE) {
                mMsgList.add(mChatHandler.createTimeMessage(newMessage))
            }
        }
        mMsgList.add(MessageItem.SdkMessage(newMessage))
        mAdapter?.notifyItemInserted(mMsgList.size);
        binding.rvChatList.layoutManager?.scrollToPosition(mMsgList.size - 1)
    }


    private fun initRv() {
        mMsgList = arrayListOf()
        mAdapter = ChatAdapter(this, mMsgList).apply {
            addChildClickViewIds(
                R.id.chat_item_header,
                R.id.chat_item_layout_content,
                R.id.chat_item_fail,
                R.id.item_phone_tip_call
            )
        }
        binding.rvChatList.adapter = mAdapter
//        binding.rvChatList.setItemViewCacheSize(30)
        mAdapter?.setOnItemChildClickListener { adapter, view, position ->
            val item = adapter.getItem(position) as MessageItem
            dealAdapterChildItemClick(view, item, position)
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
                Logger.e("加载更多历史消息")
                loadMessage()
            }

        })

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
                    retrySendMessageDialog {
                        when (item.message.messageType) {
                            V2NIMMessageType.V2NIM_MESSAGE_TYPE_TEXT -> {

                            }

                            V2NIMMessageType.V2NIM_MESSAGE_TYPE_IMAGE -> {

                            }

                            V2NIMMessageType.V2NIM_MESSAGE_TYPE_AUDIO -> {

                            }

                            V2NIMMessageType.V2NIM_MESSAGE_TYPE_FILE -> {

                            }

                            V2NIMMessageType.V2NIM_MESSAGE_TYPE_VIDEO -> {

                            }

                            else -> {

                            }
                        }
                    }
                }
                //内容
                R.id.chat_item_layout_content -> {
                    when (item.message.messageType) {
                        V2NIMMessageType.V2NIM_MESSAGE_TYPE_AUDIO -> {
//                        onPressAudio(item, view, position)
                        }

                        V2NIMMessageType.V2NIM_MESSAGE_TYPE_IMAGE -> {
                            onPressImage(view, item,position)
                        }

                        else -> {
//                        clickMessageItem(mConversationId, mConfigData, item)
                        }
                    }
                }

                R.id.item_phone_tip_call -> {


                }
            }
        }

    }

    private fun onPressImage(view: View, msg: MessageItem.SdkMessage,position:Int) {
        val pathList = ArrayList<String>()
        var position = 0
        mMsgList.forEach {
            if (it is MessageItem.SdkMessage) {
                if (it.message.messageType == V2NIMMessageType.V2NIM_MESSAGE_TYPE_IMAGE) {
                    val imageAttachment = it.message.attachment as? V2NIMMessageImageAttachment
                    val imageUrl =
                        imageAttachment?.path.takeIf { !it.isNullOrEmpty() } ?: imageAttachment?.url
                    imageUrl?.let { it1 -> pathList.add(it1) }
                }
                if (it.message.messageId == msg.message.messageId) {
                    position = pathList.size - 1
                }

            }
        }
        val intent = Intent(this, PhotoViewerActivity::class.java)
        intent.putStringArrayListExtra(PhotoViewerActivity.EXTRA_IMAGE_URLS, pathList)
        intent.putExtra(PhotoViewerActivity.EXTRA_POSITION, position)
        startActivity(intent)
    }


    override fun onDestroy() {
        super.onDestroy()
        NIMClient.getService(V2NIMMessageService::class.java).removeMessageListener(messageListener)
    }


}