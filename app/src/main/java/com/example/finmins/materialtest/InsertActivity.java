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
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class InsertActivity extends AppCompatActivity {
//    public LocationClient mLocationClient ;
public AMapLocationClient mLocationClient ;
//public AMapLocationListener mLocationListener;

    private TextView positionText;
    private static  final  String TAG ="InsertAcitvity.this";
    private  static final int LOCATION= 2 ;    //位置返回
    public static  final int TAKE_PHOTO = 1;
    public static final  int  CHOOSE_PHOTO = 2;
    public static final  int DRAW_PHOTO= 3 ;
    public static final int SOUND_RECODER = 4;
    private String streetNmae ; //街道名字
    private String city;   //市名字
    private  ImageButton addAlarmClock;    //添加闹钟按钮
    private ImageButton addAlbum;   // 从相册添加图片按钮
    private ImageButton addSoundRecoding;    //添加录音按钮
    private MyDatabaseHelper dbHelper; //数据库
    private EditText biaoti;    //事件标题
    private EditText neirong;   //事件内容
    private ImageButton addShiJian;    //改变事件返回按钮
    private  int shiJian_id;    //事件的id
    private int hour_alarm;      //设置闹钟时 的小时数
    private int minute_alarm;     //设置闹钟是的分钟数
    private int isAlarm;    //事件是否有闹钟
    private ImageButton addCamera;      //摄像添加图片按钮
    private Uri imageUri;      //图片的Uri转换表
    private ImageView  editTextPhoto;    //文本框里的图片控件
    private Bitmap bitmap;      //图片的bitmap转化
    private ImageButton drawBoard;    //画板控件
    private byte[] images = new byte[1024];    //图片的二进制转化
    private Uri soundUri ;           //录音的路径
    private Button playSoundRecoder ;     //播放录音按钮
    private String soundString ;
    private String userEmail;   //用户邮箱
    private String soundName;     //录音名字加后缀
   private  AMapLocationClientOption mLocationClientOption    ;
   private HttpClientUtils httpClientUtils = new HttpClientUtils();
   private  final  String  URL = "http://192.168.43.61:9999";



    /**接受自己输入的图片并展示
     * 调节图片大小
     * 调节图片的显示效果
     */  @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //  super.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            //返回图片
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        //将拍摄的照片显示出来
                        Bitmap bitmap1 = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        //   Log.d(TAG,"摄像里是"+imageUri.toString());
                        bitmap = setBitmap(bitmap1, 0.277f, 0);
                        images = img(bitmap);
                        //将图片展示出来
                        editTextPhoto.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
//                break;
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK) {
                    //判断手机系统版本号
                    if (Build.VERSION.SDK_INT >= 19) {
                        //4.4及以上系统使用这个方法处理图片
                        handleImageOnKitKat(data);
//                        Log.d(TAG, "在这444");
                    } else {
                        //4.4以下系统使用这个方法处理图片
                        handleImageOnKitKat(data);
                    }
                }
                break;
            case DRAW_PHOTO:
                if (resultCode == RESULT_OK) {
                    imageUri = Uri.parse(data.getStringExtra("ImageUriPath"));
                    try {
                        Bitmap bitmap1 = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        bitmap = setBitmap(bitmap1, 0.577f, 0);
                        images = img(bitmap);
                        //将图片展示出来
                        editTextPhoto.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;

            case SOUND_RECODER:
                if (resultCode == RESULT_OK) {
                    soundString = data.getStringExtra("soundRecorderUriPath");    //获取从录音活动传回来的路径
                    soundName = data.getStringExtra("soundName");
                    Log.d(TAG, "插入路径是" + soundString);

                    playSoundRecoder.setVisibility(View.VISIBLE);     //设置播放录音按钮可见
                }
                break;
            default:
                break;

        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                 if (grantResults.length>0&&grantResults[0] == PackageManager.PERMISSION_GRANTED){
                     openAlbum();
                 }else{
                     Toast.makeText(this,"被拒绝",Toast.LENGTH_SHORT).show();
                 }
                break;

            case LOCATION:
                if (grantResults.length > 0){
                    for (int result : grantResults){
                        if(result != PackageManager.PERMISSION_GRANTED){
                            Toast.makeText(this,"需同意授权才能使用本程序",Toast.LENGTH_SHORT).show();
                            finish();
                            return ;
                        }
                    }
                  requestLocation();
//                    mLocationClient.startLocation();
                }else{
                    Toast.makeText(this,"发生未知错误",Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
                break;
        }
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
            Bitmap bitmap1 = BitmapFactory.decodeFile(imagePath);
            bitmap = setBitmap(bitmap1,0.277f,90f);
            images = img(bitmap);
            editTextPhoto.setImageBitmap(bitmap);
        }else{
            Toast.makeText(this,"得到图片失败",Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);
        Intent intent =getIntent();
        userEmail = intent.getStringExtra("userEmail");


//百度
//        mLocationClient = new LocationClient(getApplicationContext());
//        mLocationClient.registerLocationListener(new MyLocationListener());
//        高德
        mLocationClient = new AMapLocationClient(getApplicationContext());
        mLocationClient.setLocationListener(new MyAMapLocationListener() );
//

        List<String> permissionList = new ArrayList<String>() ;
        if(ContextCompat.checkSelfPermission(InsertActivity.this,Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if(ContextCompat.checkSelfPermission(InsertActivity.this,Manifest.permission.READ_PHONE_STATE)!=PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if(ContextCompat.checkSelfPermission(InsertActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if(!permissionList.isEmpty()){
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(InsertActivity.this,permissions,LOCATION );
        }else{
//            mLocationClient.start();
            mLocationClient.startLocation();
        }


        initSet();   //初始化事件
        dbHelper=new MyDatabaseHelper(this,"ShiJian.db",null,1);  //创建事件数据库



       Toolbar toolbar = (Toolbar) findViewById(R.id.insert_toolbar);
        setSupportActionBar(toolbar);

        /** 单击闹钟图片时的功能：
         * 当事件点击确定后闹钟才会被创建！
         *
         */
        addAlarmClock.setOnClickListener(new  View.OnClickListener(){
            public void onClick(View v){
                int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
                int  minute = Calendar.getInstance().get(Calendar.MINUTE);
                //创建TimePickerDialog事件选择器
                TimePickerDialog timePickerDialog=new TimePickerDialog(InsertActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        hour_alarm = hourOfDay;
                        minute_alarm = minute;
                        //设置事件有闹钟
                        isAlarm=1;
                        Toast.makeText(InsertActivity.this,"事件保存闹钟自动设置",Toast.LENGTH_SHORT).show();
                    }
                },hour,minute,true);
                timePickerDialog.show();
            }
        });

        /**单击语音图片时的功能
         *
         */
        addSoundRecoding.setOnClickListener(new  View.OnClickListener(){
            public void onClick(View v){
              //录音代码
            Intent intent = new Intent(InsertActivity.this, AudioRecorderActivity.class);
            startActivityForResult(intent,SOUND_RECODER);
            }
        });


        //点击播放录音按钮
        playSoundRecoder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File soundFile = new File(soundString);
                if (soundFile.exists()) {
                   MediaPlayer mediaPlayer =  MediaPlayer.create(InsertActivity.this,Uri.parse(soundFile.getAbsolutePath()));
                    //调播放速度。
           //         mediaPlayer.setPlaybackParams(mediaPlayer.getPlaybackParams().setSpeed(0.5f));
                    mediaPlayer.start();

              //      if (Build.VERSION.SDK_INT>=24){
             //           soundUri = FileProvider.getUriForFile(InsertActivity.this,"com.example.finmins.MaterialTest.fileprovider",soundFile);
             //       }else {
               //         soundUri = Uri.fromFile(soundFile);
             //       }
                 //   Log.d(TAG,"Uri是"+soundUri.toString());
             //       Intent playIntent = new Intent(Intent.ACTION_VIEW);
            //        playIntent.setDataAndType(soundUri,"audio/ x-wav");
               //     startActivity(playIntent);
                }else {

                }
                File soundFileByName = new File(getExternalCacheDir(),soundName);
                if (soundFile.exists()) {

                }
            }
        });




        /**单击相册图片时的功能
         *
         */
        addAlbum.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(InsertActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(InsertActivity.this,new
                            String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);}
                else{
                    openAlbum();
                }

            }
        });

        //录音长按删除功能
        playSoundRecoder.setOnLongClickListener(new View.OnLongClickListener(){

            public boolean onLongClick(View v) {
                AlertDialog.Builder dialog= new AlertDialog.Builder(InsertActivity.this);
                dialog.setTitle("通知");
                dialog.setMessage("是否删除录音？");
                dialog.setCancelable(false);
                dialog.setPositiveButton("是",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        soundString =null;
                       playSoundRecoder.setVisibility(View.GONE);
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

        /**图片长按删除功能
         *
         */
        editTextPhoto.setOnLongClickListener(new View.OnLongClickListener(){

            public boolean onLongClick(View v) {
                AlertDialog.Builder dialog= new AlertDialog.Builder(InsertActivity.this);
                dialog.setTitle("通知");
                dialog.setMessage("是否删除图片？");
                dialog.setCancelable(false);
                //点击是清楚图片bitmap
                dialog.setPositiveButton("是",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        editTextPhoto.setImageBitmap(null);
                        images=null;
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



        //单击摄像机时的功能
        addCamera.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                File outputImage = new File(getExternalCacheDir(),"output_image.jpg");
                try{
                    if (outputImage.exists()){
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
                startActivityForResult(intent,TAKE_PHOTO);
            } });

        //单击画板时的功能
        drawBoard.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
              Intent intent = new Intent(InsertActivity.this,DrawboardActivity.class);
                startActivityForResult(intent,DRAW_PHOTO);
            } });




        //单击添加按钮时的功能
        addShiJian.setOnClickListener(new  View.OnClickListener(){
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

                    SimpleDateFormat dff = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    dff.setTimeZone(TimeZone.getTimeZone("GMT+08"));
                    String ee = dff.format(new Date());

//                    Time t = new Time("GMT+8");
//                     int year =   Calendar.getInstance().get(Calendar.YEAR);
//                     int month =   Calendar.getInstance().get(Calendar.MONTH)+1;
//                    int day =    Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
//                    int hour =    Calendar.getInstance().HOUR_OF_DAY;
//                    int minute = Calendar.getInstance().MINUTE;
//                    int second = Calendar.getInstance().SECOND;
//                    int year =t.year;
//                    int month =t.month+1;
//                    int day =t.monthDay;
//                    int hour =t.hour;
//                    int minute =t.minute;
//                    int second =t.second;
//                    String time = year+"/"+month+"/"+day+"/"+hour+"/"+minute+"/"+second+city+"/"+streetNmae;
//                    String time = year+"/"+month+"/"+day;/
                    String time =  ee+"/"+city+" "+streetNmae;
//                    shijian.setYear( year  );
//                    shijian.setMonth(month);
//                    shijian.setDay(  day);
                    shijian.setTime(
//                            city+"市"+streetNmae
                          time
                    );
                    shijian.setImgId(0);
                    if(soundString != null){
                     shijian.setSoundRecorderPath(soundString);
                        Log.d(TAG,"添加了"+soundString);
                    }
                    if(images!=null){
                        shijian.setPhoto(images);
                    }
                   if( shijian.save()){
                       insertShijianInServer(biaoti.getText().toString(),neirong.getText().toString(),time,images );
                       Toast.makeText(InsertActivity.this,"事件添加成功！",Toast.LENGTH_SHORT).show();
                       if(isAlarm==1)
                           addAlarm();
                   }
                    else {
                       Toast.makeText(InsertActivity.this,"事件添加失败",Toast.LENGTH_SHORT).show();
                   }
                    shiJian_id =shijian.getId();
                    finish();
                }
            }
        });


    }


    private  int insertShijianInServer(String biaoti,String neirong,String time,byte[] photo){
//         String byteString = new String(photo);
        String byteString = String.valueOf(photo);
        Log.d("这是原生TOstring插入时的图片字符串", byteString);
//   String  insert =    "{\n" +
//           "\"youxiang\":\""+userEmail+"\",\n" +
//           "\"biaoti\":\""+biaoti+"\",\n" +
//           "\"neirong\":\""+neirong+"。\",\n" +
//           "\"place\":\""+time+"\",\n" +
//           "\"photo\":\""+byteString+"\"\n" +
//           "}";
        String insert = " {\n" +
                "\"youxiang\":\""+userEmail+"\",\n" +
                "\"biaoti\":\""+biaoti+"\",\n" +
                "\"neirong\":\""+neirong+"\",\n" +
                "\"place\":\""+time+"\",\n" +
                "\"photo\":\""+byteString+"\",\n" +
                "\"finished\":\"0\"\n" +
                "}";

        Log.d("insert:email", userEmail);
        Log.d("insert:biaoti", biaoti);
        Log.d("insert:neirong", neirong);
        Log.d("inser:time", time);
//        Log.d(TAG, "insertShijanInServer: ");
           httpClientUtils.sendPostByOkHttp(URL+"/shijian/insert",insert);
           return 1;
    }

    private void requestLocation(){
         initLocation() ;
         mLocationClient.setLocationOption(mLocationClientOption);
         mLocationClient.startLocation();
    }
    private void initLocation(){
//        LocationClientOption option = new LocationClientOption();
//        option.setLocationMode(LocationClientOption.LocationMode.Device_Sensors);
//        option.setIsNeedAddress(true);

        mLocationClientOption = new AMapLocationClientOption();
        mLocationClientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);   //高精度定位
        mLocationClientOption.setOnceLocation(true);  //只定位一次
        mLocationClientOption.setNeedAddress(true);   //返回定位地址

//    场景定位    mLocationClientOption.setLocationPurpose()

        // option.setScanSpan(5000);

//        mLocationClient.setLocOption(option);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        mLocationClient.stop();
    }


    public class MyAMapLocationListener implements  AMapLocationListener{

        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            Log.d(TAG, "进入带定位功能了。");
            if (aMapLocation!= null) {

                if (aMapLocation.getErrorCode() == 0) {
//可在其中解析aMapLocation获取相应内容。
                    Log.d(TAG, "进到赋值语句了。");
                    streetNmae = aMapLocation.getFloor();
                    city = aMapLocation.getAddress();


                    mLocationClient.stopLocation();
                }else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError","location Error, ErrCode:"
                            + aMapLocation.getErrorCode() + ", errInfo:"
                            + aMapLocation.getErrorInfo());
                  mLocationClient.stopLocation();
                }
            }


        }
    }




    //内部类
