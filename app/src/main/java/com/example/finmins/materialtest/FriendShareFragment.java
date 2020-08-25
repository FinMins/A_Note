package com.example.finmins.materialtest;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.daimajia.swipe.util.Attributes;
import com.example.finmins.materialtest.Model.FriendViewModel;

import org.litepal.crud.DataSupport;

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
    private EditText shareSearch;
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
        shareAdapter = new ShareShiJianAdapter(getContext(),shareShiJianList,friendViewModel);
        shareRecyclerView = view.findViewById(R.id.share_recyclerview);
        shareButton = view.findViewById(R.id.shareButton);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        shareSearch=view.findViewById(R.id.shareEitd);

    }


    //将查询到的数据读取到listView中，还未写
    private void initShijian(){
        List<ShiJian>  shijians = DataSupport.select("*").find(ShiJian.class);
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
        shareAdapter = new ShareShiJianAdapter(getContext(),shareShiJianList,friendViewModel);
       // Log.d(getTag(), "点击了搜索");
        shareRecyclerView.setAdapter(shareAdapter);
      //  shareRecyclerView.setSelected(true);
        shareAdapter.notifyDataSetChanged();
initShijian();
    }
}
