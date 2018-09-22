package com.kw.top.bean;

/**
 * author  ： zy
 * date    ： 2018/8/11
 * des     ：
 */

public class ClubListBean {

        /**
         * releaseUserId : 85
         * createTime : 1533781209000
         * groupid : 56232062812161
         * redAmount : 200
         * type : 01
         * taskId : 1
         * describes : 一个测试任务
         */

        private int releaseUserId;
        private long createTime;
        private String groupid;
        private String redAmount;
        private String type;
        private String taskId;
        private String describes;
        private String finish; //1 已完成

        public String getFinish() {
            return finish;
        }

    public int getReleaseUserId() {
            return releaseUserId;
        }

        public void setReleaseUserId(int releaseUserId) {
            this.releaseUserId = releaseUserId;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public String getGroupid() {
            return groupid;
        }

        public void setGroupid(String groupid) {
            this.groupid = groupid;
        }

        public String getRedAmount() {
            return redAmount;
        }

        public void setRedAmount(String redAmount) {
            this.redAmount = redAmount;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getTaskId() {
            return taskId;
        }

        public void setTaskId(String taskId) {
            this.taskId = taskId;
        }

        public String getDescribes() {
            return describes;
        }

        public void setDescribes(String describes) {
            this.describes = describes;
        }
}
