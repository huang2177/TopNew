package com.kw.top.ui.activity.HomePage;

import android.app.Activity;

import com.kw.top.bean.BaseBean;
import com.kw.top.retrofit.Api;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/9/30.
 */

public class HomePageFollow {

    private HomePageView view;
    private Activity mActivity;


    public HomePageFollow(Activity mActivity, HomePageView view) {
        this.mActivity = mActivity;
        this.view = view;
    }


    /**
     * @param followId
     * @param token
     */
    public void addFollow(String followId, String token) {
        Api.getApiService().addMyFollow(followId, token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Action1<BaseBean>() {
                    @Override
                    public void call(BaseBean baseBean) {
                        if (!baseBean.isSuccess()) {
                            return;
                        }
                        view.AddFollowResult("add");
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        view.AddFollowResult(null);
                    }
                });
    }


    /**
     * @param followId
     * @param token
     */
    public void delaeteFollow(String followId, String token) {
        Api.getApiService().daleteMyFollow(followId, token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Action1<BaseBean>() {
                    @Override
                    public void call(BaseBean baseBean) {
                        if (!baseBean.isSuccess()) {
                            return;
                        }
                        view.AddFollowResult("delate");
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        view.AddFollowResult(null);
                    }
                });
    }
}
