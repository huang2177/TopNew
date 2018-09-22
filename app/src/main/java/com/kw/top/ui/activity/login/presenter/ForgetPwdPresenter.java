package com.kw.top.ui.activity.login.presenter;

import com.kw.top.base.BasePresenterImpl;
import com.kw.top.bean.BaseBean;
import com.kw.top.retrofit.Api;
import com.kw.top.ui.activity.login.contract.ForgetPwdContract;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * author: 正义
 * date  : 2018/4/17
 * desc  :
 */

public class ForgetPwdPresenter extends BasePresenterImpl<ForgetPwdContract.View> implements ForgetPwdContract.Presenter{
    @Override
    public void changePwd(String phone, String code, String newPwd1, String newPwd2) {
        Api.getApiService().changePwd(phone,code,newPwd1,newPwd2)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Action1<BaseBean>() {
                    @Override
                    public void call(BaseBean baseBean) {
                        mView.changePwdResult(baseBean);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mView.changePwdResult(null);
                    }
                });
    }

    @Override
    public void sendCode(String phone) {
        Api.getApiService().sendPhoneCode(phone)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Action1<BaseBean>() {
                    @Override
                    public void call(BaseBean baseBean) {
                        mView.sendCodeResult(baseBean);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mView.sendCodeResult(null);
                    }
                });
    }


}
