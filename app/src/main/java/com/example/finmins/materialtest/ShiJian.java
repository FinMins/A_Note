package com.example.finmins.materialtest;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.litepal.crud.DataSupport;

/**
 * Created by FinMins on 2019/11/18.
 */

public class ShiJian extends DataSupport {
    private String TAG = "ShiJian.this";
    private int imgId;
    private String biaoti;
    private String neirong;
    private String time;
    private int year;
    private int month;
    private int day;
    private int id;
    private byte[] photo;
    private String soundRecorderPath;



    public ShiJian(){

    };

    public void setImgId(int i){
        this.imgId=i;

    }
    public void setPhoto(byte[] photo){
        this.photo=photo;
//        Log.d(TAG,"Testshijan里photo为"+ String.valueOf(photo));
    }

    //设置ID
    public void setId(int id){

        this.id=id;
    }
    //设置录音路径
    public void setSoundRecorderPath(String soundRecorderPath){
        this.soundRecorderPath=soundRecorderPath;
    }
//设置天
    public void setDay(int day){

        this.day=day;
    }
//设置年份
    public void setYear(int year){

        this.year=year;
    }
//设置月份
    public void setMonth(int month){

        this.month=month;
    }
    //设置标题
    public void setBiaoti(String biaoti ){

        this.biaoti=biaoti;
    }
    //设置内容
    public  void setNeirong(String  neirong )
    {
        this.neirong=neirong;
    }
    //设置时间
    public void setTime(String time){
        this.time=time;
    }

    //获取标题
    public String getBiaoti()
    {
        return biaoti;
    }

  //获取内容
    public String getNeirong()
    {
        return neirong;
    }

    //获取年份
    public int getYear(){
        return year;
    }

    //获取ID
    public int getId()
    {return id;}

    //获取月份
    public int getMonth()
    {return month;}
   //获取录音路径
    public String getSoundRecorderPath(){
       return soundRecorderPath;
    }

    //获取天
    public int getDay()
    {return day;}

    //获取时间
    public String getTime()
    {return time;}

    public int get_imgId(){
        return this.imgId;
    }

    public String getPhotoString(){
        return photo.toString();
    }

    //获取图片
    public Bitmap getPhoto(){

        if(photo!=null)
            return BitmapFactory.decodeByteArray(photo,0,photo.length);
        else
            return null;
    }

}
