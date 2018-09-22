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
import com.kw.top.bean.InviteRecordBean;
import com.kw.top.retrofit.HttpHost;
import com.kw.top.tools.GlideTools;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * author: zy
 * data  : 2018/5/20
 * des   :
 */

public class InviteRecordAdapter extends RecyclerView.Adapter<InviteRecordAdapter.ViewHolder> {

    private Context mContext;
    private List<InviteRecordBean.InviteListBean> mList = new ArrayList<>();

    public InviteRecordAdapter(Context context, List<InviteRecordBean.InviteListBean> list) {
        this.mContext = context;
        this.mList = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_invite_record, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        InviteRecordBean.InviteListBean inviteListBean = mList.get(position);
        Glide.with(mContext).load(HttpHost.qiNiu + inviteListBean.getHeadImg()).apply(GlideTools.getHeadOptions()).into(holder.mCiHead);
        holder.mTvNickname.setText(inviteListBean.getNickName());
        holder.mTvNum.setText(inviteListBean.getUserProfit());

        holder.mTvInfo.setText(inviteListBean.getCreateTime());
        GlideTools.setVipResourceS(holder.mIvVip,inviteListBean.getGrade());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView mCiHead;
        TextView mTvNickname;
        ImageView mIvVip;
        TextView mTvNum;
        TextView mTvInfo;
        public ViewHolder(View itemView) {
            super(itemView);
            mCiHead = itemView.findViewById(R.id.ci_head);
            mTvNickname = itemView.findViewById(R.id.tv_nickname);
            mIvVip = itemView.findViewById(R.id.iv_vip);
            mTvNum = itemView.findViewById(R.id.tv_num);
            mTvInfo = itemView.findViewById(R.id.tv_info);
        }
    }

}
