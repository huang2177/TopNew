package com.kw.top.ui.fragment.center;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.kw.top.R;
import com.kw.top.app.AppManager;
import com.kw.top.base.MVPBaseFragment;
import com.kw.top.bean.BaseBean;
import com.kw.top.bean.PersonCenterBean;
import com.kw.top.bean.UserinfoBean;
import com.kw.top.bean.event.AppLoginEvent;
import com.kw.top.bean.event.CenterCouponEvent;
import com.kw.top.bean.event.UserAvatarEvent;
import com.kw.top.retrofit.Api;
import com.kw.top.retrofit.HttpHost;
import com.kw.top.tools.ComResultTools;
import com.kw.top.tools.ConstantValue;
import com.kw.top.tools.GlideTools;
import com.kw.top.tools.Logger;
import com.kw.top.ui.activity.MainActivity;
import com.kw.top.ui.activity.circle.UserCircleActivity;
import com.kw.top.ui.activity.login.LoginActivity;
import com.kw.top.ui.activity.person_info.EditInfoActivity;
import com.kw.top.ui.activity.person_info.ModifyInfoActivity;
import com.kw.top.ui.activity.user_center.InviteFriendActivity;
import com.kw.top.ui.activity.user_center.MyAccountActivity;
import com.kw.top.ui.activity.user_center.MyGiftActivity;
import com.kw.top.ui.activity.user_center.NoticeCenterActivity;
import com.kw.top.ui.activity.user_center.SettingActivity;
import com.kw.top.utils.RxToast;
import com.kw.top.utils.SPUtils;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static android.app.Activity.RESULT_OK;

/**
 * author: Administrator
 * data  : 2018/5/6
 * des   : 个人中心
 */

public class CenterFragment extends MVPBaseFragment<CenterContract.View, CenterPresenter> implements CenterContract.View {

    @BindView(R.id.ci_head)
    CircleImageView mCiHead;
    @BindView(R.id.ci_name)
    TextView tv_name;
    @BindView(R.id.home_tv_state)
    TextView tvState;
    @BindView(R.id.home_tv_fans)
    TextView tvFans;
    @BindView(R.id.home_tv_jiazhi)
    TextView tvJiazhi;
    @BindView(R.id.home_tv_tb)
    TextView tvTb;
    @BindView(R.id.home_man_lay)
    LinearLayout layMan;
    @BindView(R.id.home_girl_lay)
    LinearLayout layGirl;
    @BindView(R.id.home_man_follow)
    TextView tvManFollow;
    @BindView(R.id.home_man_tb)
    TextView tvManTb;

    private LocalMedia localMedia;
    private int allDiamon;//账户余额


    public static CenterFragment fragemnt;
    private String sex;

    public static CenterFragment newInstance() {
        if (fragemnt == null) {
            fragemnt = new CenterFragment();
        }
        return fragemnt;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public int getContentView() {
        return R.layout.frament_center;
    }

    @Override
    public void initView(View view, Bundle savedInstanceState) {
        sex = SPUtils.getString(getActivity(), ConstantValue.KEY_SEX);
        if (sex.equals("0")) {        //区分男女
            tvState.setVisibility(View.VISIBLE);
            layGirl.setVisibility(View.VISIBLE);
            layMan.setVisibility(View.GONE);
        } else {
            tvState.setVisibility(View.GONE);
            layGirl.setVisibility(View.GONE);
            layMan.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {
        getData();
    }

    @OnClick({R.id.ci_head, R.id.ci_bianji, R.id.home_tv_pyq, R.id.home_tv_qb, R.id.home_tv_fcjh, R.id.home_tv_meiyan, R.id.home_tv_yqhy, R.id.home_tv_out})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ci_head:
                PictureSelector.create(this)
                        .openGallery(PictureConfig.TYPE_IMAGE)
                        .maxSelectNum(1)
                        .minSelectNum(1)
                        .imageSpanCount(4)
                        .selectionMode(PictureConfig.MULTIPLE)
                        .previewImage(true)
                        .isCamera(true)
                        .isZoomAnim(true)
                        .enableCrop(true)// 是否裁剪 true or false
                        .withAspectRatio(1, 1)
                        .isDragFrame(true)// 是否可拖动裁剪框(固定)
                        .rotateEnabled(false) // 裁剪是否可旋转图片 true or false
                        .showCropGrid(false)// 是否显示裁剪矩形网格
                        .forResult(ConstantValue.JUMP_RELEASE_IMAGE);
                break;
            case R.id.ci_bianji:
                getActivity().startActivity(new Intent(getActivity(), ModifyInfoActivity.class));
               // EditInfoActivity.startActivityForesult(getActivity(), true, 101);
                break;
            case R.id.home_tv_pyq:       //朋友圈
                UserCircleActivity.startActivity(getActivity(), SPUtils.getString(getActivity(), ConstantValue.KEY_USER_ID));
                break;
            case R.id.home_tv_qb:       //钱包
                startActivity(new Intent(getActivity(), MyAccountActivity.class));
                break;
            case R.id.home_tv_fcjh:    //分成计划
                startActivity(new Intent(getContext(), InviteFriendActivity.class));
                break;
            case R.id.home_tv_meiyan:  //美颜设置
                break;
            case R.id.home_tv_yqhy:   //联系客服
                showDialog();
                break;
            case R.id.home_tv_out:   //退出登录
                showProgressDialog();
                mHandler1.sendEmptyMessage(1);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == ConstantValue.JUMP_RELEASE_IMAGE) {
            List<LocalMedia> list = PictureSelector.obtainMultipleResult(data);
            if (list.size() > 0) {
                localMedia = list.get(0);
                showProgressDialog();
                mPresenter.upPhoto(localMedia.getCutPath(), getToken());
            }
        }
    }


    @Override
    public void upPhotoResult(BaseBean baseBean, String key) {
        hideProgressDialog();
        if (null != baseBean) {
            RequestOptions options = new RequestOptions()
                    .placeholder(R.mipmap.ic_launcher)
                    .dontAnimate()
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL);
            Glide.with(this).load(localMedia.getPath()).apply(options).into(mCiHead);
            //((MainActivity) getActivity()).changePhoto(localMedia.getPath());
            SPUtils.saveString(getContext(), ConstantValue.KEY_HEAD, key);
            EventBus.getDefault().post(new UserAvatarEvent(key, null));
        }
    }

