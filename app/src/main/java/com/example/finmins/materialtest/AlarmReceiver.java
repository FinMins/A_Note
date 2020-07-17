package com.example.finmins.materialtest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.WindowManager;

import androidx.appcompat.app.AlertDialog;

public class AlarmReceiver extends BroadcastReceiver {
    private IntentFilter intentFilter;
    public AlarmReceiver() {

    }



    @Override
    public void onReceive(Context context, Intent intent) {
      //进入到这个灌函数了。
        /*这是通知代码
        NotificationManager notificationManager = (NotificationManager)context. getSystemService(Context.NOTIFICATION_SERVICE);
                Notification notificatioon = new NotificationCompat.Builder(context)
                        .setContentTitle("这是标题")
                        .setContentText("这是内容")
                        .setAutoCancel(true)
                        .setSmallIcon(R.mipmap.ic_setting)
                        .build();
        notificationManager.notify(1,notificatioon);
     */
        AlertDialog alert =new AlertDialog.Builder(context).create();
        alert.setTitle("这是测试");     //设置对话框的标题
        alert.setMessage("注意啦！注意啦！");    //设置对话框的内容

        //添加确定按钮
        alert.setButton(DialogInterface.BUTTON_POSITIVE,"确定",new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog,int which){

            }
        });
        alert.getWindow().setType(WindowManager.LayoutParams.TYPE_TOAST);
        alert.show();

    }
}
