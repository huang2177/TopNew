package com.kw.top.ui.activity.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.kw.top.R;
import com.kw.top.adapter.HeartGril2Adapter;
import com.kw.top.base.BaseActivity;
import com.kw.top.bean.AllUserBean;
import com.kw.top.bean.BaseBean;
import com.kw.top.bean.event.EventMoney;
import com.kw.top.retrofit.Api;
import com.kw.top.ui.activity.person_info.EditInfoActivity;
import com.kw.top.utils.RxToast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * author: zy
 * data  : 2018/5/17
 * des   :
 */

public class HeartBeatGril2 extends BaseActivity implements View.OnClickListener{

    ImageView mIvBack;
    TextView mTvTitle;
    TextView mTvTitleRight;
    RelativeLayout mRelativeTitle;
    RecyclerView mRecyclerView;
    TextView mTvSendFriend;
    private List<AllUserBean> mList = new ArrayList<>();
    private HeartGril2Adapter mAdapter;
    private boolean money = false;//是否充值

    public static void startActivity(Context context, List<AllUserBean> list) {
        Intent intent = new Intent(context, HeartBeatGril2.class);
        intent.putExtra("LIST", (Serializable) list);
        context.startActivity(intent);
    }

    public static void startActivity(Context context,boolean money){
        Intent intent = new Intent(context,HeartBeatGril2.class);
        intent.putExtra("MONEY",money);
        context.startActivity(intent);
    }

    @Override
    public int getContentView() {
        return R.layout.activity_heart_gril2;
    }

    public void initView() {
//        mList = (List<AllUserBean>) getIntent().getSerializableExtra("LIST");
//        if (mList == null || mList.size() == 0){
//        }
        getLikeObject();
        mRelativeTitle.setBackgroundColor(getResources().getColor(R.color.transparent));
        money = getIntent().getBooleanExtra("MONEY",false);
        mRecyclerView.setHasFixedSize(false);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 5);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new HeartGril2Adapter(this, mList);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void getLikeObject() {
        showProgressDialog();
        Api.getApiService().getLikeObject(getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Action1<BaseBean>() {
                    @Override
                    public void call(BaseBean baseBean) {
                        hideProgressDialog();
                        if (baseBean.isSuccess()){
                            mList.clear();
                            List<AllUserBean> userBeanList = null;
                            try {
                                userBeanList = new Gson().fromJson(baseBean.getJsonData(),new TypeToken<List<AllUserBean>>(){}.getType());
                            } catch (JsonSyntaxException e) {
                                e.printStackTrace();
                            }
                            if (userBeanList.size()>10){
                                for (int i=0; i<10; i++){
                                    mList.add(userBeanList.get(i));
                                }
                            }else {
                                mList.addAll(userBeanList);
                            }
                            mAdapter.notifyDataSetChanged();
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
        mTvSendFriend = findViewById(R.id.tv_send_friend);
        mTvSendFriend.setOnClickListener(this);
        mIvBack.setOnClickListener(this);
        EventBus.getDefault().register(this);
        initView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_send_friend:
                if (money){
                    addFriend();
                }else {
                    startActivity(ManVipActivity.class);
                }
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }

    private int count;
    private boolean sendSucceed;
    private void addFriend() {
        count =0;
        if (mList.size()==0){
            RxToast.normal("没有添加喜欢的人");
            return;
        }
        showProgressDialog();
        for (int i=0;i<mList.size();i++){
            try {
               // EMClient.getInstance().contactManager().addContact(mList.get(i).getAccount(), "约炮");
            } catch (Exception e) {
                e.printStackTrace();
            }
            Api.getApiService().sendGiftAddFriend("7","1",mList.get(i).getUserId(),getToken())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .unsubscribeOn(Schedulers.io())
                    .subscribe(new Action1<BaseBean>() {
                        @Override
                        public void call(BaseBean baseBean) {
                            count +=1;
                            sendSucceed = true;
                            if (count>=10 && sendSucceed){
                                hideProgressDialog();
                                RxToast.normal("赠送成功");
                                EditInfoActivity.startActivity(HeartBeatGril2.this,false);
                            }
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            count +=1;
                            if (count>=10 && sendSucceed){
                                hideProgressDialog();
                                RxToast.normal("赠送成功");
                                EditInfoActivity.startActivity(HeartBeatGril2.this,false);
                            }else if (count>=10 && !sendSucceed){
                                hideProgressDialog();
                                RxToast.normal("网络异常");
                            }
                        }
                    });
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setMoney(EventMoney eventMoney){
        if (eventMoney.isMoney())
            money = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
