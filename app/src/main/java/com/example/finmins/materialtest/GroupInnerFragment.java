package com.example.finmins.materialtest;


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

import com.example.finmins.materialtest.R;

import java.lang.reflect.Member;
import java.util.ArrayList;
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


        /*
        addFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController Controller = Navigation.findNavController(v);
                Controller.navigate(R.id.action_friendnav_to_friendAddFragment2);
            }
        });
*/




    }

    //初始化朋友容器，实际从数据库中读取。
    private void init(){
        for(int i = 0 ;i<10;i++){
           MemberInGroup member = new MemberInGroup(R.mipmap.app,"123",1,0,"2020","7","13");
          memberList.add(member);
            MemberInGroup member1 = new MemberInGroup(R.mipmap.app,"666",0,0,"2020","7","13");
            memberList.add(member1);
        }
    }



    private void initControl(){
        adapter = new MemberAdapter(getActivity(),memberList);
        recyclerView = view.findViewById(R.id.memberRecyclerview);
        linearLayoutManager = new LinearLayoutManager(getActivity());
    }
}
