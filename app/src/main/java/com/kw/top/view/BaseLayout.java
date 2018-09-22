package com.kw.top.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import butterknife.ButterKnife;

/**
 * Created by lsd on 17/2/6.
 */

public abstract class BaseLayout extends FrameLayout {
    protected String mTag;

    public abstract int getLayoutId();

    public abstract void initView();

    public void initListener() {
    }

    public void initAttrs(AttributeSet attrs) {
    }

    public BaseLayout(Context context) {
        this(context, null);
    }

    public BaseLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mTag = getClass().getSimpleName();
        initAttrs(attrs);
        if (getLayoutId() > 0) {
            View.inflate(getContext(), getLayoutId(), this);
            ButterKnife.bind(this);
        }
        initView();
        initListener();
    }

    protected int dp2px(int dp) {
        return (int) (getContext().getResources().getDisplayMetrics().density * dp + 0.5);
    }

    protected int getScreenHeight() {
        return getContext().getResources().getDisplayMetrics().heightPixels;
    }

    protected int getScreenWidth() {
        return getContext().getResources().getDisplayMetrics().widthPixels;
    }


}
