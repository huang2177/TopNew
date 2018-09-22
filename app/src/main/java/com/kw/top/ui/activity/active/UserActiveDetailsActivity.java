package com.kw.top.ui.activity.active;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.kw.top.R;
import com.kw.top.adapter.CircleImageAdapter;
import com.kw.top.base.BaseActivity;
import com.kw.top.bean.BaseBean;
import com.kw.top.bean.ImageBean;
import com.kw.top.bean.UserActiveBean;
import com.kw.top.retrofit.Api;
import com.kw.top.retrofit.HttpHost;
import com.kw.top.tools.ComResultTools;
import com.kw.top.tools.GlideTools;
import com.kw.top.ui.activity.PictureActivity;
import com.kw.top.ui.activity.VideoPlayActivity;
import com.kw.top.ui.activity.find.FindDetailsActivity;
import com.kw.top.utils.DisplayUtils;
import com.kw.top.utils.RxToast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * author  ： zy
 * date    ： 2018/7/27
 * des     ：
 */

public class UserActiveDetailsActivity extends BaseActivity {
    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_title_right)
    TextView mTvTitleRight;
    @BindView(R.id.relative_title)
    RelativeLayout mRelativeTitle;
    @BindView(R.id.ci_head)
    CircleImageView mCiHead;
    @BindView(R.id.tv_nickname)
    TextView mTvNickname;
    @BindView(R.id.iv_vip_grade)
    ImageView mIvVipGrade;
    @BindView(R.id.iv_like)
    ImageView mIvLike;
    @BindView(R.id.tv_content)
    TextView mTvContent;
    @BindView(R.id.iv_circle_iamge)
    ImageView mIvCircleIamge;
    @BindView(R.id.iv_video)
    ImageView mIvVideo;
    @BindView(R.id.recycler_view_image)
    RecyclerView mRecyclerViewImage;
    @BindView(R.id.rl_pager)
    RelativeLayout mRlPager;
    @BindView(R.id.tv_date)
    TextView mTvDate;
    @BindView(R.id.tv_diamond_num)
    TextView mTvDiamondNum;
    @BindView(R.id.tv_like_num)
    TextView mTvLikeNum;

    private String picOrVideoType;//   0.图片  1.视频
    private String activeId;
    private String userId;
    private List<ImageBean> mImageList = new ArrayList<>();
    private CircleImageAdapter mImageAdapter;
    private int maxheight, maxWidth;
    private boolean play_video;
    private String image_path;

    public static void startActivity(Context context, String userId, String activeId) {
        Intent intent = new Intent(context, UserActiveDetailsActivity.class);
        intent.putExtra("userID", userId);
        intent.putExtra("activeID", activeId);
        context.startActivity(intent);
    }


    @Override
    public int getContentView() {
        return R.layout.activity_user_activite;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);

        activeId = getIntent().getStringExtra("activeID");
        userId = getIntent().getStringExtra("userID");
        mTvTitle.setText("详情");
        mImageAdapter = new CircleImageAdapter(UserActiveDetailsActivity.this, mImageList);
        mRecyclerViewImage.setLayoutManager(new GridLayoutManager(UserActiveDetailsActivity.this, 3));
        mRecyclerViewImage.setAdapter(mImageAdapter);
        maxheight = DisplayUtils.dip2px(this, 300);
        maxWidth = DisplayUtils.dip2px(this, 200);
        getActiveData();
        initListener();
    }

    private void initListener() {
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getActiveData() {
        showProgressDialog();
        Api.getApiService().userActiveDetails(userId, activeId, getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Action1<BaseBean>() {
                    @Override
                    public void call(BaseBean baseBean) {
                        hideProgressDialog();
                        if (baseBean.isSuccess()) {
                            UserActiveBean userActiveBean = null;
                            try {
                                userActiveBean = new Gson().fromJson(baseBean.getJsonData(), new TypeToken<UserActiveBean>() {
                                }.getType());

                                if (userActiveBean == null) return;
                                if (null == userActiveBean.getActivityRelation()) return;
                                fillData(userActiveBean.getActivityRelation());

                                if (mImageList.size() > 0) return;

                                picOrVideoType = userActiveBean.getActivityRelation().getPicOrVideoType();
                                if (picOrVideoType.equals("1"))
                                    mIvVideo.setVisibility(View.VISIBLE);
                                for (UserActiveBean.ActivityRelationBean.ActivityPicBean picBean :
                                        userActiveBean.getActivityRelation().getActivityPicList()) {
                                    mImageList.add(new ImageBean(picBean.getActivityPic()));
                                }
                                if (mImageList.size() > 1) {
                                    mIvCircleIamge.setVisibility(View.GONE);
                                    mRecyclerViewImage.setVisibility(View.VISIBLE);
                                    mImageAdapter.notifyDataSetChanged();
                                } else if (mImageList.size() == 1){
                                    mIvCircleIamge.setVisibility(View.VISIBLE);
                                    mRecyclerViewImage.setVisibility(View.GONE);
                                    image_path = mImageList.get(0).getDynamicPic();

                                    Glide.with(UserActiveDetailsActivity.this).asBitmap()
                                            .load(HttpHost.qiNiu + image_path)
                                            .apply(GlideTools.getOptions())
                                            .listener(new RequestListener<Bitmap>() {
                                                @Override
                                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {

                                                    return false;
                                                }

                                                @Override
                                                public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                                                    return false;
                                                }
                                            }).into(new SimpleTarget<Bitmap>() {
                                        @Override
                                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mIvCircleIamge.getLayoutParams();
                                            params.width = maxWidth;
                                            int height = maxWidth * resource.getHeight() / resource.getWidth();
                                            if (height > maxheight) {
                                                params.height = maxheight;
                                            } else {
                                                params.height = height;
                                            }
                                            mIvCircleIamge.setLayoutParams(params);
                                            mIvCircleIamge.setImageBitmap(resource);
                                        }
                                    });
                                }
                            } catch (JsonSyntaxException e) {
                                e.printStackTrace();
                            }
                        } else {
                            ComResultTools.resultData(UserActiveDetailsActivity.this, baseBean);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        hideProgressDialog();
                    }
                });
    }

    private void fillData(final UserActiveBean.ActivityRelationBean bean) {
        mTvNickname.setText(bean.getNickName());
//        mTvContent.setText(bean.getTextContent());
//        mTvDate.setText(circleBean.getCreateTime());
//        mTvDiamondNum.setText(circleBean.getThumbsNum());
        mTvLikeNum.setText(bean.getThumbsUpNum());
        Glide.with(this)
                .load(HttpHost.qiNiu + bean.getHeadImg())
                .apply(GlideTools.getHeadOptions())
                .into(mCiHead);
        mCiHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FindDetailsActivity.startActivity(UserActiveDetailsActivity.this, bean.getUserId() + "");
            }
        });

        //设置margin
