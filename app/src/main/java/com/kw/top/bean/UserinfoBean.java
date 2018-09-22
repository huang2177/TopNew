package com.kw.top.bean;

import android.text.TextUtils;

/**
 * author  ： zy
 * date    ： 2018/7/26
 * des     ：
 */

public class UserinfoBean {

    /**
     * password : TOPAccountPassword
     * headImg : aebb28fd2e03c36111964
     * nickName : Nice
     * grade : 15
     * account : topaccountnumber5
     */

    private String password;
    private String headImg;
    private String nickName;
    private String grade;
    private String account;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
        if (TextUtils.isEmpty(grade))
            return "0";
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }
}
