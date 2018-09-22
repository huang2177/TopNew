package com.kw.top.bean;

import java.io.Serializable;

/**
 * author  ： zy
 * date    ： 2018/6/19
 * des     ：
 */

public class ClubBean implements Serializable{

    /**
     * headImg : c520c3385e1734953944
     * clubName : 越炮联盟
     * sendRedPacketSum : 0
     * clubId : 3
     * peopleNum : 0
     * dayRedAmount : 500
     * groupid:
     */

    private String headImg;
    private String clubName;
    private String sendRedPacketSum;
    private int clubId;
    private int peopleNum;
    private String dayRedAmount;
    private String groupid;

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getClubName() {
        return clubName;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    public String getSendRedPacketSum() {
        return sendRedPacketSum;
    }

    public void setSendRedPacketSum(String sendRedPacketSum) {
        this.sendRedPacketSum = sendRedPacketSum;
    }

    public int getClubId() {
        return clubId;
    }

    public void setClubId(int clubId) {
        this.clubId = clubId;
    }

    public int getPeopleNum() {
        return peopleNum;
    }

    public void setPeopleNum(int peopleNum) {
        this.peopleNum = peopleNum;
    }

    public String getDayRedAmount() {
        return dayRedAmount;
    }

    public void setDayRedAmount(String dayRedAmount) {
        this.dayRedAmount = dayRedAmount;
    }
}
