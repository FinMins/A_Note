package com.example.finmins.materialtest.Model;

import android.widget.ImageView;

import androidx.lifecycle.ViewModel;

import com.example.finmins.materialtest.HttpClientUtils;

public class LoginViewModel extends ViewModel {
    private ImageView userImage ;   //用户头像
    private String userEmail ; //用户邮箱
    private String userPassword; //用户密码
    private HttpClientUtils httpClientUtils ;   //请求
    final   String  URL = "http://localhost:3000" ;          //本地服务器网址

    //登录的请求 username,password
//    假设验证成功返回1 ，失败返回0
    public int login(String userName,String passWord ){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String lres=    httpClientUtils.send("get",URL," ");
            }
        })
         String logined =    httpClientUtils.send("get",URL," ");
        if(logined.equals(1));

    }


}
