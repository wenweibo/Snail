package com.fxkj.publicframework.tool.RecordVoice;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.PowerManager;
import android.os.SystemClock;
import android.text.format.Time;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.czt.mp3recorder.MP3Recorder;
import com.fxkj.publicframework.R;
import com.fxkj.publicframework.globalvariable.Constant;

import java.io.File;
import java.util.Date;


/**
 * Voice recorder view
 */
public class VoiceRecorderMp3 {
    protected Context context;

    protected PowerManager.WakeLock wakeLock;
    protected ImageView micImage;
    protected MP3Recorder mRecorder;
    static final String EXTENSION = ".mp3";
    private long startTime;
    private File voiceFile;
    private String voiceFileName;
    private String voiceFilePath;

    public VoiceRecorderMp3(Context context) {
        init(context);
    }

    private void init(Context context) {
        this.context = context;

        voiceFileName = getVoiceFileName("voice");
        voiceFile = new File(Constant.project_root_folder, voiceFileName);
        voiceFilePath = voiceFile.getAbsolutePath();
        mRecorder = new MP3Recorder(voiceFile);

        wakeLock = ((PowerManager) context.getSystemService(Context.POWER_SERVICE)).newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "demo");
    }

    private String getVoiceFileName(String uid) {
        Time now = new Time();
        now.setToNow();
        return uid + now.toString().substring(0, 15) + EXTENSION;
    }


    public void startRecording() {
        startTime = new Date().getTime();
        if (!android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            Toast.makeText(context, "发送语音需要sdcard支持", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            wakeLock.acquire();
            mRecorder.start();
        } catch (Exception e) {
            e.printStackTrace();
            if (wakeLock.isHeld())
                wakeLock.release();
            if (mRecorder != null)
                mRecorder.stop();
            Toast.makeText(context, "录音失败，请重试！", Toast.LENGTH_SHORT).show();
            return;
        }
    }


    public void discardRecording() {
        if (wakeLock.isHeld())
            wakeLock.release();
        try {
            // stop recording
            if (mRecorder.isRecording()) {
                mRecorder.stop();
            }
        } catch (Exception e) {
        }
    }

    public int stopRecoding() {
        if (wakeLock.isHeld())
            wakeLock.release();
        mRecorder.stop();
        int seconds = (int) (new Date().getTime() - startTime) / 1000;
        return seconds;
    }

    public String getVoiceFilePath() {
        return voiceFilePath;
    }

    public String getVoiceFileName() {
        return voiceFileName;
    }

    public boolean isRecording() {
        return mRecorder.isRecording();
    }

}