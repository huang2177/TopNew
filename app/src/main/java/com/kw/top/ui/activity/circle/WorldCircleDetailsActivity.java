package com.kw.top.ui.activity.circle;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
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
import com.kw.top.adapter.CommentAdapter;
import com.kw.top.base.BaseActivity;
import com.kw.top.bean.BaseBean;
import com.kw.top.bean.CircleDetailsBean;
import com.kw.top.bean.CommentBean;
import com.kw.top.bean.ImageBean;
import com.kw.top.bean.TopCircleBean;
import com.kw.top.bean.event.CircleRefreshEvent;
import com.kw.top.listener.OnDeleteListener;
import com.kw.top.listener.OnItemClickListener;
import com.kw.top.retrofit.Api;
import com.kw.top.retrofit.HttpHost;
import com.kw.top.tools.ConstantValue;
import com.kw.top.tools.GlideTools;
import com.kw.top.ui.activity.PictureActivity;
import com.kw.top.ui.activity.VideoPlayActivity;
import com.kw.top.ui.activity.find.FindDetailsActivity;
import com.kw.top.ui.activity.login.LoginActivity;
import com.kw.top.utils.DisplayUtils;
import com.kw.top.utils.RxToast;
import com.kw.top.utils.SPUtils;
import com.kw.top.utils.TimeUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * author: zy
 * data  : 2018/5/27
 * des   : 世界圈详情
 */