//    public class MyLocationListener extends BDAbstractLocationListener {
//
//        @Override
//        public void onReceiveLocation(BDLocation location) {
//            StringBuilder currentPostion = new StringBuilder();
//         if(location.getStreet()!=null  && location.getCity()!=null) {
//             Toast.makeText(InsertActivity.this,"获取成功",Toast.LENGTH_SHORT).show();
//             streetNmae = String.valueOf(location.getLatitude());
//             city = String.valueOf(location.getLongitude());
//         }else {
//             Toast.makeText(InsertActivity.this,"获取失败",Toast.LENGTH_SHORT).show();
//             streetNmae = String.valueOf(location.getLatitude())+"";
//             city = String.valueOf(location.getLongitude())+"";
//         }
            /*          currentPostion.append("维度：").append(location.getLatitude()).append("\n");
           currentPostion.append("经度：").append(location.getLongitude()).append("\n");
            currentPostion.append("国家：").append(location.getCountry()).append("\n");
            currentPostion.append("省：").append(location.getProvince()).append("\n");
            currentPostion.append("市：").append(location.getCity()).append("\n");
            currentPostion.append("区：").append(location.getDistrict()).append("\n");
            currentPostion.append("街道：").append(location.getStreet()).append("\n");
            currentPostion.append("地址：").append(location.getAddrStr()).append("\n");
            currentPostion.append("定位方式");
            if(location.getLocType()==BDLocation.TypeGpsLocation){
                currentPostion.append("GPS");
            }else  if(location.getLocType() == BDLocation.TypeNetWorkLocation){
                currentPostion.append("网络");
            }*/
          //  Toast.makeText(InsertActivity.this,location.getLocationID()+","+location.getLatitude(),Toast.LENGTH_SHORT).show();
