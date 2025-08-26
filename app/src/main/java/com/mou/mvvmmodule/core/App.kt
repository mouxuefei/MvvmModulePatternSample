package com.mou.mvvmmodule.core

import com.fortunes.commonsdk.utils.SpUtil
import com.mou.basemvvm.BaseApplication
import com.mou.mvvmmodule.ui.main.views.MainActivity
import com.netease.nimlib.sdk.NIMClient
import com.netease.nimlib.sdk.SDKOptions
import com.netease.nimlib.sdk.StatusBarNotificationConfig
import com.netease.nimlib.sdk.StatusBarNotificationFilter
import com.netease.nimlib.sdk.mixpush.MixPushConfig
import com.netease.nimlib.sdk.msg.model.IMMessage
import com.netease.nimlib.sdk.util.NIMUtil


class App : BaseApplication() {
    override fun onCreate() {
        super.onCreate()
        SpUtil.init(this)
        val options = SDKOptions()


        options.asyncInitSDK = true
        options.reducedIM = false
        options.checkManifestConfig = false
        options.enableTeamMsgAck = false
        options.enableFcs = false
        options.enableV2CloudConversation = true

        val config = getMixPushConfig()

        options.mixPushConfig = config


        initStatusBarNotificationConfig(options)
        NIMClient.initV2(this, options)
    }

    private fun initStatusBarNotificationConfig(options: SDKOptions) {
        // load notification
        val config: StatusBarNotificationConfig =
            loadStatusBarNotificationConfig()
        // load 用户的 StatusBarNotificationConfig 设置项
        // SDK statusBarNotificationConfig 生效
//        config.notificationFilter =
//            StatusBarNotificationFilter { imMessage: IMMessage? -> if (IMApplication.getForegroundActCount() > 0) StatusBarNotificationFilter.FilterPolicy.DENY else StatusBarNotificationFilter.FilterPolicy.DEFAULT }
        options.statusBarNotificationConfig = config
    }

    private fun loadStatusBarNotificationConfig(): StatusBarNotificationConfig {
        val config = StatusBarNotificationConfig()
        config.notificationEntrance = MainActivity::class.java
//        config.notificationSmallIconId = R.mipmap.ic_logo
//        config.notificationColor = Color.parseColor("#3a9efb")
//        config.notificationSound = com.netease.yunxin.app.im.NimSDKOptionConfig.NOTIFY_SOUND_KEY
//        config.notificationFoldStyle = NotificationFoldStyle.ALL
//        config.downTimeEnableNotification = true
//        config.ledARGB = Color.GREEN
//        config.ledOnMs = com.netease.yunxin.app.im.NimSDKOptionConfig.LED_ON_MS
//        config.ledOffMs = com.netease.yunxin.app.im.NimSDKOptionConfig.LED_OFF_MS
        config.showBadge = true
        return config
    }

    private fun getMixPushConfig(): MixPushConfig {
        val config = MixPushConfig()
        // 传入从小米推送平台获取到的 AppId 与 AppKey
        // 传入从小米推送平台获取到的 AppId 与 AppKey
        config.xmAppId = "xxxx"
        config.xmAppKey = "xxxx"
        // 传入网易云信控制台上小米推送对应的证书名
        config.xmCertificateName = "xxxx"

        // 传入华为推送的 App ID
        config.hwAppId = "xxxx";
        // 传入网易云信控制台上华为推送证书名
        config.hwCertificateName = "xxxx";

        // 传入荣耀推送证书名，荣耀推送的 appId 请在 AndroidManifest.xml 文件中配置
        config.honorCertificateName = "xxxx"

        // 传入网易云信控制台上配置的 vivo 推送证书名,vivo 推送的 appId appKey 请在 AndroidManifest.xml 文件中配置
        config.vivoCertificateName = "xxxx"


        config.oppoAppId = "xxxx";
        config.oppoAppKey = "xxxxxx";
        // 注意区分 AppSercet 与 MasterSecret
        config.oppoAppSercet = "xxxxxxx";
        // 传入网易云信控制台上配置的 oppo 推送证书名
        config.oppoCertificateName = "xxxx";

        //魅族
        config.mzAppId = "xxx";
        config.mzAppKey = "xxxx";
        config.mzCertificateName = "xxxx"

        //TODO：荣耀和oppo
        if (NIMUtil.isMainProcess(this)) {
            // 在此处添加以下代码
//            HonorPushClient.getInstance().init(getApplicationContext(), true);
//            com.heytap.msp.push.HeytapPushManager.init(this, true);
        }
        return config
    }
}