public class WorldCircleDetailsActivity extends BaseActivity implements OnDeleteListener,OnItemClickListener{

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
    @BindView(R.id.recycler_view_image)
    RecyclerView mRecyclerViewImage;
    @BindView(R.id.rl_pager)
    RelativeLayout mRlPager;
    @BindView(R.id.tv_date)
    TextView mTvDate;
    @BindView(R.id.tv_diamond_num)
    TextView mTvDiamondNum;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.et_commnet)
    EditText mEtCommnet;
    @BindView(R.id.iv_send)
    ImageView mIvSend;
    @BindView(R.id.iv_video)
    ImageView mIvVideo;
    private String id;
    private String commentContent, dynamicId, retUserid;
    private String retUserName;
    private List<CommentBean> mList = new ArrayList<>();
    private CommentAdapter mAdapter;
    private Dialog mLikeDialog;
    private List<ImageBean> mImageList = new ArrayList<>();
    private String myUserId;
    private CircleImageAdapter mImageAdapter;
    private int maxheight, maxWidth;
    private boolean play_video;
    private String image_path;
    private String dynamicUserId;//动态圈的用户ID
    View mRootView;
    int rootViewVisibleHeight;//纪录根视图的显示高度


    public static void startActivity(Context context, String id) {
        Intent intent = new Intent(context, WorldCircleDetailsActivity.class);
        intent.putExtra("ID", id);
        context.startActivity(intent);
    }

    @Override
    public int getContentView() {
        return R.layout.activity_world_circle_details;
    }

    public void initView() {
        mTvTitle.setText("详情");
        mTvTitleRight.setText("删除");
        mTvTitleRight.setVisibility(View.GONE);
        id = getIntent().getStringExtra("ID");
        if (TextUtils.isEmpty(id)) {
            RxToast.normal("数据异常,请稍后再试");
            return;
        }
        maxheight = DisplayUtils.dip2px(this, 300);
        maxWidth = DisplayUtils.dip2px(this, 200);
        dynamicId = (int)Double.parseDouble(id)+"";
        mAdapter = new CommentAdapter(this, mList,this,this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);

        mImageAdapter = new CircleImageAdapter(WorldCircleDetailsActivity.this, mImageList);
        mRecyclerViewImage.setLayoutManager(new GridLayoutManager(WorldCircleDetailsActivity.this, 3));
        mRecyclerViewImage.setAdapter(mImageAdapter);
        mTvTitleRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(WorldCircleDetailsActivity.this)
                        .setTitle("提示")
                        .setMessage("确定删除么?")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                deleteCircle();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
            }
        });
        myUserId = SPUtils.getString(this, ConstantValue.KEY_USER_ID, "");

        initListener();
    }

    public void initData() {
        showProgressDialog();
        Api.getApiService().dynamicDesc(id, getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnUnsubscribe(new Action0() {
                    @Override
                    public void call() {
                    }
                })
                .subscribe(new Action1<BaseBean>() {
                    @Override
                    public void call(BaseBean baseBean) {
                        hideProgressDialog();
                        if (baseBean.isSuccess()) {
                            CircleDetailsBean circleDetailsBean = null;
                            try {
                                circleDetailsBean = new Gson().fromJson(baseBean.getJsonData(), new TypeToken<CircleDetailsBean>() {
                                }.getType());
                            } catch (JsonSyntaxException e) {
                                e.printStackTrace();
                            }
                            if (null == circleDetailsBean) return;
                            mList.clear();
                            mList.addAll(circleDetailsBean.getCommentList());
                            mAdapter.notifyDataSetChanged();

                            if (mImageList.size() == 0) {
                                fillData(circleDetailsBean.getDynamicMap());
                                mImageList.addAll(circleDetailsBean.getPicList());
                                if (mImageList.size() > 1) {
                                    mIvCircleIamge.setVisibility(View.GONE);
                                    mRecyclerViewImage.setVisibility(View.VISIBLE);
                                    mImageAdapter.notifyDataSetChanged();
                                } else {
                                    mIvCircleIamge.setVisibility(View.VISIBLE);
                                    mRecyclerViewImage.setVisibility(View.GONE);
                                    if (mImageList.size() > 0) {
                                        //图片
                                        image_path = mImageList.get(0).getDynamicPic();
                                    } else {
                                        //视频
                                        if (circleDetailsBean.getVideoList().size() > 0) {
                                            image_path = circleDetailsBean.getVideoList().get(0).getDynamicVideo();
                                            mIvVideo.setVisibility(View.VISIBLE);
                                            play_video = true;
                                        }
                                    }
                                    Log.e("tag","========= world url "+ HttpHost.qiNiu + image_path);
                                    Glide.with(WorldCircleDetailsActivity.this).asBitmap()
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
                            }
                        } else if (baseBean.getCode().equals("-44")) {
                            RxToast.normal(getResources().getString(R.string.login_out));
                            SPUtils.clear(WorldCircleDetailsActivity.this);
                            startActivity(LoginActivity.class);
                            finish();
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        hideProgressDialog();
                    }
                });
    }

    private void fillData(final TopCircleBean circleBean) {
        dynamicUserId = circleBean.getUserId();

        mTvNickname.setText(circleBean.getNickName());
        mTvContent.setText(circleBean.getTextContent());
        mTvDate.setText(circleBean.getCreateTime());
        mTvDiamondNum.setText(circleBean.getThumbsNum());
        if (!TextUtils.isEmpty(myUserId) && myUserId.equals(circleBean.getUserId()) || ((myUserId+".0").equals(circleBean.getUserId()))) {
            mTvTitleRight.setVisibility(View.VISIBLE);
        }
        Glide.with(this)
                .load(HttpHost.qiNiu + circleBean.getHeadImg())
                .apply(GlideTools.getHeadOptions())
                .into(mCiHead);
        mCiHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FindDetailsActivity.startActivity(WorldCircleDetailsActivity.this, circleBean.getUserId());
            }
        });

        //设置margin
        if (TextUtils.isEmpty(circleBean.getTextContent())) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mRlPager.getLayoutParams();
            params.setMargins(0, -DisplayUtils.dip2px(this, 15), 0, 0);
            mRlPager.setLayoutParams(params);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        setContentView(getContentView());
        ButterKnife.bind(this);
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initView();
        initData();
    }

    @OnClick({R.id.iv_send, R.id.iv_like, R.id.iv_circle_iamge})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_send:
                commentContent = mEtCommnet.getText().toString().trim();
                if (TextUtils.isEmpty(commentContent)) {
                    RxToast.normal("请输入评论内容");
                } else {
                    sendComment();
                }
                break;
            case R.id.iv_like:
                showLikeDialog();
                break;
            case R.id.iv_circle_iamge:
                if (TextUtils.isEmpty(image_path))
                    return;
                if (play_video) {
//                    AppVideoPlayActivity.startActivity(this,HttpHost.qiNiu + image_path);
                    VideoPlayActivity.startActivity(this, HttpHost.qiNiu + image_path);
                } else {
                    PictureActivity.startActivity(this, image_path);
                }
                break;
        }

    }


    private void sendComment() {
        showProgressDialog();
        Api.getApiService().sendCommnet(dynamicId, retUserid, commentContent, getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Action1<BaseBean>() {
                    @Override
                    public void call(BaseBean baseBean) {
                        hideProgressDialog();
                        mEtCommnet.setText("");
//                        CommentBean commentBean = new CommentBean();
//                        commentBean.setComTime(TimeUtils.getTime(System.currentTimeMillis(), "yyyy年MM月dd日 HH:mm"));
//                        commentBean.setComContent(commentContent);
//                        commentBean.setComHeadImg(SPUtils.getString(WorldCircleDetailsActivity.this, ConstantValue.KEY_HEAD, ""));
//                        commentBean.setComNickName(SPUtils.getString(WorldCircleDetailsActivity.this, ConstantValue.KEY_NAME, ""));
//                        commentBean.setComUserid(myUserId);
//                        commentBean.setRetUserid(retUserid);
//                        commentBean.setRetNickName(retUserName);
//                        mList.add(commentBean);
//                        mAdapter.notifyDataSetChanged();
//                        mRecyclerView.smoothScrollToPosition(mList.size() - 1);
                        RxToast.normal("评论成功");
                        initData();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        hideProgressDialog();
                    }
                });
    }

    private String num;
    private EditText mEditText;

    private void showLikeDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_like, null);
        view.findViewById(R.id.tv_award).setOnClickListener(mOnClickListener);
        mEditText = view.findViewById(R.id.et_num);
        mLikeDialog = new Dialog(this, R.style.charge_dialog_style);
        mLikeDialog.setContentView(view);
        mLikeDialog.setCanceledOnTouchOutside(true);
        Window window = mLikeDialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.BOTTOM); // 此处可以设置dialog显示的位置
        window.setWindowAnimations(R.style.popupAnimation); // 添加动画
        mLikeDialog.show();
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mLikeDialog.dismiss();
            if (null != mEditText) {
                num = mEditText.getText().toString();
            }
            if (!TextUtils.isEmpty(num)) {
                addLike();
            }
        }
    };

    private void addLike() {
        showProgressDialog();
        Api.getApiService().addGood(id, num, getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Action1<BaseBean>() {
                    @Override
                    public void call(BaseBean baseBean) {
                        hideProgressDialog();
                        if (baseBean.isSuccess()) {
                            RxToast.normal("赞赏成功");
                            initData();
                        } else if (baseBean.getCode().equals("-44")) {
                            RxToast.normal(getResources().getString(R.string.login_out));
                            SPUtils.clear(WorldCircleDetailsActivity.this);
                            startActivity(LoginActivity.class);
                            finish();
                        } else {
                            RxToast.normal(baseBean.getMsg());
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        hideProgressDialog();
                        RxToast.normal(getResources().getString(R.string.net_error));
                    }
                });

    }

    private void deleteCircle() {
        showProgressDialog();
        Api.getApiService().deleteDynamic(dynamicId, getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Action1<BaseBean>() {
                    @Override
                    public void call(BaseBean baseBean) {
                        hideProgressDialog();
                        if (baseBean.isSuccess()) {
                            RxToast.normal("删除成功");
                            EventBus.getDefault().post(new CircleRefreshEvent(true));
                            WorldCircleDetailsActivity.this.finish();
                        } else if (baseBean.getCode().equals("-44")) {
                            RxToast.normal(getResources().getString(R.string.login_out));
                            SPUtils.clear(WorldCircleDetailsActivity.this);
                            startActivity(LoginActivity.class);
                            finish();
                        } else {
                            RxToast.normal(baseBean.getMsg());
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        hideProgressDialog();
                        RxToast.normal(getResources().getString(R.string.net_error));
                    }
                });
    }

    @Override
    public void onDelete(View view, int position) {
        String commendUserId = mList.get(position).getComUserid();//评论的用户ID
        if (TextUtils.equals(myUserId,dynamicUserId)){
            showDeleteDialog(true,position);
        }else if (TextUtils.equals(myUserId,commendUserId)){
            showDeleteDialog(false,position);
        }
    }

    private void showDeleteDialog(final boolean create, final int position){
        new AlertDialog.Builder(this).setTitle("")
                .setMessage("删除评论?")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setPositiveButton("删除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (create){//是否是发朋友圈的人
                    deleteOtherCom(position);
                }else {
                    deletePersonCom(position);
                }
            }
        }).show();
    }

    private void deletePersonCom(int position){
        showProgressDialog();
        final int id = mList.get(position).getId();
        Api.getApiService().deletePersonComment(id+"",getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Action1<BaseBean>() {
                    @Override
                    public void call(BaseBean baseBean) {
                        hideProgressDialog();
                        if (baseBean.isSuccess()){
                            Iterator<CommentBean> iterator = mList.iterator();
                            while(iterator.hasNext()){
                                CommentBean bean = iterator.next();
                                if(bean.getId()==id)
                                    iterator.remove();   //注意这个地方
                            }
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        hideProgressDialog();
                    }
                });
    }

    private void deleteOtherCom(int position){
        showProgressDialog();
        final int id = mList.get(position).getId();
        Api.getApiService().deleteOthersComment(id+"",dynamicId,getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Action1<BaseBean>() {
                    @Override
                    public void call(BaseBean baseBean) {
                        hideProgressDialog();
                        if (baseBean.isSuccess()){
                            Iterator<CommentBean> iterator = mList.iterator();
                            while(iterator.hasNext()){
                                CommentBean bean = iterator.next();
                                if(bean.getId()==id)
                                    iterator.remove();   //注意这个地方
                            }
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        hideProgressDialog();
                    }
                });
    }

    @Override
    public void onItemClick(View view, int position) {
        retUserid = mList.get(position).getComUserid();
        retUserName = mList.get(position).getRetNickName();
        mEtCommnet.setText("");
        mEtCommnet.setHint("回复："+mList.get(position).getComNickName());
        showKeyboard();
    }

    public void showKeyboard() {
        mEtCommnet.setFocusable(true);
        mEtCommnet.setFocusableInTouchMode(true);
        mEtCommnet.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void initListener() {
        mRootView = getWindow().getDecorView();
        //监听视图树中全局布局发生改变或者视图树中的某个视图的可视状态发生改变
        mRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //获取当前根视图在屏幕上显示的大小
                Rect r = new Rect();
                mRootView.getWindowVisibleDisplayFrame(r);

                int visibleHeight = r.height();
                System.out.println(""+visibleHeight);
                if (rootViewVisibleHeight == 0) {
                    rootViewVisibleHeight = visibleHeight;
                    return;
                }

                //根视图显示高度没有变化，可以看作软键盘显示／隐藏状态没有改变
                if (rootViewVisibleHeight == visibleHeight) {
                    return;
                }

                //根视图显示高度变小超过200，可以看作软键盘显示了
                if (rootViewVisibleHeight - visibleHeight > 200) {
                    rootViewVisibleHeight = visibleHeight;
                    return;
                }

                //根视图显示高度变大超过200，可以看作软键盘隐藏了
                if (visibleHeight - rootViewVisibleHeight > 200) {
                    rootViewVisibleHeight = visibleHeight;
                    mEtCommnet.setText("");
                    mEtCommnet.setHint("");
                    retUserid = "";
                    retUserName = "";
                    return;
                }

            }
        });
    }
}
