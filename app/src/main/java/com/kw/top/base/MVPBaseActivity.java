package com.kw.top.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import java.lang.reflect.ParameterizedType;

/**
 * Created by zzy on 2018/1/9.
 * mail: 623932889@qq.com
 * Describe: mvp架构activity的基础类
 */

public abstract class MVPBaseActivity<V extends BaseView,T extends BasePresenterImpl<V>> extends BaseActivity_ implements BaseView{

    public T mPresenter;

    @Override
    public Context getContext() {
        return this;
    }

    public <T> T getInstance(Object o, int i){
        try {
            return ((Class<T>) ((ParameterizedType) (o.getClass()
                    .getGenericSuperclass())).getActualTypeArguments()[i])
                    .newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        try {
            mPresenter = getInstance(this, 1);
            mPresenter.attachView((V) this);
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.i("tag", "onCreate ---- " + ex.getMessage());
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null){
            mPresenter.detachView();
        }
    }
}
