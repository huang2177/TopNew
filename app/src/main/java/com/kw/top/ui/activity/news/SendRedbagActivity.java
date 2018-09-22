package com.kw.top.ui.activity.news;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.kw.top.R;
import com.kw.top.base.BaseActivity;
import com.kw.top.bean.BaseBean;
import com.kw.top.bean.RedbagBean;
import com.kw.top.retrofit.Api;
import com.kw.top.retrofit.HttpHost;
import com.kw.top.tools.ComResultTools;
import com.kw.top.tools.GlideTools;
import com.kw.top.utils.RxToast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * author  ： zy
 * date    ： 2018/7/13
 * des     ：
 */

public class SendRedbagActivity extends BaseActivity {

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
    @BindView(R.id.et_amount)
    EditText mEtAmount;
    @BindView(R.id.et_num)
    EditText mEtNum;
    @BindView(R.id.tv_send_redbag)
    TextView mTvSendRedbag;
    @BindView(R.id.ll_num)
    LinearLayout mLlNum;
    private String redType = "0";//0个人红包 1群红包
    private String toUsername;
    private String amountSum;//红包总金额
    private String shareSum;//发放份数
    private String redPackageType = "1";//红包类型('0'.礼券,'1'.钻石)
    private String content = "[红包消息]";
    private String groupId = "";

    public static void startActivity(Activity context, String redType, String toUsername, int request_code,String head,String groupId) {
        Intent intent = new Intent(context, SendRedbagActivity.class);
        intent.putExtra("RED_TYPE", redType);
        intent.putExtra("TO_USER", toUsername);
        intent.putExtra("HEAD",head);
        intent.putExtra("GROUPID",groupId);
        context.startActivityForResult(intent, request_code);
    }

    @Override
    public int getContentView() {
        return R.layout.activity_send_redbag;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        ButterKnife.bind(this);

        redType = getIntent().getStringExtra("RED_TYPE");
        String nickname = getIntent().getStringExtra("TO_USER");
        String head = HttpHost.qiNiu + getIntent().getStringExtra("HEAD");
        groupId = getIntent().getStringExtra("GROUPID");

        if (redType.equals("1")) {
            mLlNum.setVisibility(View.VISIBLE);
            mCiHead.setImageResource(R.mipmap.icon_group);
        }else {
            if (redType.equals("1")) {
                mLlNum.setVisibility(View.VISIBLE);
                Glide.with(this).load(head).apply(GlideTools.getHeadOptions()).into(mCiHead);
            }
        }
        mTvNickname.setText(nickname);
        mTvTitle.setText("发红包");
        mRelativeTitle.setBackgroundColor(getResources().getColor(R.color.transparent));


    }


    @OnClick({R.id.iv_back, R.id.tv_send_redbag})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_send_redbag:
                amountSum = mEtAmount.getText().toString().trim();

                if (redType.equals("1")){
                    shareSum = mEtNum.getText().toString().trim();
                }else {
                    shareSum = "1";
                }

                if (TextUtils.isEmpty(amountSum)) {
                    RxToast.normal("请输入金额");
                    return;
                } else if (TextUtils.isEmpty(shareSum)){
                    RxToast.normal("请输入红包数量");
                    return;
                }else {
                    sendRedbag();
                }
                break;
        }
    }

    private void sendRedbag() {
        showProgressDialog();
        Api.getApiService().sendRedPackage(groupId,amountSum, shareSum, redPackageType, getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Action1<BaseBean>() {
                    @Override
                    public void call(BaseBean baseBean) {
                        hideProgressDialog();
                        if (baseBean.isSuccess()) {
                            try {
                                RedbagBean redbagBean = new Gson().fromJson(baseBean.getJsonData(), new TypeToken<RedbagBean>() {
                                }.getType());
                                Intent intent = new Intent();
                                intent.putExtra("REDBAG_ID", redbagBean.getRedPackageId());
                                intent.putExtra("CONTENT", content);
                                setResult(RESULT_OK, intent);
                                finish();
                            } catch (JsonSyntaxException e) {
                                e.printStackTrace();
                            }
                        } else {
                            ComResultTools.resultData(SendRedbagActivity.this, baseBean);
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
