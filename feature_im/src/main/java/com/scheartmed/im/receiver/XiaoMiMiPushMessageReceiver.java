package com.scheartmed.im.receiver;

import android.content.Context;

import com.netease.nimlib.sdk.mixpush.MiPushMessageReceiver;
import com.xiaomi.mipush.sdk.MiPushCommandMessage;
import com.xiaomi.mipush.sdk.MiPushMessage;

/**
* 以下这些方法运行在非 UI 线程中, 与小米 SDK PushMessageReceiver 方法一一对应。
* 如果自身也需要接入小米推送，则应将继承 PushMessageReceiver 改为继承 MiPushMessageReceiver
* 例如：自定义一个 DemoMiPushMessageReceiver 继承 MiPushMessageReceiver
*/
public class XiaoMiMiPushMessageReceiver extends MiPushMessageReceiver {



    public void onReceivePassThroughMessage(Context context, MiPushMessage message) {
    }

    public void onNotificationMessageClicked(Context context, MiPushMessage message) {
    }

    public void onNotificationMessageArrived(Context context, MiPushMessage message) {
    }

    public void onReceiveRegisterResult(Context context, MiPushCommandMessage message) {
    }

    public void onCommandResult(Context context, MiPushCommandMessage message) {
    }

    public void onRequirePermissions(Context context, String[] strings) {
    }
}
