package com.kw.top.ui.fragment.club;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.kw.top.R;
import com.kw.top.adapter.ClubGridViewAdapter;
import com.kw.top.base.BaseFragment;
import com.kw.top.bean.AllUserBean;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;

/**
 * author  ： zy
 * date    ： 2018/6/25
 * des     ：
 */

public class ClubUserListFragment extends BaseFragment {
    @BindView(R.id.grid_view)
    GridView mGridView;

    private ClubGridViewAdapter mAdapter;

    public static ClubUserListFragment onNewInstance(List<AllUserBean> list){
        ClubUserListFragment fragment = new ClubUserListFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("LIST", (Serializable) list);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getContentView() {
        return R.layout.fragment_club_user;
    }

    @Override
    public void initView(View view, Bundle savedInstanceState) {
        List<AllUserBean> list = (List<AllUserBean>) getArguments().getSerializable("LIST");

        mAdapter = new ClubGridViewAdapter(getContext(),list);
        mGridView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void initListener() {
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

            }
        });
    }

    @Override
    public void initData() {

    }

}
