package com.kw.top.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.kw.top.R;
import com.kw.top.bean.DiamondBean;

import java.util.ArrayList;
import java.util.List;

/**
 * author: zy
 * data  : 2018/6/7
 * des   : 钻石Adapter
 */

public class DiamonAdapter extends BaseAdapter{

    private Context mContext;
    private List<DiamondBean.ProductlListBean> mList = new ArrayList<>();
    private int type; //0 充值 1兑换

    public DiamonAdapter(Context context,List<DiamondBean.ProductlListBean> list,int type){
        this.mContext = context;
        this.mList = list;
        this.type = type;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        ViewHolder holder =null;
        if (holder == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_diamon,parent,false);
            holder.tvDiamonNum = convertView.findViewById(R.id.tv_num_diamon);
            holder.tvGiftNum = convertView.findViewById(R.id.tv_gift_num_diamon);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        DiamondBean.ProductlListBean productlListBean = mList.get(i);
        holder.tvDiamonNum.setText(productlListBean.getChangeAmount());
        if (type == 0){
            holder.tvGiftNum.setText("¥"+productlListBean.getAmount());
        }else {
            holder.tvGiftNum.setText(productlListBean.getAmount()+"礼券");
        }

        return convertView;
    }

    class ViewHolder{
        TextView tvDiamonNum,tvGiftNum;
    }
}
