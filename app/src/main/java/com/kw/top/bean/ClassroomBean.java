package com.kw.top.bean;

/**
 * author  ： zy
 * date    ： 2018/6/17
 * des     ：
 */

public class ClassroomBean {


    /**
     * createTime : 2018年06月08日 11:16
     * classroomName : 小葵花爸爸课题
     * classroomId : 2
     * classroomFile : 6.jpg
     * content : 这个课题只有男生才能看得到
     */

    private String createTime;
    private String classroomName;
    private int classroomId;
    private String classroomFile;
    private String content;
    private String homePic;

    public String getHomePic() {
        return homePic;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getClassroomName() {
        return classroomName;
    }

    public void setClassroomName(String classroomName) {
        this.classroomName = classroomName;
    }

    public int getClassroomId() {
        return classroomId;
    }

    public void setClassroomId(int classroomId) {
        this.classroomId = classroomId;
    }

    public String getClassroomFile() {
        return classroomFile;
    }

    public void setClassroomFile(String classroomFile) {
        this.classroomFile = classroomFile;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
