package com.kw.top.ui.activity.user_center;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kw.top.R;
import com.kw.top.base.BaseActivity;
import com.kw.top.tools.GlideTools;
import com.kw.top.ui.activity.login.ForgetPwdActivity;
import com.kw.top.ui.activity.login.LoginActivity;
import com.kw.top.utils.RxToast;
import com.kw.top.utils.SPUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by shibing on 2018/9/4.
 */

public class SettingActivity extends BaseActivity {


    @BindView(R.id.rl_change_pwd)
    RelativeLayout mRlChangePwd;
    @BindView(R.id.tv_cache)
    TextView mTvCache;
    @BindView(R.id.rl_clear_cache)
    RelativeLayout mRlClearCache;
    @BindView(R.id.rl_about)
    RelativeLayout mRlAbout;
    @BindView(R.id.rl_updata)
    RelativeLayout mRlUpdata;
    @BindView(R.id.tv_version)
    TextView mTvVersion;
    @BindView(R.id.tv_title)
    TextView tv_title;


    @Override
    public int getContentView() {
        return R.layout.frament_setting;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        ButterKnife.bind(this);
        initView();
    }


    public void initView() {
        mTvCache.setText(GlideTools.getCacheSize(SettingActivity.this));
        mTvVersion.setText(getAppVersionName());
        tv_title.setText("设置");
    }


    @OnClick({R.id.rl_change_pwd, R.id.rl_clear_cache, R.id.rl_about, R.id.rl_updata, R.id.tv_out, R.id.iv_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.rl_change_pwd:
                ForgetPwdActivity.startActivity(SettingActivity.this, "修改密码");
                break;
            case R.id.rl_clear_cache:
                showProgressDialog();
                GlideTools.deleteFolderFile(GlideTools.glide_path, false);
                mHandler.postDelayed(mRunnable, 1000);
                break;
            case R.id.rl_about:
                break;
            case R.id.rl_updata:
                //代码实现跳转
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse("https://fir.im/6phf");//此处填链接
                intent.setData(content_url);
                startActivity(intent);
                break;
            case R.id.tv_out:
                showProgressDialog();
                mHandler1.sendEmptyMessage(1);
                break;
        }
    }


    /**
     * 返回当前程序版本名
     */
    public String getAppVersionName() {
        String versionName = "";
        try {
            // ---get the package info---
            PackageManager pm = this.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(this.getPackageName(), 0);
            versionName = pi.versionName;
            if (versionName == null || versionName.length() <= 0) {
                return "未知版本";
            }
        } catch (Exception e) {
            return "未知版本";
        }
        Log.e("tag", "==========  api/topController/getTopBeautiful 版本： " + versionName);
        return versionName;
    }


    private Handler mHandler = new Handler();

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    hideProgressDialog();
                    mTvCache.setText(GlideTools.getCacheSize(SettingActivity.this));
                }
            });
        }
    };


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != mHandler)
            mHandler.removeCallbacks(mRunnable);
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler1 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            hideProgressDialog();
            switch (msg.what) {
                case 0:
                    RxToast.normal("退出登陆失败");
                    break;
                case 1:
                    SPUtils.clear(SettingActivity.this);
                    startActivity(LoginActivity.class);
                    finish();
                    break;
            }
        }
    };
}
