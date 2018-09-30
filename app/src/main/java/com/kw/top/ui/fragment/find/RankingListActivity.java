package com.kw.top.ui.fragment.find;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.androidkun.xtablayout.XTabLayout;
import com.kw.top.R;
import com.kw.top.adapter.ViewPagerAdapter;
import com.kw.top.base.BaseActivity_;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/9/30.
 */

public class RankingListActivity extends BaseActivity_ {


    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.ranking_tablayout)
    XTabLayout tabLayout;
    @BindView(R.id.ranking_viewpager)
    ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private List<String> tablist;
    private List<Fragment> fragmentList;
    private PopRankingFragmnet popRankingFragmnet;
    private VIPFragmnet vipFragmnet;

    @Override
    public int getContentView() {
        return R.layout.activity_ranking;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        ButterKnife.bind(this);
        initTab();
    }

    /**
     * tablayout
     */
    private void initTab() {
        tvTitle.setText("排行榜");
        vipFragmnet = new VIPFragmnet();
        popRankingFragmnet = new PopRankingFragmnet();
        fragmentList = new ArrayList<>();
        fragmentList.add(popRankingFragmnet);
        fragmentList.add(vipFragmnet);
        tablist = Arrays.asList("人气排行榜", "VIP排行榜");
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), fragmentList, tablist);
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }


    @OnClick(R.id.iv_back)
    public void OnClick() {
        finish();
    }
}
