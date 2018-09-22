package com.kw.top.ui.activity.club;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.kw.top.R;
import com.kw.top.base.BaseActivity;
import com.kw.top.bean.BaseBean;
import com.kw.top.bean.CircleNewsBean;
import com.kw.top.retrofit.Api;
import com.kw.top.tools.ComResultTools;
import com.kw.top.ui.activity.login.LoginActivity;
import com.kw.top.utils.RxToast;
import com.kw.top.utils.SPUtils;
import com.kw.top.utils.TimeUtils;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * author: zy
 * data  : 2018/5/27
 * des   : 创建俱乐部
 */

public class CreateClubActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener ,View.OnClickListener{

    ImageView mIvBack;
    TextView mTvTitle;
    TextView mTvTitleRight;
    RelativeLayout mRelativeTitle;
    EditText mEtClubName;
    RadioButton mRbMan;
    RadioButton mRbGril;
    RadioButton mRbAll;
    RadioGroup mRadioGroup;
    EditText mEtClubDesc;
    TextView mTvSendRedTime;
    EditText mEtRedAmount;
    private String joinClubCondition = "2"; //入会条件（'0'.女,'1'.男,'2'.全部）
    private String sendRedTime;

    @Override
    public int getContentView() {
        return R.layout.activity_create_club;
    }

    public void initView() {
        mTvTitle.setText("创建俱乐部");
        mTvTitleRight.setText("确认创建");
    }

    public void initListener() {
        mRadioGroup.setOnCheckedChangeListener(this);
        mTvTitleRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createClub();
            }
        });
    }

    private void showPickerView() {
        TimePickerView timePickerView = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                sendRedTime = date.getTime() + "";
                sendRedTime= TimeUtils.format(date.getTime(),"HH:ss:mm");
                mTvSendRedTime.setText(sendRedTime);
            }
        }).setType(new boolean[]{false, false, false, true, true, true})// 默认全部显示
                .setCancelText("取消")//取消按钮文字
                .setSubmitText("确定")//确认按钮文字
                .setContentSize(18)//滚轮文字大小
                .setTitleSize(20)//标题文字大小
                .setTitleText("选择时间")//标题文字
                .setOutSideCancelable(false)//点击屏幕，点在控件外部范围时，是否取消显示
                .isCyclic(false)//是否循环滚动
                .setTitleColor(Color.BLACK)//标题文字颜色
                .setSubmitColor(Color.BLUE)//确定按钮文字颜色
                .setCancelColor(Color.BLUE)//取消按钮文字颜色
                .setTitleBgColor(0xFF666666)//标题背景颜色 Night mode
                .setBgColor(0xFF333333)//滚轮背景颜色 Night mode
                .setLabel("", "", "", "时", "分", "秒")//默认设置为年月日时分秒
                .build();
        timePickerView.show();

    }

    private void createClub() {
        String clubName = mEtClubName.getText().toString().trim();
        if (TextUtils.isEmpty(clubName)) {
            RxToast.normal("请输入俱乐部名称");
            return;
        }
        if (TextUtils.isEmpty(sendRedTime)) {
            RxToast.normal("请选择发红包时间");
            return;
        }
        String desc = mEtClubDesc.getText().toString().trim();
        String redAmount = mEtRedAmount.getText().toString().trim();
        if (TextUtils.isEmpty(redAmount)) {
            RxToast.normal("请输入每日发放红包金额");
            return;
        }
        showProgressDialog();
        Api.getApiService().createClub(clubName, joinClubCondition, desc, sendRedTime, redAmount, getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Action1<BaseBean>() {
                    @Override
                    public void call(BaseBean baseBean) {
                        hideProgressDialog();
                        if (baseBean.isSuccess()) {
                            RxToast.normal("创建成功");
                            finish();
                        }else {
                            ComResultTools.resultData(CreateClubActivity.this,baseBean);
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
    public void onCheckedChanged(RadioGroup radioGroup, int checkId) {
        switch (checkId) {
            case R.id.rb_man:
                joinClubCondition = "1";
                break;
            case R.id.rb_gril:
                joinClubCondition = "0";
                break;
            case R.id.rb_all:
                joinClubCondition = "2";
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        setContentView(getContentView());
        mIvBack = findViewById(R.id.iv_back);
        mTvTitle= findViewById(R.id.tv_title);
        mTvTitleRight= findViewById(R.id.tv_title_right);
        mRelativeTitle= findViewById(R.id.relative_title);
        mEtClubName= findViewById(R.id.et_club_name);
        mRbMan= findViewById(R.id.rb_man);
        mRbGril= findViewById(R.id.rb_gril);
        mRbAll= findViewById(R.id.rb_all);
        mRadioGroup= findViewById(R.id.radio_group);
        mEtClubDesc= findViewById(R.id.et_club_desc);
        mTvSendRedTime= findViewById(R.id.tv_send_red_time);
        mEtRedAmount= findViewById(R.id.et_red_amount);
        mIvBack.setOnClickListener(this);
        mTvSendRedTime.setOnClickListener(this);
        initView();
        initListener();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_send_red_time:
                showPickerView();
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }
}
