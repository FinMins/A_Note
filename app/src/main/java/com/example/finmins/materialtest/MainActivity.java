package com.example.finmins.materialtest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Date;


import com.daimajia.swipe.SwipeLayout;

import org.litepal.crud.DataSupport;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private DrawerLayout mDrawerLayout;
    private SwipeRefreshLayout swipeRefreshLayout ;
    private List<ShiJian> shiJianList = new ArrayList<ShiJian>();
    private FloatingActionButton fab;
    private ShiJianAdapter shiJianAdapter;
    private ListView listView;
    private TextView swipe ;





    //将数据库的数据读取到listView中
    private void initShijian(){
      List<ShiJian> shijians = DataSupport.findAll(ShiJian.class);
        for(ShiJian shijian:shijians){
            ShiJian shijian_list=new ShiJian(shijian.getBiaoti(),shijian.getTime(),shijian.getYear(),shijian.getMonth(),shijian.getDay());
            shiJianList.add(shijian_list);
           }
    }




    //下拉刷新事件
    public void Refresh(){
        shiJianList.clear();
        shiJianAdapter = new ShiJianAdapter(MainActivity.this,R.layout.list_main,shiJianList);
        listView =(ListView)findViewById(R.id.list_view);
        listView.setAdapter(shiJianAdapter);
        listView.setOnItemClickListener(this);
    }

    //list点击事件
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //通过view获取其内部的组件，进而进行操作
        String text = (String) ((TextView)view.findViewById(R.id.swipe_delete)).getText();
        //大多数情况下，position和id相同，并且都从0开始
        String showText = "点击第" + position+1 + "项，文本内容为：" + text + "，ID为：" + id;
        Toast.makeText(this, showText, Toast.LENGTH_LONG).show();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);




                //下拉刷新
                swipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.swipeRefreshLayout);
                swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                      Refresh();
                        initShijian(); //重新加载listView
                        swipeRefreshLayout.setRefreshing(false);
            }});

                //点击悬浮按钮添加事件进入InsertActivity
                fab = (FloatingActionButton)findViewById(R.id.asd);
                fab.setOnClickListener(new View.OnClickListener(){
                    public void onClick(View v){
                     Intent intent = new Intent(MainActivity.this,InsertActivity.class);
                    startActivity(intent);
                    }
                });



                Refresh();





        initShijian();
                Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
                setSupportActionBar(toolbar);
                //添加拉框菜单
                 mDrawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);

                NavigationView navView = (NavigationView)findViewById(R.id.nav_view);
                navView.setItemIconTintList(null);

                ActionBar actionBar = getSupportActionBar();
                if (actionBar !=null){
                    actionBar.setDisplayHomeAsUpEnabled(true);
                    actionBar.setHomeAsUpIndicator(R.mipmap.wode);;
                }

                navView.setCheckedItem(R.id.nav_call);
                navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){
                    public boolean onNavigationItemSelected(MenuItem item){
                    mDrawerLayout.closeDrawers();
                    return true;
                }
                });

        }



        public boolean onCreateOptionsMenu(Menu menu){
            getMenuInflater().inflate(R.menu.toolbar,menu);
            return  true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
             switch (item.getItemId()){
                case R.id.backup:
                    Toast.makeText(MainActivity.this,"You clicked Search",Toast.LENGTH_SHORT).show();
                    break;
                 case android.R.id.home:
                     mDrawerLayout.openDrawer(GravityCompat.START);
                     break;

                default:
            }
            return true;
        }

    }




