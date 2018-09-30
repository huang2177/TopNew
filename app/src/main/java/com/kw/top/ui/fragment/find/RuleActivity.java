package com.kw.top.ui.fragment.find;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.kw.top.R;
import com.kw.top.base.BaseActivity_;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/9/30.
 */

public class RuleActivity extends BaseActivity_ {

    @BindView(R.id.tv_title)
    TextView textView;

    @Override
    public int getContentView()  {
        return R.layout.activity_rule;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        ButterKnife.bind(this);
        textView.setText("文明公约");
    }

    @OnClick(R.id.iv_back)
    public void OnClick() {
        finish();
    }

}
