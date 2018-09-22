package com.kw.top.ui.activity.user_center.contract;

import com.kw.top.base.BasePresenter;
import com.kw.top.base.BaseView;
import com.kw.top.bean.BaseBean;
import com.kw.top.bean.GiftBean;
import com.kw.top.bean.ReceiveGiftBean;
import com.kw.top.bean.ReceiveRedBean;
import com.kw.top.bean.SendGiftBean;
import com.kw.top.bean.SendRedBean;

import java.util.List;

/**
 * author: zy
 * data  : 2018/5/22
 * des   :
 */

public class ReceiveAndSendContract {

    /**
     * View中要实现的方法
     */
    public interface View extends BaseView {
        void receiveRedResult(BaseBean<ReceiveRedBean> baseBean);
        void receiveGiftResult(BaseBean<ReceiveGiftBean> baseBean);
        void sendRedResult(BaseBean<SendRedBean> baseBean);
        void sendGiftResult(BaseBean<SendGiftBean> baseBean);
    }
    /**
     * Presenter中要实现的方法
     */
    public interface Presenter extends BasePresenter<View> {
        void getReceiveGift(String token);
        void getReceiveRed(String token);
        void getSendGift(String token);
        void getSendRed(String token);
    }

}
