package com.kw.top.bean;

import android.text.TextUtils;

import java.io.Serializable;

/**
 * author: zy
 * data  : 2018/6/8
 * des   :
 */

public class AllUserBean implements Serializable {

    /**
     * headImg : 1.jpg
     * constellation : 双子座
     * city : 上海
     * nickName : TTT
     * sex : 1
     * grade : 11
     * dynamicPic : ["http://p9u9lweaa.bkt.clouddn.com/c520c3385e1734952079"]
     * userId : 1
     * age : 22
     * friends : 0
     */

    private String headImg;
    private String constellation;
    private String city;
    private String nickName;
    private String sex;
    private String grade;
    private String dynamicPic;
    private String userId;
    private String age;
    private String friends;// 0.不是好友   1.已经成为好友   2.已发出好友请求
    private String birthday;
    private String account;//环信账号
    private int applyId;
    private String loginTime;


    public void setAccount(String account) {
        this.account = account;
    }

    public String getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(String loginTime) {
        this.loginTime = loginTime;
    }

    private int apply_status = -1;//0同意 1拒绝

    public String getAccount() {
        return account;
    }

    public int getApply_status() {
        return apply_status;
    }

    public void setApply_status(int apply_status) {
        this.apply_status = apply_status;
    }

    public String getBirthday() {
        return birthday;
    }

    public int getApplyId() {
        return applyId;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public void setApplyId(int applyId) {
        this.applyId = applyId;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getConstellation() {
        return constellation;
    }

    public void setConstellation(String constellation) {
        this.constellation = constellation;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getGrade() {
        if (TextUtils.isEmpty(grade))
            return "0";
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getDynamicPic() {
        return dynamicPic;
    }

    public void setDynamicPic(String dynamicPic) {
        this.dynamicPic = dynamicPic;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAge() {
        if (TextUtils.isEmpty(age)) {
            return "18";
        }
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getFriends() {
        return friends;
    }

    public void setFriends(String friends) {
        this.friends = friends;
    }
}
