package com.example.finmins.materialtest;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

public class FinanceActivity extends AppCompatActivity {
    private Button submit;
    // 日期
    private TextView date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finance);
        submit = findViewById(R.id.addButton);
        date = findViewById(R.id.addDate);
        Button cancelbtn = findViewById(R.id.cancelButton);
        date.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final int year = Calendar.getInstance().get(Calendar.YEAR);
                final int month = Calendar.getInstance().get(Calendar.MONTH);
                final int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
                @SuppressLint("ResourceType") DatePickerDialog datepicker = new DatePickerDialog(FinanceActivity.this, 2, new DatePickerDialog.OnDateSetListener() {
                    // 绑定监听器
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // 添加时间
                        date.setText(year + "年" + (monthOfYear + 1) + "月" + dayOfMonth + "日");
                    }
                }, year, month, day);
                datepicker.show();
            }
        });
        cancelbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
    }
}
