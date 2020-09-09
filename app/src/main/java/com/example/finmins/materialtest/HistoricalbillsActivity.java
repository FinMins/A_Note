package com.example.finmins.materialtest;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class HistoricalbillsActivity extends AppCompatActivity {
//    private HistoricalViewModel historicalViewModel;
    private ImageButton imageButton;
    private String useremail ;//用户邮箱
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historicalbills);
        Intent intent =getIntent();
        useremail = intent.getStringExtra("userNum");


    }

    public String toValue(){ return useremail;}

}