//        if (TextUtils.isEmpty(circleBean.getTextContent())) {
//            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mRlPager.getLayoutParams();
//            params.setMargins(0, -DisplayUtils.dip2px(this, 15), 0, 0);
//            mRlPager.setLayoutParams(params);
//        }
    }

    @OnClick({R.id.iv_circle_iamge, R.id.iv_like})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_circle_iamge:
                if (TextUtils.isEmpty(image_path))
                    return;
                if (picOrVideoType.equals("1")) {
//                    AppVideoPlayActivity.startActivity(this,HttpHost.qiNiu + image_path);
                    VideoPlayActivity.startActivity(this, HttpHost.qiNiu + image_path);
                } else {
                    PictureActivity.startActivity(this, image_path);
                }
                break;
            case R.id.iv_like:
                showLikeDialog();
                break;
        }
    }

    private EditText mEditText;
    private Dialog mLikeDialog;
    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mLikeDialog.dismiss();
            String num = "";
            if (null != mEditText) {
                num = mEditText.getText().toString();
            }
            if (!TextUtils.isEmpty(num)) {
                addLike(num);
            }
        }
    };

    private void showLikeDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_like, null);
        view.findViewById(R.id.tv_award).setOnClickListener(mOnClickListener);
        mEditText = view.findViewById(R.id.et_num);
        mLikeDialog = new Dialog(UserActiveDetailsActivity.this, R.style.charge_dialog_style);
        mLikeDialog.setContentView(view);
        mLikeDialog.setCanceledOnTouchOutside(true);
        Window window = mLikeDialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.BOTTOM); // 此处可以设置dialog显示的位置
        window.setWindowAnimations(R.style.popupAnimation); // 添加动画
        mLikeDialog.show();
    }

    private void addLike(final String thumbsUpNum) {
        showProgressDialog();
        Api.getApiService().activeAward(activeId, userId, thumbsUpNum, getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Action1<BaseBean>() {
                    @Override
                    public void call(BaseBean baseBean) {
                        hideProgressDialog();
                        if (baseBean.isSuccess()) {
                            RxToast.normal("点赞成功");
//                            int num = Integer.parseInt(mTvLikeNum.getText().toString());
//                            num += Integer.parseInt(thumbsUpNum);
//                            mTvLikeNum.setText(num+"");
                            getActiveData();
                        } else {
                            RxToast.normal(baseBean.getMsg() + "");
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        hideProgressDialog();
                        RxToast.normal("点赞失败");
                    }
                });
    }
}
