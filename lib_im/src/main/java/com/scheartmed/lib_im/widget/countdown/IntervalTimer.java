package com.scheartmed.lib_im.widget.countdown;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.util.Timer;
import java.util.TimerTask;

public class IntervalTimer {

    private int interval = 1000;//设置间隔时间
    private OnTimerInter chjTimerInter; //回调
    private Timer timer; // 定时器
    private static final int WHAT_REFREH = 0;//刷新
    private long startTime;

    /**
     * 创建对象则开始计时
     *
     * @param chjTimerInter 接口回调
     */
    public IntervalTimer(OnTimerInter chjTimerInter) {
        this.chjTimerInter = chjTimerInter;
    }

    /**
     * 创建对象开始计时
     *
     * @param interval      间隔时间通知(使用第一个方法，默认1秒钟刷新一次)
     * @param chjTimerInter 接口回调
     */
    public IntervalTimer(int interval, OnTimerInter chjTimerInter) {
        this.chjTimerInter = chjTimerInter;
        this.interval = interval;
    }

    /**
     * 开始计时
     */
    public void start() {
        if (timer == null) {
            timer = new Timer();
        } else {
            cancel();
            return;
        }
        this.startTime = System.currentTimeMillis();
        interval();
    }


    private void interval() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                timesss.sendMessage(new Message());
            }
        }, interval);
    }


    /**
     * 终止计时
     */
    public void cancel() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        timesss.removeCallbacksAndMessages(null);
        if (chjTimerInter != null) chjTimerInter.cancel();
    }

    private final Handler timesss = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what != WHAT_REFREH) return;
            long currentTime = System.currentTimeMillis();
            long count = (currentTime - startTime) / interval;
            if (chjTimerInter != null) chjTimerInter.interval(count * interval);
            interval();
        }
    };

    /**
     * 接口
     */
    public interface OnTimerInter {

        /**
         * 间隔时间内回调
         */
        void interval(long time);

        /**
         * 终止计时
         */
        void cancel();

    }
}
