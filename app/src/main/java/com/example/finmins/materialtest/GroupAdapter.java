package com.example.finmins.materialtest;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;


import com.example.finmins.materialtest.Group;
import com.example.finmins.materialtest.MemberInGroup;
import com.example.finmins.materialtest.Model.GroupViewModel;
import com.example.finmins.materialtest.R;

import java.util.List;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.ViewHolder>
{
    private List<Group> mGroup;
    private Context context;
    private GroupViewModel groupViewModel;

    public  GroupAdapter(Context context, List<Group> group, GroupViewModel groupViewModel1){
        this.context=context;
        this.mGroup=group ;
        this.groupViewModel = groupViewModel1;
    }
    static class  ViewHolder extends RecyclerView.ViewHolder{
        ImageView groupImg ;   //群头像
        TextView  groupName ;    //群名字
        View  group ;     //群界面
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            groupImg  = itemView.findViewById(R.id.groupImg);
            groupName = itemView.findViewById(R.id.groupName);
            group = itemView;
        }
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_group,parent,false);
        final ViewHolder holder = new ViewHolder(view);


        //群组item点击功能
        holder.group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Group group = mGroup.get(position+1);
                Bundle bundle = new Bundle();
                bundle.putString("groupName",group.getGroupName());
                bundle.putString("VIPemail",group.getGroupVipEmail());
                NavController Controller = Navigation.findNavController(v);
                Controller.navigate(R.id.action_group_host_to_group_inner,bundle);
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Group group  = mGroup.get(position);
        holder.groupImg.setImageResource(group.getGroupImgId());    //设置群的头像
        holder.groupName.setText(group.getGroupName());    //设置群的名字

    }

    @Override
    public int getItemCount() {
        return mGroup.size();
    }
}
