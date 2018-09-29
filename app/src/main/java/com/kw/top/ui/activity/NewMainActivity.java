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
import com.kw.top.ui.fragment.find.HomePageFragmnet;
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
    private List<Fragment> listFragment;
    private String VerName;
    private long mExitTime = 0;

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
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        showFragment(HomePageFragmnet.newInstance());
        tabLayout.getTabAt(0).select();
        VersionCode();
    }

    /**
     * 底部导航栏
     */
    private void initTab() {
        VerName = SPUtils.getVerName(this);
        listFragment = new ArrayList<>();
        List<String> listString = Arrays.asList("首页", "消息", "TOP圈", "活动", "我");
        for (int i = 0; i < listString.size(); i++) {
            tabLayout.addTab(tabLayout.newTab().setText(listString.get(i)));
        }
        tabLayout.addOnTabSelectedListener(this);
        showFragment(HomePageFragmnet.newInstance());
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


   /* private void initIntent() {
        //make sure activity will not in background if user is logged into another device or removed
        if (getIntent() != null && (getIntent().getBooleanExtra(Constant.ACCOUNT_REMOVED, false)
                || getIntent().getBooleanExtra(Constant.ACCOUNT_KICKED_BY_CHANGE_PASSWORD, false)
                || getIntent().getBooleanExtra(Constant.ACCOUNT_KICKED_BY_OTHER_DEVICE, false))) {
            DemoHelper.getInstance().logout(false, null);
            finish();
            startActivity(new Intent(NewMainActivity.this, LoginActivity.class));
            return;
        } else if (getIntent() != null && getIntent().getBooleanExtra("isConflict", false)) {
            finish();
            startActivity(new Intent(NewMainActivity.this, LoginActivity.class));
            return;
        }


        inviteMessgeDao = new InviteMessgeDao(this);
        //register broadcast receiver to receive the change of group from DemoHelper
        registerBroadcastReceiver();
        EMClient.getInstance().contactManager().setContactListener(new MyContactListener());
        EMClient.getInstance().addClientListener(clientListener);
        EMClient.getInstance().addMultiDeviceListener(new MyMultiDeviceListener());
//        EMClient.getInstance().groupManager().addGroupChangeListener(new MyEmGroupChangeListener());
        //debug purpose only
//        registerInternalDebugReceiver();

        refreshUIWithMessage();
    }
*/

    /* private void registerBroadcastReceiver() {
         broadcastManager = LocalBroadcastManager.getInstance(this);
         IntentFilter intentFilter = new IntentFilter();
         intentFilter.addAction(Constant.ACTION_CONTACT_CHANAGED);
         intentFilter.addAction(Constant.ACTION_GROUP_CHANAGED);
         broadcastReceiver = new BroadcastReceiver() {
             @Override
             public void onReceive(Context context, Intent intent) {
                 updateUnreadLabel();
                 String action = intent.getAction();
                 if (action.equals(Constant.ACTION_GROUP_CHANAGED)) {
                     if (EaseCommonUtils.getTopActivity(NewMainActivity.this).equals(GroupsActivity.class.getName())) {
 //                        GroupsActivity.instance.onResume();
                     }
                 }
             }
         };
         broadcastManager.registerReceiver(broadcastReceiver, intentFilter);
     }


     public class MyMultiDeviceListener implements EMMultiDeviceListener {

         @Override
         public void onContactEvent(int event, String target, String ext) {

         }

         @Override
         public void onGroupEvent(int event, String target, final List<String> username) {
             switch (event) {
                 case EMMultiDeviceListener.GROUP_LEAVE:
                     ChatActivity.activityInstance.finish();
                     break;
                 default:
                     break;
             }
         }
     }


     EMClientListener clientListener = new EMClientListener() {
         @Override
         public void onMigrate2x(boolean success) {
             if (success) {
                 refreshUIWithMessage();
             }
         }
     };

 */
    public void refreshUIWithMessage() {
        runOnUiThread(new Runnable() {
            public void run() {
                updateUnreadLabel();
            }
        });
    }


    /**
     * update unread message count
     */
    public void updateUnreadLabel() {
        //   int count = getUnreadMsgCountTotal();
        // if (count > 0) {
//            unreadLabel.setText(String.valueOf(count));
//            unreadLabel.setVisibility(View.VISIBLE);
        //   if (count > 99) {
        // mTvNewsCount.setText("99+");
        //  } else {
        // mTvNewsCount.setText(count + "");
        //  }
        // mTvMsgNew.setVisibility(View.VISIBLE);
        // mTvNewsCount.setVisibility(View.VISIBLE);
        //} else {
//            unreadLabel.setVisibility(View.INVISIBLE);
        // mTvMsgNew.setVisibility(View.GONE);
        // mTvNewsCount.setText("");
        // mTvNewsCount.setVisibility(View.GONE);
        // }
    }


    /**
     * 监听好友变化请求
     */
   /* public class MyContactListener implements EMContactListener {
        *//**
     * 添加好友
     *
     * @param username
     *//*
        @Override
        public void onContactAdded(String username) {
        }

        *//**
     * 删除好友
     *
     * @param username
     *//*
        @Override
        public void onContactDeleted(final String username) {
            runOnUiThread(new Runnable() {
                public void run() {
                    if (ChatActivity.activityInstance != null && ChatActivity.activityInstance.userId != null &&
                            username.equals(ChatActivity.activityInstance.userId)) {
                        String st10 = getResources().getString(R.string.have_you_removed);
                        Toast.makeText(NewMainActivity.this, ChatActivity.activityInstance.getUserId() + st10, Toast.LENGTH_LONG)
                                .show();
                        ChatActivity.activityInstance.finish();
                    }
                }
            });
        }


        *//**
     * 接到邀请的消息，如果不处理(同意或拒绝)，掉线后，服务器会自动再发过来，所以客户端不要重复提醒
     *
     * @param username
     * @param reason
     *//*
        @Override
        public void onContactInvited(String username, String reason) {
            //收到好友邀请
            friendApply = true;
            Intent intent = new Intent(NewMainActivity.this, FriendApplyActivity.class);//点击之后进入MainActivity
            PendingIntent pendingIntent = PendingIntent.getActivity(NewMainActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationTools.showNotification(NewMainActivity.this, pendingIntent, "您有一条新的好友请求", NotificationTools.FRIEND_ID, NotificationTools.FRIEND_CHANNEL_ID);
            getApplyList();
        }

        *//**
     * 同意好友邀请
     *
     * @param username
     *//*
        @Override
        public void onFriendRequestAccepted(String username) {

            friendApply = false;
            Log.e("tag", "=============  收到好友邀请");
            Intent intent = new Intent(NewMainActivity.this, FriendApplyActivity.class);//点击之后进入MainActivity
            PendingIntent pendingIntent = PendingIntent.getActivity(NewMainActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationTools.showNotification(NewMainActivity.this, pendingIntent, "好友请求被同意", NotificationTools.FRIEND_ID, NotificationTools.FRIEND_CHANNEL_ID);
            getApplyList();
        }

        *//**
     * 拒绝好友的请求
     *
     * @param username
     *//*
        @Override
        public void onFriendRequestDeclined(String username) {
        }
    }
*/

    /**
     * get unread event notification count, including application, accepted, etc
     *
     * @return
     */
    public int getUnreadAddressCountTotal() {
        int unreadAddressCountTotal = 0;
        return unreadAddressCountTotal;
    }


    /**
     * get unread message count
     *
     * @return
     */
   /* public int getUnreadMsgCountTotal() {
     //   return EMClient.getInstance().chatManager().getUnreadMsgsCount();
    }*/
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshMsgNum(MsgCountEvent countEvent) {
        if (countEvent.isMsg())
            refreshUIWithMessage();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //  EventBus.getDefault().unregister(this);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {

        switch (tab.getPosition()) {
            case 0:
                showFragment(HomePageFragmnet.newInstance());
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


    public static void logout(Context context, boolean b) {

    }
}
