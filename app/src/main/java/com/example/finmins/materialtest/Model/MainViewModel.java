package com.example.finmins.materialtest.Model;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.finmins.materialtest.HttpClientUtils;
import com.example.finmins.materialtest.R;
import com.example.finmins.materialtest.ShiJian;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class MainViewModel extends ViewModel {
    private MutableLiveData<Integer> isLogined;  //是否登录
    public  MutableLiveData<String> userName;  //用户名
    private MutableLiveData<String> userPassword;  //密码
    private MutableLiveData<String> userEmail;    // 用户邮箱
    private MutableLiveData<List<ShiJian>> shiJianList;//事件列表
    private MutableLiveData<Integer> userImgId;    //用户头像数字
    private HttpClientUtils httpClientUtils = new HttpClientUtils();   //请求
    final String URL = "http://192.168.43.61:9999";          //本地服务器网址
    private MutableLiveData<Integer> isLogin;
    private List<ShiJian> temshiJian = new ArrayList<ShiJian>();


//    //设置登录状态
//    public void setIsLogin(int a  ){
//        isLogin.setValue(a);
//    }

//
//    //登录的请求 username,password
////    假设验证成功返回1 ，失败返回0
//    public int login(final String userName, final String passWord ){
//       final String requesbody = " { \"youxiang\":\"123456@qq.com\", \"password\":\"9666\" }";
////       final String requesbody = " { \"youxiang\":\""+userName+"\", \"password\":\""+passWord+"\" }";
//        String a = httpClientUtils.sendPostByOkHttp("http://172.18.95.221:9999/yonghu/login",requesbody);
//        if(a !=null){
////            setIsLogined(1);
//            JSONObject jb = JSON.parseObject(a);
//            String youxiang = jb.getString("youxiang");
//            String touxiang = jb.getString("touxiang");
//            String mingzi = jb.getString("mingzi");
//            String password = jb.getString("password");
//            setUserInf(youxiang,touxiang,mingzi);
//            Log.d("取出邮箱", youxiang);
//            Log.d("取出名字", mingzi);
////            Log.d("取出头像", touxiang);
//            return 1;
//        }
//
//
//
//
//        return 0;
//
//
//
//    }

    //获取用户头像数字
    public MutableLiveData<Integer> getuserImgId() {
        if (userImgId == null) {
            userImgId = new MutableLiveData<Integer>();
            userImgId.setValue(R.drawable.header);
        }
        return userImgId;
    }
//
//    //是否登录成功   login专用
//    public MutableLiveData<Integer> getIsLogin(){
//        if(isLogin==null) {
//            isLogin = new MutableLiveData<Integer>();
//            isLogin.setValue(2);
//        }
//        return isLogin;
//    }


    //是否已经登录     main专用。
    public MutableLiveData<Integer> getIsLogined() {
        if (isLogined == null) {
            isLogined = new MutableLiveData<Integer>();
            isLogined.setValue(1);
        }
        return isLogined;
    }


    //获取事件数据
    public MutableLiveData<List<ShiJian>> getShiJianList() {
        if (shiJianList == null) {
            shiJianList = new MutableLiveData<List<ShiJian>>();
            shiJianList.setValue(temshiJian);
            return shiJianList;
        }
        setShiJianList();
        return shiJianList;
    }

    //获取名字
    public MutableLiveData<String> getUserName() {
        if (userName == null) {
            userName = new MutableLiveData<String>();
            userName.setValue("1");
            return userName;
        }

//        Log.d("getusername", "进去了get");
        return userName;
    }

    //获取密码
    public MutableLiveData<String> getUserPassword() {
        if (userPassword == null) {
            userPassword = new MutableLiveData<String>();
            userPassword.setValue("xxxxx");
            return userPassword;
        }
        return userPassword;
    }


    //获取邮箱
    public MutableLiveData<String> getUserEmail() {
        if (userEmail == null) {
            userEmail = new MutableLiveData<String>();
            userEmail.setValue("1@qq.com");
            return userEmail;
        }
        return userEmail;
    }

    //注册用户
    public int registerUser() {
        String response;
        response = httpClientUtils.send("", "", "");

        return Integer.parseInt(response);
    }


    //设置用户名
    public void setUserName(String username) {
        Log.d("设置用户名：", username);
       //userName.setValue(username);
    }

    //设置密码
    public void setUserPassword(String userpassword) {
        userPassword.setValue(userpassword);
    }

    //设置邮箱
    public void setUserEmail(String usermail) {

        userEmail.setValue(usermail);
    }

    //设置登录
    public void setIsLogined(Integer isLogin) {
        isLogined.setValue(isLogin);
    }

    //设置头像
    public void setUserImgId(Integer imgId) {
        Log.d("设置了头像", imgId.toString());
        userImgId.setValue(imgId);
    }

    //设置用户信息到主界面
    public void setUserInf() {
        //设置用户头像：
        setUserImgId(userImgId.getValue());
        //设置用户邮箱
        setUserEmail(userEmail.getValue());
        //设置用户名字
        setUserName(userName.getValue());
    }


    //从数据库得到事件列表
    public void setShiJianList() {
        List<ShiJian> shijians = DataSupport.findAll(ShiJian.class);
        for (ShiJian shijian : shijians) {
            shiJianList.getValue().add(shijian);
        }

    }


    //删除事件
    public void deleteShiJian(String time){
        Log.d("gettime是", time);
        String delete = "   {\n" +
                "\"place\":\""+time+"\"\n" +
                "}\n  ";
        httpClientUtils.sendPostByOkHttp(URL+"/shijian/delete",delete);

    }
}

