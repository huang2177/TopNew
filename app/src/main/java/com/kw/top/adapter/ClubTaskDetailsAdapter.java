package com.kw.top.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.kw.top.R;
import com.kw.top.bean.ClubTaskDetailsBean;
import com.kw.top.bean.ImageBean;
import com.kw.top.retrofit.HttpHost;
import com.kw.top.tools.GlideTools;
import com.kw.top.ui.activity.ImageDetailsActivity;
import com.kw.top.ui.activity.VideoPlayActivity;
import com.kw.top.ui.activity.find.FindDetailsActivity;
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

public class ClubTaskDetailsAdapter extends RecyclerView.Adapter<ClubTaskDetailsAdapter.ViewHolder> {

    private Activity mContext;
    private List<ClubTaskDetailsBean> mList = new ArrayList<>();
    private int mMaxWidth, maxHeight;
    private int marginTop;
    private String imageUrl;

    public ClubTaskDetailsAdapter(Activity context, List<ClubTaskDetailsBean> list) {
        this.mContext = context;
        this.mList = list;
        mMaxWidth = DisplayUtils.dip2px(mContext, 150);
        maxHeight = DisplayUtils.dip2px(mContext, 230);
        marginTop = DisplayUtils.dip2px(mContext, 15);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_clubt_task_details, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final ClubTaskDetailsBean dataBean = mList.get(position);
        holder.mTvNickname.setText(dataBean.getNickName());
        holder.mTvContent.setText(dataBean.getDescribes());
        holder.mTvDes.setText(dataBean.getCreateTime());

        Glide.with(mContext)
                .load(HttpHost.qiNiu + dataBean.getHeadImg())
                .apply(GlideTools.getHeadOptions())
                .into(holder.mCiHead);

        final List<ImageBean> mImageList = new ArrayList<>();
        for (ClubTaskDetailsBean.PicListBean bean : dataBean.getPicList()) {
            mImageList.add(new ImageBean(bean.getTaskPic()));
        }
        if (mImageList.size() > 1) {
            holder.mIvCircle.setVisibility(View.GONE);
            holder.mRecyclerView.setVisibility(View.VISIBLE);
            holder.mIvVideo.setVisibility(View.GONE);
            //设置9宫格
            CircleImageAdapter imageAdapter = new CircleImageAdapter(mContext, mImageList);
            holder.mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
            holder.mRecyclerView.setAdapter(imageAdapter);
        } else {
            holder.mIvCircle.setVisibility(View.VISIBLE);
            holder.mRecyclerView.setVisibility(View.GONE);
            if (mImageList.size() == 1) {
                //图片
                final ImageBean imageBean = mImageList.get(0);
                imageUrl = imageBean.getDynamicPic();
                holder.mIvVideo.setVisibility(View.GONE);
                holder.mIvCircle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ImageDetailsActivity.startActivity(mContext, 0, mImageList);
                    }
                });
            } else {
                //视频
                if (dataBean.getVideoList() != null && dataBean.getVideoList().size() > 0) {
                    imageUrl = dataBean.getVideoList().get(0).getTaskVideo();
                    holder.mIvVideo.setVisibility(View.VISIBLE);
                    holder.mIvCircle.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            VideoPlayActivity.startActivity(mContext, HttpHost.qiNiu + imageUrl);
                        }
                    });
                } else {
                    imageUrl = "";
                }
            }
            Glide.with(mContext)
                    .asBitmap()
                    .load(HttpHost.qiNiu + imageUrl)
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
        if (TextUtils.isEmpty(dataBean.getDescribes())) {
            params.setMargins(0, -marginTop, 0, 0);
        } else {
            params.setMargins(0, marginTop, 0, 0);
        }
        holder.mRlImage.setLayoutParams(params);

        holder.mCiHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FindDetailsActivity.startActivity(mContext, dataBean.getUserId() + "");
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
        @BindView(R.id.iv_video)
        ImageView mIvVideo;
        @BindView(R.id.recycler_view_image)
        RecyclerView mRecyclerView;
        @BindView(R.id.rl_image)
        RelativeLayout mRlImage;
        @BindView(R.id.tv_des)
        TextView mTvDes;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
