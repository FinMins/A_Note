package com.example.finmins.materialtest;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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

import com.example.finmins.materialtest.Model.GroupViewModel;
import com.example.finmins.materialtest.R;


import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class GroupHostFragment extends Fragment {
    private RecyclerView recyclerView ;   //群recyclerview;
    private View view;    //当前界面
    private List<Group> groupList = new ArrayList<>();   //群容器
    private  GroupAdapter adapter;    //朋友主界面适配器
    private GroupViewModel groupViewModel;    //群数据
    private LinearLayoutManager linearLayoutManager;   //item线性布局

    public GroupHostFragment() {
        // Required empty public constructor
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
        init();


        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

    }


    //初始化朋友容器，实际从数据库中读取。
    private void init(){
        for(int i = 0 ;i<10;i++){

            Group group = new Group(R.drawable.header,"11111");
            groupList.add(group);
           // Toast.makeText(getActivity(),group.getGroupName(),Toast.LENGTH_SHORT).show();

        }
    }





    private void initControl(){
        adapter = new GroupAdapter(getActivity(),groupList);
        recyclerView = view.findViewById(R.id.groupRecycler);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        groupViewModel  = new ViewModelProvider(this).get(GroupViewModel.class);
    }
}
