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
import com.kw.top.bean.ReceiveSendGiftBean;
import com.kw.top.retrofit.HttpHost;
import com.kw.top.tools.GlideTools;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * author: zy
 * data  : 2018/6/7
 * des   :
 */

public class ReceiveSendGiftAdapter extends RecyclerView.Adapter<ReceiveSendGiftAdapter.ViewHolder> {

    private Context mContext;
    private List<ReceiveSendGiftBean> mList = new ArrayList<>();
    private int type; //0收到礼物 1送出的礼物

    public ReceiveSendGiftAdapter(Context context, List<ReceiveSendGiftBean> listBeans, int type) {
        this.mContext = context;
        this.mList = listBeans;
        this.type = type;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_receive_send_gift, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ReceiveSendGiftBean receiveSendGiftBean = mList.get(position);
        Glide.with(mContext).load(HttpHost.qiNiu + receiveSendGiftBean.getHeadImg()).apply(GlideTools.getHeadOptions()).into(holder.ciHead);
        GlideTools.setVipResourceS(holder.ivVip, Integer.parseInt(receiveSendGiftBean.getGrade()));
        Glide.with(mContext).load(HttpHost.qiNiu + receiveSendGiftBean.getGiftPicture()).apply(GlideTools.getOptions()).into(holder.ivFlower);
        holder.tvFlowerNum.setText("x" + receiveSendGiftBean.getNum());
        holder.tvNickName.setText(receiveSendGiftBean.getNickName());
        holder.tvDate.setText(receiveSendGiftBean.getReceiveTime());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView ciHead;
        TextView tvNickName, tvDate, tvFlowerNum;
        ImageView ivVip, ivFlower;

        public ViewHolder(View itemView) {
            super(itemView);
            ciHead = itemView.findViewById(R.id.ci_head);
            tvNickName = itemView.findViewById(R.id.tv_nickname);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvFlowerNum = itemView.findViewById(R.id.tv_flower_num);
            ivVip = itemView.findViewById(R.id.iv_vip);
            ivFlower = itemView.findViewById(R.id.iv_flower);
        }
    }

}
