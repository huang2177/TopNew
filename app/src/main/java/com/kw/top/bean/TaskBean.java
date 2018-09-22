package com.kw.top.bean;

/**
 * author  ： zy
 * date    ： 2018/6/12
 * des     ：
 */

public class TaskBean {

    /**
     * valid : 1
     * headImg : 3.jpg
     * nickName : 何
     * redAmount : 200
     * num : 12
     * grade : 9
     * type : 01
     * finishNum : 7
     * taskId : 1
     * describes : 露个脸
     */
    private String valid;
    private String headImg;
    private String nickName;
    private String redAmount;
    private String num;
    private int grade;
    private String type;
    private String finishNum;
    private int taskId;
    private String describes;
    private String userTaskId;

    public String getUserTaskId() {
        return userTaskId;
    }

    public void setUserTaskId(String userTaskId) {
        this.userTaskId = userTaskId;
    }

    public String getValid() {
        return valid;
    }

    public void setValid(String valid) {
        this.valid = valid;
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

    public String getRedAmount() {
        return redAmount;
    }

    public void setRedAmount(String redAmount) {
        this.redAmount = redAmount;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFinishNum() {
        return finishNum;
    }

    public void setFinishNum(String finishNum) {
        this.finishNum = finishNum;
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
