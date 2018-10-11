package com.kw.top.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
import com.kw.top.bean.ImageBean;
import com.kw.top.bean.TopCircleBean;
import com.kw.top.retrofit.HttpHost;
import com.kw.top.tools.ConstantValue;
import com.kw.top.tools.GlideTools;
import com.kw.top.ui.activity.ImageDetailsActivity;
import com.kw.top.ui.activity.VideoPlayActivity;
import com.kw.top.ui.activity.circle.UserCircleActivity;
import com.kw.top.ui.activity.circle.WorldCircleDetailsActivity;
import com.kw.top.ui.activity.find.FindDetailsActivity;
import com.kw.top.ui.fragment.find.HomePageDetailsActivity;
import com.kw.top.utils.DisplayUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * author: zy
 * data  : 2018/5/27
 * des   :
 */

public class TopCircleAdapter extends RecyclerView.Adapter<TopCircleAdapter.ViewHolder> {

    private Activity mContext;
    private List<TopCircleBean> mList = new ArrayList<>();
    private int mMaxWidth, maxHeight;
    private int marginTop;

    public TopCircleAdapter(Activity context, List<TopCircleBean> list) {
        this.mContext = context;
        this.mList = list;
        mMaxWidth = DisplayUtils.dip2px(mContext, 150);
        maxHeight = DisplayUtils.dip2px(mContext, 230);
        marginTop = DisplayUtils.dip2px(mContext, 15);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_top_circle, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final TopCircleBean dataBean = mList.get(position);
        holder.mTvNickname.setText(dataBean.getNickName());
        holder.mTvContent.setText(dataBean.getTextContent());
        holder.mTvDes.setText(dataBean.getCreateTime());
        holder.mTvNumDiamon.setText(dataBean.getThumbsNum());
//        holder.mTvZanNum.setText("赞了该条TOP圈" + dataBean.getThumbsUpNum() + "次");
        holder.mTvZanNum.setText("获得了" + dataBean.getThumbsUpNum() + "个赞");

        Glide.with(mContext)
                .load(HttpHost.qiNiu + dataBean.getHeadImg())
                .apply(GlideTools.getHeadOptions())
                .into(holder.mCiHead);
        final List<ImageBean> mImageList = dataBean.getDynamicList();
        if (mImageList.size() > 1) {
            holder.mIvCircle.setVisibility(View.GONE);
            holder.mRecyclerView.setVisibility(View.VISIBLE);
            holder.mIvVideo.setVisibility(View.GONE);
            //设置9宫格
            CircleImageAdapter imageAdapter = new CircleImageAdapter(mContext, mImageList);
            holder.mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
            holder.mRecyclerView.setAdapter(imageAdapter);
        } else if (mImageList.size() == 1) {
            holder.mIvCircle.setVisibility(View.VISIBLE);
            holder.mRecyclerView.setVisibility(View.GONE);
            final ImageBean imageBean = mImageList.get(0);
            if (imageBean.getPicOrVideoType().equals("0")) {
                holder.mIvVideo.setVisibility(View.VISIBLE);
                holder.mIvCircle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        VideoPlayActivity.startActivity(mContext, HttpHost.qiNiu + imageBean.getDynamicPic());
                    }
                });
            } else {
                holder.mIvVideo.setVisibility(View.GONE);
                holder.mIvCircle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ImageDetailsActivity.startActivity(mContext, 0, mImageList);
                    }
                });
            }
            Log.e("tag", "========= top circle  " + HttpHost.qiNiu + imageBean.getDynamicPic());
            Glide.with(mContext)
                    .asBitmap()
                    .load(HttpHost.qiNiu + imageBean.getDynamicPic())
                    .apply(GlideTools.getOptions())
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.mIvCircle.getLayoutParams();
                            params.width = mMaxWidth;
                            int height = mMaxWidth * resource.getHeight() / resource.getWidth();
                            if (height > maxHeight) {
                                params.height = maxHeight;
                            } else {
                                params.height = height;
                            }
                            holder.mIvCircle.setLayoutParams(params);
                            holder.mIvCircle.setImageBitmap(resource);
                        }
                    });
        }

        //设置margin
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.mRlImage.getLayoutParams();
        if (TextUtils.isEmpty(dataBean.getTextContent())) {
            params.setMargins(0, -marginTop, 0, 0);
        } else {
            params.setMargins(0, marginTop, 0, 0);
        }
        holder.mRlImage.setLayoutParams(params);

        holder.mCiHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(mContext, HomePageDetailsActivity.class);
                intent.putExtra(ConstantValue.KEY_USER_ID, dataBean.getUserId());
                mContext.startActivity(intent);
            }
        });
        holder.mLlItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WorldCircleDetailsActivity.startActivity(mContext, dataBean.getDynamicId());
            }
        });
        holder.mRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    holder.mLlItem.performClick();
                }
                return false;
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
        @BindView(R.id.tv_vip)
        ImageView mTvVip;
        @BindView(R.id.tv_content)
        TextView mTvContent;
        @BindView(R.id.iv_circle)
        ImageView mIvCircle;
        @BindView(R.id.rl_image)
        RelativeLayout mRlImage;
        @BindView(R.id.tv_des)
        TextView mTvDes;
        @BindView(R.id.tv_num_diamon)
        TextView mTvNumDiamon;
        @BindView(R.id.tv_zan_num)
        TextView mTvZanNum;
        @BindView(R.id.rl_zan)
        LinearLayout mRlZan;
        @BindView(R.id.ll_item)
        LinearLayout mLlItem;
        @BindView(R.id.recycler_view_image)
        RecyclerView mRecyclerView;
        @BindView(R.id.iv_video)
        ImageView mIvVideo;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
