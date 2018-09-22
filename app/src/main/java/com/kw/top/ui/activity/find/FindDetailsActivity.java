package com.kw.top.ui.activity.find;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.kw.top.R;
import com.kw.top.adapter.FindDetailsGridViewAdapter;
import com.kw.top.adapter.GiftAdapter;
import com.kw.top.base.BaseActivity;
import com.kw.top.bean.BaseBean;
import com.kw.top.bean.FindDetailsBean;
import com.kw.top.bean.GiftBean;
import com.kw.top.bean.TopCircleBean;
import com.kw.top.retrofit.Api;
import com.kw.top.retrofit.HttpHost;
import com.kw.top.tools.ComResultTools;
import com.kw.top.tools.ConstantValue;
import com.kw.top.tools.GlideTools;
import com.kw.top.ui.activity.circle.UserCircleActivity;
import com.kw.top.ui.activity.circle.WorldCircleDetailsActivity;
import com.kw.top.ui.activity.login.LoginActivity;
import com.kw.top.ui.activity.news.ChatActivity;
import com.kw.top.utils.DisplayUtils;
import com.kw.top.utils.RxToast;
import com.kw.top.utils.SPUtils;
import com.kw.top.view.ScrollGridView;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * author: zy
 * data  : 2018/5/27
 * des   : 发现详情
 */

public class FindDetailsActivity extends BaseActivity implements View.OnClickListener {

    ImageView mIvIamge;
    CircleImageView mCiHead;
    TextView mTvNickname;
    ImageView mIvVip;
    TextView mTvInfo;
    TextView mTvAddFriend;
    TextView mTvJob;
    TextView mTvEducation;
    TextView mTvLife;
    TextView mTvHeight;
    TextView mTvIncome;
    TextView mTvTreasure;
    TextView mTvSmoke;
    TextView mTvDrink;
    TextView mTvObject;
    ScrollGridView mGridView;
    ImageView mIvBack;
    TextView mTvTitle;
    TextView mTvTitleRight;
    TextView mTvSendNews;
    RelativeLayout mRelativeTitle;
    private String userId;
    private FindDetailsGridViewAdapter mAdapter;
    private List<TopCircleBean> mList = new ArrayList<>();
    private Dialog dialog;
    private GiftAdapter mGiftAdapter;
    private String receiveUserId;//添加好友id
    private String friend_status = "";//1已是好友
    private List<GiftBean> mDiamondList = new ArrayList<>();//钻石list
    private List<GiftBean> mAllGiftList = new ArrayList<>();//所有礼物list
    private String account;//环信账号
    private String nickName;
    private String head_url;


    public static void startActivity(Context context, String userid) {
        Intent intent = new Intent(context, FindDetailsActivity.class);
        intent.putExtra("USERID", userid);
        context.startActivity(intent);
    }

    @Override
    public int getContentView() {
        return R.layout.activity_find_details;
    }

    public void initView() {

        userId = getIntent().getStringExtra("USERID");
        if (TextUtils.isEmpty(userId)) {
            RxToast.normal("用户信息异常");
            return;
        }
        mGridView.setFocusable(false);
        mAdapter = new FindDetailsGridViewAdapter(this, mList);
        mGridView.setAdapter(mAdapter);
        mGiftAdapter = new GiftAdapter(this);
    }

    public void initData() {
        showProgressDialog();
        getUserDesc();
    }

