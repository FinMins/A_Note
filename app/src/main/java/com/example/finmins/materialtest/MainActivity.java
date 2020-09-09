package com.example.finmins.materialtest;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.daimajia.swipe.util.Attributes;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;
public class MainActivity extends AppCompatActivity {
//   private MainViewModel mainViewModel;

    private  static  final  Integer NO_LOGINED = 0   ;//未登录

    private  static  final  Integer IS_LOGINED = 1   ;//已登录
    final String URL = "http://192.168.43.61:9999";          //本地服务器网址

    private DrawerLayout mDrawerLayout;   //侧滑栏

    private SwipeRefreshLayout swipeRefreshLayout ;   //删除完成侧滑

    private List<ShiJian> shiJianList = new ArrayList<ShiJian>(); //事件列表

    private FloatingActionButton float_main  ; //  浮动添加按钮   ;

    private ShiJianAdapter shiJianAdapter;   //  事件适配器

    private RecyclerView recyclerView;   //RecycleView

    private String TAG="MainActivity.this";   //Log用

    private ImageView isFinishedImage;    //是否完成的图片

//    private ImageButton userImage ;   //用户头像

   private TextView userXingming ;     //用户名字

   private TextView user_Mail;       //用户邮箱

    private String userSelfEmail  ;   //用户登录后的邮箱

    private String userLoginedImg  ;
    private String userName ;

    private Integer isLogined =  0 ;

  private HttpClientUtils httpClientUtils = new HttpClientUtils() ;    //请求组件

    private NavigationView navigationView;


  private ImageView touxiang;

