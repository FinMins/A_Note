package com.example.finmins.materialtest;
import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.swipe.SwipeLayout;

import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;

import org.w3c.dom.Text;

import java.lang.reflect.Member;
import java.util.List;

/**
 * Created by FinMins on 2019/11/3.
 */

public class MemberAdapter extends RecyclerSwipeAdapter<MemberAdapter. ViewHolder> {
    private List<MemberInGroup> mMember;
    private Context myContext;

    public MemberAdapter(Context context, List<MemberInGroup> memberlist) {
        mMember = memberlist ;
        myContext = context;
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
        TextView finishedYear ;  //点击完成的年份
        TextView finishedMonth ; //点击完成的月份
        TextView finishedDay ; //点击完成的日
       // LinearLayout listItemLayout;    不知道干嘛用的
        TextView finish;    //是否完成按钮
        public  ViewHolder(View view) {
            super(view);

           // listItemLayout = (LinearLayout)view.findViewById(R.id.surfaceView);

            //  shijianView = view;
            memberImg =  view.findViewById(R.id.memberImg);
            finishedYear =  view.findViewById(R.id.finishedYear);
            finishedMonth =  view.findViewById(R.id.finishedMonth);
            finishedDay = view.findViewById(R.id.finishedDay);
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
            final int id =member.getId();    //得到ID
            final int isVip = member.getMannerIsVip();  //查看是否为VIP
            final ViewHolder itemViewHold = ((ViewHolder) viewHolder);
            itemViewHold.swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
            if (mOnItemClickLitener != null) {
                itemViewHold.swipeLayout.getSurfaceView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        itemViewHold.swipeLayout.close();//隐藏侧滑菜单区域
                        int position = itemViewHold.getLayoutPosition();
                        mOnItemClickLitener.onItemClick(itemViewHold.swipeLayout, position,id);
                    }
                });

                itemViewHold.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        itemViewHold.swipeLayout.close();
                        int position = itemViewHold.getLayoutPosition();
                        mOnItemClickLitener.onDeleteClick(position,id);
                    }
                });
                //
                itemViewHold.finish.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        itemViewHold.swipeLayout.close();
                        int position = itemViewHold.getLayoutPosition();
                        mOnItemClickLitener.onSetImgId( position,id,member);

                    }
                });
            }
            mItemManger.bindView(viewHolder.itemView, position);//实现只展现一条列表项的侧滑区域
            if(member.getMannerIsFinished()==0) {
                itemViewHold.isFinished.setImageResource(R.mipmap.yuanquan);
                itemViewHold.finish.setText("完成");
            }else{
                itemViewHold.isFinished.setImageResource(R.mipmap.zhengque);
                itemViewHold. finish.setText("未完成");
            }
            itemViewHold.memberName.setText(member.getMannerName());
            itemViewHold.finishedYear.setText(String.valueOf(member.getFinishedYear()) + "/");
            itemViewHold.finishedMonth.setText(String.valueOf(member.getFinishedMonth()) + "/");
            itemViewHold.finishedDay.setText(String.valueOf(member.getFinishedDay()) + "/");
        }

    }



    public interface OnItemClickLitener
    {
        void onItemClick(View view, int position,int id);
        void onSetImgId(int position,int id,MemberInGroup member);
        /**置顶*/
        //  void onTopClick(int position);
        /**删除*/
        void onDeleteClick(int position,int id);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener)
    {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }
}



