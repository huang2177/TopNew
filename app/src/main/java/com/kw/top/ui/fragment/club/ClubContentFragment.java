package com.kw.top.ui.fragment.club;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.kw.top.R;
import com.kw.top.adapter.ClubListAdapter;
import com.kw.top.base.MVPBaseFragment;
import com.kw.top.bean.BaseBean;
import com.kw.top.bean.ClubBean;
import com.kw.top.retrofit.Api;
import com.kw.top.tools.ComResultTools;
import com.kw.top.tools.ConstantValue;
import com.kw.top.ui.activity.login.LoginActivity;
import com.kw.top.utils.RxToast;
import com.kw.top.utils.SPUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * author: Administrator
 * data  : 2018/5/7
 * des   :
 */

public class ClubContentFragment extends MVPBaseFragment<ClubContract.View, ClubPresenter> implements ClubContract.View, OnRefreshListener, OnLoadMoreListener {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout mRefreshLayout;
    private int type = 0; // 0加入 1创建
    private List<ClubBean> mList = new ArrayList<>();
    private ClubListAdapter mAdapter;
    private int mCurrentPage = 1;
    private String loadMore = "";

    public static ClubContentFragment newInstance(int type) {
        ClubContentFragment fragment = new ClubContentFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("TYPE", type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getContentView() {
        return R.layout.fragment_club;
    }

    @Override
    public void initView(View view, Bundle savedInstanceState) {
        type = getArguments().getInt("TYPE", 0);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRefreshLayout.setOnRefreshListener(this);

        mAdapter = new ClubListAdapter(getContext(), mList, type);
        mRecyclerView.setAdapter(mAdapter);
        showProgressDialog();
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {
        if (type == 0) {
            mRefreshLayout.setEnableLoadMore(false);
            Api.getApiService().getMyClub(getToken())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .unsubscribeOn(Schedulers.io())
                    .subscribe(new Action1<BaseBean>() {
                        @Override
                        public void call(BaseBean baseBean) {
                            hideProgressDialog();
                            mRefreshLayout.finishRefresh();
                            if (baseBean.isSuccess()) {
                                try {
                                    List<ClubBean> list = new Gson().fromJson(baseBean.getJsonData(), new TypeToken<List<ClubBean>>() {
                                    }.getType());
                                    mList.clear();
                                    mList.addAll(list);
                                    mAdapter.notifyDataSetChanged();
                                } catch (JsonSyntaxException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                ComResultTools.resultData(getContext(),baseBean);
                            }
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            hideProgressDialog();
                            mRefreshLayout.finishRefresh();
                        }
                    });
        } else {
            mRefreshLayout.setOnLoadMoreListener(this);
            Api.getApiService().getAllClub(mCurrentPage + "", ConstantValue.ONE_PAGE_NUM + "", getToken())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .unsubscribeOn(Schedulers.io())
                    .subscribe(new Action1<BaseBean<List<ClubBean>>>() {
                        @Override
                        public void call(BaseBean<List<ClubBean>> baseBean) {
                            hideProgressDialog();
                            mRefreshLayout.finishRefresh();
                            mRefreshLayout.setNoMoreData(false);
                            mRefreshLayout.finishLoadMore();
                            if (baseBean.isSuccess()) {
                                if (mCurrentPage == 1) {
                                    mList.clear();
                                }
                                mList.addAll(baseBean.getData());
                                mAdapter.notifyDataSetChanged();
                                loadMore = baseBean.getMsg();
                                if ("0".equals(baseBean.getMsg())) {
                                    mRefreshLayout.finishLoadMoreWithNoMoreData();
                                }
                            } else if (baseBean.getCode().equals("-44")) {
                                RxToast.normal(getResources().getString(R.string.login_out));
                                SPUtils.clear(getContext());
                                startActivity(LoginActivity.class);
                            } else {
                                RxToast.normal(baseBean.getMsg());
                            }
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            hideProgressDialog();
                            mRefreshLayout.finishRefresh();
                        }
                    });
        }
    }

    @Override
    protected void initPresenter() {

    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        initData();
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        if (loadMore.equals("1")) {
            mCurrentPage++;
            initData();
        } else {
            refreshLayout.finishLoadMoreWithNoMoreData();
        }
    }
}
