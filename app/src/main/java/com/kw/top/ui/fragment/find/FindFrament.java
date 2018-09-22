package com.kw.top.ui.fragment.find;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.kw.top.R;
import com.kw.top.base.BaseFragment;
import com.kw.top.bean.event.FindChooseEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * author: Administrator
 * data  : 2018/5/6
 * des   : 发现
 */

public class FindFrament extends BaseFragment {
    @BindView(R.id.rb_three_left)
    RadioButton mRbNear;
    @BindView(R.id.rb_three_center)
    RadioButton mRbActive;
    @BindView(R.id.rb_three_right)
    RadioButton mRbNew;
    @BindView(R.id.radio_group_three)
    RadioGroup mRadioGroup;
    @BindView(R.id.frame_layout_three)
    FrameLayout mFrameLayoutFind;
    @BindView(R.id.home_text1)
    TextView tvTuijian;
    @BindView(R.id.home_tj_lab)
    TextView tvTuijianlab;
    @BindView(R.id.home_city_text)
    TextView tvCity;
    @BindView(R.id.home_city_lab)
    TextView tvCitylab;
    private int type = 0;// 0离我最近 1最近活跃 2最新加入
    private Dialog mFindDialog;
    private List<Fragment> fragments;
    public static FindFrament findFrament;
    private FragmentTransaction transaction;


    public static FindFrament newInstance() {
        if (findFrament == null) {
            findFrament = new FindFrament();
        }
        return findFrament;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void initFragment() {
        //默认的tab
        tvTuijian.setText("推荐");
        tvTuijian.setTextColor(getResources().getColor(R.color.white));
        tvTuijianlab.setVisibility(View.VISIBLE);
        tvCity.setText("城市选择");
        tvCity.setTextColor(getResources().getColor(R.color.light_black));
        tvCitylab.setVisibility(View.GONE);
        showFragment(FindContentFragment.newInstant(type));
    }

    @Override
    public int getContentView() {
        return R.layout.frament_find;
    }

    @Override
    public void initView(View view, Bundle savedInstanceState) {
        fragments = new ArrayList<>();
        initFragment();
    }

    @Override
    public void initListener() {
    }


    @Override
    public void initData() {

    }


    @OnClick({R.id.home_tuijian, R.id.home_city, R.id.image_choose})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.home_tuijian:
                type = 0;
                //默认的tab
                tvTuijian.setText("推荐");
                tvTuijian.setTextColor(getResources().getColor(R.color.white));
                tvTuijianlab.setVisibility(View.VISIBLE);
                tvCity.setText("城市选择");
                tvCity.setTextColor(getResources().getColor(R.color.light_black));
                tvCitylab.setVisibility(View.GONE);
                showFragment(FindContentFragment.newInstant(type));
                break;
            case R.id.home_city:
                //默认的tab
                tvTuijian.setText("推荐");
                tvTuijian.setTextColor(getResources().getColor(R.color.light_black));
                tvTuijianlab.setVisibility(View.GONE);
                tvCity.setText("城市选择");
                tvCity.setTextColor(getResources().getColor(R.color.white));
                tvCitylab.setVisibility(View.VISIBLE);
                showFragment(FindCityFragment.newInstant());
                // showDiolog();

                break;

            case R.id.image_choose:
                showChooseFindDialog();
                break;
        }
    }


    /**
     * 这个版本暂时 这个功能暂时不做
     */
    private void showDiolog() {
        new AlertDialog.Builder(getActivity())
                .setMessage("敬请期待")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        initFragment();
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        initFragment();
                        dialog.dismiss();
                    }
                }).show();
    }


    //筛选Dialog

    /**
     * 发现筛选弹框
     */
    private void showChooseFindDialog() {
        View view = View.inflate(getActivity(), R.layout.dialog_choose_find, null);
        view.findViewById(R.id.btn_select_man).setOnClickListener(findDialogOnclickListener);
        view.findViewById(R.id.btn_select_gril).setOnClickListener(findDialogOnclickListener);
        view.findViewById(R.id.btn_select_all).setOnClickListener(findDialogOnclickListener);
        view.findViewById(R.id.btn_cancel).setOnClickListener(findDialogOnclickListener);
        mFindDialog = new Dialog(getActivity(), R.style.charge_dialog_style);
        mFindDialog.setContentView(view);
        mFindDialog.setCanceledOnTouchOutside(true);
        Window window = mFindDialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.BOTTOM); // 此处可以设置dialog显示的位置
        window.setWindowAnimations(R.style.popupAnimation); // 添加动画
        mFindDialog.show();
    }

    private View.OnClickListener findDialogOnclickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn_select_man:
                    EventBus.getDefault().post(new FindChooseEvent("1"));
                    break;
                case R.id.btn_select_gril:
                    EventBus.getDefault().post(new FindChooseEvent("0"));
                    break;
                case R.id.btn_select_all:
                    EventBus.getDefault().post(new FindChooseEvent(""));
                    break;
                case R.id.btn_cancel:
                    break;
            }
            mFindDialog.dismiss();
        }
    };


    /**
     * 显示Fragment
     */
    private void showFragment(Fragment fragment) {
        if (fragment == null) {
            return;
        }
        if (!fragments.contains(fragment)) {
            fragments.add(fragment);
        }
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();

        if (!fragment.isAdded()) {
            transaction.add(R.id.frame_layout_three, fragment);
        }
        hideFragment(transaction);
        // hideFragment();
        transaction.show(fragment);
        transaction.commit();
    }


    /**
     * 隐藏所有的fragment
     */
    private void hideFragment(FragmentTransaction transaction) {
        for (int i = 0; i < fragments.size(); i++) {
            if (fragments.get(i) != null) {
                transaction.hide(fragments.get(i));
            }
        }
    }


}
