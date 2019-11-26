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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.daimajia.swipe.util.Attributes;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity  {
    private DrawerLayout mDrawerLayout;
    private SwipeRefreshLayout swipeRefreshLayout ;
    private List<ShiJian> shiJianList = new ArrayList<ShiJian>();
    private FloatingActionButton fab;
    private ShiJianAdapter shiJianAdapter;
    private RecyclerView recyclerView;
    private TextView swipe ;
    private String TAV="MainActivity.this";




    //将数据库的数据读取到listView中
    private void initShijian(){
      List<ShiJian>  shijians = DataSupport.findAll(ShiJian.class);
        for(ShiJian shijian:shijians){
            shiJianList.add(shijian);
           }
    }

    protected void onResume() {
        super.onResume();
        Refresh();
        initShijian();
        initEvents();
    }


    //adapter点击事件
        public void initEvents(){
        shiJianAdapter.setOnItemClickLitener(new ShiJianAdapter.OnItemClickLitener(){
            @Override
            public void onItemClick(View view, int position,int id) {
              Intent intent = new Intent(MainActivity.this,ChaKanActivity.class);
                intent.putExtra("ids",id);
                startActivity(intent);
            }
            @Override
            public void onDeleteClick(int position,int id) {
                DataSupport.delete(ShiJian.class,id);
                shiJianList.remove(position);
                shiJianAdapter.notifyItemRemoved(position);
              Toast.makeText(MainActivity.this,"事件删除成功",Toast.LENGTH_SHORT).show();
            }
        });
        }




    //下拉刷新事件
    public void Refresh(){
        shiJianList.clear();
        recyclerView=(RecyclerView)findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        if(shiJianAdapter ==null) {
            shiJianAdapter = new ShiJianAdapter(this, shiJianList);
            shiJianAdapter.setMode(Attributes.Mode.Single);
            recyclerView.setAdapter(shiJianAdapter);
            recyclerView.setSelected(true);
        }else {
            shiJianAdapter.notifyDataSetChanged();
        }
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
              initEvents();

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




