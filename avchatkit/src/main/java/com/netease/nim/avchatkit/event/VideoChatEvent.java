package com.netease.nim.avchatkit.event;

import android.app.Activity;
import android.text.TextUtils;

/**
 * author: zy
 * data  : 2018/6/8
 * des   : 视频聊天事件
 */

public class VideoChatEvent {
    //聊天界面的关注事件
    public static final int FOLLOW = 0;

    //聊天界面点击送礼物事件
    public static final int GIT_DIALOG = 1;

    //聊天界面点击举报事件
    public static final int TIP_OFF = 2;

    //聊天界面的关注事件
    public static final int FOLLOW_SUCCESS = 3;

    //聊天界面点击送礼物事件
    public static final int GIT_SHOW = 4;

    //金币不足 请充值
    public static final int PROMISE_RECHARGE = 5;

    //去充值
    public static final int RECHARGE = 6;

    //金币用完 关闭聊天
    public static final int CLOSE_ROOM = 7;

    //关闭聊天 成功
    public static final int CLOSE_ROOM_SUCCESS = 8;

    public int type;
    public Activity context;
    public String followType;

    public VideoChatEvent(int type) {
        this.type = type;
    }

    public VideoChatEvent(int type, String followType) {
        this.type = type;
        this.followType = TextUtils.equals("1", followType) ? "已关注" : "关注";
    }

    public VideoChatEvent(int type, Activity context) {
        this.type = type;
        this.context = context;
    }
}
