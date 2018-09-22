package com.kw.top.ui.activity.person_info;

import com.kw.top.base.BasePresenterImpl;
import com.kw.top.bean.BaseBean;
import com.kw.top.bean.InfoBean;
import com.kw.top.retrofit.Api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * author: 正义
 * date  : 2018/4/17
 * desc  :
 */

public class PersonInfoPresenter extends BasePresenterImpl<PersonInfoContract.View> implements PersonInfoContract.Presenter {

    @Override
    public void getInfoData(String token) {
        Api.getApiService().getInfoData(token)
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
                        mView.getDataResult(baseBean);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mView.getDataResult(null);
                    }
                });
    }

    @Override
    public void updataInfo(Map<String, String> map) {
        Api.getApiService().updataInfo(map)
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
                        mView.updataResult(baseBean);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mView.updataResult(null);
                    }
                });
    }


    ArrayList<String> list = new ArrayList<>();

    @Override
    public ArrayList<String> getInfoList(int type) {
        list.clear();
        switch (type) {
            case 1:             //获得性别
                list.add("男");
                list.add("女");
                break;
            case 2:             //获得身高列表
                for (int i = 140; i <= 250; i++) {
                    list.add(i + "");
                }
                break;
            case 3:             //获得年收入
                list.add("100万以下");
                list.add("100万-500万");
                list.add("500万-1000万");
                list.add("1000万-3000万");
                list.add("3000万-5000万");
                list.add("5000万-1亿");
                list.add("1亿以上");
                break;
            case 4:             //获得总资产
                list.add("50万-100万");
                list.add("100万-500万");
                list.add("500万-1000万");
                list.add("1000万-3000万");
                list.add("3000万-5000万");
                list.add("5000万-1亿");
                list.add("1亿-5亿");
                list.add("5亿以上");
                break;
            case 5:             //获得生活品质
                list.add("简约生活");
                list.add("浪漫情调");
                list.add("轻微奢侈");
                list.add("高度奢侈");
                break;
            case 6:             //获得抽烟习惯
                list.add("从不吸烟");
                list.add("偶尔一根");
                list.add("戒烟中");
                list.add("老烟枪");
                break;
            case 7:             //获得喝酒习惯
                list.add("滴酒不沾");
                list.add("小酌一杯");
                list.add("社交应酬");
                list.add("品酒达人");
                list.add("千杯不醉");
                break;
        }
        return list;
    }

}
