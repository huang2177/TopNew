package com.kw.top.ui.activity.HomePage;

import android.app.Activity;

import com.kw.top.bean.BaseBean;
import com.kw.top.retrofit.Api;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by shibing on 2018/9/25.
 */

public class AnchorPresenter {


    private Activity mActvity;
    private AnchorView anchorView;

    public AnchorPresenter(Activity mActvity, AnchorView anchorView) {
        this.mActvity = mActvity;
        this.anchorView = anchorView;
    }


    /**
     * 查询所有的礼物
     *
     * @param token
     */
    public void queryAllGift(String token) {
        Api.getApiService().getAllGift(token)
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
                        if (!baseBean.isSuccess()) {
                            return;
                        }
                        anchorView.queryAllGiftResult(baseBean);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        anchorView.queryAllGiftResult(null);
                    }
                });
    }


    /**
     * 送礼物 申请添加好友
     *
     * @param giftId
     * @param num
     * @param receiveUserId
     * @param token
     */
    public void sendGiftAddFriend(String giftId, String num, String receiveUserId, String token) {
        Api.getApiService().sendGiftAddFriend(giftId, num, receiveUserId, token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Action1<BaseBean>() {
                    @Override
                    public void call(BaseBean baseBean) {
                        if (!baseBean.isSuccess()) {
                            return;
                        }
                        anchorView.addFriendResult(baseBean);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        anchorView.addFriendResult(null);
                    }
                });
    }


    /**
     * 送礼物
     *
     * @param giftId
     * @param num
     * @param receiveUserId
     * @param token
     */
    public void sendGift(String giftId, String num, String receiveUserId, String token) {
        Api.getApiService().sendGift(giftId, num, receiveUserId, token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Action1<BaseBean>() {
                    @Override
                    public void call(BaseBean baseBean) {
                        anchorView.sendGiftResult(baseBean);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        anchorView.sendGiftResult(null);
                    }
                });
    }





}
