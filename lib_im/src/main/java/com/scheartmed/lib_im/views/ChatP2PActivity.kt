package com.scheartmed.lib_im.views

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewTreeObserver
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.fortunes.commonsdk.base.BaseActivity
import com.netease.nimlib.sdk.NIMClient
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum
import com.netease.nimlib.sdk.msg.model.IMMessage
import com.netease.nimlib.sdk.v2.auth.V2NIMLoginService
import com.netease.nimlib.sdk.v2.conversation.enums.V2NIMConversationType
import com.netease.nimlib.sdk.v2.message.V2NIMClearHistoryNotification
import com.netease.nimlib.sdk.v2.message.V2NIMMessage
import com.netease.nimlib.sdk.v2.message.V2NIMMessageDeletedNotification
import com.netease.nimlib.sdk.v2.message.V2NIMMessageListener
import com.netease.nimlib.sdk.v2.message.V2NIMMessagePinNotification
import com.netease.nimlib.sdk.v2.message.V2NIMMessageQuickCommentNotification
import com.netease.nimlib.sdk.v2.message.V2NIMMessageRevokeNotification
import com.netease.nimlib.sdk.v2.message.V2NIMMessageService
import com.netease.nimlib.sdk.v2.message.V2NIMP2PMessageReadReceipt
import com.netease.nimlib.sdk.v2.message.V2NIMTeamMessageReadReceipt
import com.netease.nimlib.sdk.v2.message.enums.V2NIMMessageType
import com.netease.nimlib.sdk.v2.message.result.V2NIMMessageListResult
import com.netease.nimlib.sdk.v2.user.V2NIMUserService
import com.netease.nimlib.sdk.v2.utils.V2NIMConversationIdUtil
import com.orhanobut.logger.Logger
import com.scheartmed.lib_im.R
import com.scheartmed.lib_im.data.MessageItem
import com.scheartmed.lib_im.data.model.ChatSession
import com.scheartmed.lib_im.databinding.ActivityChatP2pBinding
import com.scheartmed.lib_im.listener.CustomMessageListener
import com.scheartmed.lib_im.utils.ChatMsgHandler
import com.scheartmed.lib_im.utils.ChatMsgHandler.TEN_MINUTE
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

    val shortcutIcons = mapOf(
        "edy-im-xiazhenduan" to R.mipmap.edy_im_xiazhenduan,
        "im-chufang" to R.mipmap.edy_im_chufang,
        "edy-im-jianyanjiancha" to R.mipmap.edy_im_jianyanjiancha,
        "finishInterrogation" to R.mipmap.edy_im_jieshu,
        "edy-im-dianhua" to R.mipmap.edy_im_dianhua,
        "im-shipinwenzhen" to R.mipmap.edy_im_shipinwenzhen,
        "im-tupian" to R.mipmap.edy_im_tupian,
    )


    private var messageListener: CustomMessageListener = object : CustomMessageListener() {

        // 判断是否需要添加时间消息
        private fun shouldAddTimeMessage(imMessage: V2NIMMessage, lastMsg: MessageItem?): Boolean {
            return imMessage.conversationType == V2NIMConversationType.V2NIM_CONVERSATION_TYPE_P2P &&
                    imMessage.senderId == mChatSession.chatInfo?.accountId &&
                    (lastMsg == null || (lastMsg is MessageItem.SdkMessage && imMessage.createTime - lastMsg.message.createTime > TEN_MINUTE))
        }

        override fun onReceiveMessages(messages: MutableList<V2NIMMessage>) {
            val lastMsg = mMsgList.lastOrNull()
            if (shouldAddTimeMessage(messages[0], lastMsg)) {
                mMsgList.add(mChatHandler.createTimeMessage(messages[0]))
            }
            val newMessages = messages.filter {
                it.conversationType == V2NIMConversationType.V2NIM_CONVERSATION_TYPE_P2P &&
                        it.senderId == mChatSession.chatInfo?.accountId
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
        mChatHandler.loadMessage(anchorMessage, mChatSession.conversationId, {
            handleMsg(it)
        }, {
            Logger.e("villa" + it.desc)
        })
    }

    private fun handleMsg(it: V2NIMMessageListResult) {
        val messages = it.messages
        messages.reverse()
        val anchorMessage = it.anchorMessage
        //                    binding.rvChatList.hideHeadView()
        var scroll = false
        // 如果原本没有，为第一次加载，需要在加载完成后移动到最后一项
        if (mMsgList.isEmpty()) {
            scroll = true
        }
        if (messages.isNotEmpty()) {
            mMsgList.addAll(0, mChatHandler.dealLoadMessage(messages, anchorMessage))
            mAdapter?.notifyDataSetChanged()
        }
        if (scroll) {
            binding.rvChatList.layoutManager?.scrollToPosition(mMsgList.size - 1)
        }
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
        shortcutIcons.map { item ->
            data.add(MoreLayoutItemBean(item.key, item.value))
        }
        mChatUiHelper?.bindContentLayout(binding.llContent)
            ?.bindToSendButton(binding.chatInputContainer.btnSend)
            ?.bindEditText(binding.chatInputContainer.etContent)
            ?.bindBottomLayout(binding.bottomLayout)
            ?.bindEmojiLayout(binding.layoutExpress)
            ?.bindAddLayout(binding.llAdd.rootAddPanel)
            ?.bindToAddButton(binding.chatInputContainer.ivAdd)
            ?.bindToEmojiButton(binding.chatInputContainer.ivEmo)
            ?.bindAudioBtn(binding.chatInputContainer.btnAudio)
            ?.bindAudioIv(binding.chatInputContainer.ivAudioIcon)
            ?.bindMoreLayoutData(data, null)
//            .bindEmojiData()
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
            mChatHandler.sendMsg(newMessage, mChatSession.conversationId, {
                sendMessageSuccess(newMessage)
            }, {

            }, {

            })
            binding.chatInputContainer.etContent.setText("")
        }
        val v2MessageService = NIMClient.getService(
            V2NIMMessageService::class.java
        )
        v2MessageService.addMessageListener(messageListener)
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
        mAdapter?.setOnItemChildClickListener { adapter, view, position ->
            val item = adapter.getItem(position) as MessageItem
            dealAdapterChildItemClick(view, item, position)
        }

//        binding.rlEmotion.emojiRv.adapter = object : BaseQuickAdapter<String, BaseViewHolder>(
//            R.layout.item_emoji, mutableListOf<String>(
//                "目前在服用哪些药物呢？",
//                "感谢您的配合，祝您和家人健康！",
//                "请坚持服药，每1-3个月复诊一次",
//                "请不要自行减药、停药。",
//                "请遵医嘱结合药品说明书用药。"
//            )
//        ) {
//            override fun convert(holder: BaseViewHolder, item: String) {
//                holder.setText(R.id.tv_title, item)
//            }
//        }.also { mNormalToolsAdapter = it }
//        mNormalToolsAdapter?.setOnItemClickListener { adapter, view, position ->
//            val item = adapter.getItem(position) as String
//            binding.chatInputContainer.etContent.setText(item)
//        }
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
                val firstVisible = layoutManager.findFirstCompletelyVisibleItemPosition()
                if (firstVisible == 0) {
                    if (mMsgList.isEmpty() || mMsgList.size < ChatMsgHandler.ONE_QUERY_LIMIT) {
                        return
                    }
                    Logger.e("加载更多")
                    loadMessage()
                }
            }

            override fun onScrollStateChanged(rv: RecyclerView, newState: Int) {
                //快速滑动时暂停加载，滑动停下再恢复
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    Glide.with(this@ChatP2PActivity).resumeRequests()
                } else {
                    Glide.with(this@ChatP2PActivity).pauseRequests()
                }
            }
        })

    }


    /**
     * 处理点击item事件
     */
    private fun dealAdapterChildItemClick(
        view: View, item: MessageItem, position: Int
    ) {
        when (view.id) {
            //头像
            R.id.chat_item_header -> {

            }
            //失败按钮
            R.id.chat_item_fail -> {
//                retrySendMessageDialog {
//                    when (item.msgType) {
//                        MessageCustomType.TEXT -> {
//                            mConversationId?.let { id ->
//                                mPresenter.sendTextMsg(
//                                    item.payLoad?.data?.get(0)?.msgContent?.Text ?: "",
//                                    id,
//                                    item
//                                )
//                            }
//                        }
//                        MessageCustomType.IMAGE -> {
//                            mConversationId?.let { id ->
//                                val imageItem =
//                                    item.payLoad?.data?.get(0)?.msgContent?.ImageInfoArray?.get(1)
//                                val url = imageItem?.URL
//                                mPresenter.sendImageMessage(
//                                    url ?: "",
//                                    id,
//                                    item
//                                )
//                            }
//                        }
//                        MessageCustomType.SOUND -> {
//                            mConversationId?.let { id ->
//                                val second = item.payLoad?.data?.get(0)?.msgContent?.Second
//                                val url = item.payLoad?.data?.get(0)?.msgContent?.Url
//                                mPresenter.sendAudioMessage(
//                                    url ?: "",
//                                    second ?: 0,
//                                    id,
//                                    item
//                                )
//                            }
//                        }
//                    }
//                }
            }
            //内容
            R.id.chat_item_layout_content -> {
//                when (item.messageType) {
//                    V2NIMMessageType.V2NIM_MESSAGE_TYPE_AUDIO -> {
////                        onPressAudio(item, view, position)
//                    }
//
//                    V2NIMMessageType.V2NIM_MESSAGE_TYPE_IMAGE -> {
////                        onPressImage(view, item)
//                    }
//
//                    else -> {
////                        clickMessageItem(mConversationId, mConfigData, item)
//                    }
//                }
            }

            R.id.item_phone_tip_call -> {


            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        NIMClient.getService(V2NIMMessageService::class.java).removeMessageListener(messageListener)
    }

}