package com.kw.top.bean;

import android.text.TextUtils;

/**
 * author  ： zy
 * date    ： 2018/6/30
 * des     ：
 */

public class ReceiveSendGiftBean {

    /**
     * receiveTime : 06月26日
     * giftAmount : 800
     * headImg : iOS_f756f2ee-e5c8-4bfe-8334-f3022338b6f47552_1530018495.jpg
     * nickName : TTT
     * giftPicture : 3.jpg
     * grade : 11
     * num : 1
     */

    private String receiveTime;
    private String giftAmount;
    private String headImg;
    private String nickName;
    private String giftPicture;
    private String grade;
    private String num;

    public String getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(String receiveTime) {
        this.receiveTime = receiveTime;
    }

    public String getGiftAmount() {
        return giftAmount;
    }

    public void setGiftAmount(String giftAmount) {
        this.giftAmount = giftAmount;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getGiftPicture() {
        return giftPicture;
    }

    public void setGiftPicture(String giftPicture) {
        this.giftPicture = giftPicture;
    }

    public String getGrade() {
        if (TextUtils.isEmpty(grade)){
            return "0";
        }
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

}
