package com.example.finmins.materialtest;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.daimajia.swipe.util.Attributes;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * A simple {@link Fragment} subclass.
 */
public class GroupInnerFragment extends Fragment {
    private RecyclerView recyclerView ;   //朋友recyclerview;
    private View view;    //当前界面
    private List<MemberInGroup> memberList = new ArrayList<>();   //成员容器
    private  MemberAdapter adapter;    //成员适配器
    private LinearLayoutManager linearLayoutManager;   //item线性布局
//    private GroupViewModel   groupViewModel ; //群model
  private String groupName;    //这个群的名字
    private String vipEmail ;//这个群的vip邮箱
    private String userSelfEmail ; //用户自己的邮箱
    private HttpClientUtils httpClientUtils = new HttpClientUtils() ;
    private final  String URL= "http://192.168.43.61:9999";

    public GroupInnerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view=inflater.inflate(R.layout.fragment_group_inner, container, false);
        return view;
    }


    @Override//这个应该是从其他界面传数据时调用的东西
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initControl();
        init();
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);


    }


    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }

    //初始化容器，实际从数据库中读取。
    private void init(){
       requestMemberLsit(groupName);
    }

    public  void refresh(){
         memberList.clear();
        recyclerView=view.findViewById(R.id.memberRecyclerview);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager );
        adapter= new MemberAdapter(getContext(),memberList,vipEmail,this);
        recyclerView.setAdapter(adapter);
        adapter.setMode(Attributes.Mode.Single);
        recyclerView.setAdapter(adapter);
        recyclerView.setSelected(true);
//        memberList = groupViewModel.getMemberList().getValue();
        init();
        adapter.notifyDataSetChanged();
        initEvents();
    }



    //adapter点击事件
    public void initEvents(){
        adapter.setOnItemClickLitener(new MemberAdapter.OnItemClickLitener() {
            //根据ID进入事件具体内容
       @Override
       //点击完成
       public void onSetImgId(int position, String selfEmail, String groupName, String finishDate) {
//           SimpleDateFormat dff = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
           SimpleDateFormat dff = new SimpleDateFormat("yyyy-MM-dd ");
           dff.setTimeZone(TimeZone.getTimeZone("GMT+08"));
           String temDateString  = dff.format(new Date());
           Log.d("选中成员列表的邮箱", selfEmail);
           Log.d("选中成员列表的群名", groupName);
           Log.d("选中成员列表的完成时间", finishDate);

           //判断自己是不是vip：
           if(userSelfEmail.equals(vipEmail)){
               //是vip,可以操作任何人的完成
               //判断是不是已经打过卡
                  if( finishDate.equals(temDateString))
                          {  //日期相等，证明已经打过卡额
                      Toast.makeText(getContext(), "管理员：已经打过卡了", Toast.LENGTH_SHORT).show();
                      }else{
                      if(requestFinishAmember(groupName,selfEmail,temDateString)==1)
                      { //判断是不是给自己打卡
                          if(selfEmail.equals(userSelfEmail)){
                              //是给自己打卡
                              Toast.makeText(getContext(), "管理员：打卡成功", Toast.LENGTH_SHORT).show();
                          }else {
                              //给别人打
                              Toast.makeText(getContext(), "辛苦管理员大大了！", Toast.LENGTH_SHORT).show();
                          }
                      refresh();
                            }else
                                  {
                                      Toast.makeText(getContext(), "管理员：打卡失败", Toast.LENGTH_SHORT).show();
                                  }

                  }
           }else{
                   //不是vip
                   //判断是不是点的自己的
                   if(userSelfEmail.equals(selfEmail)){
                       //是点的自己的
                       if(  finishDate.equals(temDateString)
                       ){//已经打过卡了
                           Toast.makeText(getContext(), "已经打过卡了", Toast.LENGTH_SHORT).show();
                       }else {
                           //没打过卡
                            if( requestFinishAmember(groupName,selfEmail,temDateString)==1){
                                Toast.makeText(getContext(), "打卡成功", Toast.LENGTH_SHORT).show();
                                refresh();
                            }else {
                                Toast.makeText(getContext(), "打卡失败", Toast.LENGTH_SHORT).show();
                            }

                       }
                                }else {
                             Toast.makeText(getContext(), "调皮！完成自己的打卡！", Toast.LENGTH_SHORT).show();
                     }
           }
       }

       @Override
       //删除
       public void onDeleteClick(int position, String selfEmail, String groupName) {
           //判断自己是不是vip：
           if(userSelfEmail.equals(vipEmail)){
               //是
               if( !vipEmail.equals(selfEmail)){
                   //      删除别人的操作：删除成员
                   //删除gmk
                   deleteMemberAlter(groupName,selfEmail,position);
               }else {
                   //删除群
                   deleteGroupAlter(groupName);
               }
           }else {
               //不是vip,判断是不是操作自己
               if(!userSelfEmail.equals(selfEmail )){
                   //                            删除别人的操作：无权限
                   Toast.makeText(getContext(), "调皮！不能删除别人", Toast.LENGTH_SHORT).show();
               }else {
                   //删除自己的操作:退出群
                   deleteMemberAlter(groupName,selfEmail,position);
               }
           }
       }

       });
    }


