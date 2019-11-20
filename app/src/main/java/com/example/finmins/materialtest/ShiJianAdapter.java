package com.example.finmins.materialtest;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.swipe.adapters.ArraySwipeAdapter;

import java.util.List;

/**
 * Created by FinMins on 2019/11/3.
 */

public class ShiJianAdapter extends ArrayAdapter<ShiJian> {

    class ViewHolder{
      ImageView imageView;
        TextView year;
        TextView month;
        TextView day;
        TextView time;
        TextView biaoti;
    }


    private int  resourceId;
    private TextView swipe;

    public ShiJianAdapter(Context context, int textViewResOurceId, List<ShiJian> objects)
    {
        super(context ,textViewResOurceId,objects);
        resourceId = textViewResOurceId;
    }


    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe_delete;
    }



    public View getView(int position, View convertView, ViewGroup parent) {
        ShiJian shijian =getItem(position) ;
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
         viewHolder =new ViewHolder();
            viewHolder.imageView = (ImageView) view.findViewById(R.id.image);

           viewHolder. biaoti = (TextView) view.findViewById(R.id.shijianName);

           viewHolder.year = (TextView) view.findViewById(R.id.year);

            viewHolder.month = (TextView) view.findViewById(R.id.mouth);

            viewHolder.day = (TextView) view.findViewById(R.id.day);

            viewHolder.time = (TextView) view.findViewById(R.id.end_time);
            view.setTag(viewHolder);

        }else{
            view = convertView;
            viewHolder = (ViewHolder)view.getTag(); //重新获取viewHolder
        }
      viewHolder.biaoti.setText(shijian.getBiaoti());
        viewHolder.  year.setText(String.valueOf(shijian.getYear()) + "/");
        viewHolder. month.setText(String.valueOf(shijian.getMonth()) + "/");
        viewHolder. day.setText(String.valueOf(shijian.getDay()) + "/");
        viewHolder. time.setText(shijian.getTime());

        return view;}}

