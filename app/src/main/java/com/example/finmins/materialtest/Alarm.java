package com.example.finmins.materialtest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class Alarm extends AppCompatActivity{


    public  class AlarmRecviver extends BroadcastReceiver {
        public  void onReceive(Context context, Intent intent){
            Toast.makeText(Alarm.this,"添加成功",Toast.LENGTH_SHORT).show();
            }
        }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AlertDialog alert =new AlertDialog.Builder(this).create();
        alert.setTitle("这是测试");     //设置对话框的标题
        alert.setMessage("我太难了。。。");    //设置对话框的内容
       //添加确定按钮
        alert.setButton(DialogInterface.BUTTON_POSITIVE,"确定",new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog,int which){

            }
        });
   alert.show();

    }
}
