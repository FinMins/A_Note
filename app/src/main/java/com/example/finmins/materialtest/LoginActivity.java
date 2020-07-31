package com.example.finmins.materialtest;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;


import com.example.finmins.materialtest.Model.LoginViewModel;


public class LoginActivity extends AppCompatActivity {
    private LoginViewModel loginViewModel; //用户登录model
    private EditText loginUserName;   //用户名框
    private EditText loginUserPassowrd;   //密码框
    private Button loginButton;   //登录按钮
    private Button registButton;   //注册按钮

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();


        //登录按钮响应
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //判断输入框是否为空
                //不是则把密码和用户名传进去判断能否登陆
                //登陆成功退出当前界面

            }
        });

        //注册按钮响应
        registButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
     //进入注册界面
            }
        });


    }

    //初始化元素
    private void init(){
        loginUserName = findViewById(R.id.userName);
        loginUserPassowrd = findViewById(R.id.userPassword);
        loginButton = findViewById(R.id.login);
        registButton = findViewById(R.id.regist);
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
    }


    //判断点击按钮时输入框是否为空
    private Boolean checkInput(){
        if(loginUserName.getText().toString()!=null &&loginUserName.getText().toString()!=null)
            return true;
        else return  false;
    }

}
