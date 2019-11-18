package com.example.finmins.materialtest;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by FinMins on 2019/11/3.
 */

public class ShiJianAdapter extends ArrayAdapter<ShiJian> {
    private int  resourceId;
    public ShiJianAdapter(Context context, int textViewResOurceId, List<ShiJian> objects)
    {
        super(context ,textViewResOurceId,objects);
        resourceId = textViewResOurceId;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ShiJian shijian = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);

        ImageView imageView = (ImageView)view.findViewById(R.id.image);

        TextView biaoti = (TextView)view.findViewById(R.id.shijianName);

        TextView year = (TextView)view.findViewById(R.id.year);

        TextView month = (TextView)view.findViewById(R.id.mouth);

        TextView day = (TextView)view.findViewById(R.id.day);

        TextView time = (TextView)view.findViewById(R.id.end_time);

        biaoti.setText(shijian.getBiaoti());
        year.setText(String.valueOf(shijian.getYear()) );
        month.setText(String.valueOf(shijian.getMonth()));
        day.setText(String.valueOf(shijian.getDay()));
        time.setText(shijian.getTime());

        return view;
    }


}
