package com.kw.top.bean;

import java.io.Serializable;
import java.util.List;

/**
 * author: zy
 * data  : 2018/6/4
 * des   :
 */

public class TopCircleBean implements Serializable {


    /**
     * thumbsUpNum : 0
     * dynamicId : 23
     * headImg : 5.jpg
     * createTime : 2018年06月09日 23:25:22  06月09日
     * nickName : Nice
     * grade : 1
     * thumbsNum : 0
     * textContent : 嘎嘎嘎
     * userId : 5
     */

    private String thumbsUpNum;
    private String dynamicId;
    private String headImg;
    private String createTime;
    private String nickName;
    private String grade;
    private String thumbsNum;
    private String textContent;
    private String userId;
    private List<ImageBean> dynamicList;
    private String dynamicPic;

    public String getDynamicPic() {
        return dynamicPic;
    }

    public void setDynamicPic(String dynamicPic) {
        this.dynamicPic = dynamicPic;
    }

    public List<ImageBean> getDynamicList() {
        return dynamicList;
    }

    public void setDynamicList(List<ImageBean> dynamicList) {
        this.dynamicList = dynamicList;
    }

    public String getThumbsUpNum() {
        return thumbsUpNum;
    }

    public void setThumbsUpNum(String thumbsUpNum) {
        this.thumbsUpNum = thumbsUpNum;
    }

    public String getDynamicId() {
        return dynamicId;
    }

    public void setDynamicId(String dynamicId) {
        this.dynamicId = dynamicId;
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

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getThumbsNum() {
        return thumbsNum;
    }

    public void setThumbsNum(String thumbsNum) {
        this.thumbsNum = thumbsNum;
    }

    public String getTextContent() {
        return textContent;
    }

    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
