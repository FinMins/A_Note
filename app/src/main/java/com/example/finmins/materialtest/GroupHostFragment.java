package com.example.finmins.materialtest;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class GroupHostFragment extends Fragment {
    List<Gmk> gmkList = new ArrayList<Gmk>();
    private RecyclerView recyclerView ;   //群recyclerview;
    private View view;    //当前界面
    private List<Group> groupList = new ArrayList<>();   //群容器
    private  GroupAdapter adapter;    //朋友主界面适配器
//    private GroupViewModel groupViewModel;    //群数据
    private LinearLayoutManager linearLayoutManager;   //item线性布局
    private String useremail  ;//用户邮箱
    private final  String URL= "http://192.168.43.61:9999";
    private HttpClientUtils httpClientUtils =new HttpClientUtils();
    public GroupHostFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        useremail = ((Groupctivity)context).toValue();
        Log.d("这是群主界面获取到的useremail:", useremail);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_group_host, container, false);

        return view;

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initControl();


        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

    }


    //初始化群组容器，实际从数据库中读取。
    private void init(){
        watchGrouplist();
//        for(int i = 0 ;i<10;i++){
//
//            Group group = new Group(R.drawable.header,"11111");
//            groupList.add(group);
//           // Toast.makeText(getActivity(),group.getGroupName(),Toast.LENGTH_SHORT).show();
//
//        }
    }

    public  void refresh(){
        groupList.clear();
        recyclerView=view.findViewById(R.id.groupRecycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager );
        adapter= new GroupAdapter(getContext(),groupList,useremail);
        recyclerView.setAdapter(adapter);
        recyclerView.setAdapter(adapter);
        recyclerView.setSelected(true);
        adapter.notifyDataSetChanged();
        init();
    }


    public int watchGrouplist(){
        //加载用户的群组到listgroup里
        requestselectGmk(useremail);
  for (int  a = 0 ;a<gmkList.size();a++){
//      String  request= "  {\n" +
//              "\"memberemail\":\""+useremail+"\"\n" +
//              "}\n ";
//      String response  =    httpClientUtils.sendPostByOkHttp(URL+"/member/selectMG",request);
////        Log.d("这是好友请求列表内容", response);
//      if(response==null){
////            获取失败
//          Toast.makeText(getContext(), "请求失败", Toast.LENGTH_SHORT).show();
//      }
////        else  if(response.equals("[]"))
//
//      else {
//          JSONArray jsonArray = JSON.parseArray(response);
//          for (int i = 0; i < jsonArray.size(); i++) {
//              JSONObject obj = jsonArray.getJSONObject(i);
//              String groupname = (String) obj.get("groupname");
//              String groupVipyouxiang = (String) obj.get("youxiang");
//              String groupimg = (String) obj.get("groupimg");
//              Group group = new Group(Integer.parseInt(groupimg),groupname,groupVipyouxiang);
//              groupList.add(group);
//          }
//          Toast.makeText(getContext(), "请求成功", Toast.LENGTH_SHORT).show();
//      }
      String request = "{\n" +
              "\"groupname\":\""+gmkList.get(a).getGroupname()+"\"\n" +
              "}\n" ;
      Log.d("setgroup里的搜索请求", request);
      String response = httpClientUtils.sendPostByOkHttp(URL+"/group1/selectG",request);
      if( response==null){
          Toast.makeText(getContext(), "请求为空", Toast.LENGTH_SHORT).show();
         return  0 ;
      }else{
          JSONObject jb = JSON.parseObject(response);
           String   groupImg = jb.getString("groupimg");
           String   groupVIPEmail = jb.getString("youxiang");
          String   groupName = jb.getString("groupname");
          Group group = new Group(Integer.parseInt(groupImg),groupName,groupVIPEmail);
          groupList.add(group);
//          return 1;
      }
//return 1 ;

  }

return 1;
    }

    public int requestselectGmk(String selfemail){
          gmkList .clear();
        String request = " {\n" +
                "\"memberemail\":\""+selfemail+"\"\n" +
                "}";
        String response =  httpClientUtils.sendPostByOkHttp(URL+"/gmk/select",request);

        if (response==null) return 0 ;
        else {
            JSONArray jsonArray = JSON.parseArray(response);
            for (int i = 0; i < jsonArray.size(); i++) {       //这个是Json数组
                Log.d("这是jsonarray的大小", String.valueOf(jsonArray.size()));
                JSONObject obj = jsonArray.getJSONObject(i);
                String memberemail = (String) obj.get("memberemail");                //这个是Json数组的每一个json对象里的键值对。
                String groupname= (String) obj.get("groupname");
                Gmk gmk = new Gmk(memberemail,groupname);
                gmkList.add(gmk);
                Log.d("这是host内容", String.valueOf(gmkList.size()));
            }
            Toast.makeText(getContext(), "请求成功", Toast.LENGTH_SHORT).show();
        }
        return 1;
    }

    private void initControl(){
        adapter = new GroupAdapter(getActivity(),groupList,useremail);
        recyclerView = view.findViewById(R.id.groupRecycler);
        linearLayoutManager = new LinearLayoutManager(getActivity());
//        groupViewModel  = new ViewModelProvider(this).get(GroupViewModel.class);
        refresh();
    }
}
