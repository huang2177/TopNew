package com.kw.top.ui.activity.person_info;

import com.kw.top.base.BasePresenter;
import com.kw.top.base.BaseView;
import com.kw.top.bean.BaseBean;
import com.kw.top.bean.InfoBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * author: 正义
 * date  : 2018/4/17
 * desc  :
 */

public class PersonInfoContract {

    /**
     * View中要实现的方法
     */
    interface View extends BaseView {
        void updataResult(BaseBean baseBean);
        void getDataResult(BaseBean DataBean);
    }
    /**
     * Presenter中要实现的方法
     */
    interface Presenter extends BasePresenter<View> {
        /**
         * 获得传入选择弹框的数据集合
         * @param type 传入类型
         * @return
         */
        ArrayList<String> getInfoList(int type);

        void getInfoData(String token);
        void updataInfo(Map<String, String> map);
    }

}
