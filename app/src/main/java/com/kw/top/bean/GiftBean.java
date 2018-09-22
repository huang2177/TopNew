package com.kw.top.bean;

/**
 * author: zy
 * data  : 2018/6/10
 * des   :
 */

public class GiftBean {
    /**
     * giftAmount : 200
     * amountType : 0
     * giftId : 1
     * giftName : 黄瓜
     * giftPicture : 1.jpg
     * cash : 0
     */

    private String giftAmount;
    private String amountType; //0 礼券 1钻石
    private int giftId;
    private String giftName;
    private String giftPicture;
    private String cash;

    public String getGiftAmount() {
        return giftAmount;
    }

    public void setGiftAmount(String giftAmount) {
        this.giftAmount = giftAmount;
    }

    public String getAmountType() {
        return amountType;
    }

    public void setAmountType(String amountType) {
        this.amountType = amountType;
    }

    public int getGiftId() {
        return giftId;
    }

    public void setGiftId(int giftId) {
        this.giftId = giftId;
    }

    public String getGiftName() {
        return giftName;
    }

    public void setGiftName(String giftName) {
        this.giftName = giftName;
    }

    public String getGiftPicture() {
        return giftPicture;
    }

    public void setGiftPicture(String giftPicture) {
        this.giftPicture = giftPicture;
    }

    public String getCash() {
        return cash;
    }

    public void setCash(String cash) {
        this.cash = cash;
    }
}
