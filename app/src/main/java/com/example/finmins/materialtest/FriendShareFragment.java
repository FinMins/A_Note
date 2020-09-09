package com.example.finmins.materialtest;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.finmins.materialtest.Model.FriendViewModel;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class FriendShareFragment extends Fragment {
     private View view;//当前布局
     private RecyclerView shareRecyclerView;
     private List<ShiJian>  shareShiJianList = new ArrayList<ShiJian>();
     private ShareShiJianAdapter shareAdapter;
    private LinearLayoutManager linearLayoutManager;   //item线性布局
    private ImageButton shareButton  ;
    private HttpClientUtils httpClientUtils  = new HttpClientUtils();
    private EditText shareSearch;
    private String userEmail ;
    private String friendEmail ; //好友邮箱
    private final  String URL= "http://192.168.43.61:9999";
    private String search;  //搜索内容
     private FriendViewModel friendViewModel;
    public FriendShareFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view  = inflater.inflate(R.layout.fragment_friend_share, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        Log.d("点击了容器", getArguments().toString());
         userEmail =  getArguments().getString("userEmail");
         friendEmail = getArguments().getString("friendEmail");
        initControl();
        shareRecyclerView.setLayoutManager(linearLayoutManager);
        shareRecyclerView.setAdapter(shareAdapter);


        //点击搜索事件
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search =shareSearch.getText().toString();
                Refresh();
            }
        });
    }


    private void initControl(){
        friendViewModel   = new ViewModelProvider(this).get(FriendViewModel.class);
        shareAdapter = new ShareShiJianAdapter(getContext(),shareShiJianList,friendViewModel,userEmail,friendEmail);
        shareRecyclerView = view.findViewById(R.id.share_recyclerview);
        shareButton = view.findViewById(R.id.shareButton);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        shareSearch=view.findViewById(R.id.shareEitd);

    }

    //读取的事件暂时存在一个temshijian里。
    private ShiJian setTempShiJian( String getBiaoTi , String getNeiRong,byte[] getPhoto,String getTime ){
        ShiJian shijian = new ShiJian( );
        shijian.setBiaoti(getBiaoTi);
        shijian.setNeirong(getNeiRong);
        shijian.setImgId(0);
        shijian.setTime(getTime);
        shijian.setPhoto(getPhoto);
        Log.d("存在事件里的图片字符串", shijian.getPhotoString());;
        return shijian;
    }


    //将查询到的数据读取到listView中，
    private void initShijian(){
        List<ShiJian>  shijians = new ArrayList<ShiJian>();
        //从数据库得到事件列表
//        List<ShiJian> shijians = DataSupport.findAll(ShiJian.class);
//        for (ShiJian shijian : shijians) {
//            shiJianList.getValue().add(shijian);
        String request= "{\n" +
                "\"youxiang\":\""+userEmail+"\"\n" +
                "}" ;
        Log.d("这是分享好友里面的reqest", request);
            String response =   httpClientUtils .sendPostByOkHttp(URL+"/shijian/select",request);

            if(response!=null){
                JSONArray jsonArray = JSON.parseArray(response);
                for(int i =0;i<jsonArray.size();i++){
                    JSONObject obj = jsonArray.getJSONObject(i);
                    int getId = (Integer)obj.get("id");
                    String getBiaoTi = (String)obj.get("biaoti");
                    String getNeiRong = (String)obj.get("neirong");
                    byte[] getPhoto = new byte[1024];
                    getPhoto =((String)obj.get("photo")).getBytes();
                    String getTime = (String)obj.get("place");
                    Log.d("这是tostring的图片字符串", obj.get("photo").toString());
                    Log.d("这是原生转换的图片字符串", (String)obj.get("photo"));
                    ShiJian temshijian = new ShiJian();
                    temshijian =setTempShiJian(getBiaoTi,getNeiRong,getPhoto,getTime);
                    temshijian.save();
                    shijians.add(temshijian);
                }
            }
        for(ShiJian shijian:shijians){
            //对每个事件进行字符搜索
            if(checkString(shijian)==1) {
                Log.d(getTag(), "checkString==1 ");
                shareShiJianList.add(shijian);
            }
        }
    }

    private int checkString(ShiJian shijian ){
        if(shijian.getBiaoti().indexOf(search)>-1)
            return 1;
        else   if(shijian.getNeirong().indexOf(search)>-1)
            return 1;
        else if(search.equals(String.valueOf(shijian.getYear())))
            return 1;
        else  if(search.equals(String.valueOf(shijian.getMonth())))
            return 1;
        else  if(search.equals(String.valueOf(shijian.getDay())))
            return 1;
        else return 0;
    }

    public void Refresh(){
       // initShijian();
        shareShiJianList.clear();
        shareRecyclerView=view.findViewById(R.id.share_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        shareRecyclerView.setLayoutManager(layoutManager);
        shareAdapter = new ShareShiJianAdapter(getContext(),shareShiJianList,friendViewModel,userEmail,friendEmail);
       // Log.d(getTag(), "点击了搜索");
        shareRecyclerView.setAdapter(shareAdapter);
      //  shareRecyclerView.setSelected(true);
        shareAdapter.notifyDataSetChanged();
       initShijian();
    }
}
