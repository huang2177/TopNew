package com.kw.top.ui.activity.login.bean;

/**
 * Created by shibing on 2018/9/24.
 */

public class NewLoginBean {


    /**
     * registerState : 0
     * userInfo : {"headImg":"aebb28fd2e03c3618045","city":"上海市","nickName":"Test","sex":"0","grade":"1","proveState":"1","age":"23"}
     * password : TOPAccountPassword
     * userId : 85
     * account : topaccountnumber85
     * token : 8b3db864f14be42c20a769edc26c1f11
     */

    private String registerState;
    private UserInfoBean userInfo;
    private String password;
    private String userId;
    private String account;
    private String token;

    public String getRegisterState() {
        return registerState;
    }

    public void setRegisterState(String registerState) {
        this.registerState = registerState;
    }

    public UserInfoBean getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfoBean userInfo) {
        this.userInfo = userInfo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public static class UserInfoBean {
        /**
         * headImg : aebb28fd2e03c3618045
         * city : 上海市
         * nickName : Test
         * sex : 0
         * grade : 1
         * proveState : 1
         * age : 23
         */

        private String headImg;
        private String city;
        private String nickName;
        private String sex;
        private String grade;
        private String proveState;
        private String age;

        public String getHeadImg() {
            return headImg;
        }

        public void setHeadImg(String headImg) {
            this.headImg = headImg;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
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

        public String getProveState() {
            return proveState;
        }

        public void setProveState(String proveState) {
            this.proveState = proveState;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }
    }
}
