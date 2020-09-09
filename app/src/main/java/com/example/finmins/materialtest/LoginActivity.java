package com.example.finmins.materialtest;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.finmins.materialtest.Model.LoginViewModel;


public class LoginActivity extends AppCompatActivity {
    private LoginViewModel loginViewModel; //用户登录model
    private EditText loginUserName;   //用户名框
    private EditText loginUserPassowrd;   //密码框
    private Button loginButton;   //登录按钮
    private Button registButton;   //注册按钮
    private String password ;  //登录密码
    private String mingzi ;  //用户名字
    private String touxiang  ; //用户头像
    private String  email;   //邮箱
    private final  String URL= "http://192.168.43.61:9999";
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
                if(  login(loginUserName.getText().toString(),loginUserPassowrd.getText().toString())==1){
                    //密码邮箱正确。
                       //把得到的头像和名字传回去
//                    password = loginViewModel.userPassword;
//                    mingzi   = loginViewModel.userName;
//                   touxiang = loginViewModel.userImage;
//                    email    =  loginViewModel.userEmail;

                     Intent intent = new Intent(LoginActivity.this,MainActivity.class);
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


    public int login(final String userEmailtem, final String passWord ){
        final String requesbody = " { \"youxiang\":\""+userEmailtem+"\", \"password\":\""+passWord+"\" }";
        String a = httpClientUtils.sendPostByOkHttp(URL+"/yonghu/login",requesbody);
        if(a !=null){
//            setIsLogined(1);
            JSONObject jb = JSON.parseObject(a);
            email = jb.getString("youxiang");
             touxiang = jb.getString("touxiang");
                 mingzi    = jb.getString("mingzi");
//                       email    =     String  userPassword = jb.getString("password");
//            jb.getInnerMap(
//            setUserInf(youxiang,touxiang,mingzi);
//            Log.d("取出邮箱", youxiang);
//            Log.d("取出名字", userName);
            Log.d("取出头像", touxiang);
            return 1;
        }
        return 0;
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
