package com.scheartmed.push.receiver;

import android.content.Intent;
import android.os.IBinder;

import com.huawei.hms.push.RemoteMessage;
import com.netease.nimlib.sdk.mixpush.HWPushMessageService;
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
//        MixPushPlatforms.getPushPlatform(PushType.HUA_WEI).onToken(token);
    }

    /**
    * 透传消息，需要用户自己弹出通知
    *
    * @param remoteMessage
    */
    public void onMessageReceived(RemoteMessage remoteMessage) {
    }

    public void onMessageSent(String s) {
    }

    public void onDeletedMessages() {
    }

    public void onSendError(String var1, Exception var2) {
    }
}
