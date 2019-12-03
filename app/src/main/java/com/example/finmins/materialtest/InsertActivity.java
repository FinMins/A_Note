package com.example.finmins.materialtest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.InvalidMarkException;
import java.util.Calendar;

public class InsertActivity extends AppCompatActivity {
    private FloatingActionButton tianjia ;  //添加按钮
    private ImageButton naozhong;   //
    private ImageButton yuyin;
    private MyDatabaseHelper dbHelper; //数据库
    private EditText biaoti;
    private EditText neirong;
    private ImageButton fanhui;
    private ImageButton dui;
    private  int shiJian_id;
    private int hour_alarm;
    private int minute_alarm;
    private int isAlarm;
    private ImageButton photo;
    private Uri imageUri;
    private ImageView myphoto;
    private Bitmap bitmap;
    private byte[] images = new byte[1024];

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                if(resultCode == RESULT_OK){
                    try{
                        //将拍摄的照片显示出来
                        Bitmap bitmap1 = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        Matrix matrix = new Matrix();
                        matrix.setScale(0.277f, 0.277f);
                        Bitmap bitmap2 = Bitmap.createBitmap( bitmap1, 0, 0,  bitmap1.getWidth(), bitmap1.getHeight(), matrix, false);
                        matrix.setRotate(90f);
                        bitmap = Bitmap.createBitmap( bitmap2, 0, 0,  bitmap2.getWidth(), bitmap2.getHeight(), matrix, false);

                         myphoto.setImageBitmap(bitmap);
                    }catch (FileNotFoundException e){
                        e.printStackTrace();
                    }
                }
        }
    }

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
                        isAlarm=1;
                        Toast.makeText(InsertActivity.this,"时间保存闹钟自动设置",Toast.LENGTH_SHORT).show();
                      }
                    },hour,minute,true);
                 timePickerDialog.show();
        }
        });

        //单击语音图片时的功能
        yuyin.setOnClickListener(new  View.OnClickListener(){
            public void onClick(View v){
            }
        });

        //单击返回按钮时的功能
        fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(InsertActivity.this,MainActivity.class);
//                startActivity(intent);
                finish();
            }
        });

        //单击摄像机时的功能
        photo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                File outputImage = new File(getExternalCacheDir(),"output_image.jpg");
                try{
                    if ((outputImage.exists())){
                        outputImage.delete();
                    }
                    outputImage.createNewFile();
                }catch (IOException e){
                    e.printStackTrace();
                }
                if ((Build.VERSION.SDK_INT>=24)){
                    imageUri = FileProvider.getUriForFile(InsertActivity.this,"com.example.finmins.MaterialTest.fileprovider",outputImage);
                }else
                    {imageUri = Uri.fromFile(outputImage);
                     }
                //启动相机程序
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");

                intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                startActivityForResult(intent,1);
        } });





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
                 Log.d("InserActivity", "进去了么？ ");
                 if(bitmap!=null){
                     Log.d("InserActivity", "点了么？ ");
                     byte[] images = img(bitmap);
                     shijian.setPhoto(images);
                 }
                 else{
                     Log.d("InserActivity", "bitmap为空 ");
                 }
                 shijian.save();
                 shiJian_id =shijian.getId();
                  if(isAlarm==1)
                 addAlarm();
                 Toast.makeText(InsertActivity.this,"事件添加成功！，id为"+String.valueOf(shiJian_id),Toast.LENGTH_SHORT).show();
                 ImageButton btn_a= (ImageButton) findViewById(R.id.dui);
                 btn_a.setVisibility(View.VISIBLE);
                 ImageButton btn_b = (ImageButton) findViewById(R.id.dui);
                 btn_b.setVisibility(View.INVISIBLE);
                 finish();
             }
            }
        });

        dui.setOnClickListener(new  View.OnClickListener(){
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
                    if(isAlarm==1)
                        addAlarm();
                    Toast.makeText(InsertActivity.this,"事件添加成功！，id为"+String.valueOf(shiJian_id),Toast.LENGTH_SHORT).show();
                    ImageButton btn_a= (ImageButton) findViewById(R.id.dui);

                    btn_a.setVisibility(View.VISIBLE);
                    ImageButton btn_b = (ImageButton) findViewById(R.id.dui);
                    btn_b.setVisibility(View.INVISIBLE);
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
        fanhui = (ImageButton) findViewById(R.id.fanhui);
        dui = (ImageButton) findViewById(R.id.dui);
        photo = (ImageButton)findViewById(R.id.photo);
        myphoto=(ImageView) findViewById(R.id.myphoto);
    }

    //添加闹钟
    private void addAlarm(){
        AlarmManager alarm = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
  ;      Intent alarmIntent =new Intent("finmins.example.FinMins.broadcasttest.MY_BROADCAST");
        PendingIntent alarmPendingIntent= PendingIntent.getBroadcast(InsertActivity.this,shiJian_id,alarmIntent,0);
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY,hour_alarm);//设置闹钟小时数
        c.set(Calendar.MINUTE,minute_alarm);//设置闹钟的分钟数.
        c.set(Calendar.SECOND,0);//设置闹钟的秒数
        alarm.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,c.getTimeInMillis(),alarmPendingIntent);
    }
  //把图片转换为节
  private byte[] img(Bitmap bitmap){
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      bitmap.compress(Bitmap.CompressFormat.PNG,100,baos);
      return baos.toByteArray();
  }

}