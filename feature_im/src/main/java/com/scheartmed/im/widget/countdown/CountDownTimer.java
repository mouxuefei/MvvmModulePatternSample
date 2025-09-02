package com.scheartmed.im.widget.countdown;


import android.os.Handler;

import java.util.Timer;
import java.util.TimerTask;


public class CountDownTimer implements ITimerSupport {
    private Timer mTimer;

    private Handler mHandler;

    /**
     * 倒计时时间
     */
    private long mMillisInFuture;

    /**
     * 间隔时间
     */
    private long mCountDownInterval;
    /**
     * 倒计时剩余时间
     */
    private long mMillisUntilFinished;

    private OnTimerListener mOnCountDownTimerListener;

    private TimerState mTimerState = TimerState.FINISH;

    public CountDownTimer(long millisInFuture, long countDownInterval) {
        this.mMillisInFuture = millisInFuture;
        this.mMillisUntilFinished = mMillisInFuture;
        this.mCountDownInterval = countDownInterval;
        this.mHandler = new Handler();
    }

    @Override
    public void start() {
        //防止重复启动 重新启动要先reset再start
        if (mTimer == null && mTimerState != TimerState.START) {
            mTimer = new Timer();
            mTimer.scheduleAtFixedRate(createTimerTask(), 0, mCountDownInterval);
            mTimerState = TimerState.START;
        }
    }

    @Override
    public void pause() {
        if (mTimer != null && mTimerState == TimerState.START) {
            cancelTimer();
            mTimerState = TimerState.PAUSE;
        }
    }

    @Override
    public void resume() {
        if (mTimerState == TimerState.PAUSE) {
            start();
        }
    }

    @Override
    public void stop() {
        stopTimer(true);
    }

    @Override
    public void reset() {
        if (mTimer != null) {
            cancelTimer();
        }
        mMillisUntilFinished = mMillisInFuture;
        mTimerState = TimerState.FINISH;
    }

    private void stopTimer(final boolean cancel) {
        if (mTimer != null) {
            cancelTimer();
            mHandler.post(() -> {
                mTimerState = TimerState.FINISH;
                mMillisUntilFinished = mMillisInFuture;
                if (mOnCountDownTimerListener != null) {
                    if (cancel) {
                        mOnCountDownTimerListener.onCancel();
                    } else {
                        mOnCountDownTimerListener.onFinish();
                    }
                }
            });
        }
    }

    private void cancelTimer() {
        mTimer.cancel();
        mTimer.purge();
        mTimer = null;
    }

    public boolean isStart() {
        return mTimerState == TimerState.START;
    }

    public boolean isFinish() {
        return mTimerState == TimerState.FINISH;
    }

    public void setTimerListener(OnTimerListener listener) {
        this.mOnCountDownTimerListener = listener;
    }

    public long getMillisUntilFinished() {
        return mMillisUntilFinished;
    }

    public TimerState getTimerState() {
        return mTimerState;
    }

    protected TimerTask createTimerTask() {
        return new TimerTask() {
            private long startTime = -1;

            @Override
            public void run() {
                if (startTime < 0) {
                    //第一次回调 记录开始时间
                    startTime = scheduledExecutionTime() - (mMillisInFuture - mMillisUntilFinished);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (mOnCountDownTimerListener != null) {
                                mOnCountDownTimerListener.onTick(mMillisUntilFinished);
                            }
                        }
                    });
                } else {
                    //剩余时间
                    mMillisUntilFinished = mMillisInFuture - (scheduledExecutionTime() - startTime);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (mOnCountDownTimerListener != null) {
                                mOnCountDownTimerListener.onTick(mMillisUntilFinished);
                            }
                        }
                    });
                    if (mMillisUntilFinished <= 0) {
                        //如果没有剩余时间 就停止
                        stopTimer(false);
                    }
                }
            }
        };
    }

}

