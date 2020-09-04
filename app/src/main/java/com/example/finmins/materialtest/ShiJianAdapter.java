package com.example.finmins.materialtest;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;

import java.util.List;

/**
 * Created by FinMins on 2019/11/3.
 */

public class ShiJianAdapter extends RecyclerSwipeAdapter<ShiJianAdapter. ViewHolder> {
    private List<ShiJian> mShiJianList;
    private Context myContext;

    public ShiJianAdapter(Context context,List<ShiJian> shijianlist) {
        mShiJianList = shijianlist;
        myContext = context;
    }

    @Override
    public int getItemCount() {
        return mShiJianList.size();
    }


    @Override
    public  ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(myContext).inflate(R.layout.list_main, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;




    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipeLayout;  //实现只展现一条列表项的侧滑区域
    }


    static class  ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView year;
        TextView month;
        TextView day;
        TextView time;
        TextView biaoti;
         SwipeLayout swipeLayout;
        TextView delete;
        LinearLayout deleteLayout;
        LinearLayout listItemLayout;
        TextView finish;
        public  ViewHolder(View view) {
            super(view);

            listItemLayout = (LinearLayout)view.findViewById(R.id.surfaceView);

          //  shijianView = view;
            imageView = (ImageView) view.findViewById(R.id.image);
            year = (TextView) view.findViewById(R.id.year);
            month = (TextView) view.findViewById(R.id.month);
            day = (TextView) view.findViewById(R.id.day);
            time = (TextView) view.findViewById(R.id.end_time);
            biaoti = (TextView) view.findViewById(R.id.shijianName);

            swipeLayout = (SwipeLayout)view.findViewById(R.id.swipeLayout);
             finish = (TextView)view.findViewById(R.id.swipe_finish);
            delete = (TextView)view.findViewById(R.id.swipe_delete);

        }
    }


    public void onBindViewHolder(ViewHolder viewHolder,  int position) {
          if(viewHolder instanceof  ViewHolder) {
              final ShiJian shijian = mShiJianList.get(position);
              final int id =shijian.getId();
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
                          mOnItemClickLitener.onSetImgId( position,id,shijian);

                      }
                  });
              }
              mItemManger.bindView(viewHolder.itemView, position);//实现只展现一条列表项的侧滑区域
             if(shijian.get_imgId()==0) {
                 itemViewHold.imageView.setImageResource(R.mipmap.yuanquan);
                 itemViewHold.finish.setText("完成");
             }else{
                 itemViewHold.imageView.setImageResource(R.mipmap.zhengque);
                 itemViewHold. finish.setText("未完成");
             }
              itemViewHold.biaoti.setText(shijian.getBiaoti());
//              itemViewHold.year.setText(String.valueOf(shijian.getYear()) + "/");
//              itemViewHold.month.setText(String.valueOf(shijian.getMonth()) + "/");
//              itemViewHold.day.setText(String.valueOf(shijian.getDay()) + "/");
              itemViewHold.time.setText(shijian.getTime());
          }

    }



    public interface OnItemClickLitener
    {
        void onItemClick(View view, int position,int id);
       void onSetImgId(int position,int id,ShiJian shijian);
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



