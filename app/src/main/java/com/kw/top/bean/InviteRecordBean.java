package com.kw.top.bean;

import android.text.TextUtils;

import java.io.Serializable;
import java.util.List;

/**
 * author: zy
 * data  : 2018/5/24
 * des   :
 */

public class InviteRecordBean implements Serializable {


    /**
     * InviteList : [{"headImg":"iOS_f756f2ee-e5c8-4bfe-8334-f3022338b6f47552_1530018495.jpg","createTime":"05月03日","nickName":"TTT","userProfit":"200","grade":"11"},{"headImg":"iOS_f756f2ee-e5c8-4bfe-8334-f3022338b6f47552_1530018495.jpg","createTime":"05月03日","nickName":"TTT","userProfit":"0.01","grade":"11"}]
     * user : {"headImg":"headImg.png","nickName":"何","grade":"9","userProfitSum":200}
     */

    private UserBean user;
    private List<InviteListBean> InviteList;

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public List<InviteListBean> getInviteList() {
        return InviteList;
    }

    public void setInviteList(List<InviteListBean> InviteList) {
        this.InviteList = InviteList;
    }

    public static class UserBean {
        /**
         * headImg : headImg.png
         * nickName : 何
         * grade : 9
         * userProfitSum : 200
         */

        private String headImg;
        private String nickName;
        private int grade;
        private String userProfitSum;

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

        public int getGrade() {
            return grade;
        }

        public void setGrade(int grade) {
            this.grade = grade;
        }

        public String getUserProfitSum() {
            if (TextUtils.isEmpty(userProfitSum))
                return "0";
            return userProfitSum;
        }

        public void setUserProfitSum(String userProfitSum) {
            this.userProfitSum = userProfitSum;
        }
    }

    public static class InviteListBean {
        /**
         * headImg : iOS_f756f2ee-e5c8-4bfe-8334-f3022338b6f47552_1530018495.jpg
         * createTime : 05月03日
         * nickName : TTT
         * userProfit : 200
         * grade : 11
         */

        private String headImg;
        private String createTime;
        private String nickName;
        private String userProfit;
        private int grade;

        public String getHeadImg() {
            return headImg;
        }

        public void setHeadImg(String headImg) {
            this.headImg = headImg;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getUserProfit() {
            if (TextUtils.isEmpty(userProfit))
                return "0";
            return userProfit;
        }

        public void setUserProfit(String userProfit) {
            this.userProfit = userProfit;
        }

        public int getGrade() {
            return grade;
        }

        public void setGrade(int grade) {
            this.grade = grade;
        }
    }
}
