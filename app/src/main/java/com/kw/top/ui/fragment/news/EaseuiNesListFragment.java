package com.kw.top.ui.fragment.news;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.kw.top.R;
import com.kw.top.adapter.EaseChatAdapter;
import com.kw.top.base.BaseFragment;

import butterknife.BindView;


public class EaseuiNesListFragment extends BaseFragment {


    @BindView(R.id.ease_recyler)
    RecyclerView recyclerView;

    private EaseChatAdapter adapter;


    public static EaseuiNesListFragment fragment;

    public static EaseuiNesListFragment newInstance() {
        if (fragment == null) {
            fragment = new EaseuiNesListFragment();
        }
        return fragment;
    }


    @Override
    public int getContentView() {
        return R.layout.fragment_ease;
    }

    @Override
    public void initView(View view, Bundle savedInstanceState) {
        initRecyler();
    }


    private void initRecyler() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setNestedScrollingEnabled(false);
        adapter = new EaseChatAdapter(getActivity());
        recyclerView.setAdapter(adapter);
    }


    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

    }
}
