package com.kw.top.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kw.top.R;

/**
 * author  ： zy
 * date    ： 2018/6/13
 * des     ：
 */

public class SendTaskAdapter  extends BaseAdapter {

    private Context mContext;

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_send_task,parent,false);
            holder.tvAmount = convertView.findViewById(R.id.tv_amount);
            holder.tvNum = convertView.findViewById(R.id.tv_num);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        return convertView;
    }

    class ViewHolder{
        TextView tvAmount,tvNum;
    }
}
