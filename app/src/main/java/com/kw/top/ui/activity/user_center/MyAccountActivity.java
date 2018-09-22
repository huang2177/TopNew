package com.kw.top.ui.activity.user_center;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.kw.top.R;
import com.kw.top.adapter.DiamonAdapter;
import com.kw.top.base.BaseActivity;
import com.kw.top.bean.BaseBean;
import com.kw.top.bean.DiamondBean;
import com.kw.top.bean.MyAccountBean;
import com.kw.top.bean.event.CenterCouponEvent;
import com.kw.top.retrofit.Api;
import com.kw.top.tools.ComResultTools;
import com.kw.top.tools.ConstantValue;
import com.kw.top.ui.activity.AlipayActivity;
import com.kw.top.utils.SPUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * author: zy
 * data  : 2018/5/9
 * des   : 我的账户
 */

public class MyAccountActivity extends BaseActivity {
    private ImageView mIvBack;
    private TextView mTvTitle;
    private TextView mTvTitleRight;
    private RelativeLayout mRelativeTitle;
    private TextView mTvSurplus;
    private TextView mTvAllGift;
    private LinearLayout mLlSendGift;
    private TextView mTvAllRedPacket;
    private LinearLayout mLlSendRedPacket;
    private TextView mTvTakeOut;
    private List<DiamondBean.ProductlListBean> diamonList = new ArrayList<>();
    private Dialog mDialog;
    private DiamonAdapter mDiamonAdapter;
    private static final int REQUEST_PAY = 1;
    private static final int REQUEST_DRAW = 2;
    private int allDiamon;//账户余额

    @Override
    public int getContentView() {
        return R.layout.activity_my_account;
    }

    public void initView() {
        mIvBack = findViewById(R.id.iv_back);
        mTvTitle = findViewById(R.id.tv_title);
        mTvTitleRight = findViewById(R.id.tv_title_right);
        mRelativeTitle = findViewById(R.id.relative_title);
        mTvSurplus = findViewById(R.id.tv_surplus);
        mTvAllGift = findViewById(R.id.tv_all_gift);
        mLlSendGift = findViewById(R.id.ll_send_gift);
        mTvAllRedPacket = findViewById(R.id.tv_all_red_packet);
        mLlSendRedPacket = findViewById(R.id.ll_send_red_packet);
        mTvTakeOut = findViewById(R.id.tv_take_out);

        mTvTitle.setText("我的账户");
        mTvTitleRight.setText("充值");
        String sex = SPUtils.getString(this, ConstantValue.KEY_SEX, "1");
        if (sex.equals("0")) {//女
            mTvTakeOut.setVisibility(View.VISIBLE);
        }
    }

    public void initData() {
        showProgressDialog();
        Api.getApiService().myAccount(getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                    }
                })
                .subscribe(new Action1<BaseBean>() {
                    @Override
                    public void call(BaseBean baseBean) {
                        hideProgressDialog();
                        if (baseBean.isSuccess()) {
                            MyAccountBean myAccountBean = null;
                            try {
                                myAccountBean = new Gson().fromJson(baseBean.getJsonData(), new TypeToken<MyAccountBean>() {
                                }.getType());
                            } catch (JsonSyntaxException e) {
                                e.printStackTrace();
                            }
                            allDiamon = (int) Double.parseDouble(myAccountBean.getJewelSum());
                            mTvSurplus.setText(allDiamon + "");
                            mTvAllGift.setText("总计 : " + (int) (Double.parseDouble(myAccountBean.getGiftJewel())));
                            mTvAllRedPacket.setText("总计 : " + (int) (Double.parseDouble(myAccountBean.getRedJewel())));
                        } else {
                            ComResultTools.resultData(MyAccountActivity.this, baseBean);
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
                getDiamondList();
            }
        });
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mLlSendGift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReceiveAndSendActivity.startActivity(MyAccountActivity.this, 3);
            }
        });
        mLlSendRedPacket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReceiveAndSendActivity.startActivity(MyAccountActivity.this, 2);
            }
        });
        mTvTakeOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(DrawMoneyActivity.class);
            }
        });
    }

    //获得充值钻石列表
    private void getDiamondList() {
        Api.getApiService().amountToJewelList(getToken())
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
                        if (baseBean.isSuccess()) {
                            try {
                                DiamondBean diamondBean = new Gson().fromJson(baseBean.getJsonData(), new TypeToken<DiamondBean>() {
                                }.getType());
                                diamonList.clear();
                                diamonList.addAll(diamondBean.getProductlList());
                                showDiamonDialog();
                            } catch (JsonSyntaxException e) {
                                e.printStackTrace();
                            }
                        } else {
                            ComResultTools.resultData(MyAccountActivity.this, baseBean);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        hideProgressDialog();
                    }
                });

    }

    private int convertNum;//充值数量

    //充值钻石dialog
    private void showDiamonDialog() {
        mDiamonAdapter = new DiamonAdapter(this, diamonList, 0);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_diamon, null, false);
        GridView gridView = (GridView) view.findViewById(R.id.grid_view_diamon);
        gridView.setAdapter(mDiamonAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mDialog.dismiss();
                DiamondBean.ProductlListBean productlListBean = diamonList.get(i);
                convertNum = Integer.parseInt(productlListBean.getChangeAmount());
                //支付宝充值
                AlipayActivity.startActivity(MyAccountActivity.this, productlListBean.getId() + "", productlListBean.getAmount(), REQUEST_PAY);
            }
        });

        mDialog = new Dialog(this);
        mDialog.setContentView(view);
        mDialog.setCanceledOnTouchOutside(true);
        Window window = mDialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.BOTTOM);
        mDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_PAY) {
            mTvSurplus.setText((allDiamon - convertNum) + "");
            EventBus.getDefault().post(new CenterCouponEvent(true, false));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        setContentView(getContentView());
        initView();
        initData();
        initListener();
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void reFreshCounpon(CenterCouponEvent centerCouponEvent) {
        if (centerCouponEvent != null && centerCouponEvent.isRefresh() && centerCouponEvent.isRefreshMyaccount()) {
            initData();
        }
    }
}
