package com.kw.top.ui.activity.login;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.kw.top.R;
import com.kw.top.app.AppManager;
import com.kw.top.base.MVPBaseActivity;
import com.kw.top.bean.BaseBean;
import com.kw.top.bean.event.AppLoginEvent;
import com.kw.top.bean.event.RefreshFriendEvent;
import com.kw.top.bean.event.UserAvatarEvent;
import com.kw.top.retrofit.Api;
import com.kw.top.tools.ConstantValue;
import com.kw.top.ui.activity.NewMainActivity;
import com.kw.top.ui.activity.login.bean.NewLoginBean;
import com.kw.top.ui.activity.login.contract.LoginContract;
import com.kw.top.ui.activity.login.presenter.LoginPresenter;
import com.kw.top.utils.RxToast;
import com.kw.top.utils.SPUtils;
import com.kw.top.utils.Tool;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * author: 正义
 * date  : 2018/4/17
 * desc  :
 */

public class LoginActivity extends MVPBaseActivity<LoginContract.View, LoginPresenter> implements LoginContract.View {

    @BindView(R.id.tv_qh)
    EditText ed_qh;
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

    private AlertDialog dialog;
    private String phone, code, areaCode;

    @Override
    public int getContentView() {
        return R.layout.new_login_activity;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AppManager.getAppManager().finishAllActivity();
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        ButterKnife.bind(this);
        StatusBarUtil.setTranslucent(this, 0);
        initviews();
    }

    /**
     * 初始化
     */
    private void initviews() {
        ed_qh.setCursorVisible(false);   //隐藏+区号的  光标
    }


    @OnClick({R.id.tv_send_code, R.id.but_login, R.id.tv_qh})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.tv_send_code:
                sendCode(ed_qh.getText().toString(), ed_phone.getText().toString(), tv_send_code);
                break;
            case R.id.but_login:
                Login();
                break;
            case R.id.tv_qh:
                ed_qh.setCursorVisible(true);
                break;
        }
    }

    /**
     * 登陆
     */
    private void Login() {
        phone = ed_phone.getText().toString().trim();
        areaCode = ed_qh.getText().toString().trim();
        code = ed_code.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            RxToast.normal("请输入手机号");
            return;
        }
        if (TextUtils.isEmpty(code)) {
            RxToast.normal("验证码不能为空");
            return;
        }
        showProgressDialog();
        mPresenter.login(phone, code, areaCode);

    }


    @Override
    public void loginResult(BaseBean<NewLoginBean> baseBean) {
        if (null == baseBean) return;
        if (!baseBean.isSuccess()) {
            hideProgressDialog();
            RxToast.normal(baseBean.getMsg());
            return;
        }
        try {
            NewLoginBean loginBean = baseBean.getData();
            SPUtils.saveString(LoginActivity.this, ConstantValue.KEY_PHONE, phone);
            SPUtils.saveString(LoginActivity.this, ConstantValue.KEY_TOKEN, loginBean.getToken());
            SPUtils.saveString(LoginActivity.this, ConstantValue.KEY_USER_ID, loginBean.getUserId());
            SPUtils.saveString(LoginActivity.this, ConstantValue.KEY_ACCOUNT, loginBean.getAccount());
            SPUtils.saveString(LoginActivity.this, ConstantValue.KEY_CHAT_PWD, loginBean.getPassword());
            SPUtils.saveString(LoginActivity.this, ConstantValue.KEY_SEX, loginBean.getUserInfo().getSex());
            SPUtils.saveString(LoginActivity.this, ConstantValue.KEY_HEAD, loginBean.getUserInfo().getHeadImg());
            SPUtils.saveString(LoginActivity.this, ConstantValue.KEY_NAME, loginBean.getUserInfo().getNickName());
            SPUtils.saveString(LoginActivity.this, ConstantValue.KEY_PROVE_STATE, loginBean.getUserInfo().getProveState());
            SPUtils.saveString(LoginActivity.this, ConstantValue.KEY_VIP_GRADE, loginBean.getUserInfo().getGrade());
            SPUtils.saveString(this, ConstantValue.KEY_PROVE_STATE, loginBean.getUserInfo().getProveState() + "");

            EventBus.getDefault().post(new RefreshFriendEvent(true));
            EventBus.getDefault().post(new AppLoginEvent(true, loginBean.getToken()));
            EventBus.getDefault().post(new UserAvatarEvent(loginBean.getUserInfo().getHeadImg(), loginBean.getUserInfo().getNickName()));
            //如果状态等于1  就是没有注册的 区完善资料
            if ("1".equals(loginBean.getRegisterState())) {
                startActivity(SexActivity.class);
                finish();
                return;
            }
            //认证状态(0 未认证,1 已认证,2 浏览超时,3审核中,4 未通过)
            switch (loginBean.getUserInfo().getProveState()) {
                case "0":
                    hideProgressDialog();
                    if (loginBean.getUserInfo().getSex().equals("1")) {
                        //男
                        startActivity(NewMainActivity.class);
                    } else if (loginBean.getUserInfo().getSex().equals("0")) {
                        //女
                        startActivity(VideoVerifyActivity.class);
                    } else {
                        startActivity(SexActivity.class);
                    }
                    break;
                case "1":
                    Loginchat(loginBean);
                    break;
                case "3":
                    hideProgressDialog();
                    RxToast.normal("请您等待认证通过后再登录");
                    //showHint();
                    break;
                case "4":
                    hideProgressDialog();
                    RxToast.normal("视频审核未通过，请重新认证");
                    startActivity(VideoVerifyActivity.class);
                    break;
            }
        } catch (Exception e) {
        }
    }


    private void showHint() {
        dialog = new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("请您等待认证通过后再登录~")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }


    /**
     * 进入首页 保存信息
     *
     * @param loginBean
     */
    @SuppressLint("StaticFieldLeak")
    private void Loginchat(final NewLoginBean loginBean) {
        hideProgressDialog();
        if (TextUtils.isEmpty(loginBean.getUserInfo().getSex())) {
            startActivity(SexActivity.class);
            finish();
        } else {
            skipActivityAndFinish(LoginActivity.this, NewMainActivity.class);
        }
    }


    /**
     * 发送验证码
     *
     * @param phone 手机号码
     * @param view  发送验证码空间
     */
    public void sendCode(final String areaCode, final String phone, final TextView view) {
        if (TextUtils.isEmpty(phone)) {
            RxToast.normal("手机号码不能为空");
            return;
        }
        if (!Tool.isChinaPhoneLegal(phone)) {
            RxToast.normal("手机号码有误");
            return;
        }
        Api.getApiService().NewsendPhoneCode(areaCode, phone)
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


    /**
     * 点击系统返回键
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            AppManager.getAppManager().AppExit();
            LoginActivity.this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialog != null)
            dialog.dismiss();
    }
}
