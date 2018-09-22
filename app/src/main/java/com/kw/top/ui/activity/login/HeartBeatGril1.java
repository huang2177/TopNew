package com.kw.top.ui.activity.login;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.kw.top.R;
import com.kw.top.adapter.HeartBeatAdapter;
import com.kw.top.base.BaseActivity;
import com.kw.top.bean.AllUserBean;
import com.kw.top.bean.BaseBean;
import com.kw.top.retrofit.Api;
import com.kw.top.retrofit.HttpHost;
import com.kw.top.tools.ComResultTools;
import com.kw.top.tools.GlideTools;
import com.kw.top.utils.RxToast;
import com.kw.top.utils.SPUtils;
import com.kw.top.view.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
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
 * data  : 2018/5/17
 * des   :
 */

public class HeartBeatGril1 extends BaseActivity implements SwipeFlingAdapterView.onFlingListener, View.OnClickListener {

    ImageView mIvBack;
    TextView mTvTitle;
    TextView mTvTitleRight;
    RelativeLayout mRelativeTitle;
    CircleImageView mCi1;
    CircleImageView mCi2;
    CircleImageView mCi3;
    CircleImageView mCi4;
    CircleImageView mCi5;
    CircleImageView mCi6;
    CircleImageView mCi7;
    CircleImageView mCi8;
    CircleImageView mCi9;
    TextView mTvLikeNum;
    RelativeLayout mLlHeads;
    SwipeFlingAdapterView mSwipeFlingView;
    LinearLayout mLlSwipeLift;
    LinearLayout mLlSwipeRight;
    private HeartBeatAdapter mAdapter;
    private ArrayList<AllUserBean> mList = new ArrayList<>();
    private int nowPage = 1;
    private String pageNum = "10";
    private List<AllUserBean> likeImages = new ArrayList<>();
    private RequestOptions options;

    @Override
    public int getContentView() {
        return R.layout.activity_heart_gril1;
    }

    public void initView() {
        mTvTitle.setText("选择10位你想认识的女生");
        mTvTitle.setTextColor(getResources().getColor(R.color.yellow_DEAC6A));
        if (mSwipeFlingView != null) {
            mSwipeFlingView.setIsNeedSwipe(true);
            mSwipeFlingView.setFlingListener(this);
//            mSwipeFlingView.setOnItemClickListener(this);

            mAdapter = new HeartBeatAdapter(this);
            mSwipeFlingView.setAdapter(mAdapter);
        }
    }

    public void initData() {
        loadData();
        options = GlideTools.getHeadOptions();
    }

    @Override
    public void removeFirstObjectInAdapter() {
        mAdapter.remove(0);
    }

    @Override
    public void onLeftCardExit(Object dataObject) {
        Log.e("tag", "=============  不喜欢");
    }

    @Override
    public void onRightCardExit(Object dataObject) {
        Log.e("tag", "============== 喜欢");
        AllUserBean allUserBean = (AllUserBean) dataObject;
        likeImages.add(allUserBean);
        mTvLikeNum.setText(likeImages.size() + "/10");
        setheard();
        addLike(allUserBean.getUserId());
        if (likeImages.size() >= 10) {
            HeartBeatGril2.startActivity(HeartBeatGril1.this, likeImages);
            finish();
        }
    }

    @Override
    public void onAdapterAboutToEmpty(int itemsInAdapter) {
        if (itemsInAdapter == 2) {
            nowPage++;
            loadData();
        }
    }

