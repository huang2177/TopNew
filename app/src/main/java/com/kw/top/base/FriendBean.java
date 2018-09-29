package com.kw.top.base;

import java.io.Serializable;

/**
 * author: zy
 * data  : 2018/6/10
 * des   :
 */

public class FriendBean implements Serializable {

    /**
     * headImg : c520c3385e1734953944
     * nickName : Nice
     * grade : 15
     * myAccount : TOPAccountNumber28
     * friendsId : 5
     * friendAccount : TOPAccountNumber5
     */

    private String headImg;
    private String nickName;
    private String grade;
    private String myAccount;
    private String friendsId;
    private String friendAccount;

    public FriendBean(String headImg, String nickName, String friendAccount) {
        this.headImg = headImg;
        this.nickName = nickName;
        this.friendAccount = friendAccount;
    }

    public FriendBean(String headImg, String nickName, String grade, String myAccount, String friendsId, String friendAccount) {
        this.headImg = headImg;
        this.nickName = nickName;
        this.grade = grade;
        this.myAccount = myAccount;
        this.friendsId = friendsId;
        this.friendAccount = friendAccount;
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

    public String getMyAccount() {
        return myAccount;
    }

    public void setMyAccount(String myAccount) {
        this.myAccount = myAccount;
    }

    public String getFriendsId() {
        return friendsId;
    }

    public void setFriendsId(String friendsId) {
        this.friendsId = friendsId;
    }

    public String getFriendAccount() {
        return friendAccount;
    }

    public void setFriendAccount(String friendAccount) {
        this.friendAccount = friendAccount;
    }
}
