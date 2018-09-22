package com.kw.top.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kw.top.R;
import com.kw.top.bean.TopListBean;
import com.kw.top.retrofit.HttpHost;
import com.kw.top.tools.GlideTools;
import com.kw.top.ui.activity.find.FindDetailsActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * author: zy
 * data  : 2018/6/9
 * des   :
 */

public class TopListAdapter extends RecyclerView.Adapter<TopListAdapter.ViewHolder> {
    private Context mContext;
    private List<TopListBean> mList = new ArrayList<>();
    private String type;

    public TopListAdapter(Context context, List<TopListBean> listBeans,String type) {
        mContext = context;
        mList = listBeans;
        this.type = type;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_top_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final TopListBean topCircleBean = mList.get(position);
        holder.mTvName.setText(topCircleBean.getNickName());
        if (type.equals("01")){
            //魅力
            holder.mTvGift.setText(topCircleBean.getGlamour());
            holder.mIvGift.setImageResource(R.mipmap.icon_gift);
        }else {
            //实力
            holder.mTvGift.setText(topCircleBean.getAmount());
            holder.mIvGift.setImageResource(R.mipmap.icon_diamond_small);
        }
        Glide.with(mContext)
                .load(HttpHost.qiNiu + topCircleBean.getHeadImg())
                .apply(GlideTools.getOptions())
                .into(holder.mIvImage);
        holder.mIvImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FindDetailsActivity.startActivity(mContext,topCircleBean.getUserId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder{
        TextView mTvName;
        TextView mTvGift;
        ImageView mIvImage;
        ImageView mIvGift;
        public ViewHolder(View itemView) {
            super(itemView);
            mTvName = itemView.findViewById(R.id.tv_name);
            mTvGift = itemView.findViewById(R.id.tv_gift);
            mIvImage = itemView.findViewById(R.id.iv_image);
            mIvGift = itemView.findViewById(R.id.iv_gift);
        }
    }
}
