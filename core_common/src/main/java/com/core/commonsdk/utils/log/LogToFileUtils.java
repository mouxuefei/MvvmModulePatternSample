package com.core.commonsdk.utils.log;

import android.content.Context;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;

/**
 * 将log 写入到本地文件工具
 */

public class LogToFileUtils {
    /**
     * 上下文对象
     */
    private static Context mContext;
    /**
     * FileLogUtils类的实例
     */
    private static LogToFileUtils instance;
    /**
     * 用于保存日志的文件
     */
    private static File logFile;

    /**
     * 日志的最大占用空间 - 单位：字节
     * <p>
     * 注意：为了性能，没有每次写入日志时判断，故日志在写入第二次初始化之前，不会受此变量限制，所以，请注意日志工具类的初始化时间
     * <p>
     * 为了衔接上文，日志超出设定大小后不会被直接删除，而是存储一个副本，所以实际占用空间是两份日志大小
     * <p>
     * 除了第一次超出大小后存为副本外，第二次及以后再次超出大小，则会覆盖副本文件，所以日志文件最多也只有两份
     * <p>
     * 默认10M
     */
    private static final int LOG_MAX_SIZE = 10 * 1024 * 1024;

    private static final String MY_TAG = "LogToFileUtils";

    private static String mLogPath = "/heartmed/";

    /**
     * 初始化日志库
     *
     * @param context
     */
    public static void init(Context context) {
        if (null == mContext || null == instance || null == logFile || !logFile.exists()) {
            mContext = context;
            instance = new LogToFileUtils();
            logFile = getLogFile();

            // 获取当前日志文件大小
            long logFileSize = getFileSize(logFile);
            // Log.d(MY_TAG, "Log max size is: " + Formatter.formatFileSize(context, LOG_MAX_SIZE));
            //Log.i(MY_TAG, "log now size is: " + Formatter.formatFileSize(context, logFileSize));
            // 若日志文件超出了预设大小，则重置日志文件
            if (LOG_MAX_SIZE < logFileSize) {
                resetLogFile();
            }
        } else {
            Log.i(MY_TAG, "LogToFileUtils has been init ...");
        }
    }

    /**
     * 写入日志文件的数据
     *
     * @param str 需要写入的数据
     */
    public static void write(Object str) {
        // 判断是否初始化或者初始化是否成功
        if (null == mContext || null == instance || null == logFile || !logFile.exists()) {
            return;
        }

        long logFileSize = getFileSize(logFile);
        if (LOG_MAX_SIZE < logFileSize) {
            resetLogFile();
            if (!logFile.exists())return;
        }

        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(logFile, true));
            bw.write(str.toString());
            bw.write("\r\n");
            bw.close();
        } catch (Exception e) {
            Log.e(MY_TAG, "Write failure !!! " + e.toString());
        }
    }

    /**
     * 重置日志文件
     * <p>
     * 若日志文件超过一定大小，则把日志改名为lastLog.txt，然后新日志继续写入日志文件
     * <p>
     * 每次仅保存一个上一份日志，日志文件最多有两份
     * <p/>
     */
    private static void resetLogFile() {
        // 创建lastLog.txt，若存在则删除
        File lastLogFile = new File(logFile.getParent() + "/yd_ble_last_logs.txt");
        if (lastLogFile.exists()) {
            lastLogFile.delete();
        }
        // 将日志文件重命名为 lastLog.txt
        logFile.renameTo(lastLogFile);
        // 新建日志文件
        try {
            logFile.createNewFile();
        } catch (Exception e) {
            Log.e(MY_TAG, "Create log file failure !!! " + e.toString());
        }
    }

    /**
     * 获取文件大小
     *
     * @param file 文件
     * @return
     */
    private static long getFileSize(File file) {
        if (file == null) return 0;
        long size = 0;
        if (file.exists()) {
            try {
                FileInputStream fis = new FileInputStream(file);
                size = fis.available();
            } catch (Exception e) {
                Log.e(MY_TAG, e.toString());
            }
        }
        return size;
    }

    /**
     * 获取APP日志文件
     *
     * @return APP日志文件
     */
    private static File getLogFile() {
        try {
            File file = new File(mLogPath);
            // 若目录不存在则创建目录
            if (!file.exists()) {
                file.mkdirs();
            }
            File logFile = new File(file.getPath() + "/heartmed_log.txt");
            if (!logFile.exists()) {
                logFile.createNewFile();
            }
            return logFile;
        } catch (Exception e) {
            Log.e(MY_TAG, "Create log file failure !!! " + e.toString());
            return null;
        }
    }

    public static void setLogPath(String logPath) {
        LogToFileUtils.mLogPath = logPath;
        Log.e(MY_TAG, "set log path !!! " + mLogPath);
    }
}
