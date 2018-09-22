package com.kw.top.ui.fragment.active;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.kw.top.R;
import com.kw.top.adapter.ActiveAdapter;
import com.kw.top.base.BaseFragment;
import com.kw.top.bean.ActiveBean;
import com.kw.top.bean.BaseBean;
import com.kw.top.retrofit.Api;
import com.kw.top.tools.ComResultTools;
import com.kw.top.ui.activity.login.LoginActivity;
import com.kw.top.utils.RxToast;
import com.kw.top.utils.SPUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * author: Administrator
 * data  : 2018/5/6
 * des   : 世界活动
 */

public class ActiveFragment extends BaseFragment implements OnRefreshListener {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout mRefreshLayout;
    private List<ActiveBean> mList = new ArrayList<>();
    private ActiveAdapter mAdapter;

    public static ActiveFragment newInstance() {
        ActiveFragment fragment = new ActiveFragment();
        return fragment;
    }

    @Override
    public int getContentView() {
        return R.layout.frament_active;
    }

    @Override
    public void initView(View view, Bundle savedInstanceState) {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setEnableLoadMore(false);

        mAdapter = new ActiveAdapter(getContext(),mList);
        mRecyclerView.setAdapter(mAdapter);
        showProgressDialog();
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {
        Api.getApiService().getActiveList(getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Action1<BaseBean>() {
                    @Override
                    public void call(BaseBean baseBean) {
                        hideProgressDialog();
                        mRefreshLayout.finishRefresh();
                        if (baseBean.isSuccess()){
                            try {
                                List<ActiveBean> list = new Gson().fromJson(baseBean.getJsonData(), new TypeToken<List<ActiveBean>>() {
                                }.getType());
                                mList.clear();
                                mList.addAll(list);
                                mAdapter.notifyDataSetChanged();
                            } catch (JsonSyntaxException e) {
                                e.printStackTrace();
                            }
                        }else {
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
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        initData();
    }

}
