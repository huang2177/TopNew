package com.kw.top.ui.activity.club;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.kw.top.R;
import com.kw.top.base.BaseActivity;
import com.kw.top.bean.AllUserBean;
import com.kw.top.bean.BaseBean;
import com.kw.top.bean.ClubDetailsBean;
import com.kw.top.bean.event.ExitClubEvent;
import com.kw.top.retrofit.Api;
import com.kw.top.retrofit.HttpHost;
import com.kw.top.tools.ComResultTools;
import com.kw.top.tools.ConstantValue;
import com.kw.top.tools.GlideTools;
import com.kw.top.ui.activity.task.ClubTaskListActivity;
import com.kw.top.ui.fragment.club.ClubUserListFragment;
import com.kw.top.utils.RxToast;
import com.kw.top.utils.SPUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import me.relex.circleindicator.CircleIndicator;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * author  ： zy
 * date    ： 2018/6/19
 * des     ：
 */

public class ClubDetailsActivity extends BaseActivity {

    @BindView(R.id.ci_head)
    CircleImageView mCiHead;
    @BindView(R.id.tv_nickname)
    TextView mTvNickname;
    @BindView(R.id.tv_info)
    TextView mTvInfo;
    @BindView(R.id.tv_day_red)
    TextView mTvDayRed;
    @BindView(R.id.tv_all_red)
    TextView mTvAllRed;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    @BindView(R.id.indicator)
    CircleIndicator mIndicator;
    @BindView(R.id.iv_back_cd)
    ImageView mIvBack;
    @BindView(R.id.tv_title_cd)
    TextView mTvTitle;
    @BindView(R.id.tv_title_right_cd)
    TextView mTvTitleRight;
    @BindView(R.id.relative_title_cd)
    RelativeLayout mRelativeTitle;
    @BindView(R.id.tv_apply_club)
    TextView mTvApplyClub;
    @BindView(R.id.rl_apply_club)
    RelativeLayout mRlApplyClub;
    @BindView(R.id.tv_information)
    TextView mTvInformation;

    //    private ClubPagerAdapter mPagerAdapter;
    private List<AllUserBean> userList = new ArrayList<>();
    private List<ClubUserListFragment> mFragments = new ArrayList<>();
    private int pagerNum;

    private String groupId;
    private String clubId;
    private boolean isOwner;//是否是群主
    private boolean isJoin;//是否加入该群

    public static void startActivity(Context context, String groupId) {
        Intent intent = new Intent(context, ClubDetailsActivity.class);
        intent.putExtra("GROUPID", groupId);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, String groupId, String clubid) {
        Intent intent = new Intent(context, ClubDetailsActivity.class);
        intent.putExtra("GROUPID", groupId);
        intent.putExtra("CLUBID", clubid);
        context.startActivity(intent);
    }

    @Override
    public int getContentView() {
        return R.layout.activity_club_details;
    }

    public void initView() {
        groupId = getIntent().getStringExtra("GROUPID");
        clubId = getIntent().getStringExtra("CLUBID");
    }

