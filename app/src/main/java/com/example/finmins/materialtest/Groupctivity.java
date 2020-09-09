package com.example.finmins.materialtest;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.finmins.materialtest.Model.GroupViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Groupctivity extends AppCompatActivity {
    private GroupViewModel groupViewModel;
    private String userEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupctivity);
//        groupViewModel = new ViewModelProvider(this).get(GroupViewModel.class);
        Intent intent =getIntent();
         userEmail = intent.getStringExtra("userEmail");


//        groupViewModel.setUserEmail(userEmail);
        BottomNavigationView group  = findViewById(R.id.groupBottomNavigationView);
        NavController navController = Navigation.findNavController(this,R.id.groupfragment);
        AppBarConfiguration configuration = new AppBarConfiguration.Builder(group.getMenu()).build();
      //  NavigationUI.setupActionBarWithNavController(this,navController,configuration);
        NavigationUI.setupWithNavController(group,navController);
    }

    public  String toValue(){
        return  userEmail;
    }

}
