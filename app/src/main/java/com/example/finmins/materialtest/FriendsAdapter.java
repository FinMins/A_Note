package com.example.finmins.materialtest;



import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finmins.materialtest.Model.FriendViewModel;

import java.util.List;


/**
 * Created by FinMins on 2020/4/13.
 */

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.FriendsHolder>{
    private  List<Friend> allFriends ;
    private   Context context;
    private FriendViewModel friendViewModel ;
    private HttpClientUtils httpClientUtils = new HttpClientUtils();
   // private List<ShiJian> shiJianList; //接收的事件

    public  FriendsAdapter(Context context, List<Friend> friends){
        this.context=context;
        this.allFriends = friends;
//       this. friendViewModel = viewmodel ;
    }

    @NonNull
    @Override
    public FriendsHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View item_friend = layoutInflater.inflate(R.layout.item_friend,parent,false);
        final  FriendsHolder holder = new FriendsHolder(item_friend);

//               //点击删除的逻辑
//        holder.delete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int position = holder.getAdapterPosition();
//                final Friend  friend = allFriends.get(position+1);
//                AlertDialog.Builder dialog= new AlertDialog.Builder(parent.getContext());
//                dialog.setTitle("通知");
//                dialog.setMessage("是否删除好友？");
//                dialog.setCancelable(false);
//                //点击是清楚图片bitmap
//                dialog.setPositiveButton("是",new DialogInterface.OnClickListener(){
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        //删除好友逻辑
//                String request = "{\n" +
//                        "\"friendyouxiang\":\"mmj@qq.com\"\n" +
//                        "}";
//                        httpClientUtils.sendPostByOkHttp("http://192.168.43.61:9999/friend/delete",request);
//
//                        Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
//
//
//                    }
//                });
//                //点击否不做任何修改
//                dialog.setNegativeButton("否",new DialogInterface.OnClickListener(){
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                    }
//                });
//                dialog.show();
//            }
//        });

//        被放弃使用
//        //点击接收消息的逻辑
//        holder.messages.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int position = holder.getAdapterPosition();
//              Friend  friend = allFriends.get(position+1);
//                Toast.makeText(parent.getContext(),"消息",Toast.LENGTH_SHORT).show();
//
//            }
//        });

   //朋友item点击功能
//        holder.friendview.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int position = holder.getAdapterPosition();
//                Friend  friend = allFriends.get(position+1);
//                Bundle bundle = new Bundle();
//                bundle.putInt("shareId",friend.getId());
//                NavController Controller = Navigation.findNavController(v);
//                Controller.navigate(R.id.action_friendnav_to_friendShareFragment);
//
//            }
//        });


        /*
   //朋友item长按功能
       holder.friendview.setOnLongClickListener(new View.OnLongClickListener() {
           @Override
           public boolean onLongClick(View v) {
               AlertDialog.Builder dialog= new AlertDialog.Builder(parent.getContext());
               dialog.setTitle("通知");
               dialog.setMessage("是否删除好友？");
               dialog.setCancelable(false);
               //点击是清楚图片bitmap
               dialog.setPositiveButton("是",new DialogInterface.OnClickListener(){
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                     //删除好友逻辑
                   }
               });
               //点击否不做任何修改
               dialog.setNegativeButton("否",new DialogInterface.OnClickListener(){
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                   }
               });
               dialog.show();
               return true;
           }
       });*/

        return new FriendsHolder(item_friend);
     //   return null;
    }



    @Override //逻辑关联
    public void onBindViewHolder(@NonNull final FriendsHolder holder, final int position) {
      final   Friend friend = allFriends.get(position);
        holder.mingzi .setText( friend.getMingzi());
        holder.youxiang.setText(friend.getyouxiang());
//        holder.messages.setImageResource(friend.getMessages());
        final FriendsHolder itemViewHold =  holder;
        holder.touxiang.setImageResource(friend.getTouxiang());
        if (mOnItemClickLitener != null) {
            itemViewHold.itemview .setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = itemViewHold.getLayoutPosition();
                    mOnItemClickLitener.onItemClick(position,friend.getyouxiang(),v);
                }
            });

            itemViewHold.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickLitener.onDeleteClick(position,friend.getyouxiang());
                    Log.d("这是点击删除的好友的邮箱", friend.getyouxiang());
                }
            });
            //
        }
    }

    @Override
    public int getItemCount() {
        if (allFriends==null)
            return  0;
        return allFriends.size();
    }

    public interface OnItemClickLitener
    {
        void onItemClick( int position,String youxiang,View v);

        void onDeleteClick(int position,String youxiang);
    }

    private FriendsAdapter.OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(FriendsAdapter.OnItemClickLitener mOnItemClickLitener)
    {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    static class FriendsHolder extends RecyclerView.ViewHolder{
        View friendview;
        ImageView touxiang;
        TextView mingzi;
        TextView youxiang ;
        ImageView delete;
        FriendViewModel friendViewModel;
        ImageView messages;
        View itemview;
        public FriendsHolder(@NonNull View itemView){
            super(itemView);
            friendview = itemView;
         touxiang = itemView.findViewById(R.id.touxiang_friend);
         mingzi = itemView.findViewById(R.id.xingming_friend);
            youxiang = itemView.findViewById(R.id.youxiang_friend);
            delete = itemView.findViewById(R.id.delete_friend);
            itemview = itemView;
//            messages = itemView.findViewById(R.id.message_friend);
        }

    }






}