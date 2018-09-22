package com.kw.top.ui.activity.task;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kw.top.R;
import com.kw.top.adapter.ClubTaskDetailsAdapter;
import com.kw.top.base.BaseActivity;
import com.kw.top.bean.BaseBean;
import com.kw.top.bean.ClubTaskDetailsBean;
import com.kw.top.retrofit.Api;
import com.kw.top.tools.ComResultTools;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * author  ： zy
 * date    ： 2018/8/11
 * des     ：
 */

public class ClubTaskDetailsActivity extends BaseActivity {
    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_title_right)
    TextView mTvTitleRight;
    @BindView(R.id.relative_title)
    RelativeLayout mRelativeTitle;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout mRefreshLayout;

    private String taskId;
    private List<ClubTaskDetailsBean> mList = new ArrayList<>();
    private ClubTaskDetailsAdapter mAdapter;

    public static final void startActivity(Context context,String taskId){
        Intent intent = new Intent(context,ClubTaskDetailsActivity.class);
        intent.putExtra("taskid",taskId);
        context.startActivity(intent);
    }

    @Override
    public int getContentView() {
        return R.layout.activity_club_task_details;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        ButterKnife.bind(this);

        taskId = getIntent().getStringExtra("taskid");

        mTvTitle.setText("任务详情");
        mAdapter = new ClubTaskDetailsAdapter(this,mList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
        initData();
    }

    private void initData() {
        showProgressDialog();
        Api.getApiService().clubTaskDetails(taskId,getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Action1<BaseBean>() {
                    @Override
                    public void call(BaseBean baseBean) {
                        hideProgressDialog();
                        if (baseBean.isSuccess()){
                            mList.clear();
                            List<ClubTaskDetailsBean> tempLists = new Gson().fromJson(baseBean.getJsonData(),new TypeToken<List<ClubTaskDetailsBean>>(){}.getType());
                            mList.addAll(tempLists);
                            mAdapter.notifyDataSetChanged();
                        }else {
                            ComResultTools.resultData(ClubTaskDetailsActivity.this,baseBean);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        hideProgressDialog();
                    }
                });
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }
}
