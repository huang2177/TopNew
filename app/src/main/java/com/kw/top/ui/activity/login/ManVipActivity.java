package com.kw.top.ui.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kw.top.R;
import com.kw.top.base.BaseActivity;
import com.kw.top.bean.event.EventMoney;
import com.kw.top.tools.ConstantValue;
import com.kw.top.ui.activity.AlipayActivity;
import com.kw.top.ui.activity.person_info.EditInfoActivity;
import com.kw.top.utils.RxToast;
import com.kw.top.utils.SPUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * author: Administrator
 * data  : 2018/4/30
 * des   : 登录 -- 会员选择
 */

public class ManVipActivity extends BaseActivity implements View.OnClickListener {

    ImageView mIvBack;
    TextView mTvTitle;
    TextView mTvTitleRight;
    RelativeLayout mRelativeTitle;
    ImageView mIvCopper;
    TextView mCopper1;
    TextView mCopper2;
    LinearLayout mCopper3;
    RelativeLayout mRlCopper;
    ImageView mIvSilver;
    TextView mSilver1;
    TextView mSilver2;
    LinearLayout mSilver3;
    RelativeLayout mRlSilver;
    ImageView mIvGold;
    TextView mGold1;
    TextView mGold2;
    LinearLayout mGold3;
    TextView mGold4;
    RelativeLayout mRlGlod;
    TextView mTvSelect;
    private String card_type;
    private String money;
    private static final int REQUEST_PAY = 1;
    private int allMoney = 0;

    @Override
    public int getContentView() {
        return R.layout.activity_vip_select;
    }

    public void initView() {
        mTvTitleRight.setText("暂不充值");
    }

    public void initListener() {
        mTvTitleRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditInfoActivity.startActivity(ManVipActivity.this, false);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        card_type = "";
        if (resultCode == RESULT_OK) {
            allMoney = Integer.parseInt(money) + allMoney;
            if (allMoney >= 5000 || Integer.parseInt(money)>=5000) {
                SPUtils.saveBoolean(ManVipActivity.this, ConstantValue.KEY_PRIVATE, true);
            }
            EventBus.getDefault().post(new EventMoney(true));
            HeartBeatGril2.startActivity(this, true);
        }
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
        mIvCopper = findViewById(R.id.iv_copper);
        mCopper1 = findViewById(R.id.copper1);
        mCopper2 = findViewById(R.id.copper2);
        mCopper3 = findViewById(R.id.copper3);
        mRlCopper = findViewById(R.id.rl_copper);
        mIvSilver = findViewById(R.id.iv_silver);
        mSilver1 = findViewById(R.id.silver1);
        mSilver2 = findViewById(R.id.silver2);
        mSilver3 = findViewById(R.id.silver3);
        mRlSilver = findViewById(R.id.rl_silver);
        mIvGold = findViewById(R.id.iv_gold);
        mGold1 = findViewById(R.id.gold1);
        mGold2 = findViewById(R.id.gold2);
        mGold3 = findViewById(R.id.gold3);
        mGold4 = findViewById(R.id.gold4);
        mRlGlod = findViewById(R.id.rl_glod);
        mTvSelect = findViewById(R.id.tv_select);
        mRlCopper.setOnClickListener(this);
        mRlGlod.setOnClickListener(this);
        mRlSilver.setOnClickListener(this);
        mTvSelect.setOnClickListener(this);
        mIvBack.setOnClickListener(this);
        initView();
        mRelativeTitle.setBackgroundColor(getResources().getColor(R.color.transparent));
        initListener();
    }

    @Override
    public void onClick(View v) {
        mIvCopper.setVisibility(View.GONE);
        mIvGold.setVisibility(View.GONE);
        mIvSilver.setVisibility(View.GONE);
        switch (v.getId()) {
            case R.id.rl_copper:
                card_type = "1";
                money = "198";
                mIvCopper.setVisibility(View.VISIBLE);
                break;
            case R.id.rl_silver:
                card_type = "2";
                money = "5000";
                mIvSilver.setVisibility(View.VISIBLE);
                break;
            case R.id.rl_glod:
                card_type = "3";
                money = "10000";
                mIvGold.setVisibility(View.VISIBLE);
                break;
            case R.id.tv_select:
                if (TextUtils.isEmpty(card_type)) {
                    RxToast.normal("请选择需要办理的会员卡");
                } else {
                    AlipayActivity.startActivity(this, card_type, money, REQUEST_PAY);
                }
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }
}
