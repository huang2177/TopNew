package com.kw.top.ui.fragment.news;

import com.kw.top.base.BasePresenter;
import com.kw.top.base.BaseView;
import com.kw.top.base.FriendBean;
import com.kw.top.bean.BaseBean;

import java.util.List;

/**
 * author: zy
 * data  : 2018/6/2
 * des   :
 */

public class NewsListContract {

    interface View extends BaseView {
        void getFriendListResult(BaseBean baseBean);
    }

    interface Presenter extends BasePresenter<View> {
        void getFriendsList(String token);
    }

}
