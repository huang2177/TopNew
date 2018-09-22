package com.kw.top.ui.activity.task;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kw.top.R;
import com.kw.top.base.BaseActivity;
import com.kw.top.bean.BaseBean;
import com.kw.top.retrofit.Api;
import com.kw.top.tools.ComResultTools;
import com.kw.top.utils.RxToast;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * author: zy
 * data  : 2018/5/27
 * des   : 发布任务
 */

public class PublishTaskActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener, View.OnClickListener {

    ImageView mIvBack;
    TextView mTvTitle;
    TextView mTvTitleRight;
    RelativeLayout mRelativeTitle;
    EditText mEtTaskDesc;
    RadioButton mRbPic;
    RadioButton mRbVideo;
    RadioGroup mRadioGroup;
    LinearLayout mLl1;
    LinearLayout mLl2;
    LinearLayout mLl3;
    LinearLayout mLl4;
    LinearLayout mLl5;
    LinearLayout mLl6;
    LinearLayout mLl7;
    LinearLayout mLl8;
    EditText mEtAmount;
    EditText mEtNum;
    LinearLayout mLl9;
    TextView mTvConfirm;
    @BindView(R.id.et_club_award)
    EditText mEtClubAward;
    @BindView(R.id.ll_club_award)
    LinearLayout mLlClubAward;
    @BindView(R.id.ll_award)
    LinearLayout mLlAward;
    @BindView(R.id.tv_award)
    TextView mTvAward;
    private String describes, type; //'01'.图片类型,'02'.视频类型
    private String amount, num;
    private String groupId;


    public static final void startActivity(Context context, String groupId) {
        Intent intent = new Intent(context, PublishTaskActivity.class);
        intent.putExtra("groupId", groupId);
        context.startActivity(intent);
    }

    @Override
    public int getContentView() {
        return R.layout.activity_publish_task;
    }

    public void initView() {
        mTvTitle.setText("发布任务");
        groupId = getIntent().getStringExtra("groupId");
        if (!TextUtils.isEmpty(groupId)) {
            mLlAward.setVisibility(View.GONE);
            mLlClubAward.setVisibility(View.VISIBLE);
            mTvAward.setVisibility(View.GONE);
        }
    }

    public void initListener() {
        mRadioGroup.setOnCheckedChangeListener(this);
        mEtAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                amount = s.toString();
                mLl9.performClick();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mEtNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                num = s.toString();
                mLl9.performClick();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (i) {
            case R.id.rb_pic:
                type = "01";
                break;
            case R.id.rb_video:
                type = "02";
                break;
        }
    }

    //发布任务
    private void sendTask() {
        describes = mEtTaskDesc.getText().toString().trim();
        if (TextUtils.isEmpty(describes)) {
            RxToast.normal("请输入发布内容");
            return;
        } else if (TextUtils.isEmpty(type)) {
            RxToast.normal("请选择任务类型");
            return;
        } else if (TextUtils.isEmpty(amount) && TextUtils.isEmpty(groupId)) {
            RxToast.normal("请选择任务奖励");
            return;
        } else if (TextUtils.isEmpty(num) && TextUtils.isEmpty(groupId)) {
            RxToast.normal("请选择奖励人数");
            return;
        }
        if (!TextUtils.isEmpty(groupId)) {
            setClubTask();
        } else {
            sendNormalTask();
        }
    }

    private void setClubTask() {
        showProgressDialog();
        Api.getApiService().sendClubTask(describes, groupId, type, getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Action1<BaseBean>() {
                    @Override
                    public void call(BaseBean baseBean) {
                        hideProgressDialog();
                        if (baseBean.isSuccess()) {
                            hideProgressDialog();
                            RxToast.normal("任务发布成功");
                            finish();
                        } else {
                            ComResultTools.resultData(PublishTaskActivity.this, baseBean);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        hideProgressDialog();
                    }
                });
    }

    private void sendNormalTask() {
        Api.getApiService().sendTask(describes, amount, num, type, getToken())
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
                        if (baseBean.isSuccess()) {
                            hideProgressDialog();
                            RxToast.normal("任务发布成功");
                            finish();
                        } else {
                            ComResultTools.resultData(PublishTaskActivity.this, baseBean);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        hideProgressDialog();
                    }
                });
    }

    private void setEmptyLl() {
        mLl1.setSelected(false);
        mLl2.setSelected(false);
        mLl3.setSelected(false);
        mLl4.setSelected(false);
        mLl5.setSelected(false);
        mLl6.setSelected(false);
        mLl7.setSelected(false);
        mLl8.setSelected(false);
        mLl9.setSelected(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        setContentView(getContentView());
        ButterKnife.bind(this);
        bindView();
        initView();
        initListener();
    }

    private void bindView() {
        mIvBack = findViewById(R.id.iv_back);
        mTvTitle = findViewById(R.id.tv_title);
        mTvTitleRight = findViewById(R.id.tv_title_right);
        mRelativeTitle = findViewById(R.id.relative_title);
        mEtTaskDesc = findViewById(R.id.et_task_desc);
        mRbPic = findViewById(R.id.rb_pic);
        mRbVideo = findViewById(R.id.rb_video);
        mRadioGroup = findViewById(R.id.radio_group);
        mLl1 = findViewById(R.id.ll1);
        mLl2 = findViewById(R.id.ll2);
        mLl3 = findViewById(R.id.ll3);
        mLl4 = findViewById(R.id.ll4);
        mLl5 = findViewById(R.id.ll5);
        mLl6 = findViewById(R.id.ll6);
        mLl7 = findViewById(R.id.ll7);
        mLl8 = findViewById(R.id.ll8);
        mEtAmount = findViewById(R.id.et_amount);
        mEtNum = findViewById(R.id.et_num);
        mLl9 = findViewById(R.id.ll9);
        mTvConfirm = findViewById(R.id.tv_confirm);

        mLl1.setOnClickListener(this);
        mLl2.setOnClickListener(this);
        mLl3.setOnClickListener(this);
        mLl4.setOnClickListener(this);
        mLl5.setOnClickListener(this);
        mLl6.setOnClickListener(this);
        mLl7.setOnClickListener(this);
        mLl8.setOnClickListener(this);
        mLl9.setOnClickListener(this);
        mTvConfirm.setOnClickListener(this);
        mIvBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll1:
                setEmptyLl();
                mLl1.setSelected(true);
                amount = "50";
                num = "3";
                break;
            case R.id.ll2:
                setEmptyLl();
                mLl2.setSelected(true);
                amount = "100";
                num = "6";
                break;
            case R.id.ll3:
                setEmptyLl();
                mLl3.setSelected(true);
                amount = "200";
                num = "12";
                break;
            case R.id.ll4:
                setEmptyLl();
                mLl4.setSelected(true);
                amount = "500";
                num = "14";
                break;
            case R.id.ll5:
                setEmptyLl();
                mLl5.setSelected(true);
                amount = "1000";
                num = "16";
                break;
            case R.id.ll6:
                setEmptyLl();
                mLl6.setSelected(true);
                amount = "2000";
                num = "20";
                break;
            case R.id.ll7:
                setEmptyLl();
                mLl7.setSelected(true);
                amount = "5000";
                num = "30";
                break;
            case R.id.ll8:
                setEmptyLl();
                mLl8.setSelected(true);
                amount = "10000";
                num = "40";
                break;
            case R.id.ll9:
                setEmptyLl();
                mLl9.setSelected(true);
                break;
            case R.id.tv_confirm:
                sendTask();
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }

}
