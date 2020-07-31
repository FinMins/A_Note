package com.example.finmins.materialtest;

public class Group {
    private int groupImg_id  ;  //群头像
    private String groupName ; //群名
    private int id ;    //群id

    public Group(int id ,String name ){
        this.groupImg_id =id;
        this.groupName = name;
    }



    //设置群头像
    public void  setGroupImgId(int groupImgId){
        this.groupImg_id = groupImgId;
    }
    //设置群名字
    public void setGroupName(String groupName){
        this.groupName = groupName;
    }
    //返回群头像
    public int getGroupImgId(){
        return this.groupImg_id;
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
