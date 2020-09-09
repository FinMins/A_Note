package com.example.finmins.materialtest.Model;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.finmins.materialtest.Friend;
import com.example.finmins.materialtest.HttpClientUtils;
import com.example.finmins.materialtest.R;
import com.example.finmins.materialtest.ShiJian;

import java.util.List;

public class FriendViewModel extends ViewModel {
    private MutableLiveData<List<Friend>> friendList;     //朋友
    private MutableLiveData<String> searchName  ;//搜索出来的用户名
    private MutableLiveData<Integer> searchImg ;  //搜索用户头像
    private  MutableLiveData<List<ShiJian>> getShiJianList ; //接受事件
    private MutableLiveData<String> userEmail  ;   //用户邮箱
    private List<Friend>  tmpfriendlist  ;
//    private String userEmail  ;//用户邮箱
    public String friendEmail   ;//邮箱
   private HttpClientUtils httpClientUtils = new HttpClientUtils();   //请求模块
    private  final  String  URL = "http://192.168.43.61:9999";

    //获取用户email。
    public MutableLiveData<String> getUserEmail() {
        if (userEmail == null) {
            userEmail = new MutableLiveData<String>();
           userEmail.setValue("xxx@qq.com");
            Log.d("邮箱为空", "邮箱为空");
        }
//       Log.d("邮箱不为空", getUserEmail().getValue());
        return userEmail;
    }



    //获取friendlist
    public MutableLiveData<List<Friend>> getFriendList() {
        if (friendList == null) {
            friendList = new MutableLiveData<List<Friend>>();
            friendList.setValue(tmpfriendlist);

        }

        setFriendList();
        return friendList;
    }

    //获取搜索的用户名字
    public MutableLiveData<String> getSearchName() {
        if (searchName == null) {
            searchName = new MutableLiveData<String>();
            searchName.setValue(null);
        }
        return searchName;
    }

    //获取搜索的用户头像
    public MutableLiveData<Integer> getSearchImg() {
        if (searchImg == null) {
            searchImg = new MutableLiveData<Integer>();
            searchImg.setValue(R.drawable.header);
        }
        return searchImg;
    }
   //获取好友分享的事件
    public MutableLiveData<List<ShiJian>> getGetShiJianList(){
        if(getShiJianList==null){
            getShiJianList = new MutableLiveData<List<ShiJian>>();
            getShiJianList.setValue(null);
        }
        return getShiJianList;
    }
//    //设置从好友那来的事件合集
//    public void setGetShiJianList(List<ShiJian> a){
//        getShiJianList.setValue(a);
//    }
//    设置用户账号
    public void setUserEmail(String Num){
       getUserEmail();
        userEmail.setValue(Num);
//        Log.d("进入到好友设置自己邮箱", getUserEmail().getValue());
    }
    //设置用户邮箱
//    public void setUserEmail(String Email){userEmail=Email;}

    //设置好友列表
    public void setFriendList(){
       friendList.setValue( requestFriendList(userEmail.getValue()));
    }
    //设置搜索好友的头像
    public void setSearchImg(int a  ){
        searchImg.setValue(a);
    }
    //设置搜索好友的名字
    public void setSearchName(String name){
        searchName.setValue(name);
    }


    //从数据库里得到好友列表
    public List<Friend> requestFriendList(String selfEmail){
//        Log.d("这是好友列表请求", selfEmail);
        String request = "{\n" +
                "\"youxiang\":\""+selfEmail+"\"\n" +
                "}";
        String response  =    httpClientUtils.sendPostByOkHttp(URL+"friend/selectlist",request);
//        Log.d("这是好友请求列表内容", response);
        if(response==null){
//            获取失败
        //
        }else {
            JSONArray jsonArray = JSON.parseArray(response);
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                String friendmingzi = (String) obj.get("mingzi");
                String friendyouxiang = (String) obj.get("youxiang");
                String friendtouxiang = (String) obj.get("touxiang");
                Friend friend = new Friend(Integer.parseInt(friendtouxiang), friendmingzi, friendyouxiang);
                tmpfriendlist.add(friend);
            }
            //设置好友列表
//            setFriendList(tmpfriendlist);
//            return 1;
        return tmpfriendlist;
        }
        return tmpfriendlist;
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
            String touxiang = jb.getString("touxiang");
            friendEmail = jb.getString("youxiang");
            String mingzi = jb.getString("mingzi");
            searchImg.setValue(Integer.parseInt(touxiang));
            searchName.setValue(mingzi);
         return 1;
    }






    //把要分享的事件发送给后端：
    public  int requestShareShiJian(ShiJian shiJian){
        String response;
        //创建请求并返回
        response=  httpClientUtils.send(" "," ","");
        //成功则返回1，失败则返回0；
        return 0 ;
    }



    //删除
    public int deleteFriend(String email){
        String response;
        //创建请求并返回
        response= httpClientUtils.send("","","");


       return 1;
    }

    //添加朋友
    public int addFriend(String img,String mingzi,String friendemail){
        String request = "  {\n" +
                "\"touxiang\":\""+img+"\",\n" +
                "\"mingzi\":\""+mingzi+"\",\n" +
                "\"friendyouxiang\":\""+friendemail+"\",\n" +
                "\"youxiang\":\""+getUserEmail().getValue()+"\n" +
                "} ";
        Log.d("这是add里面的邮箱", getUserEmail().getValue());
       String response = httpClientUtils.sendPostByOkHttp(URL+"/friend/insert",request);




       if(response==null) return  0;
        return 1 ;
    }



}



