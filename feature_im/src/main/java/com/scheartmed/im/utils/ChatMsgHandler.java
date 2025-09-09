package com.scheartmed.im.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;

import com.core.commonsdk.utils.FileUtils;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.v2.V2NIMFailureCallback;
import com.netease.nimlib.sdk.v2.V2NIMProgressCallback;
import com.netease.nimlib.sdk.v2.V2NIMSuccessCallback;
import com.netease.nimlib.sdk.v2.message.V2NIMMessage;
import com.netease.nimlib.sdk.v2.message.V2NIMMessageCreator;
import com.netease.nimlib.sdk.v2.message.V2NIMMessageService;
import com.netease.nimlib.sdk.v2.message.config.V2NIMMessageConfig;
import com.netease.nimlib.sdk.v2.message.option.V2NIMMessageListOption;
import com.netease.nimlib.sdk.v2.message.params.V2NIMSendMessageParams;
import com.netease.nimlib.sdk.v2.message.result.V2NIMMessageListResult;
import com.netease.nimlib.sdk.v2.message.result.V2NIMSendMessageResult;
import com.scheartmed.im.data.MessageItem;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ChatMsgHandler {

    private static final String TAG = ChatMsgHandler.class.getSimpleName();

    public static final int ONE_QUERY_LIMIT = 30;
    public static final long TEN_MINUTE = 1000 * 60 * 10;

    private Context mContext;

    public ChatMsgHandler(Context context) {
        mContext = context;
    }

    /**
     * 发送文本消息
     *
     * @param text 文本
     */
    public V2NIMMessage createTextMessage(String text) {
        return V2NIMMessageCreator.createTextMessage(text);
    }

    public void sendMsg(V2NIMMessage v2Message, String conversationId, V2NIMSuccessCallback<V2NIMSendMessageResult> sendMessageResultV2NIMSuccessCallback, V2NIMFailureCallback failCallback, V2NIMProgressCallback progressCallback) {
        V2NIMMessageService v2MessageService = NIMClient.getService(V2NIMMessageService.class);


        V2NIMMessageConfig messageConfig = V2NIMMessageConfig
                .V2NIMMessageConfigBuilder
                .builder()
                .withLastMessageUpdateEnabled(true)//设置是否需要更新消息所属的会话信息
                .withHistoryEnabled(true)//设置是否需要在服务端保存历史消息
                .withOfflineEnabled(true)
                .withOnlineSyncEnabled(true)
                .withReadReceiptEnabled(true)
                .withUnreadEnabled(true)
                .build();
//推送
//        V2NIMMessagePushConfig pushConfig = V2NIMMessagePushConfig.V2NIMMessagePushConfigBuilder.builder()
//                .withContent()
//                .withForcePush()
//                .withForcePushAccountIds()
//                .withForcePushContent()
//                .withPayload()
//                .withPushEnabled()
//                .withPushNickEnabled()
//                .build();


        V2NIMSendMessageParams sendMessageParams = V2NIMSendMessageParams.V2NIMSendMessageParamsBuilder.builder().withMessageConfig(messageConfig)
//                .withPushConfig(pushConfig)
                .build();
        v2MessageService.sendMessage(v2Message, conversationId, sendMessageParams, sendMessageResultV2NIMSuccessCallback, failCallback, progressCallback);
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
    public V2NIMMessage createImageMessage(String path, int width, int height) {
        String newPath = path;
        if (newPath.contains("content://")) {
            newPath = uriToFile(path).getAbsolutePath();
        }
        return V2NIMMessageCreator.createImageMessage(newPath, null, null, width, height);

    }

    public V2NIMMessage createVideoMessage(String path, int duration) {
        return V2NIMMessageCreator.createVideoMessage(path, null, null, duration, null, null);
    }

    // 1. 把 content:// URI 转成缓存目录临时文件
    public File uriToFile(String path) {
        try {
            ContentResolver resolver = mContext.getContentResolver();
            InputStream inputStream = resolver.openInputStream(Uri.parse(path));
            String fileName = queryFileName(Uri.parse(path));
            File tempFile = new File(FileUtils.INSTANCE.getImageCachePath(), fileName);
            FileOutputStream outputStream = new FileOutputStream(tempFile);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }
            inputStream.close();
            outputStream.close();
            return tempFile;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 2. 查询文件名
    private String queryFileName(Uri uri) {
        String name = "temp_file";
        Cursor cursor = mContext.getContentResolver().query(uri, null, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                if (index != -1) {
                    name = cursor.getString(index);
                }
            }
            cursor.close();
        }
        return name;
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


    public V2NIMMessage createFileMessage(String path) {
        return V2NIMMessageCreator.createFileMessage(path, null, null);
    }

    public V2NIMMessage createCustomMessage(String text, String rawAttachment) {
        V2NIMMessage v2CustomMessage = V2NIMMessageCreator.createCustomMessage(text, rawAttachment);
        return v2CustomMessage;
    }

    public V2NIMMessage createTipsMessage(String text) {
        V2NIMMessage v2CustomMessage = V2NIMMessageCreator.createTipsMessage(text);
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
        V2NIMMessageListOption.V2NIMMessageListOptionBuilder listOption = V2NIMMessageListOption.V2NIMMessageListOptionBuilder.builder(conversationId).withLimit(ONE_QUERY_LIMIT);
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
