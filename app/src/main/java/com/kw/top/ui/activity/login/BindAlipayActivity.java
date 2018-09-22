package com.kw.top.ui.activity.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kw.top.R;
import com.kw.top.base.BaseActivity;
import com.kw.top.bean.BaseBean;
import com.kw.top.retrofit.Api;
import com.kw.top.utils.RxToast;
import com.kw.top.utils.SPUtils;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * author: zy
 * data  : 2018/6/8
 * des   : 绑定/解绑支付宝
 */

public class BindAlipayActivity extends BaseActivity implements View.OnClickListener {

    ImageView mIvBack;
    TextView mTvTitle;
    TextView mTvTitleRight;
    RelativeLayout mRelativeTitle;
    TextView mTvBind;
    EditText mEditText;
    TextView mTvText;
    private String type = "";// 0 绑定 1解绑

    public static void startActivity(Activity context, String type, int request_code) {
        Intent intent = new Intent(context, BindAlipayActivity.class);
        intent.putExtra("TYPE", type);
        context.startActivityForResult(intent, request_code);
    }

    @Override
    public int getContentView() {
        return R.layout.activity_bind_alipay;
    }

    private void initView() {
        type = getIntent().getStringExtra("TYPE");
        if (TextUtils.isEmpty(type)) {
            return;
        }
        if (type.equals("0")) {
            mTvTitle.setText("绑定支付宝");
            mTvText.setVisibility(View.VISIBLE);
            mEditText.setVisibility(View.VISIBLE);
            mTvBind.setText("授权绑定");
        } else {
            mTvTitle.setText("解绑支付宝");
            mTvBind.setText("解除绑定");
        }
    }

    //绑定支付宝
    private void bindAlipay(final String ali_num) {
        showProgressDialog();
        Api.getApiService().bindAlipay(ali_num,getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Action1<BaseBean>() {
                    @Override
                    public void call(BaseBean baseBean) {
                        if (baseBean.isSuccess()) {
                            hideProgressDialog();
                            RxToast.normal("绑定成功");
                            Intent intent = new Intent();
                            intent.putExtra("NUM",ali_num);
                            intent.putExtra("TYPE","0");
                            setResult(RESULT_OK,intent);
                            finish();
                        } else if (baseBean.getCode().equals("-44")) {
                            RxToast.normal(getResources().getString(R.string.login_out));
                            SPUtils.clear(BindAlipayActivity.this);
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

    //解绑支付宝
    private void unBindAlipay() {
        Api.getApiService().unBindAlipay(getToken())
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
                        if (baseBean.isSuccess()){
                            hideProgressDialog();
                            Intent intent = new Intent();
                            intent.putExtra("TYPE","1");
                            setResult(RESULT_OK,intent);
                            finish();
                        }else if (baseBean.getCode().equals("-44")) {
                            RxToast.normal(getResources().getString(R.string.login_out));
                            SPUtils.clear(BindAlipayActivity.this);
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
        mEditText = findViewById(R.id.et_ali_num);
        mTvText = findViewById(R.id.tv_text);
        mTvBind = findViewById(R.id.tv_bind);
        mIvBack.setOnClickListener(this);
        mTvBind.setOnClickListener(this);
        initView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_bind:
                if (type.equals("0")) {
                    String num = mEditText.getText().toString().trim();
                    if (!TextUtils.isEmpty(num)){
                        bindAlipay(num);
                    }
                } else {
                    unBindAlipay();
                }
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }
}
