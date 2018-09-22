package com.kw.top.bean.event;

/**
 * author  ： zy
 * date    ： 2018/7/15
 * des     ：
 */

public class RefreshNewsEvent {

    private boolean newsMsg;

    public RefreshNewsEvent(boolean newsMsg){
        this.newsMsg = newsMsg;
    }

    public boolean isNewsMsg() {
        return newsMsg;
    }
}
