package com.kw.top.ui.activity.user_center;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.kw.top.R;
import com.kw.top.adapter.RedbagDetailsAdapter;
import com.kw.top.base.BaseActivity;
import com.kw.top.bean.ActiveDetailsBean;
import com.kw.top.bean.BaseBean;
import com.kw.top.bean.RedbagDetailBean;
import com.kw.top.retrofit.Api;
import com.kw.top.retrofit.HttpHost;
import com.kw.top.tools.ComResultTools;
import com.kw.top.tools.GlideTools;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * author: zy
 * data  : 2018/5/22
 * des   : 红包详情
 */

public class RedPacketDetailsActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_title_right)
    TextView mTvTitleRight;
    @BindView(R.id.relative_title)
    RelativeLayout mRelativeTitle;
    @BindView(R.id.ci_head)
    CircleImageView mCiHead;
    @BindView(R.id.tv_nickname)
    TextView mTvNickname;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.tv_desc)
    TextView mTvDesc;

    private String redbagID;
    private RedbagDetailsAdapter mAdapter;
    private List<RedbagDetailBean.RedPackageDetailsListBean> mList = new ArrayList<>();

    public static void startActivity(Context context, String redbagID) {
        Intent intent = new Intent(context, RedPacketDetailsActivity.class);
        intent.putExtra("REDBAGID", redbagID);
        context.startActivity(intent);
    }

    @Override
    public int getContentView() {
        return R.layout.activity_red_packet_details;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        ButterKnife.bind(this);
        mRelativeTitle.setBackgroundColor(getResources().getColor(R.color.transparent));
        mTvTitle.setText("红包详情");

        redbagID = getIntent().getStringExtra("REDBAGID");
        if (TextUtils.isEmpty(redbagID))
            return;

        mAdapter = new RedbagDetailsAdapter(this,mList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);

        initData();
    }

    private void initData() {
        Api.getApiService().redPackageDetails(redbagID, getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Action1<BaseBean>() {
                    @Override
                    public void call(BaseBean baseBean) {
                        if (baseBean.isSuccess()) {
                            try {
                                RedbagDetailBean redbagDetailBean = new Gson().fromJson(baseBean.getJsonData(), new TypeToken<RedbagDetailBean>() {
                                }.getType());
                                RedbagDetailBean.RedPackageDetailsBean detailsBean = redbagDetailBean.getRedPackageDetails();
                                Glide.with(RedPacketDetailsActivity.this).load(HttpHost.qiNiu + detailsBean.getHeadImg()).apply(GlideTools.getHeadOptions()).into(mCiHead);
                                mTvNickname.setText(detailsBean.getNickName());
                                String desc = "";
                                if (detailsBean.getSurplusShareSum() == 0){
                                    desc = "已被抢完,";
                                }else {
                                    desc = "剩余" + detailsBean.getSurplusShareSum()+"份,";
                                }
                                mTvDesc.setText(detailsBean.getShareSum()+"个红包,"+desc+"红包总金额¥"+detailsBean.getAmountSum());

                                mList.addAll(redbagDetailBean.getRedPackageDetailsList());
                                mAdapter.notifyDataSetChanged();
                            } catch (JsonSyntaxException e) {
                                e.printStackTrace();
                            }
                        } else {
                            ComResultTools.resultData(RedPacketDetailsActivity.this, baseBean);
                        }


                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                });

    }


    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }
}
