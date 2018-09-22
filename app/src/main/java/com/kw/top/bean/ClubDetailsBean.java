package com.kw.top.bean;

import java.util.List;

/**
 * author  ： zy
 * date    ： 2018/6/19
 * des     ：
 */

public class ClubDetailsBean {

    private List<AllUserBean> member;
    private List<ChairmanBean> chairman;
    private String addState;

    public String getAddState() {
        return addState;
    }

    public void setAddState(String addState) {
        this.addState = addState;
    }

    public List<AllUserBean> getMember() {
        return member;
    }

    public void setMember(List<AllUserBean> member) {
        this.member = member;
    }

    public List<ChairmanBean> getChairman() {
        return chairman;
    }

    public void setChairman(List<ChairmanBean> chairman) {
        this.chairman = chairman;
    }

    public static class ChairmanBean {
        /**
         * headImg : headImg.png
         * constellation : 双子座
         * city : 上海
         * nickName : 何
         * clubName : 联盟
         * groupid : null
         * grade : 9
         * sendRedPacketSum : 0
         * age : 25
         * dayRedAmount : 21
         */

        private String headImg;
        private String constellation;
        private String city;
        private String nickName;
        private String clubName;
        private Object groupid;
        private String grade;
        private String sendRedPacketSum;
        private String age;
        private String dayRedAmount;
        private String clubNotice;
        private int userId;

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getClubNotice() {
            return clubNotice;
        }

        public void setClubNotice(String clubNotice) {
            this.clubNotice = clubNotice;
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

        public String getClubName() {
            return clubName;
        }

        public void setClubName(String clubName) {
            this.clubName = clubName;
        }

        public Object getGroupid() {
            return groupid;
        }

        public void setGroupid(Object groupid) {
            this.groupid = groupid;
        }

        public String getGrade() {
            return grade;
        }

        public void setGrade(String grade) {
            this.grade = grade;
        }

        public String getSendRedPacketSum() {
            return sendRedPacketSum;
        }

        public void setSendRedPacketSum(String sendRedPacketSum) {
            this.sendRedPacketSum = sendRedPacketSum;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public String getDayRedAmount() {
            return dayRedAmount;
        }

        public void setDayRedAmount(String dayRedAmount) {
            this.dayRedAmount = dayRedAmount;
        }
    }
}
