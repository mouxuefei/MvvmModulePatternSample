package com.mou.basemvvm.widget.dialog

/***
 * You may think you know what the following code does.
 * But you dont. Trust me.
 * Fiddle with it, and youll spend many a sleepless
 * night cursing the moment you thought youd be clever
 * enough to "optimize" the code below.
 * Now close this file and go play with something else.
 */


import androidx.appcompat.app.AlertDialog
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.PermissionUtils
import com.mou.easymvvm.R

/***
 *
 * █████▒█    ██  ▄████▄   ██ ▄█▀       ██████╗ ██╗   ██╗ ██████╗
 * ▓██   ▒ ██  ▓██▒▒██▀ ▀█   ██▄█▒        ██╔══██╗██║   ██║██╔════╝
 * ▒████ ░▓██  ▒██░▒▓█    ▄ ▓███▄░        ██████╔╝██║   ██║██║  ███╗
 * ░▓█▒  ░▓▓█  ░██░▒▓▓▄ ▄██▒▓██ █▄        ██╔══██╗██║   ██║██║   ██║
 * ░▒█░   ▒▒█████▓ ▒ ▓███▀ ░▒██▒ █▄       ██████╔╝╚██████╔╝╚██████╔╝
 * ▒ ░   ░▒▓▒ ▒ ▒ ░ ░▒ ▒  ░▒ ▒▒ ▓▒       ╚═════╝  ╚═════╝  ╚═════╝
 * ░     ░░▒░ ░ ░   ░  ▒   ░ ░▒ ▒░
 * ░ ░    ░░░ ░ ░ ░        ░ ░░ ░
 * ░     ░ ░      ░  ░
 *
 * Created by mou on 2018/11/16.
 * 关于用户权限的dialog
 */
object DialogHelper {

    /**
     * tips:提示用户权限信息
     * 显示用户拒绝权限提示
     */
    fun showRationaleDialog(tips: String, shouldRequest: PermissionUtils.OnRationaleListener.ShouldRequest) {
        val topActivity = ActivityUtils.getTopActivity() ?: return
        AlertDialog.Builder(topActivity, R.style.BDAlertDialog)
                .setTitle("提示")
                .setMessage(tips)
                .setPositiveButton(android.R.string.ok) { _, _ ->
                    //继续申请权限
                    shouldRequest.again(true)
                }
                .setNegativeButton(android.R.string.cancel) { _, _ ->
                    //取消权限申请
                    shouldRequest.again(false)
                }
                .setCancelable(false)
                .create()
                .show()

    }

    /**
     * 打开系统设置弹窗
     */
    fun showOpenAppSettingDialog(tips: String) {
        val topActivity = ActivityUtils.getTopActivity() ?: return
        AlertDialog.Builder(topActivity, R.style.BDAlertDialog)
                .setTitle("提示")
                .setMessage(tips)
                .setPositiveButton(android.R.string.ok) { _, _ ->
                    //跳转系统设置界面
                    PermissionUtils.launchAppDetailsSettings()
                }
                .setNegativeButton(android.R.string.cancel) { _, _ -> }
                .setCancelable(false)
                .create()
                .show()
    }
}
