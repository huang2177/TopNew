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
import com.kw.top.adapter.VipApplyAdapter;
import com.kw.top.base.BaseActivity;
import com.kw.top.bean.AllUserBean;
import com.kw.top.bean.BaseBean;
import com.kw.top.bean.event.ClubVipEvent;
import com.kw.top.listener.OnClickListener;
import com.kw.top.retrofit.Api;
import com.kw.top.tools.ComResultTools;
import com.kw.top.utils.RxToast;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * author  ： zy
 * date    ： 2018/6/19
 * des     ： 成员申请
 */

public class VipApplyActivity extends BaseActivity implements OnClickListener {

    ImageView mIvBack;
    TextView mTvTitle;
    TextView mTvTitleRight;
    RelativeLayout mRelativeTitle;
    RecyclerView mRecyclerView;
    private VipApplyAdapter mAdapter;
    private List<AllUserBean> mList = new ArrayList<>();
    private String groupid;

    public static void startActivity(Context context, String clubId) {
        Intent intent = new Intent(context, VipApplyActivity.class);
        intent.putExtra("ID", clubId);
        context.startActivity(intent);
    }


    @Override
    public int getContentView() {
        return R.layout.activity_vip_apply;
    }

    public void initView() {
        groupid = getIntent().getStringExtra("ID");
        mTvTitle.setText("成员申请");
        mAdapter = new VipApplyAdapter(this, mList, this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
    }

    public void initData() {
        showProgressDialog();
        Api.getApiService().getApplyClubMember(groupid, getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Action1<BaseBean>() {
                    @Override
                    public void call(BaseBean baseBean) {
                        hideProgressDialog();
                        if (baseBean.isSuccess()) {
                            List<AllUserBean> allUserBeans = null;
                            try {
                                allUserBeans = new Gson().fromJson(baseBean.getJsonData(), new TypeToken<List<AllUserBean>>() {
                                }.getType());
                            } catch (JsonSyntaxException e) {
                                e.printStackTrace();
                            }
                            mList.clear();
                            mList.addAll(allUserBeans);
                            mAdapter.notifyDataSetChanged();
                        } else {
                            ComResultTools.resultData(VipApplyActivity.this, baseBean);
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
    public void onClick(View view, int position) {
        switch (view.getId()) {
            case R.id.tv_agree:
                agreeApply("1", position);
                break;
            case R.id.tv_reject:
                agreeApply("0", position);
                break;
        }
    }

    /**
     * @param applyCode applyCode:'0'.拒绝,'1'.同意
     */
    private void agreeApply(final String applyCode, final int position) {
        showProgressDialog();
        final AllUserBean allUserBean = mList.get(position);
        Api.getApiService().getApplyMember(applyCode, allUserBean.getApplyId() + "", getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Action1<BaseBean>() {
                    @Override
                    public void call(BaseBean baseBean) {
                        hideProgressDialog();
                        if (baseBean.isSuccess()) {
                            if (applyCode.equals("1"))
                                EventBus.getDefault().post(new ClubVipEvent(true));
                            allUserBean.setApply_status(Integer.parseInt(applyCode));
                            mList.set(position, allUserBean);
                            mAdapter.notifyDataSetChanged();
                        } else {
                            ComResultTools.resultData(VipApplyActivity.this, baseBean);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        hideProgressDialog();
                        RxToast.normal("网络异常");
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
