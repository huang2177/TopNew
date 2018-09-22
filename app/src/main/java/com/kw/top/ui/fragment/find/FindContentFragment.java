package com.kw.top.ui.fragment.find;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.kw.top.R;
import com.kw.top.adapter.FindAdapter;
import com.kw.top.adapter.GiftAdapter;
import com.kw.top.base.MVPBaseFragment;
import com.kw.top.bean.AllUserBean;
import com.kw.top.bean.BaseBean;
import com.kw.top.bean.GiftBean;
import com.kw.top.bean.event.FindChooseEvent;
import com.kw.top.listener.OnClickListener;
import com.kw.top.tools.ComResultTools;
import com.kw.top.tools.ConstantValue;
import com.kw.top.ui.activity.login.LoginActivity;
import com.kw.top.utils.RxToast;
import com.kw.top.utils.SPUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * author: Administrator
 * data  : 2018/5/6
 * des   : 发现（内容）
 */

public class FindContentFragment extends MVPBaseFragment<FindContract.View, FindPresenter> implements FindContract.View,
        OnRefreshListener, OnLoadMoreListener, OnClickListener {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout mRefreshLayout;
    private int type = 0;//0 距离 1 活跃度 2 最新加入
    private FindAdapter mAdapter;
    private String distance = "",//距离 1 or ""
            liveness = "", //活跃度 1 or ""
            newDate = "", //最新加入 1 or ""
            sex = "",
            newLoginDate = "1"; // 性别(0.女，1.男，查询全部不传

    private String intentSex = ""; // 0,1,2
    private int nowPage = 1;
    private String pageNum = ConstantValue.ONE_PAGE_NUM;
    private List<AllUserBean> mList = new ArrayList<>();
    private String loadMore = "";
    private Dialog dialog;
    private List<GiftBean> mDiamondList = new ArrayList<>();//钻石list
    private List<GiftBean> mAllGiftList = new ArrayList<>();//所有礼物list
    private GiftAdapter mGiftAdapter;
    private String receiveUserId;//添加好友id
    private String toAddUsername;
    private String lon, lat, city;
    //声明mlocationClient对象
    public AMapLocationClient mlocationClient;
    //声明mLocationOption对象
    public AMapLocationClientOption mLocationOption = null;
    private boolean updateAddress;//位置是否更新

    public static FindContentFragment newInstant(int type) {
        FindContentFragment fragment = new FindContentFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("TYPE", type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getContentView() {
        return R.layout.frament_find_content;
    }

    @Override
    public void initView(View view, Bundle savedInstanceState) {
        //type = getArguments().getInt("TYPE", 0);
        /*switch (type) {
            case 0:
                distance = "1";
                break;
            case 1:
                liveness = "1";
                break;
            case 2:
                newDate = "1";
                break;
        }*/
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setOnLoadMoreListener(this);
        mAdapter = new FindAdapter(getActivity(), mList, this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);

        mGiftAdapter = new GiftAdapter(getContext());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initLocation();
    }

    @Override
    public void initListener() {
        EventBus.getDefault().register(this);
    }

    @Override
    public void initData() {

    }

    @Override
    protected void initPresenter() {
        showProgressDialog();
        getAllUserList();
    }

    private void getAllUserList() {
        mPresenter.getAllUserList(newLoginDate,"", distance, liveness, newDate, sex, nowPage + "", pageNum, getToken(), lon, lat);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        if (loadMore.equals("1")) {
            nowPage++;
            getAllUserList();
        } else {
            mRefreshLayout.finishLoadMoreWithNoMoreData();
        }
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        nowPage = 1;
        getAllUserList();
    }

    @Override
    public void getUserResult(BaseBean baseBean) {
        mRefreshLayout.finishRefresh();
        mRefreshLayout.setNoMoreData(false);
        mRefreshLayout.finishLoadMore();
        hideProgressDialog();
        if (baseBean == null) return;
        if (baseBean.isSuccess()) {
            List<AllUserBean> userBeanList = null;
            try {
                userBeanList = new Gson().fromJson(baseBean.getJsonData(), new TypeToken<List<AllUserBean>>() {
                }.getType());
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
            }
            if (nowPage == 1) {
                mList.clear();
            }
            mList.addAll(userBeanList);
            mAdapter.notifyDataSetChanged();
            loadMore = baseBean.getMsg();
        } else if (baseBean.getCode().equals("-44")) {
                RxToast.normal(getResources().getString(R.string.login_out));
                SPUtils.clear(getActivity());
                startActivity(LoginActivity.class);
                getActivity().finish();
            }


           // ComResultTools.resultData(getActivity(), baseBean);

    }

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
            showGiftDialog();
        } else {
            RxToast.normal(baseBean.getMsg());
        }
    }

    @Override
    public void addFriendResult(BaseBean baseBean) {
        hideProgressDialog();
        if (null != baseBean && baseBean.isSuccess()) {
            RxToast.normal("申请成功,请等待好友同意");
            //参数为要添加的好友的username和添加理由
//            try {
//                EMClient.getInstance().contactManager().addContact(toAddUsername, "约炮");
//            } catch (HyphenateException e) {
//                e.printStackTrace();
//            }
        } else if (baseBean.getCode().equals("-44")) {
            RxToast.normal(getResources().getString(R.string.login_out));
            SPUtils.clear(getContext());
            startActivity(LoginActivity.class);
        } else {
            RxToast.normal(baseBean.getMsg());
        }
    }

    @Override
    public void sendGiftResult(BaseBean baseBean) {
        if (baseBean == null) {
            RxToast.normal(getResources().getString(R.string.net_error));
        } else if (baseBean.isSuccess()) {
            RxToast.normal("赠送成功");
        } else {
            ComResultTools.resultData(getActivity(), baseBean);
        }
    }

    @Override
    public void updateAddress(boolean success) {
        updateAddress = success;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSexChooseFind(FindChooseEvent chooseEvent) {
        if (!sex.equals(chooseEvent.getSex())) {
            sex = chooseEvent.getSex();
            mRefreshLayout.autoRefresh();
        }
    }

    private void showGiftDialog() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_friend, null);
        GridView gridView = view.findViewById(R.id.grid_view);
        gridView.setAdapter(mGiftAdapter);
        mGiftAdapter.setList(friend.equals("1") ? mAllGiftList : mDiamondList);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                dialog.dismiss();
                if (friend.equals("1")) {
                    mPresenter.sendGift(mAllGiftList.get(i).getGiftId() + "", "1", receiveUserId, getToken());
                } else {
                    senFriend(i);
                }
            }
        });
        dialog = new Dialog(getActivity(), R.style.charge_dialog_style);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(true);
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.BOTTOM); // 此处可以设置dialog显示的位置
        window.setWindowAnimations(R.style.popupAnimation); // 添加动画
        dialog.show();
    }

    private void senFriend(int i) {
        String giftid = mDiamondList.get(i).getGiftId() + "";
        String num = "1";
        showProgressDialog();
        mPresenter.sendGiftAddFriend(giftid, num, receiveUserId, getToken());
    }

    private String friend;

    @Override
    public void onClick(View view, int position) {
        receiveUserId = mList.get(position).getUserId();
        friend = mList.get(position).getFriends();
        toAddUsername = mList.get(position).getAccount();
        if (mAllGiftList.size() == 0) {
            showProgressDialog();
            mPresenter.queryAllGift(getToken());
        } else {
            showGiftDialog();
        }
    }

    private void initLocation() {
        mlocationClient = new AMapLocationClient(getContext());
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位监听
        mlocationClient.setLocationListener(mAMapLocationListener);
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
        //设置定位参数
        mlocationClient.setLocationOption(mLocationOption);
    }

    //可以通过类implement方式实现AMapLocationListener接口，也可以通过创造接口类对象的方法实现
    //以下为后者的举例：
    AMapLocationListener mAMapLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            if (amapLocation != null) {
                if (amapLocation.getErrorCode() == 0) {
                    //可在其中解析amapLocation获取相应内容。
                    lat = amapLocation.getLatitude() + "";//获取纬度
                    lon = amapLocation.getLongitude() + "";//获取经度
                    city = amapLocation.getCity();//.substring(0,amapLocation.getCity().length()-1);
                    if (!TextUtils.isEmpty(city) && !updateAddress) {
                        mPresenter.updateAddress(lon, lat, city, getToken());
                    }
//                    Log.e("tag", "==================  city  "  + "  " + lat + " | " + lon);
                } else {
                    RxToast.normal("位置信息获取失败");
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError", "location Error, ErrCode:"
                            + amapLocation.getErrorCode() + ", errInfo:"
                            + amapLocation.getErrorInfo());
                }
            }
        }
    };

    @Override
    public void onStart() {
        super.onStart();
//        启动定位
        if (mlocationClient != null)
            mlocationClient.startLocation();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mlocationClient != null)
            mlocationClient.stopLocation();//停止定位后，本地定位服务并不会被销毁
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mlocationClient != null)
            mlocationClient.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}

