package com.kw.top.bean;

import java.util.List;

/**
 * Created by shibing on 2018/9/11.
 */

public class CityTagBean {


    String tag;
    private List<CityItemBean> mList;

    public CityTagBean(String tag, List<CityItemBean> list) {
        this.tag = tag;
        mList = list;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public List<CityItemBean> getList() {
        return mList;
    }

    public void setList(List<CityItemBean> list) {
        mList = list;
    }
}
