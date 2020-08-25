package com.example.finmins.materialtest.Model;

import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.finmins.materialtest.HttpClientUtils;


public class LoginViewModel extends ViewModel {
    private ImageView userImage ;   //用户头像
    private String userEmail ; //用户邮箱
    private String userPassword; //用户密码
    private HttpClientUtils httpClientUtils = new HttpClientUtils();   //请求
    final   String  URL = "http://localhost:3000" ;          //本地服务器网址
    private MutableLiveData<Integer> isLogin ;

    //设置登录状态
    private void setIsLog(Integer a  ){
        isLogin.setValue(a);
    }


    //是否已经登录
    public MutableLiveData<Integer> getIsLogin(){
        if(isLogin==null) {
          isLogin = new MutableLiveData<Integer>();
            isLogin.setValue(2);
        }
        return isLogin;
    }

    //登录的请求 username,password
//    假设验证成功返回1 ，失败返回0
    public void login(final String userName, final String passWord ){
          String a =  httpClientUtils.send("get",URL+"?username="+userName+"&password="+passWord,"");
          if(Integer.parseInt(a)==1);
            }


}
