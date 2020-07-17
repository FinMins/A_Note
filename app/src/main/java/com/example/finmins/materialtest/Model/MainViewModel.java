package com.example.finmins.materialtest.Model;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import kotlin.contracts.Returns;

public class MainViewModel extends ViewModel {
    private int isLogined = 0;  //是否登录
    private String userName ;  //用户名
    private String userPassword;  //密码
   // private String userEmail;    用户邮箱
 //   private String userNamel;    //用户的名字
   private MutableLiveData<String > userEmail;
    private MutableLiveData<String > userXingming;

    //是否已经登录
    public  int getIsLogined(){

        return isLogined;
    }

    public String getUserName(){
        if(userName ==null) {
          userName="";

        }
        return userName;
    }

    public String getUserPassword() {
        if(userPassword ==null) {
            userPassword= "";
        }
        return  userPassword;
    }

    public MutableLiveData<String> getUserXingming(){
        if(userXingming ==null) {
            userXingming = new MutableLiveData<>();
            userXingming.setValue("xxxxx");
        }
        return userXingming;
    }

    public MutableLiveData<String> getUserEmail(){
        if(userEmail ==null) {
            userEmail = new MutableLiveData<>();
            userEmail.setValue("xxxxx@xxx.com");
        }
        return userEmail;
    }
    //设置已经登录
    public  void setIsLogined(){
        isLogined = 1;
    }

    //设置用户名
    public void setUserName(String username){
        userName = username;
    }
    //设置密码
    public void setUserPassword(String userpassword){
        userPassword = userPassword;
    }

    //设置用户名
    public void setUserEmail(String usermail){
        userEmail.setValue(usermail);
    }
    /*livedata
   private MutableLiveData<Integer> isLogined;  //是否登录
   private MutableLiveData<String> userName ;  //用户名
    private MutableLiveData<String> userPassword;  //密码

    //是否已经登录
    public  MutableLiveData<Integer> getIsLogined(){
        if(isLogined ==null) {
            isLogined = new MutableLiveData<>();
            isLogined.setValue(0);
        }
        return isLogined;
    }
    //返回用户名
    public MutableLiveData<String> getUserName(){
        if(userName ==null) {
            userName = new MutableLiveData<>();
            userName.setValue("");
        }
        return userName;
    }
  //返回密码
    public MutableLiveData<String> getUserPassword() {
        if(userPassword ==null) {
            userPassword = new MutableLiveData<>();
            userPassword.setValue("");
        }
        return  userPassword;
    }

    //设置已经登录
    public  void setIsLogined(){
        isLogined.setValue(1);
    }
    //设置用户名
    public void setUserName(String username){
        userName.setValue(username);
    }
    //设置密码
    public void setUserPassword(String userpassword){
        userPassword.setValue(userpassword);
    }


     */
}
