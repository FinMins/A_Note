package com.example.finmins.materialtest;

public class MemberInGroup {
    private String groupName  ;//所在群的名字
    private int mannerImgId ; //成员头像
    private String mannerName ;//成员名字
    private String mannerEmail; //成员邮箱
//    private int mannerIsFinished ; //成员是否完成
//    private int mannerIsVip ;   //成员是否是管理员
    private String finishedDate;    //成员完成年份;
      //成员能完成填
//    private int isSelf ; //是否是自己


    //构造函数（id,名字,是否完成,完成年，完成月，完成天）
    public  MemberInGroup(String groupName,String email,int imgid,String name ,String finishedDate){
//        this.mannerIsVip=mannerIsVip;
        this.groupName = groupName;
        this.mannerEmail = email;
        this.mannerName=name;
//        this.mannerIsFinished=mannerIsFinished;
        this.finishedDate=finishedDate;

        this.mannerImgId=imgid;
//        this.isSelf = isSelf;
    }
    //设置成员所在群名字
       public void setGroupName(String s){
        this.groupName = s;
       }
    //返回成员所在群名字
       public  String getGroupName(){
        return  groupName;
       }
    //设置邮箱
    public void setMannerEmail(String s){
        this.mannerEmail = s;
    }
    //返回油箱
    public String getMannerEmail(){
        return this.mannerEmail;
    }


    //设置是否是自己
//    public void setIsSelf(int isSelf){this.isSelf = isSelf;};
    //设置成员id
//    public void setMannerId(int id ){
//        this.id = id ;
//    }
    //设置成员头像
    public void setMannerImgId(int id ){
        this.mannerImgId = id ;
    }
    //设置成员名字
    public void setMannerName(String name ){
        this.mannerName = name ;
    }
    //设置成员是否完成
//    public void setMannerIsFinished(int finished ){
//        this.mannerIsFinished = finished ;
//    }
    //设置成员是否是管理员
//    public void setMannerIsVip(int vip ){
//        this.mannerIsVip = vip ;
//    }
    //设置成员完成时间
    public void setFinishedDate(String  year ){
        this.finishedDate = year;
    }
//    //设置成员完成月份
//    public void setFinishedMonth(String  month ){
//        this.finishedMonth = month;
//    }
//    //设置成员完成天
//    public void setFinishedDay(String  day ){
//        this.finishedDay = day;
//    }

//
//    //返回id
//    public int getId(){
//        return this.id;
//    }

    //返回成员头像
    public  int getMannerImgId(){
        return  this.mannerImgId;
    }
    //返回成员名字
    public String getMannerName(){
        return this.mannerName;
    }
    //返回成员是否完成
//    public  int getMannerIsFinished(){
//        return  this.mannerIsFinished;
//    }
    //返回成员是否是管理员
//    public  int getMannerIsVip(){
//        return  this.mannerIsVip;
//    }

    //返回成员完成年份
    public String getFinishedDate( ){
        return this.finishedDate;
    }
    //返回成员完成月份
//    public String getFinishedMonth( ){
//        return this.finishedMonth;
//    }
//    //返回成员完成天
//    public String getFinishedDay( ){
//        return this.finishedDay;
//    }
//    //返回是否是自己
//    public int getIsSelf(){ return  this.isSelf;};





}

