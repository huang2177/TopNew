package com.kw.top.bean;

import android.text.TextUtils;

import java.util.List;

/**
 * author: zy
 * data  : 2018/6/10
 * des   :
 */

public class FindDetailsBean {

    /**
     * userDesc : {"education":"University","headImg":"1.jpg","totalAssets":"500万-1000万","city":"上海","qualityLife":"浪漫情调","nickName":"TTT","smoke":"从不吸烟","userId":1,"drink":"滴酒不沾","objective":"Love and Pice","constellation":"双子座","grade":"11","stature":"182","yearIncome":"100万以下","job":"CTO","backgroundPic":"1.jpg","age":"22"}
     * dynamicList : [{"thumbsUpNum":"0","dynamicId":1,"createTime":"05月09日","dynamicPic":"1.jpg","thumbsNum":"0","textContent":"1234","userId":1},{"thumbsUpNum":"0","dynamicId":5,"createTime":"05月15日","dynamicPic":"1.jpg","thumbsNum":"0","textContent":"伽利略","userId":1},{"thumbsUpNum":"0","dynamicId":6,"createTime":"05月15日","dynamicPic":"1.jpg","thumbsNum":"0","textContent":"伽利略","userId":1},{"thumbsUpNum":"0","dynamicId":7,"createTime":"05月15日","dynamicPic":"1.jpg","thumbsNum":"0","textContent":"伽利略","userId":1},{"thumbsUpNum":"0","dynamicId":8,"createTime":"05月15日","dynamicPic":"1.jpg","thumbsNum":"0","textContent":"伽利略","userId":1},{"thumbsUpNum":"0","dynamicId":9,"createTime":"05月15日","dynamicPic":"1.jpg","thumbsNum":"0","textContent":"伽利略","userId":1},{"thumbsUpNum":"0","dynamicId":10,"createTime":"05月15日","dynamicPic":"1.jpg","thumbsNum":"0","textContent":"伽利略","userId":1},{"thumbsUpNum":"0","dynamicId":11,"createTime":"05月15日","dynamicPic":"1.jpg","thumbsNum":"0","textContent":"伽利略","userId":1},{"thumbsUpNum":"0","dynamicId":12,"createTime":"05月15日","dynamicPic":"1.jpg","thumbsNum":"0","textContent":"伽利略","userId":1},{"thumbsUpNum":"0","dynamicId":13,"createTime":"05月15日","dynamicPic":"1.jpg","thumbsNum":"0","textContent":"伽利略","userId":1},{"thumbsUpNum":"0","dynamicId":14,"createTime":"05月15日","dynamicPic":"1.jpg","thumbsNum":"0","textContent":"伽利略","userId":1},{"thumbsUpNum":"0","dynamicId":17,"createTime":"06月03日","dynamicPic":"1.jpg","thumbsNum":"0","textContent":"vvv","userId":1},{"thumbsUpNum":"0","dynamicId":20,"createTime":"06月05日","dynamicPic":"iOS_7cb4a649-a1f7-41ba-b751-0bfec25ca1a92358_1528185895.jpg","thumbsNum":"0","textContent":"Love & Pice","userId":1},{"thumbsUpNum":"0","dynamicId":21,"createTime":"06月05日","dynamicPic":"iOS_7cb4a649-a1f7-41ba-b751-0bfec25ca1a92358_1528186037.jpg","thumbsNum":"0","textContent":"Hi","userId":1}]
     */

    private UserDescBean userDesc;
    private List<TopCircleBean> dynamicList;
    private String friends;
    private String account;

    public String getAccount() {
        return account;
    }

    public String getFriends() {
        return friends;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public void setFriends(String friends) {
        this.friends = friends;
    }

    public UserDescBean getUserDesc() {
        return userDesc;
    }

    public void setUserDesc(UserDescBean userDesc) {
        this.userDesc = userDesc;
    }

    public List<TopCircleBean> getDynamicList() {
        return dynamicList;
    }

    public void setDynamicList(List<TopCircleBean> dynamicList) {
        this.dynamicList = dynamicList;
    }

    public static class UserDescBean {
        /**
         * education : University
         * headImg : 1.jpg
         * totalAssets : 500万-1000万
         * city : 上海
         * qualityLife : 浪漫情调
         * nickName : TTT
         * smoke : 从不吸烟
         * userId : 1
         * drink : 滴酒不沾
         * objective : Love and Pice
         * constellation : 双子座
         * grade : 11
         * stature : 182
         * yearIncome : 100万以下
         * job : CTO
         * backgroundPic : 1.jpg
         * age : 22
         */

        private String education;
        private String headImg;
        private String totalAssets;
        private String city;
        private String qualityLife;
        private String nickName;
        private String smoke;
        private String userId;
        private String drink;
        private String objective;
        private String constellation;
        private int grade;
        private String stature;
        private String yearIncome;
        private String job;
        private String backgroundPic;
        private String age;

        public String getEducation() {
            return education;
        }

        public void setEducation(String education) {
            this.education = education;
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

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
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

        public int getGrade() {
            return grade;
        }

        public void setGrade(int grade) {
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

        public String getBackgroundPic() {
            return backgroundPic;
        }

        public void setBackgroundPic(String backgroundPic) {
            this.backgroundPic = backgroundPic;
        }

        public String getAge() {
            if (TextUtils.isEmpty(age))
                return "18";
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }
    }
}
