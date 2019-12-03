package com.example.finmins.materialtest;


import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.view.ViewGroup;
import android.widget.ImageView;

import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Dialog;


import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.util.Attributes;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import android.util.DisplayMetrics;
import java.io.File;
public class MainActivity extends AppCompatActivity  {
    private DrawerLayout mDrawerLayout;
    private SwipeRefreshLayout swipeRefreshLayout ;
    private List<ShiJian> shiJianList = new ArrayList<ShiJian>();
    private FloatingActionButton fab;
    private ShiJianAdapter shiJianAdapter;
    private RecyclerView recyclerView;
    private TextView swipe ;
    private String TAV="MainActivity.this";
    private ImageView image;


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
                final int position_item=position;
                final int id_item=id;
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle("警告");
                dialog.setMessage("确认删除？");
                dialog.setCancelable(false);
                dialog.setPositiveButton("确认",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DataSupport.delete(ShiJian.class,id_item);
                        shiJianList.remove(position_item);
                        shiJianAdapter.notifyItemRemoved(position_item);
                        Toast.makeText(MainActivity.this,"事件删除成功",Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.setNegativeButton("取消",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
               dialog.show();


            }

            public void onSetImgId(int position,int id,ShiJian shijian ){
                View view = recyclerView.getLayoutManager().findViewByPosition(position);
                ImageView imageView=(ImageView)view.findViewById(R.id.image);
                TextView  finish =(TextView)view.findViewById(R.id.swipe_finish);
                ShiJian shijian1 =new ShiJian();
                if(shijian.get_imgId()==1){
                    shijian1.setToDefault("imgId");
                    shijian1.update(id);
                    imageView.setImageResource(R.mipmap.yuanquan);
                    finish.setText("完成");
                     } else{
                    shijian1.setImgId(1);
                    shijian1.update(id);
                    imageView.setImageResource(R.mipmap.zhengque);
                    finish.setText("未完成");
                        }
              //  shiJianAdapter.notifyDataSetChanged();
                Refresh();
                }
        });
        }




    //下拉刷新事件
    public void Refresh(){
        shiJianList.clear();
        recyclerView=(RecyclerView)findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
      //if(shiJianAdapter ==null) {
            shiJianAdapter = new ShiJianAdapter(this, shiJianList);
          shiJianAdapter.setMode(Attributes.Mode.Single);
         recyclerView.setAdapter(shiJianAdapter);
          recyclerView.setSelected(true);
       //}else {
          initShijian();
        shiJianAdapter.notifyDataSetChanged();
    //  }
        initEvents();
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
                Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
                setSupportActionBar(toolbar);
                //添加拉框菜单
                 mDrawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);

                NavigationView navView = (NavigationView)findViewById(R.id.nav_view);
                navView.setItemIconTintList(null);

                ActionBar actionBar = getSupportActionBar();
                if (actionBar !=null){
                    actionBar.setDisplayHomeAsUpEnabled(true);
                image = (ImageView)findViewById(R.id.wode);
                    actionBar.setHomeAsUpIndicator(R.mipmap.wode);
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




