package com.example.finmins.materialtest.Model;

import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.finmins.materialtest.HttpClientUtils;


public class LoginViewModel extends ViewModel {
   public int userImage ;   //用户头像
   public String userEmail ; //用户邮箱
   public String userPassword; //用户密码
   public String userName ; //用户名字
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
    public int login(final String userEmailtem, final String passWord ){
        final String requesbody = " { \"youxiang\":\""+userEmailtem+"\", \"password\":\""+passWord+"\" }";
//       final String requesbody = " { \"youxiang\":\""+userName+"\", \"password\":\""+passWord+"\" }";
        String a = httpClientUtils.sendPostByOkHttp("http://172.18.95.221:9999/yonghu/login",requesbody);
        if(a !=null){
//            setIsLogined(1);
            JSONObject jb = JSON.parseObject(a);
            userEmail = jb.getString("youxiang");
            userImage = jb.getInteger("touxiang");
             userName = jb.getString("mingzi");
            userPassword = jb.getString("password");
//            setUserInf(youxiang,touxiang,mingzi);
//            Log.d("取出邮箱", youxiang);
           Log.d("取出名字", userName);
//            Log.d("取出头像", touxiang);
            return 1;
        }
        return 0;
    }
}
