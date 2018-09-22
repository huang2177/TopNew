package com.kw.top.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kw.top.R;
import com.kw.top.bean.AllUserBean;
import com.kw.top.retrofit.HttpHost;
import com.kw.top.tools.GlideTools;

import java.util.ArrayList;
import java.util.List;

/**
 * author  ： zy
 * date    ： 2018/6/19
 * des     ：
 */

public class ClubGridViewAdapter extends BaseAdapter{

    private Context mContext;
    private List<AllUserBean> mList = new ArrayList<>();

    public ClubGridViewAdapter(Context context,List<AllUserBean> list){
        this.mContext = context;
        this.mList = list;
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
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder = null;
        if (null == view){
            holder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.item_club_gridview,parent,false);
            holder.ivHead = view.findViewById(R.id.ci_head);
            holder.tvNickName = view.findViewById(R.id.tv_nickname);
            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }
        AllUserBean allUserBean = mList.get(position);
        Glide.with(mContext).load(HttpHost.qiNiu + allUserBean.getHeadImg()).apply(GlideTools.getHeadOptions()).into(holder.ivHead);
        holder.tvNickName.setText(allUserBean.getNickName());
        return view;
    }

    class ViewHolder{
        ImageView ivHead;
        TextView tvNickName;
    }
}
