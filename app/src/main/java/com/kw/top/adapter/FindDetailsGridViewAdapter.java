package com.kw.top.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.kw.top.R;
import com.kw.top.bean.TopCircleBean;
import com.kw.top.retrofit.HttpHost;
import com.kw.top.tools.GlideTools;

import java.util.List;

/**
 * author: zy
 * data  : 2018/6/10
 * des   :
 */

public class FindDetailsGridViewAdapter extends BaseAdapter {

    private Context mContext;
    private List<TopCircleBean> mList;

    public FindDetailsGridViewAdapter(Context context,List<TopCircleBean> list){
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_find_details,parent,false);
            holder.image = convertView.findViewById(R.id.iv_image);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        Glide.with(mContext)
                .load(HttpHost.qiNiu+mList.get(position).getDynamicPic())
                .apply(GlideTools.getOptions())
                .into(holder.image);
        return convertView;
    }

    class ViewHolder {
        ImageView image;
    }
}
