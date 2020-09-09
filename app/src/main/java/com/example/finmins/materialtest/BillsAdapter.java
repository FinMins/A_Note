package com.example.finmins.materialtest;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.finmins.materialtest.Model.HistoricalViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static org.litepal.LitePalApplication.getContext;

public class BillsAdapter extends RecyclerView.Adapter<BillsAdapter.BillsHolder> {
    private List<Bills> bills = new ArrayList<Bills>();
    final private int flag;
    private Context context;
    private HistoricalViewModel historicalViewModel;
    private String searchStr;
    public BillsAdapter(Context context,List<Bills> bills,HistoricalViewModel historicalViewModel,int flag,String searchStr){
        this.context =context;
        this.bills = bills;
        this.historicalViewModel = historicalViewModel;
        this.flag = flag;
        this.searchStr = searchStr;
    }
    static class BillsHolder extends RecyclerView.ViewHolder {
        TextView date;
        TextView type;
        TextView amount;
        public BillsHolder(@NonNull View itemView){
            super(itemView);
            this.date = itemView.findViewById(R.id.bill_date);
            this.type = itemView.findViewById(R.id.bill_type);
            this.amount = itemView.findViewById(R.id.bill_amount);
        }
    }
    @NonNull
    @Override
    public BillsHolder onCreateViewHolder(@NonNull final ViewGroup parent,int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        final View item_Bills = layoutInflater.inflate(R.layout.item_bills, parent, false);
//        final BillsHolder holder = new BillsHolder(item_Bills);
//        return holder;
        return new BillsHolder(item_Bills);
    }
    /** 绑定holder*/
    @Override
    public void onBindViewHolder(@NonNull final BillsHolder holder, final int position) {
        final Bills bill = bills.get(position);
        holder.date.setText(bill.getDate());
        holder.type.setText(bill.getType());
        holder.amount.setText(String.valueOf(bill.getAmount()));
        final PopupWindow mPopWindow = new PopupWindow(holder.itemView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.d(TAG, "onClick: items-->");
                View mPopView = LayoutInflater.from(context).inflate(R.layout.popup, null);
                final PopupWindow mPopWindow = new PopupWindow(mPopView, ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT, true);
                mPopWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                /** 获取弹窗的长宽度 */
                holder.itemView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
                int popupWidth = holder.itemView.getMeasuredWidth();
                int popupHeight = holder.itemView.getMeasuredHeight();
                /** 获取父控件的位置 */
                int[] location = new int[2];
                v.getLocationOnScreen(location);
                /** 显示位置 */
                mPopWindow.showAtLocation(v, Gravity.NO_GRAVITY, (location[0] + v.getWidth() / 2) - popupWidth / 2, location[1] - popupHeight);
                mPopWindow.update();
                /** 长按弹出菜单选项（复制、删除）*/
                mPopView.findViewById(R.id.delete_bill).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bills.clear();
                        HttpClientUtils client = new HttpClientUtils();
                        String deleteBody =  " {\n" +
                                "    \"timeID\":"+"\""+bill.getLocalTime()+"\""+"\n" +
                                "} ";
                        client.sendPostByOkHttp("http://192.168.43.61:9999/finance/delete",deleteBody);
                        String updateBody = "   {\n" +
                                "    \"youxiang\":\"123456789@qq.com\"\n" +
                                "} ";
                        String response = client.sendPostByOkHttp("http://192.168.43.61:9999/finance/selectlist",updateBody);
                        JSONArray jsonArray = JSON.parseArray(response);
                        for (int i = 0; i < jsonArray.size(); i++) {       //这个是Json数组
                            Log.d("这是jsonarray的大小", String.valueOf(jsonArray.size()));
                            JSONObject obj = jsonArray.getJSONObject(i);
//                          String email = (String) obj.get("youxiang");  //这个是Json数组的每一个json对象里的键值对。
                            String currentdate = (String) obj.get("currentdate");
                            Log.d(TAG, "currentdate:"+currentdate);
                            String type = (String) obj.get("type1");
                            Log.d(TAG, "type: "+type);
                            String consumptionamount = (String) obj.get("consumptionamount");
                            Log.d(TAG, "consumptionamount:"+consumptionamount);
                            String localTime = (String) obj.get("timeID");
                            Log.d(TAG, "Double.parseDouble(consumptionamount):"+Double.parseDouble(consumptionamount));

                            if(flag == 0){
                                Bills bill = new Bills(currentdate,type,Double.parseDouble(consumptionamount),localTime);
                                if (bill.getDate().contains(searchStr)
                                        ||bill.getType().contains(searchStr)
                                        ||String.valueOf(bill.getAmount()).contains(searchStr)){

                                    bills.add(bill);
                                }

                            }
                            if(flag == 1){
                                Bills bill = new Bills(currentdate,type,Double.parseDouble(consumptionamount),localTime);
                                bills.add(bill);
                            }

                            Log.d("这是host内容", String.valueOf(bills.size()));
                        }
                        Toast.makeText(getContext(), "请求成功", Toast.LENGTH_SHORT).show();
//                        bills.remove(position);
//                        notifyItemRemoved(position);
                        notifyDataSetChanged();
                        Log.d(TAG, "onClick: items-->"+holder.getItemId());
                        if (mPopWindow != null) {
                            mPopWindow.dismiss();
                        }
                    }
                });
                return false;
            }
        });
    }
    /** 返回item的数量 */
    @Override
    public int getItemCount() {
        return bills.size();
    }
}
