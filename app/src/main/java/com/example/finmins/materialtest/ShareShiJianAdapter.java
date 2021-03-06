package com.example.finmins.materialtest;



import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finmins.materialtest.Model.FriendViewModel;

import java.util.List;


/**
 * Created by FinMins on 2020/4/13.
 */

public class ShareShiJianAdapter extends RecyclerView.Adapter<ShareShiJianAdapter.ShareShiJianHolder>{
    private  List<ShiJian> allShiJian ;
    private   Context context;
    private FriendViewModel friendViewModel;
    private String userEmail ;
    private String friendEmail;
    private final  String URL= "http://192.168.43.61:9999";
    private HttpClientUtils httpClientUtils = new HttpClientUtils();
    public  ShareShiJianAdapter(Context context, List<ShiJian> shiJians, FriendViewModel friendViewModel1,String useremail,String friendEmail){
        this.context=context;
        this.allShiJian = shiJians;
        this.friendViewModel = friendViewModel1;
        this.userEmail = useremail;
        this.friendEmail = friendEmail ;
    }

    @NonNull
    @Override
    public ShareShiJianHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View item_shareShiJian = layoutInflater.inflate(R.layout.item_shareshijian,parent,false);
        final  ShareShiJianHolder holder = new ShareShiJianHolder(item_shareShiJian);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                final ShiJian shijian  = allShiJian.get(position+1);
                AlertDialog.Builder dialog= new AlertDialog.Builder(parent.getContext());
                dialog.setTitle("通知");
                dialog.setMessage("确定分享这个消息？");
                dialog.setCancelable(false);
                //点击是分享
                dialog.setPositiveButton("是",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //分享逻辑
                        String request = " {\n" +
                                "\"youxiang\":\""+ friendEmail+"\",\n" +
                                "\"biaoti\":\""+"来自"+ userEmail+":"+shijian.getBiaoti()+"\",\n" +    //这里可能出错
                                "\"neirong\":\""+shijian.getNeirong()+"\",\n" +
                                "\"place\":\""+shijian.getTime()+"\",\n" +
                                "\"photo\":\""+shijian.getPhoto()+"\",\n" +
                                "\"finished\":\"0\"\n" +
                                "}";

                        httpClientUtils.sendPostByOkHttp(URL+"/shijian/insert",request);
                        Toast.makeText(context, "分享成功！", Toast.LENGTH_SHORT).show();
                    }
                });
                //点击否不做任何修改
                dialog.setNegativeButton("否",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                dialog.show();
            }
        });



        return new ShareShiJianHolder(item_shareShiJian);

    }

    @Override //逻辑关联
    public void onBindViewHolder(@NonNull ShareShiJianHolder holder, final int position) {
       ShiJian shijian = allShiJian.get(position);
       holder.shareShiJianTime.setText(shijian.getTime());
       holder.shareShiJianBiaoti.setText(shijian.getBiaoti());
    }

    @Override
    public int getItemCount() {
        return allShiJian.size();
    }


    static class ShareShiJianHolder extends RecyclerView.ViewHolder{
      TextView shareShiJianBiaoti;
      TextView shareShiJianTime;
        public ShareShiJianHolder(@NonNull View itemView){
            super(itemView);
      shareShiJianBiaoti = itemView.findViewById(R.id.shareShiJianBiaoti);

      shareShiJianTime = itemView.findViewById(R.id.shareShiJianTime);

        }

    }






}