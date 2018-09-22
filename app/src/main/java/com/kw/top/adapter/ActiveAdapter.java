package com.kw.top.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kw.top.R;
import com.kw.top.bean.ActiveBean;
import com.kw.top.retrofit.HttpHost;
import com.kw.top.tools.GlideTools;
import com.kw.top.ui.activity.active.ActiveDetailsActivity;
import com.kw.top.utils.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * author  ： zy
 * date    ： 2018/6/17
 * des     ：
 */

public class ActiveAdapter extends RecyclerView.Adapter<ActiveAdapter.Viewholder> {

    private Context mContext;
    private List<ActiveBean> mList = new ArrayList<>();

    public ActiveAdapter(Context context, List<ActiveBean> list) {
        this.mContext = context;
        this.mList = list;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_active, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        final ActiveBean activeBean = mList.get(position);
        if (position == 0) {
            holder.mTvLine.setVisibility(View.VISIBLE);
        } else {
            holder.mTvLine.setVisibility(View.GONE);
        }
        if (activeBean.getValid().equals("1")) {
            holder.mTvActiveFinish.setVisibility(View.GONE);
            holder.mLlItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ActiveDetailsActivity.startActivity(mContext, activeBean.getActivityId() + "");
                }
            });
        } else {
            holder.mTvActiveFinish.setVisibility(View.VISIBLE);
        }
        holder.mTvTitle.setText(activeBean.getActivityName());
        holder.mTvTime.setText(activeBean.getCreateTime() + "~" + activeBean.getEndTime());
        Glide.with(mContext).load(HttpHost.qiNiu + activeBean.getPicture()).apply(GlideTools.getOptions()).into(holder.mIvImage);

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class Viewholder extends RecyclerView.ViewHolder {

        TextView mTvLine;
        ImageView mIvImage;
        TextView mTvTitle;
        TextView mTvTime;
        @BindView(R.id.ll_item)
        LinearLayout mLlItem;
        TextView mTvActiveFinish;

        public Viewholder(View itemView) {
            super(itemView);
            mTvLine = itemView.findViewById(R.id.tv_line);
            mIvImage = itemView.findViewById(R.id.iv_image);
            mTvTitle = itemView.findViewById(R.id.tv_title);
            mTvTime = itemView.findViewById(R.id.tv_time);
            mLlItem = itemView.findViewById(R.id.ll_item);
            mTvActiveFinish = itemView.findViewById(R.id.tv_active_finish);
        }
    }



}