    @Override
    protected void initPresenter() {
        getData();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void reFreshCounpon(CenterCouponEvent centerCouponEvent) {
        if (centerCouponEvent != null && centerCouponEvent.isRefresh()) {
            initPresenter();
            getUserInfo();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
    }

    //刷新用户等级
    private void getUserInfo() {
        Api.getApiService().getUserInfo(getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Action1<BaseBean>() {
                    @Override
                    public void call(BaseBean baseBean) {
                        if (baseBean.isSuccess()) {
                            try {
                                UserinfoBean userinfoBean = new Gson().fromJson(baseBean.getJsonData(), new TypeToken<UserinfoBean>() {
                                }.getType());
                                SPUtils.saveString(getContext(), ConstantValue.KEY_VIP_GRADE, userinfoBean.getGrade());
                                // ((MainActivity) getActivity()).changeVip(Integer.parseInt(userinfoBean.getGrade()));
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

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            getData();
        }
    }

    /**
     * 获取用户基本资料
     */
    @SuppressLint("SetTextI18n")
    private void getData() {
        showProgressDialog();
        Api.getApiService().getPersonCenter(getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Action1<BaseBean>() {

                    @Override
                    public void call(BaseBean baseBean) {
                        hideProgressDialog();
                        if (baseBean.isSuccess()) {
                            PersonCenterBean personCenterBean = null;
                            try {
                                personCenterBean = new Gson().fromJson(baseBean.getJsonData(), new TypeToken<PersonCenterBean>() {
                                }.getType());
                            } catch (JsonSyntaxException e) {
                                e.printStackTrace();
                            }
                            Glide.with(getActivity()).load(HttpHost.qiNiu + personCenterBean.getHeadImg()).apply(GlideTools.getHeadOptions()).into(mCiHead);

                            allDiamon = (int) Double.parseDouble(personCenterBean.getJewelSum());
                            tv_name.setText(personCenterBean.getNickName() + "");
                            tvState.setText(personCenterBean.getUserState());
                            tvFans.setText(personCenterBean.getFansSum() + "");
                            tvJiazhi.setText(personCenterBean.getProfit() + "T/Min");
                            tvTb.setText(allDiamon + "");
                            tvManFollow.setText(personCenterBean.getFollowSum() + "");
                            tvManTb.setText(allDiamon + "");

                        } else {
                            ComResultTools.resultData(getContext(), baseBean);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        hideProgressDialog();
                    }
                });
    }


    /**
     * 退出登录
     */
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
                    EventBus.getDefault().post(new AppLoginEvent(false));
                    SPUtils.clear(getActivity());
                    startActivity(LoginActivity.class);
                    AppManager.getAppManager().finishAllActivity();
                    break;
            }
        }
    };


    private void showDialog() {
        new AlertDialog.Builder(getActivity())
                .setMessage("TOP官方微信：wozuimeihahha")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("复制", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //获取剪贴板管理器：
                        ClipboardManager cm = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                        // 创建普通字符型ClipData
                        ClipData mClipData = ClipData.newPlainText("Label", "wozuimeihahha");
                        // 将ClipData内容放到系统剪贴板里。
                        cm.setPrimaryClip(mClipData);
                        dialog.dismiss();
                        RxToast.normal("客服微信已复制到剪切板");
                    }
                }).show();
    }
}