//        }
//    }


    //初始化控件
    private void initSet(){
        biaoti=(EditText)findViewById(R.id.editTextBiaotiInInsertLayout);   //获取标题控件
        neirong=(EditText)findViewById(R.id.editTextNeirongInInsertLayout);   //获取内容控件
        addAlarmClock= (ImageButton)findViewById(R.id.addAlarmClockInInsertLayout);   //获取添加闹钟控件
        addSoundRecoding =(ImageButton) findViewById(R.id.addSoundRecordingInInsertLayout);   //获取添加录音控件
        addAlbum=(ImageButton)findViewById(R.id.addAlbumInInsertLayout);     //获取添加相册图片控件
        addShiJian = (ImageButton) findViewById(R.id.addShiJianFloatInInsert);    //获取修改事件控件
        addCamera = (ImageButton)findViewById(R.id.addCameraInInsertLayout);                   //获取照相控件
        editTextPhoto=(ImageView) findViewById(R.id.editTextPhotoInInsertLayout);        //获取文本款中图片控件
        drawBoard =(ImageButton)findViewById(R.id.drawInInsertLayout);     //获取画板控件
        playSoundRecoder = (Button)findViewById(R.id.soundRecoderInInsertLayout);   //获取播放按钮.


//
//        mLocationListener = new AMapLocationListener() {
//            @Override
//            public void onLocationChanged(AMapLocation aMapLocation) {
//                if (aMapLocation!= null) {
//                    if (aMapLocation.getErrorCode() == 0) {
////可在其中解析aMapLocation获取相应内容。
//                        Log.d(TAG, "进到赋值语句了。");
//                        streetNmae = aMapLocation.getStreet();
//                        city = aMapLocation.getCity();
//
//
//                        mLocationClient.stopLocation();
//                    }else {
//                        //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
//                        Log.e("AmapError","location Error, ErrCode:"
//                                + aMapLocation.getErrorCode() + ", errInfo:"
//                                + aMapLocation.getErrorInfo());
//                      requestLocation();
//                    }
//                }
//            }
//        };
    }


    /**
     * 打开相册
     */
    private void openAlbum(){
        Intent intent = new Intent ("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent,CHOOSE_PHOTO);//打开相册
    }

    //添加闹钟
    private void addAlarm(){
        AlarmManager alarm = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
             Intent alarmIntent =new Intent("finmins.example.FinMins.broadcasttest.MY_BROADCAST");
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
   //对图片进行优化
    public Bitmap setBitmap(Bitmap bitmap,float big , float rangle){
        Matrix matrix = new Matrix();
        //设置图片的大小
        matrix.setScale(big, big);
        Bitmap bitmapTmp = Bitmap.createBitmap( bitmap, 0, 0,  bitmap.getWidth(), bitmap.getHeight(), matrix, false);
        //将图片旋转xx度
        matrix.setRotate(rangle);
        return compressImage( Bitmap.createBitmap( bitmapTmp, 0, 0,  bitmapTmp.getWidth(), bitmapTmp.getHeight(), matrix, false));

    }

    public static Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 1, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 90;
        while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset(); // 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;// 每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        return bitmap;
    }


}