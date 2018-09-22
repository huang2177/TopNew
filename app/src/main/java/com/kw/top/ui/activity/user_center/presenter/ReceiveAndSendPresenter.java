package com.kw.top.ui.activity.user_center.presenter;

import com.kw.top.base.BasePresenterImpl;
import com.kw.top.bean.BaseBean;
import com.kw.top.bean.GiftBean;
import com.kw.top.bean.ReceiveGiftBean;
import com.kw.top.bean.ReceiveRedBean;
import com.kw.top.bean.SendGiftBean;
import com.kw.top.bean.SendRedBean;
import com.kw.top.retrofit.Api;
import com.kw.top.ui.activity.user_center.contract.ReceiveAndSendContract;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * author: zy
 * data  : 2018/5/22
 * des   :
 */

public class ReceiveAndSendPresenter extends BasePresenterImpl<ReceiveAndSendContract.View> implements ReceiveAndSendContract.Presenter {
    @Override
    public void getReceiveGift(String token) {
        Api.getApiService().getGiftByUser(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {

                    }
                })
                .subscribe(new Action1<BaseBean<ReceiveGiftBean>>() {
                    @Override
                    public void call(BaseBean<ReceiveGiftBean> baseBean) {
                        mView.receiveGiftResult(baseBean);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mView.receiveGiftResult(null);
                    }
                });
    }

    @Override
    public void getReceiveRed(String token) {
        Api.getApiService().receiveRedPackage(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {

                    }
                })
                .subscribe(new Action1<BaseBean<ReceiveRedBean>>() {
                    @Override
                    public void call(BaseBean<ReceiveRedBean> baseBean) {
                        mView.receiveRedResult(baseBean);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mView.receiveRedResult(null);
                    }
                });
    }

    @Override
    public void getSendGift(String token) {
        Api.getApiService().sendGift(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {

                    }
                })
                .subscribe(new Action1<BaseBean<SendGiftBean>>() {
                    @Override
                    public void call(BaseBean<SendGiftBean> baseBean) {
                        mView.sendGiftResult(baseBean);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mView.sendGiftResult(null);
                    }
                });
    }

    @Override
    public void getSendRed(String token) {
        Api.getApiService().sendRedPackageList(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {

                    }
                })
                .subscribe(new Action1<BaseBean<SendRedBean>>() {
                    @Override
                    public void call(BaseBean<SendRedBean> baseBean) {
                        mView.sendRedResult(baseBean);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mView.sendRedResult(null);
                    }
                });
    }
}
