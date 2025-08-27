package com.scheartmed.lib_im.widget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestOptions;
import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.XXPermissions;
import com.hjq.permissions.permission.PermissionLists;
import com.orhanobut.logger.Logger;
import com.scheartmed.lib_im.R;
import com.scheartmed.lib_im.utils.FileUtils;
import com.scheartmed.lib_im.widget.countdown.IntervalTimer;


import java.io.File;

import androidx.appcompat.widget.AppCompatButton;

public class RecordButton extends AppCompatButton {

    private String mFile = FileUtils.INSTANCE.getVoiceCachePath() + "voice_" + System.currentTimeMillis() + ".mp3";

    private OnFinishedRecordListener finishedListener;
    /**
     * 最短录音时间
     **/
    private int MIN_INTERVAL_TIME = 1000;
    /**
     * 最长录音时间
     **/
    private int MAX_INTERVAL_TIME = 1000 * 60;

    private View mDialogContainer;

    private TextView mStateTV;
    private TextView mStateTime;

    private ImageView mStateGif;
    private ImageView mStateTanahao;

    private MediaRecorder mRecorder;

    private float y;

    private static long startTime;
    private Dialog recordDialog;
    private static final String BUTTON_NORMAL = "松开发送 上滑取消";
    private static final String BUTTON_CANCEL = "松开手指 取消发送";
    private GifDrawable mDrawableGif;
    private IntervalTimer mRecordTimer;

    public RecordButton(Context context) {
        super(context);
        init();
    }

    public RecordButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public RecordButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void setOnFinishedRecordListener(OnFinishedRecordListener listener) {
        finishedListener = listener;
    }

    @SuppressLint("HandlerLeak")
    private void init() {

    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        XXPermissions.with(getContext()).permission(PermissionLists.getRecordAudioPermission()).request((grantedList, deniedList) -> {
            boolean allGranted = deniedList.isEmpty();
            if (!allGranted) {
                // 判断请求失败的权限是否被用户勾选了不再询问的选项
                boolean doNotAskAgain = XXPermissions.isDoNotAskAgainPermissions((Activity) getContext(), deniedList);
                // 在这里处理权限请求失败的逻辑
                //TODO:
                return;
            }
            onTouchButton(event);
        });
        return true;
    }


