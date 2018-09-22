package com.kw.top.bean;

/**
 * author: zy
 * data  : 2018/6/10
 * des   :
 */

public class FriendApplyBean {

    /**
     * headImg : 1.jpg
     * nickName : TTT
     * giftPicture : 2.jpg
     * grade : 11
     * num : 1
     * friendsState : 0
     * relationId : 13
     * userId : 2
     * userGiftId : 21
     * cpNum : 0
     * friendsId : 1
     */

    private String headImg;
    private String nickName;
    private String giftPicture;
    private String grade;
    private String num;
    private String friendsState; //'0'.等待对方确认,'1'.好友关系,'2'.对方已拒绝,'3'.好友申请超时)
    private int relationId;
    private int userId;
    private int userGiftId;
    private String cpNum;
    private int friendsId;

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

    public String getFriendsState() {
        return friendsState;
    }

    public void setFriendsState(String friendsState) {
        this.friendsState = friendsState;
    }

    public int getRelationId() {
        return relationId;
    }

    public void setRelationId(int relationId) {
        this.relationId = relationId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserGiftId() {
        return userGiftId;
    }

    public void setUserGiftId(int userGiftId) {
        this.userGiftId = userGiftId;
    }

    public String getCpNum() {
        return cpNum;
    }

    public void setCpNum(String cpNum) {
        this.cpNum = cpNum;
    }

    public int getFriendsId() {
        return friendsId;
    }

    public void setFriendsId(int friendsId) {
        this.friendsId = friendsId;
    }
}
