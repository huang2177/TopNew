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
import com.kw.top.bean.FriendApplyBean;
import com.kw.top.listener.OnClickListener;
import com.kw.top.retrofit.HttpHost;
import com.kw.top.tools.GlideTools;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * author: zy
 * data  : 2018/6/10
 * des   :
 */

public class FriendApplyAdapter extends RecyclerView.Adapter<FriendApplyAdapter.ViewHolder> {


    private Context mContext;
    private List<FriendApplyBean> mList;
    private OnClickListener mOnClickListener;

    public FriendApplyAdapter(Context context, List<FriendApplyBean> list,OnClickListener listener) {
        this.mContext = context;
        this.mList = list;
        this.mOnClickListener = listener;
    }

    public void setData(List<FriendApplyBean> list){
        this.mList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_friend_apply, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        FriendApplyBean applyBean = mList.get(position);
        holder.mTvNickName.setText(applyBean.getNickName());
        holder.mTvDes.setText("我是" + applyBean.getNickName() + "很高兴遇见你...");
        holder.mTvGiftNum.setText("X" + applyBean.getNum());
        Glide.with(mContext).load(HttpHost.qiNiu + applyBean.getHeadImg()).apply(GlideTools.getHeadOptions()).into(holder.mCiHead);
        Glide.with(mContext).load(HttpHost.qiNiu+applyBean.getGiftPicture()).into(holder.mIvGift);
        String friendStatus = applyBean.getFriendsState();
        if (friendStatus.equals("2")) {
            //拒绝
            holder.mTvAgree.setVisibility(View.GONE);
            holder.mTvReject.setText("已拒绝");
        } else if (friendStatus.equals("1")) {
            //同意
            holder.mTvReject.setText("已同意");
            holder.mTvReject.setTextColor(mContext.getResources().getColor(R.color.yellow_E7C375));
            holder.mTvAgree.setVisibility(View.GONE);
        } else {
            //未处理
            holder.mTvAgree.setVisibility(View.VISIBLE);
            holder.mTvReject.setText("拒绝");
            holder.mTvAgree.setText("同意");
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
        @BindView(R.id.ci_head)
        CircleImageView mCiHead;
        @BindView(R.id.tv_des)
        TextView mTvDes;
        @BindView(R.id.iv_gift)
        ImageView mIvGift;
        @BindView(R.id.tv_gift_num)
        TextView mTvGiftNum;
        @BindView(R.id.tv_reject)
        TextView mTvReject;
        @BindView(R.id.tv_agree)
        TextView mTvAgree;
        @BindView(R.id.tv_nickname)
        TextView mTvNickName;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
