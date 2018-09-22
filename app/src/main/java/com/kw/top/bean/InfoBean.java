package com.kw.top.bean;

import java.util.List;

/**
 * author: zy
 * data  : 2018/6/9
 * des   :
 */

public class InfoBean {


    /**
     * msg :
     * code : 0000
     * data : [{"age":"21","birthday":"2018/06/09","constellation":"双子座","drink":"社交应酬","education":"研究生","headImg":"5.jpg","job":"投资人","nickName":"Nice","objective":"越炮，人称长宁钢炮","qualityLife":"高度奢侈","sex":"1","smoke":"偶尔一根","stature":"180","totalAssets":"1亿-5亿","userId":null,"yearIncome":"3000万-5000万"}]
     */

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * age : 21
         * birthday : 2018/06/09
         * constellation : 双子座
         * drink : 社交应酬
         * education : 研究生
         * headImg : 5.jpg
         * job : 投资人
         * nickName : Nice
         * objective : 越炮，人称长宁钢炮
         * qualityLife : 高度奢侈
         * sex : 1
         * smoke : 偶尔一根
         * stature : 180
         * totalAssets : 1亿-5亿
         * userId : null
         * yearIncome : 3000万-5000万
         */

        private String age;
        private String birthday;
        private String constellation;
        private String drink;
        private String education;
        private String headImg;
        private String job;
        private String nickName;
        private String objective;
        private String qualityLife;
        private String sex;
        private String smoke;
        private String stature;
        private String totalAssets;
        private Object userId;
        private String yearIncome;

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public String getConstellation() {
            return constellation;
        }

        public void setConstellation(String constellation) {
            this.constellation = constellation;
        }

        public String getDrink() {
            return drink;
        }

        public void setDrink(String drink) {
            this.drink = drink;
        }

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

        public String getJob() {
            return job;
        }

        public void setJob(String job) {
            this.job = job;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getObjective() {
            return objective;
        }

        public void setObjective(String objective) {
            this.objective = objective;
        }

        public String getQualityLife() {
            return qualityLife;
        }

        public void setQualityLife(String qualityLife) {
            this.qualityLife = qualityLife;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getSmoke() {
            return smoke;
        }

        public void setSmoke(String smoke) {
            this.smoke = smoke;
        }

        public String getStature() {
            return stature;
        }

        public void setStature(String stature) {
            this.stature = stature;
        }

        public String getTotalAssets() {
            return totalAssets;
        }

        public void setTotalAssets(String totalAssets) {
            this.totalAssets = totalAssets;
        }

        public Object getUserId() {
            return userId;
        }

        public void setUserId(Object userId) {
            this.userId = userId;
        }

        public String getYearIncome() {
            return yearIncome;
        }

        public void setYearIncome(String yearIncome) {
            this.yearIncome = yearIncome;
        }
    }
}
