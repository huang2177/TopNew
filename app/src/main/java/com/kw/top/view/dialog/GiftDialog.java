package com.kw.top.view.dialog;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kw.top.R;
import com.kw.top.adapter.GiftAdapter;
import com.kw.top.bean.BaseBean;
import com.kw.top.bean.GiftBean;
import com.kw.top.retrofit.HttpHost;
import com.kw.top.ui.activity.HomePage.AnchorPresenter;
import com.kw.top.ui.activity.HomePage.AnchorView;
import com.kw.top.utils.RxToast;
import com.netease.nim.avchatkit.activity.AVChatActivity;
import com.netease.nim.avchatkit.event.VideoChatEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/9/30.
 */

public class GiftDialog extends XBottomDialog implements AdapterView.OnItemClickListener, AnchorView {


    private GridView gridView;
    private AnchorPresenter anchorPresenter;
    private List<GiftBean> mDiamondList;//钻石list
    private List<GiftBean> mAllGiftList;//所有礼物list
    private String type, receiveUserId, Token;

    private int clickPosition;

    public GiftDialog(@NonNull Activity activity, String type, String receiveUserId, String Token) {
        super(activity);
        anchorPresenter = new AnchorPresenter(activity, this);
        anchorPresenter.queryAllGift(Token);
        mDiamondList = new ArrayList<>();
        mAllGiftList = new ArrayList<>();
        this.type = type;
        this.receiveUserId = receiveUserId;
        this.Token = Token;

    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_add_friend;
    }

    @Override
    protected void initView() {
        gridView = findViewById(R.id.grid_view);
    }

    @Override
    protected void setListener() {
        gridView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (type.equals("1")) {
            clickPosition = i;
            anchorPresenter.sendGift(mAllGiftList.get(i).getGiftId() + "", "1", receiveUserId, Token);
        } else {
            anchorPresenter.sendGiftAddFriend(mDiamondList.get(i).getGiftId() + "", "1", receiveUserId, Token);
        }
        dismiss();
    }


    /**
     * 查询所有的礼物
     *
     * @param baseBean
     */
    @Override
    public void queryAllGiftResult(BaseBean baseBean) {
        if (null != baseBean && baseBean.isSuccess()) {
            List<GiftBean> giftBeans = new Gson().fromJson(baseBean.getJsonData(), new TypeToken<List<GiftBean>>() {
            }.getType());
            mAllGiftList.addAll(giftBeans);
            for (GiftBean gift: giftBeans) {
                if (gift.getAmountType().equals("1"))
                    mDiamondList.add(gift);
            }
            GiftAdapter giftAdapter = new GiftAdapter(activity);
            gridView.setAdapter(giftAdapter);
            giftAdapter.setList(type.equals("1") ? mAllGiftList : mDiamondList);
        } else {
            RxToast.normal(baseBean.getMsg());
        }
    }

    /**
     * 添加好友赠送礼物
     *
     * @param baseBean
     */
    @Override
    public void addFriendResult(BaseBean baseBean) {
        if (baseBean.getCode().equals("0000")) {
            RxToast.normal("申请成功,请等待好友同意");
        } else {
            RxToast.normal(activity, baseBean.getMsg()).show();
        }
    }

    /**
     * 送礼物
     *
     * @param baseBean
     */
    @Override
    public void sendGiftResult(BaseBean baseBean) {
        if (baseBean == null) {
            RxToast.normal("赠送失败");
        } else if (baseBean.getCode().equals("0000")) {
            RxToast.normal("赠送成功");
            if (activity instanceof AVChatActivity) {
                EventBus.getDefault().post(new VideoChatEvent(VideoChatEvent.GIT_SHOW
                        , HttpHost.qiNiu + mAllGiftList.get(clickPosition).getGiftPicture()
                        , mAllGiftList.get(clickPosition).getGiftName()));
            }
        } else {
            RxToast.normal(baseBean.getMsg());
        }
    }

}
