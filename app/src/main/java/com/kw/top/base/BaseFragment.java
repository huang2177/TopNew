package com.kw.top.base;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.kw.top.R;
import com.kw.top.tools.ConstantValue;
import com.kw.top.utils.SPUtils;
import com.kw.top.view.RxDialog;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import pl.droidsonroids.gif.GifImageView;

/**
 * author: 正义
 * date  : 2018/4/13
 * desc  :
 */

public abstract class BaseFragment extends Fragment{

    protected Unbinder mUnbinder;
    private RxDialog mDialog;
    private View loading_view;
    private ProgressDialog mProgressDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getContentView(), container , false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mUnbinder = ButterKnife.bind(this,view);
        initProgressDialog();
        initView(view,savedInstanceState);
        initListener();
        initData();
    }

    private void initProgressDialog() {
        mDialog = new RxDialog(getContext());
        loading_view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_loading, null);
        GifImageView imageView = loading_view.findViewById(R.id.gif_loading);
        Glide.with(this).load(R.mipmap.icon_loading).into(imageView);

        mProgressDialog = new ProgressDialog(getContext());
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
        if (mProgressDialog!=null && mProgressDialog.isShowing()){
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        mUnbinder.unbind();
        super.onDestroy();
    }

    /** 返回一个用于显示界面的布局id */
    public abstract int getContentView();

    /** 初始化View的代码写在这个方法中 */
    public abstract void initView(View view, Bundle savedInstanceState);

    /** 初始化监听器的代码写在这个方法中 */
    public abstract void initListener();

    /** 初始数据的代码写在这个方法中，用于从服务器获取数据 */
    public abstract void initData();

    /**
     * 通过Class跳转界面
     **/
    public void startActivity(Class<?> cls) {
        startActivity(cls, null);
    }

    /**
     * 通过Class跳转界面
     **/
    public void startActivityForResult(Class<?> cls, int requestCode) {
        startActivityForResult(cls, null, requestCode);
    }

    /**
     * 含有Bundle通过Class跳转界面
     **/
    public void startActivityForResult(Class<?> cls, Bundle bundle, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    /**
     * 含有Bundle通过Class跳转界面
     **/
    public void startActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    public String getToken(){
        String token = SPUtils.getString(getContext(), ConstantValue.KEY_TOKEN,"");
        return token;
    }
}
