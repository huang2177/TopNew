package com.kw.top.ui.fragment.club;

import com.kw.top.base.BasePresenter;
import com.kw.top.base.BaseView;

/**
 * author: Administrator
 * data  : 2018/5/6
 * des   :
 */

public class ClubContract {

    /**
     * View中要实现的方法
     */
    interface View extends BaseView {
    }
    /**
     * Presenter中要实现的方法
     */
    interface Presenter extends BasePresenter<View> {
    }

}
