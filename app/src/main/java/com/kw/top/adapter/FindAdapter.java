package com.kw.top.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.kw.top.R;
import com.kw.top.bean.AllUserBean;
import com.kw.top.listener.OnClickListener;
import com.kw.top.listener.OnItemClickListener;
import com.kw.top.retrofit.HttpHost;
import com.kw.top.tools.ConstantValue;
import com.kw.top.tools.GlideTools;
import com.kw.top.ui.activity.find.FindDetailsActivity;
import com.kw.top.utils.DisplayUtils;
import com.kw.top.utils.SPUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * author: zy
 * data  : 2018/5/27
 * des   :
 */

public class FindAdapter extends RecyclerView.Adapter<FindAdapter.ViewHolder> {

    private Context mContext;
    private List<AllUserBean> mList = new ArrayList<>();
    private OnClickListener mOnClickListener;
    private int maxWidth;

    public FindAdapter(Activity context, List<AllUserBean> list, OnClickListener onClickListener) {
        this.mContext = context;
        this.mList = list;
        this.mOnClickListener = onClickListener;
        maxWidth = DisplayUtils.getScreenWidth(context) - DisplayUtils.dip2px(context, 30);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_find, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final AllUserBean userBean = mList.get(position);
        holder.mTvNickname.setText(userBean.getNickName());

        if (TextUtils.isEmpty(userBean.getAge())){
            holder.tv_age.setVisibility(View.GONE);
        }else {
            holder.tv_age.setText(userBean.getAge() + "岁");
        }

        if (TextUtils.isEmpty(userBean.getCity())){
            holder.tv_city.setVisibility(View.GONE);
        }else {
            holder.tv_city.setText(userBean.getCity());
        }


        if (TextUtils.isEmpty(userBean.getConstellation())){
            holder.tv_xz.setVisibility(View.GONE);
        }else {
            holder.tv_xz.setText(userBean.getConstellation());
        }
        /*holder.tv_age.setText(userBean.getAge() + "岁");
        holder.tv_city.setText(userBean.getCity());
        holder.tv_xz.setText(userBean.getConstellation());*/

        if (TextUtils.isEmpty(userBean.getLoginTime())) {
            holder.tvTime.setVisibility(View.GONE);
        } else {
            holder.tvTime.setText("最近上线时间: " + userBean.getLoginTime() + "前");
        }
        GlideTools.setVipResourceS(holder.mIvVip, Integer.parseInt(userBean.getGrade()));
        final String myUseId = SPUtils.getString(mContext, ConstantValue.KEY_USER_ID, "") + ".0";
        if (userBean.getUserId().equals(myUseId)) {
            holder.mTvAddFriend.setVisibility(View.GONE);
        } else {
            holder.mTvAddFriend.setVisibility(View.VISIBLE);
            if (userBean.getFriends().equals("1")) {
                holder.mTvAddFriend.setText("赠送礼物");
            } else {
                holder.mTvAddFriend.setText("加为好友");
                holder.mTvAddFriend.setVisibility(View.VISIBLE);
            }
        }

        holder.mTvAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnClickListener.onClick(view, position);
            }
        });
        Glide.with(mContext)
                .asBitmap()
                .load(HttpHost.qiNiu + userBean.getDynamicPic())
                .apply(GlideTools.getOptions())
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.mIvImage.getLayoutParams();
                        params.height = maxWidth * resource.getHeight() / resource.getWidth();
                        params.width = maxWidth;
                        holder.mIvImage.setLayoutParams(params);
                        holder.mIvImage.setImageBitmap(resource);
                    }
                });

        holder.mLlItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FindDetailsActivity.startActivity(mContext, userBean.getUserId() + "");
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTvNickname, tv_age, tv_city, tv_xz, mTvAddFriend, tvTime;
        ImageView mIvImage;
        LinearLayout mLlItem;
        ImageView mIvVip;

        public ViewHolder(View itemView) {
            super(itemView);
            mTvNickname = itemView.findViewById(R.id.tv_nickname_find);
            tv_age = itemView.findViewById(R.id.tv_age);
            tv_city = itemView.findViewById(R.id.tv_city);
            tv_xz = itemView.findViewById(R.id.tv_xz);
            mTvAddFriend = itemView.findViewById(R.id.tv_add_friend);
            mIvImage = itemView.findViewById(R.id.iv_image_home);
            mLlItem = itemView.findViewById(R.id.ll_item);
            mIvVip = itemView.findViewById(R.id.iv_vip);
            tvTime = itemView.findViewById(R.id.iv_time);
        }
    }

}
