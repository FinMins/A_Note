package com.example.finmins.materialtest;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;


public class MyDatabaseHelper extends SQLiteOpenHelper {
    public static  final  String CREATE_SHIJIAN=" create table ShiJian ("
            + " id integer primary key autoincrement,"
            + " biaoti text, "
            + " neirong text, "
            + " year integer, "
            + " month integer, "
            + " day integer, "
            + " time text) ";
    private Context mContext;

    public  MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,int version){
        super(context,name,factory,version);
        mContext=context;
    }
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_SHIJIAN);
        Toast.makeText(mContext,"创建成功",Toast.LENGTH_SHORT).show();
    }

    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){
    }


}