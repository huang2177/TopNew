package com.kw.top.ui.fragment.find.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.kw.top.R;
import com.kw.top.retrofit.HttpHost;
import com.kw.top.tools.ConstantValue;
import com.kw.top.tools.GlideTools;
import com.kw.top.ui.fragment.find.HomePageDetailsActivity;
import com.kw.top.ui.fragment.find.baen.HomeBean;
import com.kw.top.utils.DisplayUtils;
import com.kw.top.utils.OnItemClickListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by shibing on 2018/9/24.
 */

public class HomePageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private Context mContext;
    private List<HomeBean> list;
    private int maxWidth;
    private OnItemClickListener listener;

    public HomePageAdapter(Context mContext, List<HomeBean> list) {
        this.mContext = mContext;
        this.list = list;
        maxWidth = DisplayUtils.getScreenWidth(mContext) - DisplayUtils.dip2px(mContext, 20);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.homepage_item, null, false);
        return new ViewHodler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        final ViewHodler viewHodler = (ViewHodler) holder;
        if (listener != null) {
            viewHodler.imaRanking.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(position);
                }
            });
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, HomePageDetailsActivity.class);
                intent.putExtra(ConstantValue.KEY_USER_ID, list.get(position).getUserId());
                mContext.startActivity(intent);
            }
        });

        /*Glide.with(mContext)
                .asBitmap()
                .load(HttpHost.qiNiu + list.get(position).getHeadImg())
                .apply(GlideTools.getOptions())
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) viewHodler.imageView.getLayoutParams();
                        params.height = maxWidth * resource.getHeight() / resource.getWidth();
                        params.width = maxWidth;
                        viewHodler.imageView.setLayoutParams(params);
                        viewHodler.imageView.setImageBitmap(resource);
                    }
                });*/


        Glide.with(mContext)
                .asBitmap()
                .load(HttpHost.qiNiu + list.get(position).getHeadImg())
                .apply(GlideTools.getOptions())
                .into(viewHodler.imageView);

        if (TextUtils.isEmpty(list.get(position).getStarNum())) {
            viewHodler.iamgeStaricon1.setVisibility(View.GONE);
            viewHodler.iamgeStaricon2.setVisibility(View.GONE);
            viewHodler.iamgeStaricon3.setVisibility(View.GONE);
            viewHodler.iamgeStaricon4.setVisibility(View.GONE);
            viewHodler.iamgeStaricon5.setVisibility(View.GONE);
        } else {
            switch (list.get(position).getStarNum()) {
                case "1":
                    viewHodler.iamgeStaricon1.setVisibility(View.VISIBLE);
                    viewHodler.iamgeStaricon2.setVisibility(View.GONE);
                    viewHodler.iamgeStaricon3.setVisibility(View.GONE);
                    viewHodler.iamgeStaricon4.setVisibility(View.GONE);
                    viewHodler.iamgeStaricon5.setVisibility(View.GONE);
                    break;
                case "2":
                    viewHodler.iamgeStaricon1.setVisibility(View.VISIBLE);
                    viewHodler.iamgeStaricon2.setVisibility(View.VISIBLE);
                    viewHodler.iamgeStaricon3.setVisibility(View.GONE);
                    viewHodler.iamgeStaricon4.setVisibility(View.GONE);
                    viewHodler.iamgeStaricon5.setVisibility(View.GONE);
                    break;
                case "3":
                    viewHodler.iamgeStaricon1.setVisibility(View.VISIBLE);
                    viewHodler.iamgeStaricon2.setVisibility(View.VISIBLE);
                    viewHodler.iamgeStaricon3.setVisibility(View.VISIBLE);
                    viewHodler.iamgeStaricon4.setVisibility(View.GONE);
                    viewHodler.iamgeStaricon5.setVisibility(View.GONE);
                    break;
                case "4":
                    viewHodler.iamgeStaricon1.setVisibility(View.VISIBLE);
                    viewHodler.iamgeStaricon2.setVisibility(View.VISIBLE);
                    viewHodler.iamgeStaricon3.setVisibility(View.VISIBLE);
                    viewHodler.iamgeStaricon4.setVisibility(View.VISIBLE);
                    viewHodler.iamgeStaricon5.setVisibility(View.GONE);
                    break;
                case "5":
                    viewHodler.iamgeStaricon1.setVisibility(View.VISIBLE);
                    viewHodler.iamgeStaricon2.setVisibility(View.VISIBLE);
                    viewHodler.iamgeStaricon3.setVisibility(View.VISIBLE);
                    viewHodler.iamgeStaricon4.setVisibility(View.VISIBLE);
                    viewHodler.iamgeStaricon5.setVisibility(View.VISIBLE);
                    break;
            }
            viewHodler.textView2.setText(list.get(position).getProfit() + "T币/分钟");
            viewHodler.textView.setText(list.get(position).getNickName());
            viewHodler.textView1.setText(list.get(position).getObjective());
        }

    }

    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }


    public class ViewHodler extends RecyclerView.ViewHolder {
        @BindView(R.id.home_item_iamge)
        ImageView imageView;
        @BindView(R.id.tv_lable1)
        TextView tvLable1;
        @BindView(R.id.tv_lable2)
        TextView tvLable2;
        @BindView(R.id.tv_lable3)
        TextView tvLable3;
        @BindView(R.id.home_item_state)
        TextView tvState;
        @BindView(R.id.home_item_ranking)
        ImageView imaRanking;
        @BindView(R.id.home_item_tv)
        TextView textView;
        @BindView(R.id.staricon1)
        ImageView iamgeStaricon1;
        @BindView(R.id.staricon2)
        ImageView iamgeStaricon2;
        @BindView(R.id.staricon3)
        ImageView iamgeStaricon3;
        @BindView(R.id.staricon4)
        ImageView iamgeStaricon4;
        @BindView(R.id.staricon5)
        ImageView iamgeStaricon5;
        @BindView(R.id.home_item_tv1)
        TextView textView1;
        @BindView(R.id.home_item_tv2)
        TextView textView2;

        public ViewHodler(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    public void addOnItemOnClick(OnItemClickListener listener) {
        this.listener = listener;
    }


}
