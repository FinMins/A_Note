package com.example.finmins.materialtest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.finmins.materialtest.Model.HistoricalViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class HistoricalFragment extends Fragment {
    private RecyclerView recyclerView;
    private View view;  //界面视图
    private HistoricalViewModel mViewModel;
    private List<Bills> billsList = new ArrayList<Bills>(); //账单列表
    private BillsAdapter adapter;   //账单属性适配器
    private LinearLayoutManager linearLayoutManager;   //item线性布局
    private TextView dateView;   //账单时间View
    private TextView amountView; //账单金额View
    private ImageView searchBills; //搜索账单View
    private String userEmail ; //用户邮箱
    public HistoricalFragment(){
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        userEmail = ((HistoricalbillsActivity)context).toValue();
    }

    public static HistoricalFragment newInstance() {
        return new HistoricalFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
       view =  inflater.inflate(R.layout.fragment_historicalbills, container, false);
       return view;
    }

    /** 活动运行后调用 */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        mViewModel = ViewModelProviders.of(this).get(HistoricalViewModel.class);
        // TODO: Use the ViewModel
        initControl();
        try {
            initData();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "passWay: billAmount");
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        //        imageButton = findViewById(R.id.searchBills);
//        imageButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent searchIntent = new Intent(HistoricalbillsActivity.this,SearchBillsActivity.class);
//                startActivity(searchIntent);
//            }
//        });
//        historicalViewModel = new ViewModelProvider(this).get(HistoricalViewModel.class);
//        Intent intent = getIntent();
        // 添加点击事件（排序）
        /**将账单进行排序（时间、金额）*/
        dateView = getView().findViewById(R.id.billDate);
        amountView = getView().findViewById(R.id.billAmount);
        dateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(billsList!=null){

                    Collections.sort(billsList, new Comparator<Bills>() {
                        @Override
                        public int compare(Bills b1, Bills b2) {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            try {
                                if (sdf.parse(b1.getDate()).getTime() > sdf.parse(b2.getDate()).getTime()){
                                    return 1;
                                } else if (sdf.parse(b1.getDate()).getTime() < sdf.parse(b2.getDate()).getTime()){
                                    return -1;
                                }else{
                                    return 0;
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            return 0;
                        }
                    });
                }
                initControl();
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(adapter);
            }
        });
        amountView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (billsList != null){
                    Collections.sort(billsList,new Comparator<Bills>() {
                        @Override
                        public int compare(Bills b1, Bills b2) {
                            //按金额排序
                            if(b1.getAmount() > b2.getAmount()) {
                                return 1;
                            }else if(b1.getAmount() < b2.getAmount()) {
                                return -1;
                            }else {
                                return 0;
                            }
                        }
                    });
                }
                initControl();
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(adapter);
//                adapter.notifyDataSetChanged();
//                Log.d(TAG, "Amount1："+billsList.get(0).getAmount());
            }
        });
        /*
           搜索账单
        */
        searchBills = getView().findViewById(R.id.searchBills);
        searchBills.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent searchIntent = new Intent(getActivity(),SearchBillsActivity.class);
                startActivity(searchIntent);
            }
        });

    }
        /** 从数据库中读取数据 */
        private void initData() throws ParseException {
            HttpClientUtils client = new HttpClientUtils();
            String body = "   {\n" +
                    "    \"youxiang\":\""+userEmail+"\"\n" +
                    "} ";
            Log.d("这是Body:", body);
            String response = client.sendPostByOkHttp("http://192.168.43.61:9999/finance/selectlist",body);
            JSONArray jsonArray = JSON.parseArray(response);
            for (int i = 0; i < jsonArray.size(); i++) {       //这个是Json数组
                Log.d("这是jsonarray的大小", String.valueOf(jsonArray.size()));
                JSONObject obj = jsonArray.getJSONObject(i);
//                String email = (String) obj.get("youxiang");  //这个是Json数组的每一个json对象里的键值对。
                String currentdate = (String) obj.get("currentdate");
                Log.d(TAG, "currentdate:"+currentdate);
                String type = (String) obj.get("type1");
                Log.d(TAG, "type: "+type);
                String consumptionamount = (String) obj.get("consumptionamount");
                Log.d(TAG, "consumptionamount:"+consumptionamount);
                String localTime = (String) obj.get("timeID");
                Log.d(TAG, "Double.parseDouble(consumptionamount):"+Double.parseDouble(consumptionamount));
                Bills bill = new Bills(currentdate,type,Double.parseDouble(consumptionamount),localTime);
                billsList.add(bill);
                Log.d("这是host内容", String.valueOf(billsList.size()));
            }
            Toast.makeText(getContext(), "请求成功", Toast.LENGTH_SHORT).show();
        }
        /** 初始化元素 */
        private void initControl(){
            adapter = new BillsAdapter(getActivity(),billsList,mViewModel,1,null);
            recyclerView = view.findViewById(R.id.billsRecyclerView);
            linearLayoutManager = new LinearLayoutManager(getActivity());
            mViewModel = new ViewModelProvider(this).get(HistoricalViewModel.class);
        }
//        private void init(){
//            initControl();
//        }
}
