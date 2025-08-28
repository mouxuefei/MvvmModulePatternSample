package com.scheartmed.lib_im.utils;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.text.TextUtils;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.v2.V2NIMError;
import com.netease.nimlib.sdk.v2.V2NIMFailureCallback;
import com.netease.nimlib.sdk.v2.V2NIMSuccessCallback;
import com.netease.nimlib.sdk.v2.message.V2NIMMessage;
import com.netease.nimlib.sdk.v2.message.V2NIMMessageCreator;
import com.netease.nimlib.sdk.v2.message.V2NIMMessageService;
import com.netease.nimlib.sdk.v2.message.option.V2NIMMessageListOption;
import com.netease.nimlib.sdk.v2.message.result.V2NIMMessageListResult;
import com.scheartmed.lib_im.data.MessageItem;
import com.scheartmed.lib_im.data.model.ChatSession;

import java.util.ArrayList;
import java.util.List;

/**
 * 聊天工具类
 * Created by wudeng on 2017/9/13.
 */

public class ChatMsgHandler {

    private static final String TAG = ChatMsgHandler.class.getSimpleName();

    private static final int ONE_QUERY_LIMIT = 20;
    private static final long TEN_MINUTE = 1000 * 60 * 10;

    private Context mContext;
    private ChatSession mChatSession;

    public ChatMsgHandler(Context context, ChatSession session) {
        mContext = context;
        mChatSession = session;
    }

    /**
     * 发送文本消息
     *
     * @param text 文本
     */
    public V2NIMMessage createTextMessage(String text) {
        return V2NIMMessageCreator.createTextMessage(text);
    }

    /**
     * 发送图片消息
     * <p>
     * Params:
     * imagePath – 图片文件地址 name – 文件显示名称
     * sceneName – 文件存储场景名
     * width – 图片文件宽度
     * height – 图片文件高度
     * Returns:
     * V2NIMMessage
     */
    public V2NIMMessage createImageMessage(String path, Integer width, Integer height) {
        return V2NIMMessageCreator.createImageMessage(path, null, null, width, height);

    }

    /**
     * 发送语音消息
     *
     * @param path 语音文件路径
     * @param time 录音时间 ms
     */
    public V2NIMMessage createAudioMessage(String path, int time) {
        return V2NIMMessageCreator.createAudioMessage(path, null, null, time);
    }

//    /**
//     * 发送视频消息
//     *
//     * @param path 视频文件路径
//     */
//    public V2NIMMessage createVideoMessage(String path) {
//        File file = new File(path);
//        MediaPlayer player = MediaPlayer.create(mContext, Uri.fromFile(file));
//        int duration = player.getDuration();
//        int height = player.getVideoHeight();
//        int width = player.getVideoWidth();
//        return MessageBuilder.createVideoMessage(mChatSession.getSessionId(),
//                mChatSession.getSessionType(), file, duration, width, height, null);
//    }

    public V2NIMMessage createFileMessage(String path) {
        return V2NIMMessageCreator.createFileMessage(path, null, null);
    }

    public V2NIMMessage createCustomMessage(String text, String rawAttachment) {
        V2NIMMessage v2CustomMessage = V2NIMMessageCreator.createCustomMessage(text, rawAttachment);
        return v2CustomMessage;
    }


    /**
     * 加载历史消息记录
     *
     * @param anchorMessage 锚点消息
     * @param listener      加载回调
     */
    public void loadMessage(V2NIMMessage anchorMessage, String conversationId, V2NIMSuccessCallback<V2NIMMessageListResult> listener, V2NIMFailureCallback failureCallback) {
        V2NIMMessageService v2MessageService = NIMClient.getService(V2NIMMessageService.class);
        V2NIMMessageListOption.V2NIMMessageListOptionBuilder listOption = V2NIMMessageListOption.V2NIMMessageListOptionBuilder.builder(conversationId).withLimit(50);
        if (anchorMessage != null) {
            listOption.withAnchorMessage(anchorMessage);
        }
        v2MessageService.getMessageListEx(listOption.build(), listener, failureCallback);

    }

    /**
     * 处理历史消息记录，如果两条消息之间相隔大于 TEN_MINUTE,则需要在两条之间新增时间点文本消息
     *
     * @param messages      历史消息列表
     * @param anchorMessage 锚点消息
     * @return 处理完成后的消息列表
     */
    public List<MessageItem> dealLoadMessage(List<V2NIMMessage> messages, V2NIMMessage anchorMessage) {
        List<MessageItem> list = new ArrayList<>();
        for (int i = 0; i < messages.size(); i++) {
            list.add(new MessageItem.SdkMessage(messages.get(i)));
        }
        V2NIMMessage lastMsg = messages.get(messages.size() - 1);
        if (anchorMessage.getCreateTime() - lastMsg.getCreateTime() >= TEN_MINUTE) {
            list.add(messages.size() - 1, createTimeMessage(lastMsg));
        }


        for (int i = messages.size() - 2; i > 0; i--) {
            if (messages.get(i).getCreateTime() - messages.get(i - 1).getCreateTime() >= TEN_MINUTE) {
                list.add(i, createTimeMessage(messages.get(i)));
            }
        }
        return list;
    }


    public MessageItem createTimeMessage(V2NIMMessage message) {
        return new MessageItem.TimeDivider(message.getCreateTime());
    }

}
