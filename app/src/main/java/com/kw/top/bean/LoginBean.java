package com.kw.top.bean;

/**
 * author: zy
 * data  : 2018/5/12
 * des   :
 */

public class LoginBean {
    /**
     * password : TOPAccountPassword
     * headImg : c520c3385e1734953944
     * nickName : Nice
     * sex : 1
     * grade : 15
     * account : TOPAccountNumber5
     * token : f98532af-cccd-496e-bee5-9901d103c78c2710
     */

    private String password;
    private String headImg;
    private String nickName;
    private String sex;
    private String grade;
    private String account;
    private String token;
    private String userId;
    private String proveState; //认证状态(0 未认证,1 已认证,2 认证中,3审核中,4 未通过)

    public String getProveState() {
        return proveState;
    }

    public void setProveState(String proveState) {
        this.proveState = proveState;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
