package com.kw.top.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.kw.top.R;
import com.kw.top.bean.TopCircleBean;
import com.kw.top.retrofit.HttpHost;
import com.kw.top.tools.GlideTools;
import com.kw.top.ui.activity.circle.WorldCircleDetailsActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * author: zy
 * data  : 2018/5/27
 * des   :
 */

public class WorldCircleAdapter extends RecyclerView.Adapter<WorldCircleAdapter.ViewHolder> {

    private Context mContext;
    private List<TopCircleBean> mList = new ArrayList<>();

    public WorldCircleAdapter(Context context, List<TopCircleBean> list) {
        this.mContext = context;
        this.mList = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_world_circle, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final TopCircleBean circleBean = mList.get(position);
        holder.mTvDate.setText(circleBean.getCreateTime());
        holder.mTvContent.setText(circleBean.getTextContent());
        holder.mTvLikeNum.setText(circleBean.getThumbsUpNum());
        holder.mTvDiamondNum.setText(circleBean.getThumbsNum());

        Glide.with(mContext)
                .load(HttpHost.qiNiu+circleBean.getDynamicPic())
                .apply(GlideTools.getOptions())
                .into(holder.mIvCircleIamge);
        holder.mRlItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WorldCircleDetailsActivity.startActivity(mContext,circleBean.getDynamicId());
            }
        });

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_date)
        TextView mTvDate;
        @BindView(R.id.iv_circle_iamge)
        ImageView mIvCircleIamge;
        @BindView(R.id.tv_like_num)
        TextView mTvLikeNum;
        @BindView(R.id.ll_like_num)
        LinearLayout mLlLikeNum;
        @BindView(R.id.tv_diamond_num)
        TextView mTvDiamondNum;
        @BindView(R.id.ll_diamond_num)
        LinearLayout mLlDiamondNum;
        @BindView(R.id.tv_content)
        TextView mTvContent;
        @BindView(R.id.rl_item)
        RelativeLayout mRlItem;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
