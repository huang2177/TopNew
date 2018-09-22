package com.kw.top.bean.event;

/**
 * author  ： zy
 * date    ： 2018/8/18
 * des     ：
 */

public class ExitClubEvent {

    private String groupId;

    public ExitClubEvent(String groupId){
        this.groupId = groupId;
    }

    public String getGroupId() {
        return groupId;
    }
}
