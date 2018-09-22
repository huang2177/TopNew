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
import com.kw.top.bean.CircleNewsBean;
import com.kw.top.listener.OnClickListener;
import com.kw.top.retrofit.HttpHost;
import com.kw.top.tools.GlideTools;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * author  ： zy
 * date    ： 2018/7/7
 * des     ：
 */

public class TopNewAdapter extends RecyclerView.Adapter<TopNewAdapter.ViewHolder> {
    private Context mContext;
    private List<CircleNewsBean.CommentListBean> mList = new ArrayList<>();
    private OnClickListener mOnClickListener;

    public TopNewAdapter(Context context, List<CircleNewsBean.CommentListBean> list,OnClickListener onClickListener) {
        this.mContext = context;
        this.mList = list;
        this.mOnClickListener = onClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_top_news, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        CircleNewsBean.CommentListBean circleNewsBean = mList.get(position);
        holder.mTvNickname.setText(circleNewsBean.getComNickName());
        holder.mTvContent.setText(circleNewsBean.getComContent());
        holder.mTvTime.setText(circleNewsBean.getComTime());
        Glide.with(mContext).load(HttpHost.qiNiu + circleNewsBean.getComHeadImg()).apply(GlideTools.getHeadOptions()).into(holder.mCiHead);
        Glide.with(mContext).load(HttpHost.qiNiu + circleNewsBean.getDynamicPicdynamicPic()).apply(GlideTools.getOptions()).into(holder.mIvImage);
        if (circleNewsBean.getType().equals("02")){
            holder.mIvVideo.setVisibility(View.VISIBLE);
        }else {
            holder.mIvVideo.setVisibility(View.INVISIBLE);
        }
        holder.mRlItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnClickListener.onClick(v,position);
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
        TextView mTvContent;
        ImageView mIvImage;
        RelativeLayout mRlItem;
        TextView mTvTime;
        ImageView mIvVideo;
        public ViewHolder(View itemView) {
            super(itemView);
            mCiHead = itemView.findViewById(R.id.ci_head);
            mTvNickname = itemView.findViewById(R.id.tv_nickname);
            mTvContent = itemView.findViewById(R.id.tv_content);
            mIvImage = itemView.findViewById(R.id.iv_image);
            mTvTime = itemView.findViewById(R.id.tv_time);
            mRlItem = itemView.findViewById(R.id.rl_item);
            mIvVideo = itemView.findViewById(R.id.iv_video);
        }
    }

}