    @SuppressLint("StaticFieldLeak")
    private void loadData() {
        //加载数据
        showProgressDialog();
        Api.getApiService().getAllUserList("", "","", "", "", "0", nowPage + "", pageNum, getToken(), "", "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Action1<BaseBean>() {
                    @Override
                    public void call(BaseBean baseBean) {
                        hideProgressDialog();
                        if (baseBean.isSuccess()) {
                            List<AllUserBean> userBeanList = null;
                            try {
                                userBeanList = new Gson().fromJson(baseBean.getJsonData(), new TypeToken<List<AllUserBean>>() {
                                }.getType());
                            } catch (JsonSyntaxException e) {
                                e.printStackTrace();
                            }
                            if (userBeanList.size() < 10) {
                                nowPage = 1;
                            }
                            mList.clear();
                            mList.addAll(userBeanList);
                            mAdapter.addAll(mList);
                        } else {
                            ComResultTools.resultData(HeartBeatGril1.this, baseBean);
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
    public void onScroll(float progress, float scrollXProgress) {

    }

    private void setheard() {
        switch (likeImages.size()) {
            case 1:
                Glide.with(this).load(HttpHost.qiNiu + likeImages.get(0).getHeadImg()).apply(options).into(mCi1);
                break;
            case 2:
                Glide.with(this).load(HttpHost.qiNiu + likeImages.get(1).getHeadImg()).apply(options).into(mCi2);
                break;
            case 3:
                Glide.with(this).load(HttpHost.qiNiu + likeImages.get(2).getHeadImg()).apply(options).into(mCi3);
                break;
            case 4:
                Glide.with(this).load(HttpHost.qiNiu + likeImages.get(3).getHeadImg()).apply(options).into(mCi4);
                break;
            case 5:
                Glide.with(this).load(HttpHost.qiNiu + likeImages.get(4).getHeadImg()).apply(options).into(mCi5);
                break;
            case 6:
                Glide.with(this).load(HttpHost.qiNiu + likeImages.get(5).getHeadImg()).apply(options).into(mCi6);
                break;
            case 7:
                Glide.with(this).load(HttpHost.qiNiu + likeImages.get(6).getHeadImg()).apply(options).into(mCi7);
                break;
            case 8:
                Glide.with(this).load(HttpHost.qiNiu + likeImages.get(7).getHeadImg()).apply(options).into(mCi8);
                break;
            case 9:
                Glide.with(this).load(HttpHost.qiNiu + likeImages.get(8).getHeadImg()).apply(options).into(mCi9);
                break;
        }
    }

    private void addLike(String objectiveId) {
        Api.getApiService().addLikeObject(objectiveId, getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Action1<BaseBean>() {
                    @Override
                    public void call(BaseBean baseBean) {

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        setContentView(getContentView());
        mIvBack = findViewById(R.id.iv_back);
        mTvTitle = findViewById(R.id.tv_title);
        mTvTitleRight = findViewById(R.id.tv_title_right);
        mRelativeTitle = findViewById(R.id.relative_title);
        mCi1 = findViewById(R.id.ci1);
        mCi2 = findViewById(R.id.ci2);
        mCi3 = findViewById(R.id.ci3);
        mCi4 = findViewById(R.id.ci4);
        mCi5 = findViewById(R.id.ci5);
        mCi6 = findViewById(R.id.ci6);
        mCi7 = findViewById(R.id.ci7);
        mCi8 = findViewById(R.id.ci8);
        mCi9 = findViewById(R.id.ci9);
        mTvLikeNum = findViewById(R.id.tv_like_num);
        mLlHeads = findViewById(R.id.ll_heads);
        mSwipeFlingView = findViewById(R.id.swipe_fling_view);
        mLlSwipeLift = findViewById(R.id.ll_swipe_lift);
        mLlSwipeRight = findViewById(R.id.ll_swipe_right);
        mRelativeTitle.setBackgroundColor(getResources().getColor(R.color.transparent));
        mIvBack.setOnClickListener(this);
        mLlSwipeLift.setOnClickListener(this);
        mLlSwipeRight.setOnClickListener(this);
        initView();
        initData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_swipe_lift:
                if (mAdapter.getCount() > 0 || likeImages.size() < 10)
                    mSwipeFlingView.swipeLeft();
                break;
            case R.id.ll_swipe_right:
                if (mAdapter.getCount() > 0 || likeImages.size() < 10)
                    mSwipeFlingView.swipeRight();
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }
}
