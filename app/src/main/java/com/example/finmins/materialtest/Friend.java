package com.example.finmins.materialtest;

import android.provider.ContactsContract;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class Friend {
    //ImageView touxiang;   //头像
    int id ;
    int touxiang;
    String mingzi;    //名字
    String youxiang ;   //邮箱
   //
   // Button methods;    //方法
    int methods ;
   // ImageView alert;   //提醒
    //ImageView messages;    //消息
    int alert;
    int messages;


    public Friend(int touxiang, String mingzi ,String  youxiang,int messages,int methods ){
        this.touxiang=touxiang;
        this.messages=messages;
        this.youxiang=youxiang;
        this.methods=methods;

      this.mingzi = mingzi;
    }
    public void setTouxiang(int id){
        this.id=id;
    }
    public void setMingzi(String mingzi){
        this.mingzi=mingzi; }

    public int getTouxiang(){
        return touxiang;
    }
    public String getMingzi(){
        return mingzi;
    }
    public String getyouxiang(){
        return youxiang ;
    }
    public int getMethods() {
        return methods;
    }
    public int getAlert(){
        return alert;
    }

    public int getMessages(){
        return messages;
    }
    public int getId(){return id;}
}
