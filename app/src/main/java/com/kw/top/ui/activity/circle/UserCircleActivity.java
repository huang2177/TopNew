package com.kw.top.ui.activity.circle;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.kw.top.adapter.WorldCircleAdapter;
import com.kw.top.base.BaseActivity;
import com.kw.top.bean.BaseBean;
import com.kw.top.bean.CircleNewsBean;
import com.kw.top.bean.TopCircleBean;
import com.kw.top.bean.UserCircleBean;
import com.kw.top.bean.event.CircleRefreshEvent;
import com.kw.top.retrofit.Api;
import com.kw.top.ui.activity.active.ActiveDetailsActivity;
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
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * author: zy
 * data  : 2018/6/10
 * des   :
 */

public class UserCircleActivity extends BaseActivity {

    ImageView mIvBack;
    TextView mTvTitle;
    TextView mTvTitleRight;
    RelativeLayout mRelativeTitle;
    TextView mTvLikeNum;
    TextView mTvDiamondNum;
    RecyclerView mRecyclerView;
    private String userId;
    private List<TopCircleBean> mList = new ArrayList<>();
    private WorldCircleAdapter mAdapter;


    public static void startActivity(Context context, String userid) {
        Intent intent = new Intent(context, UserCircleActivity.class);
        intent.putExtra("USERID", userid);
        context.startActivity(intent);
    }

    @Override
    public int getContentView() {
        return R.layout.activity_user_circle;
    }

    public void initView() {
        mTvTitle.setText("世界圈");
        mRelativeTitle.setBackgroundResource(R.color.black_bg);

        mAdapter = new WorldCircleAdapter(this, mList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
    }

    public void initData() {
        userId = getIntent().getStringExtra("USERID");
        if (TextUtils.isEmpty(userId)) {
            RxToast.normal("用户信息异常,请稍后再试");
            return;
        }
       getCircleData();
    }

    private void getCircleData(){
        showProgressDialog();
        Api.getApiService().getTOPListByUser(userId, getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Action1<BaseBean>() {
                    @Override
                    public void call(BaseBean baseBean) {
                        hideProgressDialog();
                        if (baseBean.isSuccess()) {
                            try {
                                UserCircleBean userCircleBean = new Gson().fromJson(baseBean.getJsonData(),new TypeToken<UserCircleBean>(){}.getType());
                                UserCircleBean.ReceiveBeam receiveBeam = userCircleBean.getReceive();
                                mTvLikeNum.setText(receiveBeam.getThumbsUpNumSum() + "");
                                mTvDiamondNum.setText(receiveBeam.getThumbsNumSum() + "");
                                mList.addAll(userCircleBean.getDynamicList());
                                mAdapter.notifyDataSetChanged();
                            } catch (JsonSyntaxException e) {
                                e.printStackTrace();
                            }

                        }else if (baseBean.getCode().equals("-44")){
                            RxToast.normal(getResources().getString(R.string.login_out));
                            SPUtils.clear(UserCircleActivity.this);
                            startActivity(LoginActivity.class);
                            finish();
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
        mTvLikeNum = findViewById(R.id.tv_like_num);
        mTvDiamondNum = findViewById(R.id.tv_diamond_num);
        mRecyclerView = findViewById(R.id.recycler_view);
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        EventBus.getDefault().register(this);
        initView();
        initData();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshCircle(CircleRefreshEvent event){
        if (event.isRefresh())
            getCircleData();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
    }
}
