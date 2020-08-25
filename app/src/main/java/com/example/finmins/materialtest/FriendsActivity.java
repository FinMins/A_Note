package com.example.finmins.materialtest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.finmins.materialtest.Model.FriendViewModel;

import java.util.ArrayList;
import java.util.List;

public class FriendsActivity extends AppCompatActivity {
    private static final String TAG = "FriendActivity";
    private FriendViewModel friendViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        friendViewModel = new ViewModelProvider(this).get(FriendViewModel.class);
        Intent intent =getIntent();
        String userNum = intent.getStringExtra("userNum");
        friendViewModel.setUserEmail(userNum);

    }

}
