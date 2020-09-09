package com.example.finmins.materialtest;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class ChangeinformationActivity extends AppCompatActivity {
//    private int ChangeImage;  //我的头像
//    private EditText changeName; //我的昵称
    private EditText changeUserName; //用户名
    private EditText changeUserPassword; //我的密码
    private EditText confirmUserPassword; //确认密码
    private  Button finish ;    //完成按钮
    private Button  cancel ;//取消按钮
    private HttpClientUtils httpClientUtils = new HttpClientUtils();
//    private ChangeInforViewModel changeInforViewModel;    //加载viewmodel
    private String name;          //修改的名字
    private String username;      //修改的名字
    private String userpswd;     //修改的密码
    private String useremail;     //用户邮箱
    private String userImg ;//用户头像
    private  final  String URL = "http://192.168.43.61:9999";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changeinformation);
        init();



        //完成按钮
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkIsNull(changeUserName) && checkIsNull(changeUserPassword)&&checkIsNull(confirmUserPassword) ){
                    //判断要修改用户名
                    if(checkPasIsEqual()){
                        //要修改密码
                        userpswd = changeUserPassword.getText().toString();
                        username = changeUserName.getText().toString();
                        final String requesbody = "   {\n" +
                                "    \"youxiang\":\""+useremail+"\",\n" +
                                "    \"password\":\""+userpswd+"\",\n" +
                                "    \"mingzi\":\""+username+"\"\n" +
                                "}\n";
                        String response =    httpClientUtils.sendPostByOkHttp(URL+"/yonghu/update",requesbody);
//                        Log.d("请求返回", response);
                        JSONObject jb = JSON.parseObject(response);
//                         String isSuccess = jb.getString("msg");
                       if(response!=null){
                           Toast.makeText(ChangeinformationActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
//                           Intent intent = new Intent(ChangeinformationActivity.this,MainActivity.class);
//                            intent.putExtra("loginMingZi",username);
//                            intent.putExtra("loginYouXiang",useremail);
//                            intent.putExtra("loginTouXiang",userImg);
//                           Log.d("Mainactivity:从登录获取的头像字符",userImg);
//                           Log.d("Mainactivity:从登录获取的名字",username );
//                           Log.d("Mainactivity:从登录获取的邮箱",useremail );
//                            startActivity(intent);
                           Intent intent = new Intent(ChangeinformationActivity.this,MainActivity.class);
                           intent.putExtra("loginMingZi",username);
                           intent.putExtra("loginTouXiang",userImg);
                           intent.putExtra("loginYouXiang",useremail);
                           Log.d("Mainactivity:从登录获取的头像字符",userImg);
                           Log.d("Mainactivity:从登录获取的名字",username );
                           Log.d("Mainactivity:从登录获取的邮箱",useremail );
                           startActivity(intent);
                       }
                       else{
                           Toast.makeText(ChangeinformationActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
                       }



                    }else {
                        Toast.makeText(ChangeinformationActivity.this, "两次输入的秘密不一致", Toast.LENGTH_SHORT).show();
                    }



                }


            }
        });

        //取消按钮，点击退出
        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

            }


    //检查输入框是否为空
    private  Boolean checkIsNull(EditText editText ){
        if(editText.getText().toString().equals("")){
            return false ;
        }
        return true ;
    }

    //判断两个密码是否相等
    private Boolean checkPasIsEqual(){
        if( changeUserPassword.getText().toString().equals(confirmUserPassword.getText().toString())){
            return true;
        }
        return false;
    }




    //初始化数据和控件
     private void init(){
         changeUserName = (EditText)findViewById(R.id.ChangeUserName);
         changeUserPassword = (EditText)findViewById(R.id.ChangeUserPassword);
         confirmUserPassword = (EditText)findViewById(R.id.ConfirmUserPassword);
         cancel = findViewById(R.id.cancel);
         finish = findViewById(R.id.finish);
         Intent intent =getIntent();
         useremail= intent.getStringExtra("userEmail");
         userImg = intent.getStringExtra("userTouXiang");
         Log.d("修改信息里获取的useremail", useremail);
         Log.d("修改信息里获取的userImg", userImg);
    }
}
