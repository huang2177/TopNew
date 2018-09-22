package com.kw.top.ui.activity.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kw.top.R;
import com.kw.top.base.BaseActivity;
import com.kw.top.tools.ConstantValue;
import com.kw.top.ui.activity.AlipayActivity;
import com.kw.top.ui.activity.person_info.EditInfoActivity;
import com.kw.top.utils.SPUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * author: zy
 * data  : 2018/6/6
 * des   :
 */

public class WomanVipActivity extends BaseActivity implements View.OnClickListener{

    ImageView mIvBack;
    TextView mTvTitle;
    TextView mTvTitleRight;
    RelativeLayout mRelativeTitle;
    TextView mTvPay;
    private int card_type = 0;//0铜卡 1银卡 2金卡
    private static final int REQUEST_PAY = 1;

    public static void startActivity(Context context, String card_type) {
        Intent intent = new Intent(context, WomanVipActivity.class);
        intent.putExtra("TYPE", card_type);
        context.startActivity(intent);
    }

    @Override
    public int getContentView() {
        return R.layout.activity_vip_pay;
    }

    public void initView() {
        card_type = getIntent().getIntExtra("TYPE", 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
//            startActivity(BaseInfoActivity.class);
            SPUtils.saveBoolean(WomanVipActivity.this, ConstantValue.KEY_PRIVATE,true);
            EditInfoActivity.startActivity(this,false);
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
        mTvPay = findViewById(R.id.tv_pay);
        mIvBack.setOnClickListener(this);
        mTvPay.setOnClickListener(this);
        initView();
        mRelativeTitle.setBackgroundColor(getResources().getColor(R.color.transparent));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_pay:
                AlipayActivity.startActivity(this, "3", "10000", REQUEST_PAY);
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }
}
