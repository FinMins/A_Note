package com.example.finmins.materialtest;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.finmins.materialtest.Model.FriendViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class friendHostFragment extends Fragment {
     private RecyclerView recyclerView ;   //朋友recyclerview;
     private View view;    //当前界面
    private List<Friend> friendList = new ArrayList<>();   //朋友容器
    private  FriendsAdapter adapter;    //朋友主界面适配器
    private Button addFriend;    //添加朋友按钮
    private FriendViewModel friendViewModel ; //  好友viewModel

    private LinearLayoutManager linearLayoutManager;   //item线性布局
    public friendHostFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
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

        addFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController Controller = Navigation.findNavController(v);
                Controller.navigate(R.id.action_friendnav_to_friendAddFragment2);
            }
        });

        //观察friendView中的好友列表
       friendViewModel.getFriendList().observe(this, new Observer<List<Friend>>() {
           @Override
           public void onChanged(List<Friend> friends) {

           }
       });


    }




        //初始化朋友容器，实际从数据库中读取。
        private void init(){
            for(int i = 0 ;i<10;i++){
                Friend xiaofei = new Friend(R.drawable.addfriendtouxiang,"富二代","123",R.drawable.chat);
                friendList.add(xiaofei);
                Friend erha = new Friend(R.drawable.addfriendtouxiang,"穷三代","123",R.drawable.chat);
                friendList.add(erha);
                Friend guanyu = new Friend(R.drawable.addfriendtouxiang,"拆一代","123",R.drawable.chat);
                friendList.add(guanyu);
            }
        }


  //初始化元素
        private void initControl(){
            adapter = new FriendsAdapter(getActivity(),friendList,friendViewModel);
            recyclerView = view.findViewById(R.id.friendhostrecyclerview);
            addFriend = view.findViewById(R.id.addfriend);
            linearLayoutManager = new LinearLayoutManager(getActivity());
            friendViewModel = new ViewModelProvider(this).get(FriendViewModel.class);
        }
    }

