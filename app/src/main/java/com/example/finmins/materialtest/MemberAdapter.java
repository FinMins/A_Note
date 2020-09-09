package com.example.finmins.materialtest;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;

import java.util.List;

/**
 * Created by FinMins on 2019/11/3.
 */

public class MemberAdapter extends RecyclerSwipeAdapter<MemberAdapter. ViewHolder> {
    public List<MemberInGroup> mMember;
    public Context myContext;
//    public GroupViewModel groupViewModel;
    public String  vipEmail;
    public GroupInnerFragment groupInnerFragment;     //当前碎片

    public MemberAdapter(Context context, List<MemberInGroup> memberlist, String VIPemail,GroupInnerFragment groupInnerFragment1) {
        mMember = memberlist ;
        myContext = context;
        groupInnerFragment = groupInnerFragment1;
//        this.groupViewModel = groupViewModel;
        this.vipEmail =VIPemail;
    }

    @Override
    public int getItemCount() {
        return mMember.size();
    }


    @Override
    public  ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(myContext).inflate(R.layout.item_member, parent, false);    //这里要改。
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.memberSwipeLayout;  //实现只展现一条列表项的侧滑区域
    }

  //这里数据要改
    static class  ViewHolder extends RecyclerView.ViewHolder {
        ImageView memberImg;   //用户头像
        ImageView isFinished ;  //是否完成
        TextView memberName;   //用户名字
        SwipeLayout swipeLayout;
        TextView delete;    //管理员删除用户
        TextView finishedTime ; //点击完成的日
       // LinearLayout listItemLayout;    不知道干嘛用的
        TextView finish;    //是否完成按钮

        public  ViewHolder(View view) {
            super(view);
           // listItemLayout = (LinearLayout)view.findViewById(R.id.surfaceView);
            //  shijianView = view;
            memberImg =  view.findViewById(R.id.memberImg);
            finishedTime = view.findViewById(R.id.finishedTime);
            memberName=  view.findViewById(R.id.memberName);
            swipeLayout = view.findViewById(R.id.memberSwipeLayout);
            finish = view.findViewById(R.id.swipeFinishInGroup);
            delete = view.findViewById(R.id.swipeDeleteInGroup);
            isFinished = view.findViewById(R.id.memberIsFinished);
        }
    }


    public void onBindViewHolder(ViewHolder viewHolder,  int position) {
        if(viewHolder instanceof  ViewHolder) {
            final MemberInGroup member = mMember.get(position);
//            final int id =member.getId();    //得到ID
            final  String selfEmail = member.getMannerEmail();
            final String thisGroupName = member.getGroupName();
            final ViewHolder itemViewHold = ((ViewHolder) viewHolder);
            itemViewHold.swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
            if (mOnItemClickLitener != null) {



                itemViewHold.swipeLayout.getSurfaceView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        itemViewHold.swipeLayout.close();//隐藏侧滑菜单区域
                        int position = itemViewHold.getLayoutPosition();
                    }
                });
                //删除接口
                itemViewHold.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        itemViewHold.swipeLayout.close();
                        int position = itemViewHold.getLayoutPosition();
                        mOnItemClickLitener.onDeleteClick(position,selfEmail,thisGroupName);
                    }
                });
                    //完成接口
                itemViewHold.finish.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        itemViewHold.swipeLayout.close();
                        int position = itemViewHold.getLayoutPosition();
                        mOnItemClickLitener.onSetImgId( position,selfEmail,thisGroupName,member.getFinishedDate());
                    }
                });

                //删除按钮
                itemViewHold.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        itemViewHold.swipeLayout.close();
                        int position = itemViewHold.getLayoutPosition();
                        mOnItemClickLitener.onDeleteClick(position,selfEmail,thisGroupName);
                    }
                });

            }
            mItemManger.bindView(viewHolder.itemView, position);//实现只展现一条列表项的侧滑区域

//
//
//            if(member.getMannerIsFinished()==0) {
//                itemViewHold.finish.setText("完成");
//            }else{
//                itemViewHold. finish.setText("未完成");
//            }



//            itemViewHold. finish.setText("打卡");
            itemViewHold.isFinished.setImageResource(member.getMannerfinishedImg());
            itemViewHold.memberName.setText(member.getMannerName());
            itemViewHold. memberImg.setImageResource(member.getMannerImgId());   //用户头像
            itemViewHold.finishedTime.setText(member.getFinishedDate()); ; //点击完成的日

        }

    }








    public interface OnItemClickLitener
    {

        void onSetImgId(int position,String selfEmail,String groupName,String finishDate);
        /**置顶*/
        //  void onTopClick(int position);
        /**删除*/
        void onDeleteClick(int position,String selfEmail,String groupName);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener)
    {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }
}



