package com.kw.top.ui.fragment.find;

import com.kw.top.base.BasePresenter;
import com.kw.top.base.BaseView;
import com.kw.top.bean.AllUserBean;
import com.kw.top.bean.BaseBean;
import com.kw.top.bean.GiftBean;

import java.util.List;

/**
 * author: Administrator
 * data  : 2018/5/6
 * des   :
 */

public class FindContract {

    /**
     * View中要实现的方法
     */
    interface View extends BaseView {
        void getUserResult(BaseBean baseBean);

        void queryAllGiftResult(BaseBean baseBean);

        void addFriendResult(BaseBean baseBean);

        void sendGiftResult(BaseBean baseBean);

        void updateAddress(boolean success);
    }

    /**
     * Presenter中要实现的方法
     */
    interface Presenter extends BasePresenter<View> {
        void getAllUserList(String newLoginDate, String city, String distance, String liveness, String newDate, String sex, String nowPage, String pageNum, String token, String lon, String lat);

        void queryAllGift(String token);

        void sendGiftAddFriend(String giftId, String num, String receiveUserId, String token);

        void sendGift(String giftId, String num, String receiveUserId, String token);

        void updateAddress(String lon, String lat, String city, String token);
    }

}
