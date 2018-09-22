package com.kw.top.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kw.top.R;
import com.kw.top.bean.AllUserBean;
import com.kw.top.listener.OnClickListener;
import com.kw.top.retrofit.HttpHost;
import com.kw.top.tools.GlideTools;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * author  ： zy
 * date    ： 2018/6/19
 * des     ：
 */

public class VipApplyAdapter extends RecyclerView.Adapter<VipApplyAdapter.ViewHolder> {


    private Context mContext;
    private OnClickListener mOnClickListener;
    private List<AllUserBean> mList = new ArrayList<>();

    public VipApplyAdapter(Context context,List<AllUserBean> list,OnClickListener onClickListener) {
        this.mContext = context;
        this.mOnClickListener = onClickListener;
        this.mList = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_vip_apply, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        AllUserBean allUserBean = mList.get(position);
        Glide.with(mContext).load(HttpHost.qiNiu+allUserBean.getHeadImg()).apply(GlideTools.getHeadOptions()).into(holder.mCiHead);
        holder.mTvNickname.setText(allUserBean.getNickName());

        if (allUserBean.getApply_status() == 0){
            holder.mTvAgree.setText("已拒绝");
            holder.mTvAgree.setBackgroundResource(R.drawable.shape_black_bg);
            holder.mTvReject.setVisibility(View.GONE);
        }else if (allUserBean.getApply_status() ==1){
            holder.mTvAgree.setText("已同意");
            holder.mTvAgree.setBackgroundResource(R.drawable.shape_yellow_bg);
            holder.mTvReject.setVisibility(View.GONE);
        }else {
            holder.mTvAgree.setText("同意");
            holder.mTvAgree.setBackgroundResource(R.drawable.shape_yellow_bg);
            holder.mTvReject.setVisibility(View.VISIBLE);
            holder.mTvAgree.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnClickListener.onClick(view,position);
                }
            });
            holder.mTvReject.setOnClickListener(new View.OnClickListener() {
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
        CircleImageView mCiHead;
        TextView mTvNickname;
        TextView mTvInfo;
        TextView mTvAgree;
        TextView mTvReject;
        public ViewHolder(View itemView) {
            super(itemView);
            mCiHead = itemView.findViewById(R.id.ci_head);
            mTvNickname = itemView.findViewById(R.id.tv_nickname);
            mTvInfo = itemView.findViewById(R.id.tv_info);
            mTvAgree = itemView.findViewById(R.id.tv_agree);
            mTvReject = itemView.findViewById(R.id.tv_reject);
        }
    }
}
