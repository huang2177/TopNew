package com.kw.top.bean;

import android.text.TextUtils;

import java.util.List;

/**
 * author  ： zy
 * date    ： 2018/6/16
 * des     ：
 */

public class SendGiftBean {

    /**
     * sendGiftAmountSum : 1600
     * sendGiftJewelList : [{"receiveTime":"06月26日","giftAmount":"800","headImg":"iOS_f756f2ee-e5c8-4bfe-8334-f3022338b6f47552_1530018495.jpg","nickName":"TTT","giftPicture":"3.jpg","grade":"11","num":"1"},{"receiveTime":"06月26日","giftAmount":"800","headImg":"headImg.png","nickName":"何","giftPicture":"3.jpg","grade":"9","num":"1"}]
     */

    private int sendGiftAmountSum;
    private List<ReceiveSendGiftBean> sendGiftJewelList;

    public int getSendGiftAmountSum() {
        return sendGiftAmountSum;
    }

    public List<ReceiveSendGiftBean> getSendGiftJewelList() {
        return sendGiftJewelList;
    }

    public void setSendGiftJewelList(List<ReceiveSendGiftBean> sendGiftJewelList) {
        this.sendGiftJewelList = sendGiftJewelList;
    }

    public void setSendGiftAmountSum(int sendGiftAmountSum) {
        this.sendGiftAmountSum = sendGiftAmountSum;
    }
}
