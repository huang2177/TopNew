package com.kw.top.ui.activity.active;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.kw.top.R;
import com.kw.top.adapter.ActiveAdapter;
import com.kw.top.base.BaseActivity;
import com.kw.top.bean.ActiveBean;
import com.kw.top.bean.BaseBean;
import com.kw.top.retrofit.Api;
import com.kw.top.tools.ComResultTools;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 精彩活动
 */

public class MarvellousActivity extends BaseActivity implements OnRefreshListener {


    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.tv_title)
    TextView tv_title;
    private List<ActiveBean> mList = new ArrayList<>();
    private ActiveAdapter mAdapter;

    @Override
    public int getContentView() {
        return R.layout.frament_active;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        ButterKnife.bind(this);
        initviews();
    }

    private void initviews() {
        tv_title.setText("精彩活动");
        mRecyclerView.setLayoutManager(new LinearLayoutManager(MarvellousActivity.this));
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setEnableLoadMore(false);

        mAdapter = new ActiveAdapter(MarvellousActivity.this, mList);
        mRecyclerView.setAdapter(mAdapter);
        showProgressDialog();
        initData();
    }


    @OnClick(R.id.iv_back)
    public void OnClick() {
        finish();
    }

    /**
     * 请求数据
     */
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
                        if (baseBean.isSuccess()) {
                            try {
                                List<ActiveBean> list = new Gson().fromJson(baseBean.getJsonData(), new TypeToken<List<ActiveBean>>() {
                                }.getType());
                                mList.clear();
                                mList.addAll(list);
                                mAdapter.notifyDataSetChanged();
                            } catch (JsonSyntaxException e) {
                                e.printStackTrace();
                            }
                        } else {
                            ComResultTools.resultData(MarvellousActivity.this, baseBean);
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
