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
import com.kw.top.bean.RedbagDetailBean;
import com.kw.top.retrofit.HttpHost;
import com.kw.top.tools.GlideTools;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * author  ： zy
 * date    ： 2018/7/15
 * des     ：
 */

public class RedbagDetailsAdapter extends RecyclerView.Adapter<RedbagDetailsAdapter.ViewHolder> {

    private Context mContext;
    private List<RedbagDetailBean.RedPackageDetailsListBean> mList = new ArrayList<>();

    public RedbagDetailsAdapter(Context context, List<RedbagDetailBean.RedPackageDetailsListBean> listBeans) {
        this.mContext = context;
        this.mList = listBeans;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_redbag_details, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RedbagDetailBean.RedPackageDetailsListBean listBean = mList.get(position);
        holder.mTvNickname.setText(listBean.getNickName());
        holder.mTvTime.setText(listBean.getGetTime());
        holder.mTvAmount.setText(listBean.getAmount()+"");
        Glide.with(mContext).load(HttpHost.qiNiu + listBean.getHeadImg())
                .apply(GlideTools.getHeadOptions()).into(holder.mCiHead);
        GlideTools.setVipToTextView(mContext,holder.mTvNickname,listBean.getGrade());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView mCiHead;
        TextView mTvNickname;
        TextView mTvTime;
        TextView mTvAmount;
        public ViewHolder(View itemView) {
            super(itemView);
            mCiHead = itemView.findViewById(R.id.ci_head);
            mTvNickname = itemView.findViewById(R.id.tv_nickname);
            mTvTime = itemView.findViewById(R.id.tv_time);
            mTvAmount = itemView.findViewById(R.id.tv_amount);
        }
    }

}
