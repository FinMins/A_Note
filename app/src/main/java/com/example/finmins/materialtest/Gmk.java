package com.example.finmins.materialtest;

public class Gmk {
    private String memberemail;
    private String groupname;

    public  Gmk(String memberemail,String groupname){
        this.memberemail=memberemail;
        this.groupname = groupname;
    }

    public  String getMemberemail(){
        return this .memberemail;
    }
    public String getGroupname(){
        return this.groupname;
    }
}
