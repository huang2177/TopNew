package com.kw.top.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kw.top.R;
import com.kw.top.bean.ClubListBean;
import com.kw.top.ui.activity.circle.SendCircleActivity;
import com.kw.top.ui.activity.task.ClubTaskDetailsActivity;
import com.kw.top.utils.RxToast;

import java.util.ArrayList;
import java.util.List;

/**
 * author  ： zy
 * date    ： 2018/8/11
 * des     ：
 */

public class ClubTaskAdapter extends RecyclerView.Adapter<ClubTaskAdapter.ViewHolder> {

    private Context mContext;
    private List<ClubListBean> mList = new ArrayList<>();
    private boolean isOwner;

    public ClubTaskAdapter(Context context,List<ClubListBean> listBeans,boolean isOwner){
        this.mContext = context;
        this.mList = listBeans;
        this.isOwner = isOwner;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_clubt_task, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final ClubListBean bean = mList.get(position);
        holder.mTvDesc.setText(bean.getDescribes());
        holder.mTvMoney.setText("¥"+bean.getRedAmount());
        if (isOwner){
            holder.mTvComplete.setText("任务详情");
            holder.mTvComplete.setBackgroundResource(R.drawable.shape_yellow_bg);
        } else {
            if (bean.getFinish().equals("1")){
                holder.mTvComplete.setText("已完成");
                holder.mTvComplete.setBackgroundResource(R.drawable.shape_gray_bg);
            }else {
                holder.mTvComplete.setText("前去完成");
                holder.mTvComplete.setBackgroundResource(R.drawable.shape_yellow_bg);
            }
        }

        holder.mTvComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOwner){
                    ClubTaskDetailsActivity.startActivity(mContext,bean.getTaskId());
                }else {
                    if (bean.getFinish().equals("0")){
                        SendCircleActivity.startActivity(mContext,bean.getTaskId(),bean.getType(),true);
                    }else {
                        RxToast.normal("任务已完成");
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mIvImage;
        TextView mTvDesc;
        TextView mTvComplete;
        TextView mTvMoney;

        public ViewHolder(View itemView) {
            super(itemView);
            mIvImage = itemView.findViewById(R.id.iv_image);
            mTvDesc = itemView.findViewById(R.id.tv_desc);
            mTvComplete = itemView.findViewById(R.id.tv_complete);
            mTvMoney = itemView.findViewById(R.id.tv_money);
        }
    }

}
