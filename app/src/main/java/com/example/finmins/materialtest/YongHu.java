package com.example.finmins.materialtest;

import com.alibaba.fastjson.annotation.JSONField;
import com.example.finmins.materialtest.Friend;
import com.example.finmins.materialtest.Group;
import com.example.finmins.materialtest.ShiJian;

import java.util.List;

public class YongHu {
    @JSONField(name = "touxiang ")
    private int touxiang ;    //头像
    @JSONField(name = "youxiang ")
    private String youxiang;
    @JSONField(name = "mingzi")
    private String  mingzi ;
    @JSONField(name = "password")
    private String password ;
//    @JSONField(name = "FRIEND LIST ")
//    List<Friend>  friendList    ;   //拥有的好友
//    @JSONField(name = "GROUP LIST")
//    List<Group> groupList ;  //拥有的群
//    @JSONField(name = "LIST SHI JIAN")
//    List<ShiJian> listShiJian  ;//拥有的事件
//    @JSONField(name = "ADD LIST SHI JIAN ")
//    List<ShiJian> addListShiJian  ; //从好友处传过来的事件集。

    public  YongHu(int touxiang,String youxiang ,String mingzi ,String password){
        this.touxiang  = touxiang;
//        this.addListShiJian=addListSHiJian;
//        this.friendList = friendList;
//        this.groupList = groupList;
        this.mingzi = mingzi;
//        this.listShiJian =listShiJian;
        this.password = password;
        this.youxiang = youxiang;
    }
    public void setTouxiang(int touxiang){
        this.touxiang = touxiang;
    }

    public void setYouxiang(String youxiang){
        this.youxiang = youxiang;
    }

    public void setMingzi(String mingzi ){
        this.mingzi = mingzi;
    }

    public  void setPassword(String password){
        this.password = password;
    }

//    public void setFriendList(List<Friend> friendList){
//        this.friendList = friendList;
//    }
//
//    public void setGroupList(List<Group> groupList){
//        this.groupList = groupList;
//    }
//
//    public void setAddListShiJian(List<ShiJian> addListShiJian){
//        this.addListShiJian = addListShiJian;
//    }

  public  int getTouxiang(){
        return this.touxiang;
  }

  public String getYouxiang(){
        return this.youxiang;
  }

  public String getMingzi(){
        return this.mingzi;
  }

  public String getPassword(){
        return this.password;
  }

//  public List<ShiJian> getListShiJian(){
//        return this.listShiJian;}
//
//    public List<ShiJian> getAddListShiJian(){return this.addListShiJian;}
//
//    public List<Group>getGroupList(){
//        return this.groupList;
//    }
//
//    public List<Friend> getFriendList(){
//        return this.friendList;
//    }
//    public void setListShiJian(List<ShiJian> listShiJian){
//        this.listShiJian = listShiJian;
//    }

}
