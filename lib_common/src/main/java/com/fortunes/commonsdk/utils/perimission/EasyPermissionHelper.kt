package com.fortunes.commonsdk.utils.perimission

import android.Manifest
import android.content.Context
import com.blankj.utilcode.constant.PermissionConstants
import com.blankj.utilcode.util.PermissionUtils
import com.mou.basemvvm.helper.extens.toast
import com.mou.basemvvm.widget.dialog.DialogHelper


/***
 * You may think you know what the following code does.
 * But you dont. Trust me.
 * Fiddle with it, and youll spend many a sleepless
 * night cursing the moment you thought youd be clever
 * enough to "optimize" the code below.
 * Now close this file and go play with something else.
 */
/***
 *
 *   █████▒█    ██  ▄████▄   ██ ▄█▀       ██████╗ ██╗   ██╗ ██████╗
 * ▓██   ▒ ██  ▓██▒▒██▀ ▀█   ██▄█▒        ██╔══██╗██║   ██║██╔════╝
 * ▒████ ░▓██  ▒██░▒▓█    ▄ ▓███▄░        ██████╔╝██║   ██║██║  ███╗
 * ░▓█▒  ░▓▓█  ░██░▒▓▓▄ ▄██▒▓██ █▄        ██╔══██╗██║   ██║██║   ██║
 * ░▒█░   ▒▒█████▓ ▒ ▓███▀ ░▒██▒ █▄       ██████╔╝╚██████╔╝╚██████╔╝
 *  ▒ ░   ░▒▓▒ ▒ ▒ ░ ░▒ ▒  ░▒ ▒▒ ▓▒       ╚═════╝  ╚═════╝  ╚═════╝
 *  ░     ░░▒░ ░ ░   ░  ▒   ░ ░▒ ▒░
 *  ░ ░    ░░░ ░ ░ ░        ░ ░░ ░
 *           ░     ░ ░      ░  ░
 *
 * Created by mou on 2018/11/16.
 * 关于6.0权限申明的类
 * 例如:
 * EasyPermissionHelper.requestAll({
 *      //申请成功执行的方法
 *      }, {
 *      //申请失败执行的方法
 *      })
 */
object EasyPermissionHelper {

    /**
     * 一次性申请(相机,存储,电话,定位,录音)权限
     */
    fun requestAll(context: Context,
                   grantedListener: () -> Unit,
                   deniedListener: () -> Unit = {}) {
        request(
            context, "相机,存储,电话,定位,录音", grantedListener, deniedListener,
            false, permissions = *arrayOf(
                PermissionConstants.CAMERA,
                PermissionConstants.STORAGE, PermissionConstants.PHONE,
                PermissionConstants.SMS, PermissionConstants.LOCATION,
                PermissionConstants.MICROPHONE
            )
        )
    }

    /**
     * 申请相机权限
     */
    fun requestCamera(context: Context,
                      grantedListener: () -> Unit,
                      deniedListener: () -> Unit = {}) {
        request(
            context, "拍照", grantedListener, deniedListener,
            permissions = *arrayOf(PermissionConstants.CAMERA)
        )
    }

    /**
     * 申请相机和存储权限
     */

    fun requestCameraAndStorage(context: Context,
                                grantedListener: () -> Unit,
                                deniedListener: () -> Unit = {}) {
        request(
            context, "拍照和访问手机相册", grantedListener, deniedListener,
            permissions = *arrayOf(PermissionConstants.CAMERA, PermissionConstants.STORAGE)
        )
    }

    /**
     * 申请存储权限
     */
    fun requestStorage(context: Context,
                       grantedListener: () -> Unit,
                       deniedListener: () -> Unit = {}) {
        request(
            context, "存储", grantedListener, deniedListener,
            permissions = *arrayOf(PermissionConstants.STORAGE)
        )
    }

    /**
     * 申请关联电话功能的权限组
     */
    fun requestPhone(context: Context,
                     grantedListener: () -> Unit,
                     deniedListener: () -> Unit = {}) {
        request(
            context, "电话功能", grantedListener, deniedListener,
            permissions = *arrayOf(PermissionConstants.PHONE)
        )
    }

    /**
     * 申请定位权限
     */
    fun requestLocation(context: Context,
                        grantedListener: () -> Unit,
                        deniedListener: () -> Unit = {}) {
        request(
            context, "定位", grantedListener, deniedListener,
            permissions = *arrayOf(PermissionConstants.LOCATION)
        )
    }

    /**
     * 获取手机通讯录的权限
     */

    fun requestContact(context: Context,  grantedListener: () -> Unit,
                       deniedListener: () -> Unit = {}){
        request(
            context, "通讯录", grantedListener, deniedListener, isContact = true,
            permissions = *arrayOf(PermissionConstants.CONTACTS)
        )
    }

    /**
     * 指纹
     */
    fun requestFinger(context: Context,grantedListener: () -> Unit,
                      deniedListener: () -> Unit = {}){
        request(
            context, "通讯录", grantedListener, deniedListener,
            permissions = *arrayOf(Manifest.permission.USE_FINGERPRINT)
        )
    }

    private fun request(context: Context,
                        tips: String,
                        grantedListener: () -> Unit,
                        deniedListener: () -> Unit,
                        isRationale: Boolean = true,
                        isContact:Boolean=false,
                        @PermissionConstants.Permission vararg permissions: String) {
        PermissionUtils.permission(*permissions)
                //设置拒绝权限后再次请求的回调接口
                .rationale { shouldRequest ->
                    if (isRationale) {
                        if (isContact){
                            DialogHelper.showRationaleDialog("为了更好的进行运营商服务，需向您申请通讯录权限", shouldRequest)
                        }else{
                            DialogHelper.showRationaleDialog("请开启${tips}的权限~", shouldRequest)
                        }
                    }
                }
                .callback(object : PermissionUtils.FullCallback {
                    override fun onGranted(permissionsGranted: List<String>) {
                        grantedListener()
                    }

                    override fun onDenied(permissionsDeniedForever: List<String>, permissionsDenied: List<String>) {
                        if (permissionsDenied.isNotEmpty() && isRationale) {
                            DialogHelper.showOpenAppSettingDialog("拒绝${tips}可能导致某些功能无法体验,是否去系统设置中开启该权限")
                        }
                        context.toast("您已拒绝开启${tips}的权限")
                        deniedListener()
                    }
                })
                .request()
    }
}