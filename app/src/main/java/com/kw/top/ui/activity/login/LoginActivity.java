package com.kw.top.ui.activity.login;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.jaeger.library.StatusBarUtil;
import com.kw.top.R;
import com.kw.top.app.AppManager;
import com.kw.top.base.MVPBaseActivity;
import com.kw.top.bean.BaseBean;
import com.kw.top.retrofit.Api;
import com.kw.top.tools.ConstantValue;
import com.kw.top.tools.Logger;
import com.kw.top.ui.activity.NewMainActivity;
import com.kw.top.ui.activity.login.bean.NewLoginBean;
import com.kw.top.ui.activity.login.contract.LoginContract;
import com.kw.top.ui.activity.login.presenter.LoginPresenter;
import com.kw.top.utils.RxToast;
import com.kw.top.utils.SPUtils;
import com.kw.top.utils.Tool;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * author: 正义
 * date  : 2018/4/17
 * desc  :
 */

public class LoginActivity extends MVPBaseActivity<LoginContract.View, LoginPresenter> implements LoginContract.View,
        View.OnClickListener {

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


    private String phone, code, areaCode, lon = "", lat = "", city = "";
    //声明mlocationClient对象
    public AMapLocationClient mlocationClient;
    //声明mLocationOption对象
    public AMapLocationClientOption mLocationOption = null;
    private int WRITE_COARSE_LOCATION_REQUEST_CODE = 1;
    private String registrationId;//激光ID

    @Override
    public int getContentView() {
        return R.layout.new_login_activity;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
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
        registrationId = JPushInterface.getRegistrationID(getApplicationContext());
        if (TextUtils.isEmpty(registrationId)) {
            RxToast.normal("Get registration fail, JPush init failed!");
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    WRITE_COARSE_LOCATION_REQUEST_CODE);//自定义的code
        } else {
            initLocation();
        }
        ed_qh.setCursorVisible(true);   //隐藏+区号的  光标
    }


    /**
     * 获取位置权限
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //可在此继续其他操作。
        if (requestCode == WRITE_COARSE_LOCATION_REQUEST_CODE && grantResults.length > 0) {
            initLocation();
            mlocationClient.startLocation();
        }
    }

    /**
     * 定位信息
     */
    private void initLocation() {
        mlocationClient = new AMapLocationClient(this);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位监听
        mlocationClient.setLocationListener(mAMapLocationListener);
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
        //设置定位参数
        mlocationClient.setLocationOption(mLocationOption);
    }


    @OnClick({R.id.tv_send_code, R.id.but_login})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.tv_send_code:
                sendCode(ed_qh.getText().toString(), ed_phone.getText().toString(), tv_send_code);
                break;
            case R.id.but_login:
                Login();
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
    public void loginResult(final BaseBean baseBean) {
        if (null == baseBean) return;
        if (!baseBean.isSuccess()) {
            hideProgressDialog();
            RxToast.normal(baseBean.getMsg());
            return;
        }
        NewLoginBean loginBean = null;
        try {
            loginBean = new Gson().fromJson(baseBean.getJsonData(), new TypeToken<NewLoginBean>() {
            }.getType());
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        SPUtils.saveString(this, ConstantValue.KEY_PROVE_STATE, loginBean.getUserInfo().getProveState() + "");
        SPUtils.saveString(LoginActivity.this, ConstantValue.KEY_TOKEN, loginBean.getToken());


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
            case "2":
                hideProgressDialog();
                startActivity(ManVipActivity.class);
                break;
            case "3":
                hideProgressDialog();
                showHint();
                break;
            case "4":
                hideProgressDialog();
                RxToast.normal("视频审核未通过，请重新认证");
                startActivity(VideoVerifyActivity.class);
                break;
        }
    }


    private void showHint() {
        new AlertDialog.Builder(this)
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
        SPUtils.clear(this);
        SPUtils.saveString(LoginActivity.this, ConstantValue.KEY_TOKEN, loginBean.getToken());
        SPUtils.saveString(LoginActivity.this, ConstantValue.KEY_SEX, loginBean.getUserInfo().getSex());
        SPUtils.saveString(LoginActivity.this, ConstantValue.KEY_PHONE, phone);
        SPUtils.saveString(LoginActivity.this, ConstantValue.KEY_ACCOUNT, loginBean.getAccount());
        SPUtils.saveString(LoginActivity.this, ConstantValue.KEY_CHAT_PWD, loginBean.getPassword());
        SPUtils.saveString(LoginActivity.this, ConstantValue.KEY_HEAD, loginBean.getUserInfo().getHeadImg());
        SPUtils.saveString(LoginActivity.this, ConstantValue.KEY_NAME, loginBean.getUserInfo().getNickName());
        SPUtils.saveString(LoginActivity.this, ConstantValue.KEY_USER_ID, loginBean.getUserId());
        SPUtils.saveString(LoginActivity.this, ConstantValue.KEY_PROVE_STATE, loginBean.getUserInfo().getProveState());
        SPUtils.saveString(LoginActivity.this, ConstantValue.KEY_VIP_GRADE, loginBean.getUserInfo().getGrade());
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_forget_Pwd:
                startActivity(new Intent(this, ForgetPwdActivity.class));
                break;
            case R.id.tv_login:

                break;
        }
    }

    //可以通过类implement方式实现AMapLocationListener接口，也可以通过创造接口类对象的方法实现
    //以下为后者的举例：
    AMapLocationListener mAMapLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            if (amapLocation != null) {
                if (amapLocation.getErrorCode() == 0) {
                    //可在其中解析amapLocation获取相应内容。
                    lat = amapLocation.getLatitude() + "";//获取纬度
                    lon = amapLocation.getLongitude() + "";//获取经度
                    city = amapLocation.getCity();//.substring(0,amapLocation.getCity().length()-1);
                    Log.e("tag", "==================  city  " + city + "  " + lat + " | " + lon);
                } else {
                    RxToast.normal("位置信息获取失败");
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError", "location Error, ErrCode:"
                            + amapLocation.getErrorCode() + ", errInfo:"
                            + amapLocation.getErrorInfo());
                }
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        //启动定位
        if (mlocationClient != null)
            mlocationClient.startLocation();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mlocationClient != null)
            mlocationClient.stopLocation();//停止定位后，本地定位服务并不会被销毁
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mlocationClient != null)
            mlocationClient.onDestroy();
    }


}
