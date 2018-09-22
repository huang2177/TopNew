package com.kw.top.ui.activity.club;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.kw.top.R;
import com.kw.top.base.BaseActivity;
import com.kw.top.ui.fragment.club.ClubContentFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by shibing on 2018/9/4.
 */

public class ClubActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {


    @BindView(R.id.rb_left)
    RadioButton mRbLeft;
    @BindView(R.id.rb_right)
    RadioButton mRbRight;
    @BindView(R.id.radio_group_two)
    RadioGroup mRadioGroupTwo;
    @BindView(R.id.frame_layout_two)
    FrameLayout mFrameLayoutTwo;
    @BindView(R.id.tv_title)
    TextView tv_title;

    private ClubContentFragment mMyJoinFragment, mAllClubFragment;
    private FragmentManager mFragmentManager;
    private FragmentTransaction mTransaction;

    @Override
    public int getContentView() {
        return R.layout.frament_club;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        ButterKnife.bind(this);
        initData();
    }


    public void initData() {
        tv_title.setText("俱乐部列表");
        mRadioGroupTwo.setOnCheckedChangeListener(this);
        mFragmentManager = getSupportFragmentManager();
        mTransaction = mFragmentManager.beginTransaction();
        if (null == mMyJoinFragment) {
            mMyJoinFragment = ClubContentFragment.newInstance(0);
            mTransaction.add(R.id.frame_layout_two, mMyJoinFragment);
        }
        mTransaction.show(mMyJoinFragment);
        mTransaction.commit();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        mTransaction = mFragmentManager.beginTransaction();
        switch (checkedId) {
            case R.id.rb_left:
                if (null != mAllClubFragment) {
                    mTransaction.hide(mAllClubFragment);
                }
                if (null == mMyJoinFragment) {
                    mMyJoinFragment = ClubContentFragment.newInstance(0);
                    mTransaction.add(R.id.frame_layout_two, mMyJoinFragment);
                }
                mTransaction.show(mMyJoinFragment);
                break;
            case R.id.rb_right:
                if (null != mMyJoinFragment) {
                    mTransaction.hide(mMyJoinFragment);
                }
                if (null == mAllClubFragment) {
                    mAllClubFragment = ClubContentFragment.newInstance(1);
                    mTransaction.add(R.id.frame_layout_two, mAllClubFragment);
                }
                mTransaction.show(mAllClubFragment);
                break;
        }
        mTransaction.commit();
    }


    @OnClick(R.id.iv_back)
    public void OnClick() {
        finish();
    }
}
