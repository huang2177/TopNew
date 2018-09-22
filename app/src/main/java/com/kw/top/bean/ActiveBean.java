package com.kw.top.bean;

/**
 * author  ： zy
 * date    ： 2018/6/17
 * des     ：
 */

public class ActiveBean {

    /**
     * valid : 1
     * activityId : 1
     * createTime : 2018年06月07日
     * activityName : 2018最美
     * endTime : 2018年09月09日
     * picture : 1.jpg
     * describes : 选出你心中的女神
     * activityThumbsUpNum : 0
     */

    private String valid;
    private int activityId;
    private String createTime;
    private String activityName;
    private String endTime;
    private String picture;
    private String describes;
    private String activityThumbsUpNum;

    public String getValid() {
        return valid;
    }

    public void setValid(String valid) {
        this.valid = valid;
    }

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getDescribes() {
        return describes;
    }

    public void setDescribes(String describes) {
        this.describes = describes;
    }

    public String getActivityThumbsUpNum() {
        return activityThumbsUpNum;
    }

    public void setActivityThumbsUpNum(String activityThumbsUpNum) {
        this.activityThumbsUpNum = activityThumbsUpNum;
    }
}
