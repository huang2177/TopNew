package com.kw.top.adapter;

import android.content.Context;
import android.content.Intent;
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
import com.kw.top.bean.TaskBean;
import com.kw.top.listener.OnClickListener;
import com.kw.top.retrofit.HttpHost;
import com.kw.top.tools.GlideTools;
import com.kw.top.ui.activity.task.PublishTaskActivity;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * author  ： zy
 * date    ： 2018/6/12
 * des     ：
 */

public class MyTaskAdapter extends RecyclerView.Adapter<MyTaskAdapter.ViewHolder> {

    private Context mContext;
    private List<TaskBean> mList = new ArrayList<>();
    private int type;
    private OnClickListener mOnClickListener;

    public MyTaskAdapter(Context context, List<TaskBean> list,int type,OnClickListener onClickListener) {
        mContext = context;
        mList = list;
        this.type = type;
        this.mOnClickListener = onClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_my_task, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        TaskBean taskBean = mList.get(position);
        holder.mTvMoney.setText("¥"+taskBean.getRedAmount());
        holder.mTvDesc.setText(taskBean.getDescribes());
        if (type ==0){
            //世界任务
            holder.mCiHead.setVisibility(View.VISIBLE);
            holder.mTvNikename.setVisibility(View.VISIBLE);
            holder.mIvVip.setVisibility(View.VISIBLE);
            holder.mTvComplete.setVisibility(View.VISIBLE);
            holder.mTvCompleteNum.setVisibility(View.VISIBLE);
            holder.mLlMyTask.setVisibility(View.GONE);
            Glide.with(mContext)
                    .load(HttpHost.qiNiu+taskBean.getHeadImg())
                    .apply(GlideTools.getHeadOptions())
                    .into(holder.mCiHead);
            holder.mTvComplete.setText("去完成");
            if (taskBean.getValid().equals("1")){
                //有效
                holder.mTvComplete.setBackgroundResource(R.drawable.shape_yellow_bg);
            }else {
                holder.mTvComplete.setBackgroundResource(R.drawable.shape_black_bg);
            }
            holder.mTvCompleteNum.setText("已完成"+taskBean.getFinishNum()+"/"+taskBean.getNum());
            holder.mTvNikename.setText(taskBean.getNickName());
            GlideTools.setVipResourceS(holder.mIvVip,taskBean.getGrade());
            holder.mTvComplete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnClickListener.onClick(view,position);
                }
            });
            holder.mRlItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    mOnClickListener.onClick(view,position);
                }
            });
        }else {
            //我的任务
            holder.mCiHead.setVisibility(View.INVISIBLE);
            holder.mTvNikename.setVisibility(View.GONE);
            holder.mIvVip.setVisibility(View.GONE);
            holder.mTvComplete.setVisibility(View.GONE);
            holder.mTvCompleteNum.setVisibility(View.GONE);
            holder.mLlMyTask.setVisibility(View.VISIBLE);
            holder.mIvImage.setVisibility(View.VISIBLE);

            holder.mTvMyCompleteNum.setText("已完成"+taskBean.getFinishNum()+"/"+taskBean.getNum());
            holder.mTvTaskDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnClickListener.onClick(view,position);
                }
            });
        }






    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mIvImage;
        CircleImageView mCiHead;
        ImageView mIvVip;
        TextView mTvComplete;
        TextView mTvCompleteNum;
        TextView mTvMoney;
        TextView mTvNikename;
        LinearLayout mLlMyTask;
        TextView mTvTaskDetails,mTvMyCompleteNum,mTvDesc;
        RelativeLayout mRlItem;
        public ViewHolder(View itemView) {
            super(itemView);
            mIvImage = itemView.findViewById(R.id.iv_image);
            mCiHead = itemView.findViewById(R.id.ci_head);
            mIvVip = itemView.findViewById(R.id.iv_vip);
            mTvComplete = itemView.findViewById(R.id.tv_complete);
            mTvCompleteNum = itemView.findViewById(R.id.tv_complete_num);
            mTvMoney = itemView.findViewById(R.id.tv_money);
            mTvNikename = itemView.findViewById(R.id.tv_nickname);
            mLlMyTask = itemView.findViewById(R.id.ll_my_task);
            mTvTaskDetails = itemView.findViewById(R.id.tv_task_details);
            mTvMyCompleteNum = itemView.findViewById(R.id.tv_my_complete_num);
            mTvDesc = itemView.findViewById(R.id.tv_desc);
            mRlItem = itemView.findViewById(R.id.rl_task_item);
        }
    }

}
