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
import com.netease.nimlib.sdk.v2.message.attachment.V2NIMMessageAudioAttachment
import com.netease.nimlib.sdk.v2.message.attachment.V2NIMMessageFileAttachment
import com.netease.nimlib.sdk.v2.message.attachment.V2NIMMessageImageAttachment
import com.netease.nimlib.sdk.v2.message.attachment.V2NIMMessageVideoAttachment
import com.netease.nimlib.sdk.v2.message.enums.V2NIMMessageType
import com.netease.nimlib.sdk.v2.message.result.V2NIMMessageListResult
import com.netease.nimlib.sdk.v2.user.V2NIMUserService
import com.orhanobut.logger.Logger
import com.scheartmed.lib_im.R
import com.scheartmed.lib_im.data.MessageItem
import com.scheartmed.lib_im.data.model.ChatSession
import com.scheartmed.lib_im.databinding.ActivityChatGroupBinding
import com.scheartmed.lib_im.ext.retrySendMessageDialog
import com.scheartmed.lib_im.listener.CustomMessageListener
import com.scheartmed.lib_im.utils.ChatMsgHandler
import com.scheartmed.lib_im.utils.FilePickerAndSender
import com.scheartmed.lib_im.utils.FileUtils
import com.scheartmed.lib_im.utils.GlideEngine
import com.scheartmed.lib_im.utils.ImageFileCompressEngine
import com.scheartmed.lib_im.utils.MediaManager
import com.scheartmed.lib_im.viewmodels.ChatGroupViewModel
import com.scheartmed.lib_im.views.adapter.ChatAdapter
import com.scheartmed.lib_im.widget.ChatContextMenu
import com.scheartmed.lib_im.widget.ChatUiHelper
import com.scheartmed.lib_im.widget.RelativePopupWindow
import com.scheartmed.lib_im.widget.morelayout.MoreLayoutItemBean
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

    override fun providerVMClass(): Class<ChatGroupViewModel>? {
        return ChatGroupViewModel::class.java
    }

    private var mAdapter: ChatAdapter? = null
    private var mChatUiHelper: ChatUiHelper? = null
    private lateinit var mChatHandler: ChatMsgHandler
    private lateinit var mChatSession: ChatSession
    private lateinit var mMsgList: ArrayList<MessageItem>
    private var isLoadingMessageList = false
    private var hasMore = true // 是否还有更多历史消息
    private var mNewGifDrawable: GifDrawable? = null
    private var mIvItemAudio: ImageView? = null
    private lateinit var filePickerSender: FilePickerAndSender
    private val mHandler by lazy { Handler(Looper.getMainLooper()) }

    private var messageListener: CustomMessageListener = object : CustomMessageListener() {

        // 判断是否需要添加时间消息
        private fun shouldAddTimeMessage(imMessage: V2NIMMessage, lastMsg: MessageItem?): Boolean {
            return imMessage.conversationType == V2NIMConversationType.V2NIM_CONVERSATION_TYPE_TEAM && (lastMsg == null || (lastMsg is MessageItem.SdkMessage && imMessage.createTime - lastMsg.message.createTime > ChatMsgHandler.TEN_MINUTE))
        }

        override fun onReceiveMessages(messages: MutableList<V2NIMMessage>) {
            val lastMsg = mMsgList.lastOrNull()
            if (shouldAddTimeMessage(messages[0], lastMsg)) {
                mMsgList.add(mChatHandler.createTimeMessage(messages[0]))
            }
            val newMessages = messages.filter {
                it.conversationType == V2NIMConversationType.V2NIM_CONVERSATION_TYPE_TEAM
            }
            newMessages.forEach {
                mMsgList.add(MessageItem.SdkMessage(it))
            }
            if (newMessages.isNotEmpty()) {
                mAdapter?.notifyDataSetChanged()
                binding.rvChatList.layoutManager?.scrollToPosition(mMsgList.size - 1)
            }
//            messages.forEach {
//                if (!it.isSelf) {
//                    NIMClient.getService(V2NIMMessageService::class.java)
//                        .sendP2PMessageReceipt(it, { }) { }
//                }
//            }
        }

        /**
         * 本端发送消息状态回调 来源： 发送消息， 插入消息
         */
        override fun onSendMessage(message: V2NIMMessage) {
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


    }

    override fun initView() {
        initTitleBar()
        initRecyclerView()
        initListener()
        initChatUi()
    }

    override fun initData() {
        createChatSession()
        loadMessage()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initChatUi() {
        filePickerSender = FilePickerAndSender(this@ChatGroupActivity, this@ChatGroupActivity)
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

                    "im_video" -> {
                        PictureSelector.create(this).openGallery(SelectMimeType.ofVideo())
                            .setMaxSelectNum(1).setImageEngine(GlideEngine.createGlideEngine())
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

                        //TODO：发送完需要删除缓存视频

                    }

                    "im_camera" -> {
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


                    "im_file" -> {
                        filePickerSender.pickOfficeOrPdfFile { picked ->
                            Log.d(
                                "villa",
                                "选中文件: ${picked.file?.absolutePath}, size: ${picked.size}"
                            )
                            val newMessage = mChatHandler.createFileMessage(
                                picked.file?.absolutePath,
                            )
                            sendMessage(newMessage)
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
        //录音结束回调,发送录音
        binding.chatInputContainer.btnAudio.setOnFinishedRecordListener { audioPath, time ->
            val file = File(audioPath)
            if (file.exists()) {
                val newMessage = mChatHandler.createAudioMessage(audioPath, time)
                sendMessage(newMessage)
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

    private fun createChatSession() {
        //TODO
        val conversationId = "xxx"
        val myAccountId = NIMClient.getService(V2NIMLoginService::class.java).loginUser
        val userService = NIMClient.getService(V2NIMUserService::class.java)
        val myUsrInfo = userService.getUserInfo(myAccountId).data
        mChatSession = ChatSession()
        mChatSession.myInfo = myUsrInfo
        mChatSession.conversationType = V2NIMConversationType.V2NIM_CONVERSATION_TYPE_TEAM
        mChatSession.conversationId = conversationId
        mChatHandler = ChatMsgHandler(this)

    }

    private fun initRecyclerView() {
        mMsgList = arrayListOf()
        mAdapter =
            ChatAdapter(this, mMsgList, V2NIMConversationType.V2NIM_CONVERSATION_TYPE_TEAM).apply {
                addChildClickViewIds(
                    R.id.chat_item_header,
                    R.id.chat_item_layout_content,
                    R.id.chat_item_fail,
                )
                addChildLongClickViewIds(R.id.chat_item_layout_content)
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
                Logger.e("加载更多历史消息")
                loadMessage()
            }

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
        messages.forEach {
//            if (!it.isSelf) {
//                NIMClient.getService(V2NIMMessageService::class.java)
//                    .sendP2PMessageReceipt(it, { }) { }
//            }
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

    private fun RecyclerView.doAfterNextLayout(action: () -> Unit) {
        viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                viewTreeObserver.removeOnPreDrawListener(this)
                action()
                return true
            }
        })
    }

    private fun dealAdapterChildItemLoongClick(
        view: View, item: MessageItem.SdkMessage, position: Int
    ) {
        val chatContextMenu = ChatContextMenu(view.context)
        chatContextMenu.showOnAnchor(
            view,
            RelativePopupWindow.VerticalPosition.ABOVE,
            RelativePopupWindow.HorizontalPosition.CENTER
        )
    }

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
                    //TODO：
                    retrySendMessageDialog {
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
        val v2NIMMessageFileAttachment = item.message.attachment as V2NIMMessageFileAttachment
        Logger.e("file url " + v2NIMMessageFileAttachment.url)
        val intent = Intent(this, WebViewActivity::class.java)
        intent.putExtra("url", v2NIMMessageFileAttachment.url)
        startActivity(intent)
//        try {
//            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(v2NIMMessageFileAttachment.url))
//            intent.addCategory(Intent.CATEGORY_BROWSABLE)
//            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//            startActivity(intent)
//        } catch (e: Exception) {
//            e.printStackTrace()
//            Toast.makeText(this, "未找到可用的浏览器", Toast.LENGTH_SHORT).show()
//        }
    }

    private fun onPressShowVideo(item: MessageItem.SdkMessage) {
        val v2NIMMessageVideoAttachment = item.message.attachment as V2NIMMessageVideoAttachment
        val intent = Intent(this, VideoPlayerActivity::class.java)
        intent.putExtra("videoUrl", v2NIMMessageVideoAttachment.url)
        startActivity(intent)
    }

    /**
     * 点击音频，播放
     */
    private fun onPressShowAudio(msg: MessageItem.SdkMessage, view: View, position: Int) {
        MediaManager.release()
        mNewGifDrawable?.let {
            if (it.isRunning) {
                mNewGifDrawable?.stop()
                if (msg.message.isSelf) {
                    mIvItemAudio?.setImageResource(R.mipmap.ic_audio_animation_right)
                } else {
                    mIvItemAudio?.setImageResource(R.mipmap.ic_audio_animation_left)
                }
            }
        }
        mIvItemAudio = view.findViewById<ImageView>(R.id.ivAudio)
        val options = RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE)
        mIvItemAudio?.let {
            Glide.with(this)
                .load(if (msg.message.isSelf) R.drawable.voice_white else R.drawable.voice_black)
                .apply(options).into(it)
        }
        val gifDrawable = mIvItemAudio?.drawable
        mNewGifDrawable = gifDrawable?.let {
            gifDrawable as GifDrawable
        }
        mNewGifDrawable?.start()
        val v2NIMMessageAudioAttachment = msg.message.attachment as V2NIMMessageAudioAttachment
        MediaManager.playSound(this@ChatGroupActivity, v2NIMMessageAudioAttachment.url, {
            mNewGifDrawable?.stop()
            if (msg.message.isSelf) {
                mIvItemAudio?.setImageResource(R.mipmap.ic_audio_animation_right)
            } else {
                mIvItemAudio?.setImageResource(R.mipmap.ic_audio_animation_left)
            }
            MediaManager.release()
        }, MediaPlayer.OnErrorListener { p0, p1, p2 ->
            if (msg.message.isSelf) {
                mIvItemAudio?.setImageResource(R.mipmap.ic_audio_animation_right)
            } else {
                mIvItemAudio?.setImageResource(R.mipmap.ic_audio_animation_left)
            }
            MediaManager.release()
            return@OnErrorListener false
        })

    }


    private fun initTitleBar() {
        binding.titleBar.setTitle("群组")
    }

    private fun onPressShowImage(view: View, msg: MessageItem.SdkMessage, position: Int) {
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
        val intent = Intent(this, PhotoViewerActivity::class.java).apply {
            putStringArrayListExtra(PhotoViewerActivity.EXTRA_IMAGE_URLS, ArrayList(pathList))
            putExtra(PhotoViewerActivity.EXTRA_POSITION, position)
        }
        startActivity(intent)
    }

}