package com.kw.top.ui.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.kw.top.R;
import com.kw.top.base.BaseActivity;
import com.kw.top.bean.BaseBean;
import com.kw.top.bean.OrderInfoBean;
import com.kw.top.retrofit.Api;
import com.kw.top.tools.ComResultTools;
import com.kw.top.ui.activity.login.LoginActivity;
import com.kw.top.utils.RxToast;
import com.kw.top.utils.SPUtils;
import com.kw.top.view.RxDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.droidsonroids.gif.GifImageView;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * author  ： zy
 * date    ： 2018/6/26
 * des     ：
 */

public class AlipayActivity extends BaseActivity {

    @BindView(R.id.cancel_iv)
    ImageView mCancelIv;
    @BindView(R.id.price_tv)
    TextView mPriceTv;
    @BindView(R.id.alipay_radio_iv)
    ImageView mAlipayRadioIv;
    @BindView(R.id.alipay_layout)
    LinearLayout mAlipayLayout;
    @BindView(R.id.pay_btn_tv)
    TextView mPayBtnTv;
    @BindView(R.id.pay_layout)
    LinearLayout mPayLayout;
    private RxDialog mDialog;
    private View loading_view;

    public static void startActivity(Activity context, String id, String money, int request_code) {
        Intent intent = new Intent(context, AlipayActivity.class);
        intent.putExtra("ID", id);
        intent.putExtra("MONEY", money);
        context.startActivityForResult(intent, request_code);
        context.overridePendingTransition(R.anim.activity_alpha_in, 0);
    }

    private String id, money, orderInfo;

    @Override
    public int getContentView() {
        return R.layout.activity_alpay;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        ButterKnife.bind(this);
        initView();
    }

    public void initView() {
        id = getIntent().getStringExtra("ID");
        money = getIntent().getStringExtra("MONEY");
        if (TextUtils.isEmpty(id)) {
            RxToast.normal("产品有误");
            return;
        }
        mPriceTv.setText(money);
        initData();
    }

    public void initData() {
        mDialog = new RxDialog(this);
        loading_view = LayoutInflater.from(this).inflate(R.layout.dialog_loading, null);
        GifImageView imageView = loading_view.findViewById(R.id.gif_loading);
        Glide.with(this).load(R.mipmap.icon_loading).into(imageView);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setContentView(loading_view);
    }

    private static final int SDK_PAY_FLAG = 1;
    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == SDK_PAY_FLAG) {
                //resultStatus={9000};memo={};result={{"alipay_trade_app_pay_response":{"code":"10000","msg":"Success","app_id":"2018061360364297","auth_app_id":"2018061360364297","charset":"utf-8","timestamp":"2018-06-26 22:35:59","total_amount":"0.01","trade_no":"2018062621001004750531377234","seller_id":"2088031294063550","out_trade_no":"CZ20180626223534358306"},"sign":"glKo8qwpLYDaGLfrddYxP81tuMyfXuOZsbd7kw2syjsm3XSTOI50bRqO1YS0kbO2oVz03cTh1z0Pf9w2oJjxfIfySvra6Nitx9lMkz2r4NmgPR1b0nEoxx99nabCLE7MhU8xnLLeN6x7ll008M2woxfbzxZfychVsAvLKKXdZKJaaDoRikN9ksHZxMMvif+CmTpu/0WQREWKiqa2k1JpUScWeEWC6C9KF7Ubn6xg2Ff0SwAy16c4wWLZmWA2owP/Jewo1hgh7bO2U+kYUKpiSD3werwicysTmpCO8qi0mWT8PTNmCjGqEkmLZj2esMEib7lUeeE3STBC4BH8U+yEmw==","sign_type":"RSA2"}}
//                @SuppressWarnings("unchecked")
//                PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                /**
                 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                 */
//                String resultInfo = payResult.getResult();// 同步返回需要验证的信息
//                String resultStatus = payResult.getResultStatus();
                String result = msg.obj.toString();
                // 判断resultStatus 为9000则代表支付成功
                if (result.contains("resultStatus={9000}")) { //TextUtils.equals(resultStatus, "9000")
                    // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                    Toast.makeText(AlipayActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                } else {
                    // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                    Toast.makeText(AlipayActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    private void jumpAlipay() {
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(AlipayActivity.this);
                String result = alipay.pay(orderInfo, true);
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    private void getorderInfo() {
        showProgressDialog();
        //获得订单信息
        Api.getApiService().aliPay(id, getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Action1<BaseBean>() {
                    @Override
                    public void call(BaseBean baseBean) {
                        hideProgressDialog();
                        if (baseBean.isSuccess()) {
                            OrderInfoBean orderInfoBean = null;
                            try {
                                orderInfoBean = new Gson().fromJson(baseBean.getJsonData(), new TypeToken<OrderInfoBean>() {
                                }.getType());
                            } catch (JsonSyntaxException e) {
                                e.printStackTrace();
                            }
                            orderInfo = orderInfoBean.getAppOrderInfo();
                            jumpAlipay();
                        } else {
                            ComResultTools.resultData(AlipayActivity.this,baseBean);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        hideProgressDialog();
                        RxToast.normal("生成订单失败");
                    }
                });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.activity_bottom_close);
    }

    protected void onDestroy() {
        super.onDestroy();
        if (null != mHandler)
            mHandler.removeMessages(SDK_PAY_FLAG);
    }

    @OnClick({R.id.cancel_iv, R.id.pay_btn_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cancel_iv:
                finish();
                break;
            case R.id.pay_btn_tv:
                getorderInfo();
                break;
        }
    }

}
