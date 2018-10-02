package com.kw.top.ui.activity.HomePage;

import com.kw.top.bean.BaseBean;
import com.kw.top.retrofit.Api;
import com.kw.top.ui.fragment.find.HomePageDetailsActivity;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/9/30.
 */

public class HomePageFollow {

    private HomePageDetailsActivity mActivity;


    public HomePageFollow(HomePageDetailsActivity mActivity) {
        this.mActivity = mActivity;
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
                        mActivity.AddFollowResult("1");
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mActivity.AddFollowResult(null);
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
                        mActivity.AddFollowResult("0");
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mActivity.AddFollowResult(null);
                    }
                });
    }

    /**
     * 获取首页数据
     *
     * @param userId
     * @param token
     */
    public void getHonePageData(String userId, String token) {
        mActivity.showProgressDialog();
        Api.getApiService().getuserInfoHomepage(userId, token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Action1<BaseBean>() {
                    @Override
                    public void call(BaseBean baseBean) {
                        mActivity.hideProgressDialog();
                        mActivity.SuccessData(baseBean);


                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mActivity.hideProgressDialog();
                    }
                });
    }
}
