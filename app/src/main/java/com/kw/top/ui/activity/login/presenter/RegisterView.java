package com.kw.top.ui.activity.login.presenter;

import com.kw.top.bean.BaseBean;

import java.util.List;

/**
 * Created by Administrator on 2018/10/9.
 */

public interface RegisterView {

    void UpPicSuccsee(String resKey);

    void UpMorePicSuccess(List<String> listkey);

    void UpGirlInfoSuccess(BaseBean baseBean);

    void UpSingInfoSuccess(BaseBean baseBean);
}
