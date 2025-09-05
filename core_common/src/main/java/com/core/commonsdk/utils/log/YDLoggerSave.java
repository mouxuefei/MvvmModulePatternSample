package com.core.commonsdk.utils.log;

import android.content.Context;
import android.util.Log;

import java.text.SimpleDateFormat;

public class YDLoggerSave {

    private static SimpleDateFormat logSDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS ");
    private boolean mSaveLogs = false;
    private static YDLoggerSave mYDLoggerSave;

    public static YDLoggerSave ddLog() {
        if (mYDLoggerSave == null) {
            mYDLoggerSave = new YDLoggerSave();
        }
        return mYDLoggerSave;
    }

    public void v(String message) {
        if (mSaveLogs) ThreadPrintsLog.add(getSystemCurrentTime() + "V " + message);
    }

    public void d(String message) {
        if (mSaveLogs) ThreadPrintsLog.add(getSystemCurrentTime() + "D " + message);
    }

    public void i(String message) {
        if (mSaveLogs) ThreadPrintsLog.add(getSystemCurrentTime() + "I " + message);
    }

    public void w(String message) {
        if (mSaveLogs) ThreadPrintsLog.add(getSystemCurrentTime() + "W " + message);
    }

    public void e(String message) {
        if (mSaveLogs) ThreadPrintsLog.add(getSystemCurrentTime() + "E " + message);
    }

    private String getSystemCurrentTime() {
        return logSDF.format(new java.util.Date());
    }

    public void writeToFileEnable(Context context) {
        LogToFileUtils.init(context);
        ThreadPrintsLog.setThreadRun(true);
        ThreadPrintsLog.startSaveDBThread();
        mSaveLogs = true;
    }

    public void setLogSavePath(String path) {
        LogToFileUtils.setLogPath(path);
    }
}
