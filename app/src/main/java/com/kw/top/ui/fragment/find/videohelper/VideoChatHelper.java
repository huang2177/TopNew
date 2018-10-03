package com.kw.top.ui.fragment.find.videohelper;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import com.kw.top.tools.ConstantValue;
import com.kw.top.ui.activity.user_center.MyAccountActivity;
import com.kw.top.utils.RxToast;
import com.kw.top.utils.SPUtils;
import com.kw.top.view.GiftDialog;
import com.kw.top.view.TipOffDialog;
import com.netease.nim.avchatkit.AVChatKit;
import com.netease.nim.avchatkit.activity.AVChatActivity;
import com.netease.nim.avchatkit.event.VideoChatEvent;
import com.netease.nim.uikit.business.uinfo.UserInfoHelper;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.avchat.constant.AVChatType;
import com.netease.nimlib.sdk.event.EventSubscribeService;
import com.netease.nimlib.sdk.event.EventSubscribeServiceObserver;
import com.netease.nimlib.sdk.event.model.Event;
import com.netease.nimlib.sdk.event.model.EventSubscribeRequest;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * Des: 直播界面相关 辅助类
 * Created by huang on 2018/10/2 0002 10:51
 */
public class VideoChatHelper extends Handler implements Runnable {
    private static final long DELAYED = 1000 * 60;

    private Context context;

    private String roomNum;
    private String anchorId;
    private String followType;
    private VideoChatView mChatView;

    private String token;
    private boolean isInitVideo;
    private GiftDialog giftDialog;
    private TipOffDialog tipOffDialog;

    public void init(Context context) {
        this.context = context;
        mChatView = new VideoChatView(this);
        token = SPUtils.getString(this.context, ConstantValue.KEY_TOKEN);

        EventBus.getDefault().unregister(this);
        EventBus.getDefault().register(this);
    }

    @Override
    public void run() {
        mChatView.isAdequate(roomNum, token);
        postDelayed(this, DELAYED);
    }


    /***
     * 创建房间
     */
    public void createRoom(Context context, String anchorId, String followType) {
        init(context);
        this.anchorId = anchorId;
        this.followType = followType;

        mChatView.createRoom(anchorId, SPUtils.getString(context, ConstantValue.KEY_USER_ID), token);
    }

    /***
     * 创建房间成功
     * @param roomNum
     */
    public void bindCreateRoom(String roomNum) {
        this.roomNum = roomNum;
        mChatView.isAdequate(roomNum, token);
    }

    /***
     * 是否够继续观看直播
     * @param i 0不管  1提示方式金币不足，请充值  2直接关播
     */
    public void isAdequate(int i) {
        switch (i) {
            case 0:
                startChat();
                break;
            case 1:
                promise();
                break;
            case 2:
                closeChat();
                break;
        }
    }

    private void startChat() {
        if (!isInitVideo) {
            removeCallbacks(this);
            post(this);
            AVChatKit.outgoingCall(context
                    , String.valueOf(ConstantValue.ACCOUNT_TEXT + anchorId)
                    , UserInfoHelper.getUserDisplayName(ConstantValue.ACCOUNT_TEXT + anchorId)
                    , AVChatType.VIDEO.getValue()
                    , AVChatActivity.FROM_INTERNAL);
            EventBus.getDefault().post(new VideoChatEvent(VideoChatEvent.FOLLOW_SUCCESS, followType));
            isInitVideo = true;
        }
    }

    private void promise() {
        if (isInitVideo) {
            EventBus.getDefault().post(new VideoChatEvent(VideoChatEvent.PROMISE_RECHARGE));
        } else {
            startChat();
        }
    }

    private void closeChat() {
        if (isInitVideo) {
            removeCallbacks(this);
            EventBus.getDefault().post(new VideoChatEvent(VideoChatEvent.CLOSE_ROOM));
        } else {
            RxToast.normal("您的金币不足，请充值！");
        }
    }


    /**
     * 视频聊天页面相关事件
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onVideoChatEvent(VideoChatEvent event) {
        switch (event.type) {
            case VideoChatEvent.CLOSE_ROOM_SUCCESS: //关闭直播
                onDestroy();
                mChatView.closeRoom(roomNum, token);
                break;
            case VideoChatEvent.RECHARGE: //去充值
                event.context.startActivity(new Intent(event.context, MyAccountActivity.class));
                break;
            case VideoChatEvent.TIP_OFF:  // 举报dialog
                if (event.context.isFinishing() || event.context.isDestroyed()) {
                    return;
                }
                if (tipOffDialog == null) {
                    tipOffDialog = new TipOffDialog(event.context, roomNum, token);
                }
                tipOffDialog.show();
                break;
            case VideoChatEvent.GIT_DIALOG: //送礼物Dialog
                if (event.context.isFinishing() || event.context.isDestroyed()) {
                    return;
                }
                if (giftDialog == null) {
                    giftDialog = new GiftDialog(event.context, "1", anchorId, token);
                }
                giftDialog.show();
                break;
        }
    }

    public void onResume() {
//        postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if (AVChatActivity.isNeedResume()) {
//                    Intent localIntent = new Intent();
//                    localIntent.setClass(context, AVChatActivity.class);
//                    localIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                    context.startActivity(localIntent);
//                }
//            }
//        }, 300);
    }

    private void onDestroy() {
        removeCallbacks(this);
        EventBus.getDefault().unregister(this);
    }

}
