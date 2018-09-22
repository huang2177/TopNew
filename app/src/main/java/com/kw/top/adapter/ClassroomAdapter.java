package com.kw.top.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kw.top.R;
import com.kw.top.app.BaseApplication;
import com.kw.top.bean.ClassroomBean;
import com.kw.top.retrofit.HttpHost;
import com.kw.top.tools.GlideTools;
import com.kw.top.ui.activity.classroom.ClassroomDetailsActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * author  ： zy
 * date    ： 2018/6/17
 * des     ：
 */

public class ClassroomAdapter extends RecyclerView.Adapter<ClassroomAdapter.ViewHolder> {

    private Context mContext;
    private List<ClassroomBean> mList = new ArrayList<>();

    public ClassroomAdapter(Context context, List<ClassroomBean> list) {
        this.mContext = context;
        this.mList = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_classroom, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final ClassroomBean classroomBean = mList.get(position);
        Glide.with(mContext).load(HttpHost.qiNiu+classroomBean.getHomePic()).apply(GlideTools.getOptions()).into(holder.mIvImage);
        holder.mTvTitle.setText(classroomBean.getClassroomName());
        holder.mTvDesc.setText(Html.fromHtml("<font size='12'>"+classroomBean.getContent()+"</font>"));
        holder.mTvTime.setText(classroomBean.getCreateTime());

        holder.mLlItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClassroomDetailsActivity.startActivity(mContext,classroomBean.getClassroomId()+"");
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mIvImage;
        TextView mTvTitle;
        TextView mTvDesc;
        TextView mTvTime;
        LinearLayout mLlItem;

        public ViewHolder(View itemView) {
            super(itemView);
            mIvImage = itemView.findViewById(R.id.iv_image);
            mTvTitle = itemView.findViewById(R.id.tv_title);
            mTvDesc = itemView.findViewById(R.id.tv_desc);
            mLlItem = itemView.findViewById(R.id.ll_item);
            mTvTime = itemView.findViewById(R.id.tv_time);
        }
    }
}
