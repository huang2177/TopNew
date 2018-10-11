package com.kw.top.ui.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kw.top.R;
import com.kw.top.base.BaseActivity;
import com.kw.top.base.BaseActivity_;
import com.kw.top.bean.BaseBean;
import com.kw.top.retrofit.Api;
import com.kw.top.tools.ComResultTools;
import com.kw.top.tools.ConstantValue;
import com.kw.top.utils.RxToast;
import com.kw.top.utils.SPUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * author: 正义
 * date  : 2018/4/17
 * desc  : 登录 -- 选择性别
 */

public class SexActivity extends BaseActivity_ {

    ImageView mIvBack;
    TextView mTvTitleRight;
    RelativeLayout mRelativeTitle;
    private String sex = "";//性别('0'.女,'1'.男)
    private boolean setSexSuccess = false;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.image_man)
    ImageView mIvMan;
    @BindView(R.id.image_gird)
    ImageView mIvWoman;

    @Override
    public int getContentView() {
        return R.layout.activity_sex;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        ButterKnife.bind(this);
        initView();
    }

    public void initView() {
        mTvTitle.setText("选择性别");
        mTvTitle.setTextSize(16);
    }

    @OnClick({R.id.iv_back, R.id.image_man, R.id.image_gird, R.id.but_next})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.image_man:
                sex = "1";
                mIvMan.setBackground(getResources().getDrawable(R.drawable.shape_yellow_sex_bg));
                mIvWoman.setBackground(getResources().getDrawable(R.drawable.shape_yellow_sex_no_bg));
                break;
            case R.id.image_gird:
                sex = "0";
                mIvMan.setBackground(getResources().getDrawable(R.drawable.shape_yellow_sex_no_bg));
                mIvWoman.setBackground(getResources().getDrawable(R.drawable.shape_yellow_sex_bg));
                break;
            case R.id.but_next:
                if (TextUtils.isEmpty(sex)) {
                    RxToast.normal("请选择性别");
                } else {
                    if (setSexSuccess) {
                        Intent intent = new Intent(this, GirlInfoActivity.class);
                        startActivity(intent);
                    } else {
                        confirm();
                    }
                }
                break;
        }
    }

    /**
     * 设置性别
     */
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
                                Intent intent = new Intent(SexActivity.this, GirlInfoActivity.class);
                                startActivity(intent);
                            } else {
                                startActivity(BaseInfoActivity.class);
                                finish();
                            }
                        } else {
                            ComResultTools.resultData(SexActivity.this, baseBean);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                });
    }
}
