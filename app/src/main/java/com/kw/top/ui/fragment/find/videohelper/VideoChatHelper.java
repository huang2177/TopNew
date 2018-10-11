package com.kw.top.ui.fragment.find.videohelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.view.WindowManager;

import com.kw.top.redpacket.NimManger;
import com.kw.top.tools.ConstantValue;
import com.kw.top.ui.activity.user_center.MyAccountActivity;
import com.kw.top.utils.SPUtils;
import com.kw.top.utils.ScreenUtil;
import com.kw.top.view.dialog.GiftDialog;
import com.kw.top.view.dialog.TipOffDialog;
import com.netease.nim.avchatkit.AVChatKit;
import com.netease.nim.avchatkit.activity.AVChatActivity;
import com.netease.nim.avchatkit.event.VideoChatEvent;
import com.netease.nim.uikit.business.uinfo.UserInfoHelper;
import com.netease.nimlib.sdk.avchat.constant.AVChatType;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Des: 直播界面相关 辅助类
 * Created by huang on 2018/10/2 0002 10:51
 */
public class VideoChatHelper extends Handler implements Runnable {
    private static final long DELAYED = 1000 * 60;

    private Context context;
    private Activity chatContext;

    private String roomNum;
    private String anchorId;
    private String followType;
    private VideoChatView mChatView;

    private String token;
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

        //创建直播间
        mChatView.createRoom(anchorId, SPUtils.getString(context, ConstantValue.KEY_USER_ID), token);
        //更新用户被呼叫次数成功
        mChatView.updateCallNum(anchorId, token);
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
        if (chatContext == null) {
            removeCallbacks(this);
            post(this);
            AVChatKit.outgoingCall(context
                    , String.valueOf(ConstantValue.ACCOUNT_TEXT + anchorId)
                    , UserInfoHelper.getUserDisplayName(ConstantValue.ACCOUNT_TEXT + anchorId)
                    , AVChatType.VIDEO.getValue()
                    , AVChatActivity.FROM_INTERNAL);
        }
    }

    private void promise() {
        if (chatContext != null) {
            showRechargeDialog(chatContext);
        } else {
            startChat();
        }
    }

    private void closeChat() {
        if (chatContext != null) {
            removeCallbacks(this);
            EventBus.getDefault().post(new VideoChatEvent(VideoChatEvent.CLOSE_ROOM));
        } else {
            showRechargeDialog(context);
        }
    }


    /**
     * 视频聊天页面相关事件
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onVideoChatEvent(VideoChatEvent event) {
        switch (event.type) {
            case VideoChatEvent.OPEN_CHAT:
                chatContext = event.context;
                EventBus.getDefault().post(new VideoChatEvent(VideoChatEvent.FOLLOW_SUCCESS, followType));
                break;
            case VideoChatEvent.CLOSE_ROOM_SUCCESS: //关闭直播
                onDestroy();
                mChatView.closeRoom(roomNum, token);
                NimManger.instance().updateUserState(ConstantValue.USER_ONLINE);
                break;
            case VideoChatEvent.TIP_OFF:  // 举报dialog
                if (isChatFinished()) {
                    return;
                }
                if (tipOffDialog == null) {
                    tipOffDialog = new TipOffDialog(chatContext, roomNum, token);
                }
                tipOffDialog.show();
                break;
            case VideoChatEvent.GIT_DIALOG: //送礼物Dialog
                if (isChatFinished()) {
                    return;
                }
                if (giftDialog == null) {
                    giftDialog = new GiftDialog(chatContext, "1", anchorId, token);
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

    public void showRechargeDialog(final Context context) {
        if (isChatFinished() && context instanceof AVChatActivity) {
            return;
        }
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle("提示信息")
                .setMessage("您的金币不足，请充值！")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        context.startActivity(new Intent(context, MyAccountActivity.class));
                        dialog.dismiss();
                    }
                })
                .create();
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = ScreenUtil.getScreenWidth(context) * 4 / 5;
        dialog.getWindow().setAttributes(params);
        dialog.show();
    }

    private boolean isChatFinished() {
        return chatContext == null || chatContext.isFinishing() || chatContext.isDestroyed();
    }

    private void onDestroy() {
        removeCallbacks(this);
        EventBus.getDefault().unregister(this);
    }

}
