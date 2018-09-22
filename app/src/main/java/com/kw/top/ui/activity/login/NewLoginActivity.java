package com.kw.top.ui.activity.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.kw.top.R;
import com.kw.top.base.BaseActivity_;
import com.kw.top.bean.BaseBean;
import com.kw.top.retrofit.Api;
import com.kw.top.utils.RxToast;
import com.kw.top.utils.Tool;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


public class NewLoginActivity extends BaseActivity_ {


    @BindView(R.id.tv_qh)
    TextView tv_qh;
    @BindView(R.id.image_qh)
    ImageView image_qh;
    @BindView(R.id.ed_phone)
    EditText ed_phone;
    @BindView(R.id.ed_code)
    EditText ed_code;
    @BindView(R.id.tv_send_code)
    TextView tv_send_code;
    @BindView(R.id.but_login)
    Button but_login;


    @Override
    public int getContentView() {
        return R.layout.new_login_activity;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        StatusBarUtil.setTranslucent(this, 0);
        ButterKnife.bind(this);
    }


    @OnClick({R.id.tv_send_code, R.id.but_login})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.tv_send_code:
                sendCode(ed_phone.getText().toString(), tv_send_code);
                break;
            case R.id.but_login:
                break;
        }
    }


    /**
     * 发送验证码
     *
     * @param phone 手机号码
     * @param view  发送验证码空间
     */
    public void sendCode(final String phone, final TextView view) {
        if (TextUtils.isEmpty(phone)) {
            RxToast.normal("手机号码不能为空");
            return;
        }
        if (!Tool.isChinaPhoneLegal(phone)) {
            RxToast.normal("手机号码有误");
            return;
        }
        Api.getApiService().sendPhoneCode(phone)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Action1<BaseBean>() {
                    @Override
                    public void call(BaseBean baseBean) {
                        RxToast.normal("验证码已发送，请注意查收");
                        Tool.countDown(view, 60000, 1000, "获取验证码");
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        // mView.sendCodeResult(null);
                    }
                });

    }


}
