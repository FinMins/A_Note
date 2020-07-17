package com.example.finmins.materialtest;

import android.content.Intent;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;


import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

public class WelcomeActivity extends AppCompatActivity {
    private Timer timer;
  private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (getSupportActionBar()!=null){
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_welcome);

        TimerTask hello=new TimerTask() {
            @Override
            public void run() {
                Intent intent=new Intent(WelcomeActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        };

   imageView=(ImageView)findViewById(R.id.iv);
        timer=new Timer(true);     //注意：这里我已将timer设置为全局变量
        timer.schedule(hello,2000);




    }
}