//    //删除群的警告
//    public  void  deleteMemberAlter(final String a,final String b  ){
//
//        //点击删除时的警告窗口
//        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
//        dialog.setTitle("警告");
//        dialog.setMessage("确认删除？");
//        dialog.setCancelable(false);
//        //点击确定执行的代码
//        dialog.setPositiveButton("确认",new DialogInterface.OnClickListener(){
//
//            public void onClick(DialogInterface dialog, int which) {
//
//
//
//                Toast.makeText(getContext(),"群删除成功",Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        //点击取消执行的代码
//        dialog.setNegativeButton("取消",new DialogInterface.OnClickListener(){
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                //点击取消无任何反应
//            }
//        });
//        dialog.show();
//    }


    //删除群的警告
    public void deleteGroupAlter(final  String groupname ){
        //点击删除时的警告窗口
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setTitle("警告");
        dialog.setMessage("确认删除群组？");
        dialog.setCancelable(false);
        //点击确定执行的代码
        dialog.setPositiveButton("确认",new DialogInterface.OnClickListener(){

            public void onClick(DialogInterface dialog, int which) {
                        deleteGroup(groupname);
                      deleteGroupGmk(groupname);
                      getActivity().onBackPressed();//相当于按下了返回键
            }
        });

        //点击取消执行的代码
        dialog.setNegativeButton("取消",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //点击取消无任何反应
            }
        });
        dialog.show();
    }


    //删除成员的警告
    public void deleteMemberAlter(final  String groupname,final  String selfemail,final int position){
        //点击删除时的警告窗口
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setTitle("警告");
        dialog.setMessage("确认删除群组？");
        dialog.setCancelable(false);
        //点击确定执行的代码
        dialog.setPositiveButton("确认",new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which) {
               deleteMember(groupname,selfemail);
               deletegmk(groupname,selfemail);
                memberList.remove(position);
                adapter.notifyItemRemoved(position);
                getActivity().onBackPressed();//相当于按下了返回键

            }
        });

        //点击取消执行的代码
        dialog.setNegativeButton("取消",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //点击取消无任何反应
            }
        });
        dialog.show();
    }
