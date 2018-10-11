package com.kw.top.bean;

/**
 * author  ： zy
 * date    ： 2018/6/27
 * des     ：
 */

public class PersonCenterBean {

    /**
     * headImg : c520c3385e1734953944
     * nickName : Nice
     * grade : 15
     * jewelSum : 494800.00
     * couponsSum : 500000.00
     */

    private String headImg;
    private String nickName;
    private String grade;
    private String jewelSum;
    private double couponsSum;
    private int fansSum;
    private String profit;
    private int followSum;
    private String sex;
    private String userState;

    //1.在线,3.活跃,5.在聊,7.勿扰,9.离线
    public String getUserState() {
        switch (userState) {
            case "1":
                return "在线";
            case "3":
                return "活跃";
            case "5":
                return "在聊";
            case "7":
                return "勿扰";
            case "9":
                return "离线";
        }
        return "在线";
    }

    public void setUserState(String userState) {
        this.userState = userState;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getFansSum() {
        return fansSum;
    }

    public void setFansSum(int fansSum) {
        this.fansSum = fansSum;
    }

    public int getFollowSum() {
        return followSum;
    }

    public void setFollowSum(int followSum) {
        this.followSum = followSum;
    }

    public String getProfit() {
        return profit;
    }

    public void setProfit(String profit) {
        this.profit = profit;
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

    public String getJewelSum() {
        return jewelSum;
    }

    public void setJewelSum(String jewelSum) {
        this.jewelSum = jewelSum;
    }

    public double getCouponsSum() {
        return couponsSum;
    }

    public void setCouponsSum(double couponsSum) {
        this.couponsSum = couponsSum;
    }
}
