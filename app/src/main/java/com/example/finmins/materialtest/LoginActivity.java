package com.example.finmins.materialtest;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;


import com.example.finmins.materialtest.Model.LoginViewModel;
import com.example.finmins.materialtest.Model.MainViewModel;


public class LoginActivity extends AppCompatActivity {
    private LoginViewModel loginViewModel; //用户登录model
    private EditText loginUserName;   //用户名框
    private EditText loginUserPassowrd;   //密码框
    private Button loginButton;   //登录按钮
    private Button registButton;   //注册按钮
    private String password ;  //登录密码
    private String mingzi ;  //用户名字
    private int touxiang  ; //用户头像
    private String  email;   //邮箱
    private HttpClientUtils httpClientUtils = new HttpClientUtils();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();





//        监听是否登录成功



        //进入注册界面
        registButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
           Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
           startActivity(intent);
        }
        });


        //登录按钮响应
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
           //  判断输入框是否为空
              if(  checkInput()){
                  //不是则把密码和用户名传进去判断能否登陆
                if(  loginViewModel.login(loginUserName.getText().toString(),loginUserPassowrd.getText().toString())==1){
                    //密码邮箱正确。
                       //把得到的头像和名字传回去
                    password = loginViewModel.userPassword;
                    mingzi = loginViewModel.userName;
                    touxiang = loginViewModel.userImage;
                    email  =  loginViewModel.userEmail;

                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                    intent.putExtra("loginPassword",password);
                    intent.putExtra("loginMingZi",mingzi);
                    intent.putExtra("loginTouXiang",touxiang);
                    intent.putExtra("loginYouXiang",email);
                    startActivity(intent);
                  }
                else {
//                    登陆失败
                    Toast.makeText(LoginActivity.this, "登陆失败", Toast.LENGTH_SHORT).show();
                }
              }
            }
        });

        //观察登录状态
//        loginViewModel.getIsLogin().observe(this, new Observer<Integer>() {
//            @Override
//            public void onChanged(Integer integer) {
//                //登录状态变为1
//                if(integer ==1){
//                    Toast.makeText(LoginActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
//                    //退出登录界面
//                    finish();
//                }
//                if(integer ==0){
//                    Toast.makeText(LoginActivity.this, "账户密码错误", Toast.LENGTH_SHORT).show();
//                }
//
//            }
//        });






    }

    //初始化元素
    private void init(){
        loginUserName = findViewById(R.id.userName);
        loginUserPassowrd = findViewById(R.id.userPassword);
        loginButton = findViewById(R.id.login);
        registButton = findViewById(R.id.registButton);
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);


        };





    //判断点击按钮时输入框是否为空
    private Boolean checkInput(){
        if(loginUserName.getText().toString()!=null &&loginUserName.getText().toString()!=null){
            Log.d("", "check正确");
            return true;
        }

          else {
            Log.d("", "check错误");
              return  false;
        }
    }

}
