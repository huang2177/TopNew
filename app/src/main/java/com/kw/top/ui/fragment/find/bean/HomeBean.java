package com.kw.top.ui.fragment.find.bean;

/**
 * Created by shibing on 2018/9/24.
 */

public class HomeBean {


    /**
     * headImg : aebb28fd2e03c3618045
     * constellation : 金牛座
     * city : 上海市
     * nickName : Test
     * grade : 1
     * userId : 85
     * profit : 14
     * starNum : 5
     * age : 23
     */

    private String headImg;
    private String constellation;
    private String city;
    private String nickName;
    private String grade;
    private String userId;
    private String profit;
    private String starNum;
    private String age;
    private String objective;
    private String userState;
    private String job;

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public void setUserState(String userState) {
        this.userState = userState;
    }

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


    public String getObjective() {
        return objective;
    }

    public void setObjective(String objective) {
        this.objective = objective;
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

    public String getProfit() {
        return profit;
    }

    public void setProfit(String profit) {
        this.profit = profit;
    }

    public String getStarNum() {
        return starNum;
    }

    public void setStarNum(String starNum) {
        this.starNum = starNum;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}
