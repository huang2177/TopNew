package com.kw.top.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.bumptech.glide.Glide;
import com.gyf.barlibrary.ImmersionBar;
import com.jaeger.library.StatusBarUtil;
import com.kw.top.R;
import com.kw.top.app.AppManager;
import com.kw.top.tools.ConstantValue;
import com.kw.top.utils.NetworkUtils;
import com.kw.top.utils.SPUtils;
import com.kw.top.utils.ToastUtils;
import com.kw.top.view.RxDialog;

import pl.droidsonroids.gif.GifImageView;

/**
 * author: 正义
 * date  : 2018/4/13
 * desc  : 所有Activity基类
 */

public abstract class BaseActivity_ extends AppCompatActivity {

    private RxDialog mDialog;
    private View loading_view;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //将当前的Activity添加到容器
        AppManager.getAppManager().addActivity(this);
        initProgressDialog();
        if (!NetworkUtils.isConnected()) {
            ToastUtils.showShort("请检查网络是否连接");
        }
        StatusBarUtil.setColorNoTranslucent(this, getResources().getColor(R.color.status_color));
    }

    private void initProgressDialog() {
        mDialog = new RxDialog(this);
        loading_view = LayoutInflater.from(this).inflate(R.layout.dialog_loading, null);
        GifImageView imageView = loading_view.findViewById(R.id.gif_loading);
        Glide.with(this).load(R.mipmap.icon_loading).into(imageView);

        mProgressDialog = new ProgressDialog(this);
    }

    public void showProgressDialog() {
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setContentView(loading_view);
        mDialog.show();
    }

    public void showProgressDialog(String msg) {
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setMessage(msg);
        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        mDialog.dismiss();
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //将当前Activity移除容器
        AppManager.getAppManager().finishActivity(this);
        if (mDialog != null)
            mDialog.dismiss();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * 返回一个用于显示界面的布局id
     *
     * @return 试图id
     */
    public abstract int getContentView();

    /**
     * 初始化View
     */
//    public abstract void initView();
//
//    /**
//     * 初始化数据
//     */
//    public abstract void initData();
//
//    /**
//     * 初始化监听器的代码
//     */
//    public abstract void initListener();

    /**
     * 通过Class跳转界面
     *
     * @param cls
     */
    public void startActivity(Class<?> cls) {
        startActivity(cls, null);
    }


    /**
     * Activity 跳转
     *
     * @param context
     * @param goal
     */
    public static void skipActivityAndFinish(Context context, Class<?> goal) {
        Intent intent = new Intent(context, goal);
        context.startActivity(intent);
        ((Activity) context).finish();
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
        String token = SPUtils.getString(this, ConstantValue.KEY_TOKEN, "");
        Log.e("tag", "================= token " + token);
        return token;
    }

}
