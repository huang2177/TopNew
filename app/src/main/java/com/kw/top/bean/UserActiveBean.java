package com.kw.top.bean;

import java.util.List;

/**
 * author  ： zy
 * date    ： 2018/7/28
 * des     ：
 */

public class UserActiveBean {


    /**
     * activityRelation : {"thumbsUpNum":"0","activityId":"4","headImg":"aebb28fd2e03c3612127","nickName":"哦哦","sex":"1","grade":"0","activityPic":[{"activityPic":"aebb28fd2e03c3611963"},{"activityPic":"aebb28fd2e03c3611956"}],"id":6,"userId":28,"picOrVideoType":"0"}
     */

    private ActivityRelationBean activityRelation;

    public ActivityRelationBean getActivityRelation() {
        return activityRelation;
    }

    public void setActivityRelation(ActivityRelationBean activityRelation) {
        this.activityRelation = activityRelation;
    }

    public static class ActivityRelationBean {
        /**
         * thumbsUpNum : 0
         * activityId : 4
         * headImg : aebb28fd2e03c3612127
         * nickName : 哦哦
         * sex : 1
         * grade : 0
         * activityPic : [{"activityPic":"aebb28fd2e03c3611963"},{"activityPic":"aebb28fd2e03c3611956"}]
         * id : 6
         * userId : 28
         * picOrVideoType : 0
         */

        private String thumbsUpNum;
        private String activityId;
        private String headImg;
        private String nickName;
        private String sex;
        private int grade;
        private int id;
        private int userId;
        private String picOrVideoType;
//        private List<ActivityPicBean> activityPic;
        private List<ActivityPicBean> activityPicList;

        public List<ActivityPicBean> getActivityPicList() {
            return activityPicList;
        }

        public void setActivityPicList(List<ActivityPicBean> activityPicList) {
            this.activityPicList = activityPicList;
        }

        public String getThumbsUpNum() {
            return thumbsUpNum;
        }

        public void setThumbsUpNum(String thumbsUpNum) {
            this.thumbsUpNum = thumbsUpNum;
        }

        public String getActivityId() {
            return activityId;
        }

        public void setActivityId(String activityId) {
            this.activityId = activityId;
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

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public int getGrade() {
            return grade;
        }

        public void setGrade(int grade) {
            this.grade = grade;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getPicOrVideoType() {
            return picOrVideoType;
        }

        public void setPicOrVideoType(String picOrVideoType) {
            this.picOrVideoType = picOrVideoType;
        }

//        public List<ActivityPicBean> getActivityPic() {
//            return activityPic;
//        }
//
//        public void setActivityPic(List<ActivityPicBean> activityPic) {
//            this.activityPic = activityPic;
//        }

        public static class ActivityPicBean {
            /**
             * activityPic : aebb28fd2e03c3611963
             */

            private String activityPic;

            public String getActivityPic() {
                return activityPic;
            }

            public void setActivityPic(String activityPic) {
                this.activityPic = activityPic;
            }
        }
    }
}
