package com.kw.top.ui.activity.task;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.kw.top.R;
import com.kw.top.adapter.ClubTaskAdapter;
import com.kw.top.base.BaseActivity;
import com.kw.top.bean.BaseBean;
import com.kw.top.bean.ClubListBean;
import com.kw.top.retrofit.Api;
import com.kw.top.tools.ComResultTools;

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

public class ClubTaskListActivity extends BaseActivity {

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

    private ClubTaskAdapter mAdapter;
    private String groupId;
    private List<ClubListBean> mList = new ArrayList<>();
    private boolean isOwner; //是否是群主

    public static void startActivity(Context context, String groupId,boolean isOwner) {
        Intent intent = new Intent(context, ClubTaskListActivity.class);
        intent.putExtra("groupId", groupId);
        intent.putExtra("owner",isOwner);
        context.startActivity(intent);
    }

    @Override
    public int getContentView() {
        return R.layout.activity_club_task_list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        groupId = getIntent().getStringExtra("groupId");
        isOwner = getIntent().getBooleanExtra("owner",false);
        initView();
        initData();
    }

    private void initData() {
        showProgressDialog();

        if (isOwner){
            ownerData();
        }else {
            memberData();
        }
    }

    //成员
    private void memberData() {
        Api.getApiService().clubUserTaskList(groupId,getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Action1<BaseBean>() {
                    @Override
                    public void call(BaseBean baseBean) {
                        hideProgressDialog();
                        if (baseBean.isSuccess()) {
                            try {
                                List<ClubListBean> listBeans = new Gson().fromJson(baseBean.getJsonData(), new TypeToken<List<ClubListBean>>() {
                                }.getType());
                                mList.addAll(listBeans);
                                mAdapter.notifyDataSetChanged();
                            } catch (JsonSyntaxException e) {
                                e.printStackTrace();
                            }
                        } else {
                            ComResultTools.resultData(ClubTaskListActivity.this, baseBean);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        hideProgressDialog();
                    }
                });
    }

    //群主
    private void ownerData() {
        Api.getApiService().clubTaskList(groupId, getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Action1<BaseBean>() {
                    @Override
                    public void call(BaseBean baseBean) {
                        hideProgressDialog();
                        if (baseBean.isSuccess()) {
                            try {
                                List<ClubListBean> listBeans = new Gson().fromJson(baseBean.getJsonData(), new TypeToken<List<ClubListBean>>() {
                                }.getType());
                                mList.addAll(listBeans);
                                mAdapter.notifyDataSetChanged();
                            } catch (JsonSyntaxException e) {
                                e.printStackTrace();
                            }
                        } else {
                            ComResultTools.resultData(ClubTaskListActivity.this, baseBean);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        hideProgressDialog();
                    }
                });
    }

    private void initView() {
        mTvTitle.setText("社团任务");
        if (isOwner){
            mTvTitleRight.setText("发布");
        }
        mAdapter = new ClubTaskAdapter(this, mList,isOwner);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
    }


    @OnClick({R.id.iv_back, R.id.tv_title_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_title_right:
                PublishTaskActivity.startActivity(this,groupId);
                break;
        }
    }
}
