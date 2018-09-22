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
import com.kw.top.listener.OnDeleteListener;
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

public class VipMangerAdapter extends RecyclerView.Adapter<VipMangerAdapter.ViewHolder> {

    private Context mContext;
    private List<AllUserBean> mList = new ArrayList<>();
    private OnDeleteListener mOnDeleteListener;

    public VipMangerAdapter(Context context,List<AllUserBean> list,OnDeleteListener onDeleteListener) {
        this.mContext = context;
        this.mOnDeleteListener = onDeleteListener;
        this.mList = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_vip_manager, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        AllUserBean allUserBean = mList.get(position);
        Glide.with(mContext).load(HttpHost.qiNiu+allUserBean.getHeadImg()).apply(GlideTools.getHeadOptions()).into(holder.mCiHead);
        holder.mTvNickname.setText(allUserBean.getNickName());
        holder.mTvInfo.setText(allUserBean.getAge()+"岁  " + allUserBean.getCity()+ allUserBean.getConstellation());

        holder.mTvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnDeleteListener.onDelete(view,position);
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
        TextView mTvInfo;
        TextView mTvDelete;
        public ViewHolder(View itemView) {
            super(itemView);
            mCiHead = itemView.findViewById(R.id.ci_head);
            mTvNickname = itemView.findViewById(R.id.tv_nickname);
            mTvInfo = itemView.findViewById(R.id.tv_info);
            mTvDelete = itemView.findViewById(R.id.tv_delete);
        }
    }
}
