package com.example.finmins.materialtest;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

//import okhttp3.Call;
//import okhttp3.Callback;
//import okhttp3.FormBody;
//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.Response;
//import okhttp3.RequestBody;

public class FinanceActivity extends AppCompatActivity {
    private Button submit;
    // 日期
    private TextView dateView;
    private Spinner billType;
    private EditText billAmount;
    private TextView currAmount;
    private String userEmail ;  //用户邮箱
    private String dateStr;     //时间
    private String type;        //类型
    private double amount;      //金额
    private String localTime;   //本地时间
    private Button cancelButton;  //取消账单
    private Button addButton;     //提交账单
    private Button history;       //历史账单
    private static final String TAG = "FinanceActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finance);
        //获取用户邮箱
        Intent intent =getIntent();
        userEmail = intent.getStringExtra("userNum");


        currAmount = findViewById(R.id.currAmount);
        submit = findViewById(R.id.addButton);
        dateView = findViewById(R.id.addDate);
        billType = findViewById(R.id.type);
        billAmount = findViewById(R.id.money);
        cancelButton = findViewById(R.id.cancelButton);
        addButton = findViewById(R.id.addButton);
        history = findViewById(R.id.history);
        dateView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final int year = Calendar.getInstance().get(Calendar.YEAR);
                final int month = Calendar.getInstance().get(Calendar.MONTH);
                final int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
                @SuppressLint("ResourceType") DatePickerDialog datePicker = new DatePickerDialog(FinanceActivity.this, 2, new DatePickerDialog.OnDateSetListener() {
                    // 绑定监听器
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        dateStr = year+"-"+(monthOfYear+1)+"-"+dayOfMonth;
                        Log.d("这是dateStr:", dateStr);

//                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//                        try {
//                            date = sdf.parse(dateStr);
//                        } catch (ParseException e) {
//                            e.printStackTrace();
//                        }
                        // 添加时间
                        dateView.setText(year + "年" + (monthOfYear + 1) + "月" + dayOfMonth + "日");
                    }
                }, year, month, day);
                datePicker.show();
            }
        });
        //历史账单界面
        history.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(FinanceActivity.this,HistoricalbillsActivity.class);

                intent.putExtra("userEmail",userEmail );
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
        addButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(!billAmount.getText().toString().isEmpty()){
                    amount = Double.parseDouble(billAmount.getText().toString());
                }
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                sdf.setTimeZone(TimeZone.getTimeZone("GMT+08"));
                localTime = sdf.format(new Date());
                type = (String) billType.getSelectedItem();
                HttpClientUtils client = new HttpClientUtils();
                String body = " {\n" +
                        "    \"youxiang\":\""+userEmail+"\",\n" +
                        "    \"currentdate\":"+"\""+dateStr+"\""+",\n" +
                        "    \"type\":"+"\""+type+"\""+",\n" +
                        "    \"consumptionamount\":"+"\""+amount+"\""+",\n" +
                        "    \"timeID\":"+"\""+localTime+"\""+"\n" +
                        "} ";
                Log.d("这是Body:", body);
                client.sendPostByOkHttp("http://192.168.43.61:9999/finance/insertx",body);
                Toast.makeText(FinanceActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
            }
        });

//        RequestBody body = new FormBody.Builder()    //构建body
//                .add("type1","fafaad")  //添加参数的键值对，可多次add()
//                .build();
//        Request request = new Request.Builder()
//                .url("http://192.168.43.61:9999/finance/insert")
//                .post(body)    //将body加进来
//                .build();
//        //请求
//        Call call = client.newCall(request);
//        //添加到消息队列里，传入Callback，当响应报文回来时执行相应的回调方法
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(@NotNull Call call, @NotNull IOException e) {
//                /*
//                 * 请求失败的一些操作
//                 */
//                Log.d(TAG, e.toString());
//            }
//
//            @Override
//            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
//                try{
//                    String result = response.body().toString();
//                }catch(Exception e){
//                    e.printStackTrace();
//                }
//            }
//        });
    }
}
