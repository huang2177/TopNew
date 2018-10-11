package com.kw.top.ui.activity.circle;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.kw.top.R;
import com.kw.top.adapter.TopNewAdapter;
import com.kw.top.base.BaseActivity;
import com.kw.top.bean.BaseBean;
import com.kw.top.bean.CircleNewsBean;
import com.kw.top.listener.OnClickListener;
import com.kw.top.retrofit.Api;
import com.kw.top.ui.activity.login.LoginActivity;
import com.kw.top.utils.RxToast;
import com.kw.top.utils.SPUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * author: zy
 * data  : 2018/5/27
 * des   : 我的消息
 */

public class MyNewsActivity extends BaseActivity implements OnRefreshListener ,OnClickListener{
    ImageView mIvBack;
    TextView mTvTitle;
    TextView mTvTitleRight;
    RelativeLayout mRelativeTitle;
    RecyclerView mRecyclerView;
    SmartRefreshLayout mRefreshLayout;
    private List<CircleNewsBean.CommentListBean> mList = new ArrayList<>();
    private TopNewAdapter mAdapter;
    @Override
    public int getContentView() {
        return R.layout.activity_my_news;
    }

    public void initView() {

        mRecyclerView = findViewById(R.id.recycler_view);
        mRefreshLayout = findViewById(R.id.refresh_layout);
        mIvBack = findViewById(R.id.iv_back);
        mTvTitle = findViewById(R.id.tv_title);
        mTvTitleRight = findViewById(R.id.tv_title_right);
        mRelativeTitle = findViewById(R.id.relative_title);

        mTvTitle.setText("我的消息");
        mTvTitleRight.setVisibility(View.VISIBLE);
//        mTvTitleRight.setText("清空");

        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setEnableLoadMore(false);

        mAdapter = new TopNewAdapter(this,mList,this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
    }

    public void initData() {
        getTopNews();
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void getTopNews() {
        showProgressDialog();
        Api.getApiService().getDynamicCom(getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Action1<BaseBean>() {
                    @Override
                    public void call(BaseBean baseBean) {
                        mRefreshLayout.finishRefresh();
                        hideProgressDialog();
                        if (baseBean.isSuccess()){
                            CircleNewsBean circleNewsBean = null;
                            try {
                                circleNewsBean = new Gson().fromJson(baseBean.getJsonData(),new TypeToken<CircleNewsBean>(){}.getType());
                            } catch (JsonSyntaxException e) {
                                e.printStackTrace();
                            }
                            mList.clear();
                            if (null == circleNewsBean)return;
                            mList.addAll(circleNewsBean.getCommentList());
                            mAdapter.notifyDataSetChanged();
                        }else if (baseBean.getCode().equals("-44")){
                            RxToast.normal(getResources().getString(R.string.login_out));
                            SPUtils.clear(MyNewsActivity.this);
                            startActivity(LoginActivity.class);
                            finish();
                        }else {
                            RxToast.normal(baseBean.getMsg());
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mRefreshLayout.finishRefresh();
                        hideProgressDialog();
                    }
                });
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        setContentView(getContentView());
        ButterKnife.bind(this);
        initView();
        initData();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        getTopNews();
    }

    @Override
    public void onClick(View view, int position) {
        CircleNewsBean.CommentListBean commentListBean = mList.get(position);
        if (null == commentListBean || TextUtils.isEmpty(commentListBean.getId())){
            RxToast.normal("数据异常");
        }else {
            upComNews(commentListBean.getId());
            WorldCircleDetailsActivity.startActivity(this,commentListBean.getId()+"");
        }
    }

    private void upComNews(String id){
        Api.getApiService().upDynamicCom(id,getToken())
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
}
