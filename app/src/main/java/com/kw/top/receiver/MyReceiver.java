package com.kw.top.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.kw.top.bean.GiftBean;
import com.kw.top.bean.GiftRecBean;
import com.kw.top.retrofit.HttpHost;
import com.kw.top.ui.activity.NewMainActivity;
import com.netease.nim.avchatkit.event.VideoChatEvent;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

/**
 * 自定义接收器
 * <p>
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class MyReceiver extends BroadcastReceiver {
    private static final String TAG = "JIGUANG-Example";

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Bundle bundle = intent.getExtras();
            Log.d(TAG, "=============[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + bundle);

            if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
                String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
                Log.e("tag", "================== 接收Registration Id : " + regId);

            } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
                Log.e("tag", "==================  接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
                Log.e("tag", "==================  接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_EXTRA));

                processCustomMessage(context, bundle);

            } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
                Log.d(TAG, "=============[MyReceiver] 接收到推送下来的通知");
                int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
                Log.e("tag", "==================   接收到推送下来的通知的ID: " + notifactionId);

            } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
                if (!NewMainActivity.isForeground) {
                    Intent i = new Intent(context, NewMainActivity.class);
                    i.putExtras(bundle);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(i);
                }
            } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
                //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..
                Log.e("tag", "==================  用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
                boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
                Log.e("tag", "================== [MyReceiver]" + intent.getAction() + " connected state change to " + connected);
            } else {
                Log.e("tag", "================== [MyReceiver] Unhandled intent - " + intent.getAction());
            }
        } catch (Exception e) {

        }

    }

    private void processCustomMessage(Context context, Bundle bundle) {
        if (!NewMainActivity.isForeground || bundle == null) {
            return;
        }
        String extra = bundle.getString(JPushInterface.EXTRA_EXTRA);
        GiftRecBean bean = new Gson().fromJson(extra, GiftRecBean.class);
        EventBus.getDefault().post(new VideoChatEvent(VideoChatEvent.GIT_SHOW
                , HttpHost.qiNiu + bean.getGiftPicture()
                , bean.getGiftName()));
    }
}
