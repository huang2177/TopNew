package com.kw.top.ui.activity.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.kw.top.R;
import com.kw.top.base.MVPBaseActivity;
import com.kw.top.bean.BaseBean;
import com.kw.top.ui.activity.login.contract.ForgetPwdContract;
import com.kw.top.ui.activity.login.presenter.ForgetPwdPresenter;
import com.kw.top.utils.RxToast;
import com.kw.top.utils.SPUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * author: Administrator
 * data  : 2018/5/2
 * des   : 忘记密码
 */

public class ForgetPwdActivity extends MVPBaseActivity<ForgetPwdContract.View, ForgetPwdPresenter> implements ForgetPwdContract.View
        , View.OnClickListener {

    ImageView mIvBack;
    TextView mTvTitle;
    TextView mTvTitleRight;
    RelativeLayout mRelativeTitle;
    EditText mEtPhone;
    EditText mEtCode;
    TextView mTvSend;
    EditText mEtPwd1;
    EditText mEtPwd2;
    TextView mTvConfirm;

    String phone, code, pwd1, pwd2;

    private String title = "忘记密码";

    public static void startActivity(Context context,String title){
        Intent intent = new Intent(context,ForgetPwdActivity.class);
        intent.putExtra("TITLE",title);
        context.startActivity(intent);
    }

    @Override
    public int getContentView() {
        return R.layout.activity_forget_pwd;
    }

    private void initView() {
        title = getIntent().getStringExtra("TITLE");
        if (TextUtils.isEmpty(title)){
            title = "忘记密码";
        }
        mTvTitle.setText(title);
        StatusBarUtil.setColor(this, getResources().getColor(R.color.black_bg));
    }

    Handler mHandler = new Handler();
    private int sum_time = 60 * 1000;

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            sum_time -= 1000;
            if (sum_time >= 1) {
                mTvSend.setText(sum_time / 1000 + "s");
                mTvSend.setBackgroundResource(R.drawable.shape_black_bg);
                mTvSend.setEnabled(false);
            } else {
                mTvSend.setText("重新发送");
                mTvSend.setEnabled(true);
                mTvSend.setBackgroundResource(R.drawable.tv_send_red);
            }
            mHandler.postDelayed(this, 1000);
        }
    };

    @Override
    public void sendCodeResult(BaseBean baseBean) {
        mTvSend.setEnabled(true);
        sum_time = 60 * 1000;
        if (baseBean.isSuccess()) {
            RxToast.normal("发送成功");
            mHandler.post(mRunnable);
        } else if (baseBean.getCode().equals("-44")) {
            RxToast.normal(getResources().getString(R.string.login_out));
            SPUtils.clear(ForgetPwdActivity.this);
            startActivity(LoginActivity.class);
            finish();
        }else {
            RxToast.normal("网络或服务器异常");
        }
    }

    @Override
    public void changePwdResult(BaseBean baseBean) {
        hideProgressDialog();
        if (baseBean.isSuccess()) {
            RxToast.normal("修改成功");
            startActivity(LoginActivity.class);
            finish();
        } else {
            RxToast.normal("修改失败");
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHandler != null) {
            mHandler.removeCallbacks(mRunnable);
            mHandler = null;
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
        mEtPhone = findViewById(R.id.et_phone);
        mEtCode = findViewById(R.id.et_code);
        mTvSend = findViewById(R.id.tv_send);
        mEtPwd1 = findViewById(R.id.et_pwd1);
        mEtPwd2 = findViewById(R.id.et_pwd2);
        mTvConfirm = findViewById(R.id.tv_confirm);
        mIvBack.setOnClickListener(this);
        mTvConfirm.setOnClickListener(this);
        mTvSend.setOnClickListener(this);
        initView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_send:
                phone = mEtPhone.getText().toString().trim();
                if (phone.length() < 11) {
                    RxToast.normal("请输入正确长度的手机号码");
                    return;
                }
                mPresenter.sendCode(phone);
                mTvSend.setEnabled(false);
                break;
            case R.id.tv_confirm:
                phone = mEtPhone.getText().toString().trim();
                code = mEtCode.getText().toString().trim();
                pwd1 = mEtPwd1.getText().toString().trim();
                pwd2 = mEtPwd2.getText().toString().trim();
                if (phone.length() < 11) {
                    RxToast.normal("请输入正确长度的手机号码");
                    return;
                } else if (TextUtils.isEmpty(code)) {
                    RxToast.normal("请输入验证码");
                    return;
                } else if (TextUtils.isEmpty(pwd1) || TextUtils.isEmpty(pwd2)) {
                    RxToast.normal("请输入新密码");
                    return;
                } else if (!pwd1.equals(pwd2)) {
                    RxToast.normal("请输入相同的新密码");
                    return;
                } else {
                    mPresenter.changePwd(phone, code, pwd1, pwd2);
                }
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }
}
