package com.example.finmins.materialtest;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.litepal.crud.DataSupport;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

public class ChaKanActivity extends AppCompatActivity {
    private static final  String TAG ="ChaKanActivity.this";
    private ImageButton addCream;    //照相图片
    private ImageButton addAlbum;    //相册图片
    private int positions;          //事件的位置
    private ImageButton addAlarmClock;   //闹钟图片
    private ImageButton addSoundRecording;      //语音图片
    private MyDatabaseHelper dbHelper;  //数据库
    private EditText editTextBiaoTi;     //标题文本
    private EditText editTextNeiRong;    //内容文本
    private ImageButton isChangedShiJian;      //勾号图片
    private  int shiJian_id;    //事件id
    private int hour_alarm;     //闹钟的小时
    private int minute_alarm;   //闹钟的的分钟
    private int isAlarm;     // 事件是否有闹钟
    private ImageView editTextPhoto;         //事件里的图片
    private Uri imageUri;     //获取到的图片转化成Uri地址
    //private ImageView myphoto;    //事件的图片
    private Bitmap bitmap;        //图片的转换bitmap形式
    private byte[] images = new byte[1024];    //图片的转换二进制形式
    private ShiJian shijian;        //事件
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
                        editTextPhoto.setImageBitmap(bitmap);
                        images=img(bitmap);
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
        //完成初始化界面
        getPosition();

          //点击相册的功能
        addAlbum.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(ChaKanActivity.this,"相册功能待开发",Toast.LENGTH_SHORT).show();
            }
        });

        //点击摄像机的功能
        addCream.setOnClickListener(new View.OnClickListener(){
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
                    imageUri = FileProvider.getUriForFile(ChaKanActivity.this,"com.example.finmins.MaterialTest.fileprovider",outputImage);
                }else
                {imageUri = Uri.fromFile(outputImage);
                }
                //启动相机程序
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");

                intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                startActivityForResult(intent,1);
            } });

        //图片长按删除功能
        editTextPhoto.setOnLongClickListener(new View.OnLongClickListener(){

         public boolean onLongClick(View v) {

             AlertDialog.Builder dialog= new AlertDialog.Builder(ChaKanActivity.this);
             dialog.setTitle("通知");
             dialog.setMessage("是否删除图片？");
             dialog.setCancelable(false);
             dialog.setPositiveButton("是",new DialogInterface.OnClickListener(){
                 @Override
                 public void onClick(DialogInterface dialog, int which) {
                     editTextPhoto.setImageBitmap(null);

                     images=null;
                 }
             });
             dialog.setNegativeButton("否",new DialogInterface.OnClickListener(){
                 @Override
                 public void onClick(DialogInterface dialog, int which) {

                 }
             });
             dialog.show();
             return true;
         }
     });

        //单击语音图片时的功能
        addSoundRecording.setOnClickListener(new  View.OnClickListener(){
            public void onClick(View v){
                Toast.makeText(ChaKanActivity.this,"录音功能待开发",Toast.LENGTH_SHORT).show();
            }
        });


        //单击闹钟图片时的功能
        addAlarmClock.setOnClickListener(new  View.OnClickListener(){
            public void onClick(View v){
                int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
                int  minute = Calendar.getInstance().get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog=new TimePickerDialog(ChaKanActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        hour_alarm = hourOfDay;
                        minute_alarm = minute;
                        isAlarm=1;
                        Toast.makeText(ChaKanActivity.this,"时间保存闹钟自动设置",Toast.LENGTH_SHORT).show();
                    }
                },hour,minute,true);
                timePickerDialog.show();
            }
        });



        //点击√的功能
        isChangedShiJian.setOnClickListener(new  View.OnClickListener(){
            public void onClick(View v){
                dbHelper.getWritableDatabase();
                //判断标题是否为空
                if(TextUtils.isEmpty(editTextBiaoTi.getText().toString()))
                {
                    Toast.makeText(ChaKanActivity.this,"标题不能为空！",Toast.LENGTH_SHORT).show();
                } else {
                    ShiJian shijian =new ShiJian();
                    shijian.setBiaoti(editTextBiaoTi.getText().toString());
                    shijian.setNeirong(editTextNeiRong.getText().toString());

                    shijian.setPhoto(images);
                    if(images==null) {
                        shijian.setToDefault("photo");
                    }
                    else{
                        shijian.setPhoto(images);
                    }
                    shijian.update(shiJian_id);
                    if(isAlarm==1)
                        addAlarm();
                    Toast.makeText(ChaKanActivity.this,"事件修改成功！",Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });



    }



    //获取时间的位置
    public void getPosition(){
        Intent intent=getIntent();
        //从上一个intent获取位置和id,,两个都默认为1
        positions =intent.getIntExtra("positions",1);
        shiJian_id = intent.getIntExtra("ids",1);

        isChangedShiJian=(ImageButton)findViewById(R.id.isChangedShiJianInChaKanLayout);      //活动里的修改按钮控件
        editTextPhoto= (ImageView)findViewById(R.id.editTextPhotoInChaKanLayout);   //获取文本里的图片控件
        editTextBiaoTi=(EditText)findViewById(R.id.editTextBiaotiInChaKanLayout);   //获取事件里的标题
        editTextNeiRong=(EditText)findViewById(R.id.editTextNeirongInChaKanLayout);   //获取时间里的内容控件
       addSoundRecording=(ImageButton)findViewById(R.id.addSoundRecordingInChaKanLayout);    // 活动界面里的语音控件
        shijian=DataSupport.find(ShiJian.class,shiJian_id);   //根据返回的id获取事件
       addAlarmClock = (ImageButton)findViewById(R.id.addAlarmClockInChaKanLayout);    //活动界面里的的闹钟
        addAlbum = (ImageButton)findViewById(R.id.addAlbumInChaKanLayout);    //活动界面的相册控件
        editTextBiaoTi.setText(shijian.getBiaoti());              //输出获得的标题
        editTextNeiRong.setText(shijian.getNeirong());        //输出获得的内容
        addCream=(ImageButton)findViewById(R.id.addCameraInChaKanLayout);          //获取活动里的相机控件

        //从数据库里获取图片
        if(shijian.getPhoto()!=null)
        {editTextPhoto.setImageBitmap(shijian.getPhoto());
          images = img(shijian.getPhoto());
        }
        dbHelper=new MyDatabaseHelper(this,"ShiJian.db",null,1);  //创建事件数据库
    }


   //将图片转化成二进制码
    private byte[] img(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,baos);
        return baos.toByteArray();
    }


    //添加闹钟
    private void addAlarm(){
        AlarmManager alarm = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        //创建一个广播
        Intent alarmIntent =new Intent("finmins.example.FinMins.broadcasttest.MY_BROADCAST");
        //根据id创建延迟活动
        PendingIntent alarmPendingIntent= PendingIntent.getBroadcast(ChaKanActivity.this,shiJian_id,alarmIntent,0);
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY,hour_alarm);//设置闹钟小时数
        c.set(Calendar.MINUTE,minute_alarm);//设置闹钟的分钟数.
        c.set(Calendar.SECOND,0);//设置闹钟的秒数
        alarm.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,c.getTimeInMillis(),alarmPendingIntent);
    }

}
