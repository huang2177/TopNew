package com.kw.top.base;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * author: 正义
 * date  : 2018/4/16
 * desc  : 禁止横屏切换
 */

public abstract class BaseActivity extends BaseActivity_ {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //禁止横屏
        BaseActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

}
