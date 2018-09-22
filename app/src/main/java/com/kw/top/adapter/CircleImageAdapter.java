package com.kw.top.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.kw.top.R;
import com.kw.top.bean.ImageBean;
import com.kw.top.retrofit.HttpHost;
import com.kw.top.tools.GlideTools;
import com.kw.top.ui.activity.ImageDetailsActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * author  ： zy
 * date    ： 2018/7/7
 * des     ：
 */

public class CircleImageAdapter  extends RecyclerView.Adapter<CircleImageAdapter.ViewHoder>{

    private Activity mContext;
    private List<ImageBean> mList = new ArrayList<>();

    public CircleImageAdapter(Activity context, List<ImageBean> list){
        this.mContext = context;
        this.mList = list;
    }

    @NonNull
    @Override
    public ViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_image,parent,false);
        return new ViewHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHoder holder, final int position) {
        Glide.with(mContext).load(HttpHost.qiNiu + mList.get(position).getDynamicPic()).apply(GlideTools.getOptions()).into(holder.mImageView);
        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageDetailsActivity.startActivity(mContext,position,mList);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHoder extends RecyclerView.ViewHolder{
        ImageView mImageView;
        public ViewHoder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.image);
        }
    }
}
