package com.kw.top.ui.activity.task;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.kw.top.R;
import com.kw.top.base.BaseActivity;
import com.kw.top.ui.fragment.task.TaskContentFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by shibing on 2018/9/4.
 */

public class TopTaskActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {

    @BindView(R.id.toptask_iamge)
    ImageView toptaskImage;


    @BindView(R.id.rb_left)
    RadioButton mRbLeft;
    @BindView(R.id.rb_right)
    RadioButton mRbRight;
    @BindView(R.id.radio_group_task)
    RadioGroup mRadioGroupTask;
    @BindView(R.id.frame_layout_task)
    FrameLayout mFrameLayoutTask;
    private TaskContentFragment hallTaskFragment, myTaskFragment;
    private FragmentManager mFragmentManager;
    private FragmentTransaction mTransaction;

    @Override
    public int getContentView() {
        return R.layout.activity_task;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        ButterKnife.bind(this);
        initData();
    }


    public void initData() {
        mRadioGroupTask.setOnCheckedChangeListener(this);
        mFragmentManager = getSupportFragmentManager();
        mTransaction = mFragmentManager.beginTransaction();
        if (null == hallTaskFragment) {
            hallTaskFragment = TaskContentFragment.newInstance(0);
            mTransaction.add(R.id.frame_layout_task, hallTaskFragment);
        }
        mTransaction.show(hallTaskFragment);
        mTransaction.commit();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        mTransaction = mFragmentManager.beginTransaction();
        switch (checkedId) {
            case R.id.rb_left:
                if (null != myTaskFragment) {
                    mTransaction.hide(myTaskFragment);
                }
                if (null == hallTaskFragment) {
                    hallTaskFragment = TaskContentFragment.newInstance(0);
                    mTransaction.add(R.id.frame_layout_task, hallTaskFragment);
                }
                mTransaction.show(hallTaskFragment);
                break;
            case R.id.rb_right:
                if (null != hallTaskFragment) {
                    mTransaction.hide(hallTaskFragment);
                }
                if (null == myTaskFragment) {
                    myTaskFragment = TaskContentFragment.newInstance(1);
                    mTransaction.add(R.id.frame_layout_task, myTaskFragment);
                }
                mTransaction.show(myTaskFragment);
                break;
        }
        mTransaction.commit();
    }


    @OnClick({R.id.toptask_iamge, R.id.iv_back})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.toptask_iamge:
                startActivity(PublishTaskActivity.class);
                break;
        }
    }
}
