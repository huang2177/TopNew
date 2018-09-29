package com.kw.top.ui.fragment.news;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kw.top.R;
import com.kw.top.base.BaseFragment;
import com.kw.top.bean.BaseBean;
import com.kw.top.bean.FriendApplyBean;
import com.kw.top.retrofit.Api;
import com.kw.top.ui.activity.news.FriendApplyActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * author: Administrator
 * data  : 2018/5/6
 * des   : 消息
 */

public class NewsFragment extends BaseFragment {
    @BindView(R.id.frame_layout_news)
    FrameLayout mFrameLayoutNews;
    @BindView(R.id.new_message)
    ImageView imageMessage;
    @BindView(R.id.new_message_lab)
    TextView tvMessagelab;
    @BindView(R.id.news_txl)
    ImageView imageTxl;
    @BindView(R.id.news_txl_lab)
    TextView tvTxllab;
    @BindView(R.id.news_hy_lay)
    RelativeLayout ray_hyqq;
    @BindView(R.id.info_mess_coun)
    TextView tv_num;
    @BindView(R.id.info_icon_coun)
    TextView tv_ic_num;


    private List<Fragment> fragments;
    private List<String> MessageState;

    public static NewsFragment fragment;
    private RecentSessionFragment mSessionFragment;

    public static NewsFragment newInstance() {
        if (fragment == null) {
            fragment = new NewsFragment();
        }
        return fragment;
    }

    @Override
    public int getContentView() {
        return R.layout.frament_news;
    }


    private void initFragment() {
        //默认的标签
        mSessionFragment =new  RecentSessionFragment();
        imageMessage.setImageResource(R.drawable.ic_news_message);
        tvMessagelab.setVisibility(View.VISIBLE);
        imageTxl.setImageResource(R.drawable.ic_news_txl_two);
        tvTxllab.setVisibility(View.GONE);
        ray_hyqq.setVisibility(View.GONE);
        showFragment(mSessionFragment);
    }


    @Override
    public void onResume() {
        super.onResume();
        getApplyList();
    }

    @Override
    public void initView(View view, Bundle savedInstanceState) {
        fragments = new ArrayList<>();
        MessageState = new ArrayList<>();
        initFragment();
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

    }

    @OnClick({R.id.message_lay, R.id.txl_ray, R.id.news_hy_lay})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.message_lay:
                imageMessage.setImageResource(R.drawable.ic_news_message);
                tvMessagelab.setVisibility(View.VISIBLE);
                ray_hyqq.setVisibility(View.GONE);
                imageTxl.setImageResource(R.drawable.ic_news_txl_two);
                tvTxllab.setVisibility(View.GONE);
                showFragment(mSessionFragment);
                break;
            case R.id.txl_ray:
                imageMessage.setImageResource(R.drawable.ic_news_message_two);
                tvMessagelab.setVisibility(View.GONE);
                ray_hyqq.setVisibility(View.VISIBLE);
                imageTxl.setImageResource(R.drawable.ic_news_txl);
                tvTxllab.setVisibility(View.VISIBLE);
                showFragment(FriendsListFragment.newInstance("1"));
                break;
            case R.id.news_hy_lay:
                startActivity(FriendApplyActivity.class);
                break;
        }
    }

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
            transaction.add(R.id.frame_layout_news, fragment);
        }
        hideFragment(transaction);
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


    /**
     * 获取好友请求数量
     */
    private void getApplyList() {
        Api.getApiService().applyFriendsList(getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Action1<BaseBean<List<FriendApplyBean>>>() {
                    @Override
                    public void call(BaseBean<List<FriendApplyBean>> baseBean) {
                        if (baseBean.isSuccess()) {
                            MessageState.clear();
                            for (int i = 0; i < baseBean.getData().size(); i++) {
                                if ("0".equals(baseBean.getData().get(i).getFriendsState())) {
                                    MessageState.add(baseBean.getData().get(i).getFriendsState());
                                }
                            }
                            if (MessageState.size() == 0) {
                                tv_num.setVisibility(View.GONE);
                                tv_ic_num.setVisibility(View.GONE);
                            } else {
                                tv_ic_num.setText(MessageState.size() + "");
                                tv_ic_num.setBackground(getResources().getDrawable(R.drawable.dot_vp_slt));
                                tv_num.setText(MessageState.size() + "");
                                tv_num.setBackground(getResources().getDrawable(R.drawable.dot_vp_slt));
                            }


                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                    }
                });
    }

}
