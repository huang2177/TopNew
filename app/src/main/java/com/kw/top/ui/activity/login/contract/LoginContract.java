package com.kw.top.ui.activity.login.contract;

import com.kw.top.base.BasePresenter;
import com.kw.top.base.BaseView;
import com.kw.top.bean.BaseBean;
import com.kw.top.bean.LoginBean;

/**
 * author: 正义
 * date  : 2018/4/17
 * desc  :
 */

public class LoginContract {

    /**
     * View中要实现的方法
     */
    public interface View extends BaseView {
        void loginResult(BaseBean baseBean);
    }
    /**
     * Presenter中要实现的方法
     */
    public interface Presenter extends BasePresenter<View> {
        void login(String phone, String password, String lon, String lat, String city,String registrationId);
    }

}
