package com.example.finmins.materialtest;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import java.util.Calendar;
import android.media.MediaPlayer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.AndroidCharacter;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class AlarmActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
/*
        final  TimePicker timePicker = (TimePicker)findViewById(R.id.timepicker); //获取时间拾取组件
        timePicker.setIs24HourView(true);   //设置使用24小时制
        Button button_duan = (Button)findViewById(R.id.alarm_shijiandian);
        Button button_dian = (Button)findViewById(R.id.alarm_shijiandian);
        button_dian.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent =new Intent(AlarmActivity.this,AlarmTest.class);
                PendingIntent pendingIntent= PendingIntent.getBroadcast(AlarmActivity.this,0,intent,0);
                AlarmManager alarm = (AlarmManager)getSystemService(ALARM_SERVICE);
                Calendar c = Calendar.getInstance();
                c.set(Calendar.HOUR_OF_DAY,timePicker.getCurrentHour());//设置闹钟小时数
                c.set(Calendar.MINUTE,timePicker.getCurrentMinute());//设置闹钟的分钟数.
                c.set(Calendar.SECOND,0);//设置闹钟的秒数
                alarm.set(AlarmManager.RTC_WAKEUP,c.getTimeInMillis(),pendingIntent);//设置一个闹钟
                Toast.makeText(AlarmActivity.this,"闹钟设置成功",Toast.LENGTH_SHORT).show();
            }
        });

*/



      final  TimePicker timePicker = (TimePicker)findViewById(R.id.timepicker); //获取时间拾取组件
        timePicker.setIs24HourView(true);   //设置使用24小时制
        Button button_duan = (Button)findViewById(R.id.alarm_shijiandian);
        Button button_dian = (Button)findViewById(R.id.alarm_shijiandian);
        button_dian.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent =new Intent(AlarmActivity.this,Alarm.class);
                PendingIntent pendingIntent= PendingIntent.getActivity(AlarmActivity.this,0,intent,0);
                AlarmManager alarm = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
                Calendar c = Calendar.getInstance();
                c.set(Calendar.HOUR_OF_DAY,timePicker.getCurrentHour());//设置闹钟小时数
                c.set(Calendar.MINUTE,timePicker.getCurrentMinute());//设置闹钟的分钟数.
                c.set(Calendar.SECOND,0);//设置闹钟的秒数
                alarm.set(AlarmManager.RTC_WAKEUP,c.getTimeInMillis(),pendingIntent);//设置一个闹钟
                Toast.makeText(AlarmActivity.this,"闹钟设置成功",Toast.LENGTH_SHORT).show();
            }
        });
    }
    }



