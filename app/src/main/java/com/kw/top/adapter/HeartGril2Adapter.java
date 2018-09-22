package com.kw.top.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.kw.top.R;
import com.kw.top.bean.AllUserBean;
import com.kw.top.retrofit.HttpHost;
import com.kw.top.tools.GlideTools;

import java.util.ArrayList;
import java.util.List;

/**
 * author: zy
 * data  : 2018/6/9
 * des   :
 */

public class HeartGril2Adapter extends RecyclerView.Adapter<HeartGril2Adapter.ViewHolder> {

    private Context mContext;
    private List<AllUserBean> mList = new ArrayList<>();

    public HeartGril2Adapter(Context context,List<AllUserBean> beans){
        this.mContext = context;
        this.mList = beans;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_heart2,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AllUserBean allUserBean = mList.get(position);

        Glide.with(mContext)
                .load(HttpHost.qiNiu+ allUserBean.getHeadImg())
                .apply(GlideTools.getOptions())
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.iv_image);
        }
    }
}
