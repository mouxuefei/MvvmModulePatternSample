package com.scheartmed.push.receiver;

import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.core.commonsdk.utils.MyLogger;
import com.huawei.hms.push.RemoteMessage;
import com.netease.nimlib.sdk.mixpush.HWPushMessageService;

import java.util.logging.Logger;

import androidx.annotation.Nullable;

/**
 * 以下这些方法运行在非 UI 线程中, 与华为的 HmsMessageService 方法一一对应。
 * 当开发者自身也接入华为推送，则应将继承 HmsMessageService 改为继承 HWPushMessageService，其他不变
 * 例如：自定义一个 DemoHWPushMessageService 继承 HWPushMessageService
 */
public class HuaWeiHWPushMessageService extends HWPushMessageService {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onNewToken(String token) {
        super.onNewToken(token);
        MyLogger.getLogger().d("HWPushMessageService onNewToken token=" + token);
    }

    /**
     * 透传消息，需要用户自己弹出通知
     *
     * @param remoteMessage
     */
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        MyLogger.getLogger().d("HWPushMessageService onMessageReceived remoteMessage=" + remoteMessage.getData());
    }

    public void onMessageSent(String s) {
        super.onMessageSent(s);
    }

    public void onDeletedMessages() {
        super.onDeletedMessages();
    }

    public void onSendError(String var1, Exception var2) {
        super.onSendError(var1, var2);
    }
}
