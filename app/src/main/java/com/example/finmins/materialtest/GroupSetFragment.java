package com.example.finmins.materialtest;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 */
public class GroupSetFragment extends Fragment {
    private Button searchButton ; //搜索群按钮
    private Button creatGroupButton ;//创建群按钮
    private ImageView searchGroupImg;//搜索群图标
    private TextView searchGroupName;//搜索群的名
    private ImageButton addGroupButton;//添加群按钮
    private EditText searchGroupEdit;//搜索群号文本框
    private EditText creatGroupNameEdit;//创建群名文本框
//    private GroupViewModel groupViewModel ; //群viewmodel
    private String isTrueName  ;//这是能用的群名
   private  String useremail ;    //用户邮箱
    private String groupImg  ;    //群头像
    private  String  groupName ; //群名字
    private  Integer grouptouxiang ;  //群真正的头像
    private Integer grouprandomtouxiang ;  //群随机的头像
    private  String  groupVIPEmail ;  //群管理员邮箱。
    private HttpClientUtils httpClientUtils = new HttpClientUtils() ;
    private final  String URL= "http://192.168.43.61:9999";
    private Random randomImgInt ;    //随机生成数
    Random r = new Random(1);



    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        useremail = ((Groupctivity)context).toValue();
        Log.d("这是加群界面获取到的useremail:", useremail);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_group_set, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("setgroup", "进入了setgroup了啊 ");
        init();


        //点击搜索群的功能
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("点击了搜索按钮", "onClick: ");
                //传入输入的群名，判断是否有这个群
                if(   requestGroup(searchGroupEdit.getText().toString())==1 ){
                    isTrueName= searchGroupEdit.getText().toString();
                };
            }
        });



//        //观测搜索群头像
//        groupViewModel.getSearchImg().observe(this, new Observer<Integer>() {
//            @Override
//            public void onChanged(Integer integer) {
//                searchGroupImg.setImageResource(integer);
//            }
//        });
//        //观测搜索群的名字
//        groupViewModel.getSearchName().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(String s) {
//                searchGroupName.setText(s);
//            }
//        });



