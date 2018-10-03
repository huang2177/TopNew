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
import com.kw.top.bean.FriendBean;
import com.kw.top.listener.OnFriendClickListener;
import com.kw.top.listener.OnFriendDeleteListener;
import com.kw.top.retrofit.HttpHost;
import com.kw.top.tools.GlideTools;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * author: zy
 * data  : 2018/6/10
 * des   :
 */

public class FriendListAdapter extends RecyclerView.Adapter<FriendListAdapter.ViewHolder> {

    private Context mContext;
    private List<FriendBean> mList = new ArrayList<>();
    private OnFriendClickListener mOnClickListener;
    private OnFriendDeleteListener mOnDeleteListener;

    public FriendListAdapter(Context context, List<FriendBean> list, OnFriendClickListener onClickListener, OnFriendDeleteListener onDeleteListener) {
        this.mContext = context;
        this.mList = list;
        this.mOnClickListener = onClickListener;
        this.mOnDeleteListener = onDeleteListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_friend_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final FriendBean friendBean = mList.get(position);
        holder.mTvNickname.setText(friendBean.getNickName());
        GlideTools.setVipResourceS(holder.mIvVip, Integer.parseInt(friendBean.getGrade()));
        holder.tv_leave.setText("V" + friendBean.getGrade());
        Glide.with(mContext)
                .load(HttpHost.qiNiu + friendBean.getHeadImg())
                .apply(GlideTools.getHeadOptions())
                .into(holder.mCiHead);
        holder.mRlItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnClickListener.onClick(v, friendBean);
            }
        });
        holder.mTvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnDeleteListener.onDelete(v, friendBean.getFriendsId() + "");
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ci_head)
        CircleImageView mCiHead;
        @BindView(R.id.tv_nickname)
        TextView mTvNickname;
        @BindView(R.id.iv_vip)
        ImageView mIvVip;
        @BindView(R.id.rl_item)
        RelativeLayout mRlItem;
        @BindView(R.id.tv_delete)
        TextView mTvDelete;
        @BindView(R.id.tv_leav)
        TextView tv_leave;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
