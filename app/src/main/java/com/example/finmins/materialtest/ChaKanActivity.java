package com.example.finmins.materialtest;

import android.Manifest;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;

public class ChaKanActivity extends AppCompatActivity {
    private static final  int TAKE_PHOTO = 1;
    private static final int CHOOSE_PHOTO = 2;
    private static  final  int DRAW_PHOTO = 3 ;
    private static  final int SOUND_RECODER = 4;
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
    private String time ; //事件时间
    private ImageView editTextPhoto;         //事件里的图片
    private ImageView editTextAlbum;        //事件里的相册图片
    private Uri imageUri;     //获取到的图片转化成Uri地址
    //private ImageView myphoto;    //事件的图片
    private Bitmap bitmap;        //图片的转换bitmap形式
    private byte[] imagesByPhoto = new byte[1024];    //图片的转换二进制形式
//    private ShiJian shijian;        //事件
    private String  biaoti  ;
    private String neirong   ;
   private  ImageButton drawBoard;     //画板控件
    private Button playSoundRecorder ;    //播放录音控件
    private String soundRecorderPath;    //录音路径；
    private HttpClientUtils httpClientUtils = new HttpClientUtils();
    private  final  String  URL = "http://192.168.43.61:9999";
     private  Uri  soundUri ;
    /**接受自己输入的图片并展示
     * 调节图片大小
     * 调节图片的显示效果
     */

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            //返回图片
            case TAKE_PHOTO:
                if(resultCode == RESULT_OK){
                    try{
                        //将拍摄的照片显示出来
                        Bitmap bitmap1 = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        bitmap = setBitmap(bitmap1,0.277f,90f);
                        imagesByPhoto=img(bitmap);
                        //将图片展示出来
                        editTextPhoto.setImageBitmap(bitmap);
                    }catch (FileNotFoundException e){
                        e.printStackTrace();
                    }
                }
                break;
            case CHOOSE_PHOTO:
                if(resultCode == RESULT_OK){
                    //判断手机系统版本号
                    if (Build.VERSION.SDK_INT >= 19) {
                        //4.4及以上系统使用这个方法处理图片
                      handleImageOnKitKat(data);
                        Log.d(TAG, "在这444");
                    }else{
                        //4.4以下系统使用这个方法处理图片
                      handleImageOnKitKat(data);
                    }
                }
                break;
            case DRAW_PHOTO:
                if(resultCode == RESULT_OK){
                    imageUri =Uri.parse(data.getStringExtra("ImageUriPath"));
                    try{
                        Bitmap bitmap1 =  BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        bitmap = setBitmap(bitmap1,0.277f,90f);
                        imagesByPhoto=img(bitmap);
                        //将图片展示出来
                        editTextPhoto.setImageBitmap(bitmap);
                    }catch (FileNotFoundException e){
                        e.printStackTrace();
                    }
                    //  Log.d(TAG,"插入里的是"+data.getStringExtra("ImageUriPath"));
                    //     Bitmap bitmap =  BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                }
                break;
            case SOUND_RECODER:
                if(resultCode == RESULT_OK){
                    soundRecorderPath = data.getStringExtra("soundRecorderUriPath");    //获取从录音活动传回来的路径
                    Log.d(TAG,"路径是"+soundRecorderPath );
                    soundUri = Uri.parse(soundRecorderPath );           //将路径转换成Uri
                    playSoundRecorder.setVisibility(View.VISIBLE);     //设置播放录音按钮可见
                }
                break;
            default:
                break;

        }
    }


    //相册功能


    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if (grantResults.length>0&&grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    openAlbum();
                }else{
                    Toast.makeText(this,"被拒绝",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    private void openAlbum(){
        Intent intent = new Intent ("android.intent.action.GET_CONTENT");
        intent.setType("image/*");

        startActivityForResult(intent,CHOOSE_PHOTO);//打开相册
        Log.d(TAG, "在这333");
    }

    private void handleImageOnKitKat(Intent data){
        String imagePath = null;
        Uri uri = data.getData();
        if(DocumentsContract.isDocumentUri(this,uri)){
            //如果是documejt类型的Uri,则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if("com.android.providers.media.documents".equals(uri.getAuthority())){
                String id = docId.split(":")[1];//解析出数字格式的id
                String selection = MediaStore.Images.Media._ID+ "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
            }else if("com.android.providers.downloads.documents".equals(uri.getAuthority())){
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),Long.valueOf(docId));
                imagePath = getImagePath(contentUri,null);
            }
            else if ("content".equalsIgnoreCase(uri.getScheme())){
                //如果是content类型的uri，则使用普通方式处理
                imagePath = getImagePath(uri,null);
            }else if("file".equalsIgnoreCase(uri.getScheme())){
                //如果是file类型的U日，直接获取图片路径即可
                imagePath=uri.getPath();
            }
            displayImage(imagePath);//根据图片路径显示图片
        }
    }

    private  void handleImageBeforeKitKat(Intent data){
        Uri uri = data.getData();
        String imagePath = getImagePath(uri,null);
        displayImage(imagePath);
    }

    private String getImagePath(Uri uri,String selection){
        String path = null;
        //通过Uri和selection来获取真实的图片路径
        Cursor cursor = getContentResolver().query(uri,null,selection,null,null);
        if(cursor != null){
            if(cursor.moveToFirst()){
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void displayImage(String imagePath){
        if(imagePath != null){
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            bitmap = setBitmap(bitmap,0.277f,90f);
            imagesByPhoto = img(bitmap);
            editTextPhoto.setImageBitmap(bitmap);
        }else{
            Toast.makeText(this,"得到图片失败",Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cha_kan);
        //完成初始化界面
        getPosition();

          //点击相册的功能
        addAlbum.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(ChaKanActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){

                    ActivityCompat.requestPermissions(ChaKanActivity.this,new
                            String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);}
                else{
                    Log.d(TAG, "点击了打开相册");
                    openAlbum();
                }
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

        //录音长按删除功能
        playSoundRecorder.setOnLongClickListener(new View.OnLongClickListener(){

            public boolean onLongClick(View v) {
                AlertDialog.Builder dialog= new AlertDialog.Builder(ChaKanActivity.this);
                dialog.setTitle("通知");
                dialog.setMessage("是否删除录音？");
                dialog.setCancelable(false);
                dialog.setPositiveButton("是",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        soundRecorderPath =null;
                        playSoundRecorder.setVisibility(View.GONE);
                    }
                });
                //点击否不做任何修改
                dialog.setNegativeButton("否",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                dialog.show();
                return true;
            }
        });


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

                     imagesByPhoto=null;
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
                Intent intent = new Intent(ChaKanActivity.this, AudioRecorderActivity.class);
                startActivityForResult(intent,SOUND_RECODER);

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
                        Toast.makeText(ChaKanActivity.this,"事件保存闹钟自动设置",Toast.LENGTH_SHORT).show();
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
                    changeBiaoti(editTextBiaoTi.getText().toString());
                    changeNeiRong(editTextNeiRong.getText().toString());
            //判断录音文件是否更改
                    if(soundRecorderPath== null){
                        shijian.setToDefault("soundRecorderPath");

                    }
                    else {
                        shijian.setSoundRecorderPath(soundRecorderPath);
                    }
             //判断图片文件是否更改
                    if(imagesByPhoto==null) {

                        shijian.setToDefault("photo");
                    }
                    else{
                        shijian.setPhoto(imagesByPhoto);
                        changeImg(imagesByPhoto);
                    }

                    shijian.update(shiJian_id);
                    if(isAlarm==1)
                        addAlarm();
                    Toast.makeText(ChaKanActivity.this,"事件修改成功！",Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

        //点击播放录音按钮
        playSoundRecorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //功能
                File soundFile = new File(soundRecorderPath);
                MediaPlayer mediaPlayer =  MediaPlayer.create(ChaKanActivity.this,Uri.parse(soundFile.getAbsolutePath()));
                mediaPlayer.setPlaybackParams(mediaPlayer.getPlaybackParams().setSpeed(0.5f));
                mediaPlayer.start();
            }
        });

      //单击画板时的功能
        drawBoard.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChaKanActivity.this,DrawboardActivity.class);
                startActivityForResult(intent,DRAW_PHOTO);
            } });


    }

    //上传修改的标题
    private void changeBiaoti( String biaoti){
        String updateBiaoti = "{\n" +
                "\n" +
                "\"biaoti\":\""+ biaoti+"\",\n" +
                "\"place\":\""+time+"\"\n" +
                "\n" +
                "}";
        httpClientUtils.sendPostByOkHttp(URL+"/shijian/updatebiaoti",updateBiaoti);
    }

    //上传修改的内容
    private void changeNeiRong( String neirong){
        String updateNeirong = "{\n" +
                "\n" +
                "\"neirong\":\""+ neirong+"\",\n" +
                "\"place\":\""+time+"\"\n" +
                "\n" +
                "}";
        httpClientUtils.sendPostByOkHttp(URL+"/shijian/update",updateNeirong);
    }
    //上传修改的图片
    private void changeImg( byte[] img){
        String updateimg = "{\n" +
                "\n" +
                "\"photo\":\""+ img+"\",\n" +
                "\"place\":\""+time+"\"\n" +
                "\n" +
                "}";
        httpClientUtils.sendPostByOkHttp(URL+"/shijian/updatephoto",updateimg);
    }
    //获取时间的位置
    public void getPosition(){
        Intent intent=getIntent();
        //从上一个intent获取位置和id,,两个都默认为1
        positions =intent.getIntExtra("positions",1);
        shiJian_id = intent.getIntExtra("ids",1);
        biaoti = intent.getStringExtra("biaoti");
        neirong= intent.getStringExtra("neirong");
        isChangedShiJian=(ImageButton)findViewById(R.id.isChangedShiJianInChaKanLayout);      //活动里的修改按钮控件
        editTextPhoto= (ImageView)findViewById(R.id.editTextPhotoInChaKanLayout);   //获取文本里的图片控件
        editTextBiaoTi=(EditText)findViewById(R.id.editTextBiaotiInChaKanLayout);   //获取事件里的标题
        editTextNeiRong=(EditText)findViewById(R.id.editTextNeirongInChaKanLayout);   //获取时间里的内容控件
       addSoundRecording=(ImageButton)findViewById(R.id.addSoundRecordingInChaKanLayout);    // 活动界面里的语音控件
//        shijian= DataSupport.find(ShiJian.class,shiJian_id);   //根据返回的id获取事件
        time = intent.getStringExtra("time");
       addAlarmClock = (ImageButton)findViewById(R.id.addAlarmClockInChaKanLayout);    //活动界面里的的闹钟
        addAlbum = (ImageButton)findViewById(R.id.addAlbumInChaKanLayout);    //活动界面的相册控件
        editTextBiaoTi.setText(biaoti);              //输出获得的标题
        editTextNeiRong.setText(neirong);        //输出获得的内容
        addCream=(ImageButton)findViewById(R.id.addCameraInChaKanLayout);          //获取活动里的相机控件
       drawBoard = (ImageButton)findViewById(R.id.drawInChaKanLayout);    //获取画板控件
        playSoundRecorder = (Button)findViewById(R.id.soundRecoderInChaKanLayout);  //获取播放录音控件
        //从数据库里获取图片
//        if(shijian.getPhoto()!=null)
//        {
//            Log.d(TAG, "不为空");
//            editTextPhoto.setImageBitmap(shijian.getPhoto());
//          imagesByPhoto = img(shijian.getPhoto());
//        }
//        if(shijian.getSoundRecorderPath()!=null){
//            playSoundRecorder.setVisibility(View.VISIBLE);
//            soundRecorderPath = shijian.getSoundRecorderPath();
//        }
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
        Log.d(TAG, "事件ID是"+shiJian_id);
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY,hour_alarm);//设置闹钟小时数
        c.set(Calendar.MINUTE,minute_alarm);//设置闹钟的分钟数.
        c.set(Calendar.SECOND,0);//设置闹钟的秒数
        alarm.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,c.getTimeInMillis(),alarmPendingIntent);
    }

    //图片优化
    public Bitmap setBitmap(Bitmap bitmap,float big , float rangle){
        Matrix matrix = new Matrix();
        //设置图片的大小
        matrix.setScale(big, big);
        Bitmap bitmapTmp = Bitmap.createBitmap( bitmap, 0, 0,  bitmap.getWidth(), bitmap.getHeight(), matrix, false);
        //将图片旋转xx度
        matrix.setRotate(rangle);
        return Bitmap.createBitmap( bitmapTmp, 0, 0,  bitmapTmp.getWidth(), bitmapTmp.getHeight(), matrix, false);
    }

}
