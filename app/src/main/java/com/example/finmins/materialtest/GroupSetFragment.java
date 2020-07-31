package com.example.finmins.materialtest;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.finmins.materialtest.Model.GroupViewModel;
import com.example.finmins.materialtest.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class GroupSetFragment extends Fragment {
    private Button searchButton ; //搜索群按钮
    private Button creatGroupButton ;//创建群按钮
    private ImageView searchGroupImg;//搜索群图标
    private TextView searchGroupName;//搜索群的名
    private Button addGroupButton;//添加群按钮
    private EditText searchGroupEdit;//搜索群号文本框
    private EditText creatGroupNumEidt;//创建群号文本框
    private EditText creatGroupNameEdit;//创建群名文本框
    private GroupViewModel groupViewModel ; //群viewmodel


    public GroupSetFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_group_set, container, false);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        init();


        //点击搜索群的功能
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });


        //点击创建群的功能
        creatGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



            }
        });

        //监测搜索群的图标改变
        groupViewModel.getSearchImg().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                searchGroupImg.setImageResource(integer);
            }
        });

        //监测搜索群的群名改变
        groupViewModel.getSearchName().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
               searchGroupName.setText(s);
            }
        });


    }


    //初始化元素
    private void init(){
     searchButton  = getView().findViewById(R.id.searchGroupButton);
         creatGroupButton = getView().findViewById(R.id.creatGroupButton);
         searchGroupImg=getView().findViewById(R.id.searchGroupImg);
         searchGroupName = getView().findViewById(R.id.searchGroupName);
         addGroupButton = getView().findViewById(R.id.addGroupButton);
         searchGroupEdit = getView().findViewById(R.id.searchGroupEdit);
        creatGroupNumEidt = getView().findViewById(R.id.creatGroupNumEdit);
       creatGroupNameEdit = getView().findViewById(R.id.creatGroupNameEdit);
        groupViewModel = new ViewModelProvider(this).get(GroupViewModel.class);
    }

}
