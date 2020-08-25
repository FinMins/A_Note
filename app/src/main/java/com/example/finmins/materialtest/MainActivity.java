package com.example.finmins.materialtest;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;


import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Dialog;


import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.util.Attributes;
import com.example.finmins.materialtest.Model.MainViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import android.util.DisplayMetrics;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.io.File;
public class MainActivity extends AppCompatActivity {
   private MainViewModel mainViewModel;

    private  static  final  Integer NO_LOGINED = 0   ;//未登录

    private  static  final  Integer IS_LOGINED = 1   ;//已登录

    private DrawerLayout mDrawerLayout;   //侧滑栏

    private SwipeRefreshLayout swipeRefreshLayout ;   //删除完成侧滑

    private List<ShiJian> shiJianList = new ArrayList<ShiJian>(); //事件列表

    private FloatingActionButton float_main  ; //  浮动添加按钮   ;

    private ShiJianAdapter shiJianAdapter;   //  事件适配器

    private RecyclerView recyclerView;   //RecycleView

    private String TAG="MainActivity.this";   //Log用

    private ImageView isFinishedImage;    //是否完成的图片

    private ImageButton userImage ;   //用户头像

   private TextView userXingming ;     //用户名字

   private TextView user_Mail;       //用户邮箱

  private HttpClientUtils httpClientUtils = new HttpClientUtils() ;    //请求组件

    private NavigationView navigationView;


  private ImageView touxiang;

    //将数据库的数据读取到listView中,这是唯一的main获取数据库的入口
    private void initShijian(){
        shiJianList= mainViewModel.getShiJianList().getValue();
    }

  //覆写重新加载函数
    protected void onResume() {
        super.onResume();
        Refresh();
        initEvents();
    }

    //adapter点击事件
    public void initEvents(){
        shiJianAdapter.setOnItemClickLitener(new ShiJianAdapter.OnItemClickLitener(){

            //根据ID进入事件具体内容
            @Override
            public void onItemClick(View view, int position,int id) {
                //创建对应的intent
                Intent intent = new Intent(MainActivity.this,ChaKanActivity.class);
                intent.putExtra("ids",id);
                startActivity(intent);
            }


            //根据ID删除事件
            @Override
            public void onDeleteClick(int position,int id) {
                final int position_item=position;   //获取listview中的位置
                final int id_item=id;    //事件的id

                //点击删除时的警告窗口
               AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle("警告");
                dialog.setMessage("确认删除？");
                dialog.setCancelable(false);

                //点击确定执行的代码
                dialog.setPositiveButton("确认",new DialogInterface.OnClickListener(){

                    public void onClick(DialogInterface dialog, int which) {
                     //根据ID删除事件
                        DataSupport.delete(ShiJian.class,id_item);
                        shiJianList.remove(position_item);
                        shiJianAdapter.notifyItemRemoved(position_item);
                        Refresh();
                        Toast.makeText(MainActivity.this,"事件删除成功",Toast.LENGTH_SHORT).show();
                    }
                });

                //点击取消执行的代码
                dialog.setNegativeButton("取消",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //点击取消无任何反应
                    }
                });
               dialog.show();


            }

            //根据完成与否设置完成图片
            public void onSetImgId(int position,int id,ShiJian shijian ){

                View view = recyclerView.getLayoutManager().findViewByPosition(position);

                ImageView imageView=(ImageView)view.findViewById(R.id.image);
                //默认的已完成
                TextView  finish =(TextView)view.findViewById(R.id.swipe_finish);

                ShiJian shijian1 =new ShiJian();

                //根据事件imgID设置图片并修改imgID
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
            shiJianAdapter = new ShiJianAdapter(this, shiJianList);
          shiJianAdapter.setMode(Attributes.Mode.Single);
         recyclerView.setAdapter(shiJianAdapter);
          recyclerView.setSelected(true);
          initShijian();
        shiJianAdapter.notifyDataSetChanged();

        initEvents();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);

                mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
                navigationView= findViewById(R.id.nav_view);

                //观察登录状态
        mainViewModel.getIsLogined().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if(integer ==1){
                  //登录状态从登录变成已登录
//                        Viewmodel里自己设置了
                }
                if(integer ==0){
                    //登录状态从已登录变成未登录
               touxiang.setImageResource(R.drawable.header);
               user_Mail.setText("xxxxxx@xxxxx.com");
               userXingming.setText("xxxx");
                }
            }
        });

                //观察用户邮箱
        mainViewModel.getUserEmail().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
            user_Mail.setText(s);
            }
        });
        //观察用户头像
        mainViewModel.getuserImgId().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
            touxiang.setImageResource(integer);
            }
        });

                //观察用户名
                mainViewModel.getUserEmail().observe(this, new Observer<String>() {
                    @Override
                    public void onChanged(String s) {
                 userXingming.setText(s);
                        }
                });

             //观察事件组，如果一改变就直接把整个事件组传递给后台。
                 mainViewModel.getShiJianList().observe(this, new Observer<List<ShiJian>>() {
                   @Override
                 public void onChanged(List<ShiJian> s) {
                       Log.d(TAG, "调用了观察 ");
                      Refresh();

                 }
                  });



                user_Mail = navigationView.getHeaderView(0).findViewById(R.id.user_mail);
                userXingming = navigationView.getHeaderView(0).findViewById(R.id.userXingming);
                touxiang = navigationView.getHeaderView(0).findViewById(R.id.user_image);

                touxiang.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                   if( mainViewModel.getIsLogined().getValue()==0){
                       Intent loginIntent = new Intent(MainActivity.this,LoginActivity.class);
                       startActivity(loginIntent);
                   }
                   if(mainViewModel.getIsLogined().getValue() == 1){
                       //进入用户登录后的界面
                       Toast.makeText(MainActivity.this, "长按退出登录", Toast.LENGTH_SHORT).show();
                   }
                    }
                });