    //将数据库的数据读取到listView中,这是唯一的main获取数据库的入口
    private void initShijian(){
         setShiJianList();
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
            public void onItemClick(View view, int position,int id,ShiJian shiJian) {
                //创建对应的intent
                Intent intent = new Intent(MainActivity.this,ChaKanActivity.class);
                intent.putExtra("biaoti",shiJian.getBiaoti());
                intent.putExtra("neirong",shiJian.getBiaoti());
                intent.putExtra("time", shiJian.getTime());
//                intent.putExtra("email",userSelfEmail);
                startActivity(intent);
            }


            //根据ID删除事件
            @Override
            public void onDeleteClick(int position, final int id,final  String time) {
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
//                       ShiJian shijian = DataSupport.find(ShiJian.class,id);
//                        DataSupport.delete(ShiJian.class,id_item);
                        shiJianList.remove(position_item);
                        shiJianAdapter.notifyItemRemoved(position_item);
                        deleteShiJian(time);
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
                   setFinishe("0",shijian.getTime(),userSelfEmail);
                    imageView.setImageResource(R.mipmap.yuanquan);
                    finish.setText("完成");
                    Log.d("点击了完成", "变成未完成");
                     } else{
                    shijian1.setImgId(1);
                    setFinishe("1",shijian.getTime(),userSelfEmail);
                    imageView.setImageResource(R.mipmap.zhengque);
                    finish.setText("未完成");
                    Log.d("点击了完成", "变成已完成");

                }
              //  shiJianAdapter.notifyDataSetChanged();
                Refresh();
                }
        });
        }




    //下拉刷新事件
        public void Refresh(){
//            mainViewModel.getUserName();
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

    private int setFinishe(String finish,String time ,String youxiang){
        String request = " {\n" +
                "\"finished\":\""+finish+"\",\n" +
                "\"place\":\""+time+"\",\n" +
                "\"youxiang\":\""+youxiang+"\"\n" +
                "}\n";
        String response = httpClientUtils.sendPostByOkHttp(URL+"/shijian/updatefinished",request);

        return 1;
    }



    private   void init(){
        Intent intent = getIntent();
        String userloginedName = intent.getStringExtra("loginMingZi");
        String  userloginedEmail = intent.getStringExtra("loginYouXiang");
        String   userloginedTouXiang =  intent.getStringExtra("loginTouXiang");

        if (userloginedEmail==null||userloginedName==null||userloginedTouXiang==null){
            userLoginedImg = String.valueOf(R.drawable.header);
            userSelfEmail = "xxxx@qq.com";
            userName = "未登录";
//            mainViewModel.getUserName();
//            mainViewModel.getUserEmail();
//            mainViewModel.getuserImgId();
//            mainViewModel.getUserPassword();
//            mainViewModel.getIsLogined();
//            mainViewModel.userName.setValue(userName);
//            System.out.println(mainViewModel.userName.toString());
//            Log.d("这是大佬的内容", mainViewModel.userName.toString());
//            mainViewModel.setUserEmail(userEmail);
//            mainViewModel.setUserImgId(userTouXiang);
//            mainViewModel.setUserPassword(userPassword);
//            mainViewModel.setIsLogined(1);
            touxiang.setImageResource(R.drawable.header);
        }else{
            Log.d("Mainactivity:从登录获取的头像字符",userloginedTouXiang );
            Log.d("Mainactivity:从登录获取的名字",userloginedName );
            Log.d("Mainactivity:从登录获取的邮箱",userloginedEmail );
            userLoginedImg = userloginedTouXiang;
            userSelfEmail = userloginedEmail;
            userName = userloginedName;
            touxiang.setImageResource(Integer.parseInt(userLoginedImg));
            isLogined = IS_LOGINED;
        }

        user_Mail.setText(userSelfEmail);
        userXingming.setText(userName);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);
//               mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);

        navigationView= findViewById(R.id.nav_view);
//                //观察登录状态
//        mainViewModel.getIsLogined().observe(this, new Observer<Integer>() {
//            @Override
//            public void onChanged(Integer integer) {
//                if(integer ==1){
//                  //登录状态从登录变成已登录
//                  mainViewModel.setUserInf();
//                }
//                if(integer ==0){
//                    //登录状态从已登录变成未登录
////               touxiang.setImageResource(R.drawable.header);
//////               user_Mail.setText("xxxxxx@xxxxx.com");
//////               userXingming.setText("xxxx");
//                    mainViewModel.setUserImgId(R.drawable.header);
//                    mainViewModel.setUserEmail("xxxx@qq.com");
//                    mainViewModel.getUserName();
//                    mainViewModel.setUserName("xxxxx");
//                }
//            }
//        });

//                //观察用户邮箱
//        mainViewModel.getUserEmail().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(String s) {
//                Log.d("观察到了邮箱", s);
//            user_Mail.setText(s);
//            }
//        });
        //观察用户头像
//        mainViewModel.getuserImgId().observe(this, new Observer<Integer>() {
//            @Override
//            public void onChanged(Integer integer) {
//            touxiang.setImageResource(integer);
//            }
//        });
        //观察用户名
//                mainViewModel.getUserName().observe(this, new Observer<String>() {
//                    @Override
//                    public void onChanged(String s) {
//                        Log.d("观察到了用户名", s);
//                 userXingming.setText(s);
//                        }
//                });

//             //观察事件组，如果一改变就直接把整个事件组传递给后台。
//                 mainViewModel.getShiJianList().observe(this, new Observer<List<ShiJian>>() {
//                   @Override
//                 public void onChanged(List<ShiJian> s) {
//                       Log.d(TAG, "调用了观察 ");
//                      Refresh();
//
//                 }
//                  });
        user_Mail = navigationView.getHeaderView(0).findViewById(R.id.user_mail);
        userXingming = navigationView.getHeaderView(0).findViewById(R.id.userXingming);
        touxiang = navigationView.getHeaderView(0).findViewById(R.id.user_image);
                init();




                //头像单击登录
                touxiang.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                   if( isLogined==NO_LOGINED){
                       Intent loginIntent = new Intent(MainActivity.this,LoginActivity.class);
                       startActivity(loginIntent);
                   }
                   if(isLogined == IS_LOGINED){
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
                if(isLogined==NO_LOGINED){
                    Toast.makeText(MainActivity.this, "请登陆在尝试退出", Toast.LENGTH_SHORT).show();
                }
                //已登录
                if(isLogined==IS_LOGINED) {

                    AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                    dialog.setTitle("通知");
                    dialog.setMessage("是否退出登录");
                    dialog.setCancelable(false);
                    dialog.setPositiveButton("是", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                       //退出账号
                            isLogined = NO_LOGINED;
                            touxiang.setImageResource(R.drawable.header);
                            user_Mail.setText("xxxx@qq.com");
                            userXingming.setText("xxxx");

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
                        if (isLogined==NO_LOGINED){
                            Toast.makeText(MainActivity.this, "请先登陆", Toast.LENGTH_SHORT).show();
                        }
                        if(isLogined==IS_LOGINED) {
                            Intent intent = new Intent(MainActivity.this,InsertActivity.class);
                            intent.putExtra("userEmail",userSelfEmail);
                            startActivity(intent);
                        }
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
//              userImage = (ImageButton)findViewById(R.id.user_image) ;

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
                             if (isLogined==NO_LOGINED){
                                 Toast.makeText(MainActivity.this, "请先登陆", Toast.LENGTH_SHORT).show();
                                 break;
                             }
                             if(isLogined==IS_LOGINED) {
                                 Intent friendIntent = new Intent(MainActivity.this, FriendsActivity.class);
                                 //把viewmodel里的用户账号传输过去
                                 friendIntent.putExtra("userNum",userSelfEmail);
                                 friendIntent.putExtra("username",userName);
                                 friendIntent.putExtra("userloginedimg",userLoginedImg);
                                 startActivity(friendIntent);
                                 break;
                             }
                         case R.id.nav_group:
                             //群
                             if (isLogined==NO_LOGINED){
                                 Toast.makeText(MainActivity.this, "请先登陆", Toast.LENGTH_SHORT).show();
                                 break;
                             }
                             if(isLogined==IS_LOGINED) {
                            Intent groupIntent = new Intent(MainActivity.this,Groupctivity.class);
                            groupIntent.putExtra("userEmail",userSelfEmail);
                            startActivity(groupIntent);
                             //群组逻辑
                                 break;
                             }
                         case R.id.nav_userdata:
                             //修改资料
                             if (isLogined==NO_LOGINED){
                                 Toast.makeText(MainActivity.this, "请先登陆", Toast.LENGTH_SHORT).show();
                                 break;
                             }
                             if(isLogined==IS_LOGINED) {
                                 Intent changeInfor = new Intent(MainActivity.this,ChangeinformationActivity.class);
                                 Log.d("发送给修改资料的用户自己的", userSelfEmail);
                                 changeInfor.putExtra("userEmail",userSelfEmail );
                                 changeInfor.putExtra("userTouXiang",userLoginedImg);
                                 startActivity(changeInfor);
                                 break;
                             }

                         case R.id.nav_bill:
                             //我的账单
                             if (isLogined==NO_LOGINED){
                                 Toast.makeText(MainActivity.this, "请先登陆", Toast.LENGTH_SHORT).show();
                                 break;
                             }
                             if(isLogined==IS_LOGINED) {
                                 Intent mybill = new Intent(MainActivity.this,FinanceActivity.class);
                                 Log.d("发送给修改资料的用户自己的", userSelfEmail);
                                 mybill.putExtra("userEmail",userSelfEmail );
                                 startActivity(mybill);
                                 break;
                             }
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


    //读取的事件暂时存在一个temshijian里。
    private ShiJian setTempShiJian( String getBiaoTi , String getNeiRong,byte[] getPhoto,String getTime,String isfinish ){
        ShiJian shijian = new ShiJian( );
        shijian.setBiaoti(getBiaoTi);
        shijian.setNeirong(getNeiRong);
        shijian.setImgId(Integer.parseInt(isfinish));
        shijian.setTime(getTime);
        shijian.setPhoto(getPhoto);
        Log.d("存在事件里的图片字符串", shijian.getPhotoString());;
        return shijian;
    }


    //删除事件
    public void deleteShiJian(String time){
        Log.d("gettime是", time);
        String delete = "   {\n" +
                "\"place\":\""+time+"\"\n" +
                "}\n  ";
            httpClientUtils.sendPostByOkHttp(URL+"/shijian/delete",delete);

    }


    //从数据库得到事件列表
    public void setShiJianList() {
//        List<ShiJian> shijians = DataSupport.findAll(ShiJian.class);
//        for (ShiJian shijian : shijians) {
//            shiJianList.getValue().add(shijian);
        if(!userSelfEmail.equals("xxxx@qq.com")){
            String request= "{\n" +
                    "\"youxiang\":\""+userSelfEmail+"\"\n" +
                    "}" ;
            String response =    httpClientUtils.sendPostByOkHttp(URL+"/shijian/select",request);
            if(response!=null){
//                JSONObject jsonObject = new JSONObject();
                JSONArray jsonArray = JSON.parseArray(response);
//                Log.d("这是返回的事件集合", ""+jsonArray);
                for(int i =0;i<jsonArray.size();i++){
                    JSONObject obj = jsonArray.getJSONObject(i);
                    int getId = (Integer)obj.get("id");
                    String getBiaoTi = (String)obj.get("biaoti");
                    String getNeiRong = (String)obj.get("neirong");
//         byte[] getPhoto = (byte[])obj.get("phot");
                    byte[] getPhoto = new byte[1024];
                    getPhoto =((String)obj.get("photo")).getBytes();
//        getPhoto = Base64.getDecoder().decode((String)obj.get("photo"));
                    String getTime = (String)obj.get("place");
                    String getfinish = (String)obj.get("finished");
//        Log.d("这是返回的事件集合的", getTime);
//        Log.d("这是返回时的图片字符串", (String)obj.get("photo"));
                    Log.d("这是tostring的图片字符串", obj.get("photo").toString());
                    Log.d("这是原生转换的图片字符串", (String)obj.get("photo"));


//        Log.d("这是返回的事件集合的", getTime);
                    ShiJian temshijian = new ShiJian();
                    temshijian =setTempShiJian(getBiaoTi,getNeiRong,getPhoto,getTime,getfinish);
//                temshijian.save();
//        Log.d("这是存进去的时间，判断是否为空", temshijian.getTime());     不为空
                    shiJianList.add(temshijian);
                }

            }
        }

//        }
    }




    }




