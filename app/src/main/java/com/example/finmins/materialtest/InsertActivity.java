package com.example.finmins.materialtest;
import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
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
    public static  final int TAKE_PHOTO = 1;
    public static final  int  CHOOSE_PHOTO = 2;
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
    private byte[] images = new byte[1024];    //图片的二进制转化


    /**接受自己输入的图片并展示
     * 调节图片大小
     * 调节图片的显示效果
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){

            //返回图片
            case TAKE_PHOTO:
                if(resultCode == RESULT_OK){
                    try{
                        //将拍摄的照片显示出来
                        Bitmap bitmap1 = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        Matrix matrix = new Matrix();
                        //设置图片的大小
                        matrix.setScale(0.277f, 0.277f);
                        Bitmap bitmap2 = Bitmap.createBitmap( bitmap1, 0, 0,  bitmap1.getWidth(), bitmap1.getHeight(), matrix, false);
                        //将图片旋转xx度
                        matrix.setRotate(90f);
                        bitmap = Bitmap.createBitmap( bitmap2, 0, 0,  bitmap2.getWidth(), bitmap2.getHeight(), matrix, false);
                       //将图片展示出来
                        editTextPhoto.setImageBitmap(bitmap);
                    }catch (FileNotFoundException e){
                        e.printStackTrace();
                    }
                }
            case CHOOSE_PHOTO:
                if(resultCode == RESULT_OK){
                    //判断手机系统版本号
                    if (Build.VERSION.SDK_INT >= 19) {
                        //4.4及以上系统使用这个方法处理图片
                        handleImageOnKitKat(data);
                    }else{
                        //4.4以下系统使用这个方法处理图片
                        handleImageOnKitKat(data);
                    }
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
            editTextPhoto.setImageBitmap(bitmap);
        }else{
            Toast.makeText(this,"得到图片失败",Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(InsertActivity.this,"时间保存闹钟自动设置",Toast.LENGTH_SHORT).show();
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
                if(ContextCompat.checkSelfPermission(InsertActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(InsertActivity.this,new
                            String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);}
                else{
                    openAlbum();
                }
            }
        });





        /**单击相册图片时的功能
         *
         */
        addAlbum.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {


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
                startActivityForResult(intent,TAKE_PHOTO);
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
                    shijian.setYear(Calendar.getInstance().get(Calendar.YEAR));
                    shijian.setMonth(Calendar.getInstance().get(Calendar.MONTH)+1);
                    shijian.setDay(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                    shijian.setTime( String.valueOf(Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) +"时"+
                            String.valueOf(Calendar.getInstance().get(Calendar.MINUTE) )+"分"+
                            String.valueOf(Calendar.getInstance().get(Calendar.SECOND))+"秒");
                    shijian.setImgId(0);
                    if(bitmap!=null){
                        byte[] images = img(bitmap);
                        shijian.setPhoto(images);
                    }
                   if( shijian.save()){
                       Log.d("InsertActivity,this","save成功");
                   }
                    else {
                       Log.d("InsertActivity,this","save失败");
                   }
                    shiJian_id =shijian.getId();
                    if(isAlarm==1)
                        addAlarm();
                    Toast.makeText(InsertActivity.this,"事件添加成功！，id为"+String.valueOf(shiJian_id)+"标题为"+shijian .getBiaoti(),Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });


    }
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