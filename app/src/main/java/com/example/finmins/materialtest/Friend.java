package com.example.finmins.materialtest;

import android.provider.ContactsContract;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Friend {
    //ImageView touxiang;   //头像
    private int id ;   //好友ID
     private int touxiang;    //好友头像
    private String mingzi;    //好友名字
    private  String youxiang ;   //好友邮箱
   //
    private List<ShiJian> shareShiJians =new ArrayList<ShiJian>();

   // Button methods;    //方法

   // ImageView alert;   //提醒
    //ImageView messages;    //消息

   private int haveMessages;//是否有消息，有消息为1，没有消息为2

    public Friend(int touxiang, String mingzi ,String  youxiang ){
        this.touxiang=touxiang;
//        this.haveMessages=messages;
        this.youxiang=youxiang;
      this.mingzi = mingzi;
    }
//    //将分享的事件添加到朋友的分享事件组中
//   public void setShareShiJians(ShiJian shijian){
//        this.shareShiJians.add(shijian);
//   }

    //设置朋友的头像
    public void setTouxiang(int id){
        this.id=id;
    }
    //设置朋友的名字
    public void setMingzi(String mingzi){
        this.mingzi=mingzi; }
    //获取朋友的头像
    public int getTouxiang(){
        return touxiang;
    }
    //获取朋友的名字
    public String getMingzi(){
        return mingzi;
    }

    //获取朋友的邮箱
    public String getyouxiang(){
        return youxiang ;
    }

//    //返回分享的事件组给viewmodel
//    public List<ShiJian> getShareShiJians(){
//        return this.shareShiJians;
//    }

//    //返回有消息时的图片ID
//    public int getMessages(){
//        return haveMessages;
//    }
    //获取朋友的ID
    public int getId(){return id;}
}
