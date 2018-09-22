package com.kw.top.bean.event;

/**
 * author: zy
 * data  : 2018/6/10
 * des   :
 */

public class FindChooseEvent {

    private String sex;

    public FindChooseEvent(String sex){
        this.sex = sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getSex() {
        return sex;
    }
}
