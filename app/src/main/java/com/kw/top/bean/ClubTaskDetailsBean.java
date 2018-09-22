package com.kw.top.bean;

import java.util.List;

/**
 * author  ： zy
 * date    ： 2018/8/11
 * des     ：
 */

public class ClubTaskDetailsBean {

        /**
         * userTaskId : 1
         * headImg : aebb28fd2e03c3611902
         * nickName : 沫宝
         * groupid : 56232062812161
         * type : 01
         * userId : 85
         * describes : 一个测试任务
         * releaseUserId : 85
         * createTime : 2018年08月09日 10:58
         * picList : [{"taskPic":"iOS_c5c391ba-0875-4198-8725-d8e599c747023066_1533783531.jpg"}]
         * videoList : []
         * redAmount : 200
         * grade : 1
         * taskId : 1
         */

        private int userTaskId;
        private String headImg;
        private String nickName;
        private String groupid;
        private String type;
        private int userId;
        private String describes;
        private int releaseUserId;
        private String createTime;
        private String redAmount;
        private String grade;
        private int taskId;
        private List<PicListBean> picList;
        private List<PicListBean> videoList;

        public int getUserTaskId() {
            return userTaskId;
        }

        public void setUserTaskId(int userTaskId) {
            this.userTaskId = userTaskId;
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

        public String getGroupid() {
            return groupid;
        }

        public void setGroupid(String groupid) {
            this.groupid = groupid;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getDescribes() {
            return describes;
        }

        public void setDescribes(String describes) {
            this.describes = describes;
        }

        public int getReleaseUserId() {
            return releaseUserId;
        }

        public void setReleaseUserId(int releaseUserId) {
            this.releaseUserId = releaseUserId;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getRedAmount() {
            return redAmount;
        }

        public void setRedAmount(String redAmount) {
            this.redAmount = redAmount;
        }

        public String getGrade() {
            return grade;
        }

        public void setGrade(String grade) {
            this.grade = grade;
        }

        public int getTaskId() {
            return taskId;
        }

        public void setTaskId(int taskId) {
            this.taskId = taskId;
        }

        public List<PicListBean> getPicList() {
            return picList;
        }

        public void setPicList(List<PicListBean> picList) {
            this.picList = picList;
        }

        public List<PicListBean> getVideoList() {
            return videoList;
        }

        public void setVideoList(List<PicListBean> videoList) {
            this.videoList = videoList;
        }

        public static class PicListBean {
            /**
             * taskPic : iOS_c5c391ba-0875-4198-8725-d8e599c747023066_1533783531.jpg
             */

            private String taskPic;
            private String taskVideo;

            public String getTaskVideo() {
                return taskVideo;
            }

            public void setTaskVideo(String taskVideo) {
                this.taskVideo = taskVideo;
            }

            public String getTaskPic() {
                return taskPic;
            }

            public void setTaskPic(String taskPic) {
                this.taskPic = taskPic;
            }
        }
}
