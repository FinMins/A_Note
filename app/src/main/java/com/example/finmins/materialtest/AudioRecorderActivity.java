package com.example.finmins.materialtest;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.Message;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;



import com.example.finmins.materialtest.util.AudioRecorder;
import com.example.finmins.materialtest.util.PcmToWavUtil;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class AudioRecorderActivity extends AppCompatActivity {
    private static final String TAG = "AudioRecorderActivity";
    private ImageButton btn;
    private Drawable drawable;
    private boolean recording = false;
    private Button button;
    public final static int VOICE_VOLUME = 1;
    public final static int RECORD_STOP = 2;
    private File pcmFile;
    private File handlerWavFile;
    private static int sampleRateInHz = 9000;
    private String soundRecoderUriToString    ; //录音路径
    private static int channelConfig = AudioFormat.CHANNEL_IN_DEFAULT;
    private static int audioFormat = AudioFormat.ENCODING_PCM_16BIT;

    private  String name;
    String[] address = new String[50];



    private Integer bufferSize;
    //    private int bufferSize;
    private AudioRecord record=null;
    private boolean on=false;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
//    private GoogleApiClient client;

    private void addHeadData() {
        pcmFile = new File(getExternalCacheDir(), name + ".pcm");
        handlerWavFile = new File(getExternalCacheDir(), name + ".wav");
        PcmToWavUtil pcmToWavUtil = new PcmToWavUtil(8000, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
        pcmToWavUtil.pcmToWav(pcmFile.toString(), handlerWavFile.toString());
    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case AudioRecorder.VOICE_VOLUME:
                    drawable.setLevel(msg.arg1);
                    break;
                case AudioRecorder.RECORD_STOP:
                    drawable.setLevel(0);
                    break;
                default:
                    break;
            }
            return true;
        }
    });
    public static AudioRecorderActivity instance=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_recorder);
        btn = (ImageButton)findViewById(R.id.fanhui);
       name =String.valueOf( Calendar.getInstance().get(Calendar.DAY_OF_MONTH))+String.valueOf(Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) +
               String.valueOf(Calendar.getInstance().get(Calendar.MINUTE) )+
               String.valueOf(Calendar.getInstance().get(Calendar.SECOND));
        btn.setOnClickListener(new  View.OnClickListener(){
            public void onClick(View v){
                finish();
            }
        });
     /**
          final EditText et = new EditText(AudioRecorderActivity.this);
        new AlertDialog.Builder(AudioRecorderActivity.this).setTitle("请输入备注")
                .setIcon(android.R.drawable.sym_def_app_icon)
                .setCancelable(false)
                .setView(et)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //按下确定键后的事件
                        name = et.getText().toString();
                        soundRecoderUriToString = getExternalCacheDir()+"/"+name + ".wav";

                    }
                }).setNegativeButton("取消", null).show();
      */
        button = (Button) findViewById(R.id.button);
        ImageView imageView = (ImageView) findViewById(R.id.img);
        drawable = imageView.getDrawable();
        button.setBackground(drawable);
        button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startRecorder();
                        break;
                    case MotionEvent.ACTION_UP:
                        stopRecorder();
                        break;
                }
                return true;

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length <= 0 || grantResults[0] == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(this, "无法录音", Toast.LENGTH_SHORT).show();
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO))
                    Toast.makeText(this, "没有录音权限", Toast.LENGTH_SHORT).show();
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startRecorder();
            }
        }
    }

    private void startRecorder() {

        button.setText("正在录音");
        button.setBackground(drawable);
        start(handler, name);
        recording = !recording;
    }
    //
    private void stopRecorder() {

        button.setText("按住说话");
        stop();
        recording = !recording;

        Intent intent = new Intent();
        soundRecoderUriToString = getExternalCacheDir()+"/"+name + ".wav";
        intent.putExtra("soundRecorderUriPath",soundRecoderUriToString);
        intent.putExtra("soundName", name+".wav");
        setResult(RESULT_OK,intent);
        finish();

    }

    public void start(final Handler handler, String str) {
        if (record != null || on)
            return;
        bufferSize = AudioRecord.getMinBufferSize(sampleRateInHz, channelConfig, audioFormat);
        record = new AudioRecord(MediaRecorder.AudioSource.MIC,
                sampleRateInHz,
                channelConfig,
                audioFormat,
                bufferSize);

        if (record.getState() == AudioRecord.STATE_UNINITIALIZED) {
            Log.e(TAG, "start: record state uninitialized");
            return;
        }
        pcmFile = new File(getExternalCacheDir(), str + ".pcm");

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

    public void stop() {
        on = false;
        Toast.makeText(AudioRecorderActivity.this,"保存成功",Toast.LENGTH_SHORT).show();
        Log.d(TAG,"录音活动路径是"+soundRecoderUriToString);
    }

}





