package com.kw.top.ui.fragment.list;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.kw.top.R;
import com.kw.top.adapter.TopListAdapter;
import com.kw.top.base.BaseFragment;
import com.kw.top.bean.BaseBean;
import com.kw.top.bean.TopListBean;
import com.kw.top.retrofit.Api;
import com.kw.top.retrofit.HttpHost;
import com.kw.top.tools.ComResultTools;
import com.kw.top.tools.ConstantValue;
import com.kw.top.tools.GlideTools;
import com.kw.top.ui.activity.find.FindDetailsActivity;
import com.kw.top.utils.DisplayUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * author: zy
 * data  : 2018/5/27
 * des   : TOP 榜
 */

public class ListFragment extends BaseFragment implements OnRefreshListener, OnLoadMoreListener {
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.iv_image1)
    ImageView mIvImage1;
    @BindView(R.id.tv_type1_name)
    TextView mTvType1Name;
    @BindView(R.id.tv_type1_gift)
    TextView mTvType1Gift;
    @BindView(R.id.iv_type2_left)
    ImageView mIvType2Left;
    @BindView(R.id.tv_type2_name1)
    TextView mTvType2Name1;
    @BindView(R.id.tv_type2_gift1)
    TextView mTvType2Gift1;
    @BindView(R.id.rl_type2_one)
    RelativeLayout mRlType2One;
    @BindView(R.id.iv_type2_right)
    ImageView mIvType2Right;
    @BindView(R.id.tv_type2_name2)
    TextView mTvType2Name2;
    @BindView(R.id.tv_type2_gift2)
    TextView mTvType2Gift2;
    @BindView(R.id.rl_type2_two)
    RelativeLayout mRlType2Two;
    @BindView(R.id.scroll_view)
    NestedScrollView mScrollView;
    @BindView(R.id.iv_gift)
    ImageView mIvGift;
    @BindView(R.id.iv_gift_left)
    ImageView mIvGiftLeft;
    @BindView(R.id.iv_gift_right)
    ImageView mIvGiftRight;
    private String type = "01"; // 01魅力 02实力
    private int mCurrentPager = 1;
    private TopListAdapter mTopListAdapter;
    private List<TopListBean> mList = new ArrayList<>();
    private TopListBean mTopListBean1, mTopListBean2, mTopListBean3;

    public static ListFragment newInstance(String type) {
        ListFragment fragment = new ListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("TYPE", type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getContentView() {
        return R.layout.fragment_top_list;
    }

    @Override
    public void initView(View view, Bundle savedInstanceState) {
        type = getArguments().getString("TYPE", "01");

        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setOnLoadMoreListener(this);
        mTopListAdapter = new TopListAdapter(getContext(), mList, type);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        mRecyclerView.setAdapter(mTopListAdapter);
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {
        getTopList();
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        //加载到底
        if (mCurrentPager == 1) {
            refreshLayout.finishLoadMoreWithNoMoreData();
        } else {
            mCurrentPager++;
            getTopList();
        }
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        mCurrentPager = 1;
        getTopList();
    }

    private void getTopList() {
        String token = getToken();
        showProgressDialog();
        Api.getApiService().getTopBeautifulList(type + "", mCurrentPager + "", ConstantValue.ONE_PAGE_NUM, token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Action1<BaseBean>() {
                    @Override
                    public void call(BaseBean baseBean) {
                        hideProgressDialog();
                        if (baseBean.isSuccess()) {
                            mScrollView.setVisibility(View.VISIBLE);
                            List<TopListBean> list = null;
                            try {
                                list = new Gson().fromJson(baseBean.getJsonData(), new TypeToken<List<TopListBean>>() {
                                }.getType());
                            } catch (JsonSyntaxException e) {
                                e.printStackTrace();
                            }
                            if (null == list) return;
                            if (mCurrentPager == 1) {
                                mList.clear();
                            }
                            mRefreshLayout.finishRefresh();
                            mRefreshLayout.setNoMoreData(false);
                            mRefreshLayout.finishLoadMore();
                            if (mCurrentPager == 1) {
                                mList.clear();
                                fillUI(list);
                                for (int i = 0; i < list.size(); i++) {
                                    if (i != 0 && i != 1 && i != 2) {
                                        mList.add(list.get(i));
                                        mTopListAdapter.notifyDataSetChanged();
                                    }
                                }
                            } else {
                                mList.addAll(list);
                                mTopListAdapter.notifyDataSetChanged();
                            }
                            mTopListAdapter.notifyDataSetChanged();
                            if ("0".equals(baseBean.getMsg())) {
                                mRefreshLayout.finishLoadMoreWithNoMoreData();
                            }
                        } else {
                            ComResultTools.resultData(getContext(), baseBean);
                        }

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        hideProgressDialog();
                        mRefreshLayout.finishRefresh();
                        mRefreshLayout.setNoMoreData(false);
                        mRefreshLayout.finishLoadMore();
                    }
                });
    }

    private void fillUI(List<TopListBean> listBeans) {
        if (type.equals("01")) {
            mIvGift.setImageResource(R.mipmap.icon_gift);
            mIvGiftLeft.setImageResource(R.mipmap.icon_gift);
            mIvGiftRight.setImageResource(R.mipmap.icon_gift);
        } else {
            mIvGift.setImageResource(R.mipmap.icon_diamond_small);
            mIvGiftLeft.setImageResource(R.mipmap.icon_diamond_small);
            mIvGiftRight.setImageResource(R.mipmap.icon_diamond_small);
        }
        TopListBean topListBean = null;
        if (listBeans.size() >= 1) {
            topListBean = listBeans.get(0);
            mTopListBean1 = topListBean;
            Glide.with(getContext())
                    .asBitmap()
                    .load(HttpHost.qiNiu + topListBean.getHeadImg())
                    .apply(GlideTools.getOptions())
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            int maxWidth = DisplayUtils.getScreenWidth(getActivity()) - DisplayUtils.dip2px(getContext(), 30);
                            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mIvImage1.getLayoutParams();
                            params.width = maxWidth;
                            params.height = maxWidth * resource.getHeight() / resource.getWidth();
                            mIvImage1.setLayoutParams(params);
                            mIvImage1.setImageBitmap(resource);
                        }
                    });
            mTvType1Name.setText(topListBean.getNickName());
            if (type.equals("01")) {
                mTvType1Gift.setText(topListBean.getGlamour());
            } else {
                mTvType1Gift.setText(topListBean.getAmount());
            }

        }
        if (listBeans.size() >= 2) {
            topListBean = listBeans.get(1);
            mTopListBean2 = topListBean;
            Glide.with(getContext())
                    .asBitmap()
                    .load(HttpHost.qiNiu + topListBean.getHeadImg())
                    .apply(GlideTools.getOptions())
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
//                            int maxWidth = (DisplayUtils.getScreenWidth(getActivity()) - DisplayUtils.dip2px(getContext(), 32)) / 2;
//                            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mIvType2Left.getLayoutParams();
//                            params.width = maxWidth;
//                            params.height = maxWidth * resource.getHeight() / resource.getWidth();
//                            mIvType2Left.setLayoutParams(params);
                            mIvType2Left.setImageBitmap(resource);
                        }
                    });
            mTvType2Name1.setText(topListBean.getNickName());
            if (type.equals("01")) {
                mTvType2Gift1.setText(topListBean.getGlamour());
            } else {
                mTvType2Gift1.setText(topListBean.getAmount());
            }
        }
        if (listBeans.size() >= 3) {
            topListBean = listBeans.get(2);
            mTopListBean3 = topListBean;
            Glide.with(getContext())
                    .asBitmap()
                    .load(HttpHost.qiNiu + topListBean.getHeadImg())
                    .apply(GlideTools.getOptions())
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
//                            int maxWidth = (DisplayUtils.getScreenWidth(getActivity()) - DisplayUtils.dip2px(getContext(), 32)) / 2;
//                            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mIvType2Right.getLayoutParams();
//                            params.width = maxWidth;
//                            params.height = maxWidth * resource.getHeight() / resource.getWidth();
//                            mIvType2Right.setLayoutParams(params);
                            mIvType2Right.setImageBitmap(resource);
                        }
                    });
            mTvType2Name2.setText(topListBean.getNickName());
            if (type.equals("01")) {
                mTvType2Gift2.setText(topListBean.getGlamour());
            } else {
                mTvType2Gift2.setText(topListBean.getAmount());
            }
        }
    }

    private void startUserCircle(TopListBean topListBean) {
        if (null == topListBean) return;
        FindDetailsActivity.startActivity(getContext(), topListBean.getUserId());
    }

    @OnClick({R.id.iv_image1, R.id.iv_type2_left, R.id.iv_type2_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_image1:
                startUserCircle(mTopListBean1);
                break;
            case R.id.iv_type2_left:
                startUserCircle(mTopListBean2);
                break;
            case R.id.iv_type2_right:
                startUserCircle(mTopListBean3);
                break;
        }
    }
}
