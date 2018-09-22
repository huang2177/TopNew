package com.kw.top.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kw.top.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * author  ： zy
 * date    ： 2018/7/7
 * des     ：
 */

public class NoticeCenterAdapter extends RecyclerView.Adapter<NoticeCenterAdapter.ViewHolder> {

    private Context mContext;
    private List<String> mList = new ArrayList<>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_notice_center, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTvNotice;
        TextView mTvTime;
        public ViewHolder(View itemView) {
            super(itemView);
            mTvNotice = itemView.findViewById(R.id.tv_notice);
            mTvTime = itemView.findViewById(R.id.tv_time);
        }
    }

}
