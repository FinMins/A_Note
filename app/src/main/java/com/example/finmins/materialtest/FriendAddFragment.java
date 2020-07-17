package com.example.finmins.materialtest;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class FriendAddFragment extends Fragment {

    private View view ;//当前界面
    private Button   searchButton ;//搜索按钮
    private EditText searchText ;//搜索内容
    private Button addButton ; //添加按钮
    private TextView searchName  ;//搜索用户的名字

    public FriendAddFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_friend_add, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);




    }

    //初始化控件
    private void init(){
        searchButton = view.findViewById(R.id.searchbutton);
        searchText=view.findViewById(R.id.searchGroupText);
        searchName=view.findViewById(R.id.searchName);
        addButton =view.findViewById(R.id.addInSearch);
    }

}
