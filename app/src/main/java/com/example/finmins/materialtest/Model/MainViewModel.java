package com.example.finmins.materialtest.Model;

import android.util.Log;
import android.widget.ImageView;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.finmins.materialtest.HttpClientUtils;
import com.example.finmins.materialtest.R;
import com.example.finmins.materialtest.ShiJian;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class MainViewModel extends ViewModel {
    private MutableLiveData<Integer> isLogined  ;  //是否登录
    private MutableLiveData<String >userName ;  //用户名
    private MutableLiveData<String > userPassword;  //密码
   private MutableLiveData<String > userEmail;    // 用户邮箱
   private MutableLiveData<String > userXingming;     //用户的名字
    private MutableLiveData<List<ShiJian>>  shiJianList ;//事件列表
    private ImageView userImage ;   //用户头像
    private MutableLiveData<Integer> userImgId;    //用户头像数字
    private String userLoginEmail ; //用户邮箱
    private String userLoginPassword; //用户密码
    private HttpClientUtils httpClientUtils = new HttpClientUtils();   //请求
    final   String  URL = "http://localhost:3000" ;          //本地服务器网址
    private MutableLiveData<Integer> isLogin ;
    private List<ShiJian> temshiJian = new ArrayList<ShiJian>();


    //设置登录状态
    public void setIsLogin(int a  ){
        isLogin.setValue(a);
    }


    //登录的请求 username,password
//    假设验证成功返回1 ，失败返回0
    public int login(final String userName, final String passWord ){
        String a =  httpClientUtils.send("get",URL+"?username="+userName+"&password="+passWord,"");
        if(Integer.parseInt(a)==1){
            setIsLogined(1);
            setUserInf(userName);
            return 1;
        }

        else return  0;
    }

    //获取用户头像数字
    public MutableLiveData<Integer>  getuserImgId(){
        if(userImgId== null){
            userImgId = new MutableLiveData<Integer>();
            userImgId.setValue(R.drawable.header);
        }
        return userImgId;
    }

    //是否登录成功   login专用
    public MutableLiveData<Integer> getIsLogin(){
        if(isLogin==null) {
            isLogin = new MutableLiveData<Integer>();
            isLogin.setValue(2);
        }
        return isLogin;
    }




    //是否已经登录     main专用。
    public MutableLiveData<Integer> getIsLogined(){
        if(isLogined ==null) {
            isLogined = new MutableLiveData<Integer>();
            isLogined.setValue( 1);
        }
        return isLogined;
    }


//获取事件数据
    public  MutableLiveData<List<ShiJian>> getShiJianList(){
        if (shiJianList ==null){
            shiJianList = new MutableLiveData<List<ShiJian>>();
            shiJianList.setValue(temshiJian);
            return shiJianList;
        }
        setShiJianList();
        return shiJianList;
    }

//获取名字
public MutableLiveData<String> getUserName(){
    if(userName ==null) {
        userName= new MutableLiveData<String>();
        userName.setValue("");
        return  userName;
    }
    return userName;
}

//获取密码
public MutableLiveData<String> getUserPassword(){
    if(userPassword ==null) {
        userPassword = new MutableLiveData<String >();
        userPassword.setValue("xxxxx");
        return  userPassword;
    }
    return userPassword;
}



//获取邮箱
    public MutableLiveData<String> getUserEmail(){
        if(userEmail==null) {
            userEmail = new MutableLiveData<String >();
            userEmail.setValue("xxxxx@xxx.com");
            return userEmail;
        }
        return userEmail;
    }

    //注册用户
    public int registerUser(){
        String response;
        response = httpClientUtils.send("","","");

       return Integer.parseInt(response);
    }



    //设置用户名
    public void setUserName(String username){
        userName.setValue(username);
    }
    //设置密码
    public void setUserPassword(String userpassword){
        userPassword.setValue(userpassword);
    }

    //设置邮箱
    public void setUserEmail(String usermail){
        userEmail.setValue(usermail);
    }

    //设置登录
    public void setIsLogined(Integer isLogin){
   isLogined.setValue(isLogin);
    }

    //设置头像
    public void setUserImgId(Integer imgId){
        userImgId.setValue(imgId);
    }

//设置用户信息到主界面
    public void setUserInf(String userName){
        //设置用户头像：
        setUserImgId(  requestImg(userName)) ;
        //设置用户邮箱
        setUserEmail(userName);
        //设置用户名字
        setUserName(requestName(userName));
    }


    //从数据库得到事件列表
    public  void setShiJianList(){
        List<ShiJian>  shijians = DataSupport.findAll(ShiJian.class);
        for(ShiJian shijian:shijians){
            shiJianList.getValue().add(shijian);
        }
        //
    //把事件给发送到后台
        httpClientUtils.send("","","");

    }

    //从数据库得到用户头像 ： 参数：用户邮箱String ,返回 头像int
    public int requestImg(String email){
        return Integer.parseInt(httpClientUtils.send("","","")  );
    }

    //从数据库获取用户名字： 参数：用户邮箱:String ,返回 名字String
    public String requestName(String email){
        return  httpClientUtils.send("","","");
    }


}
