package com.kw.top.ui.activity.login.contract;

import com.kw.top.base.BasePresenter;
import com.kw.top.base.BaseView;

/**
 * author: zy
 * data  : 2018/5/20
 * des   :
 */

public class HeartBeatGril1Contract {

    /**
     * View中要实现的方法
     */
    public interface View extends BaseView {
    }
    /**
     * Presenter中要实现的方法
     */
    public interface Presenter extends BasePresenter<View> {
        void addLickObject(String objectId, String token);
    }


}
