package com.kw.top.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.kw.top.R;
import com.kw.top.bean.GiftBean;
import com.kw.top.retrofit.HttpHost;
import com.kw.top.tools.GlideTools;

import java.util.ArrayList;
import java.util.List;

/**
 * author: zy
 * data  : 2018/6/10
 * des   :
 */

public class GiftAdapter extends BaseAdapter{

    private Context mContext;
    private List<GiftBean> mList = new ArrayList<>();

    public GiftAdapter(Context context){
        this.mContext = context;
//        this.mList = list;
    }

    public void setList(List<GiftBean> list) {
        mList = list;
        notifyDataSetChanged();
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
        ViewHolder holder =null;
        if (holder == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_gift,parent,false);
            holder.ivGift = convertView.findViewById(R.id.iv_gift);
            holder.tvDiamon = convertView.findViewById(R.id.tv_diamond);
            holder.ivDiamond = convertView.findViewById(R.id.iv_diamond);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        GiftBean giftBean = mList.get(position);
        holder.tvDiamon.setText(giftBean.getGiftAmount());
        Glide.with(mContext)
                .load(HttpHost.qiNiu+giftBean.getGiftPicture())
                .apply(GlideTools.getOptions())
                .into(holder.ivGift);
        if (giftBean.getAmountType().equals("1")){
            holder.ivDiamond.setVisibility(View.VISIBLE);
            holder.tvDiamon.setText(giftBean.getGiftAmount());
        }else {
            holder.ivDiamond.setVisibility(View.GONE);
            holder.tvDiamon.setText(giftBean.getGiftAmount()+"礼券");
        }
        return convertView;
    }

    class ViewHolder{
        ImageView ivGift,ivDiamond;
        TextView tvDiamon;
    }

}
