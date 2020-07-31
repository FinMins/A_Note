package com.example.finmins.materialtest.Model;

import android.icu.util.Freezable;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.finmins.materialtest.Friend;
import com.example.finmins.materialtest.ShiJian;

import java.util.List;

public class FriendViewModel extends ViewModel {
    private MutableLiveData<List<Friend>> friendList;     //朋友
    private MutableLiveData<String> searchName;//搜索出来的用户名
    private MutableLiveData<Integer> searchImg;  //搜索用户头像
    private  MutableLiveData<ShiJian> getShiJian; //接受事件
    private MutableLiveData<List<ShiJian>> shareShiJian ;  //分享事件
    private MutableLiveData<String> userNum ;   //用户账号


    //获取登陆的账户
    public MutableLiveData<String> getUserNum(){
        if (userNum==null){
            userNum = new MutableLiveData<String >();
            userNum.setValue(null);
        }
        return userNum;
    }

    //获取数据库里要分享的事件
    public  MutableLiveData<List<ShiJian>> getShareShiJian(){
        if (getShareShiJian()== null) {
            shareShiJian = new MutableLiveData<List<ShiJian>>();
            shareShiJian.setValue(null);
        }
        return shareShiJian;
    }

    //获取friendlist
    public MutableLiveData<List<Friend>> getFriendList() {
        if (friendList == null) {
            friendList = new MutableLiveData<List<Friend>>();
            friendList.setValue(null);
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
    public MutableLiveData<ShiJian> getGetShiJian(){
        if(getShiJian==null){
            getShiJian = new MutableLiveData<ShiJian>();
            getShiJian.setValue(null);
        }
        return getShiJian;
    }

    //设置用户账号
    public void setUserNum(String Num){
        if (userNum==null){
            userNum = new MutableLiveData<String >();
            userNum.setValue(null);
        }
       this.userNum.setValue(Num);
    }


    //从数据库里得到好友列表

    //从数据库里得到搜索的头像


    //从数据库里得到搜索的名字

    //从数据库里得到分享的事件
}



