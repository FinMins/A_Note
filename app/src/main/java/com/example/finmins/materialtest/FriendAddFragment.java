package com.example.finmins.materialtest;


import android.content.Context;
import android.os.Bundle;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

//import net.sf.json.JSONObject;

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
    private String imgid;
    private String mingzi;
//    private FriendViewModel friendViewModel  ;//好友数据
    private String    isTrueEmail =null;    //是否搜索到了。
    private String userEmail ;//用户邮箱
    private String friendEmail ;
    private String userimg ;
    private String usermingzi;
    private String useremail;
    private final  String URL= "http://192.168.43.61:9999";
    private HttpClientUtils httpClientUtils = new HttpClientUtils();
    public FriendAddFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        useremail = ((FriendsActivity)context).toValue();
        usermingzi = ((FriendsActivity)context).tomingziValue();
        userimg =  ((FriendsActivity)context).toImgValue();
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
        userEmail =  getArguments().getString("userEmail");
        Log.d("这是friendadd里的邮箱", userEmail);
           init();





//        //观察搜索好友时的好友头像
//        friendViewModel.getSearchImg().observe(this, new Observer<Integer>() {
//            @Override
//            public void onChanged(Integer integer) {
//              searchFriendImg.setImageResource(integer);
//            }
//        });
//
//        //观察搜索好友时的好友名字
//        friendViewModel.getSearchName().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(String s) {
//            searchName.setText(s);
//            }
//        });

        //按下搜索按钮的功能
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
         //判断输入框是否为空
               if( checkInput()){

                    if( requestSearch( searchText.getText().toString())==1 ) {

                        isTrueEmail = searchText.getText().toString();
                    }
                }else
                   Toast.makeText(getContext(), "输入为空", Toast.LENGTH_SHORT).show();

            }
        });
        //按下添加按钮的功能
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isTrueEmail!=null&&addFriend(userEmail,imgid,mingzi,friendEmail)==1&&addFriend(friendEmail,userimg,usermingzi,userEmail)==1){
                    //添加成功
//                    Log.d("添加时的邮箱为：", friendViewModel.getUserEmail().getValue());
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
        searchButton = view.findViewById(R.id.searchfriendbutton);
        searchText=view.findViewById(R.id.searchfriendEdit);
        searchName=view.findViewById(R.id.searchName);
        addButton =view.findViewById(R.id.addInSearch);
//        friendViewModel = new ViewModelProvider(this).get(FriendViewModel.class);
        searchFriendImg = view.findViewById(R.id.searchFriendImg);
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





    //添加朋友
    public int addFriend(String useremail,String img,String mingzi,String friendemail){
        String request = " {\n" +
                "\"touxiang\":\""+img+"\",\n" +
                "\"mingzi\":\""+mingzi+"\",\n" +
                "\"friendyouxiang\":\""+friendemail+"\",\n" +
                "\"youxiang\":\""+useremail+"\"\n" +
                "} ";
        Log.d("添加朋友的touxiang", img);
        Log.d("添加朋友的mingzi", mingzi);
        Log.d("添加朋友的friendemail", friendemail);
        Log.d("这是add里面的邮箱", userEmail);
        Log.d("这是add里面的request", request);
        String response = httpClientUtils.sendPostByOkHttp(URL+"/friend/insert",request);
        if(response==null) return  0;
        return 1 ;
    }


    //从数据库里得到搜索的用户
    public int requestSearch(String email){
        String request = "{\n" +
                "            \"youxiang\":\""+email+"\"\n" +
                "        }"   ;
        String response = httpClientUtils.sendPostByOkHttp(URL+"/yonghu/select",request);
        if( response==null){
            return  0 ;
        }
        JSONObject jb = JSON.parseObject(response);
         imgid = jb.getString("touxiang");
        friendEmail = jb.getString("youxiang");
        mingzi = jb.getString("mingzi");
        searchFriendImg.setImageResource(Integer.parseInt(imgid));
        searchName.setText(mingzi);
        return 1;
    }




}
