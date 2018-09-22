package com.kw.top.bean;

import java.util.List;

/**
 * author  ： zy
 * date    ： 2018/6/17
 * des     ：
 */

public class ClassroomDetailsBean {


    /**
     * classroom : {"createTime":"2018年06月08日 11:17","classroomName":"帅哥","classroomId":4,"classroomFile":"9.jpg","content":"日常撩妹~"}
     */

    private ClassroomBean classroom;
    private List<CommentBean> commentList;

    public List<CommentBean> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<CommentBean> commentList) {
        this.commentList = commentList;
    }

    public ClassroomBean getClassroom() {
        return classroom;
    }

    public void setClassroom(ClassroomBean classroom) {
        this.classroom = classroom;
    }

}
