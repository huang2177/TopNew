package com.kw.top.ui.activity;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.jaeger.library.StatusBarUtil;
import com.kw.top.R;
import com.kw.top.app.AppManager;
import com.kw.top.base.MyEaseBaseActivity;
import com.kw.top.bean.BaseBean;
import com.kw.top.bean.CircleNewsBean;
import com.kw.top.bean.event.FindChooseEvent;
import com.kw.top.bean.event.MsgCountEvent;
import com.kw.top.retrofit.Api;
import com.kw.top.retrofit.HttpHost;
import com.kw.top.tools.ConstantValue;
import com.kw.top.tools.GlideTools;
import com.kw.top.ui.activity.circle.MyNewsActivity;
import com.kw.top.ui.activity.club.CreateClubActivity;
import com.kw.top.ui.activity.login.LoginActivity;
import com.kw.top.ui.activity.news.FriendApplyActivity;
import com.kw.top.ui.activity.person_info.EditInfoActivity;
import com.kw.top.ui.activity.task.PublishTaskActivity;
import com.kw.top.ui.fragment.active.ActiveFragment;
import com.kw.top.ui.fragment.center.CenterFragment;
import com.kw.top.ui.fragment.circle.CircleFragment;
import com.kw.top.ui.fragment.classroom.ClassRoomFragment;
import com.kw.top.ui.fragment.club.ClubFragment;
import com.kw.top.ui.fragment.find.FindFrament;
import com.kw.top.ui.fragment.list.TopListFragment;
import com.kw.top.ui.fragment.news.NewsFragment;
import com.kw.top.ui.fragment.setting.SettingFragment;
import com.kw.top.ui.fragment.task.TaskFragment;
import com.kw.top.utils.RxToast;
import com.kw.top.utils.SPUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


public class MainActivity extends MyEaseBaseActivity implements View.OnClickListener {

    TextView mToolTitle, mTvName, mTvFind, mTvOrganization, mTvTopCircle, mTvTopTask, mTvTopList, mTvClassRoom, mTvActive, mTvNews, mTvSetting, mToolRightTitle, mTvLoginOut, mTvMsgNew, mTvNewsCount, mTvRightDot;//新消息
    ImageView mToolIcon, mIvVip, mIvOrganization, mIvTopList, mIvNews, mIvSetting, mIvTopTask,
            mIvActive, mIvClassRoom, mIvFind, mIvTopCircle, mIvToolbarRight;
    LinearLayout mLlRight, mLlFind, mLlOrganization, mLlTopCircle, mLlTopList, mLlTopTask, mLlClassRoom,
            mLlActive, mLlNews, mLlSetting;
    FrameLayout mFlLeft, mFrameLayout;
    DrawerLayout mDrawerLayout;
    CircleImageView mCivPhoto;
    RelativeLayout mToolItem;
    private boolean isDrawer = false;
    private FragmentManager mFragmentManager;
    private FindFrament mFindFrament;
    private ClubFragment mClubFragment;
    private CircleFragment mTopCircleFragment;
    private TopListFragment mTopListFragment;
    private TaskFragment mTaskFragment;
    private ClassRoomFragment mClassRoomFragment;
    private ActiveFragment mActiveFragment;
    private NewsFragment mNewsFragment;
    private SettingFragment mSettingFragment;
    private CenterFragment mCenterFragment;
    private List<Fragment> mFragments = new ArrayList<>();
    private int tool_right = ConstantValue.MENU_FLAG_FIND;
    private Dialog mFindDialog;
    public static boolean isForeground = false;
    private boolean friendApply;


    public int getContentView() {
        return R.layout.activity_main;

    }

    public void initView() {
        String grade = SPUtils.getString(this, ConstantValue.KEY_VIP_GRADE, "0");
        GlideTools.setVipResourceS(mIvVip, Integer.parseInt(grade));
        initDrawerLayout();
    }

    public void initData() {
        selectFragment(ConstantValue.MENU_FLAG_FIND);
        String head = HttpHost.qiNiu + SPUtils.getString(this, ConstantValue.KEY_HEAD, "");
        changePhoto(head);
        String nickname = SPUtils.getString(this, ConstantValue.KEY_NAME, "");
        mTvName.setText(nickname);
    }

