package com.kw.top.ui.activity.circle;

import com.kw.top.base.BasePresenter;
import com.kw.top.base.BaseView;
import com.kw.top.bean.BaseBean;

import java.util.List;

/**
 * author: zy
 * data  : 2018/6/4
 * des   :
 */

public class SendCircleContract {

    interface View extends BaseView {
        void upFiledResult();
        void upSuccessResult();
        void finishTaskResult(BaseBean baseBean);
        void addActiveResult(BaseBean baseBean);
    }

    interface Presenter extends BasePresenter<View> {
        void upFile(String token, List<String> paths, String content, String dynamicType,
                    int type,String taskId,String activeId,boolean isGroupTask);
        void finishTask(String taskId,String urlAddress,String token);
    }

}
