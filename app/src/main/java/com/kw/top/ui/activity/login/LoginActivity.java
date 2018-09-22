package com.kw.top.ui.activity.login;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.kw.top.R;
import com.kw.top.app.AppManager;
import com.kw.top.base.MVPBaseActivity;
import com.kw.top.bean.BaseBean;
import com.kw.top.bean.LoginBean;
import com.kw.top.tools.ChatHelper;
import com.kw.top.tools.ConstantValue;
import com.kw.top.ui.activity.MainActivity;
import com.kw.top.ui.activity.NewMainActivity;
import com.kw.top.ui.activity.login.contract.LoginContract;
import com.kw.top.ui.activity.login.presenter.LoginPresenter;
import com.kw.top.utils.RxToast;
import com.kw.top.utils.SPUtils;

import java.util.logging.Logger;

import cn.jpush.android.api.JPushInterface;

/**
 * author: 正义
 * date  : 2018/4/17
 * desc  :
 */

public class LoginActivity extends MVPBaseActivity<LoginContract.View, LoginPresenter> implements LoginContract.View,
        View.OnClickListener {
    TextView mTvLogin;
    EditText mEtPhone;
    EditText mEtPassword;
    TextView mTvForgetPwd;
    private String phone, password, lon = "", lat = "", city = "";
    //声明mlocationClient对象
    public AMapLocationClient mlocationClient;
    //声明mLocationOption对象
    public AMapLocationClientOption mLocationOption = null;
    private int WRITE_COARSE_LOCATION_REQUEST_CODE = 1;
    private String registrationId;//激光ID

    @Override
    public int getContentView() {
        return R.layout.activity_login;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        mTvLogin = findViewById(R.id.tv_login);
        mEtPhone = findViewById(R.id.et_phone);
        mEtPassword = findViewById(R.id.et_password);
        mTvForgetPwd = findViewById(R.id.tv_forget_Pwd);
        mTvForgetPwd.setOnClickListener(this);
        mTvLogin.setOnClickListener(this);
        registrationId = JPushInterface.getRegistrationID(getApplicationContext());
        if (TextUtils.isEmpty(registrationId)) {
            RxToast.normal("Get registration fail, JPush init failed!");
        }
        Log.e("Tag", "============== reid " + registrationId);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    WRITE_COARSE_LOCATION_REQUEST_CODE);//自定义的code
        } else {
            initLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //可在此继续其他操作。
        if (requestCode == WRITE_COARSE_LOCATION_REQUEST_CODE && grantResults.length > 0) {
            initLocation();
            mlocationClient.startLocation();
        }
    }

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

    @Override
    public void loginResult(final BaseBean baseBean) {
        if (null == baseBean) return;
        if (!baseBean.isSuccess()) {
            hideProgressDialog();
            RxToast.normal(baseBean.getMsg());
            return;
        }
       /* if (ChatHelper.getInstance().isLoggedIn()) {
            //退出登陆
            EMClient.getInstance().logout(true, new EMCallBack() {

                @Override
                public void onSuccess() {
                    // TODO Auto-generated method stub
//                        mHandler.sendEmptyMessage(1);
                    //Loginchat(loginBean);
                }

                @Override
                public void onProgress(int progress, String status) {
                    // TODO Auto-generated method stub
                }

                @Override
                public void onError(int code, String message) {
                    // TODO Auto-generated method stub
//                        mHandler.sendEmptyMessage(0);
                }
            });
        }*/

        LoginBean loginBean = null;
        try {
            loginBean = new Gson().fromJson(baseBean.getJsonData(), new TypeToken<LoginBean>() {
            }.getType());
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        SPUtils.saveString(this, ConstantValue.KEY_PROVE_STATE, loginBean.getProveState() + "");
        SPUtils.saveString(LoginActivity.this, ConstantValue.KEY_TOKEN, loginBean.getToken());

        //认证状态(0 未认证,1 已认证,2 浏览超时,3审核中,4 未通过)
        if (loginBean.getProveState().equals("1")) {
            Loginchat(loginBean);
        } else if (loginBean.getProveState().equals("0")) {
            hideProgressDialog();
            if (loginBean.getSex().equals("1")) {
                //男
                startActivity(NewMainActivity.class);
            } else if (loginBean.getSex().equals("0")) {
                //女
                startActivity(VideoVerifyActivity.class);
            } else {
                startActivity(SexActivity.class);
            }
        } else if (loginBean.getProveState().equals("2")) {
            hideProgressDialog();
            startActivity(ManVipActivity.class);
        } else if (loginBean.getProveState().equals("3")) {
            hideProgressDialog();
            showHint();
        } else if (loginBean.getProveState().equals("4")) {
            hideProgressDialog();
            RxToast.normal("视频审核未通过，请重新认证");
            startActivity(VideoVerifyActivity.class);
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

    @SuppressLint("StaticFieldLeak")
    private void Loginchat(final LoginBean loginBean) {
        SPUtils.clear(this);
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                EMClient.getInstance().login(loginBean.getAccount(), loginBean.getPassword(), new EMCallBack() {//回调
                    @Override
                    public void onSuccess() {
                        hideProgressDialog();
                        SPUtils.saveString(LoginActivity.this, ConstantValue.KEY_TOKEN, loginBean.getToken());
                        SPUtils.saveString(LoginActivity.this, ConstantValue.KEY_SEX, loginBean.getSex());
                        SPUtils.saveString(LoginActivity.this, ConstantValue.KEY_PHONE, phone);
                        SPUtils.saveString(LoginActivity.this, ConstantValue.KEY_CHAT_NUM, loginBean.getAccount());
                        SPUtils.saveString(LoginActivity.this, ConstantValue.KEY_CHAT_PWD, loginBean.getPassword());
                        SPUtils.saveString(LoginActivity.this, ConstantValue.KEY_HEAD, loginBean.getHeadImg());
                        SPUtils.saveString(LoginActivity.this, ConstantValue.KEY_NAME, loginBean.getNickName());
                        SPUtils.saveString(LoginActivity.this, ConstantValue.KEY_USER_ID, loginBean.getUserId());
                        SPUtils.saveString(LoginActivity.this, ConstantValue.KEY_PROVE_STATE, loginBean.getProveState());
                        SPUtils.saveString(LoginActivity.this, ConstantValue.KEY_VIP_GRADE, loginBean.getGrade());
                        EMClient.getInstance().groupManager().loadAllGroups();
                        EMClient.getInstance().chatManager().loadAllConversations();
                        com.kw.top.tools.Logger.e("------环信登陆", "环信登陆成功");
                        if (TextUtils.isEmpty(loginBean.getSex())) {
                            startActivity(SexActivity.class);
                            finish();
                        } else {
                            skipActivityAndFinish(LoginActivity.this, NewMainActivity.class);
                        }
                    }

                    @Override
                    public void onProgress(int progress, String status) {

                    }

                    @Override
                    public void onError(int code, String message) {
                        Log.d("===============", "登录聊天服务器失败！");
                        hideProgressDialog();
                        RxToast.normal(getResources().getString(R.string.net_error));
                    }
                });
                return null;
            }
        }.execute();
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
                phone = mEtPhone.getText().toString().trim();
                password = mEtPassword.getText().toString().trim();
//                city = "上海";
//                lon ="1";
//                lat = "2";
                if (TextUtils.isEmpty(phone)) {
                    RxToast.normal("请输入手机号");
                } else if (TextUtils.isEmpty(password)) {
                    RxToast.normal("请输入密码");
                } else if (TextUtils.isEmpty(city)) {
                    RxToast.normal("位置信息获取失败");
                } else {
                    showProgressDialog();
                    mPresenter.login(phone, password, lon, lat, city, registrationId);
                }
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
