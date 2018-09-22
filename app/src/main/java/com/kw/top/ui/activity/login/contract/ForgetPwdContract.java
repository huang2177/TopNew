package com.kw.top.ui.activity.login.contract;

import com.kw.top.base.BasePresenter;
import com.kw.top.base.BaseView;
import com.kw.top.bean.BaseBean;

/**
 * author: 正义
 * date  : 2018/4/17
 * desc  :
 */

public class ForgetPwdContract {

    /**
     * View中要实现的方法
     */
    public interface View extends BaseView {
        void sendCodeResult(BaseBean baseBean);
        void changePwdResult(BaseBean baseBean);
    }
    /**
     * Presenter中要实现的方法
     */
    public interface Presenter extends BasePresenter<View> {
        void changePwd(String phone, String code, String newPwd1, String newPwd2);

        void sendCode(String phone);
    }

}
