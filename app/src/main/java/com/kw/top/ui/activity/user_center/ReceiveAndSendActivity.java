package com.kw.top.ui.activity.user_center;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kw.top.R;
import com.kw.top.adapter.ReceiveRedAdapter;
import com.kw.top.adapter.ReceiveSendGiftAdapter;
import com.kw.top.adapter.SendRedAdapter;
import com.kw.top.base.MVPBaseActivity;
import com.kw.top.bean.BaseBean;
import com.kw.top.bean.GiftBean;
import com.kw.top.bean.ReceiveGiftBean;
import com.kw.top.bean.ReceiveRedBean;
import com.kw.top.bean.ReceiveSendGiftBean;
import com.kw.top.bean.SendGiftBean;
import com.kw.top.bean.SendRedBean;
import com.kw.top.ui.activity.login.LoginActivity;
import com.kw.top.ui.activity.user_center.contract.ReceiveAndSendContract;
import com.kw.top.ui.activity.user_center.presenter.ReceiveAndSendPresenter;
import com.kw.top.utils.RxToast;
import com.kw.top.utils.SPUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * author: zy
 * data  : 2018/5/22
 * des   : 收到的红包/收到的礼物/送出的红包/送出的礼物
 */

public class ReceiveAndSendActivity extends MVPBaseActivity<ReceiveAndSendContract.View, ReceiveAndSendPresenter> implements ReceiveAndSendContract.View {

    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_title_right)
    TextView mTvTitleRight;
    @BindView(R.id.relative_title)
    RelativeLayout mRelativeTitle;
    @BindView(R.id.tv_num)
    TextView mTvNum;
    @BindView(R.id.iv_diamond)
    ImageView mIvDiamond;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    private int type = 0;//0 收到红包 1 收到礼物 2送出红包 3送出礼物
    private ReceiveSendGiftAdapter mReceiveSendGiftAdapter;
    private ReceiveRedAdapter mReceiveRedAdapter;
    private SendRedAdapter mSendRedAdapter;
    private List<ReceiveSendGiftBean> mReceiveGifList = new ArrayList<>();
    private List<SendRedBean.SendRedPackageListBean> sendRedList = new ArrayList<>();
    private List<ReceiveRedBean.GetRedCouponsListBean> receiveRedList = new ArrayList<>();

    public static void startActivity(Context context, int type) {
        Intent intent = new Intent(context, ReceiveAndSendActivity.class);
        intent.putExtra("TYPE", type);
        context.startActivity(intent);
    }

    @Override
    public int getContentView() {
        return R.layout.activity_receive_and_give;
    }

    public void initView() {
        showProgressDialog();
        type = getIntent().getIntExtra("TYPE", 0);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        initData();
    }

    public void initData() {
        switch (type) {
            case 0:
                mTvTitle.setText("我收到的红包");
                mReceiveRedAdapter = new ReceiveRedAdapter(this,receiveRedList);
                mRecyclerView.setAdapter(mReceiveRedAdapter);
                mPresenter.getReceiveRed(getToken());
                break;
            case 1:
                mTvTitle.setText("我收到的礼物");
                mReceiveSendGiftAdapter = new ReceiveSendGiftAdapter(this,mReceiveGifList,0);
                mRecyclerView.setAdapter(mReceiveSendGiftAdapter);
                mPresenter.getReceiveGift(getToken());
                break;
            case 2:
                mTvTitle.setText("我送出的红包");
                mSendRedAdapter = new SendRedAdapter(this,sendRedList);
                mRecyclerView.setAdapter(mSendRedAdapter);
                mPresenter.getSendRed(getToken());
                break;
            case 3:
                mTvTitle.setText("我送出的礼物");
                mReceiveSendGiftAdapter = new ReceiveSendGiftAdapter(this,mReceiveGifList,1);
                mRecyclerView.setAdapter(mReceiveSendGiftAdapter);
                mPresenter.getSendGift(getToken());
                break;
        }
    }

    //收到红包
    @Override
    public void receiveRedResult(BaseBean<ReceiveRedBean> baseBean) {
        hideProgressDialog();
        if (baseBean != null && baseBean.isSuccess()) {
            ReceiveRedBean receiveRedBean = baseBean.getData();
            mTvNum.setText("我一共收到了"+receiveRedBean.getGetRedAmountSum());
            mIvDiamond.setVisibility(View.VISIBLE);
            receiveRedList.addAll(receiveRedBean.getGetRedCouponsList());
            mReceiveRedAdapter.notifyDataSetChanged();
        } else if (baseBean.getCode().equals("-44")) {
            RxToast.normal(getResources().getString(R.string.login_out));
            SPUtils.clear(ReceiveAndSendActivity.this);
            startActivity(LoginActivity.class);
            finish();
        } else {
            RxToast.normal("数据访问失败");
        }
    }

    //收到礼物
    @Override
    public void receiveGiftResult(BaseBean<ReceiveGiftBean> baseBean) {
        hideProgressDialog();
        if (baseBean != null && baseBean.isSuccess()) {
            ReceiveGiftBean receiveGiftBean = baseBean.getData();
            if (null == receiveGiftBean)
                return;
            mTvNum.setText("我一共收到"+receiveGiftBean.getGiftCoupons()+"礼券");
//            mIvDiamond.setVisibility(View.VISIBLE);
            mReceiveGifList.addAll(receiveGiftBean.getMyGift());
            mReceiveSendGiftAdapter.notifyDataSetChanged();
        } else if (baseBean.getCode().equals("-44")) {
            RxToast.normal(getResources().getString(R.string.login_out));
            SPUtils.clear(ReceiveAndSendActivity.this);
            startActivity(LoginActivity.class);
            finish();
        } else {
            RxToast.normal("数据访问失败");
        }
    }

    //送出的红包
    @Override
    public void sendRedResult(BaseBean<SendRedBean> baseBean) {
        hideProgressDialog();
        if (baseBean != null && baseBean.isSuccess()) {
            SendRedBean sendRedBean = baseBean.getData();
            mTvNum.setText("我一共送出了"+sendRedBean.getSendRedPackageSum());
            mIvDiamond.setVisibility(View.VISIBLE);
            sendRedList.addAll(sendRedBean.getSendRedPackageList());
            mSendRedAdapter.notifyDataSetChanged();
        } else if (baseBean.getCode().equals("-44")) {
            RxToast.normal(getResources().getString(R.string.login_out));
            SPUtils.clear(ReceiveAndSendActivity.this);
            startActivity(LoginActivity.class);
            finish();
        } else {
            RxToast.normal("数据访问失败");
        }
    }

    //送出礼物
    @Override
    public void sendGiftResult(BaseBean<SendGiftBean> baseBean) {
        hideProgressDialog();
        if (baseBean != null && baseBean.isSuccess()) {
            SendGiftBean sendGiftBean = baseBean.getData();
            if (null == sendGiftBean)
                return;
            mTvNum.setText("我一共送出"+sendGiftBean.getSendGiftAmountSum());
            mIvDiamond.setVisibility(View.VISIBLE);
            mReceiveGifList.addAll(sendGiftBean.getSendGiftJewelList());
            mReceiveSendGiftAdapter.notifyDataSetChanged();
        } else if (baseBean.getCode().equals("-44")) {
            RxToast.normal(getResources().getString(R.string.login_out));
            SPUtils.clear(ReceiveAndSendActivity.this);
            startActivity(LoginActivity.class);
            finish();
        } else {
            RxToast.normal("数据访问失败");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        setContentView(getContentView());
        ButterKnife.bind(this);
        initView();
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }
}
