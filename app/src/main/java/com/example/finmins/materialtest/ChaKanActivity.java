package com.example.finmins.materialtest;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import org.litepal.crud.DataSupport;

import java.util.Calendar;
import java.util.List;

public class ChaKanActivity extends AppCompatActivity {
    private FloatingActionButton tianjia ;  //添加按钮
    private ImageButton naozhong;   //
    private ImageButton yuyin;

    private EditText biaoti;
    private EditText neirong;
    private ImageButton fanhui;
    private  int shiJian_id;
    private int hour_alarm;
    private int minute_alarm;
    private int isAlarm;
    private Uri imageUri;
    private ImageView myphoto;
    private Bitmap bitmap;
    private byte[] images = new byte[1024];
     private int positions;
    private int ids;
    private EditText biaoti_tem;
    private EditText neirong_tem;
    private ShiJian shijian;
    private MyDatabaseHelper dbHelper;
    private ImageButton xiugai;
    private ImageView photo;
    private  ImageButton dui;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cha_kan);
        //完成初始化界面
        getPosition();

        dui.setOnClickListener(new  View.OnClickListener(){
            public void onClick(View v){
                dbHelper.getWritableDatabase();
                //判断标题是否为空
                if(TextUtils.isEmpty(biaoti_tem.getText().toString()))
                {
                    Toast.makeText(ChaKanActivity.this,"标题不能为空！",Toast.LENGTH_SHORT).show();
                } else {
                    ShiJian shijian =new ShiJian();

                    shijian.setBiaoti(biaoti_tem.getText().toString());

                    shijian.setNeirong(neirong_tem.getText().toString());
                    shijian.update(ids);
                    Toast.makeText(ChaKanActivity.this,"事件修改成功！",Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });



    }



    public void getPosition(){
        Intent intent=getIntent();
        positions =intent.getIntExtra("positions",1);
        ids = intent.getIntExtra("ids",1);
        dui=(ImageButton)findViewById(R.id.dui);   //修改按钮
         photo = (ImageView)findViewById(R.id.myphoto);
        biaoti_tem=(EditText)findViewById(R.id.editText_biaoti);
        neirong_tem=(EditText)findViewById(R.id.editText_neirong);
        fanhui = (ImageButton)findViewById(R.id .fanhui);  //返回按钮。
        shijian=DataSupport.find(ShiJian.class,ids);

        biaoti_tem.setText(shijian.getBiaoti());
        neirong_tem.setText(shijian.getNeirong());
        if(shijian.getPhoto()!=null)
          photo.setImageBitmap(shijian.getPhoto());

        dbHelper=new MyDatabaseHelper(this,"ShiJian.db",null,1);  //创建事件数据库
    }

}
