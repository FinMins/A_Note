package com.example.finmins.materialtest.Model;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.finmins.materialtest.ShiJian;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import kotlin.contracts.Returns;

public class MainViewModel extends ViewModel {
    private MutableLiveData<Integer> isLogined  ;  //是否登录
    private MutableLiveData<String >userName ;  //用户名
    private MutableLiveData<String > userPassword;  //密码
  //  private String userEmail;    用户邮箱
  // private String userNamel;    //用户的名字
   private MutableLiveData<String > userEmail;    // 用户邮箱
 //  private MutableLiveData<String > userXingming;     //用户的名字
    private MutableLiveData<List<ShiJian>>  shiJianList ;//事件列表
    private List<ShiJian> shiJian = new ArrayList<ShiJian>();

    //是否已经登录
    public MutableLiveData<Integer> getIsLogined(){
        if(isLogined ==null) {
            isLogined = new MutableLiveData<Integer>();
            isLogined.setValue(1);
        }
        return isLogined;
    }

/*
    public String getUserName(){
        if(userName ==null) {
          userName="";
        return null;
        }
        return userName;
    }
*/
//获取事件数据
    public  MutableLiveData<List<ShiJian>> getShiJian(){
        if (shiJianList ==null){
            shiJianList = new MutableLiveData<List<ShiJian>>();
            shiJianList.setValue(shiJian);
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
/*
    public String getUserPassword() {
        if(userPassword ==null) {
            userPassword= "";
            return  null;
        }
        return  userPassword;
    }
*/
//获取密码
public MutableLiveData<String> getUserPassword(){
    if(userPassword ==null) {
        userPassword = new MutableLiveData<String >();
        userPassword.setValue("xxxxx");
        return  userPassword;
    }
    return userPassword;
}
/*
//获取名字
    public MutableLiveData<String> getUserXingming(){
        if(userXingming ==null) {
            userXingming = new MutableLiveData<>();
            userXingming.setValue("xxxxx");
            return  null;
        }
        return userXingming;
    }

 */
//获取邮箱
    public MutableLiveData<String> getUserEmail(){
        if(userEmail==null) {
            userEmail = new MutableLiveData<String >();
            userEmail.setValue("xxxxx@xxx.com");
            return userEmail;
        }
        return userEmail;
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

    //从数据库得到事件列表
    public  void setShiJianList(){
        List<ShiJian>  shijians = DataSupport.findAll(ShiJian.class);
        for(ShiJian shijian:shijians){
            shiJianList.getValue().add(shijian);
        }
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
