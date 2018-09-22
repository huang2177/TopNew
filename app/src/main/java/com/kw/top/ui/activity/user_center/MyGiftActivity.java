package com.kw.top.ui.activity.user_center;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.kw.top.R;
import com.kw.top.adapter.DiamonAdapter;
import com.kw.top.base.BaseActivity;
import com.kw.top.bean.BaseBean;
import com.kw.top.bean.DiamondBean;
import com.kw.top.bean.MyAccountBean;
import com.kw.top.bean.MyGiftBean;
import com.kw.top.bean.event.CenterCouponEvent;
import com.kw.top.retrofit.Api;
import com.kw.top.tools.ComResultTools;
import com.kw.top.ui.activity.login.LoginActivity;
import com.kw.top.utils.RxToast;
import com.kw.top.utils.SPUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * author: zy
 * data  : 2018/5/9
 * des   : 我的礼物
 */

public class MyGiftActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_title_right)
    TextView mTvTitleRight;
    @BindView(R.id.relative_title)
    RelativeLayout mRelativeTitle;
    @BindView(R.id.tv_all_liquan)
    TextView mTvAllLiquan;
    @BindView(R.id.tv_receive_gift)
    TextView mTvReceiveGift;
    @BindView(R.id.rl_receive_gift)
    RelativeLayout mRlReceiveGift;
    @BindView(R.id.tv_receive_red)
    TextView mTvReceiveRed;
    @BindView(R.id.rl_receive_red_packet)
    RelativeLayout mRlReceiveRedPacket;
    private Dialog mDialog;
    private DiamonAdapter mDiamonAdapter;
    private List<DiamondBean.ProductlListBean> diamonList = new ArrayList<>();
    private int allCoupon;

    @Override
    public int getContentView() {
        return R.layout.activity_my_gift;
    }

    public void initView() {
        mTvTitle.setText("我的礼物");
//        mTvTitleRight.setText("兑换");
    }

    public void initData() {
        showProgressDialog();
        Api.getApiService().myGift(getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {

                    }
                })
                .subscribe(new Action1<BaseBean>() {
                    @Override
                    public void call(BaseBean baseBean) {
                        hideProgressDialog();
                        if (baseBean.isSuccess()) {
                            MyGiftBean myGiftBean = null;
                            try {
                                myGiftBean = new Gson().fromJson(baseBean.getJsonData(), new TypeToken<MyGiftBean>() {
                                }.getType());
                            } catch (JsonSyntaxException e) {
                                e.printStackTrace();
                            }
                            allCoupon = (int) Double.parseDouble(myGiftBean.getCouponsSum());
                            mTvAllLiquan.setText(allCoupon+"");
                            mTvReceiveGift.setText("总计:" + myGiftBean.getGiftCoupons() + " 礼券");
                            mTvReceiveRed.setText("总计:" + myGiftBean.getRedCoupons());
                        } else {
                            ComResultTools.resultData(MyGiftActivity.this,baseBean);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        hideProgressDialog();
                    }
                });
    }

    public void initListener() {
//        mTvTitleRight.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getDiamondList();
//            }
//        });
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mRlReceiveGift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReceiveAndSendActivity.startActivity(MyGiftActivity.this, 1);
            }
        });
        mRlReceiveRedPacket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReceiveAndSendActivity.startActivity(MyGiftActivity.this, 0);
            }
        });
    }

    //获得充值钻石列表
    private void getDiamondList() {
        Api.getApiService().couponsToJewelList(getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        showProgressDialog();
                    }
                })
                .subscribe(new Action1<BaseBean>() {
                    @Override
                    public void call(BaseBean baseBean) {
                        hideProgressDialog();
                        if (baseBean.isSuccess()) {
                            try {
                                DiamondBean diamondBean = new Gson().fromJson(baseBean.getJsonData(), new TypeToken<DiamondBean>() {
                                }.getType());
                                diamonList.clear();
                                diamonList.addAll(diamondBean.getProductlList());
                                showDiamonDialog();
                            } catch (JsonSyntaxException e) {
                                e.printStackTrace();
                            }
                        } else {
                            ComResultTools.resultData(MyGiftActivity.this,baseBean);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        hideProgressDialog();
                    }
                });

    }

    //充值钻石dialog
    private void showDiamonDialog() {
        mDiamonAdapter = new DiamonAdapter(this, diamonList, 1);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_diamon, null, false);
        GridView gridView = (GridView) view.findViewById(R.id.grid_view_diamon);
        gridView.setAdapter(mDiamonAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mDialog.dismiss();
                //兑换
                convertDiamond(diamonList.get(i).getId()+"",diamonList.get(i).getAmount());
            }
        });

        mDialog = new Dialog(this);
        mDialog.setContentView(view);
        mDialog.setCanceledOnTouchOutside(true);
        Window window = mDialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.BOTTOM);
        mDialog.show();
    }

    //礼券兑换钻石
    private void convertDiamond(String id, final String amount) {
        showProgressDialog();
        Api.getApiService().couponsToJewel(id,getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Action1<BaseBean>() {
                    @Override
                    public void call(BaseBean baseBean) {
                        hideProgressDialog();
                        if (baseBean.isSuccess()){
                            RxToast.normal("兑换成功");
                            allCoupon = allCoupon - Integer.parseInt(amount);
                            mTvAllLiquan.setText(allCoupon+"");
                            EventBus.getDefault().post(new CenterCouponEvent(true,false));
                        }else {
                            ComResultTools.resultData(MyGiftActivity.this,baseBean);
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
}
