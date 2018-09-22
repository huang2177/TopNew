package com.kw.top.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kw.top.R;
import com.kw.top.bean.ReceiveRedBean;
import com.kw.top.retrofit.HttpHost;
import com.kw.top.tools.GlideTools;
import com.kw.top.ui.activity.user_center.RedPacketDetailsActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * author  ： zy
 * date    ： 2018/6/16
 * des     ：
 */

public class ReceiveRedAdapter extends RecyclerView.Adapter<ReceiveRedAdapter.ViewHolder> {

    private Context mContext;
    private List<ReceiveRedBean.GetRedCouponsListBean> mList = new ArrayList<>();

    public ReceiveRedAdapter(Context context,List<ReceiveRedBean.GetRedCouponsListBean> list){
        this.mContext = context;
        this.mList = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_receive_red, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final ReceiveRedBean.GetRedCouponsListBean listBean = mList.get(position);
        holder.mTvNickname.setText(listBean.getNickName());
        holder.mTvDate.setText(listBean.getGetTime());
        holder.mTvAmountNum.setText(listBean.getAmount());
        GlideTools.setVipResourceS(holder.mIvVip,listBean.getGrade());
        Glide.with(mContext).load(HttpHost.qiNiu + listBean.getHeadImg()).apply(GlideTools.getHeadOptions()).into(holder.mCiHead);
        holder.mRlItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RedPacketDetailsActivity.startActivity(mContext,listBean.getRedPackageId());
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
        TextView mTvAmountNum;
        RelativeLayout mRlItem;
        public ViewHolder(View itemView) {
            super(itemView);
            mCiHead = itemView.findViewById(R.id.ci_head);
            mTvNickname = itemView.findViewById(R.id.tv_nickname);
            mIvVip = itemView.findViewById(R.id.iv_vip);
            mTvDate = itemView.findViewById(R.id.tv_date);
            mTvAmountNum = itemView.findViewById(R.id.tv_amount_num);
            mRlItem = itemView.findViewById(R.id.rl_item);
        }
    }

}
