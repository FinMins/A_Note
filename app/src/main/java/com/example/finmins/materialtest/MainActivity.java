package com.example.finmins.materialtest;

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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import org.litepal.crud.DataSupport;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
   private SwipeRefreshLayout swipeRefreshLayout ;
    private List<ShiJian> shiJianList = new ArrayList<ShiJian>();
//将数据库的数据读取到listView中
    private void initShijian(){
      List<ShiJian> shijians = DataSupport.findAll(ShiJian.class);
        for(ShiJian shijian:shijians){
            ShiJian shijian_list=new ShiJian(shijian.getBiaoti(),shijian.getTime(),shijian.getYear(),shijian.getMonth(),shijian.getDay());
            shiJianList.add(shijian_list);
           }
    }






    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initShijian();

        //下拉刷新
        swipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                shiJianList.clear();     //清楚listView内容
                shiJianList.notify(); ;
                initShijian();     //重新加载listView内容
                swipeRefreshLayout.setRefreshing(false);
            }
        });


        //添加事件
        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.asd);
        fab.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
             Intent intent = new Intent(MainActivity.this,InsertActivity.class);
            startActivity(intent);
            }
        });

        ShiJianAdapter shiJianAdapter = new ShiJianAdapter(MainActivity.this,R.layout.list_main,shiJianList);
        ListView listView =(ListView)findViewById(R.id.list_view);
        listView.setAdapter(shiJianAdapter);

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
                    Toast.makeText(this,"You clicked Search",Toast.LENGTH_SHORT).show();
                    break;
                /***
                case R.id.delete:
                    Toast.makeText(this,"You clicked Delete",Toast.LENGTH_SHORT).show();
                    break;
                case R.id.settings:
                    Toast.makeText(this,"You clicked setting",Toast.LENGTH_SHORT).show();
                    break;
                 **/
                 case android.R.id.home:
                     mDrawerLayout.openDrawer(GravityCompat.START);
                     break;

                default:
            }
            return true;
        }

    }




