package com.example.finmins.materialtest;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class FriendsActivity extends AppCompatActivity {
    private static final String TAG = "FriendActivity";
//    private FriendViewModel friendViewModel;
    private String userEmail;
    private String usermingzi;
    private  String userLoginedImg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
//        friendViewModel = new ViewModelProvider(this).get(FriendViewModel.class);
        Intent intent =getIntent();
        userEmail = intent.getStringExtra("userNum");
        userLoginedImg = intent.getStringExtra("username");
       usermingzi = intent.getStringExtra("userloginedimg");
    }
    public  String toValue(){
        return  userEmail;
    }
    public String toImgValue(){return usermingzi;
   }
  public String tomingziValue(){return userLoginedImg;
  }

}
