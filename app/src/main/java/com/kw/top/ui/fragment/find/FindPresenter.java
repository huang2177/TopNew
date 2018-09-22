package com.kw.top.ui.fragment.find;

import com.kw.top.base.BasePresenterImpl;
import com.kw.top.bean.AllUserBean;
import com.kw.top.bean.BaseBean;
import com.kw.top.bean.GiftBean;
import com.kw.top.retrofit.Api;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * author: Administrator
 * data  : 2018/5/6
 * des   :
 */

public class FindPresenter extends BasePresenterImpl<FindContract.View> implements FindContract.Presenter {

    @Override
    public void getAllUserList(String newLoginDate, String city, String distance, String liveness, String newDate, String sex, String nowPage, String pageNum, String token, String lon, String lat) {
        Api.getApiService().getAllUserList(newLoginDate,city, distance, liveness, newDate, sex, nowPage, pageNum, token, lon, lat)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Action1<BaseBean>() {
                    @Override
                    public void call(BaseBean baseBean) {
                        mView.getUserResult(baseBean);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mView.getUserResult(null);
                    }
                });
    }

    @Override
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
                        mView.queryAllGiftResult(baseBean);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mView.queryAllGiftResult(null);
                    }
                });
    }

    @Override
    public void sendGiftAddFriend(String giftId, String num, String receiveUserId, String token) {
        Api.getApiService().sendGiftAddFriend(giftId, num, receiveUserId, token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Action1<BaseBean>() {
                    @Override
                    public void call(BaseBean baseBean) {
                        mView.addFriendResult(baseBean);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mView.addFriendResult(null);
                    }
                });
    }

    @Override
    public void sendGift(String giftId, String num, String receiveUserId, String token) {
        Api.getApiService().sendGift(giftId, num, receiveUserId, token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Action1<BaseBean>() {
                    @Override
                    public void call(BaseBean baseBean) {
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
    public void updateAddress(String lon, String lat, String city, String token) {
        Api.getApiService().updateAddress(lon, lat, city, token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Action1<BaseBean>() {
                    @Override
                    public void call(BaseBean baseBean) {
                        if (baseBean.isSuccess())
                            mView.updateAddress(true);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                    }
                });
    }


}
