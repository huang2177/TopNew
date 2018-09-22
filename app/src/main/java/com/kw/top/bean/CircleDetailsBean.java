package com.kw.top.bean;

import java.util.List;

/**
 * author: zy
 * data  : 2018/6/10
 * des   :
 */

public class CircleDetailsBean {
    /**
     * commentList :
     * dynamicMap : {"thumbsUpNum":"0","headImg":"5.jpg","dynamicId":23,"createTime":"2018年06月09日 23:25","nickName":"Nice","grade":"1","thumbsNum":"0","textContent":"嘎嘎嘎","userId":5}
     */

    private List<CommentBean> commentList;
    private TopCircleBean dynamicMap;
    private List<ImageBean> picList;
    private List<ImageBean> videoList;

    public List<ImageBean> getVideoList() {
        return videoList;
    }

    public void setVideoList(List<ImageBean> videoList) {
        this.videoList = videoList;
    }

    public List<ImageBean> getPicList() {
        return picList;
    }

    public void setPicList(List<ImageBean> picList) {
        this.picList = picList;
    }

    public List<CommentBean> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<CommentBean> commentList) {
        this.commentList = commentList;
    }

    public TopCircleBean getDynamicMap() {
        return dynamicMap;
    }

    public void setDynamicMap(TopCircleBean dynamicMap) {
        this.dynamicMap = dynamicMap;
    }
}
