package com.example.finmins.materialtest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.finmins.materialtest.Model.FriendViewModel;
import com.example.finmins.materialtest.Model.GroupViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Groupctivity extends AppCompatActivity {
    private GroupViewModel groupViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupctivity);
        groupViewModel = new ViewModelProvider(this).get(GroupViewModel.class);
        Intent intent =getIntent();
        String userNum = intent.getStringExtra("userEmail");
        groupViewModel.setUserEmail(userNum);
        BottomNavigationView group  = findViewById(R.id.groupBottomNavigationView);
        NavController navController = Navigation.findNavController(this,R.id.groupfragment);
        AppBarConfiguration configuration = new AppBarConfiguration.Builder(group.getMenu()).build();


      //  NavigationUI.setupActionBarWithNavController(this,navController,configuration);
        NavigationUI.setupWithNavController(group,navController);


    }
}
