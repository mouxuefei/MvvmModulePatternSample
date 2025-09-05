package com.core.commonsdk.utils.log;


import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;


/**
 * 操作日志保存处理
 */
public class ThreadPrintsLog {


    private static boolean threadRun = true; // 线程是否运行

    /**
     * 队列
     */
    private static Queue<String> queue = new ConcurrentLinkedQueue<String>();


    public static void setThreadRun(boolean threadRun) {
        ThreadPrintsLog.threadRun = threadRun;
    }

    /**
     * 向队列中增加SysLog对象
     *
     * @param syslog
     */
    public static void add(String syslog) {
        queue.offer(syslog);
    }

    /**
     * 获取SysLog对象
     *
     * @return
     */
    public static String getSysLog() {
        return queue.poll();

    }

    /**
     * 启动入库线程
     */
    public static void startSaveDBThread() {
        try {
            Thread insertDbThread = new Thread(new Runnable() {
                public void run() {
                    String sysLog = null;
                    while (threadRun) {
                        try {
                            sysLog = getSysLog();
                            if (null == sysLog) {
                                Thread.sleep(200);
                            } else {
//                                LogToFileUtils.write(DESUtil.encrypt(sysLog));
                                LogToFileUtils.write(sysLog);
                            }
                        } catch (Exception e) {
                            if (null != sysLog) {
                                sysLog = null;
                            }
                            e.printStackTrace();
                        }
                    }
                }
            });
            insertDbThread.start();
        } catch (Exception e) {
            throw new RuntimeException("ThreadSysLog new Thread Exception");
        }
    }

}