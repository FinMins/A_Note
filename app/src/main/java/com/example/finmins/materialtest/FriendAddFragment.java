package com.example.finmins.materialtest;


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

import com.example.finmins.materialtest.Model.FriendViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class FriendAddFragment extends Fragment {

    private View view ;//当前界面
    private Button   searchButton ;//搜索按钮
    private EditText searchText ;//搜索内容
    private Button addButton ; //添加按钮
    private TextView searchName  ;//搜索用户的名字
    private ImageView searchFriendImg ; //搜索好友头像
    private FriendViewModel friendViewModel  ;//好友数据
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
           init();



        //观察搜索好友时的好友头像
        friendViewModel.getSearchImg().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
              searchFriendImg.setImageResource(integer);
            }
        });

        //观察搜索好友时的好友名字
        friendViewModel.getSearchName().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
            searchName.setText(s);
            }
        });

        //按下搜索按钮的功能
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
         //判断输入框是否为空
                checkInput();
            }
        });
        //按下添加按钮的功能
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    //初始化控件
    private void init(){
        searchButton = view.findViewById(R.id.searchbutton);
        searchText=view.findViewById(R.id.searchGroupEdit);
        searchName=view.findViewById(R.id.searchName);
        addButton =view.findViewById(R.id.addInSearch);
        friendViewModel = new ViewModelProvider(this).get(FriendViewModel.class);
        searchFriendImg = view.findViewById(R.id.searchFriendImg);
    }

    //判断输入框是否为空
    private boolean checkInput(){
        if(searchText.getText().toString()!= null)
            return true;
        else return false;
    }
}
