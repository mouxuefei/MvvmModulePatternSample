package com.mou.mvvmmodule.util

import android.app.ActivityManager
import android.app.ActivityManager.RunningAppProcessInfo
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.text.TextUtils
import androidx.core.content.FileProvider
import java.io.File

/**
 * @FileName: AppUtils.java
 * @author: villa_mou
 * @date: 02-15:35
 * @version V1.0 <描述当前版本功能>
 * @desc
 */
object KtAppUtils {

    const val M_BYTE = 1024 * 1024

    fun getAppName(app: Application?): String? {
        return app?.let {
            getAppName(it, it.packageName)
        }
    }

    /**
     * 通过包名获取app名称
     */
    private fun getAppName(app: Application?, packageName: String): String {
        return if (TextUtils.isEmpty(packageName) || app == null) {
            ""
        } else try {
            val pm = app.packageManager
            val pi = pm.getPackageInfo(packageName, 0)
            pi?.applicationInfo?.loadLabel(pm)?.toString() ?: ""
        } catch (e: PackageManager.NameNotFoundException) {
            ""
        }
    }

    /**
     * 获取app的icon
     */
    fun getAppIcon(app: Application?): Drawable? {
        return app?.let {
            val pm = it.packageManager
            val packageName = it.packageName
            return if (pm == null || TextUtils.isEmpty(packageName)) null else try {
                val packageInfo = pm.getPackageInfo(packageName, 0)
                val ai = packageInfo.applicationInfo ?: return null
                ai.loadIcon(pm)
            } catch (e: PackageManager.NameNotFoundException) {
                null
            }
        }

    }

    /**
     * 获取app版本名称
     */
    fun getAppVersionName(app: Application?): String? {
        return app?.let {
            val packageName = it.packageName
            return if (TextUtils.isEmpty(packageName)) "" else try {
                val pm = it.packageManager
                val pi = pm.getPackageInfo(packageName, 0)
                if (pi == null) "" else pi.versionName
            } catch (e: PackageManager.NameNotFoundException) {
                ""
            }
        }

    }

    /**
     * 获取app版本号
     */
    fun getAppVersionCode(app: Application?): Int? {
        return app?.let {
            val packageName = it.packageName
            return if (TextUtils.isEmpty(packageName)) -1 else try {
                val pm = it.packageManager
                val pi = pm.getPackageInfo(packageName, 0)
                pi?.versionCode ?: -1
            } catch (e: PackageManager.NameNotFoundException) {
                -1
            }
        }

    }

    /**
     * 获取app的包名
     */
    fun getAppPackageName(app: Application?): String {
        return app?.packageName ?: ""
    }

    /**
     * 获取app的最大内存
     */
    fun getMaxMemory(): Long {
        val r = Runtime.getRuntime()
        return if (r != null) r.maxMemory() / M_BYTE else -1
    }

    /**
     * 获取app的剩余未使用的内存值
     */
    fun getUselessMemory(): Long {
        val r = Runtime.getRuntime()
        return if (r != null) (r.maxMemory() - (r.totalMemory() - r.freeMemory())) / M_BYTE else -1
    }

    /**
     * 根据文件地址安装app
     */
    fun installApp(app: Application?, filePath: String) {
        installApp(app, getFileByPath(filePath))
    }


    /**
     * 根据文件安装app
     */
    fun installApp(app: Application?, file: File?) {
        val installAppIntent = getInstallAppIntent(app, file)
        if (installAppIntent == null || app == null) return
        app.startActivity(installAppIntent)
    }


    /**
     * 根据文件uri安装api
     */
    fun installApp(app: Application?, uri: Uri?) {
        val installAppIntent = getInstallAppIntent(uri)
        if (installAppIntent == null || app == null) return
        app.startActivity(installAppIntent)
    }

    /**
     * 根据包名卸载app
     */
    fun uninstallApp(app: Application?, packageName: String) {
        if (isSpace(packageName) || app == null) return
        app.startActivity(getUninstallAppIntent(packageName))
    }


    /**
     * 根据包名判断app是否安装
     */
    fun isAppInstalled(app: Application?, pkgName: String?): Boolean {
        if (isSpace(pkgName) || app == null) return false
        val pm = app.packageManager
        return try {
            pm.getApplicationInfo(pkgName, 0).enabled
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    /**
     * 判断app是否处于后台
     */
    fun isAppBackground(context: Application): Boolean {
        //TODO:有待验证，用lifecycler比较好
        val activityManager = context
            .getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val appProcesses = activityManager
            .runningAppProcesses
        for (appProcess in appProcesses) {
            if (appProcess.processName == context.packageName) {
                return appProcess.importance != RunningAppProcessInfo.IMPORTANCE_FOREGROUND
            }
        }
        return false
    }

    /**
     * 根据文件路径创建文件
     */
    private fun getFileByPath(filePath: String): File? {
        return if (isSpace(filePath)) null else File(filePath)
    }

    private fun isSpace(s: String?): Boolean {
        if (s == null) return true
        var i = 0
        val len = s.length
        while (i < len) {
            if (!Character.isWhitespace(s[i])) {
                return false
            }
            ++i
        }
        return true
    }


    private fun getInstallAppIntent(app: Application?, file: File?): Intent? {
        if (!isFileExists(file) || app == null) return null
        val uri: Uri = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            Uri.fromFile(file)
        } else {
            //TODO: 需要和清单文件fileprovider的authority保持一次
            val authority = app.packageName + ".fileprovider"
            FileProvider.getUriForFile(app, authority, file!!)
        }
        return getInstallAppIntent(uri)
    }

    private fun getInstallAppIntent(uri: Uri?): Intent? {
        if (uri == null) return null
        val intent = Intent(Intent.ACTION_VIEW)
        val type = "application/vnd.android.package-archive"
        intent.setDataAndType(uri, type)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        }
        return intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }

    private fun isFileExists(file: File?): Boolean {
        return file != null && file.exists()
    }

    private fun getUninstallAppIntent(pkgName: String): Intent? {
        val intent = Intent(Intent.ACTION_DELETE)
        intent.data = Uri.parse("package:$pkgName")
        return intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
}