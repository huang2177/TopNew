package com.kw.top.ui.activity.active;

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
import com.kw.top.ui.fragment.list.ListFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by shibing on 2018/9/4.
 */

public class TopListActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {


    @BindView(R.id.rb_left)
    RadioButton mRbLeft;
    @BindView(R.id.rb_right)
    RadioButton mRbRight;
    @BindView(R.id.radio_group_top_list)
    RadioGroup mRadioGroupTopList;
    @BindView(R.id.frame_layout_top_list)
    FrameLayout mFrameLayoutTopList;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    private ListFragment charmFragment, powerFragment;
    private FragmentManager mFragmentManager;
    private FragmentTransaction mTransaction;


    @Override
    public int getContentView() {
        return R.layout.frament_top_list;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frament_top_list);
        ButterKnife.bind(this);
        tvTitle.setText("名人堂");
        initData();
    }


    @OnClick(R.id.iv_back)
    public void OnClick() {
        finish();
    }


    public void initData() {
        mRadioGroupTopList.setOnCheckedChangeListener(this);
        mFragmentManager = getSupportFragmentManager();
        mTransaction = mFragmentManager.beginTransaction();
        if (null == charmFragment) {
            charmFragment = ListFragment.newInstance("01");
            mTransaction.add(R.id.frame_layout_top_list, charmFragment);
        }
        mTransaction.show(charmFragment);
        mTransaction.commit();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
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
