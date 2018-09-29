package com.kw.top.bean.event;

/**
 * author: zy
 * data  : 2018/6/8
 * des   : app 登录 退出事件
 */

public class AppLoginEvent {
    private boolean isLogin;
    private String token;


    public AppLoginEvent(boolean isLogin) {
        this.isLogin = isLogin;
    }

    public AppLoginEvent(boolean isLogin, String token) {
        this.isLogin = isLogin;
        this.token = token;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public String getToken() {
        return token;
    }
}
