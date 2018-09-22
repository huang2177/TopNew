package com.kw.top.bean.event;

/**
 * author  ： zy
 * date    ： 2018/7/23
 * des     ：
 */

public class MsgCountEvent {

    private boolean msg;

    public MsgCountEvent(boolean msg) {
        this.msg = msg;
    }

    public boolean isMsg() {
        return msg;
    }

    public void setMsg(boolean msg) {
        this.msg = msg;
    }
}
