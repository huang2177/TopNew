package com.kw.top.ui.activity.login;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kw.top.R;
import com.kw.top.base.BaseActivity;
import com.kw.top.bean.BaseBean;
import com.kw.top.retrofit.Api;
import com.kw.top.tools.ComResultTools;
import com.kw.top.tools.ConstantValue;
import com.kw.top.ui.activity.person_info.EditInfoActivity;
import com.kw.top.utils.RxToast;
import com.kw.top.utils.SPUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * author: 正义
 * date  : 2018/4/17
 * desc  : 登录 -- 选择性别
 */

public class SexActivity extends BaseActivity implements View.OnClickListener{

    ImageView mIvBack;
    TextView mTvTitle;
    TextView mTvTitleRight;
    RelativeLayout mRelativeTitle;
    TextView mTvManBg;
    ImageView mIvMan;
    TextView mTvWomanBg;
    ImageView mIvWoman;
    TextView mTvConfirm;
    private String sex = "";//性别('0'.女,'1'.男)
    private String token;
    private boolean setSexSuccess = false;

    @Override
    public int getContentView() {
        return R.layout.activity_sex;
    }

    public void initView() {
        mTvTitle.setText("选择性别");
        mTvTitle.setTextSize(15);
    }

    public void confirm() {
        Api.getApiService().addSex(sex, getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Action1<BaseBean>() {
                    @Override
                    public void call(BaseBean baseBean) {
                        hideProgressDialog();
                        if (baseBean.isSuccess()) {
                            RxToast.normal("性别设置成功");
                            setSexSuccess = true;
                            SPUtils.saveString(SexActivity.this, ConstantValue.KEY_SEX, sex);
                            if (sex.equals("0")) {
                                //女
                                startActivity(VideoVerifyActivity.class);
                                finish();
                            } else {
                                startActivity(HeartBeatGril1.class);
                                finish();
                            }
                        }else {
                            ComResultTools.resultData(SexActivity.this,baseBean);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

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
        mTvManBg = findViewById(R.id.tv_man_bg);
        mIvMan = findViewById(R.id.iv_man);
        mTvWomanBg = findViewById(R.id.tv_woman_bg);
        mIvWoman = findViewById(R.id.iv_woman);
        mTvConfirm = findViewById(R.id.tv_confirm);
        mIvBack.setOnClickListener(this);
        mIvMan.setOnClickListener(this);
        mIvWoman.setOnClickListener(this);
        mTvConfirm.setOnClickListener(this);
        initView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_man:
                mIvMan.setImageResource(R.mipmap.icon_man);
                mIvWoman.setImageResource(R.mipmap.icon_woman_slt);
                mTvManBg.setVisibility(View.VISIBLE);
                mTvWomanBg.setVisibility(View.INVISIBLE);
                sex = "1";
                break;
            case R.id.iv_woman:
                mIvMan.setImageResource(R.mipmap.icon_man_slt);
                mIvWoman.setImageResource(R.mipmap.icon_woman);
                mTvManBg.setVisibility(View.INVISIBLE);
                mTvWomanBg.setVisibility(View.VISIBLE);
                sex = "0";
                break;
            case R.id.tv_confirm:
                if (TextUtils.isEmpty(sex)) {
                    RxToast.normal("请选择性别");
                } else {
                    if (setSexSuccess) {
                        startActivity(VideoVerifyActivity.class);
                    } else {
                        confirm();
                    }

                }
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }
}
