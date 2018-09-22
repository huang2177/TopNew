package com.kw.top.bean.event;

import com.hyphenate.chat.EMMessage;

/**
 * author  ： zy
 * date    ： 2018/6/24
 * des     ：
 */

public class MessageEvent {

    private EMMessage mEMMessage;

    public MessageEvent(EMMessage message){
        this.mEMMessage = message;
    }

    public EMMessage getEMMessage() {
        return mEMMessage;
    }

    public void setEMMessage(EMMessage EMMessage) {
        mEMMessage = EMMessage;
    }
}
