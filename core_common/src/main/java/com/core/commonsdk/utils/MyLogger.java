package com.core.commonsdk.utils;

import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import com.core.basemvvm.BaseApplication;
import com.core.commonsdk.utils.log.YDLogCatFormatStrategy;
import com.core.commonsdk.utils.log.YDLoggerSave;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.DiskLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.LogcatLogStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.orhanobut.logger.Logger.DEBUG;
import static com.orhanobut.logger.Logger.ERROR;
import static com.orhanobut.logger.Logger.INFO;
import static com.orhanobut.logger.Logger.VERBOSE;
import static com.orhanobut.logger.Logger.WARN;

/**
 * Created by jzhao on 2016/4/20.
 */
public class MyLogger {
    private static boolean mLogFlag = false;
    private static boolean mLogcatFlag = true;
    private static final int MAX_BYTES = 5 * 1024 * 1024; // 每个文件最多5m
    private static final int MAX_FILE_COUNT = 3; // 最多保持3个文件
    public static String TAG = "villa";

    private static MyLogger mDingding = null;
    private String mLogFolderPath = null;
    private boolean mIsStarting = false;

    private MyLogger() {

    }


    /**
     * 通过设置tag获取logger实例
     *
     * @return logger实例
     */
    public static MyLogger ddLog(String tag) {
        if (mDingding == null) {
            mDingding = new MyLogger();
        }
        TAG = tag;
        return mDingding;
    }

    /**
     * 获取logger实例
     *
     * @return logger实例
     */
    public static MyLogger getLogger() {
        if (mDingding == null) {
            mDingding = new MyLogger();
        }
        return mDingding;
    }

    /**
     * 开启日志
     */
    public void start() {
        if (mIsStarting) {
            return;
        }

        mIsStarting = true;
        // 添加logcat日志
//        LogcatLogStrategy logcatStrategy = new LogcatLogStrategy();
//        FormatStrategy logCatFormatStrategy = YDLogCatFormatStrategy.newBuilder().logStrategy(logcatStrategy).tag(TAG).build();
        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder().showThreadInfo(false).methodCount(0).methodOffset(7).tag("villa").build();
        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy) {
            @Override
            public boolean isLoggable(int priority, String tag) {
                return mLogcatFlag;
            }
        });
    }

    /**
     * 关闭日志
     */
    public void stop() {
        mIsStarting = false;
        Logger.clearLogAdapters();
    }

    /**
     * 清除日志
     */
    public void clear() {
        // TODO 未针对用户
        stop();

        String diskPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        // 日志文件路径
        String logFolderPath = diskPath + File.separatorChar + "YDASDKLog";
        File folder = new File(logFolderPath);
        if (folder.exists()) {
            File[] fileArray = folder.listFiles();
            if (fileArray == null) return;
            for (File f : fileArray) {
                f.delete();
            }
        }
    }

    /**
     * 获取日志文件
     *
     * @return 日志文件列表
     */
    public List<File> getLogFilePaths() {
        if (mLogFolderPath != null) {
            File folder = new File(mLogFolderPath);
            if (folder.exists()) {
                File[] fileArray = folder.listFiles();
                if (fileArray == null) return null;
                List<File> fileList = new ArrayList<>();
                for (File f : fileArray) {
                    fileList.add(f);
                }
                return fileList;
            }
        }
        return null;
    }


    public String getLogFolderPath() {
        return mLogFolderPath;
    }

    public void enableLog(boolean enable) {
        mLogFlag = enable;
    }

    public void enableLogcat(boolean enable) {
        mLogcatFlag = enable;
    }

    /**
     * The Log Level:i
     *
     * @param str
     */
    public void i(Object str) {
        Logger.log(INFO, TAG, str.toString(), null);
        YDLoggerSave.ddLog().i(getFunctionName() + " - " + str.toString());
    }

    /**
     * The Log Level:d
     *
     * @param str
     */
    public void d(Object str) {
        Logger.log(DEBUG, TAG, str.toString(), null);
        YDLoggerSave.ddLog().d(getFunctionName() + " - " + str.toString());
    }

    /**
     * The Log Level:V
     *
     * @param str
     */
    public void v(Object str) {
        Logger.log(VERBOSE, TAG, str.toString(), null);
        YDLoggerSave.ddLog().v(getFunctionName() + " - " + str.toString());
    }

    /**
     * The Log Level:w
     *
     * @param str
     */
    public void w(Object str) {
        Logger.log(WARN, TAG, str.toString(), null);
        YDLoggerSave.ddLog().w(getFunctionName() + " - " + str.toString());
    }

    /**
     * The Log Level:e
     *
     * @param str
     */
    public void e(Object str) {
        Logger.log(ERROR, TAG, str.toString(), null);
        YDLoggerSave.ddLog().e(getFunctionName() + " - " + str.toString());
    }

    /**
     * The Log Level:e
     *
     * @param ex
     */
    public void e(Exception ex) {
        Logger.log(ERROR, TAG, ex.toString(), null);
        YDLoggerSave.ddLog().e(getFunctionName() + " - " + ex.toString());
    }

    /**
     * The Log Level:e
     *
     * @param log
     * @param tr
     */
    public void e(String log, Throwable tr) {
        Logger.log(ERROR, TAG, log, tr);
        YDLoggerSave.ddLog().e(getFunctionName() + " - " + log);
    }

    private String getFunctionName() {
        StackTraceElement[] sts = Thread.currentThread().getStackTrace();
        if (sts == null) {
            return null;
        }

        for (StackTraceElement st : sts) {
            if (st.isNativeMethod()) {
                continue;
            }
            if (st.getClassName().equals(Thread.class.getName())) {
                continue;
            }
            if (st.getClassName().equals(this.getClass().getName())) {
                continue;
            }

            return "[线程：" + Thread.currentThread().getName() + "  文件名：" + st.getFileName() + " 行号：" + st.getLineNumber() + " 方法名：" + st.getMethodName() + " ]";
        }

        return null;
    }
}
