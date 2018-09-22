package com.kw.top.ui.activity.login;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.kw.top.R;
import com.kw.top.base.BaseActivity_;

import butterknife.ButterKnife;

/**
 * Created by shibing on 2018/9/18.
 */

public class ChoiceSexActivity extends BaseActivity_ {
    @Override
    public int getContentView() {
        return R.layout.choicesex_activity;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        ButterKnife.bind(this);
    }
}
