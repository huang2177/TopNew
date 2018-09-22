package com.kw.top.bean.event;

/**
 * author  ： zy
 * date    ： 2018/8/18
 * des     ：
 */

public class ClubVipEvent {

    private boolean refresh;

    public ClubVipEvent(boolean refresh){
        this.refresh = refresh;
    }

    public boolean isRefresh() {
        return refresh;
    }
}
