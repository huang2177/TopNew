package com.kw.top.tools;

/**
 * Created by zzy on 2018/1/9.
 * mail: 623932889@qq.com
 * Describe: 接口，用来存储一些不用初始化的静态变量
 */

public interface ConstantValue {

    //侧滑菜单栏标记
    int MENU_FLAG_FIND = 0;//发现
    int MENU_FLAG_CLUB = 1;//社团
    int MENU_FLAG_CIRCLE = 2;//TOP圈
    int MENU_FLAG_LIST = 3;//TOP榜
    int MENU_FLAG_TASK = 4;//TOP任务
    int MENU_FLAG_CLASS_ROOM = 5;//小课堂
    int MENU_FLAG_ACTIVE = 6;//世界活动
    int MENU_FLAG_NEWS = 7;//消息
    int MENU_FLAG_SETTING = 8;//设置
    int MENU_FLAG_CENTER = 9;//个人中心


    //SharedPreferencesUtils key
    String KEY_TOKEN = "TOKEN";
    String KEY_PHONE = "PHONE";
    String KEY_SEX = "SEX";
    String KEY_NAME = "NAME";
    String KEY_HEAD = "HEAD";
    String KEY_ACCOUNT = "CHAT_NUM";
    String KEY_CHAT_PWD = "CHAR_PWD";
    String KEY_USER_ID = "USER_ID";
    String KEY_PROVE_STATE = "proveState";
    String KEY_VIP_GRADE = "vip_grade";
    String KEY_PRIVATE = "private";//身份保密


    //跳转Key
    String JUMP_PICKER_VIEW = "picker_view";
    int JUMP_RELEASE_IMAGE = 11;
    int JUMP_RELEASE_IMAGES = 12;

    //地址选择器等级
    String GRADE_1 = "grade1";
    String GRADE_2 = "grade2";
    String GRADE_3 = "grade3";


    //一页数据条数
    String ONE_PAGE_NUM = "20";

    String NET_EASE_TOKEN = "Net_Ease_Token";//网易Token
    String RED_PACKET_ID = "red_packet_id";
    String ACCOUNT_TEXT = "topaccountnumber";

    //用户在线状态 1.在线,3.活跃,5.在聊,7.勿扰,9.离线
    String USER_ONLINE = "1";    //在线
    String USER_ACTIVE = "3";    //活跃
    String USER_CHATING = "5"; //在聊
    String USER_UN_DISTURB = "7";//勿扰
    String USER_OFFLINE = "9";   //离线
}