package com.example.finmins.materialtest;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
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
public class FriendHostFragment extends Fragment {
     private RecyclerView recyclerView ;   //朋友recyclerview;
     private View view;    //当前界面
    private List<Friend> friendList = new ArrayList<>();   //朋友容器
    private  FriendsAdapter adapter;    //朋友主界面适配器
    private Button addFriend;    //添v加朋友按钮
//    private Button recMessage;   //接受消息按钮；
    private String useremail ;
    private String userimg ;
    private String usermingzi ;
//    private FriendViewModel friendViewModel ; //  好友viewModel
    private LinearLayoutManager linearLayoutManager;   //item线性布局
    private HttpClientUtils httpClientUtils = new HttpClientUtils() ;
    private final  String URL= "http://192.168.43.61:9999";
    public FriendHostFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
         useremail = ((FriendsActivity)context).toValue();
         usermingzi = ((FriendsActivity)context).toImgValue();
        Log.d("hostfriend ;email", useremail);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
            view = inflater.inflate(R.layout.fragment_friend_host, container, false);

                return view;
    }


    @Override//这个应该是从其他界面传数据时调用的东西
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        initControl();
        init();
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);


        //点击添加朋友响应
        addFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("userEmail",useremail);
                Log.d("这是发送给add的usermail", useremail);
                NavController Controller = Navigation.findNavController(v);
                Controller.navigate(R.id.friendAddFragment2,bundle);
            }
        });



//        //点击接受消息响应
//        recMessage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });



//        //观察friendView中的好友列表
//       friendViewModel.getFriendList().observe(this, new Observer<List<Friend>>() {
//           @Override
//           public void onChanged(List<Friend> friends) {
////                Refresh();
//           }
//       });





    }


//       刷新

    public void Refresh(){
       friendList.clear();
        recyclerView=  view.findViewById(R.id.friendhostrecyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setSelected(true);
        initFriend();
        adapter = new FriendsAdapter(getContext(), friendList);
        adapter.notifyDataSetChanged();
    }

//从viewmodel里获取数据
    private void initFriend(){
        requestFriendList(useremail);
    }




        //初始化朋友容器，实际从数据库中读取。
        private void init(){
//          initFriend();
            adapter.setOnItemClickLitener(new FriendsAdapter.OnItemClickLitener(){

                @Override
                public void onItemClick(int position, String youxiang,View view) {
                    Bundle bundle = new Bundle();
                    bundle.putString("userEmail",useremail);
                    bundle.putString("friendEmail",youxiang);

                    NavController Controller = Navigation.findNavController(view);
                    Controller.navigate(R.id.friendShareFragment,bundle);
                }

                //根据ID删除好友
                @Override
                public void onDeleteClick(int position, String youxiang) {
                    final int position_item=position;   //获取listview中的位置
                    final String  email=youxiang;   //事件的id

                    //点击删除时的警告窗口
                    AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                    dialog.setTitle("警告");
                    dialog.setMessage("确认删除？");
                    dialog.setCancelable(false);

                    //点击确定执行的代码
                    dialog.setPositiveButton("确认",new DialogInterface.OnClickListener(){

                        public void onClick(DialogInterface dialog, int which) {
                            //根据ID删除事件
                         delete(useremail,email);
                         delete(email,useremail);
                            friendList.remove(position_item);
                            adapter.notifyItemRemoved(position_item);

                            Refresh();
                            Toast.makeText(getContext(),"好友删除成功",Toast.LENGTH_SHORT).show();
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


            });

        }

        private void delete(String useremail,String friendemail){
        String request = "{\n" +
                "\"friendyouxiang\":\""+friendemail+"\",\n" +
                "\"youxiang\":\""+useremail+"\"\n" +
                "}";
        String response = httpClientUtils.sendPostByOkHttp(URL+"/friend/delete",request);
        }


  //初始化元素
        private void initControl(){
//            recMessage = view.findViewById(R.id.alertMessage);
            adapter = new FriendsAdapter(getActivity(),friendList);
            recyclerView = view.findViewById(R.id.friendhostrecyclerview);
            addFriend = view.findViewById(R.id.addfriend);
            linearLayoutManager = new LinearLayoutManager(getActivity());
//            friendViewModel =new  ViewModelProvider(this).get(FriendViewModel.class);
//            friendViewModel.setUserEmail(useremail);//
//            Log.d("这是fragment里的email", friendViewModel.getUserEmail().getValue());
            //初始化时检测有没有朋友发过来的消息

            //有接受的话就点击按钮，按钮回复原来色，把事件集读取过来并保存在本地事件中，同时后台删除好友事件集。


            Refresh();
        }





    //从数据库里得到好友列表
    public void requestFriendList(String selfEmail){
//        Log.d("这是好友列表请求", selfEmail);
        String request = "{\n" +
                "\"youxiang\":\""+selfEmail+"\"\n" +
                "}";
        Log.d("好友列表请求邮箱", selfEmail);
        String response  =    httpClientUtils.sendPostByOkHttp(URL+"/friend/selectlist",request);
//        Log.d("这是好友请求列表内容", response);
        if(response==null){
//            获取失败
            Toast.makeText(getContext(), "请求失败", Toast.LENGTH_SHORT).show();
            //
        }else {
            JSONArray jsonArray = JSON.parseArray(response);
            for (int i = 0; i <jsonArray.size(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                String friendmingzi = (String) obj.get("mingzi");
                String friendyouxiang = (String) obj.get("friendyouxiang");
                String friendtouxiang = (String) obj.get("touxiang");

                Friend friend = new Friend(Integer.parseInt(friendtouxiang), friendmingzi, friendyouxiang);
                Log.d("这是friend的mingzi,youxiang,touxiang", friendmingzi+friendyouxiang+friendtouxiang );
                friendList.add(friend);
                Log.d("这是执行了添加", "尺寸为"+jsonArray.size());
            }
            Toast.makeText(getContext(), "请求成功", Toast.LENGTH_SHORT).show();
        }

    }




}

