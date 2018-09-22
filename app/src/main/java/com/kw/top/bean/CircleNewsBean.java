package com.kw.top.bean;

import java.util.List;

/**
 * author  ： zy
 * date    ： 2018/7/7
 * des     ：
 */

public class CircleNewsBean {
        private List<CommentListBean> commentList;

        public List<CommentListBean> getCommentList() {
            return commentList;
        }

        public void setCommentList(List<CommentListBean> commentList) {
            this.commentList = commentList;
        }

        public static class CommentListBean {
            /**
             * comHeadImg : c520c3385e1734953944
             * comNickName : Nice
             * comTime : 2018年07月07日 13:50
             * retUserid : 5
             * comUserid : 5
             * retNickName : Nice
             * comContent : 哈哈哈哈
             * id : 31
             * retHeadImg : c520c3385e1734953944
             */

            private String comHeadImg;
            private String comNickName;
            private String comTime;
            private String retUserid;
            private String comUserid;
            private String retNickName;
            private String comContent;
            private String id;
            private String retHeadImg;
//            private String dynamicPic;
            private String picOrVideoType; //0 图片 1视频
            private String dynamicPicdynamicPic;
            private String type;

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getDynamicPicdynamicPic() {
                return dynamicPicdynamicPic;
            }

            public void setDynamicPicdynamicPic(String dynamicPicdynamicPic) {
                this.dynamicPicdynamicPic = dynamicPicdynamicPic;
            }

//            public void setDynamicPic(String dynamicPic) {
//                this.dynamicPic = dynamicPic;
//            }

            public void setPicOrVideoType(String picOrVideoType) {
                this.picOrVideoType = picOrVideoType;
            }

//            public String getDynamicPic() {
//                return dynamicPic;
//            }

            public String getPicOrVideoType() {
                return picOrVideoType;
            }

            public String getComHeadImg() {
                return comHeadImg;
            }

            public void setComHeadImg(String comHeadImg) {
                this.comHeadImg = comHeadImg;
            }

            public String getComNickName() {
                return comNickName;
            }

            public void setComNickName(String comNickName) {
                this.comNickName = comNickName;
            }

            public String getComTime() {
                return comTime;
            }

            public void setComTime(String comTime) {
                this.comTime = comTime;
            }

            public String getRetUserid() {
                return retUserid;
            }

            public void setRetUserid(String retUserid) {
                this.retUserid = retUserid;
            }

            public String getComUserid() {
                return comUserid;
            }

            public void setComUserid(String comUserid) {
                this.comUserid = comUserid;
            }

            public String getRetNickName() {
                return retNickName;
            }

            public void setRetNickName(String retNickName) {
                this.retNickName = retNickName;
            }

            public String getComContent() {
                return comContent;
            }

            public void setComContent(String comContent) {
                this.comContent = comContent;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getRetHeadImg() {
                return retHeadImg;
            }

            public void setRetHeadImg(String retHeadImg) {
                this.retHeadImg = retHeadImg;
            }
        }
}
