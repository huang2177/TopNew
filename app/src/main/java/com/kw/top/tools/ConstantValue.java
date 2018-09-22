package com.kw.top.tools;

/**
 * Created by zzy on 2018/1/9.
 * mail: 623932889@qq.com
 * Describe: 接口，用来存储一些不用初始化的静态变量
 */

public interface ConstantValue {

    //侧滑菜单栏标记
    public static final int MENU_FLAG_FIND = 0;//发现
    public static final int MENU_FLAG_CLUB = 1;//社团
    public static final int MENU_FLAG_CIRCLE = 2;//TOP圈
    public static final int MENU_FLAG_LIST = 3;//TOP榜
    public static final int MENU_FLAG_TASK = 4;//TOP任务
    public static final int MENU_FLAG_CLASS_ROOM = 5;//小课堂
    public static final int MENU_FLAG_ACTIVE = 6;//世界活动
    public static final int MENU_FLAG_NEWS = 7;//消息
    public static final int MENU_FLAG_SETTING = 8;//设置
    public static final int MENU_FLAG_CENTER =9;//个人中心


    //SharedPreferencesUtils key
    public static final String KEY_TOKEN = "TOKEN";
    public static final String KEY_PHONE = "PHONE";
    public static final String KEY_ALIAS = "ALIAS";
    public static final String KEY_PWD   = "PWD";
    public static final String KEY_REGISTER = "REGISTER";
    public static final String KEY_GYM  = "GYM";
    public static final String KEY_USER_BESN ="USER_BESN";
    public static final String KEY_PHOTO = "PHOTO";
    public static final String KEY_SEX = "SEX";
    public static final String KEY_NAME = "NAME";
    public static final String KEY_HEAD = "HEAD";
    public static final String KEY_CHAT_NUM ="CHAT_NUM";
    public static final String KEY_CHAT_PWD ="CHAR_PWD";
    public static final String KEY_INVITE_URL ="INVITE_URL";
    public static final String KEY_USER_ID = "USER_ID";
    public static final String KEY_PROVE_STATE = "proveState";
    public static final String KEY_VIP_GRADE = "vip_grade";
    public static final String KEY_PRIVATE = "private";//身份保密


    //跳转Key
    public static final String JUMP_PICKER_VIEW = "picker_view";
    public static final int JUMP_RELEASE_IMAGE = 11;

    //地址选择器等级
    public static final String GRADE_1 ="grade1";
    public static final String GRADE_2 ="grade2";
    public static final String GRADE_3 ="grade3";

    //文件路径
    public static final String IMAGE_RELEASE = "ylke/image/";

    //一页数据条数
    public static final String ONE_PAGE_NUM = "20";


    //环信 userName  TOPAccountNumber + ID
    public static final String CHAT_USER_NAME = "TOPAccountNumber";
    public static final String CHAT_PWD = "TOPAccountPassword";

    public static final String VIP = "vip";
    public static final String HEAD_URL = "head_url";
    public static final String NICK_NAME = "nick_name";//自定义扩展消息

}