    private void onTouchButton(MotionEvent event) {
        int action = event.getAction();
        y = event.getY();
        if (mStateTV != null && mStateGif != null && y < 0) {
            mStateTV.setText("松开手指,取消发送");
            setText("松开手指,取消发送");
            mDialogContainer.setBackgroundResource(R.drawable.shape_record_warning);
        } else if (mStateTV != null) {
            mStateTV.setText("手指上滑,取消发送");
        }
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                setBackgroundResource(R.drawable.shape_session_btn_voice_press);
                setText("松开 发送");
                initDialogAndStartRecord();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                setBackgroundResource(R.drawable.shape_session_btn_voice_normal);
                this.setText("按住 说话");
                if (y >= 0 && (System.currentTimeMillis() - startTime <= MAX_INTERVAL_TIME)) {
                    Logger.d("结束录音:");
                    finishRecord();
                } else if (y < 0) {  //当手指向上滑，会cancel
                    cancelRecord();
                }
                break;
        }
    }

    /**
     * 初始化录音对话框 并 开始录音
     */
    private void initDialogAndStartRecord() {
        initTimer();
        recordDialog = new Dialog(getContext(), R.style.like_toast_dialog_style);
        View view = View.inflate(getContext(), R.layout.dialog_record, null);
        mDialogContainer = view.findViewById(R.id.rc_audio_state);
        mStateGif = (ImageView) view.findViewById(R.id.rc_audio_state_image_gif);
        mStateTanahao = (ImageView) view.findViewById(R.id.rc_audio_state_image_tanhan);
        mStateTV = (TextView) view.findViewById(R.id.rc_audio_state_text);
        mStateTime = (TextView) view.findViewById(R.id.rc_audio_state_time);
        RequestOptions options = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        Glide.with(getContext()).load(R.drawable.recording).apply(options).into(mStateGif);
        mDrawableGif = (GifDrawable) mStateGif.getDrawable();

        setImageVisible(true, false);
        mStateTV.setVisibility(View.VISIBLE);
        mStateTime.setVisibility(View.VISIBLE);
        mStateTV.setText("手指上滑,取消发送");
        recordDialog.setContentView(view, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        recordDialog.setOnDismissListener(onDismiss);
        WindowManager.LayoutParams lp = recordDialog.getWindow().getAttributes();
        lp.gravity = Gravity.CENTER;
        startRecording();
        recordDialog.show();
    }

    public void setImageVisible(boolean gif, boolean tanhan) {
        mStateGif.setVisibility(gif ? View.VISIBLE : View.GONE);
        mStateTanahao.setVisibility(tanhan ? View.VISIBLE : View.GONE);
    }

    private void initTimer() {
        startTime = System.currentTimeMillis();
    }

    /**
     * 放开手指，结束录音处理
     */
    private void finishRecord() {
        long intervalTime = System.currentTimeMillis() - startTime;
        if (intervalTime < MIN_INTERVAL_TIME) {
            mDialogContainer.setBackgroundResource(R.drawable.shape_recording_cancel);
            setImageVisible(false, true);
            mStateTime.setVisibility(View.GONE);
            mStateTV.setText("时间不足");
            if (mDrawableGif.isRunning()) {
                mDrawableGif.stop();
            }
            new Handler().postDelayed(() -> {
                stopRecording();
                recordDialog.dismiss();
            }, 500);
            File file = new File(mFile);
            file.delete();
        /*    stopRecording();
            recordDialog.dismiss();*/
            return;
        } else {
            stopRecording();
            recordDialog.dismiss();
        }
        Logger.d("录音完成的路径:" + mFile);
        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(mFile);
            mediaPlayer.prepare();
            mediaPlayer.getDuration();
            Logger.d("获取到的时长:" + mediaPlayer.getDuration() / 1000);
        } catch (Exception e) {

        }

        if (finishedListener != null)
            finishedListener.onFinishedRecord(mFile, mediaPlayer.getDuration() / 1000);

    }

    /**
     * 取消录音对话框和停止录音
     */
    public void cancelRecord() {
        stopRecording();
        recordDialog.dismiss();
        File file = new File(mFile);
        file.delete();
    }

    //获取类的实例
    // ExtAudioRecorder extAudioRecorder; //压缩的录音（WAV）

    /**
     * 执行录音操作
     */
    //int num = 0 ;
    private void startRecording() {
        if (mRecorder != null) {
            mRecorder.reset();
        } else {
            mRecorder = new MediaRecorder();
        }
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        File file = new File(mFile);
        Logger.d("创建文件的路径:" + mFile);
        Logger.d("文件创建成功:" + file.exists());
        mRecorder.setOutputFile(mFile);
        try {
            mRecorder.prepare();
            mRecorder.start();
            setTimerStart();
        } catch (Exception e) {
            Logger.d("preparestart异常,重新开始录音:" + e.toString());
            e.printStackTrace();
            mRecorder.release();
            mRecorder = null;
            startRecording();
        }

    }

    private void setTimerStart() {
        if (mRecordTimer != null) {
            mRecordTimer.cancel();
        }
        mRecordTimer = new IntervalTimer(new IntervalTimer.OnTimerInter() {
            @Override
            public void interval(long time) {
                if (time < 10 * 1000) {
                    mStateTime.setText("00:0" + time / 1000);
                } else {
                    mStateTime.setText("00:" + time / 1000);
                }
            }

            @Override
            public void cancel() {

            }
        });
        mRecordTimer.start();
    }


    private void stopRecording() {
        if (mRecorder != null) {
            try {
                mRecorder.stop();//停止时没有prepare，就会报stop failed
                mRecorder.reset();
                mRecorder.release();
                mRecorder = null;
            } catch (RuntimeException pE) {
                pE.printStackTrace();
            } finally {
                if (recordDialog.isShowing()) {
                    recordDialog.dismiss();
                }
            }
        }
        if (mRecordTimer != null) {
            mRecordTimer.cancel();
        }
    }


    private DialogInterface.OnDismissListener onDismiss = new DialogInterface.OnDismissListener() {
        @Override
        public void onDismiss(DialogInterface dialog) {
            stopRecording();
        }
    };

    public interface OnFinishedRecordListener {
        public void onFinishedRecord(String audioPath, int time);
    }


}
