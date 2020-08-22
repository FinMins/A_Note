package com.example.finmins.materialtest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Groupctivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupctivity);

        BottomNavigationView group  = findViewById(R.id.groupBottomNavigationView);
        NavController navController = Navigation.findNavController(this,R.id.groupfragment);
        AppBarConfiguration configuration = new AppBarConfiguration.Builder(group.getMenu()).build();
      //  NavigationUI.setupActionBarWithNavController(this,navController,configuration);
        NavigationUI.setupWithNavController(group,navController);
    }
}
