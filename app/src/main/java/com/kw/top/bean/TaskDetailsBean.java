package com.kw.top.bean;

/**
 * author  ： zy
 * date    ： 2018/7/15
 * des     ：
 */

public class TaskDetailsBean {

    /**
     * userTaskId : 1
     * headImg : iOS_f756f2ee-e5c8-4bfe-8334-f3022338b6f47552_1530018495.jpg
     * createTime : 2018年06月12日 01:13
     * nickName : TTT
     * grade : 11
     * state : 1
     * urlAddress : 1.jpg
     * taskId : 1
     * describes : 露个脸
     */

    private int userTaskId;
    private String headImg;
    private String createTime;
    private String nickName;
    private int grade;
    private String state;
    private String urlAddress;
    private int taskId;
    private String describes;
    private String type; //01图片 02视频

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

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

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getUrlAddress() {
        return urlAddress;
    }

    public void setUrlAddress(String urlAddress) {
        this.urlAddress = urlAddress;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getDescribes() {
        return describes;
    }

    public void setDescribes(String describes) {
        this.describes = describes;
    }
}
