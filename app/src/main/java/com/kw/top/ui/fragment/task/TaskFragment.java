package com.kw.top.ui.fragment.task;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.kw.top.R;
import com.kw.top.base.BaseFragment;

import butterknife.BindView;

/**
 * author: Administrator
 * data  : 2018/5/6
 * des   : TOP任务
 */

public class TaskFragment extends BaseFragment implements RadioGroup.OnCheckedChangeListener,
        View.OnClickListener {


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

    public static TaskFragment newInstance() {
        TaskFragment fragment = new TaskFragment();
        return fragment;
    }

    @Override
    public int getContentView() {
        return R.layout.frament_task;
    }

    @Override
    public void initView(View view, Bundle savedInstanceState) {

    }

    @Override
    public void initListener() {
        mRadioGroupTask.setOnCheckedChangeListener(this);
    }

    @Override
    public void initData() {
        mFragmentManager = getActivity().getSupportFragmentManager();
        mTransaction = mFragmentManager.beginTransaction();
        if (null == hallTaskFragment) {
            hallTaskFragment = TaskContentFragment.newInstance(0);
            mTransaction.add(R.id.frame_layout_task, hallTaskFragment);
        }
        mTransaction.show(hallTaskFragment);
        mTransaction.commit();
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
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

}
