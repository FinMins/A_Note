package com.example.finmins.materialtest;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.sql.Time;
import java.util.Calendar;

public class InsertActivity extends AppCompatActivity {
    private FloatingActionButton tianjia ;
    private ImageButton naozhong;
    private ImageButton yuyin;
    private MyDatabaseHelper dbHelper; //数据库
    private EditText biaoti;
    private EditText neirong;
    private  int shiJian_id;
    private int hour_alarm;
    private int minute_alarm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);
        initSet();   //初始化事件
        dbHelper=new MyDatabaseHelper(this,"ShiJian.db",null,1);  //创建事件数据库
        Toolbar toolbar = (Toolbar) findViewById(R.id.insert_toolbar);
        setSupportActionBar(toolbar);

        //单击闹钟图片时的功能
        naozhong.setOnClickListener(new  View.OnClickListener(){
            public void onClick(View v){
                 int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
                int  minute = Calendar.getInstance().get(Calendar.MINUTE);
             TimePickerDialog timePickerDialog=new TimePickerDialog(InsertActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        hour_alarm = hourOfDay;
                        minute_alarm = minute;
                        Toast.makeText(InsertActivity.this,"时间保存闹钟自动设置",Toast.LENGTH_SHORT).show();
                      }
                    },hour,minute,true);
                 timePickerDialog.show();
            }
        });

        //单击语音图片时的功能
        yuyin.setOnClickListener(new  View.OnClickListener(){
            public void onClick(View v){

               // Intent intent =new Intent(InsertActivity.this,LuYinActivity.class);
              //  startActivity(intent);
            }
        });

        //单击添加按钮时的功能
        tianjia.setOnClickListener(new  View.OnClickListener(){
            public void onClick(View v){
                dbHelper.getWritableDatabase();
                //判断标题是否为空
             if(TextUtils.isEmpty(biaoti.getText().toString()))
             {
                 Toast.makeText(InsertActivity.this,"标题不能为空！",Toast.LENGTH_SHORT).show();
             } else {
               ShiJian shijian =new ShiJian();
                shijian.setBiaoti(biaoti.getText().toString());
                shijian.setNeirong(neirong.getText().toString());
                 shijian.setYear(Calendar.getInstance().get(Calendar.YEAR));
                 shijian.setMonth(Calendar.getInstance().get(Calendar.MONTH)+1);
                 shijian.setDay(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                 shijian.setTime( String.valueOf(Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) +"时"+
                         String.valueOf(Calendar.getInstance().get(Calendar.MINUTE) )+"分"+
                         String.valueOf(Calendar.getInstance().get(Calendar.SECOND))+"秒");
                 shijian.setImgId(0);
                 shijian.save();
                 shiJian_id =shijian.getId();

                 addAlarm();

                 Toast.makeText(InsertActivity.this,"事件添加成功！，id为"+String.valueOf(shiJian_id),Toast.LENGTH_SHORT).show();
                 finish();
             }
            }
        });

    }
  //初始化控件
    private void initSet(){

        biaoti=(EditText)findViewById(R.id.editText_biaoti);
        neirong=(EditText)findViewById(R.id.editText_neirong);
        tianjia  = (FloatingActionButton)findViewById(R.id.tianjia);
        naozhong = (ImageButton)findViewById(R.id.naozhong);
        yuyin =(ImageButton) findViewById(R.id.yuyin);
    }

    //添加闹钟
    private void addAlarm(){

        AlarmManager alarm = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent intent =new Intent("com.example.FinMins.broadcasttest.MY_BROADCAST");
        PendingIntent pendingIntent= PendingIntent.getBroadcast(InsertActivity.this,0,intent,0);
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY,hour_alarm);//设置闹钟小时数
        c.set(Calendar.MINUTE,minute_alarm);//设置闹钟的分钟数.
        c.set(Calendar.SECOND,0);//设置闹钟的秒数
        alarm.setExact(AlarmManager.RTC_WAKEUP,c.getTimeInMillis(),pendingIntent);//设置一个闹钟
        Intent alarmIntent =new Intent("com.example.FinMins.broadcasttest.MY_BROADCAST");
        PendingIntent alarmPendingIntent= PendingIntent.getBroadcast(InsertActivity.this,shiJian_id,alarmIntent,0);
        alarm.setExact(AlarmManager.RTC_WAKEUP,c.getTimeInMillis(),alarmPendingIntent);

    }



}