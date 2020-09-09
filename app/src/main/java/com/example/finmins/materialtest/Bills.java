package com.example.finmins.materialtest;

import java.util.Date;

public class Bills {
    private String date;      //账单时间
    private String type;    //账单类型
    private double amount;  //账单金额
    private String localTime ; //时间
    public Bills(){

    }
    public Bills(String date,String type,double amount,String localTime){
        this.date = date;
        this.type = type;
        this.amount = amount;
        this.localTime = localTime;
    }

    public String getDate(){
        return date;
    }

    public String getType(){
        return type;
    }

    public double getAmount(){
        return amount;
    }
    public String getLocalTime()
    {
        return localTime;
    }
}
