package com.example.finmins.materialtest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.daimajia.swipe.util.Attributes;
import com.example.finmins.materialtest.Model.HistoricalViewModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class SearchBillsActivity extends AppCompatActivity {
//    private  final String TAG = "SearchBillsActivity.this";
    private HistoricalViewModel historicalViewModel;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<Bills> billsList = new ArrayList<Bills>(); //账单列表
    private BillsAdapter billsAdapter;  // Bills适配器
    private RecyclerView recyclerView;  //RecycleView
    private ImageButton searchButton;   //确认搜索按钮
    private String searchStr ;          //搜索内容
    private EditText searchEdit;        //搜索框
    private ImageView backView;         //返回按钮
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_bills);
        searchButton = findViewById(R.id.searchInSearch); //搜索按钮
        searchEdit = findViewById(R.id.editTextInSearch); //搜索框
        swipeRefreshLayout=findViewById(R.id.swipeRefreshLayoutInSearch);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh();
                swipeRefreshLayout.setRefreshing(false);
            }});
        //点击搜索图标
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取搜索内容
                searchStr = searchEdit.getText().toString();
                //初始化搜查的账单数据
                Refresh();
            }
        });
        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    //将查询到的数据读取到listView中
    private void initBills(){
//       从数据库中获取账单列表bills
        List<Bills> bills = new ArrayList<Bills>();
        HttpClientUtils client = new HttpClientUtils();
        String body = "   {\n" +
                "    \"youxiang\":\"123456789@qq.com\"\n" +
                "} ";
        String response = client.sendPostByOkHttp("http://192.168.43.61:9999/finance/selectlist",body);
        JSONArray jsonArray = JSON.parseArray(response);
        for (int i = 0; i < jsonArray.size(); i++) {       //这个是Json数组
            Log.d("这是jsonarray的大小", String.valueOf(jsonArray.size()));
            JSONObject obj = jsonArray.getJSONObject(i);
//                String email = (String) obj.get("youxiang");  //这个是Json数组的每一个json对象里的键值对。
            String currentdate = (String) obj.get("currentdate");
            Log.d(TAG, "currentdate:" + currentdate);
            String type = (String) obj.get("type1");
            Log.d(TAG, "type: " + type);
            String consumptionamount = (String) obj.get("consumptionamount");
            Log.d(TAG, "consumptionamount:" + consumptionamount);
            String localTime = (String) obj.get("timeID");
            Log.d(TAG, "Double.parseDouble(consumptionamount):" + Double.parseDouble(consumptionamount));
            Bills bill = new Bills(currentdate, type, Double.parseDouble(consumptionamount), localTime);
            bills.add(bill);
        }
            Log.d("这是host内容", String.valueOf(billsList.size()));
        for(Bills bill : bills){
            //对每个事件进行字符搜索
            if(isValid(bill)){
                billsList.add(bill);
            }
        }
    }

    private boolean isValid(Bills bill){
        return bill.getDate().contains(searchStr)
                ||bill.getType().contains(searchStr)
                ||String.valueOf(bill.getAmount()).contains(searchStr);
    }
    public void initEvents() {

    }
    //刷新账单列表
    public void Refresh(){
        billsList.clear();
        recyclerView=findViewById(R.id.recycler_viewInSearch);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        billsAdapter = new BillsAdapter(this, billsList,historicalViewModel,0,searchStr);
        recyclerView.setAdapter(billsAdapter);
        initBills();
        billsAdapter.notifyDataSetChanged();
    }
}
