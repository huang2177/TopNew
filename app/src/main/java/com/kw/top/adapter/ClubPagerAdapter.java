package com.kw.top.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.luck.picture.lib.tools.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * author  ： zy
 * date    ： 2018/6/19
 * des     ：
 */

public class ClubPagerAdapter extends FragmentPagerAdapter {

    private Context mContext;
    private List<Fragment> mList = new ArrayList<>();

    public ClubPagerAdapter(FragmentManager fm, Constant constant,List<Fragment> list) {
        super(fm);
        this.mList = list;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        return super.instantiateItem(container, position);
    }

    @Override
    public Fragment getItem(int position) {
        return mList.get(position);
    }

    @Override
    public int getCount() {
        return mList.size();
    }
}
