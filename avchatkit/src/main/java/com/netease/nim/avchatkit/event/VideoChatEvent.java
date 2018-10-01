package com.netease.nim.avchatkit.event;

import android.app.Activity;
import android.content.Context;

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

    public int type;
    public Activity context;

    public VideoChatEvent(int type) {
        this.type = type;
    }

    public VideoChatEvent(int type, Activity context) {
        this.type = type;
        this.context = context;
    }
}