//        //监测搜索群的图标改变
//        groupViewModel.getSearchImg().observe(this, new Observer<Integer>() {
//            @Override
//            public void onChanged(Integer integer) {
//                searchGroupImg.setImageResource(integer);
//            }
//        });
//
//        //监测搜索群的群名改变
//        groupViewModel.getSearchName().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(String s) {
//               searchGroupName.setText(s);
//            }
//        });

        //添加群的按钮
        addGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击添加按钮加群。
                Log.d("点击了添加按钮", "onClick: ");
                if(isTrueName!=null){
                    if(  requestAddGroup(useremail,isTrueName)==1);
                    Toast.makeText(getContext(), "加群成功", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getContext(), "失败，返回值错误", Toast.LENGTH_SHORT).show();
                }

            }
        });

        //创建群的按钮
        creatGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("点击了创建按钮", "onClick: ");
                if(creatGroupNameEdit.getText().toString()!=null){
                    if( requestCreateGroup(useremail,creatGroupNameEdit.getText().toString())==1){
                        Toast.makeText(getContext(), "创建成功", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getContext(), "创建失败", Toast.LENGTH_SHORT).show();
                    }

                }
                else    Toast.makeText(getContext(), "输入为空", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    //创建群
    public int requestCreateGroup(String selfEmail,String groupName){
            String request = "{\n" +
                    "\"groupname\":\""+groupName+"\",\n" +
                    "\"groupimg\":\""+grouptouxiang+"\",\n" +
                    "\"youxiang\":\""+selfEmail+"\"\n" +
                    "}";
                 String response = httpClientUtils.sendPostByOkHttp(URL+"/group1/insert",request);
                 if(response==null){
                     return 0;
                 }
                 requestAddGroup(selfEmail,groupName);
        return 1;
    }


    //添加群
    public int requestAddGroup(String selfEmail,String groupNmae){
//        SimpleDateFormat dff = new SimpleDateFormat("yyyy-MM-dd ");
//        dff.setTimeZone(TimeZone.getTimeZone("GMT+08"));
//        String temDateString  = dff.format(new Date());
        //先获取自己的信息
        String request1 = "{\n" +
                "            \"youxiang\":\""+selfEmail+"\"\n" +
                "        }"   ;
        String response1 = httpClientUtils.sendPostByOkHttp(URL+"/yonghu/select",request1);
        if( response1==null){
            return  0 ;
        }
//
//        String request2  =  " {\n" +
//                "\"youxiang\":\""+groupVIPEmail +"\",\n" +
//                "\"groupimg\":\""+groupImg+"\",\n" +
//                "\"groupname\":\""+groupNmae+"\",\n" +
//                "\"memberemail\":\""+selfEmail+"\"\n" +
//                "}  ";
//
        JSONObject jb = JSON.parseObject(response1);
       String selfimgid = jb.getString("touxiang");
//        String selfEmail = jb.getString("youxiang");
        String  selfmingzi = jb.getString("mingzi");
//再添加
        String request=" {\n" +
                "\"groupname\":\""+groupNmae+"\",\n" +
                "\"memberimg\":\""+selfimgid+"\",\n" +
                "\"membername\":\""+selfmingzi+"\",\n" +
                "\"memberemail\":\""+selfEmail+"\",\n" +
                "\"finishedtime\":\""+"刚进群"+"\"\n" +
                "}\n" ;
       String  response = httpClientUtils.sendPostByOkHttp(URL+"/member/insert",request);
        requestAddGmk(selfEmail,groupNmae);
         if(response!=null) return 0;
        else {
            return 1;
         }
    }

  public int requestAddGmk(String selfemail,String groupname){
        String request = "{\n" +
                "\"groupname\":\""+groupname+"\",\n" +
                "\"memberemail\":\""+selfemail+"\"\n" +
                "}";
      Log.d("这是添加键值对", request);
         String response =  httpClientUtils.sendPostByOkHttp(URL+"/gmk/insert",request);
         if (response!=null) return 1 ;
         return 0;
  }





    //从数据库获取--搜索群的信息
    public  int requestGroup(String grouName ){
        String request = "{\n" +
                "\"groupname\":\""+grouName+"\"\n" +
                "}\n" ;
        Log.d("setgroup里的搜索请求", request);
        String response = httpClientUtils.sendPostByOkHttp(URL+"/group1/selectG",request);
        if( response==null){
            Toast.makeText(getContext(), "请求为空", Toast.LENGTH_SHORT).show();
            return  0 ;
        }else{
            JSONObject jb = JSON.parseObject(response);
            groupImg = jb.getString("groupimg");
            groupVIPEmail = jb.getString("youxiang");
            groupName = jb.getString("groupname");
            searchGroupImg.setImageResource(Integer.parseInt(groupImg));
            searchGroupName.setText(groupName);
            return 1;
        }

    }


    //初始化元素
    private void init(){
        searchButton  = getView().findViewById(R.id.searchGroupButton);
         creatGroupButton = getView().findViewById(R.id.createGroupButton);
         searchGroupImg=getView().findViewById(R.id.searchGroupImg);
         searchGroupName = getView().findViewById(R.id.searchGroupName);
         addGroupButton = getView().findViewById(R.id.addGroupButton);
         searchGroupEdit = getView().findViewById(R.id.searchgroupEdit);
       creatGroupNameEdit = getView().findViewById(R.id.creatGroupNameEdit);
//        groupViewModel = new ViewModelProvider(this).get(GroupViewModel.class);
        randomImgInt = new Random();
        grouprandomtouxiang= randomImgInt.nextInt(4);
        setImgId(grouprandomtouxiang);     //随机好用户的头像
    }


    //根据随机的头像数字来设置群头像的resourceId
    private void setImgId(int radomImgId){
        switch (radomImgId){
            case 1:
                grouptouxiang = R.drawable.quntou1;
                break;
            case 2:
                grouptouxiang = R.drawable.quntou2;
                break;
            case 3:
                grouptouxiang = R.drawable.quntou3;
                break;
            case 4:
                grouptouxiang = R.drawable.quntou4;
                break;
            default:
                grouptouxiang = R.drawable.quntou1;

        }

    }


}
