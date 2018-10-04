package com.kw.top.base;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.bumptech.glide.Glide;
import com.kw.top.R;
import com.kw.top.app.AppManager;
import com.kw.top.tools.ConstantValue;
import com.kw.top.utils.SPUtils;
import com.kw.top.view.dialog.RxDialog;

import pl.droidsonroids.gif.GifImageView;

/**
 * author  ： zy
 * date    ： 2018/7/1
 * des     ：
 */
@SuppressLint("Registered")
public class MyEaseBaseActivity extends com.kw.top.base.EaseBaseActivity{

    private RxDialog mDialog;
    private View loading_view;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        //禁止横屏
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //将当前的Activity添加到容器
        AppManager.getAppManager().addActivity(this);
        initProgressDialog();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void initProgressDialog() {
        mDialog = new RxDialog(this);
        loading_view = LayoutInflater.from(this).inflate(R.layout.dialog_loading, null);
        GifImageView imageView = loading_view.findViewById(R.id.gif_loading);
        Glide.with(this).load(R.mipmap.icon_loading).into(imageView);
    }

    public void showProgressDialog() {
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setContentView(loading_view);
        mDialog.show();
    }

    public void showProgressDialog(String msg) {
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setContentView(loading_view);
        mDialog.show();
    }

    public void hideProgressDialog() {
        mDialog.dismiss();
    }

    /**
     * 通过Class跳转界面
     *
     * @param cls
     */
    public void startActivity(Class<?> cls) {
        startActivity(cls, null);
    }

    /**
     * 通过Class跳转界面
     *
     * @param cls
     * @param requestCode
     */
    public void startActivityForResult(Class<?> cls, int requestCode) {
        startActivityForResult(cls, null, requestCode);
    }

    /**
     * 携带Bundle通过Class跳转
     *
     * @param cls
     * @param bundle
     */
    public void startActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }


    /**
     * 携带Bundle通过Class跳转
     *
     * @param cls
     * @param bundle
     * @param requestCode
     */
    public void startActivityForResult(Class<?> cls, Bundle bundle, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    public String getToken() {
        return SPUtils.getString(this, ConstantValue.KEY_TOKEN, "");
    }

}
