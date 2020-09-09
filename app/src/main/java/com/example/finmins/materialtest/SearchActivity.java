package com.example.finmins.materialtest;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.daimajia.swipe.util.Attributes;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    private  final String TAG = "SearchActivity.this"; 
    
    private SwipeRefreshLayout swipeRefreshLayout ;   //删除完成侧滑

    private List<ShiJian> shiJianList = new ArrayList<ShiJian>(); //事件列表


    private ShiJianAdapter shiJianAdapter;   //  事件适配器

    private RecyclerView recyclerView;   //RecycleView

    private String TAV="MainActivity.this";   //Log用

    private ImageView isFinishedImage;    //是否完成的图片

    private String search ;   //搜索内容

    private ImageButton searchButton ;   //搜索的图片

    private EditText searchEdit;       //搜索框 a

    //将查询到的数据读取到listView中，还未写
    private void initShijian(){
        List<ShiJian>  shijians = DataSupport.select("*").find(ShiJian.class);
        Log.d(TAG, search);
        for(ShiJian shijian:shijians){
           //对每个事件进行字符搜索
     if(checkString(shijian)==1)
            shiJianList.add(shijian);
        }
    }

   private int checkString(ShiJian shijian ){
       if(shijian.getBiaoti().indexOf(search)>-1)
           return 1;
       else   if(shijian.getNeirong().indexOf(search)>-1)
           return 1;
       else if(search.equals(String.valueOf(shijian.getYear())))
           return 1;
       else  if(search.equals(String.valueOf(shijian.getMonth())))
           return 1;
       else  if(search.equals(String.valueOf(shijian.getDay())))
           return 1;
       else return 0;
   }




    //adapter点击事件
    public void initEvents(){
        shiJianAdapter.setOnItemClickLitener(new ShiJianAdapter.OnItemClickLitener(){

            //根据ID进入事件具体内容
            @Override
            public void onItemClick(View view, int position, int id,ShiJian shiJian) {
                //创建对应的intent
                Intent intent = new Intent(SearchActivity.this,ChaKanActivity.class);
                intent.putExtra("ids",id);
                startActivity(intent);
            }


            //根据ID删除事件
            @Override
            public void onDeleteClick(int position,int id,String time) {
                final int position_item=position;   //获取listview中的位置
                final int id_item=id;    //事件的id

                //点击删除时的警告窗口
                AlertDialog.Builder dialog = new AlertDialog.Builder(SearchActivity.this);
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
                        Toast.makeText(SearchActivity.this,"事件删除成功",Toast.LENGTH_SHORT).show();
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
                TextView finish =(TextView)view.findViewById(R.id.swipe_finish);

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
        recyclerView=findViewById(R.id.recycler_viewInSearch);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        shiJianAdapter = new ShiJianAdapter(this, shiJianList);
        shiJianAdapter.setMode(Attributes.Mode.Single);
        recyclerView.setAdapter(shiJianAdapter);
        recyclerView.setSelected(true);
        Log.d(TAG, "点击了搜索");
        initShijian();
        shiJianAdapter.notifyDataSetChanged();
        initEvents();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchButton =findViewById(R.id.searchInSearch);
        searchEdit =findViewById(R.id.editTextInSearch) ;

        swipeRefreshLayout=findViewById(R.id.swipeRefreshLayoutInSearch);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
               // Refresh();
                swipeRefreshLayout.setRefreshing(false);
            }});
        //点击搜索图标
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               //将搜索关键词两边添加*号来确保搜索准确性。
                search =searchEdit.getText().toString();
                //初始化搜查的事件数据
              Refresh();


            }
        });

    }






}
