package com.kw.top.utils;

import android.os.Handler;
import android.os.Looper;

/**
 * Created by zzy on 2018/1/9.
 * mail: 623932889@qq.com
 * Describe:
 */

public class MainHandler extends Handler {

    private static volatile MainHandler instance;

    public static MainHandler getInstance(){
        if (null == instance) {
            synchronized (MainHandler.class) {
                if (null == instance) {
                    instance = new MainHandler();
                }
            }
        }

        return instance;
    }

    private MainHandler() {
        super(Looper.getMainLooper());
    }

}