    public void initListener() {
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                WorldCircleDetailsActivity.startActivity(FindDetailsActivity.this, mList.get(i).getDynamicId());
            }
        });
    }

    private void getUserDesc() {
        Api.getApiService().queryUserDesc(userId, getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Action1<BaseBean>() {
                    @Override
                    public void call(BaseBean baseBean) {
                        hideProgressDialog();
                        if (null != baseBean && baseBean.isSuccess()) {
                            FindDetailsBean findDetailsBean = null;
                            try {
                                findDetailsBean = new Gson().fromJson(baseBean.getJsonData(), new TypeToken<FindDetailsBean>() {
                                }.getType());
                            } catch (JsonSyntaxException e) {
                                e.printStackTrace();
                            }
                            if (null == findDetailsBean)return;
                            account = findDetailsBean.getAccount();
                            friend_status = findDetailsBean.getFriends();
                            fillUserInfo(findDetailsBean.getUserDesc());
                            mList.addAll(findDetailsBean.getDynamicList());
                            mAdapter.notifyDataSetChanged();
                        } else {
                            ComResultTools.resultData(FindDetailsActivity.this,baseBean);
                        }

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        hideProgressDialog();
                    }
                });
    }

    private void fillUserInfo(FindDetailsBean.UserDescBean userDesc) {
        mTvJob.setText(userDesc.getJob());
        mTvLife.setText(userDesc.getQualityLife());
        mTvIncome.setText(userDesc.getYearIncome());
        mTvSmoke.setText(userDesc.getSmoke());
        mTvDrink.setText(userDesc.getDrink());
        mTvEducation.setText(userDesc.getEducation());
        mTvHeight.setText(userDesc.getStature() + "");
        mTvTreasure.setText(userDesc.getTotalAssets());
        mTvObject.setText(userDesc.getObjective());

        nickName = userDesc.getNickName();
        head_url = HttpHost.qiNiu + userDesc.getHeadImg();
        if (userDesc.getUserId().equals(SPUtils.getString(this, ConstantValue.KEY_USER_ID,""))){
            mTvAddFriend.setVisibility(View.GONE);
        }

        String myUseId = SPUtils.getString(this, ConstantValue.KEY_USER_ID,"")+".0";
        if (userId.equals(myUseId)){
            mTvAddFriend.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(friend_status) && friend_status.equals("1")) {
            mTvAddFriend.setText("赠送礼物");
            mTvSendNews.setVisibility(View.VISIBLE);
        } else {
            mTvAddFriend.setText("添加好友");
        }

        mTvInfo.setText(userDesc.getAge() + "岁  " + userDesc.getCity() + "  " + userDesc.getConstellation());
        mTvNickname.setText(userDesc.getNickName());

        GlideTools.setVipResourceS(mIvVip, userDesc.getGrade());
        Glide.with(this)
                .asBitmap()
                .load(HttpHost.qiNiu + userDesc.getBackgroundPic())
                .apply(GlideTools.getOptions())
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        int maxWidth = DisplayUtils.getScreenWidth(FindDetailsActivity.this) ;//- DisplayUtils.dip2px(getContext(), 32)) / 2;
                        int maxHeight = DisplayUtils.dip2px(FindDetailsActivity.this,360);
                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mIvIamge.getLayoutParams();
                        int height = maxWidth * resource.getHeight() / resource.getWidth();
                        if (height>maxHeight){
                            height = maxHeight;
                            params.height = height;
                            params.width = height*resource.getWidth()/resource.getHeight();
                        }else {
                            params.height = height;
                            params.width = maxWidth;
                        }
                        mIvIamge.setLayoutParams(params);
                        mIvIamge.setImageBitmap(resource);
                    }
                });
        Glide.with(this)
                .load(HttpHost.qiNiu + userDesc.getHeadImg())
                .apply(GlideTools.getOptions())
                .into(mCiHead);
        receiveUserId = userDesc.getUserId() + "";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        setContentView(getContentView());
        mIvIamge = findViewById(R.id.iv_iamge);
        mCiHead = findViewById(R.id.ci_head);
        mTvNickname = findViewById(R.id.tv_nickname);
        mIvVip = findViewById(R.id.iv_vip);
        mTvInfo = findViewById(R.id.tv_info);
        mTvAddFriend = findViewById(R.id.tv_add_friend);
        mTvJob = findViewById(R.id.tv_job);
        mTvEducation = findViewById(R.id.tv_education);
        mTvLife = findViewById(R.id.tv_life);
        mTvHeight = findViewById(R.id.tv_height);
        mTvIncome = findViewById(R.id.tv_income);
        mTvTreasure = findViewById(R.id.tv_treasure);
        mTvSmoke = findViewById(R.id.tv_smoke);
        mTvDrink = findViewById(R.id.tv_drink);
        mGridView = findViewById(R.id.grid_view);
        mIvBack = findViewById(R.id.iv_back);
        mTvTitle = findViewById(R.id.tv_title);
        mTvTitleRight = findViewById(R.id.tv_title_right);
        mRelativeTitle = findViewById(R.id.relative_title);
        mTvObject = findViewById(R.id.tv_object);
        mTvSendNews = findViewById(R.id.tv_send_news);
        mIvBack.setOnClickListener(this);
        mTvAddFriend.setOnClickListener(this);
        mTvSendNews.setOnClickListener(this);
        mCiHead.setOnClickListener(this);
        mRelativeTitle.setBackgroundColor(getResources().getColor(R.color.transparent));
        initView();
        initData();
        initListener();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_add_friend:
                if (!TextUtils.isEmpty(receiveUserId)) {
                    if (mAllGiftList.size() == 0){
                        getGiftList();
                    }else {
                        showGiftDialog();
                    }
                }
                break;
            case R.id.tv_send_news:
                ChatActivity.startActivity(this,account,nickName,head_url,nickName);
                break;
            case R.id.ci_head:
                UserCircleActivity.startActivity(this,userId);
                break;
        }
    }

    private void showGiftDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_friend, null);
        GridView gridView = view.findViewById(R.id.grid_view);
        gridView.setAdapter(mGiftAdapter);
        mGiftAdapter.setList(friend_status.equals("1")?mAllGiftList:mDiamondList);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                dialog.dismiss();
                if (!TextUtils.isEmpty(friend_status) && friend_status.equals("1")){
                    sendGift(i);
                }else {
                    senFriend(i);
                }
            }
        });
        dialog = new Dialog(this, R.style.charge_dialog_style);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(true);
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.BOTTOM); // 此处可以设置dialog显示的位置
        window.setWindowAnimations(R.style.popupAnimation); // 添加动画
        dialog.show();
    }

    private void sendGift(int i) {
        showGiftDialog();
        Api.getApiService().sendGift(mAllGiftList.get(i).getGiftId()+"","1",receiveUserId,getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Action1<BaseBean>() {
                    @Override
                    public void call(BaseBean baseBean) {
                        hideProgressDialog();
                        if (baseBean.isSuccess()){
                            RxToast.normal("赠送成功");
                        }else {
                            ComResultTools.resultData(FindDetailsActivity.this,baseBean);
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

    private void getGiftList() {
        Api.getApiService().getAllGift(getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                    }
                })
                .subscribe(new Action1<BaseBean>() {
                    @Override
                    public void call(BaseBean baseBean) {
                        if (baseBean.isSuccess()) {
                            List<GiftBean> giftBeans = null;
                            try {
                                giftBeans = new Gson().fromJson(baseBean.getJsonData(), new TypeToken<List<GiftBean>>() {
                                }.getType());
                            } catch (JsonSyntaxException e) {
                                e.printStackTrace();
                            }
                            mAllGiftList.clear();
                            mDiamondList.clear();
                            mAllGiftList.addAll(giftBeans);
                            for (GiftBean gift : giftBeans) {
                                if (gift.getAmountType().equals("1"))
                                    mDiamondList.add(gift);
                            }
                            showGiftDialog();
                        } else {
                            ComResultTools.resultData(FindDetailsActivity.this,baseBean);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        RxToast.normal("加载礼物列表失败");
                    }
                });
    }

    private void senFriend(int i) {
        String giftid = mDiamondList.get(i).getGiftId() + "";
        String num = "1";
        showProgressDialog();
        Api.getApiService().sendGiftAddFriend(giftid, num, receiveUserId, getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                    }
                })
                .subscribe(new Action1<BaseBean>() {
                    @Override
                    public void call(BaseBean baseBean) {
                        hideProgressDialog();
                        if (null != baseBean && baseBean.isSuccess()) {
                            RxToast.normal("申请成功,请等待好友同意");
                            //参数为要添加的好友的username和添加理由
                            try {
                                EMClient.getInstance().contactManager().addContact(account, "约炮");
                            } catch (HyphenateException e) {
                                e.printStackTrace();
                            }
                        } else {
                            ComResultTools.resultData(FindDetailsActivity.this,baseBean);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        hideProgressDialog();
                    }
                });
    }
}
