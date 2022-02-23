package com.mou.mvvmmodule.util;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;

import java.io.File;
import java.util.List;

import androidx.core.content.FileProvider;


/**
 * @version V1.0
 * @FileName: AppUtil.java
 * @author: villa_mou
 * @date: 02-13:48
 * @desc: utils about app
 */
public final class AppUtils {


    private static final int M_BYTE = 1024 * 1024;

    /**
     * 获取app的名称
     */
    public static String getAppName(Application app) {
        if (app == null) return "";
        return getAppName(app, app.getPackageName());
    }

    /**
     * 通过包名获取app名称
     */
    private static String getAppName(Application app, final String packageName) {
        if (TextUtils.isEmpty(packageName) || app == null) {
            return "";
        }
        try {
            PackageManager pm = app.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(packageName, 0);
            return pi == null ? "" : pi.applicationInfo.loadLabel(pm).toString();
        } catch (PackageManager.NameNotFoundException e) {
            return "";
        }
    }


    /**
     * 获取app的icon
     */
    public static Drawable getAppIcon(Application app) {
        if (app == null) return null;
        PackageManager pm = app.getPackageManager();
        String packageName = app.getPackageName();
        if (pm == null || TextUtils.isEmpty(packageName)) return null;
        try {
            PackageInfo packageInfo = pm.getPackageInfo(packageName, 0);
            ApplicationInfo ai = packageInfo.applicationInfo;
            if (ai == null) return null;
            return ai.loadIcon(pm);
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }


    /**
     * 获取app版本名称
     */
    public static String getAppVersionName(Application app) {
        if (app == null) return "";
        String packageName = app.getPackageName();
        if (TextUtils.isEmpty(packageName)) return "";
        try {
            PackageManager pm = app.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(packageName, 0);
            return pi == null ? "" : pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return "";
        }
    }

    /**
     * 获取app版本号
     */
    public static int getAppVersionCode(Application app) {
        if (app == null) return -1;
        String packageName = app.getPackageName();
        if (TextUtils.isEmpty(packageName)) return -1;
        try {
            PackageManager pm = app.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(packageName, 0);
            return pi == null ? -1 : pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            return -1;
        }
    }

    /**
     * 获取app的包名
     */
    public static String getAppPackageName(Application app) {
        if (app == null) return "";
        return app.getPackageName();
    }

    /**
     * 获取app的最大内存
     */
    public static long getMaxMemory() {
        Runtime r = Runtime.getRuntime();
        return r != null ? (r.maxMemory() / M_BYTE) : -1;
    }

    /**
     * 获取app的剩余未使用的内存值
     */
    public static long getUselessMemory() {
        Runtime r = Runtime.getRuntime();
        return (r.maxMemory() - (r.totalMemory() - r.freeMemory())) / M_BYTE;
    }

    /**
     * 根据文件地址安装app
     */
    public static void installApp(Application app, final String filePath) {
        installApp(app, getFileByPath(filePath));
    }


    /**
     * 根据文件安装app
     */
    public static void installApp(Application app, final File file) {
        Intent installAppIntent = getInstallAppIntent(app,file);
        if (installAppIntent == null || app == null) return;
        app.startActivity(installAppIntent);
    }


    /**
     * 根据文件uri安装api
     */
    public static void installApp(Application app, final Uri uri) {
        Intent installAppIntent = getInstallAppIntent(uri);
        if (installAppIntent == null || app == null) return;
        app.startActivity(installAppIntent);
    }

    /**
     * 根据包名卸载app
     */
    public static void uninstallApp(Application app, final String packageName) {
        if (isSpace(packageName) || app == null) return;
        app.startActivity(getUninstallAppIntent(packageName));
    }


    /**
     * 根据包名判断app是否安装
     */
    public static boolean isAppInstalled(Application app, final String pkgName) {
        if (isSpace(pkgName) || app == null) return false;
        PackageManager pm = app.getPackageManager();
        try {
            return pm.getApplicationInfo(pkgName, 0).enabled;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    /**
     * 判断app是否处于后台
     */
    public static boolean isAppBackground(Application context) {
        //TODO:有待验证，用lifecycler比较好
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                return appProcess.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND;
            }
        }
        return false;
    }

    /**
     * 根据文件路径创建文件
     */
    private static File getFileByPath(final String filePath) {
        return isSpace(filePath) ? null : new File(filePath);
    }

    private static boolean isSpace(final String s) {
        if (s == null) return true;
        for (int i = 0, len = s.length(); i < len; ++i) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }


    private static Intent getInstallAppIntent(Application app, final File file) {
        if (!isFileExists(file) || app == null) return null;
        Uri uri;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            uri = Uri.fromFile(file);
        } else {
            //TODO: 需要和清单文件fileprovider的authority保持一次
            String authority = app.getPackageName() + ".fileprovider";
            uri = FileProvider.getUriForFile(app, authority, file);
        }
        return getInstallAppIntent(uri);
    }

    public static Intent getInstallAppIntent(final Uri uri) {
        if (uri == null) return null;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        String type = "application/vnd.android.package-archive";
        intent.setDataAndType(uri, type);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        return intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    public static boolean isFileExists(final File file) {
        return file != null && file.exists();
    }

    private static Intent getUninstallAppIntent(final String pkgName) {
        Intent intent = new Intent(Intent.ACTION_DELETE);
        intent.setData(Uri.parse("package:" + pkgName));
        return intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    }

}


