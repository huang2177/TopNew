package com.kw.top.bean;

import android.text.TextUtils;

/**
 * author: zy
 * data  : 2018/6/9
 * des   :
 */

public class TopListBean {

    /**
     * amount : 9000
     * headImg : 3.jpg
     * nickName : 何
     * grade : 9
     * userId : 2
     */

    private String amount;
    private String headImg;
    private String nickName;
    private String grade;
    private String userId;
    private String glamour;

    public String getGlamour() {
        if (TextUtils.isEmpty(glamour))
            return "0";
        glamour = (int)Double.parseDouble(glamour) +"";
        return glamour;
    }

    public String getAmount() {
        if (TextUtils.isEmpty(amount))
            return "0";
        amount = (int)Double.parseDouble(amount) +"";
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setGlamour(String glamour) {
        this.glamour = glamour;
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

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}
