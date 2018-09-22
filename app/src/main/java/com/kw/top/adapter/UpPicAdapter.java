package com.kw.top.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.kw.top.R;
import com.kw.top.listener.OnDeleteListener;
import com.luck.picture.lib.entity.LocalMedia;

import java.util.ArrayList;
import java.util.List;

/**
 * author: zy
 * data  : 2018/6/5
 * des   :
 */

public class UpPicAdapter extends RecyclerView.Adapter<UpPicAdapter.ViewHolder> {

    private Context mContext;
    private List<LocalMedia> mList = new ArrayList<>();

    public UpPicAdapter(Context context,OnDeleteListener onDeleteListener) {
        this.mContext = context;
        this.mOnDeleteListener = onDeleteListener;
    }

    private OnDeleteListener mOnDeleteListener;

    public void setData(List<LocalMedia> list){
        this.mList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_uppic, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Glide.with(mContext).load(mList.get(position).getPath())
                .into(holder.mImage);
//        mIvDelete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mOnDeleteListener.onDelete(view,position);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mImage;
        public ViewHolder(View itemView) {
            super(itemView);
            mImage = itemView.findViewById(R.id.image_uppic);
        }
    }

}
