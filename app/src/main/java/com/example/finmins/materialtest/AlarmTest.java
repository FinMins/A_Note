package com.example.finmins.materialtest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;




public class AlarmTest extends BroadcastReceiver {
    public AlarmTest() {
        Log.d("AlarmTest.this", "闹钟跳到这个广播里面来了！！ ");
    }

    @Override
    public void onReceive(Context context, Intent intent) {

      Log.d("context","log运行了");
        AlertDialog alert =new AlertDialog.Builder(context).create();
        alert.setTitle("这是测试");     //设置对话框的标题
        alert.setMessage("我太难了。。。");    //设置对话框的内容
        alert.show();

        throw new UnsupportedOperationException("Not yet implemented");
    }
}
