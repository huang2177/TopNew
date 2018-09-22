package com.kw.top.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.kw.top.R;
import com.kw.top.listener.OnDeleteListener;
import com.kw.top.listener.OnItemClickListener;
import com.luck.picture.lib.entity.LocalMedia;

import java.util.List;

/**
 * author: zy
 * data  : 2018/6/4
 * des   :
 */

public class GridViewAdapter extends BaseAdapter {

    private Context mContext;
    private List<LocalMedia> mList;
    private OnDeleteListener mOnDeleteListener;
    private OnItemClickListener mOnItemClickListener;

    public GridViewAdapter(Context context, List<LocalMedia> list, OnDeleteListener onDeleteListener, OnItemClickListener onItemClickListener){
        this.mContext = context;
        this.mList = list;
        this.mOnDeleteListener = onDeleteListener;
        this.mOnItemClickListener = onItemClickListener;
    }

    public void setData(List<LocalMedia> list){
        this.mList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        int count;
        if (mList.size() <9) {
            count = mList.size()+1;
        }else {
            count = mList.size();
        }
        return count;
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_circle_gridview,null);
            holder.image = (ImageView) convertView.findViewById(R.id.image);
            holder.rlAdd = (RelativeLayout) convertView.findViewById(R.id.rl_add);
            holder.ivDelete = convertView.findViewById(R.id.iv_delete);
            holder.iv_video = convertView.findViewById(R.id.iv_video);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (position == mList.size()){
            holder.rlAdd.setVisibility(View.VISIBLE);
            holder.image.setVisibility(View.GONE);
            holder.ivDelete.setVisibility(View.GONE);
            holder.iv_video.setVisibility(View.GONE);
        }else {
            holder.rlAdd.setVisibility(View.GONE);
            holder.image.setVisibility(View.VISIBLE);
            holder.ivDelete.setVisibility(View.VISIBLE);
            Glide.with(mContext)
                    .load(mList.get(position).getPath())
                    .into(holder.image);

            if (mList.get(position).getPictureType().contains("video")){
                holder.iv_video.setVisibility(View.VISIBLE);
            }else {
                holder.iv_video.setVisibility(View.GONE);
            }
        }

        if (mList.size()>=9){
            holder.rlAdd.setVisibility(View.GONE);
        }

        holder.rlAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(v,position);
            }
        });

        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnDeleteListener.onDelete(v,position);
            }
        });

        return convertView;
    }

    class ViewHolder {
        ImageView image,ivDelete,iv_video;
        RelativeLayout rlAdd;
    }
}
