package com.kw.top.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kw.top.R;
import com.kw.top.bean.CommentBean;
import com.kw.top.listener.OnDeleteListener;
import com.kw.top.listener.OnItemClickListener;
import com.kw.top.retrofit.HttpHost;
import com.kw.top.tools.GlideTools;
import com.kw.top.utils.RxTextTool;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * author  ： zy
 * date    ： 2018/6/30
 * des     ：
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private Context mContext;
    private List<CommentBean> mList = new ArrayList<>();
    private OnDeleteListener mOnDeleteListener;
    private OnItemClickListener mOnItemClickListener;

    public CommentAdapter(Context context, List<CommentBean> list, OnDeleteListener onDeleteListener, OnItemClickListener onItemClickListener) {
        this.mContext = context;
        this.mList = list;
        this.mOnDeleteListener = onDeleteListener;
        this.mOnItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_comment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        CommentBean commentBean = mList.get(position);
        holder.mTvTime.setText(commentBean.getComTime());
        holder.mTvNickname.setText(commentBean.getComNickName());
        if (TextUtils.isEmpty(commentBean.getRetUserid())){
            holder.mTvContent.setText(commentBean.getComContent());
        }else {
            RxTextTool.getBuilder("回复")
                    .append(commentBean.getRetNickName()).setClickSpan(new TextClick())
                    .append(": "+commentBean.getComContent())
            .into(holder.mTvContent);
        }

        Glide.with(mContext).load(HttpHost.qiNiu + commentBean.getComHeadImg()).apply(GlideTools.getHeadOptions()).into(holder.mCiHead);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               mOnItemClickListener.onItemClick(v,position);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mOnDeleteListener != null)
                    mOnDeleteListener.onDelete(v, position);
                return false;
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
        TextView mTvTime;
        TextView mTvContent;
        RelativeLayout mRlCommentItem;

        public ViewHolder(View itemView) {
            super(itemView);
            mCiHead = itemView.findViewById(R.id.ci_head);
            mTvNickname = itemView.findViewById(R.id.tv_nickname);
            mTvTime = itemView.findViewById(R.id.tv_time);
            mTvContent = itemView.findViewById(R.id.tv_content);
            mRlCommentItem = itemView.findViewById(R.id.rl_comment_item);
        }
    }

    private class TextClick extends ClickableSpan{

        @Override
        public void onClick(View widget) {

        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setColor(mContext.getResources().getColor(R.color.yellow_DEAC6A));
            ds.setUnderlineText(false);
        }
    }

}
