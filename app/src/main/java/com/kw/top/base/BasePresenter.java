package com.kw.top.base;

/**
 * Created by zzy on 2018/1/9.
 * mail: 623932889@qq.com
 * Describe: MVP架构，P层基础类
 */

public interface BasePresenter<V extends BaseView> {

    void attachView(V view);

    void detachView();
}
