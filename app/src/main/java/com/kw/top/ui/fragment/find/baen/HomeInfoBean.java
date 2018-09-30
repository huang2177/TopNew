package com.kw.top.ui.fragment.find.baen;

import java.util.List;

/**
 * Created by shibing on 2018/9/25.
 */

public class HomeInfoBean {


    private FollowNumBean followNum;
    private UserInfoMapBean userInfoMap;
    private String follow;
    private String friends;
    private String account;

    public void setAccount(String account) {
        this.account = account;
    }

    public String getAccount() {
        return account.contains(".") ? account.substring(0, account.indexOf(".")) : account;
    }

    public String getFriends() {
        return friends;
    }

    public void setFriends(String friends) {
        this.friends = friends;
    }

    private List<GiftListBean> giftList;
    private List<PhotoalbumListBean> photoalbumList;
    private List<PictureListBean> pictureList;

    public FollowNumBean getFollowNum() {
        return followNum;
    }

    public void setFollowNum(FollowNumBean followNum) {
        this.followNum = followNum;
    }

    public UserInfoMapBean getUserInfoMap() {
        return userInfoMap;
    }

    public void setUserInfoMap(UserInfoMapBean userInfoMap) {
        this.userInfoMap = userInfoMap;
    }

    public String getFollow() {
        return follow;
    }

    public void setFollow(String follow) {
        this.follow = follow;
    }

    public List<GiftListBean> getGiftList() {
        return giftList;
    }

    public void setGiftList(List<GiftListBean> giftList) {
        this.giftList = giftList;
    }

    public List<PhotoalbumListBean> getPhotoalbumList() {
        return photoalbumList;
    }

    public void setPhotoalbumList(List<PhotoalbumListBean> photoalbumList) {
        this.photoalbumList = photoalbumList;
    }

    public List<PictureListBean> getPictureList() {
        return pictureList;
    }

    public void setPictureList(List<PictureListBean> pictureList) {
        this.pictureList = pictureList;
    }

    public static class FollowNumBean {
        /**
         * followNum : 0
         */

        private int followNum;

        public int getFollowNum() {
            return followNum;
        }

        public void setFollowNum(int followNum) {
            this.followNum = followNum;
        }
    }

    public static class UserInfoMapBean {
        /**
         * birthday : 1997-07-31
         * headImg : dd5bfab10189f94e7330
         * totalAssets : 100万-500万
         * qualityLife : 轻微奢侈
         * nickName : 薛薛薛宝
         * smoke : 偶尔一根
         * userId : 96
         * drink : 小酌一杯
         * objective : 交盆友
         * constellation : 狮子座
         * grade : 1
         * stature : 168
         * yearIncome : 100万以下
         * job : 模特
         * profit : 10
         * age : 21
         */

        private String birthday;
        private String headImg;
        private String totalAssets;
        private String qualityLife;
        private String nickName;
        private String smoke;
        private String userId;
        private String drink;
        private String objective;
        private String constellation;
        private String grade;
        private String stature;
        private String yearIncome;
        private String job;
        private String profit;
        private String age;

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public String getHeadImg() {
            return headImg;
        }

        public void setHeadImg(String headImg) {
            this.headImg = headImg;
        }

        public String getTotalAssets() {
            return totalAssets;
        }

        public void setTotalAssets(String totalAssets) {
            this.totalAssets = totalAssets;
        }

        public String getQualityLife() {
            return qualityLife;
        }

        public void setQualityLife(String qualityLife) {
            this.qualityLife = qualityLife;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getSmoke() {
            return smoke;
        }

        public void setSmoke(String smoke) {
            this.smoke = smoke;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getDrink() {
            return drink;
        }

        public void setDrink(String drink) {
            this.drink = drink;
        }

        public String getObjective() {
            return objective;
        }

        public void setObjective(String objective) {
            this.objective = objective;
        }

        public String getConstellation() {
            return constellation;
        }

        public void setConstellation(String constellation) {
            this.constellation = constellation;
        }

        public String getGrade() {
            return grade;
        }

        public void setGrade(String grade) {
            this.grade = grade;
        }

        public String getStature() {
            return stature;
        }

        public void setStature(String stature) {
            this.stature = stature;
        }

        public String getYearIncome() {
            return yearIncome;
        }

        public void setYearIncome(String yearIncome) {
            this.yearIncome = yearIncome;
        }

        public String getJob() {
            return job;
        }

        public void setJob(String job) {
            this.job = job;
        }

        public String getProfit() {
            return profit;
        }

        public void setProfit(String profit) {
            this.profit = profit;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }
    }

    public static class GiftListBean {
        /**
         * giftAmount : 520
         * headImg : dd5bfab10189f94e44476
         */

        private int giftAmount;
        private String headImg;

        public int getGiftAmount() {
            return giftAmount;
        }

        public void setGiftAmount(int giftAmount) {
            this.giftAmount = giftAmount;
        }

        public String getHeadImg() {
            return headImg;
        }

        public void setHeadImg(String headImg) {
            this.headImg = headImg;
        }
    }

    public static class PhotoalbumListBean {
        /**
         * picture : ["dd5bfab10189f94e2182"
         */

        private String picture;

        public String getPicture() {
            return picture;
        }

        public void setPicture(String picture) {
            this.picture = picture;
        }
    }

    public static class PictureListBean {
        /**
         * dynamicPic : dd5bfab10189f94e20748
         */

        private String dynamicPic;

        public String getDynamicPic() {
            return dynamicPic;
        }

        public void setDynamicPic(String dynamicPic) {
            this.dynamicPic = dynamicPic;
        }
    }
}
