package com.kw.top.ui.fragment.find;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.kw.top.R;
import com.kw.top.adapter.GiftAdapter;
import com.kw.top.base.BaseActivity_;
import com.kw.top.bean.BaseBean;
import com.kw.top.bean.GiftBean;
import com.kw.top.retrofit.Api;
import com.kw.top.retrofit.HttpHost;
import com.kw.top.tools.ComResultTools;
import com.kw.top.tools.ConstantValue;
import com.kw.top.tools.GlideTools;
import com.kw.top.ui.activity.HomePage.AnchorPresenter;
import com.kw.top.ui.activity.HomePage.AnchorView;
import com.kw.top.ui.activity.login.LoginActivity;
import com.kw.top.ui.fragment.find.adapter.GlideImageLoader;
import com.kw.top.ui.fragment.find.adapter.HomeFriendsAdapter;
import com.kw.top.ui.fragment.find.adapter.HomeGiftAdapter;
import com.kw.top.ui.fragment.find.baen.HomeInfoBean;
import com.kw.top.utils.RxToast;
import com.kw.top.utils.SPUtils;
import com.kw.top.utils.StatusUtil;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by shibing on 2018/9/24.
 */

public class HomePageDetailsActivity extends BaseActivity_ implements AnchorView {


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


    private GiftAdapter mGiftAdapter;

    private List<GiftBean> mDiamondList = new ArrayList<>();//钻石list
    private List<GiftBean> mAllGiftList = new ArrayList<>();//所有礼物list
    private Dialog dialog;
    private String friend = "0";
    private String receiveUserId;
    private AnchorPresenter anchorPresenter;
    private String follow, friends;

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
        anchorPresenter = new AnchorPresenter(this, this);
        listBanner = new ArrayList<>();
        userId = getIntent().getStringExtra(ConstantValue.KEY_USER_ID);
        getHonePageData(userId, getToken());
        anchorPresenter.queryAllGift(getToken());   //查询所有的礼物
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


    /**
     *
     */
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
                break;
            case R.id.image_add:  //添加好友
                showGiftDialog("0");
                break;
            case R.id.iamge_lw:   //曾送礼物
                showGiftDialog("1");
                break;
            case R.id.iamge_sp:   //与她视频
//                AVChatKit.outgoingCall(this, getAccount()
//                        , UserInfoHelper.getUserDisplayName(getAccount())
//                        , AVChatType.VIDEO.getValue()
//                        , AVChatActivity.FROM_INTERNAL);
                break;
            case R.id.tv_follow:
                if (follow.equals("1")) {
                    tvFollow.setEnabled(false);
                    return;
                }
                anchorPresenter.addFollow(userId, getToken());
                break;
        }
    }


    /**
     * 获取首页数据
     */
    private void getHonePageData(String anchorId, String token) {
        showProgressDialog();
        Api.getApiService().getuserInfoHomepage(anchorId, token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Action1<BaseBean>() {
                    @Override
                    public void call(BaseBean baseBean) {
                        hideProgressDialog();
                        SuccessData(baseBean);


                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        hideProgressDialog();
                    }
                });

    }

    /**
     * 数据处理
     *
     * @param baseBean
     */
    private void SuccessData(BaseBean baseBean) {
        if (baseBean.isSuccess()) {
            try {
                homeInfoBean = new Gson().fromJson(baseBean.getJsonData(), new TypeToken<HomeInfoBean>() {
                }.getType());
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
                friends = homeInfoBean.getFriends(); //是否为好友

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
     * 礼物dialog
     */
    private void showGiftDialog(final String type) {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_friend, null);
        GridView gridView = view.findViewById(R.id.grid_view);
        mGiftAdapter = new GiftAdapter(this);
        gridView.setAdapter(mGiftAdapter);
        mGiftAdapter.setList(type.equals("1") ? mAllGiftList : mDiamondList);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                dialog.dismiss();
                if (type.equals("1")) {
                    anchorPresenter.sendGift(mAllGiftList.get(i).getGiftId() + "", "1", receiveUserId, getToken());
                } else {
                    anchorPresenter.sendGiftAddFriend(mDiamondList.get(i).getGiftId() + "", "1", receiveUserId, getToken());
                }
            }
        });
        dialog = new Dialog(this, R.style.charge_dialog_style);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(true);
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.BOTTOM); // 此处可以设置dialog显示的位置
        window.setWindowAnimations(R.style.popupAnimation); // 添加动画
        dialog.show();
    }


    /**
     * 查询所有的礼物
     *
     * @param baseBean
     */
    @Override
    public void queryAllGiftResult(BaseBean baseBean) {
        hideProgressDialog();
        if (null != baseBean && baseBean.isSuccess()) {
            List<GiftBean> giftBeans = new Gson().fromJson(baseBean.getJsonData(), new TypeToken<List<GiftBean>>() {
            }.getType());
            mAllGiftList.addAll(giftBeans);
            for (GiftBean gift : giftBeans) {
                if (gift.getAmountType().equals("1"))
                    mDiamondList.add(gift);
            }
        } else {
            RxToast.normal(baseBean.getMsg());
        }
    }

    /**
     * 添加好友
     *
     * @param baseBean
     */
    @Override
    public void addFriendResult(BaseBean baseBean) {
        hideProgressDialog();
        if (null != baseBean && baseBean.isSuccess()) {
            RxToast.normal("申请成功,请等待好友同意");
        } else if (baseBean.getCode().equals("-44")) {
            RxToast.normal(getResources().getString(R.string.login_out));
            SPUtils.clear(this);
            startActivity(LoginActivity.class);
        } else {
            RxToast.normal(baseBean.getMsg());
        }
    }

    /**
     * 赠送礼物
     *
     * @param baseBean
     */
    @Override
    public void sendGiftResult(BaseBean baseBean) {
        if (baseBean == null) {
            RxToast.normal(getResources().getString(R.string.net_error));
        } else if (baseBean.isSuccess()) {
            RxToast.normal("赠送成功");
        } else {
            ComResultTools.resultData(this, baseBean);
        }
    }

    /**
     * 添加关注
     *
     * @param baseBean
     */
    @Override
    public void AddFollowResult(BaseBean baseBean) {
        if (!baseBean.isSuccess()) {
            return;
        }
        RxToast.normal("关注成功");
        follow = "1";
        tvFollow.setText("已关注");
    }
}
