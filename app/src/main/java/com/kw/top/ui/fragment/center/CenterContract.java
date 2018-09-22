package com.kw.top.ui.fragment.center;

import com.kw.top.base.BasePresenter;
import com.kw.top.base.BaseView;
import com.kw.top.bean.BaseBean;

/**
 * author: zy
 * data  : 2018/6/13
 * des   :
 */

public class CenterContract {

    interface View extends BaseView {
        void upPhotoResult(BaseBean baseBean, String key);
    }

    interface Presenter extends BasePresenter<View> {
        void upPhoto(String path, String taken);
    }

}
