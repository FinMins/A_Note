package com.example.finmins.materialtest;


import android.app.VoiceInteractor;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.renderscript.ScriptGroup;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.finmins.materialtest.Model.FriendViewModel;

import net.sf.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class FriendAddFragment extends Fragment {

    private View view ;//当前界面
    private Button   searchButton ;//搜索按钮
    private EditText searchText ;//搜索内容
    private ImageButton addButton ; //添加按钮
    private TextView searchName  ;//搜索用户的名字
    private ImageView searchFriendImg ; //搜索好友头像
    private FriendViewModel friendViewModel  ;//好友数据
    private String    isTrueEmail =null;    //是否搜索到了。
    private HttpClientUtils httpClientUtils = new HttpClientUtils();
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

        if( friendViewModel.requestSearchImg()&&friendViewModel.requestSearchName() ) {
            isTrueEmail = searchText.getText().toString();
        }
            }
        });
        //按下添加按钮的功能
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isTrueEmail!=null){
                    friendViewModel.addFriend(friendViewModel.getUserEmail().getValue(),isTrueEmail);
                    //添加成功
                    Toast.makeText(getContext(), "添加成功", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getContext(), "添加失败", Toast.LENGTH_SHORT).show();
                }




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

    private void sendRequestWithHttpURLConnection(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                BufferedReader reader =null ;

                try{
                  URL url = new URL("https://api.apiopen.top/getJoke?page=1&count=2&type=video");
                  connection =(HttpURLConnection)url.openConnection();
                  connection.setRequestMethod("GET");
                  connection.setConnectTimeout(5000);
                  connection.setReadTimeout(5000);
                  InputStream in = connection.getInputStream();
                    //下面对获取到的输入流进行读取
                    reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while((line = reader.readLine())!=null){
                        response.append(line);
                    }
                    showResponse(response.toString());
                } catch (Exception e){
                    e.printStackTrace();
                }finally {
                    if(reader!=null){
                        try {
                            reader.close();
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                    if (connection != null){
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

    private void showResponse(final  String response){
        Log.d(getContext().toString(), "这是回应内容"+response);
    }




    //判断输入框是否为空
    private boolean checkInput(){
        if(searchText.getText().toString()!= null)
            return true;
        else return false;
    }
}
