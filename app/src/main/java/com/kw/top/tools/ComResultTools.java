package com.kw.top.tools;

import android.content.Context;
import android.content.Intent;

import com.kw.top.R;
import com.kw.top.bean.BaseBean;
import com.kw.top.ui.activity.login.LoginActivity;
import com.kw.top.utils.RxToast;
import com.kw.top.utils.SPUtils;

/**
 * author  ： zy
 * date    ： 2018/7/11
 * des     ：
 */

public class ComResultTools {

    public static void resultData(Context context,BaseBean baseBean){
        if (null == baseBean){
            RxToast.normal(context.getResources().getString(R.string.net_error));
            return;
        }
        if (baseBean.getCode().equals("-44")){
            RxToast.normal(baseBean.getMsg());
            SPUtils.clear(context);
            context.startActivity(new Intent(context,LoginActivity.class));
        }else {
            RxToast.normal(baseBean.getMsg());
        }
    }

}
