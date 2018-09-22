package com.kw.top.ui.activity.news;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kw.top.R;
import com.kw.top.adapter.FriendApplyAdapter;
import com.kw.top.base.BaseActivity;
import com.kw.top.bean.BaseBean;
import com.kw.top.bean.FriendApplyBean;
import com.kw.top.bean.event.RefreshFriendEvent;
import com.kw.top.listener.OnClickListener;
import com.kw.top.retrofit.Api;
import com.kw.top.ui.activity.login.LoginActivity;
import com.kw.top.utils.RxToast;
import com.kw.top.utils.SPUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * author: zy
 * data  : 2018/6/10
 * des   :
 */

public class FriendApplyActivity extends BaseActivity implements OnClickListener {

    ImageView mIvBack;
    TextView mTvTitle;
    TextView mTvTitleRight;
    RelativeLayout mRelativeTitle;
    RecyclerView mRecyclerView;
    TextView mTvEmpty;
    private List<FriendApplyBean> mList = new ArrayList<>();
    private FriendApplyAdapter mAdapter;
    private int mPosition;
    private FriendApplyBean mFriendApplyBean;

    @Override
    public int getContentView() {
        return R.layout.activity_friend_apply;
    }

    public void initView() {
        mTvTitle.setText("好友申请");

        mAdapter = new FriendApplyAdapter(this, mList, this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
    }

    public void initData() {
        getApplyList();
    }


    @Override
    public void onClick(View view, int position) {
        mPosition = position;
        mFriendApplyBean = mList.get(position);
        showProgressDialog();
        switch (view.getId()) {
            case R.id.tv_reject:
                mFriendApplyBean.setFriendsState("2");
                agreeApply(mFriendApplyBean.getRelationId() + "", "0");
                break;
            case R.id.tv_agree:
                mFriendApplyBean.setFriendsState("1");
                agreeApply(mFriendApplyBean.getRelationId() + "", "1");
                break;
        }
    }

    /**
     * 获取好友请求 列表
     */
    private void getApplyList() {
        showProgressDialog();
        Api.getApiService().applyFriendsList(getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Action1<BaseBean<List<FriendApplyBean>>>() {
                    @Override
                    public void call(BaseBean<List<FriendApplyBean>> baseBean) {
                        hideProgressDialog();
                        if (baseBean.isSuccess()) {
                            if (baseBean.getData().size() == 0) {
                                RxToast.normal("暂时没有好友申请");
                                mRecyclerView.setVisibility(View.GONE);
                                mTvEmpty.setVisibility(View.VISIBLE);
                                mTvEmpty.setText("暂时没有好友申请");
                            } else if (baseBean.getCode().equals("-44")) {
                                RxToast.normal(getResources().getString(R.string.login_out));
                                SPUtils.clear(FriendApplyActivity.this);
                                startActivity(LoginActivity.class);
                                finish();
                            } else {
                                mList.addAll(baseBean.getData());
                                mAdapter.setData(mList);
                            }
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        hideProgressDialog();
                    }
                });
    }

    /**
     * @param relationId 申请id
     * @param isAgree    0拒绝 1同意
     */
    private void agreeApply(String relationId, final String isAgree) {
        Api.getApiService().agreeFriendApply(relationId, isAgree, getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                    }
                }).subscribe(new Action1<BaseBean>() {
            @Override
            public void call(BaseBean baseBean) {
                hideProgressDialog();
                if (baseBean.isSuccess()) {
                    if (isAgree.equals("1")){
                        EventBus.getDefault().post(new RefreshFriendEvent(true));
                    }
                    mList.set(mPosition, mFriendApplyBean);
                    mAdapter.notifyItemChanged(mPosition);
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        setContentView(getContentView());
        mIvBack = findViewById(R.id.iv_back);
        mTvTitle = findViewById(R.id.tv_title);
        mTvTitleRight = findViewById(R.id.tv_title_right);
        mRelativeTitle = findViewById(R.id.relative_title);
        mRecyclerView = findViewById(R.id.recycler_view);
        mTvEmpty = findViewById(R.id.tv_empty);
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initView();
        initData();
    }
}
