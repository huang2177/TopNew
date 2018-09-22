package com.kw.top.bean.event;

/**
 * author  ： zy
 * date    ： 2018/6/30
 * des     ：
 */

public class CenterCouponEvent {

    private String diamon;//钻石
    private String coupon;//礼券
    private boolean refresh;
    private boolean refreshMyaccount;

    public CenterCouponEvent(boolean refresh,boolean refreshMyaccount) {
        this.refresh = refresh;
        this.refreshMyaccount = refreshMyaccount;
    }

    public boolean isRefresh() {
        return refresh;
    }

    public boolean isRefreshMyaccount() {
        return refreshMyaccount;
    }
}
