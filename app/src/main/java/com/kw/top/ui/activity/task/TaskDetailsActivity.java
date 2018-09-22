package com.kw.top.ui.activity.task;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.kw.top.R;
import com.kw.top.adapter.TaskDetailsAdapter;
import com.kw.top.base.BaseActivity;
import com.kw.top.bean.BaseBean;
import com.kw.top.bean.TaskDetailsBean;
import com.kw.top.listener.OnClickListener;
import com.kw.top.retrofit.Api;
import com.kw.top.tools.ComResultTools;
import com.kw.top.utils.RxToast;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * author: zy
 * data  : 2018/5/27
 * des   : 任务详情
 */

public class TaskDetailsActivity extends BaseActivity implements OnRefreshListener ,OnClickListener {
    ImageView mIvBack;
    TextView mTvTitle;
    TextView mTvTitleRight;
    RelativeLayout mRelativeTitle;
    RecyclerView mRecyclerView;
    SmartRefreshLayout mRefreshLayout;
    private String taskId;
    private List<TaskDetailsBean> mList = new ArrayList<>();
    private TaskDetailsAdapter mAdapter;
    private int type; //0 世界任务

    public static void startActivity(Context context, String taskId,int type) {
        Intent intent = new Intent(context, TaskDetailsActivity.class);
        intent.putExtra("ID", taskId);
        intent.putExtra("TYPE",type);
        context.startActivity(intent);
    }

    @Override
    public int getContentView() {
        return R.layout.activity_task_details;
    }

    public void initView() {
        mTvTitle.setText("任务详情");
        taskId = getIntent().getStringExtra("ID");
        type = getIntent().getIntExtra("TYPE",1);

        if (TextUtils.isEmpty(taskId)) {
            RxToast.normal("数据异常");
            return;
        }
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setEnableLoadMore(false);

        mAdapter = new TaskDetailsAdapter(this,mList,this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);

//        if (type == 0){
//            //世界任务
            getDetailsList();
//        }else {
//            getMyDetailsList();
//        }

    }

    private void getMyDetailsList() {
        Api.getApiService().taskDetails(taskId, getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        showProgressDialog();
                    }
                })
                .subscribe(new Action1<BaseBean>() {
                    @Override
                    public void call(BaseBean baseBean) {
                        hideProgressDialog();
                        mRefreshLayout.finishRefresh();
                        if (baseBean.isSuccess()) {
                            mList.clear();
                            try {
                                List<TaskDetailsBean> list = new Gson().fromJson(baseBean.getJsonData(), new TypeToken<List<TaskDetailsBean>>() {
                                }.getType());
                                mList.addAll(list);
                                mAdapter.notifyDataSetChanged();
                            } catch (JsonSyntaxException e) {
                                e.printStackTrace();
                            }
                        } else {
                            ComResultTools.resultData(TaskDetailsActivity.this, baseBean);
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

    private void getDetailsList(){
        Api.getApiService().getTaskDesc(taskId, getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        showProgressDialog();
                    }
                })
                .subscribe(new Action1<BaseBean>() {
                    @Override
                    public void call(BaseBean baseBean) {
                        hideProgressDialog();
                        mRefreshLayout.finishRefresh();
                        if (baseBean.isSuccess()) {
                            mList.clear();
                            try {
                                List<TaskDetailsBean> list = new Gson().fromJson(baseBean.getJsonData(), new TypeToken<List<TaskDetailsBean>>() {
                                }.getType());
                                mList.addAll(list);
                                mAdapter.notifyDataSetChanged();
                            } catch (JsonSyntaxException e) {
                                e.printStackTrace();
                            }
                        } else {
                            ComResultTools.resultData(TaskDetailsActivity.this, baseBean);
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

    /**
     * 1赞赏 2拒绝
     *
     * @param state
     */
    private void agreeTask(final int position, final String state, String userTaskId) {
        Api.getApiService().agreeTask(userTaskId, state, getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        showProgressDialog();
                    }
                })
                .subscribe(new Action1<BaseBean>() {
                    @Override
                    public void call(BaseBean baseBean) {
                        hideProgressDialog();
                        if (baseBean.isSuccess()){
                            TaskDetailsBean taskDetailsBean = mList.get(position);
                            taskDetailsBean.setState(state);
                            mList.set(position,taskDetailsBean);
                            mAdapter.notifyDataSetChanged();
                        }else {
                            ComResultTools.resultData(TaskDetailsActivity.this,baseBean);
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
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
//        if (type == 0){
//            //世界任务
            getDetailsList();
//        }else {
//            getMyDetailsList();
//        }
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
        mRecyclerView = findViewById(R.id.recycler_view);
        mRefreshLayout = findViewById(R.id.refresh_layout);
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initView();
    }

    @Override
    public void onClick(View view, int position) {
        switch (view.getId()){
            case R.id.tv_reject:
                agreeTask(position,"2",mList.get(position).getUserTaskId()+"");
                break;
            case R.id.tv_award:
                agreeTask(position,"1",mList.get(position).getUserTaskId()+"");
                break;
        }
    }
}
