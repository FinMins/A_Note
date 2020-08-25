package com.example.finmins.materialtest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.finmins.materialtest.Model.LoginViewModel;
import com.example.finmins.materialtest.Model.MainViewModel;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    private Button registerButton;         //注册按钮
    private Button cancelButton ;     //取消注册按钮
    private EditText  registerName;        //注册名字控件
    private EditText registPassword;      //   注册密码控件
    private EditText  registerPasswordAgin;         //  重复密码控件
    private EditText registerEmail;          //    注册邮箱控件
   private MainViewModel mainViewModel  = new MainViewModel();        //    loginiemodel
      private   HttpClientUtils httpClientUtils; //   请求类
    private Integer userImgID ;   //用户随机的头像ID。
    private Random randomImgInt ;    //随机生成数


    Random r = new Random(1);


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //初始化控件
            init();






//        注册点击响应
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkall()==1){
                    //验证已过，开始发注册给后台
                 //把Json 包装成数据


                //请求响应返回类型为1，则退出当前界面，返回登录界面。
                finish();
                }
            }
        });

//        取消点击响应,退回上一个界面
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


  //根据随机的头像数字来设置头像的resourceId
   private void setImgId(int radomImgId){

        switch (radomImgId){
            case 1:
                userImgID = R.drawable.touxiang1;
                break;
            case 2:
                userImgID = R.drawable.touxiang2;
                break;
            case 3:
                userImgID = R.drawable.touxiang3;
                break;
            case 4:
                userImgID = R.drawable.touxiang4;
                break;
            default:
                 userImgID = R.drawable.touxiang1;

        }

   }


//判断字符串是否是邮箱格式的方法
    public static boolean isEmailValid(String email) {
       boolean isValid = false;
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
                isValid = true;
            }
        return isValid;
            }



    //判断两个密码是否相等
    private int checkPasIsEqual(){
       if( registPassword.getText().toString().equals(registerPasswordAgin.getText().toString())){
           return 1;
       }
        return 0;
    }

    //判断输入字符是否为空，并且确定密码相等，邮箱为正确格式
    private int checkall(){
        String registerPasswordText = registPassword.getText().toString();
        String registerPasswordAgainText = registerPasswordAgin.getText().toString();
        String registerNameText = registerName.getText().toString();
        String registEmail = registerEmail.getText().toString();
        if(registEmail.equals("")||registerPasswordAgainText.equals("")||registerPasswordText.equals("")||registerNameText.equals("")){
            Toast.makeText(RegisterActivity.this, "输入框有为空，请重新输入", Toast.LENGTH_SHORT).show();
            return 0;
        }
        if(!isEmailValid(registEmail)){
            Toast.makeText(RegisterActivity.this, "邮箱格式输入错误", Toast.LENGTH_SHORT).show();
            return 0 ;
        }
         return 1;
    }



    //初始化控件
     private void init(){

        registerButton = findViewById(R.id.ConfirmRegister);
        cancelButton = findViewById(R.id.CancelRegister);
        registerName= findViewById(R.id.registerName);
        registPassword = findViewById(R.id.regisPassword);
        registerPasswordAgin = findViewById(R.id.registerPasswordAgain);
        registerEmail = findViewById(R.id.registEmail);
         mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        httpClientUtils = new HttpClientUtils();
        randomImgInt = new Random(1);
         userImgID = randomImgInt.nextInt(4);
         setImgId(userImgID);     //随机好用户的头像

     }


}
