package com.example.finmins.materialtest;

import android.app.AlarmManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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
                Intent intent =new Intent(InsertActivity.this,AlarmActivity.class);
                startActivity(intent);
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
                 /**使用sqlite添加数据
                     SQLiteDatabase db = dbHelper.getWritableDatabase();
                     ContentValues values = new ContentValues();
                     //开始组装第一条数据
                     values.put("biaoti", biaoti.getText().toString());
                    values.put("neirong", neirong.getText().toString());
                    values.put("year",Calendar.getInstance().get(Calendar.YEAR));
                    values.put("month",Calendar.getInstance().get(Calendar.MONTH)+1);
                    values.put("day",Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                     values.put("time",
                                  String.valueOf(Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) +"时"+
                                  String.valueOf(Calendar.getInstance().get(Calendar.MINUTE) )+"分"+
                                  String.valueOf(Calendar.getInstance().get(Calendar.SECOND))+"秒");
                     db.insert("ShiJian",null,values);
                  **/
               ShiJian shijian =new ShiJian();
                shijian.setBiaoti(biaoti.getText().toString());
                shijian.setNeirong(neirong.getText().toString());
                 shijian.setYear(Calendar.getInstance().get(Calendar.YEAR));
                 shijian.setMonth(Calendar.getInstance().get(Calendar.MONTH)+1);
                 shijian.setDay(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                 shijian.setTime( String.valueOf(Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) +"时"+
                         String.valueOf(Calendar.getInstance().get(Calendar.MINUTE) )+"分"+
                         String.valueOf(Calendar.getInstance().get(Calendar.SECOND))+"秒");
                  shijian.setId(1);
                 shijian.save();
                 Toast.makeText(InsertActivity.this,"事件添加成功！",Toast.LENGTH_SHORT).show();
             }
            }
        });

    }
  //初始化控件
    void initSet(){
        biaoti=(EditText)findViewById(R.id.editText_biaoti);
        neirong=(EditText)findViewById(R.id.editText_neirong);
        tianjia  = (FloatingActionButton)findViewById(R.id.tianjia);
        naozhong = (ImageButton)findViewById(R.id.naozhong);
        yuyin =(ImageButton) findViewById(R.id.yuyin);
    }

}