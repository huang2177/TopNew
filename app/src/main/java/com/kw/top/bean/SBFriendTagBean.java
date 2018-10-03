package com.kw.top.bean;

import java.util.List;

/**
 * author  ： zy
 * date    ： 2018/8/18
 * des     ：
 */

public class SBFriendTagBean {

    String tag;
    private List<FriendBean> mList;

    public SBFriendTagBean(String tag, List<FriendBean> list) {
        this.tag = tag;
        mList = list;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public List<FriendBean> getList() {
        return mList;
    }

    public void setList(List<FriendBean> list) {
        mList = list;
    }
}
