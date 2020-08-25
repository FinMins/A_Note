package com.example.finmins.materialtest.Model;

import android.icu.util.Freezable;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.finmins.materialtest.Friend;
import com.example.finmins.materialtest.HttpClientUtils;
import com.example.finmins.materialtest.ShiJian;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class FriendViewModel extends ViewModel {
    private MutableLiveData<List<Friend>> friendList;     //朋友
    private MutableLiveData<String> searchName;//搜索出来的用户名
    private MutableLiveData<Integer> searchImg;  //搜索用户头像
    private  MutableLiveData<List<ShiJian>> getShiJianList; //接受事件
    private MutableLiveData<ShiJian> shareShiJian ;  //分享事件
    private MutableLiveData<String> userEmail ;   //用户邮箱
    private List<Friend>  tmpfriendlist = new ArrayList<Friend>() ;
   private HttpClientUtils httpClientUtils = new HttpClientUtils();   //请求模块

    //获取登陆的账户
    public MutableLiveData<String> getUserEmail(){
        if (userEmail==null){
            userEmail = new MutableLiveData<String >();
            userEmail.setValue(null);
        }
        return userEmail;
    }

    //获取数据库里要分享的事件
    public  MutableLiveData<ShiJian> getShareShiJian(){
        if (getShareShiJian()== null) {
            shareShiJian = new MutableLiveData<ShiJian>();
            shareShiJian.setValue(null);
        }
        return shareShiJian;
    }

    //获取friendlist
    public MutableLiveData<List<Friend>> getFriendList() {
        if (friendList == null) {
            friendList = new MutableLiveData<List<Friend>>();
            friendList.setValue(tmpfriendlist);
        }
        return friendList;
    }

    //获取搜索的用户名字
    public MutableLiveData<String> getSearchName() {
        if (searchName == null) {
            searchName = new MutableLiveData<String>();
            searchName.setValue(null);
        }
        return searchName;
    }

    //获取搜索的用户头像
    public MutableLiveData<Integer> getSearchImg() {
        if (searchImg == null) {
            searchImg = new MutableLiveData<Integer>();
            searchImg.setValue(null);
        }
        return searchImg;
    }
   //获取好友分享的事件
    public MutableLiveData<List<ShiJian>> getGetShiJianList(){
        if(getShiJianList==null){
            getShiJianList = new MutableLiveData<List<ShiJian>>();
            getShiJianList.setValue(null);
        }
        return getShiJianList;
    }
    //设置从好友那来的事件合集
    public void setGetShiJianList(List<ShiJian> a){
        getShiJianList.setValue(a);
    }
    //设置用户账号
    public void setUserEmail(String Num){
        if (userEmail==null){
            userEmail = new MutableLiveData<String >();
            userEmail.setValue(null);
        }
       this.userEmail.setValue(Num);
    }
    //设置好友列表
    public void setFriendList(List<Friend> a){
        friendList.setValue(a);
    }
    //设置搜索好友的头像
    public void setSearchImg(int a  ){
        searchImg.setValue(a);
    }
    //设置搜索好友的名字
    public void setSearchName(String name){
        searchName.setValue(name);
    }
    //设置要分享的事件
    public void setShareShiJian(ShiJian a){
        shareShiJian .setValue(a);
    }

    //从数据库里得到好友列表
    public void requestFriendList(){
        String respose;
        List<Friend> listfriend = new ArrayList<Friend>();
         respose =     httpClientUtils.send("","","");
        //将response转换成List<Friend>形式



        //设置好友列表
         setFriendList(listfriend);
    }

    //从数据库里得到搜索的头像
    public Boolean requestSearchImg(){
        int response;
        //请求创建并返回
        response = Integer.parseInt( httpClientUtils.send("","",""));

        setSearchImg(response);
        return  true ;

    }

    //从数据库里得到搜索的名字
    public  Boolean requestSearchName(){
        String response;
        //传入邮箱得到用户的名字  ：参数  String 邮箱  ，返回值： success: 1 or 0 ;  name :Sting
        response = httpClientUtils.send("","","");


        setSearchName(response);
        return true;
    }



    //从数据库里得到要接收的事件
    public void requestShiJian(){
        String response;
        List<ShiJian>  listshijian = new ArrayList<ShiJian>();
        //传入用户的 邮箱  ： 参数：String 邮箱，返回值：{ success:1 or 0 ,shijian:["biaoti:"标题",neirong      ]  }
       response =   httpClientUtils.send("","","");
       //转化成list<ShiJian>形式

        //
        setGetShiJianList(listshijian);
    }

    //把要分享的事件发送给后端：
    public  int requestShareShiJian(ShiJian shiJian){
        String response;
        //创建请求并返回
        response=  httpClientUtils.send(" "," ","");

        //成功则返回1，失败则返回0；
        return 0 ;
    }



    //删除
    public int deleteFriend(String email){
        String response;
        //创建请求并返回
        response= httpClientUtils.send("","","");


       return 1;
    }

    public int addFriend( String selfemail,String feiemail){
        String response;
        response = httpClientUtils.send("","","");


        return 1 ;
    }



}



