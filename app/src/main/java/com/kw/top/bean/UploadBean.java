package com.kw.top.bean;

/**
 * author  ： zy
 * date    ： 2018/6/23
 * des     ：
 */

public class UploadBean {

    private String path;
    private int percent;
    private int state; //1 成功 0失败

    public UploadBean(String path,int percen,int state){
        this.path = path;
        this.percent = percen;
        this.state = state;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getPercent() {
        return percent;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }
}