;

            //头像长按退出
        touxiang.setOnLongClickListener(new View.OnLongClickListener(){

            public boolean onLongClick(View v) {
                //没登录
                if(mainViewModel.getIsLogined().getValue()==NO_LOGINED){
                    Toast.makeText(MainActivity.this, "请登陆在尝试退出", Toast.LENGTH_SHORT).show();
                }
                //已登录
                if(mainViewModel.getIsLogined().getValue()==IS_LOGINED) {

                    AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                    dialog.setTitle("通知");
                    dialog.setMessage("是否退出登录");
                    dialog.setCancelable(false);
                    dialog.setPositiveButton("是", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                       //退出账号
                            mainViewModel.setIsLogined(NO_LOGINED);
                        }
                    });

                    //点击否不做任何修改
                    dialog.setNegativeButton("否", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });

                    dialog.show();
                }
                return true;

            }
        });


                //下拉刷新
                swipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.swipeRefreshLayout);
                swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                      Refresh();
                        swipeRefreshLayout.setRefreshing(false);
            }});

                //点击悬浮按钮添加事件进入InsertActivity
                float_main = (FloatingActionButton)findViewById(R.id.float_main );
                float_main.setOnClickListener(new View.OnClickListener(){
                    public void onClick(View v){
                     Intent intent = new Intent(MainActivity.this,InsertActivity.class);
                    startActivity(intent);
                    }
                });
                Refresh();

                //状态栏
               Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
                setSupportActionBar(toolbar);

                //添加拉框菜单
                 mDrawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);
                NavigationView navView = (NavigationView)findViewById(R.id.nav_view);
                navView.setItemIconTintList(null);

                 ActionBar actionBar = getSupportActionBar();
                if (actionBar !=null){
                    //侧滑栏点击按钮
                    actionBar.setDisplayHomeAsUpEnabled(true);

                isFinishedImage = (ImageView)findViewById(R.id.wode);
                    actionBar.setHomeAsUpIndicator(R.mipmap.wode);
                }
                //用户头像点击(暂时没有setonclick方法
//                userImage = (ImageButton)findViewById(R.id.user_image) ;

                /*
                userImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MainActivity.this,"好友",Toast.LENGTH_SHORT).show();
                    }
                });
*/

                navView.setCheckedItem(R.id.nav_mainShiJian);
                navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){
                    public boolean onNavigationItemSelected(MenuItem item){
                     switch (item.getItemId())
                     {
                         case R.id.nav_friends:
                             //好友逻辑
                             if (mainViewModel.getIsLogined().getValue()==NO_LOGINED){
                                 Toast.makeText(MainActivity.this, "请先登陆", Toast.LENGTH_SHORT).show();
                                 break;
                             }
                             if(mainViewModel.getIsLogined().getValue()==IS_LOGINED) {
                                 Intent friendIntent = new Intent(MainActivity.this, FriendsActivity.class);
                                 //把viewmodel里的用户账号传输过去
                                 friendIntent.putExtra("userNum",mainViewModel.getUserEmail().getValue());
                                 startActivity(friendIntent);
                                 break;
                             }
                         case R.id.nav_group:
                             if (mainViewModel.getIsLogined().getValue()==NO_LOGINED){
                                 Toast.makeText(MainActivity.this, "请先登陆", Toast.LENGTH_SHORT).show();
                                 break;
                             }
                             if(mainViewModel.getIsLogined().getValue()==IS_LOGINED) {
                            Intent groupIntent = new Intent(MainActivity.this,Groupctivity.class);
                            groupIntent.putExtra("userEmail",mainViewModel.getUserEmail().getValue());
                            startActivity(groupIntent);
                             //群组逻辑
                                 break;
                             }
                         case R.id.nav_userdata:
                             //修改资料
                             Intent changeInfor = new Intent(MainActivity.this,ChangeinformationActivity.class);
                             startActivity(changeInfor);
                             break;
                         case R.id.nav_bill:
                             //我的账单
                             Intent mybill = new Intent(MainActivity.this,FinanceActivity.class);
                             startActivity(mybill);
                             break;
                     }
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

                 //点击测试按钮
                case R.id.backup:
                //    Toast.makeText(MainActivity.this,"You clicked Search",Toast.LENGTH_SHORT).show();
                    Intent intentSearch  = new Intent(MainActivity.this,SearchActivity.class);
                    startActivity(intentSearch);
                    break;

                //点击？按钮
                 case android.R.id.home:
                     mDrawerLayout.openDrawer(GravityCompat.START);
                     break;

                default:
            }
            return true;
        }


    }




