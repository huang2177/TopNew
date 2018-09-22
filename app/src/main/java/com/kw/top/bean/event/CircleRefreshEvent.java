package com.kw.top.bean.event;

/**
 * author  ： zy
 * date    ： 2018/7/5
 * des     ：
 */

public class CircleRefreshEvent {

    private boolean refresh;

    public CircleRefreshEvent(boolean refresh){
        this.refresh = refresh;
    }

    public boolean isRefresh() {
        return refresh;
    }

    public void setRefresh(boolean refresh) {
        this.refresh = refresh;
    }
}
