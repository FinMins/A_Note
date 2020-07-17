package com.example.finmins.materialtest;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;

public class LongRunningServiece extends Service {
    public LongRunningServiece() {



    }

    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                /*
                NotificationManager manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
                Notification notification = new NotificationCompat.Builder(LongRunningServiece.this)
                        .setContentTitle("这是通知")
                        .setContentText("这是通知内容（实际应用中应该不要这个内容）")
                        .setWhen(System.currentTimeMillis())
                        .setSmallIcon(R.mipmap.app)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.app))
                        .build();
                manager.notify(1,notification);*/
            }
        }).start();

        AlarmManager manager = (AlarmManager)getSystemService(ALARM_SERVICE);
        int Time = 1000*5;
        long triggerAtTime = SystemClock.elapsedRealtime()+Time;
        Intent i = new Intent(this,LongRunningServiece.class);
        PendingIntent pi=PendingIntent.getService(this,0,i,0);
        manager.setExact(AlarmManager.ELAPSED_REALTIME,triggerAtTime,pi);
        return super.onStartCommand(intent, flags, startId);
    }
}

