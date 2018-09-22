package com.kw.top.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.kw.top.R;
import com.kw.top.bean.ImageBean;
import com.kw.top.bean.TaskDetailsBean;
import com.kw.top.listener.OnClickListener;
import com.kw.top.retrofit.HttpHost;
import com.kw.top.tools.GlideTools;
import com.kw.top.ui.activity.ImageDetailsActivity;
import com.kw.top.ui.activity.VideoPlayActivity;
import com.kw.top.utils.DisplayUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * author: zy
 * data  : 2018/5/27
 * des   :
 */

public class TaskDetailsAdapter extends RecyclerView.Adapter<TaskDetailsAdapter.ViewHolder> {

    private Activity mContext;
    private List<TaskDetailsBean> mList = new ArrayList<>();
    private int mMaxWidth,maxHeight;
    private int marginTop;
    private OnClickListener mOnClickListener;
    private List<ImageBean> images = new ArrayList<>();

    public TaskDetailsAdapter(Activity context, List<TaskDetailsBean> list,OnClickListener onClickListener) {
        this.mContext = context;
        this.mList = list;
        this.mOnClickListener = onClickListener;
        mMaxWidth = DisplayUtils.dip2px(mContext,150);
        maxHeight = DisplayUtils.dip2px(mContext,230);
        marginTop = DisplayUtils.dip2px(mContext,15);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_task_details, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final TaskDetailsBean detailsBean = mList.get(position);
        holder.mTvNickname.setText(detailsBean.getNickName());
        holder.mTvContent.setText(detailsBean.getDescribes());
        holder.mTvDate.setText(detailsBean.getCreateTime());
        GlideTools.setVipResourceS(holder.mIvVip,detailsBean.getGrade());
        Glide.with(mContext).load(HttpHost.qiNiu + detailsBean.getHeadImg()).apply(GlideTools.getHeadOptions()).into(holder.mCiHead);

        if (detailsBean.getState().equals("0")){
            //未赞赏
            holder.mTvAward.setVisibility(View.VISIBLE);
            holder.mTvReject.setVisibility(View.VISIBLE);
            holder.mTvReject.setBackgroundResource(R.drawable.shape_9a9a9a);
            holder.mTvReject.setText("拒绝");
            holder.mTvReject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnClickListener.onClick(v,position);
                }
            });
            holder.mTvAward.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnClickListener.onClick(v,position);
                }
            });
        }else if (detailsBean.getState().equals("1")){
            //赞赏
            holder.mTvAward.setVisibility(View.GONE);
            holder.mTvReject.setBackgroundResource(R.drawable.shape_yellow_bg);
            holder.mTvReject.setText("已赞赏");
        }else {
            //2 拒绝
            holder.mTvAward.setVisibility(View.GONE);
            holder.mTvReject.setBackgroundResource(R.drawable.shape_9a9a9a);
            holder.mTvReject.setText("已拒绝");
        }

        Glide.with(mContext).asBitmap().load(HttpHost.qiNiu + detailsBean.getUrlAddress())
                .apply(GlideTools.getOptions())
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.mIvImage.getLayoutParams();
                        params.width = mMaxWidth;
                        int height = mMaxWidth*resource.getHeight()/resource.getWidth();
                        if ( height >maxHeight){
                            params.height = maxHeight;
                        }else {
                            params.height = height;
                        }
                        holder.mIvImage.setLayoutParams(params);
                        holder.mIvImage.setImageBitmap(resource);
                    }
                });
        //设置margin
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.mRlImage.getLayoutParams();
        if (TextUtils.isEmpty(detailsBean.getDescribes())){
            params.setMargins(0,-marginTop,0,0);
        }else {
            params.setMargins(0,marginTop,0,0);
        }
        if (detailsBean.getType().equals("02")){
            holder.mIvVideo.setVisibility(View.VISIBLE);
        }else {
            holder.mIvVideo.setVisibility(View.GONE);
        }
        holder.mRlImage.setLayoutParams(params);

        holder.mRlImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (detailsBean.getType().equals("02")){
                    VideoPlayActivity.startActivity(mContext,HttpHost.qiNiu + detailsBean.getUrlAddress());
                }else {
                    images.clear();
                    images.add(new ImageBean(detailsBean.getUrlAddress()));
                    ImageDetailsActivity.startActivity(mContext,0,images);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView mCiHead;
        TextView mTvNickname;
        ImageView mIvVip;
        TextView mTvDate;
        TextView mTvContent;
        ImageView mIvImage;
        TextView mTvReject;
        TextView mTvAward;
        RelativeLayout mRlImage;
        ImageView mIvVideo;
        public ViewHolder(View itemView) {
            super(itemView);
            mCiHead = itemView.findViewById(R.id.ci_head);
            mTvNickname = itemView.findViewById(R.id.tv_nickname);
            mIvVip = itemView.findViewById(R.id.iv_vip);
            mTvDate = itemView.findViewById(R.id.tv_date);
            mTvContent = itemView.findViewById(R.id.tv_content);
            mIvImage = itemView.findViewById(R.id.iv_image);
            mTvReject = itemView.findViewById(R.id.tv_reject);
            mTvAward = itemView.findViewById(R.id.tv_award);
            mRlImage = itemView.findViewById(R.id.rl_image);
            mIvVideo = itemView.findViewById(R.id.iv_video);
        }
    }

}