    public void initListener() {
        mToolItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        //  ChatHelper.getInstance().init(this);
    }

    private void initTitle(String title, String titleRight, int rightRes) {
        mToolTitle.setText(title);
        mToolRightTitle.setText(titleRight);
        mIvToolbarRight.setVisibility(View.VISIBLE);
        mIvToolbarRight.setBackgroundResource(rightRes);
    }

    //初始化DrawerLayout
    private void initDrawerLayout() {
        StatusBarUtil.setColorForDrawerLayout(this, mDrawerLayout, getResources().getColor(R.color.black_bg));
        mLlRight.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (isDrawer) {
                    return mFlLeft.dispatchTouchEvent(motionEvent);
                } else {
                    return false;
                }

            }
        });
        //背景透明
        mDrawerLayout.setScrimColor(Color.TRANSPARENT);
        //设置右边布局跟随左边菜单滑动
        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
                isDrawer = true;
                //获取屏幕宽高
                WindowManager manager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
                Display display = manager.getDefaultDisplay();
                //设置右边的布局位置
                mLlRight.layout(mFlLeft.getRight(), 0, mFlLeft.getRight() + display.getWidth(), display.getHeight());
            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
//                mDrawerLayout.setDrawerLockMode(
//                        DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.RIGHT);
                isDrawer = true;
            }

            @Override
            public void onDrawerStateChanged(int newState) {
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(Gravity.START)) {
            mDrawerLayout.closeDrawer(Gravity.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tool_icon:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.tool_right_title:
                toolRightClick();
                break;
            case R.id.tool_right_icon:
                toolRightClick();
                break;
            case R.id.civ_photo:
                selectFragment(ConstantValue.MENU_FLAG_CENTER);
                break;
            case R.id.ll_find:
                selectFragment(ConstantValue.MENU_FLAG_FIND);
                break;
            case R.id.ll_organization:
                selectFragment(ConstantValue.MENU_FLAG_CLUB);
                break;
            case R.id.ll_top_circle:
                selectFragment(ConstantValue.MENU_FLAG_CIRCLE);
                break;
            case R.id.ll_top_list:
                selectFragment(ConstantValue.MENU_FLAG_LIST);
                break;
            case R.id.ll_top_task:
                selectFragment(ConstantValue.MENU_FLAG_TASK);
                break;
            case R.id.ll_class_room:
                selectFragment(ConstantValue.MENU_FLAG_CLASS_ROOM);
                break;
            case R.id.ll_active:
                selectFragment(ConstantValue.MENU_FLAG_ACTIVE);
                break;
            case R.id.ll_news:
                selectFragment(ConstantValue.MENU_FLAG_NEWS);
                break;
            case R.id.ll_setting:
                selectFragment(ConstantValue.MENU_FLAG_SETTING);
                break;
            case R.id.tv_login_out:
                showProgressDialog();
               /* DemoHelper.getInstance().logout(true, new EMCallBack() {
                    @Override
                    public void onSuccess() {
                        mHandler.sendEmptyMessage(1);
                    }

                    @Override
                    public void onError(int i, String s) {
                        mHandler.sendEmptyMessage(0);
                    }

                    @Override
                    public void onProgress(int i, String s) {

                    }
                });*/
                break;
        }
    }


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            hideProgressDialog();
            switch (msg.what) {
                case 0:
                    RxToast.normal("退出登陆失败");
                    break;
                case 1:
                    SPUtils.clear(MainActivity.this);
                    startActivity(LoginActivity.class);
                    finish();
                    break;
            }
        }
    };

    /**
     * toolbar 右侧点击事件
     */
    private void toolRightClick() {
        switch (tool_right) {
            case ConstantValue.MENU_FLAG_FIND:
                showChooseFindDialog();
                break;
            case ConstantValue.MENU_FLAG_CLUB:
                startActivity(CreateClubActivity.class);
                break;
            case ConstantValue.MENU_FLAG_CIRCLE:
                startActivity(MyNewsActivity.class);
                break;
            case ConstantValue.MENU_FLAG_TASK:
                startActivity(PublishTaskActivity.class);
                break;
            case ConstantValue.MENU_FLAG_NEWS:
                friendApply = false;
                mTvRightDot.setVisibility(View.GONE);
                startActivity(FriendApplyActivity.class);
                break;
            case ConstantValue.MENU_FLAG_CENTER:
                EditInfoActivity.startActivityForesult(this, true, 101);
                break;
        }
    }


    //点击fragment 显示隐藏
    private void selectFragment(int id) {
        mFragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        hideFragment(transaction);
        setMenuIcon();
        tool_right = id;
        mTvRightDot.setVisibility(View.GONE);
        switch (id) {
            case ConstantValue.MENU_FLAG_FIND:
                if (mFindFrament == null) {
                    mFindFrament = FindFrament.newInstance();
                    transaction.add(R.id.frame_layout, mFindFrament);
                    mFragments.add(mFindFrament);
                } else {
                    transaction.show(mFindFrament);
                }
                transaction.commit();
                initTitle("发现", "", R.mipmap.icon_title_find);
                mIvFind.setImageResource(R.mipmap.menu_find_pre);
                mTvFind.setTextColor(getResources().getColor(R.color.yellow_E7C375));

                break;
            case ConstantValue.MENU_FLAG_CLUB:
                if (mClubFragment == null) {
                    mClubFragment = ClubFragment.newInstance();
                    transaction.add(R.id.frame_layout, mClubFragment);
                    mFragments.add(mClubFragment);
                } else {
                    transaction.show(mClubFragment);
                }
                transaction.commit();
                initTitle("社团", "", R.mipmap.icon_title_club);
                mIvOrganization.setImageResource(R.mipmap.menu_organization_pre);
                mTvOrganization.setTextColor(getResources().getColor(R.color.yellow_E7C375));
                break;
            case ConstantValue.MENU_FLAG_CIRCLE:
                if (mTopCircleFragment == null) {
                    mTopCircleFragment = CircleFragment.newInstance();
                    transaction.add(R.id.frame_layout, mTopCircleFragment);
                    mFragments.add(mTopCircleFragment);
                } else {
                    transaction.show(mTopCircleFragment);
                }
                transaction.commit();
                initTitle("TOP圈", "", R.mipmap.icon_title_cicle);
                mIvTopCircle.setImageResource(R.mipmap.menu_top_circle_pre);
                mTvTopCircle.setTextColor(getResources().getColor(R.color.yellow_E7C375));
                break;
            case ConstantValue.MENU_FLAG_LIST:
                if (mTopListFragment == null) {
                    mTopListFragment = TopListFragment.newInstance();
                    transaction.add(R.id.frame_layout, mTopListFragment);
                    mFragments.add(mTopListFragment);
                }
                transaction.show(mTopListFragment);
                transaction.commit();
                initTitle("TOP榜", "", 0);
                mIvTopList.setImageResource(R.mipmap.menu_top_list_pre);
                mTvTopList.setTextColor(getResources().getColor(R.color.yellow_E7C375));
                break;
            case ConstantValue.MENU_FLAG_TASK:
                if (mTaskFragment == null) {
                    mTaskFragment = TaskFragment.newInstance();
                    transaction.add(R.id.frame_layout, mTaskFragment);
                    mFragments.add(mTaskFragment);
                }
                transaction.show(mTaskFragment);
                transaction.commit();
                initTitle("TOP任务", "", R.mipmap.icon_title_task);
                mIvTopTask.setImageResource(R.mipmap.menu_top_task_pre);
                mTvTopTask.setTextColor(getResources().getColor(R.color.yellow_E7C375));
                break;
            case ConstantValue.MENU_FLAG_CLASS_ROOM:
                if (mClassRoomFragment == null) {
                    mClassRoomFragment = ClassRoomFragment.newInstance();
                    transaction.add(R.id.frame_layout, mClassRoomFragment);
                    mFragments.add(mClassRoomFragment);
                }
                transaction.show(mClassRoomFragment);
                transaction.commit();
                initTitle("小课堂", "", 0);
                mIvClassRoom.setImageResource(R.mipmap.menu_classroom_pre);
                mTvClassRoom.setTextColor(getResources().getColor(R.color.yellow_E7C375));
                break;
            case ConstantValue.MENU_FLAG_ACTIVE:
                if (mActiveFragment == null) {
                    mActiveFragment = ActiveFragment.newInstance();
                    transaction.add(R.id.frame_layout, mActiveFragment);
                    mFragments.add(mActiveFragment);
                }
                transaction.show(mActiveFragment);
                transaction.commit();
                initTitle("世界活动", "", 0);
                mIvActive.setImageResource(R.mipmap.menu_active_pre);
                mTvActive.setTextColor(getResources().getColor(R.color.yellow_E7C375));
                break;
            case ConstantValue.MENU_FLAG_NEWS:
                if (mNewsFragment == null) {
                    mNewsFragment = NewsFragment.newInstance();
                    transaction.add(R.id.frame_layout, mNewsFragment);
                    mFragments.add(mNewsFragment);
                }
                transaction.show(mNewsFragment);
                transaction.commit();
                initTitle("消息", "", R.mipmap.icon_title_cicle);
                mIvNews.setImageResource(R.mipmap.menu_news_pre);
                mTvNews.setTextColor(getResources().getColor(R.color.yellow_E7C375));
                if (friendApply)
                    mTvRightDot.setVisibility(View.VISIBLE);
                break;
            case ConstantValue.MENU_FLAG_SETTING:
                if (mSettingFragment == null) {
                    mSettingFragment = SettingFragment.newInstance();
                    transaction.add(R.id.frame_layout, mSettingFragment);
                    mFragments.add(mSettingFragment);
                }
                transaction.show(mSettingFragment);
                transaction.commit();
                initTitle("设置", "", 0);
                mIvSetting.setImageResource(R.mipmap.menu_setting_pre);
                mTvSetting.setTextColor(getResources().getColor(R.color.yellow_E7C375));
                break;
            case ConstantValue.MENU_FLAG_CENTER:
                if (mCenterFragment == null) {
                    mCenterFragment = CenterFragment.newInstance();
                    transaction.add(R.id.frame_layout, mCenterFragment);
                    mFragments.add(mCenterFragment);
                }
                transaction.show(mCenterFragment);
                transaction.commit();
                initTitle("个人中心", "编辑资料", 0);
                break;
        }
        mDrawerLayout.closeDrawers();
    }

    //设置菜单icon
    private void setMenuIcon() {
        mIvFind.setImageResource(R.mipmap.menu_find);
        mIvOrganization.setImageResource(R.mipmap.menu_organization);
        mIvTopCircle.setImageResource(R.mipmap.menu_top_circle);
        mIvTopList.setImageResource(R.mipmap.menu_top_list);
        mIvTopTask.setImageResource(R.mipmap.menu_top_task);
        mIvClassRoom.setImageResource(R.mipmap.menu_classroom);
        mIvActive.setImageResource(R.mipmap.menu_active);
        mIvNews.setImageResource(R.mipmap.menu_news);
        mIvSetting.setImageResource(R.mipmap.menu_setting);

        mTvFind.setTextColor(getResources().getColor(R.color.white));
        mTvOrganization.setTextColor(getResources().getColor(R.color.white));
        mTvTopCircle.setTextColor(getResources().getColor(R.color.white));
        mTvTopList.setTextColor(getResources().getColor(R.color.white));
        mTvTopTask.setTextColor(getResources().getColor(R.color.white));
        mTvClassRoom.setTextColor(getResources().getColor(R.color.white));
        mTvActive.setTextColor(getResources().getColor(R.color.white));
        mTvNews.setTextColor(getResources().getColor(R.color.white));
        mTvNews.setTextColor(getResources().getColor(R.color.white));
        mTvSetting.setTextColor(getResources().getColor(R.color.white));
    }

    private void hideFragment(FragmentTransaction trs) {
        for (int i = 0; i < mFragments.size(); i++) {
            if (null != mFragments) {
                trs.hide(mFragments.get(i));
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            changeName();
        }
    }

    /**
     * 发现筛选弹框
     */
    private void showChooseFindDialog() {
        View view = View.inflate(this, R.layout.dialog_choose_find, null);
        view.findViewById(R.id.btn_select_man).setOnClickListener(findDialogOnclickListener);
        view.findViewById(R.id.btn_select_gril).setOnClickListener(findDialogOnclickListener);
        view.findViewById(R.id.btn_select_all).setOnClickListener(findDialogOnclickListener);
        view.findViewById(R.id.btn_cancel).setOnClickListener(findDialogOnclickListener);
        mFindDialog = new Dialog(this, R.style.charge_dialog_style);
        mFindDialog.setContentView(view);
        mFindDialog.setCanceledOnTouchOutside(true);
        Window window = mFindDialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.BOTTOM); // 此处可以设置dialog显示的位置
        window.setWindowAnimations(R.style.popupAnimation); // 添加动画
        mFindDialog.show();
    }

    private View.OnClickListener findDialogOnclickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn_select_man:
                    EventBus.getDefault().post(new FindChooseEvent("1"));
                    break;
                case R.id.btn_select_gril:
                    EventBus.getDefault().post(new FindChooseEvent("0"));
                    break;
                case R.id.btn_select_all:
                    EventBus.getDefault().post(new FindChooseEvent(""));
                    break;
                case R.id.btn_cancel:
                    break;
            }
            mFindDialog.dismiss();
        }
    };

    /**
     * 退出程序提示
     */
    private long mExitTime = 0;

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
//                MobclickAgent.onKillProcess(this);
                AppManager.getAppManager().finishAllActivity();
                MainActivity.this.finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    //修改头像
    public void changePhoto(String url) {
        Glide.with(this).load(url).apply(GlideTools.getHeadOptions()).into(mCivPhoto);
    }

    public void changeVip(int grade) {
        GlideTools.setVipResourceS(mIvVip, grade);
    }

    public void changeName() {
        mTvName.setText(SPUtils.getString(this, ConstantValue.KEY_NAME, ""));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        bindView();
        initView();
        initData();
        initListener();

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
        //make sure activity will not in background if user is logged into another device or removed
        /*if (getIntent() != null && (getIntent().getBooleanExtra(Constant.ACCOUNT_REMOVED, false)
                || getIntent().getBooleanExtra(Constant.ACCOUNT_KICKED_BY_CHANGE_PASSWORD, false)
                || getIntent().getBooleanExtra(Constant.ACCOUNT_KICKED_BY_OTHER_DEVICE, false)))
        {
            DemoHelper.getInstance().logout(false, null);
            finish();
            startActivity(new Intent(this, LoginActivity.class));
            return;
        } else if (getIntent() != null && getIntent().getBooleanExtra("isConflict", false)) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
            return;
        }*/


        //register broadcast receiver to receive the change of group from DemoHelper
        registerBroadcastReceiver();


       /* EMClient.getInstance().contactManager().setContactListener(new MyContactListener());
        EMClient.getInstance().addClientListener(clientListener);
        EMClient.getInstance().addMultiDeviceListener(new MyMultiDeviceListener());*/
//        EMClient.getInstance().groupManager().addGroupChangeListener(new MyEmGroupChangeListener());
        //debug purpose only
//        registerInternalDebugReceiver();

       // refreshUIWithMessage();
    }

    private void bindView() {
        mToolTitle = findViewById(R.id.tool_title);
        mToolIcon = findViewById(R.id.tool_icon);
        mLlRight = findViewById(R.id.ll_right);
        mFlLeft = findViewById(R.id.fl_left);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mFrameLayout = findViewById(R.id.frame_layout);
        mCivPhoto = findViewById(R.id.civ_photo);
        mTvName = findViewById(R.id.tv_name);
        mIvVip = findViewById(R.id.iv_vip);
        mIvFind = findViewById(R.id.iv_find);
        mTvFind = findViewById(R.id.tv_find);
        mLlFind = findViewById(R.id.ll_find);
        mIvOrganization = findViewById(R.id.iv_organization);
        mTvOrganization = findViewById(R.id.tv_organization);
        mLlOrganization = findViewById(R.id.ll_organization);
        mIvTopCircle = findViewById(R.id.iv_top_circle);
        mTvTopCircle = findViewById(R.id.tv_top_circle);
        mLlTopCircle = findViewById(R.id.ll_top_circle);
        mIvTopList = findViewById(R.id.iv_top_list);
        mTvTopList = findViewById(R.id.tv_top_list);
        mLlTopList = findViewById(R.id.ll_top_list);
        mIvTopTask = findViewById(R.id.iv_top_task);
        mTvTopTask = findViewById(R.id.tv_top_task);
        mLlTopTask = findViewById(R.id.ll_top_task);
        mIvClassRoom = findViewById(R.id.iv_class_room);
        mTvClassRoom = findViewById(R.id.tv_class_room);
        mLlClassRoom = findViewById(R.id.ll_class_room);
        mIvActive = findViewById(R.id.iv_active);
        mTvActive = findViewById(R.id.tv_active);
        mLlActive = findViewById(R.id.ll_active);
        mIvNews = findViewById(R.id.iv_news);
        mTvNews = findViewById(R.id.tv_news);
        mLlNews = findViewById(R.id.ll_news);
        mIvSetting = findViewById(R.id.iv_setting);
        mTvSetting = findViewById(R.id.tv_setting);
        mLlSetting = findViewById(R.id.ll_setting);
        mToolItem = findViewById(R.id.tool_item);
        mToolRightTitle = findViewById(R.id.tool_right_title);
        mTvLoginOut = findViewById(R.id.tv_login_out);
        mIvToolbarRight = findViewById(R.id.tool_right_icon);
        mTvMsgNew = findViewById(R.id.tv_msg_new);
        mTvNewsCount = findViewById(R.id.tv_news_count);
        mTvRightDot = findViewById(R.id.tv_right_dot);

        mToolIcon.setOnClickListener(this);
        mToolRightTitle.setOnClickListener(this);
        mCivPhoto.setOnClickListener(this);
        mLlFind.setOnClickListener(this);
        mLlOrganization.setOnClickListener(this);
        mLlTopCircle.setOnClickListener(this);
        mLlTopList.setOnClickListener(this);
        mLlTopTask.setOnClickListener(this);
        mLlClassRoom.setOnClickListener(this);
        mLlActive.setOnClickListener(this);
        mLlNews.setOnClickListener(this);
        mLlSetting.setOnClickListener(this);
        mTvLoginOut.setOnClickListener(this);
        mIvToolbarRight.setOnClickListener(this);
    }

    //********************************************
    //
    //环信相关
    //
    //
    //*******************************************
    private LocalBroadcastManager broadcastManager;
    private BroadcastReceiver broadcastReceiver;
    private BroadcastReceiver internalDebugReceiver;

    private void registerBroadcastReceiver() {
        broadcastManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        /*intentFilter.addAction(Constant.ACTION_CONTACT_CHANAGED);
        intentFilter.addAction(Constant.ACTION_GROUP_CHANAGED);*/
        broadcastReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                //   updateUnreadLabel();
//                if (currentTabIndex == 0) {
//                    // refresh conversation list
//                    if (conversationListFragment != null) {
//                        conversationListFragment.refresh();
//                    }
//                } else if (currentTabIndex == 1) {
//                    if(contactListFragment != null) {
//                        contactListFragment.refresh();
//                    }
//                }
                String action = intent.getAction();
                /*if (action.equals(Constant.ACTION_GROUP_CHANAGED)) {
                    if (EaseCommonUtils.getTopActivity(MainActivity.this).equals(GroupsActivity.class.getName())) {
//                        GroupsActivity.instance.onResume();
                    }
                }
            }
        };*/
                // broadcastManager.registerReceiver(broadcastReceiver, intentFilter);
            }

            /*  public class MyMultiDeviceListener implements EMMultiDeviceListener {

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
          //            Toast.makeText(MainActivity.this, "onUpgradeFrom 2.x to 3.x " + (success ? "success" : "fail"), Toast.LENGTH_LONG).show();
                      if (success) {
                          refreshUIWithMessage();
                      }
                  }
              };
          */
            public void refreshUIWithMessage() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        // refresh unread count
                        //   updateUnreadLabel();
