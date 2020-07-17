package com.example.finmins.materialtest.util;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by hhhao on 2020/2/15.
 */
public class AudioRecorder {
    public final static int VOICE_VOLUME = 1;
    public final static int RECORD_STOP = 2;
    private File pcmFile;
    private File handlerWavFile;
    private static final String TAG = "AudioRecorder";
    private String name;
    private static int sampleRateInHz = 8000;
    private static int channelConfig = AudioFormat.CHANNEL_IN_DEFAULT;
    private static int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
    private static AudioRecorder recorder;
    private Integer bufferSize;
//    private int bufferSize;
    private AudioRecord record;
    private boolean on;
    private AudioRecorder() {
        on = false;
        record = null;
        bufferSize = AudioRecord.getMinBufferSize(sampleRateInHz, channelConfig, audioFormat);
    }
    private void addHeadData(){
        pcmFile = new File("/mnt/sdcard",name+".pcm");
        handlerWavFile = new File("/mnt/sdcard",name+".wav");
        PcmToWavUtil pcmToWavUtil = new PcmToWavUtil(8000,AudioFormat.CHANNEL_IN_MONO,AudioFormat.ENCODING_PCM_16BIT);
        pcmToWavUtil.pcmToWav(pcmFile.toString(),handlerWavFile.toString());
    }
    private Context mcontext;
    public AudioRecorder (Context context){
        this.mcontext=context;
    }
    /**
     * singleton
     * @return an AudioRecorder object
     */
    public static AudioRecorder getInstance() {
        if (recorder == null)
            recorder = new AudioRecorder();
        return recorder;
    }

    /**
     * start recorder
     * @param handler a handler to send back message for ui changing
     */
    public void start(final Handler handler,String str) {
        if (record != null || on)
            return;
        record = new AudioRecord(MediaRecorder.AudioSource.MIC,
                sampleRateInHz,
                channelConfig,
                audioFormat,
                bufferSize);

        if (record.getState() == AudioRecord.STATE_UNINITIALIZED) {
            Log.e(TAG, "start: record state uninitialized");
            return;
        }
        name = str;
        pcmFile = new File("/mnt/sdcard",name+".pcm");

        new Thread(new Runnable() {
            @Override
            public void run() {
                record.startRecording();
                on = true;
                FileOutputStream fileOutputStream = null;
                try {
                    fileOutputStream = new FileOutputStream(pcmFile);
                while (on) {
                    byte[] buffer = new byte[bufferSize];
//                    short[] buffer = new short[bufferSize];
                    int sz = record.read(buffer, 0, bufferSize);
                    fileOutputStream.write(buffer);
                    fileOutputStream.flush();
                    long v = 0;

                    for (int tmp : buffer)
                        v += tmp * tmp;
                    double vol = 10 * Math.log10(v / (double) sz);

                    Message message = new Message();
                    message.what = VOICE_VOLUME;
                    message.arg1 = (int) (vol * 100 + 3000);
                    handler.sendMessage(message);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                record.stop();
                fileOutputStream.flush();
                fileOutputStream.close();
                addHeadData();
                record.release();
                record = null;

                Message message = new Message();
                message.what = RECORD_STOP;
                handler.sendMessage(message);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    record.stop();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
    /**
     * stop recording
     */
    public void stop() {
        on = false;

    }

}