//删除成员
private void deleteMember(String groupname,String memberemail){
    String request = " {\n" +
            "    \"groupname\":\""+groupname+"\",\n" +
            "    \"memberemail\":\""+memberemail+"\"\n" +
            "}";
    String response = httpClientUtils.sendPostByOkHttp(URL+"/member/delete",request);
    if (response==null) Toast.makeText(getContext(), "成员删除失败", Toast.LENGTH_SHORT).show();

//    Toast.makeText(getContext(), "删除成功", Toast.LENGTH_SHORT).show();
}
//删除单个gmk
 private void deletegmk(String groupname,String memberemail){
        String request = " {\n" +
                "    \"groupname\":\""+groupname+"\",\n" +
                "    \"memberemail\":\""+memberemail+"\"\n" +
                "}";
         String response = httpClientUtils.sendPostByOkHttp(URL+"/gmk/delete",request);
         if (response==null) Toast.makeText(getContext(), "gmk删除失败", Toast.LENGTH_SHORT).show();

//     Toast.makeText(getContext(), "删除成功", Toast.LENGTH_SHORT).show();
 }

    //删除一个群
    private void deleteGroup(String groupname){
        //根据ID删除事件
        String  request ="{\n" +
                "\"groupname\":\""+groupname+"\"\n" +
                "}\n";
        String response = httpClientUtils.sendPostByOkHttp(URL+"/group1/delete",request);
        if(response !=null)
            Toast.makeText(getContext(),"群组删除成功",Toast.LENGTH_SHORT).show();
//                Toast.makeText(getContext(),"群组删除失败",Toast.LENGTH_SHORT).show();
    }

    //删除一个群的gmk
    private void deleteGroupGmk(String groupname ){
        String request = "  {\n" +
                "    \"groupname\":\""+groupname+"\"\n" +
                "} ";
        Log.d("删除群的groupgmk：", request);
        String response = httpClientUtils.sendPostByOkHttp(URL+"/gmk/deleteG",request);
        if (response==null) Toast.makeText(getContext(), "gmk删除失败", Toast.LENGTH_SHORT).show();

//     Toast.makeText(getContext(), "删除成功", Toast.LENGTH_SHORT).show();
    }

    //从后台请求memberList数据
    private void initControl(){
        groupName= getArguments().getString("groupName");
        vipEmail = getArguments().getString("VIPemail");
        userSelfEmail =  getArguments().getString("selfEnami");
        Log.d("用户自己的邮箱", userSelfEmail);
        Log.d("群的管理员邮箱", vipEmail);
        Log.d("群的名字", groupName);

        adapter = new MemberAdapter(getContext(),memberList,vipEmail,this);
        recyclerView = view.findViewById(R.id.memberRecyclerview);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        //初始化List
         requestMemberLsit(groupName);
//        memberList = getMemberList().getValue();
    }
    //设置成员完成时间。

     public int   requestFinishAmember(String groupname,String selfemail,String temDateString){
                String request = "{\n" +
                        "\"finishedtime\":\""+temDateString+"\",\n" +
                        "\"groupname\":\""+groupname+"\",\n" +
                        "\"memberemail\":\""+selfemail+"\"\n" +
                        "}" ;
                String response = httpClientUtils.sendPostByOkHttp(URL+"/member/updatetime",request);
         Log.d("这是打卡reponse", response);
                if(response==null) return 0;

                  return 1 ;
     }

     public int isfinished( String time){
         SimpleDateFormat dff = new SimpleDateFormat("yyyy-MM-dd ");
         dff.setTimeZone(TimeZone.getTimeZone("GMT+08"));
         String temDateString  = dff.format(new Date());
         if(temDateString.equals(time))return R.drawable.zhengque;
         else  return R.drawable.yuanquan;

     }


     public void requestMemberLsit(String groupName){

         SimpleDateFormat dff = new SimpleDateFormat("yyyy-MM-dd");
         dff.setTimeZone(TimeZone.getTimeZone("GMT+08"));
         String temDateString  = dff.format(new Date());

        String  request= " {\n" +
                "\"groupname\":\""+groupName+"\"\n" +
                "}\n";
        String response = httpClientUtils.sendPostByOkHttp(URL+"/member/select",request);
         if(response==null){
//            获取失败
             Toast.makeText(getContext(), "请求失败", Toast.LENGTH_SHORT).show();
             //
         }else {
             JSONArray jsonArray = JSON.parseArray(response);
             for (int i = 0; i < jsonArray.size(); i++) {
                 JSONObject obj = jsonArray.getJSONObject(i);
                 String groupname = (String) obj.get("groupname");
                 String memberimg = (String) obj.get("memberimg");
                 String membername = (String) obj.get("membername");
                 String finishedtime = (String) obj.get("finishedtime");
                 String memberemail = (String) obj.get("memberemail");
                 int isFinished  = isfinished(finishedtime);
                 MemberInGroup memberInGroup = new MemberInGroup(groupname,memberemail,Integer.parseInt(memberimg),membername,finishedtime,isFinished);
                memberList.add(memberInGroup);
             }
             Toast.makeText(getContext(), "请求成功", Toast.LENGTH_SHORT).show();
         }

     }

}
