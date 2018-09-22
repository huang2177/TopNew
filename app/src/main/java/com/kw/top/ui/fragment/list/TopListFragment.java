package com.kw.top.ui.fragment.list;

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
 * des   : TOP榜
 */

public class TopListFragment extends BaseFragment implements RadioGroup.OnCheckedChangeListener,
        View.OnClickListener {


    @BindView(R.id.rb_left)
    RadioButton mRbLeft;
    @BindView(R.id.rb_right)
    RadioButton mRbRight;
    @BindView(R.id.radio_group_top_list)
    RadioGroup mRadioGroupTopList;
    @BindView(R.id.frame_layout_top_list)
    FrameLayout mFrameLayoutTopList;
    private ListFragment charmFragment, powerFragment;
    private FragmentManager mFragmentManager;
    private FragmentTransaction mTransaction;

    public static TopListFragment newInstance() {
        TopListFragment fragment = new TopListFragment();
        return fragment;
    }

    @Override
    public int getContentView() {
        return R.layout.frament_top_list;
    }

    @Override
    public void initView(View view, Bundle savedInstanceState) {

    }

    @Override
    public void initListener() {
        mRadioGroupTopList.setOnCheckedChangeListener(this);
    }

    @Override
    public void initData() {
        mFragmentManager = getActivity().getSupportFragmentManager();
        mTransaction = mFragmentManager.beginTransaction();
        if (null == charmFragment) {
            charmFragment = ListFragment.newInstance("01");
            mTransaction.add(R.id.frame_layout_top_list, charmFragment);
        }
        mTransaction.show(charmFragment);
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
                if (null != powerFragment) {
                    mTransaction.hide(powerFragment);
                }
                if (null == charmFragment) {
                    charmFragment = ListFragment.newInstance("01");
                    mTransaction.add(R.id.frame_layout_top_list, charmFragment);
                }
                mTransaction.show(charmFragment);
                break;
            case R.id.rb_right:
                if (null != charmFragment) {
                    mTransaction.hide(charmFragment);
                }
                if (null == powerFragment) {
                    powerFragment = ListFragment.newInstance("02");
                    mTransaction.add(R.id.frame_layout_top_list, powerFragment);
                }
                mTransaction.show(powerFragment);
                break;
        }
        mTransaction.commit();
    }
}
