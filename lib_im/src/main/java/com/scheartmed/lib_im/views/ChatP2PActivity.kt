package com.scheartmed.lib_im.views

import android.graphics.Color
import android.view.View
import android.view.ViewTreeObserver
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.fortunes.commonsdk.base.BaseActivity
import com.scheartmed.lib_im.R
import com.scheartmed.lib_im.data.model.IconFontEntity
import com.scheartmed.lib_im.data.model.MessageCustomType
import com.scheartmed.lib_im.data.model.MessageListEntity
import com.scheartmed.lib_im.data.model.SystemTextListEntity
import com.scheartmed.lib_im.databinding.ActivityChatP2pBinding
import com.scheartmed.lib_im.viewmodels.ChatP2PViewModel
import com.scheartmed.lib_im.views.adapter.ChatAdapter
import com.scheartmed.lib_im.views.adapter.SystemActionListener
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
class ChatP2PActivity : BaseActivity<ChatP2PViewModel>(), SwipeRefreshLayout.OnRefreshListener {
    override val binding: ActivityChatP2pBinding by lazy {
        ActivityChatP2pBinding.inflate(layoutInflater)
    }

    override fun providerVMClass(): Class<ChatP2PViewModel> {
        return ChatP2PViewModel::class.java
    }

    private var mAdapter: ChatAdapter? = null
    private var mNormalToolsAdapter: BaseQuickAdapter<String, BaseViewHolder>? = null
    private var mChatUiHelper: ChatUiHelper? = null

    val shortcutIcons = mapOf(
        "edy-im-xiazhenduan" to R.mipmap.edy_im_xiazhenduan,
        "im-chufang" to R.mipmap.edy_im_chufang,
        "edy-im-jianyanjiancha" to R.mipmap.edy_im_jianyanjiancha,
        "finishInterrogation" to R.mipmap.edy_im_jieshu,
        "edy-im-dianhua" to R.mipmap.edy_im_dianhua,
        "im-shipinwenzhen" to R.mipmap.edy_im_shipinwenzhen,
        "im-tupian" to R.mipmap.edy_im_tupian,
        "edy-im-guahao" to R.mipmap.edy_im_guahao,
        "edy-im-mingpian" to R.mipmap.edy_im_mingpian,
        "edy-im-wenzhang" to R.mipmap.edy_im_wenzhang,
        "edy-im-fuwubao" to R.mipmap.edy_im_fuwubao,
        "edy-IM-suifang" to R.mipmap.edy_im_suifang,
        "edy-im-ruyuanzheng" to R.mipmap.edy_im_ruyuanzheng,
        "edy-fuzhenyuyue" to R.mipmap.edy_im_fuzhenyuyue,
        "im-tuijianyisheng" to R.mipmap.edy_im_tuijianyisheng,
        "edy-im-dianzibingli-new" to R.mipmap.edy_im_dianzibingli_new,
        "im-shensu" to R.mipmap.edy_im_shensu,
        "im-tuikuan" to R.mipmap.edy_im_tuikuan,
    )

    override fun initView() {
        initRv()
        initListener()
        initChatUi()
    }

    private fun initChatUi() {
        mChatUiHelper = ChatUiHelper.with(this)
        val data = arrayListOf<MoreLayoutItemBean>()
        shortcutIcons.map { item ->
            data.add(MoreLayoutItemBean(item.key, item.value))
        }
        mChatUiHelper?.bindContentLayout(binding.llContent)
            ?.bindToSendButton(binding.chatInputContainer.btnSend)
            ?.bindEditText(binding.chatInputContainer.etContent)
            ?.bindBottomLayout(binding.bottomLayout)?.bindEmojiLayout(binding.rlEmotion.rootEmoji)
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
//            mConversationId?.let { it1 ->
//                mPresenter.sendTextMsg(
//                    binding.chatInputContainer.etContent.text.toString(),
//                    conversationId = it1
//                )
//            }
            binding.chatInputContainer.etContent.setText("")
        }

    }


    private fun initRv() {
        mAdapter = ChatAdapter(this, ArrayList())
        binding.rvChatList.adapter = mAdapter
        binding.swipeChat.setOnRefreshListener(this)
        mAdapter?.addChildClickViewIds(
            R.id.chat_item_header,
            R.id.chat_item_layout_content,
            R.id.chat_item_fail,
            R.id.item_card_pre_look_detail,
            R.id.item_card_pre_use,
            R.id.item_phone_tip_call,
            R.id.item_card_pre_look_his_detail
        )
        mAdapter?.setOnItemChildClickListener { adapter, view, position ->
            val item = adapter.getItem(position) as MessageListEntity
            dealAdapterChildItemClick(view, item, position)
        }

        binding.rlEmotion.emojiRv.adapter = object : BaseQuickAdapter<String, BaseViewHolder>(
            R.layout.item_emoji, mutableListOf<String>(
                "目前在服用哪些药物呢？",
                "感谢您的配合，祝您和家人健康！",
                "请坚持服药，每1-3个月复诊一次",
                "请不要自行减药、停药。",
                "请遵医嘱结合药品说明书用药。"
            )
        ) {
            override fun convert(holder: BaseViewHolder, item: String) {
                holder.setText(R.id.tv_title, item)
            }
        }.also { mNormalToolsAdapter = it }
        mNormalToolsAdapter?.setOnItemClickListener { adapter, view, position ->
            val item = adapter.getItem(position) as String
            binding.chatInputContainer.etContent.setText(item)
        }
        binding.rvChatList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
//                val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
//                val firstItemPosition = linearLayoutManager.findFirstVisibleItemPosition()
//                val position = (mLastConditionDataItemPosition ?: 0) + 1
//                if (mLastConditionDataItem != null && firstItemPosition >= position && layout_patient_info.visibility == View.GONE) {
//                    layout_patient_info.visibility = View.VISIBLE
//                } else if (mLastConditionDataItem != null && firstItemPosition < position && layout_patient_info.visibility == View.VISIBLE) {
//                    layout_patient_info.visibility = View.GONE
//                }
            }
        })
        //点击上传资料的查看资料
        mAdapter?.setSystemActionListener(object : SystemActionListener {
            override fun clickView(item: MessageListEntity, child: SystemTextListEntity) {
//                val href = UrlUtil.parse(child.href)
//                val baseUrl = href["baseUrl"]
//                val param = href["param"]
//                //TODO
//                toast(child.href)
            }
        })
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
    }

    /**
     * 处理点击item事件
     */
    private fun dealAdapterChildItemClick(
        view: View, item: MessageListEntity, position: Int
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
                when (item.msgType) {
                    MessageCustomType.SOUND -> {
//                        onPressAudio(item, view, position)
                    }

                    MessageCustomType.IMAGE -> {
//                        onPressImage(view, item)
                    }

                    else -> {
//                        clickMessageItem(mConversationId, mConfigData, item)
                    }
                }
            }
            //查看处方详情
            R.id.item_card_pre_look_detail -> {

            }

            R.id.item_card_pre_look_his_detail -> {

            }
            //使用处方
            R.id.item_card_pre_use -> {

            }

            R.id.item_phone_tip_call -> {


            }
        }
    }

    override fun initData() {
    }

    override fun onRefresh() {
//        mConversationId?.let {
//            mPresenter.getByOrderId(it, false)
//            val data = mAdapter?.data
//            if (data?.size!! > 0) {
//                val item = mAdapter?.getItem(data.size - 1)
//                mPresenter.fetchMessageList(item?.time, it, true)
//            } else {
//                swipeChat.isRefreshing = false
//            }
//        }
    }
}