    public void initData() {
        showProgressDialog();
        Api.getApiService().clubDetails(groupId + "", getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Action1<BaseBean>() {
                    @Override
                    public void call(BaseBean baseBean) {
                        hideProgressDialog();
                        if (baseBean.isSuccess()) {
                            ClubDetailsBean clubDetailsBean = null;
                            try {
                                clubDetailsBean = new Gson().fromJson(baseBean.getJsonData(), new TypeToken<ClubDetailsBean>() {
                                }.getType());
                            } catch (JsonSyntaxException e) {
                                e.printStackTrace();
                            }
                            if (null == clubDetailsBean) return;
                            initViewPager(clubDetailsBean);
                        } else {
                            ComResultTools.resultData(ClubDetailsActivity.this, baseBean);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        hideProgressDialog();
                        RxToast.normal(getResources().getString(R.string.net_error));
                    }
                });
    }

    public void initListener() {
        mTvTitleRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                VipManagerActivity.startActivity(ClubDetailsActivity.this, groupId);
                showDialog();
            }
        });
    }

    private void initViewPager(ClubDetailsBean clubDetailsBean) {
        if (clubDetailsBean == null)
            return;
        if (clubDetailsBean.getChairman() != null && clubDetailsBean.getChairman().size() > 0) {
            ClubDetailsBean.ChairmanBean chairmanBean = clubDetailsBean.getChairman().get(0);
            mTvNickname.setText(chairmanBean.getNickName());
            mTvInfo.setText(chairmanBean.getAge() + "岁  " + chairmanBean.getCity() + chairmanBean.getConstellation());
            mTvInformation.setText(chairmanBean.getClubNotice());

            SpannableString styledText = new SpannableString("每日红包¥" + chairmanBean.getDayRedAmount());
            styledText.setSpan(new TextAppearanceSpan(this, R.style.text_style1), 4, 5 + chairmanBean.getDayRedAmount().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            mTvDayRed.setText(styledText, TextView.BufferType.SPANNABLE);
            styledText = new SpannableString("已累计送出¥" + chairmanBean.getSendRedPacketSum());
            styledText.setSpan(new TextAppearanceSpan(this, R.style.text_style2), 5, 6 + chairmanBean.getSendRedPacketSum().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            mTvAllRed.setText(styledText, TextView.BufferType.SPANNABLE);
            Glide.with(this).load(HttpHost.qiNiu + chairmanBean.getHeadImg()).apply(GlideTools.getOptions()).into(mCiHead);
            mTvTitle.setText(chairmanBean.getClubName());

            String myUserId = SPUtils.getString(ClubDetailsActivity.this, ConstantValue.KEY_USER_ID, "");
            Log.e("tag", "=========  :" + myUserId + "  : " + chairmanBean.getUserId());
            if (myUserId.equals(chairmanBean.getUserId() + "")) {
                isOwner = true;
            } else {
                isOwner = false;
            }
        }


        userList.addAll(clubDetailsBean.getMember());

        pagerNum = (int) Math.ceil(userList.size() / 10.0);

        for (int i = 0; i < pagerNum; i++) {
            int start = i * 10;
            int end;
            if (i == pagerNum - 1) {
                //最后一页
                end = userList.size();
            } else {
                end = start + 10;
            }
            List<AllUserBean> list = new ArrayList<>();
            for (int j = 0; j < userList.size(); j++) {
                if (j >= start && j < end) {
                    list.add(userList.get(j));
                }
            }
            mFragments.add(ClubUserListFragment.onNewInstance(list));
        }
        mViewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        mViewPager.setCurrentItem(0);

        if (clubDetailsBean.getAddState().equals("0")) {
            mRlApplyClub.setVisibility(View.VISIBLE);
            isJoin = false;
        } else {
            isJoin = true;
        }
        if (isOwner || isJoin) {
            mTvTitleRight.setVisibility(View.VISIBLE);
            mTvTitleRight.setBackgroundResource(R.drawable.icon_more);
        } else {
            mTvTitleRight.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        setContentView(getContentView());
        ButterKnife.bind(this);
        initView();
        initData();
        initListener();
    }

    @OnClick({R.id.iv_back_cd, R.id.tv_apply_club})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back_cd:
                finish();
                break;
            case R.id.tv_apply_club:
                applyClub();
                break;
        }
    }

    private void applyClub() {
        showProgressDialog();
        Api.getApiService().applyClub(clubId, getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Action1<BaseBean>() {
                    @Override
                    public void call(BaseBean baseBean) {
                        hideProgressDialog();
                        if (baseBean.isSuccess()) {
                            RxToast.normal("申请成功,请等待管理员审核");
                            new Thread(mRunnable).start();
                        } else {
                            RxToast.normal(baseBean.getMsg());
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        hideProgressDialog();
                        RxToast.normal(getResources().getString(R.string.net_error));
                    }
                });
    }

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                //EMClient.getInstance().groupManager().applyJoinToGroup(groupId, "求加入");//需异步处理
                Log.e("tag", "=========== 发送申请");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }
    }

    private Dialog dialog;

    private void showDialog() {
        View view = View.inflate(this, R.layout.dialog_club, null);
        view.findViewById(R.id.btn_club_task).setOnClickListener(mOnClickListener);
        Button btnManeger = view.findViewById(R.id.btn_club_manager);
        Button btnSetting = view.findViewById(R.id.btn_club_setting);
        Button btnExit = view.findViewById(R.id.btn_club_exit);
        view.findViewById(R.id.btn_cancel).setOnClickListener(mOnClickListener);
        btnManeger.setOnClickListener(mOnClickListener);
        btnSetting.setOnClickListener(mOnClickListener);
        btnExit.setOnClickListener(mOnClickListener);
        if (!isOwner) {
            btnManeger.setVisibility(View.GONE);
            btnSetting.setVisibility(View.GONE);
        } else {
            btnExit.setVisibility(View.GONE);
        }
        dialog = new Dialog(this, R.style.charge_dialog_style);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(true);
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.BOTTOM); // 此处可以设置dialog显示的位置
        window.setWindowAnimations(R.style.popupAnimation); // 添加动画
        dialog.show();
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            dialog.dismiss();
            switch (view.getId()) {
                case R.id.btn_club_task:
                    ClubTaskListActivity.startActivity(ClubDetailsActivity.this, groupId, isOwner);
                    break;
                case R.id.btn_club_manager:
                    VipManagerActivity.startActivity(ClubDetailsActivity.this, groupId);
                    break;
                case R.id.btn_club_setting:
                    ClubNoticeActivity.startActivityForResult(ClubDetailsActivity.this, groupId, CLUB_NOTICE_CODE);
                    break;
                case R.id.btn_club_exit:
                    new AlertDialog.Builder(ClubDetailsActivity.this)
                            .setMessage("确认退出社团？")
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .setPositiveButton("退出", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    exitClub();
                                }
                            })
                            .show();
                    break;
                case R.id.btn_cancel://取消
                    break;

            }
        }
    };

    //退出俱乐部
    private void exitClub() {
        showProgressDialog();
        Api.getApiService().clubExit(groupId, getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Action1<BaseBean>() {
                    @Override
                    public void call(BaseBean baseBean) {
                        hideProgressDialog();
                        if (baseBean.isSuccess()) {
                            RxToast.normal("退出社团成功");
                            EventBus.getDefault().post(new ExitClubEvent(groupId));
                            finish();
                        } else {
                            ComResultTools.resultData(ClubDetailsActivity.this, baseBean);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        hideProgressDialog();
                    }
                });
    }

    private static int CLUB_NOTICE_CODE = 1;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            String desc = data.getStringExtra("desc");
            mTvInformation.setText(desc);
        }
    }
}
