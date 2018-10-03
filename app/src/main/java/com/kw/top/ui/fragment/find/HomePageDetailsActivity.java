package com.kw.top.ui.fragment.find;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.kw.top.R;
import com.kw.top.base.BaseActivity_;
import com.kw.top.bean.BaseBean;
import com.kw.top.ui.fragment.find.videohelper.VideoChatHelper;
import com.netease.nim.avchatkit.event.VideoChatEvent;
import com.kw.top.retrofit.HttpHost;
import com.kw.top.tools.ConstantValue;
import com.kw.top.tools.GlideTools;
import com.kw.top.ui.activity.HomePage.HomePageFollow;
import com.kw.top.ui.activity.HomePage.HomePageView;
import com.kw.top.ui.activity.circle.UserCircleActivity;
import com.kw.top.ui.activity.login.LoginActivity;
import com.kw.top.ui.fragment.find.adapter.GlideImageLoader;
import com.kw.top.ui.fragment.find.adapter.HomeFriendsAdapter;
import com.kw.top.ui.fragment.find.adapter.HomeGiftAdapter;
import com.kw.top.ui.fragment.find.bean.HomeInfoBean;
import com.kw.top.utils.RxToast;
import com.kw.top.utils.SPUtils;
import com.kw.top.utils.StatusUtil;
import com.kw.top.view.GiftDialog;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.event.EventSubscribeService;
import com.netease.nimlib.sdk.event.model.Event;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by shibing on 2018/9/24.
 */

public class HomePageDetailsActivity extends BaseActivity_ implements HomePageView {

    @BindView(R.id.detalis_banner)
    Banner banner;
    private List<String> listBanner;

    @BindView(R.id.tv_jb)
    TextView tvJinb;
    @BindView(R.id.lw_recylerview)
    RecyclerView lwRecyler;
    @BindView(R.id.tv_gd)
    TextView tvGd;
    @BindView(R.id.pyq_recylerview)
    RecyclerView pyqRecyler;
    @BindView(R.id.tv_sg)
    TextView tvSg;
    @BindView(R.id.tv_job)
    TextView tvJob;
    @BindView(R.id.tv_shengri)
    TextView tvShengRi;
    @BindView(R.id.tv_shpz)
    TextView tvShpz;
    @BindView(R.id.tv_yjxg)
    TextView tvYjxg;
    @BindView(R.id.tv_sr)
    TextView tvNsr;
    @BindView(R.id.tv_zzc)
    TextView tvZzc;
    @BindView(R.id.tv_home_name)
    TextView tvName;
    @BindView(R.id.home_leave)
    ImageView imageLeave;
    @BindView(R.id.tv_fans)
    TextView tvFans;
    @BindView(R.id.home_item_state)
    TextView tvState;
    @BindView(R.id.tv_follow)
    TextView tvFollow;


    private String userId;
    private HomeInfoBean homeInfoBean;
    //朋友圈
    private List<HomeInfoBean.PictureListBean> pictureListBeans;
    private HomeFriendsAdapter friendsAdapter;

    //礼物榜单
    private HomeGiftAdapter giftAdapter;
    private List<HomeInfoBean.GiftListBean> giftListBeans;

    //banner图
    private List<HomeInfoBean.PhotoalbumListBean> photoalbumListBeans;


    private String receiveUserId;
    private HomePageFollow homePageFollow;
    private String follow;
    private GiftDialog giftDialog;
    private VideoChatHelper chatHelper;

    @Override
    public int getContentView() {
        return R.layout.activity_homepage;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        ButterKnife.bind(this);
        initStatus();
        initRecylerview();
    }


    /**
     * 状态栏
     */
    private void initStatus() {
        if (true) {
            StatusUtil.setStatusBar(this, false, false);
            StatusUtil.setStatusTextColor(false, this);
        }
        homePageFollow = new HomePageFollow(this);
        listBanner = new ArrayList<>();
        userId = getIntent().getStringExtra(ConstantValue.KEY_USER_ID);
        homePageFollow.getHonePageData(userId, getToken());
        EventBus.getDefault().register(this);
    }


    /**
     * 初始化 recylerview
     */
    private void initRecylerview() {
        lwRecyler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        pyqRecyler.setLayoutManager(new GridLayoutManager(this, 3));
        lwRecyler.setNestedScrollingEnabled(false);
        pyqRecyler.setNestedScrollingEnabled(false);
    }


    private void initBanner() {
        banner.setBannerStyle(1);
        banner.setImageLoader(new GlideImageLoader());  //设置图片加载器
        banner.setImages(listBanner);  //设置图片集合
        banner.isAutoPlay(true);  //设置自动轮播，默认为true
        banner.setDelayTime(3000);   //设置轮播时间
        banner.setIndicatorGravity(BannerConfig.RIGHT);//设置指示器位置（当banner模式中有指示器时）
        banner.start();//banner设置方法全部调用完毕时最后调用
    }


