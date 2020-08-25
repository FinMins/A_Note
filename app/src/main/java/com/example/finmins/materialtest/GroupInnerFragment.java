package com.example.finmins.materialtest;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.util.Attributes;
import com.example.finmins.materialtest.Model.GroupViewModel;
import com.example.finmins.materialtest.R;

import org.litepal.crud.DataSupport;

import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class GroupInnerFragment extends Fragment {
    private RecyclerView recyclerView ;   //朋友recyclerview;
    private View view;    //当前界面
    private List<MemberInGroup> memberList = new ArrayList<>();   //成员容器
    private  MemberAdapter adapter;    //成员适配器
    private LinearLayoutManager linearLayoutManager;   //item线性布局
    private GroupViewModel   groupViewModel ; //群model
    private String grouopName;    //这个群的名字
    private String vipEmail ;//这个群的vip邮箱
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
        for(int i = 0 ;i<10;i++){
           MemberInGroup member = new MemberInGroup("asd","asd",R.mipmap.app,"asd","asd");
          memberList.add(member);
            MemberInGroup member1 = new MemberInGroup("asd","asd",R.mipmap.app,"asd","123");
            memberList.add(member1);
        }
    }

    public  void refresh(){
         memberList.clear();
        recyclerView=view.findViewById(R.id.memberRecyclerview);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager );
        adapter= new MemberAdapter(getContext(),memberList,groupViewModel,vipEmail,this);
        recyclerView.setAdapter(adapter);

        adapter.setMode(Attributes.Mode.Single);
        recyclerView.setAdapter(adapter);
        recyclerView.setSelected(true);
        memberList = groupViewModel.getMemberList().getValue();
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
                                              String  temMonth =  String.valueOf(   Calendar.getInstance().get(Calendar.MONTH)+1);
                                              String  temDay = String.valueOf(   Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                                              String temYear = String.valueOf(   Calendar.getInstance().get(Calendar.YEAR));
                                              String temDateString = temYear+"/"+temMonth+"/"+temDay;
                                              //判断自己是不是vip：
                                              if(groupViewModel.getUserEmail()==vipEmail){
                                                  //是vip,可以操作任何人的完成
                                                  //判断是不是已经打过卡
                                                     if( finishDate.equals(temDateString))
                                                             {  //日期相等，证明已经打过卡额
                                                         Toast.makeText(getContext(), "已经打过卡了", Toast.LENGTH_SHORT).show();
                                                         }else{
                                                         if(    groupViewModel.requestFinishAmember(groupName,selfEmail,temDateString)==1)
                                                         { //判断是不是给自己打卡
                                                             if(selfEmail==groupViewModel.getUserEmail()){
                                                                 //是给自己打卡


                                                                 Toast.makeText(getContext(), "打卡成功", Toast.LENGTH_SHORT).show();
                                                             }else {
                                                                 //给别人打
                                                                 Toast.makeText(getContext(), "辛苦管理员大大了！", Toast.LENGTH_SHORT).show();
                                                             }
                                                         refresh();
                                                               }else
                                                                     {
                                                                         Toast.makeText(getContext(), "打卡失败", Toast.LENGTH_SHORT).show();
                                                                     }

                                                     }


                                              }else{
                                                      //不是vip
                                                      //判断是不是点的自己的
                                                      if(groupViewModel.getUserEmail()==selfEmail){
                                                          //是点的自己的
                                                          if(  finishDate.equals(temDateString)
                                                          ){//已经打过卡了
                                                              Toast.makeText(getContext(), "已经打过卡了", Toast.LENGTH_SHORT).show();
                                                          }else {
                                                              //没打过卡
                                                               if(  groupViewModel.requestFinishAmember(groupName,selfEmail,temDateString)==1){

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
                                              if(groupViewModel.getUserEmail()==vipEmail){
                                                  //是
                                                  if( vipEmail!=selfEmail){
                                                      //      删除别人的操作：删除成员
                                                      adapter.deleteMemberAlter(groupName,selfEmail);
                                                      memberList.remove(position);
                                                      adapter.notifyItemRemoved(position);

                                                  }else {
                                                      //删除群
                                                      adapter.deleteGroupAlter(groupName);
                                                      getActivity().onBackPressed();//相当于按下了返回键
                                                  }
                                              }else {
                                                  //不是vip,判断是不是操作自己
                                                  if(groupViewModel.getUserEmail()!=selfEmail ){
                                                      //                            删除别人的操作：无权限
                                                      Toast.makeText(getContext(), "调皮！不能删除别人", Toast.LENGTH_SHORT).show();
                                                  }else {
                                                      //删除自己的操作:退出群
                                                      adapter.deleteGroupAlter(selfEmail);
                                                      getActivity().onBackPressed();//相当于按下了返回键

                                                  }
                                              }
                                          }

                                      });
    }










    //从后台请求memberList数据
    private void initControl(){
        grouopName= getArguments().getString("groupName");
        vipEmail = getArguments().getString("VIPemail");
        adapter = new MemberAdapter(getContext(),memberList,groupViewModel,vipEmail,this);
        recyclerView = view.findViewById(R.id.memberRecyclerview);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        //初始化List
         groupViewModel.requestMemberLsit(grouopName);
        memberList = groupViewModel.getMemberList().getValue();
    }

}
