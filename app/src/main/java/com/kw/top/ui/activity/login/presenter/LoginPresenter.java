package com.kw.top.ui.activity.login.presenter;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.kw.top.R;
import com.kw.top.app.BaseApplication;
import com.kw.top.base.BasePresenterImpl;
import com.kw.top.bean.BaseBean;
import com.kw.top.bean.CircleDetailsBean;
import com.kw.top.bean.LoginBean;
import com.kw.top.retrofit.Api;
import com.kw.top.ui.activity.login.contract.LoginContract;
import com.kw.top.utils.RxToast;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * author: 正义
 * date  : 2018/4/17
 * desc  :
 */

public class LoginPresenter extends BasePresenterImpl<LoginContract.View> implements LoginContract.Presenter {

    @Override
    public void login(String phone, String password, String lon, String lat, String city,String registrationId) {
        Api.getApiService().login(phone, password, lon, lat, city,registrationId)
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
                            mView.loginResult(baseBean);

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mView.loginResult(null);
                    }
                });
    }
}
