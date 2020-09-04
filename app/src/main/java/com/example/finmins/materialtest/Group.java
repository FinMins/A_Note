package com.example.finmins.materialtest;

import com.alibaba.fastjson.annotation.JSONField;

public class Group {
    @JSONField(name = "GROUP IMG ID")
    private int groupImgId  ;  //群头像
    @JSONField(name = "GROUP NAME")
    private String groupName ; //群名
    private int id ;    //群id
    @JSONField(name = "GROUP VIP EMAIL")
    private String groupVipEmail ;//群管理员邮箱

    public Group(int id ,String name ){
        this.groupImgId =id;
        this.groupName = name;
    }

//设置群邮箱
   public  void  setGroupVipEmail(String email){
        this.groupVipEmail = email;
   }

    public  String getGroupVipEmail(){
        return groupVipEmail ;
    }

    //设置群头像
    public void  setGroupImgId(int groupImgId){
        this.groupImgId = groupImgId;
    }
    //设置群名字
    public void setGroupName(String groupName){
        this.groupName = groupName;
    }
    //返回群头像
    public int getGroupImgId(){
        return this.groupImgId;
    }
    //返回群名字
    public String getGroupName(){
        return this.groupName;
    }
    //返回群ID
    public  int getId(){
        return this.id;
    }




}
