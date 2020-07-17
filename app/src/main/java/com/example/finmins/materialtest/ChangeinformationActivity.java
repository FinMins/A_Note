package com.example.finmins.materialtest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ChangeinformationActivity extends AppCompatActivity {
    private int ChangeImage;  //我的头像
    private EditText ChangeName; //我的昵称
    private EditText ChangeUserName; //用户名
    private EditText ChangeUserPassword; //我的密码
    private EditText ConfirmUserPassword; //确认密码
    private EditText ChangeEmail; //我的邮箱
    private String name;
    private String username;
    private String userpswd;
    private String useremail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changeinformation);
        ChangeName = (EditText)findViewById(R.id.ChangeName);
        ChangeUserName = (EditText)findViewById(R.id.ChangeUserName);
        ChangeUserPassword = (EditText)findViewById(R.id.ChangeUserPassword);
        ConfirmUserPassword = (EditText)findViewById(R.id.ConfirmUserPassword);
        ChangeEmail = (EditText)findViewById(R.id.ChangeEmail);
        name = ChangeName.getText().toString();
        username = ChangeUserName.getText().toString();
        userpswd = ChangeUserPassword.getText().toString();
        useremail = ChangeEmail.getText().toString();
        Button cancel = findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
    }
}
