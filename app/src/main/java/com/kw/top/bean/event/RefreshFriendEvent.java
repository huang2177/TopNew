package com.kw.top.bean.event;

/**
 * author  ： zy
 * date    ： 2018/7/30
 * des     ：
 */

public class RefreshFriendEvent {

    private boolean refresh;

    public RefreshFriendEvent(boolean refresh){
        this.refresh = refresh;
    }

    public boolean isRefresh() {
        return refresh;
    }
}
