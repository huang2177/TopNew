package com.kw.top.ui.fragment.setting;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kw.top.R;
import com.kw.top.base.BaseFragment;
import com.kw.top.tools.GlideTools;
import com.kw.top.ui.activity.login.ForgetPwdActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.umeng.socialize.utils.DeviceConfig.context;

/**
 * author: zy
 * data  : 2018/5/6
 * des   :
 */

public class SettingFragment extends BaseFragment {

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

    public static SettingFragment newInstance() {
        SettingFragment fragment = new SettingFragment();
        return fragment;
    }

    @Override
    public int getContentView() {
        return R.layout.frament_setting;
    }

    @Override
    public void initView(View view, Bundle savedInstanceState) {
        mTvCache.setText(GlideTools.getCacheSize(getContext()));
        mTvVersion.setText(getAppVersionName());
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    private Handler mHandler = new Handler();

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    hideProgressDialog();
                    mTvCache.setText(GlideTools.getCacheSize(getContext()));
                }
            });
        }
    };

    @OnClick({R.id.rl_change_pwd, R.id.rl_clear_cache, R.id.rl_about,R.id.rl_updata})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_change_pwd:
                ForgetPwdActivity.startActivity(getContext(), "修改密码");
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
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != mHandler)
            mHandler.removeCallbacks(mRunnable);
    }

    /**
     * 返回当前程序版本名
     */
    public String getAppVersionName() {
        String versionName = "";
        try {
            // ---get the package info---
            PackageManager pm = getContext().getPackageManager();
            PackageInfo pi = pm.getPackageInfo(getContext().getPackageName(), 0);
            versionName = pi.versionName;
            if (versionName == null || versionName.length() <= 0) {
                return "未知版本";
            }
        } catch (Exception e) {
            return "未知版本";
        }
        Log.e("tag","==========  版本： " + versionName);
        return versionName;
    }
}
