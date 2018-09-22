package com.kw.top.base;

/**
 * Created by zzy on 2018/1/9.
 * mail: 623932889@qq.com
 * Describe:
 */

public class BasePresenterImpl<V extends BaseView> implements BasePresenter<V> {

    protected V mView;

    @Override
    public void attachView(V view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
    }

}
