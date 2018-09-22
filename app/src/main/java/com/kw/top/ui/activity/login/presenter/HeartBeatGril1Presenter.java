package com.kw.top.ui.activity.login.presenter;

import com.kw.top.base.BasePresenterImpl;
import com.kw.top.bean.BaseBean;
import com.kw.top.retrofit.Api;
import com.kw.top.ui.activity.login.contract.HeartBeatGril1Contract;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * author: zy
 * data  : 2018/5/20
 * des   :
 */

public class HeartBeatGril1Presenter extends BasePresenterImpl<HeartBeatGril1Contract.View> implements HeartBeatGril1Contract.Presenter {
    @Override
    public void addLickObject(String objectId, String token) {
        Api.getApiService().addLikeObject(objectId,token)
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

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                });
    }

}
