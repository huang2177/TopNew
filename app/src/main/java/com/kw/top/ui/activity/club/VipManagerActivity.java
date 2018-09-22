package com.kw.top.ui.activity.club;

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
import com.kw.top.adapter.VipMangerAdapter;
import com.kw.top.base.BaseActivity;
import com.kw.top.bean.AllUserBean;
import com.kw.top.bean.BaseBean;
import com.kw.top.bean.event.ClubVipEvent;
import com.kw.top.listener.OnDeleteListener;
import com.kw.top.retrofit.Api;
import com.kw.top.tools.ComResultTools;
import com.kw.top.ui.activity.login.LoginActivity;
import com.kw.top.utils.RxToast;
import com.kw.top.utils.SPUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
 * date    ： 2018/6/19
 * des     ： 成员管理
 */

public class VipManagerActivity extends BaseActivity implements OnDeleteListener {
    ImageView mIvBack;
    TextView mTvTitle;
    TextView mTvTitleRight;
    RelativeLayout mRelativeTitle;
    RecyclerView mRecyclerView;
    private String groupid;
    private VipMangerAdapter mAdapter;
    private List<AllUserBean> mList = new ArrayList<>();

    public static void startActivity(Context context, String groupid) {
        Intent intent = new Intent(context, VipManagerActivity.class);
        intent.putExtra("ID", groupid);
        context.startActivity(intent);
    }

    @Override
    public int getContentView() {
        return R.layout.activity_vip_manager;
    }

    public void initView() {
        groupid = getIntent().getStringExtra("ID");

        mTvTitle.setText("成员管理");
        mTvTitleRight.setBackgroundResource(R.mipmap.icon_apply);

        mAdapter = new VipMangerAdapter(this, mList, this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
    }

    public void initData() {
        showProgressDialog();
        Api.getApiService().getClubAllPeople(groupid, getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Action1<BaseBean>() {
                    @Override
                    public void call(BaseBean baseBean) {
                        hideProgressDialog();
                        if (baseBean.isSuccess()) {
                            List<AllUserBean>  allUserBeans = null;
                            try {
                                allUserBeans = new Gson().fromJson(baseBean.getJsonData(),new TypeToken<List<AllUserBean>>(){}.getType());
                            } catch (JsonSyntaxException e) {
                                e.printStackTrace();
                            }
                            mList.clear();
                            mList.addAll(allUserBeans);
                            mAdapter.notifyDataSetChanged();
                        }else {
                            ComResultTools.resultData(VipManagerActivity.this,baseBean);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        hideProgressDialog();

                    }
                });
    }

    public void initListener() {
        mTvTitleRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VipApplyActivity.startActivity(VipManagerActivity.this, groupid);
            }
        });
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onDelete(View view, int position) {
        showProgressDialog();
        Api.getApiService().deleteClubMember(mList.get(position).getApplyId()+"", getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Action1<BaseBean>() {
                    @Override
                    public void call(BaseBean baseBean) {
                        hideProgressDialog();
                        if (baseBean.isSuccess()){
                            initData();
                        }else{
                            ComResultTools.resultData(VipManagerActivity.this,baseBean);
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
        EventBus.getDefault().register(this);
        mIvBack = findViewById(R.id.iv_back);
        mTvTitle= findViewById(R.id.tv_title);
        mTvTitleRight= findViewById(R.id.tv_title_right);
        mRelativeTitle= findViewById(R.id.relative_title);
        mRecyclerView= findViewById(R.id.recycler_view);
        initView();
        initData();
        initListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshClubVip(ClubVipEvent clubVipEvent){
        if (clubVipEvent.isRefresh()){
            initData();
        }
    }

}