//                if (currentTabIndex == 0) {
//                    // refresh conversation list
//                    if (conversationListFragment != null) {
//                        conversationListFragment.refresh();
//                    }
//                }
                    }
                });
            }

            /**
             * update unread message count
             */
//    private TextView unreadLabel;
            /*public void updateUnreadLabel() {
                int count = getUnreadMsgCountTotal();
                if (count > 0) {
//            unreadLabel.setText(String.valueOf(count));
//            unreadLabel.setVisibility(View.VISIBLE);
                    if (count > 99) {
                        mTvNewsCount.setText("99+");
                    } else {
                        mTvNewsCount.setText(count + "");
                    }
                    mTvMsgNew.setVisibility(View.VISIBLE);
                    mTvNewsCount.setVisibility(View.VISIBLE);
                } else {
//            unreadLabel.setVisibility(View.INVISIBLE);
                    mTvMsgNew.setVisibility(View.GONE);
                    mTvNewsCount.setText("");
                    mTvNewsCount.setVisibility(View.GONE);
                }
            }*/

            /**
             * 监听好友变化请求
             */
   /* public class MyContactListener implements EMContactListener {
        @Override
        public void onContactAdded(String username) {
        }

        @Override
        public void onContactDeleted(final String username) {
            runOnUiThread(new Runnable() {
                public void run() {
                    if (ChatActivity.activityInstance != null && ChatActivity.activityInstance.userId != null &&
                            username.equals(ChatActivity.activityInstance.userId)) {
                        String st10 = getResources().getString(R.string.have_you_removed);
                        Toast.makeText(MainActivity.this, ChatActivity.activityInstance.getUserId() + st10, Toast.LENGTH_LONG)
                                .show();
                        ChatActivity.activityInstance.finish();
                    }
                }
            });
        }

        @Override
        public void onContactInvited(String username, String reason) {
            //收到好友邀请
            friendApply = true;
            Intent intent = new Intent(MainActivity.this, FriendApplyActivity.class);//点击之后进入MainActivity
            PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationTools.showNotification(MainActivity.this, pendingIntent, "您有一条新的好友请求", NotificationTools.FRIEND_ID, NotificationTools.FRIEND_CHANNEL_ID);
        }

        @Override
        public void onFriendRequestAccepted(String username) {
            //同意好友邀请
            friendApply = false;
            Log.e("tag", "=============  收到好友邀请");
            Intent intent = new Intent(MainActivity.this, FriendApplyActivity.class);//点击之后进入MainActivity
            PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationTools.showNotification(MainActivity.this, pendingIntent, "好友请求被同意", NotificationTools.FRIEND_ID, NotificationTools.FRIEND_CHANNEL_ID);
        }

        @Override
        public void onFriendRequestDeclined(String username) {
        }
    }
*/


            /**
             * 获取未读评论
             */
            private void getComment() {
                showProgressDialog();
                Api.getApiService().getDynamicCom(getToken())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.io())
                        .subscribe(new Action1<BaseBean>() {
                            @Override
                            public void call(BaseBean baseBean) {
                                hideProgressDialog();
                                if (baseBean.isSuccess()) {
                                    CircleNewsBean circleNewsBean = null;
                                    try {
                                        circleNewsBean = new Gson().fromJson(baseBean.getJsonData(), new TypeToken<CircleNewsBean>() {
                                        }.getType());
                                        if (circleNewsBean != null && circleNewsBean.getCommentList().size() > 0) {

                                        }
                                    } catch (JsonSyntaxException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }, new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                            }
                        });
            }

            @Subscribe(threadMode = ThreadMode.MAIN)
            public void refreshMsgNum(MsgCountEvent countEvent) {
                if (countEvent.isMsg())
                    refreshUIWithMessage();
            }

         /*   @Override
            protected void onDestroy() {
                super.onDestroy();
                EventBus.getDefault().unregister(this);
            }

            @Override
            protected void onResume() {
                isForeground = true;
                // super.onResume();
            }


            @Override
            protected void onPause() {
                isForeground = false;
                //  super.onPause();
            }*/

        };
    }
}



