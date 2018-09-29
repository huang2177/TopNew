package com.kw.top.bean.event;

/**
 * author: zy
 * data  : 2018/6/8
 * des   : app 登录 退出事件
 */

public class UserAvatarEvent {
    private String avatar;
    private String name;


    public UserAvatarEvent(String avatar, String name) {
        this.name = name;
        this.avatar = avatar;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getName() {
        return name;
    }
}