    @OnClick({R.id.image_back, R.id.tv_gd, R.id.image_add, R.id.iamge_lw, R.id.iamge_sp, R.id.tv_follow})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.image_back:
                finish();
                break;
            case R.id.tv_gd:      //朋友圈更多
                UserCircleActivity.startActivity(this, userId);
                break;
            case R.id.image_add:  //添加好友
                showGifDialog("0");
                break;
            case R.id.iamge_lw:   //曾送礼物
                showGifDialog("1");
                break;
            case R.id.iamge_sp:   //与她视频
                call();
                break;
            case R.id.tv_follow:
                follow();
                break;
        }
    }

    /**
     * 视频
     */
    private void call() {
        if (homeInfoBean != null) {
            chatHelper = new VideoChatHelper();
            chatHelper.createRoom(this, userId, homeInfoBean.getFollow());
        }
    }

    public void follow() {
        if (follow.equals("1")) {
            homePageFollow.delaeteFollow(userId, getToken());
        } else {
            homePageFollow.addFollow(userId, getToken());
        }
//        Event event = new Event(1, 100000000, 60);
//        event.setConfig("hello");
//        event.setBroadcastOnlineOnly(true);
//        NIMClient.getService(EventSubscribeService.class).publishEvent(event);
    }

    public void showGifDialog(String type) {
        if (giftDialog == null) {
            giftDialog = new GiftDialog(this, type, receiveUserId, getToken());
        }
        giftDialog.show();
    }


    /**
     * 数据处理
     *
     * @param baseBean
     */
    public void SuccessData(BaseBean baseBean) {
        if (baseBean.isSuccess()) {
            try {
                homeInfoBean = new Gson().fromJson(baseBean.getJsonData(), HomeInfoBean.class);
                //banner图
                photoalbumListBeans = homeInfoBean.getPhotoalbumList();
                listBanner.clear();
                for (int i = 0; i < photoalbumListBeans.size(); i++) {
                    listBanner.add(HttpHost.qiNiu + photoalbumListBeans.get(i).getPicture());
                }
                initBanner();
                //朋友圈
                pictureListBeans = homeInfoBean.getPictureList();
                friendsAdapter = new HomeFriendsAdapter(this, pictureListBeans);
                pyqRecyler.setAdapter(friendsAdapter);
                //礼物榜单
                giftListBeans = homeInfoBean.getGiftList();
                giftAdapter = new HomeGiftAdapter(this, giftListBeans);
                lwRecyler.setAdapter(giftAdapter);
                receiveUserId = homeInfoBean.getUserInfoMap().getUserId();
                //获取基本信息
                GlideTools.setVipResourceS(imageLeave, Integer.parseInt(homeInfoBean.getUserInfoMap().getGrade()));
                tvName.setText(homeInfoBean.getUserInfoMap().getNickName());
                tvFans.setText(homeInfoBean.getFollowNum().getFollowNum() + "粉丝");
                tvJinb.setText(homeInfoBean.getUserInfoMap().getProfit() + "T币/分钟");
                tvSg.setText(homeInfoBean.getUserInfoMap().getStature() + "cm");
                tvJob.setText(homeInfoBean.getUserInfoMap().getJob());
                tvShengRi.setText(homeInfoBean.getUserInfoMap().getBirthday());
                tvShpz.setText(homeInfoBean.getUserInfoMap().getQualityLife());
                tvYjxg.setText(homeInfoBean.getUserInfoMap().getDrink());
                tvNsr.setText(homeInfoBean.getUserInfoMap().getYearIncome());
                tvZzc.setText(homeInfoBean.getUserInfoMap().getTotalAssets());
                if (homeInfoBean.getFollow().equals("1")) {
                    tvFollow.setText("已关注");
                } else {
                    tvFollow.setText("关注");
                }
                follow = homeInfoBean.getFollow();   //是否关注

            } catch (JsonSyntaxException e) {
                e.printStackTrace();
            }
        } else {
            RxToast.normal(getResources().getString(R.string.login_out));
            SPUtils.clear(this);
            startActivity(LoginActivity.class);
            finish();
        }
    }


    /**
     * 添加关注
     */
    @Override
    public void AddFollowResult(String type) {
        if (type.equals("1")) {
            RxToast.normal("关注成功");
            follow = "1";
            tvFollow.setText("已关注");
        } else {
            RxToast.normal("取消关注成功");
            follow = "0";
            tvFollow.setText("关注");
        }
        EventBus.getDefault().post(new VideoChatEvent(VideoChatEvent.FOLLOW_SUCCESS, type));
    }


    /**
     * 视频聊天页面相关事件
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onVideoChatEvent(VideoChatEvent event) {
        switch (event.type) {
            case VideoChatEvent.FOLLOW:
                follow();
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (chatHelper != null) {
            chatHelper.onResume();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
