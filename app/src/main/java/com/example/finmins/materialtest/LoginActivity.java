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
    private TextView loginUserName;   //用户名框
    private TextView loginUserPassowrd;   //密码框
    private Button loginButton;   //登录按钮
    private Button registButton;   //注册按钮

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(LoginViewModel.class);
        loginUserName = findViewById(R.id.userName);
        loginUserPassowrd = findViewById(R.id.userPassword);
        loginButton = findViewById(R.id.login);
        registButton = findViewById(R.id.regist);

        //登录按钮响应
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //注册按钮响应
        registButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }
}
