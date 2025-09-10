package com.mou.mvvmmodule.core

import android.graphics.Color
import com.core.basemvvm.BaseApplication
import com.core.commonsdk.utils.FileUtils
import com.core.commonsdk.utils.SpUtil
import com.huawei.hms.support.common.ActivityMgr
import com.mou.mvvmmodule.R
import com.mou.mvvmmodule.ui.main.views.MainActivity
import com.netease.nimlib.sdk.NIMClient
import com.netease.nimlib.sdk.NotificationFoldStyle
import com.netease.nimlib.sdk.SDKOptions
import com.netease.nimlib.sdk.StatusBarNotificationConfig
import com.netease.nimlib.sdk.StatusBarNotificationFilter
import com.netease.nimlib.sdk.StatusBarNotificationFilter.FilterPolicy
import com.netease.nimlib.sdk.mixpush.MixPushConfig
import com.netease.nimlib.sdk.util.NIMUtil
import com.scheartmed.im.widget.WebViewPool
import java.io.File


class App : BaseApplication() {
    override fun onCreate() {
        super.onCreate()
        initSp()
        initIM()
        initWebView()
    }

    private fun initSp() {
        SpUtil.init(this)
    }

    private fun initIM() {
        val options = SDKOptions()
        options.asyncInitSDK = true
        options.reducedIM = false
        options.checkManifestConfig = true
        options.enableTeamMsgAck = true
        options.enableFcs = false
        options.enableV2CloudConversation = true

        options.sdkStorageRootPath =
            getExternalFilesDir(null)?.absolutePath + File.separatorChar + "nim"

        val mixConfig = getMixPushConfig()
        options.mixPushConfig = mixConfig

        val statusBarConfig = loadStatusBarNotificationConfig()
        options.statusBarNotificationConfig = statusBarConfig

        NIMClient.initV2(this, options)
    }

    private fun initWebView() {
        WebViewPool.getInstance(this).preCreateWebView()
    }


    /**
     * 只有 statusBarNotificationConfig 配置不为空时，toggleNotification 和 toggleRevokeMessageNotification 方法才有效。
     */
    private fun loadStatusBarNotificationConfig(): StatusBarNotificationConfig {
        val config = StatusBarNotificationConfig()
        // 单击通知需要跳转到的界面
        config.notificationEntrance = MainActivity::class.java
        config.notificationSmallIconId = R.mipmap.ic_launcher
        // 通知铃声的 uri 字符串
//        config.notificationSound = "raw/msg"
        config.notificationFolded = true
        config.notificationFoldStyle = NotificationFoldStyle.ALL
        config.downTimeEnableNotification = true
        // 呼吸灯配置
        config.ledARGB = Color.GREEN
        config.ledOnMs = 1000
        config.ledOffMs = 1500
        // 是否 App ICON 显示未读数红点(安卓 O 有效)
        config.showBadge = true

        config.notificationFilter = StatusBarNotificationFilter { FilterPolicy.PERMIT }
        return config
    }

    private fun getMixPushConfig(): MixPushConfig {

        // huawei push
        ActivityMgr.INST.init(this)

        val config = MixPushConfig()
        // 传入从小米推送平台获取到的 AppId 与 AppKey
        // 传入从小米推送平台获取到的 AppId 与 AppKey
//        config.xmAppId = "xxxx"
//        config.xmAppKey = "xxxx"
//        // 传入网易云信控制台上小米推送对应的证书名
//        config.xmCertificateName = "xxxx"

        // 传入华为推送的 App ID
        config.hwAppId = "115244125";
        // 传入网易云信控制台上华为推送证书名
        config.hwCertificateName = "huawei";

        // 传入荣耀推送证书名，荣耀推送的 appId 请在 AndroidManifest.xml 文件中配置
//        config.honorCertificateName = "xxxx"

        // 传入网易云信控制台上配置的 vivo 推送证书名,vivo 推送的 appId appKey 请在 AndroidManifest.xml 文件中配置
//        config.vivoCertificateName = "xxxx"


//        config.oppoAppId = "xxxx";
//        config.oppoAppKey = "xxxxxx";
//        // 注意区分 AppSercet 与 MasterSecret
//        config.oppoAppSercet = "xxxxxxx";
//        // 传入网易云信控制台上配置的 oppo 推送证书名
//        config.oppoCertificateName = "xxxx";


        //TODO：荣耀和oppo
        if (NIMUtil.isMainProcess(this)) {
            // 在此处添加以下代码
//            HonorPushClient.getInstance().init(getApplicationContext(), true);
//            com.heytap.msp.push.HeytapPushManager.init(this, true);
        }
        return config
    }
}