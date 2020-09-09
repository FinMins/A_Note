package com.example.finmins.materialtest.Model;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.finmins.materialtest.HttpClientUtils;
import com.example.finmins.materialtest.R;
import com.example.finmins.materialtest.ShiJian;

import java.util.ArrayList;
import java.util.List;

public class MainViewModel extends ViewModel {
    private MutableLiveData<Integer> isLogined;  //是否登录
    public  MutableLiveData<String> userName;  //用户名
    private MutableLiveData<String> userPassword;  //密码
    private MutableLiveData<String> userEmail;    // 用户邮箱
    private MutableLiveData<List<ShiJian>> shiJianList;//事件列表
    private MutableLiveData<Integer> userImgId;    //用户头像数字
    private HttpClientUtils httpClientUtils = new HttpClientUtils();   //请求
    final String URL = "http://192.168.43.61:9999";          //本地服务器网址
    private MutableLiveData<Integer> isLogin;
    private List<ShiJian> temshiJian = new ArrayList<ShiJian>();



    //获取用户头像数字
    public MutableLiveData<Integer> getuserImgId() {
        if (userImgId == null) {
            userImgId = new MutableLiveData<Integer>();
            userImgId.setValue(R.drawable.header);
        }
        return userImgId;
    }


    //是否已经登录     main专用。
    public MutableLiveData<Integer> getIsLogined() {
        if (isLogined == null) {
            isLogined = new MutableLiveData<Integer>();
            isLogined.setValue(1);
        }
        return isLogined;
    }


    //获取事件数据
    public MutableLiveData<List<ShiJian>> getShiJianList() {
        if (shiJianList == null) {
            shiJianList = new MutableLiveData<List<ShiJian>>();
            shiJianList.setValue(temshiJian);
            Log.d("事件列表为空", "123");

            return shiJianList;
        }
        Log.d("事件列表不为空", "213");

        setShiJianList();
        return shiJianList;
    }

    //获取名字
    public MutableLiveData<String> getUserName() {
        if (userName == null) {
            userName = new MutableLiveData<String>();
            userName.setValue("1");
            return userName;
        }

        return userName;
    }

    //获取密码
    public MutableLiveData<String> getUserPassword() {
        if (userPassword   == null) {
            userPassword = new MutableLiveData<String>();
            userPassword.setValue("xxxxx");
            return userPassword;
        }
        return userPassword;
    }


    //获取邮箱
    public MutableLiveData<String> getUserEmail() {
        if (userEmail == null) {
            userEmail = new MutableLiveData<String>();
            userEmail.setValue("1@qq.com");
            return userEmail;
        }
        return userEmail;
    }



    //设置用户名
    public void setUserName(String username) {
        Log.d("设置用户名：", username);
       //userName.setValue(username);
    }

    //设置密码
    public void setUserPassword(String userpassword) {
        userPassword.setValue(userpassword);
    }

    //设置邮箱
    public void setUserEmail(String usermail) {

        userEmail.setValue(usermail);
    }

    //设置登录
    public void setIsLogined(Integer isLogin) {
        isLogined.setValue(isLogin);
    }

    //设置头像
    public void setUserImgId(Integer imgId) {
//        Log.d("设置了头像", imgId.toString());
        userImgId.setValue(imgId);
    }

    //设置用户信息到主界面
    public void setUserInf() {
        //设置用户头像：
        setUserImgId(userImgId.getValue());
        //设置用户邮箱
        setUserEmail(userEmail.getValue());
        //设置用户名字
        setUserName(userName.getValue());
    }


    //从数据库得到事件列表
    public void setShiJianList() {
//        List<ShiJian> shijians = DataSupport.findAll(ShiJian.class);
//        for (ShiJian shijian : shijians) {
//            shiJianList.getValue().add(shijian);

            String request= "{\n" +
                    "\"youxiang\":\""+userEmail.getValue()+"\"\n" +
                    "}" ;
         String response =    httpClientUtils.sendPostByOkHttp(URL+"/shijian/select",request);
            if(response!=null){
//                JSONObject jsonObject = new JSONObject();
                JSONArray jsonArray = JSON.parseArray(response);
//                Log.d("这是返回的事件集合", ""+jsonArray);
    for(int i =0;i<jsonArray.size();i++){
        JSONObject obj = jsonArray.getJSONObject(i);
         int getId = (Integer)obj.get("id");
         String getBiaoTi = (String)obj.get("biaoti");
         String getNeiRong = (String)obj.get("neirong");
//         byte[] getPhoto = (byte[])obj.get("phot");
        byte[] getPhoto = new byte[1024];
       getPhoto =((String)obj.get("photo")).getBytes();
//        getPhoto = Base64.getDecoder().decode((String)obj.get("photo"));
         String getTime = (String)obj.get("place");
//        Log.d("这是返回的事件集合的", getTime);
//        Log.d("这是返回时的图片字符串", (String)obj.get("photo"));
        Log.d("这是tostring的图片字符串", obj.get("photo").toString());
        Log.d("这是原生转换的图片字符串", (String)obj.get("photo"));


//        Log.d("这是返回的事件集合的", getTime);
         ShiJian temshijian = new ShiJian();
         temshijian =setTempShiJian(getBiaoTi,getNeiRong,getPhoto,getTime);
         temshijian.save();
//        Log.d("这是存进去的时间，判断是否为空", temshijian.getTime());     不为空
         shiJianList.getValue().add(temshijian);
    }

            }
//        }
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

    //删除事件
    public void deleteShiJian(String time){
        Log.d("gettime是", time);
        String delete = "   {\n" +
                "\"place\":\""+time+"\"\n" +
                "}\n  ";
        httpClientUtils.sendPostByOkHttp(URL+"/shijian/delete",delete);

    }
}

