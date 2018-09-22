package com.kw.top.bean;

import java.util.List;

/**
 * author  ： zy
 * date    ： 2018/6/17
 * des     ：
 */

public class ActiveDetailsBean {


    /**
     * activityDes : {"valid":"1","activityId":1,"createTime":"2018年06月07日","activityName":"2018最美","endTime":"2018年09月09日","picture":"1.jpg","describes":"选出你心中的女神","activityThumbsUpNum":"0"}
     * activityUserList : [{"thumbsUpNum":"2","headImg":"1.jpg","userId":1},{"thumbsUpNum":"0","headImg":"3.jpg","userId":3},{"thumbsUpNum":"0","headImg":"3.jpg","userId":2}]
     */

    private ActiveBean activityDes;
    private List<ActiveUserBean> activityUserList;

    public ActiveBean getActivityDes() {
        return activityDes;
    }

    public void setActivityDes(ActiveBean activityDes) {
        this.activityDes = activityDes;
    }

    public List<ActiveUserBean> getActivityUserList() {
        return activityUserList;
    }

    public void setActivityUserList(List<ActiveUserBean> activityUserList) {
        this.activityUserList = activityUserList;
    }

}
