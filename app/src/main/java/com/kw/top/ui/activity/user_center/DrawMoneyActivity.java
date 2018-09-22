package com.kw.top.ui.activity.user_center;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.AuthTask;
import com.google.gson.internal.LinkedTreeMap;
import com.kw.top.R;
import com.kw.top.base.BaseActivity;
import com.kw.top.bean.AuthInfoBean;
import com.kw.top.bean.BaseBean;
import com.kw.top.bean.event.CenterCouponEvent;
import com.kw.top.retrofit.Api;
import com.kw.top.tools.ComResultTools;
import com.kw.top.ui.activity.login.LoginActivity;
import com.kw.top.utils.AuthResult;
import com.kw.top.utils.RxToast;
import com.kw.top.utils.SPUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * author: zy
 * data  : 2018/5/22
 * des   : 提现
 */

public class DrawMoneyActivity extends BaseActivity implements View.OnClickListener {

    private static int REQUEST_CODE = 1;
    ImageView mIvBack;
    TextView mTvTitle;
    TextView mTvTitleRight;
    RelativeLayout mRelativeTitle;
    EditText mEtNum;
    TextView mTvAllDraw;
    TextView mTvPayNumber;
    LinearLayout mLlBingPay;
    private String type;//0 绑定 1解绑

    @Override
    public int getContentView() {
        return R.layout.activity_draw_money;
    }

    public void initView() {
        mTvTitle.setText("提现");
        queryAlipay();
    }

    //查询是否绑定支付宝 0未 1绑定
    private void queryAlipay() {
        Api.getApiService().queryAlipayNum(getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Action1<BaseBean>() {
                    @Override
                    public void call(BaseBean baseBean) {
                        hideProgressDialog();
                        if (baseBean.isSuccess()) {
                            LinkedTreeMap<String, String> map = (LinkedTreeMap<String, String>) baseBean.getData();
                            String alipayNum = map.get("alipayNum");
                            if (TextUtils.isEmpty(alipayNum)) {
                                mTvPayNumber.setText("尚未绑定支付宝账号");
                                type = "0";
                            } else {
                                mTvPayNumber.setText("已绑定");
                                type = "1";
                            }
                        } else {
                            ComResultTools.resultData(DrawMoneyActivity.this,baseBean);
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
        ButterKnife.bind(this);
        mIvBack = findViewById(R.id.iv_back);
        mTvTitle = findViewById(R.id.tv_title);
        mTvTitleRight = findViewById(R.id.tv_title_right);
        mRelativeTitle = findViewById(R.id.relative_title);
        mEtNum = findViewById(R.id.et_num);
        mTvAllDraw = findViewById(R.id.tv_all_draw);
        mTvPayNumber = findViewById(R.id.tv_pay_number);
        mLlBingPay = findViewById(R.id.ll_bing_pay);
        initView();
    }

    @OnClick({R.id.iv_back, R.id.tv_all_draw, R.id.ll_bing_pay})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_all_draw:
                String num = mEtNum.getText().toString().trim();
                if (!TextUtils.isEmpty(num))
                    withDraw(num);
                break;
            case R.id.ll_bing_pay:
                if (TextUtils.isEmpty(type))
                    return;
                //绑定支付宝
                if (type.equals("0"))
                    getAuthBindInfo();
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }

    private void withDraw(String num) {
        showProgressDialog();
        Api.getApiService().withDraw(num, getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Action1<BaseBean>() {
                    @Override
                    public void call(BaseBean baseBean) {
                        hideProgressDialog();
                        if (baseBean.isSuccess()) {
                            RxToast.normal("提现成功");
                            mEtNum.setText("");
                            EventBus.getDefault().post(new CenterCouponEvent(true, true));
                        } else {
                            RxToast.normal(baseBean.getMsg());
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        hideProgressDialog();
                    }
                });
    }

    private void getAuthBindInfo() {
        showProgressDialog();
        Api.getApiService().toDrawMoney(getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Action1<BaseBean<AuthInfoBean>>() {
                    @Override
                    public void call(BaseBean<AuthInfoBean> baseBean) {
                        hideProgressDialog();
                        if (baseBean.isSuccess()) {
                            authAlipay(baseBean.getData().getResponseBody());
                        } else {
                            RxToast.normal(baseBean.getMsg());
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        hideProgressDialog();
                    }
                });
    }

    private static final int SDK_AUTH_FLAG = 2;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_AUTH_FLAG: {
                    @SuppressWarnings("unchecked")
                    AuthResult authResult = new AuthResult((Map<String, String>) msg.obj, true);
                    String resultStatus = authResult.getResultStatus();

                    // 判断resultStatus 为“9000”且result_code
                    // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
                    if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
                        // 获取alipay_open_id，调支付时作为参数extern_token 的value
                        // 传入，则支付账户为该授权账户
                        Toast.makeText(DrawMoneyActivity.this,
                                "授权成功\n", Toast.LENGTH_SHORT)
                                .show();
                        bindAlipay(authResult.getUserId());

                    } else {
                        // 其他状态值则为授权失败
                        Toast.makeText(DrawMoneyActivity.this,
                                "授权失败" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT).show();

                    }
                    break;
                }
                default:
                    break;
            }
        }

        ;
    };

    private void authAlipay(final String authInfo) {
        Runnable authRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造AuthTask 对象
                AuthTask authTask = new AuthTask(DrawMoneyActivity.this);
                // 调用授权接口，获取授权结果
                Map<String, String> result = authTask.authV2(authInfo, true);

                Message msg = new Message();
                msg.what = SDK_AUTH_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread authThread = new Thread(authRunnable);
        authThread.start();
    }


    //绑定支付宝
    private void bindAlipay(final String ali_num) {
        showProgressDialog();
        Api.getApiService().bindAlipay(ali_num, getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Action1<BaseBean>() {
                    @Override
                    public void call(BaseBean baseBean) {
                        if (baseBean.isSuccess()) {
                            hideProgressDialog();
                            mTvPayNumber.setText("已绑定");
                            type = "1";
                        } else if (baseBean.getCode().equals("-44")) {
                            RxToast.normal(getResources().getString(R.string.login_out));
                            SPUtils.clear(DrawMoneyActivity.this);
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
}
