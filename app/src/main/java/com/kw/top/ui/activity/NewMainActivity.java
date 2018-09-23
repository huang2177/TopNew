package com.kw.top.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.kw.top.R;
import com.kw.top.app.AppManager;
import com.kw.top.base.EaseTokenBean;
import com.kw.top.base.MyEaseBaseActivity;
import com.kw.top.bean.BaseBean;
import com.kw.top.bean.FriendApplyBean;
import com.kw.top.bean.VersionBean;
import com.kw.top.bean.event.MsgCountEvent;
import com.kw.top.retrofit.Api;
import com.kw.top.tools.ConstantValue;
import com.kw.top.tools.Logger;
import com.kw.top.ui.activity.login.LoginActivity;
import com.kw.top.ui.fragment.active.NewActivityFragment;
import com.kw.top.ui.fragment.center.CenterFragment;
import com.kw.top.ui.fragment.circle.CircleContentFragment;
import com.kw.top.ui.fragment.find.FindFrament;
import com.kw.top.ui.fragment.news.NewsFragment;
import com.kw.top.utils.SPUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 */

public class NewMainActivity extends MyEaseBaseActivity implements TabLayout.OnTabSelectedListener {


    @BindView(R.id.main_container)
    FrameLayout frameLayout;
    @BindView(R.id.tablayout)
    TabLayout tabLayout;
    private List<String> listString;
    private List<Fragment> listFragment;
    private String VerName;
    private long mExitTime = 0;
    /**
     * 环信相关
     **/
    private LocalBroadcastManager broadcastManager;
    private BroadcastReceiver broadcastReceiver;
    private BroadcastReceiver internalDebugReceiver;
    public static boolean isForeground = false;
    private boolean friendApply;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_main);
        ButterKnife.bind(this);
        //ChatHelper.getInstance().init(this);
//        EventBus.getDefault().register(this);
        initTab();
        initSdk();
        // initIntent();
        VersionCode();
        EaseToken();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        showFragment(FindFrament.newInstance());
        tabLayout.getTabAt(0).select();
        VersionCode();
    }

    /**
     * 底部导航栏
     */
    private void initTab() {
        VerName = SPUtils.getVerName(this);
        listFragment = new ArrayList<>();
        listString = Arrays.asList("推荐", "消息", "TOP圈", "活动", "我");
        for (int i = 0; i < listString.size(); i++) {
            tabLayout.addTab(tabLayout.newTab().setText(listString.get(i)));
        }
        tabLayout.addOnTabSelectedListener(this);
        showFragment(FindFrament.newInstance());
    }

    private void initSdk() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String packageName = getPackageName();
            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                try {
                    //some device doesn't has activity to handle this intent
                    //so add try catch
                    Intent intent = new Intent();
                    intent.setAction(android.provider.Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                    intent.setData(Uri.parse("package:" + packageName));
                    startActivity(intent);
                } catch (Exception e) {
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
      //  EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onResume() {
        isForeground = true;
        super.onResume();
    }


    @Override
    protected void onPause() {
        isForeground = false;
        super.onPause();
    }


    @Override
    public void onTabSelected(TabLayout.Tab tab) {

        switch (tab.getPosition()) {
            case 0:
                showFragment(FindFrament.newInstance());
                break;
            case 1:
                showFragment(NewsFragment.newInstance());
                break;
            case 2:
                showFragment(CircleContentFragment.newInstance());
                break;
            case 3:
                showFragment(NewActivityFragment.newInstance());
                break;
            case 4:
                showFragment(CenterFragment.newInstance());
                break;
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }


    /**
     * 显示Fragment
     */
    private void showFragment(Fragment fragment) {
        if (fragment == null) {
            return;
        }
        if (!listFragment.contains(fragment)) {
            listFragment.add(fragment);
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (!fragment.isAdded()) {
            transaction.add(R.id.main_container, fragment);
        }

        hideFragment(transaction);
        transaction.show(fragment);
        transaction.commit();
    }


    /**
     * 隐藏所有的fragment
     */
    private void hideFragment(FragmentTransaction transaction) {
        for (int i = 0; i < listFragment.size(); i++) {
            if (listFragment.get(i) != null) {
                transaction.hide(listFragment.get(i));
            }
        }
    }


    private void VersionCode() {
        String token = getToken();
        Api.getApiService().queryVersion("02", token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Action1<BaseBean<VersionBean>>() {
                    @Override
                    public void call(BaseBean<VersionBean> baseBean) {
                        if (VerName.equals(baseBean.getData().getVersion())) {
                            return;
                        }
                        showVerSionDialog();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                });
    }


    /**
     * 版本更新dialog
     */
    private void showVerSionDialog() {
        new AlertDialog.Builder(this)
                .setMessage("发现新版本，是否更新？")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.setAction("android.intent.action.VIEW");
                        Uri content_url = Uri.parse("https://fir.im/6phf");//此处填链接
                        intent.setData(content_url);
                        startActivity(intent);
                        dialog.dismiss();
                    }
                }).show();
    }


    /**
     * 获取网易云token  并保存在本地
     */

    private void EaseToken() {
        String token = getToken();
        Api.getApiService().EaseToken(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Action1<BaseBean<EaseTokenBean>>() {
                    @Override
                    public void call(BaseBean<EaseTokenBean> baseBean) {
                        if (!baseBean.isSuccess()) {
                            return;
                        }
                        Logger.e("--EaseToke----", baseBean.getData().getToken());

                        SPUtils.saveString(NewMainActivity.this, ConstantValue.NET_EASE_TOKEN, baseBean.getData().getToken());
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                });

    }


    /**
     * 点击系统返回键
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - mExitTime > 2000) {
                Toast.makeText(this, getResources().getString(R.string.back_again), Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {
                //MobclickAgent.onKillProcess(this);
                AppManager.getAppManager().finishAllActivity();
                // finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}
