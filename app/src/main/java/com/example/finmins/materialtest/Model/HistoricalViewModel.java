package com.example.finmins.materialtest.Model;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.finmins.materialtest.Bills;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HistoricalViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    final static String SERVERURL = "serverURL";
    private MutableLiveData<List<Bills>> billsList; //账单列表
    private MutableLiveData<String> userEmail ;   //用户账号（邮箱）

    //获取登陆的账户
    public MutableLiveData<String> getUserNum(){
        if (userEmail==null){
            userEmail = new MutableLiveData<String>();
            userEmail.setValue(null);
        }
        return userEmail;
    }

    //获取账单列表Bills
    public MutableLiveData<List<Bills>> getBillsList() {
        if (billsList == null) {
            billsList = new MutableLiveData<List<Bills>>();
            billsList.setValue(null);
        }
        return billsList;
    }

    //从数据库里得到账单列表
//    public MutableLiveData<List<Bills>> getBillsFromDB(){
//        OkHttpClient mOkHttpClient = new OkHttpClient();
//        //创建一个Request  默认是get请求
//        final Request request = new Request.Builder()
//                .url(SERVERURL)
//                .build();
//        //new call
//        Call call = mOkHttpClient.newCall(request);
//        //请求加入调度
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(Request request, IOException e)
//            {
//
//            }
//            @Override
//            public void onResponse(final Response response) throws IOException
//            {
//                String htmlStr =  response.body().string();
//            }
//        });
//        return
//    }

}
