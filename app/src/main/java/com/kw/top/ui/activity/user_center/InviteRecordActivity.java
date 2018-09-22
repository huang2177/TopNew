package com.kw.top.ui.activity.user_center;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.kw.top.R;
import com.kw.top.adapter.InviteRecordAdapter;
import com.kw.top.base.BaseActivity;
import com.kw.top.bean.BaseBean;
import com.kw.top.bean.InviteRecordBean;
import com.kw.top.retrofit.Api;
import com.kw.top.retrofit.HttpHost;
import com.kw.top.tools.ComResultTools;
import com.kw.top.tools.GlideTools;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * author: zy
 * data  : 2018/5/20
 * des   :
 */

public class InviteRecordActivity extends BaseActivity {

    ImageView mIvBack;
    TextView mTvTitle;
    TextView mTvTitleRight;
    RelativeLayout mRelativeTitle;
    CircleImageView mCiHead;
    TextView mTvNickname;
    ImageView mIvVip;
    TextView mTvSumDiamon;
    RecyclerView mRecyclerView;
    private InviteRecordAdapter mAdapter;
    private List<InviteRecordBean.InviteListBean> mList = new ArrayList<>();

    @Override
    public int getContentView() {
        return R.layout.activity_invite_record;
    }

    public void initView() {
        mTvTitle.setText("邀请记录");
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new InviteRecordAdapter(this, mList);
        mRecyclerView.setAdapter(mAdapter);
    }

    public void initData() {
        showProgressDialog();
        Api.getApiService().inviteList(getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Action1<BaseBean>() {
                    @Override
                    public void call(BaseBean baseBean) {
                        hideProgressDialog();
                        if (baseBean.isSuccess()) {

                            InviteRecordBean inviteRecordBean = null;
                            try {
                                inviteRecordBean = new Gson().fromJson(baseBean.getJsonData(), new TypeToken<InviteRecordBean>() {
                                }.getType());
                            } catch (JsonSyntaxException e) {
                                e.printStackTrace();
                            }
                            fillUI(inviteRecordBean);
                        } else {
                            ComResultTools.resultData(InviteRecordActivity.this, baseBean);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        hideProgressDialog();
                    }
                });
    }

    private void fillUI(InviteRecordBean inviteRecordBean) {
        InviteRecordBean.UserBean userBean = inviteRecordBean.getUser();
        mTvNickname.setText(userBean.getNickName());
        Glide.with(this).load(HttpHost.qiNiu + userBean.getHeadImg()).apply(GlideTools.getHeadOptions()).into(mCiHead);
        GlideTools.setVipResourceS(mIvVip, userBean.getGrade());
        mTvSumDiamon.setText(userBean.getUserProfitSum());

        mList.addAll(inviteRecordBean.getInviteList());
        mAdapter.notifyDataSetChanged();
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
        mCiHead = findViewById(R.id.ci_head);
        mTvNickname = findViewById(R.id.tv_nickname);
        mIvVip = findViewById(R.id.iv_vip);
        mTvSumDiamon = findViewById(R.id.tv_sum_diamon);
        mRecyclerView = findViewById(R.id.recycler_view);
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
