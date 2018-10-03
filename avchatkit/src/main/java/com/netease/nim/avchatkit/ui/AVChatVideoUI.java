package com.netease.nim.avchatkit.ui;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.netease.nim.avchatkit.AVChatKit;
import com.netease.nim.avchatkit.R;
import com.netease.nim.avchatkit.common.imageview.HeadImageView;
import com.netease.nim.avchatkit.common.permission.BaseMPermission;
import com.netease.nim.avchatkit.common.widgets.ToggleListener;
import com.netease.nim.avchatkit.constant.AVChatExitCode;
import com.netease.nim.avchatkit.controll.AVChatController;
import com.netease.nim.avchatkit.event.VideoChatEvent;
import com.netease.nim.avchatkit.module.AVChatControllerCallback;
import com.netease.nim.avchatkit.module.AVSwitchListener;
import com.netease.nimlib.sdk.avchat.AVChatManager;
import com.netease.nimlib.sdk.avchat.constant.AVChatType;
import com.netease.nimlib.sdk.avchat.constant.AVChatVideoScalingType;
import com.netease.nimlib.sdk.avchat.model.AVChatData;
import com.netease.nimlib.sdk.avchat.model.AVChatSurfaceViewRenderer;
import com.netease.nrtc.video.render.IVideoRender;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * 视频界面变化及点击事件
 * Created by winnie on 2017/12/11.
 */

public class AVChatVideoUI implements View.OnClickListener, ToggleListener {

    // constant
    private static final int PEER_CLOSE_CAMERA = 0;
    private static final int LOCAL_CLOSE_CAMERA = 1;
    private static final int AUDIO_TO_VIDEO_WAIT = 2;

    private final String[] BASIC_PERMISSIONS = new String[]{Manifest.permission.CAMERA,};

    /**
     * surface view
     */
    private LinearLayout largeSizePreviewLayout;
    private FrameLayout smallSizePreviewFrameLayout;
    private LinearLayout smallSizePreviewLayout;
    private ImageView smallSizePreviewCoverImg;//stands for peer or local close camera
    private View largeSizePreviewCoverLayout;//stands for peer or local close camera
    private View touchLayout;

    /**
     * video view
     */
    //顶部控制按钮
    private View topRoot;
    private TextView topCareTV;
    private TextView topNickNameTV;
    private HeadImageView topHeadImg;
    private TextView middleGiftShowTV;
    private ImageView middleGiftShowIV;
    private LinearLayout middleGiftRoot;

    //中间控制按钮
    private View middleRoot;
    private HeadImageView headImg;
    private TextView nickNameTV;
    private View refuse_receive;
    private TextView refuseTV;
    private TextView receiveTV;
    //底部控制按钮
    private View bottomRoot;
    private FrameLayout giftView;
    private FrameLayout filterView;
    private FrameLayout tipOffView;
    private FrameLayout hangUpView;
    //face unity
    private View faceUnityRoot;
    //摄像头权限提示显示
    private View permissionRoot;

    //render
    private AVChatSurfaceViewRenderer smallRender;
    private AVChatSurfaceViewRenderer largeRender;

    // state
    private boolean surfaceInit = false;
    private boolean videoInit = false;
    private boolean canSwitchCamera = false;
    private boolean isPeerVideoOff = false;
    private boolean isLocalVideoOff = false;
    private boolean localPreviewInSmallSize = true;

    // data
    private TouchZoneCallback touchZoneCallback;
    private AVChatData avChatData;
    private String account;
    private String displayName;

    private int topRootHeight = 0;
    private int bottomRootHeight = 0;

    private String largeAccount; // 显示在大图像的用户id
    private String smallAccount; // 显示在小图像的用户id

    private Context context;
    private View root;
    private AVChatController avChatController;
    private boolean isReleasedVideo = false;
    private boolean mIsInComingCall; //是否是来电

    // touch zone
    public interface TouchZoneCallback {
        void onTouch();
    }

    public AVChatVideoUI(Context context, View root, AVChatData avChatData, String displayName,
                         AVChatController avChatController, TouchZoneCallback touchZoneCallback) {
        this.root = root;
        this.context = context;
        this.avChatData = avChatData;
        this.displayName = displayName;
        this.avChatController = avChatController;
        this.touchZoneCallback = touchZoneCallback;
        this.smallRender = new AVChatSurfaceViewRenderer(context);
        this.largeRender = new AVChatSurfaceViewRenderer(context);

        EventBus.getDefault().register(this);
    }

    /**
     * ********************** surface 初始化 **********************
     */

    private void findSurfaceView() {
        if (surfaceInit) {
            return;
        }
        View surfaceView = root.findViewById(R.id.avchat_surface_layout);
        if (surfaceView != null) {
            touchLayout = surfaceView.findViewById(R.id.touch_zone);
            touchLayout.setOnTouchListener(touchListener);

            smallSizePreviewLayout = (LinearLayout) surfaceView.findViewById(R.id.small_size_preview);
            smallSizePreviewCoverImg = (ImageView) surfaceView.findViewById(R.id.smallSizePreviewCoverImg);
            smallSizePreviewFrameLayout = (FrameLayout) surfaceView.findViewById(R.id.small_size_preview_layout);
            smallSizePreviewFrameLayout.setOnClickListener(this);

            largeSizePreviewCoverLayout = surfaceView.findViewById(R.id.notificationLayout);
            largeSizePreviewLayout = (LinearLayout) surfaceView.findViewById(R.id.large_size_preview);

            surfaceInit = true;
        }
    }


    private View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_UP && touchZoneCallback != null) {
                touchZoneCallback.onTouch();
            }

            return true;
        }
    };


    private IVideoRender remoteRender;
    private IVideoRender localRender;

    // 大小图像显示切换
    private void switchRender(String user1, String user2) {
        String remoteId = TextUtils.equals(user1, AVChatKit.getAccount()) ? user2 : user1;

        if (remoteRender == null && localRender == null) {
            localRender = smallRender;
            remoteRender = largeRender;
        }

        //交换
        IVideoRender render = localRender;
        localRender = remoteRender;
        remoteRender = render;


        //断开SDK视频绘制画布
        AVChatManager.getInstance().setupLocalVideoRender(null, false, 0);
        AVChatManager.getInstance().setupRemoteVideoRender(remoteId, null, false, 0);

        //重新关联上画布
        AVChatManager.getInstance().setupLocalVideoRender(localRender, false, AVChatVideoScalingType.SCALE_ASPECT_BALANCED);
        AVChatManager.getInstance().setupRemoteVideoRender(remoteId, remoteRender, false, AVChatVideoScalingType.SCALE_ASPECT_BALANCED);

    }

    /**
     * ************************** video 初始化 ***********************
     */
    private void findVideoViews() {
        if (videoInit) {
            return;
        }
        View videoRoot = root.findViewById(R.id.avchat_video_layout);
        topRoot = videoRoot.findViewById(R.id.avchat_video_top_control);
        topCareTV = topRoot.findViewById(R.id.avchat_video_care);
        topHeadImg = topRoot.findViewById(R.id.avchat_video_avatar);
        topNickNameTV = topRoot.findViewById(R.id.avchat_video_name);
        middleGiftShowTV = topRoot.findViewById(R.id.avchat_video_gift_show);
        middleGiftShowIV = topRoot.findViewById(R.id.avchat_video_gift_img);
        middleGiftRoot = topRoot.findViewById(R.id.avchat_video_gift_root);
        topCareTV.setOnClickListener(this);

        middleRoot = videoRoot.findViewById(R.id.avchat_video_middle_control);
        headImg = middleRoot.findViewById(R.id.avchat_video_head);
        nickNameTV = (TextView) middleRoot.findViewById(R.id.avchat_video_nickname);

        refuse_receive = middleRoot.findViewById(R.id.avchat_video_refuse_receive);
        refuseTV = (TextView) refuse_receive.findViewById(R.id.refuse);
        receiveTV = (TextView) refuse_receive.findViewById(R.id.receive);
        refuseTV.setOnClickListener(this);
        receiveTV.setOnClickListener(this);

        bottomRoot = videoRoot.findViewById(R.id.avchat_video_bottom_control);
        faceUnityRoot = videoRoot.findViewById(R.id.avchat_video_face_unity);

        giftView = bottomRoot.findViewById(R.id.fl_avchat_video_gif);
        hangUpView = bottomRoot.findViewById(R.id.fl_avchat_video_logout);
        filterView = bottomRoot.findViewById(R.id.fl_avchat_video_filter);
        tipOffView = bottomRoot.findViewById(R.id.fl_avchat_video_tip_off);
        giftView.setOnClickListener(this);
        tipOffView.setOnClickListener(this);
        filterView.setOnClickListener(this);
        hangUpView.setOnClickListener(this);

        permissionRoot = videoRoot.findViewById(R.id.avchat_video_permission_control);
        videoInit = true;
    }

    /**
     * ********************** 视频流程 **********************
     */

    //来电
    public void showIncomingCall(AVChatData avChatData) {
        mIsInComingCall = true;
        this.avChatData = avChatData;
        this.account = avChatData.getAccount();

        findSurfaceView();
        findVideoViews();

        showProfile();//对方的详细信息
        setRefuseReceive(true);
        receiveTV.setText(R.string.avchat_pickup);
        setTopRoot(false);
        setMiddleRoot(true);
        setBottomRoot(false);
        setFaceUnityRoot(false);
    }

    //去电
    public void doOutgoingCall(String account) {
        mIsInComingCall = false;
        this.account = account;

        findSurfaceView();
        findVideoViews();

        showProfile();//对方的详细信息
        setRefuseReceive(false);
        setTopRoot(false);
        setMiddleRoot(true);
        setBottomRoot(false);
        setFaceUnityRoot(false);

        avChatController.doCalling(account, AVChatType.VIDEO, new AVChatControllerCallback<AVChatData>() {
            @Override
            public void onSuccess(AVChatData data) {
                avChatData = data;
                avChatController.setAvChatData(data);
                List<String> deniedPermissions = BaseMPermission.getDeniedPermissions((Activity) context, BASIC_PERMISSIONS);
                if (deniedPermissions != null && !deniedPermissions.isEmpty()) {
                    showNoneCameraPermissionView(true);
                    return;
                }
                canSwitchCamera = true;
                initLargeSurfaceView(AVChatKit.getAccount());
            }

            @Override
            public void onFailed(int code, String errorMsg) {
                closeSession();
            }
        });
    }

    public void showVideoInitLayout() {
        findSurfaceView();
        findVideoViews();

        setTopRoot(true);
        setMiddleRoot(false);
        setBottomRoot(true);
        setFaceUnityRoot(false);
        showNoneCameraPermissionView(false);
    }

    // 小图像surface view 初始化
    public void initSmallSurfaceView(String account) {
        smallAccount = account;
        smallSizePreviewFrameLayout.setVisibility(View.VISIBLE);

        // 设置画布，加入到自己的布局中，用于呈现视频图像
        AVChatManager.getInstance().setupLocalVideoRender(null, false, AVChatVideoScalingType.SCALE_ASPECT_BALANCED);
        AVChatManager.getInstance().setupLocalVideoRender(smallRender, false, AVChatVideoScalingType.SCALE_ASPECT_BALANCED);
        addIntoSmallSizePreviewLayout(smallRender);

        smallSizePreviewFrameLayout.bringToFront();
        localRender = smallRender;
        localPreviewInSmallSize = true;
    }

    private void addIntoSmallSizePreviewLayout(SurfaceView surfaceView) {
        smallSizePreviewCoverImg.setVisibility(View.GONE);
        if (surfaceView.getParent() != null) {
            ((ViewGroup) surfaceView.getParent()).removeView(surfaceView);
        }
        smallSizePreviewLayout.removeAllViews();
        smallSizePreviewLayout.addView(surfaceView);
        surfaceView.setZOrderMediaOverlay(true);
        smallSizePreviewLayout.setVisibility(View.VISIBLE);
    }


    // 大图像surface view 初始化
    public void initLargeSurfaceView(String account) {
        // 设置画布，加入到自己的布局中，用于呈现视频图像
        // account 要显示视频的用户帐号
        largeAccount = account;
        if (AVChatKit.getAccount().equals(account)) {
            AVChatManager.getInstance().setupLocalVideoRender(largeRender, false, AVChatVideoScalingType.SCALE_ASPECT_BALANCED);
        } else {
            AVChatManager.getInstance().setupRemoteVideoRender(account, largeRender, false, AVChatVideoScalingType.SCALE_ASPECT_BALANCED);
        }
        addIntoLargeSizePreviewLayout(largeRender);
        remoteRender = largeRender;
    }

    private void addIntoLargeSizePreviewLayout(SurfaceView surfaceView) {
        if (surfaceView.getParent() != null) {
            ((ViewGroup) surfaceView.getParent()).removeView(surfaceView);
        }
        largeSizePreviewLayout.removeAllViews();
        largeSizePreviewLayout.addView(surfaceView);
        surfaceView.setZOrderMediaOverlay(false);
        largeSizePreviewCoverLayout.setVisibility(View.GONE);
    }


    /********************** 界面显示 **********************************/

    // 显示个人信息
    private void showProfile() {
        headImg.loadBuddyAvatar(account);
        nickNameTV.setText(displayName);
    }


    private void setRefuseReceive(boolean visible) {
        receiveTV.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    private void setTopRoot(boolean visible) {
        topNickNameTV.setText(displayName);
        topHeadImg.loadBuddyAvatar(account);
        topRoot.setVisibility(visible ? View.VISIBLE : View.GONE);
        if (topRootHeight == 0) {
            Rect rect = new Rect();
            topRoot.getGlobalVisibleRect(rect);
            topRootHeight = rect.bottom;
        }
    }

    private void setMiddleRoot(boolean visible) {
        middleRoot.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    private void setBottomRoot(boolean visible) {
        bottomRoot.setVisibility(visible ? View.VISIBLE : View.GONE);
        if (bottomRootHeight == 0) {
            bottomRootHeight = bottomRoot.getHeight();
        }
    }

    private void setFaceUnityRoot(boolean visible) {
        if (faceUnityRoot == null) {
            return;
        }
        faceUnityRoot.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public void showNoneCameraPermissionView(boolean show) {
        permissionRoot.setVisibility(show ? View.VISIBLE : View.GONE);
    }


    // 摄像头切换时，布局显隐
    private void switchAndSetLayout() {
        localPreviewInSmallSize = !localPreviewInSmallSize;
        largeSizePreviewCoverLayout.setVisibility(View.GONE);
        smallSizePreviewCoverImg.setVisibility(View.GONE);
        if (isPeerVideoOff) {
            peerVideoOff();
        }
        if (isLocalVideoOff) {
            localVideoOff();
        }
    }

    /**
     * ******************** 点击事件 **********************
     */

    @Override
    public void toggleOn(View v) {
        onClick(v);
    }

    @Override
    public void toggleOff(View v) {
        onClick(v);
    }

    @Override
    public void toggleDisable(View v) {

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.refuse) {
            doRefuseCall();
        } else if (i == R.id.receive) {
            doReceiveCall();
        } else if (i == R.id.fl_avchat_video_logout) {
            doHangUp();
        } else if (i == R.id.fl_avchat_video_filter) {
            doFilter();
        } else if (i == R.id.fl_avchat_video_gif) {
            doGift();
        } else if (i == R.id.fl_avchat_video_tip_off) {
            doTipOff();
        } else if (i == R.id.avchat_video_care) {
            doCare();
        } else if (i == R.id.small_size_preview_layout) {
            switchTemp();
        }
    }

    private void switchTemp() {
        String temp;
        switchRender(smallAccount, largeAccount);
        temp = largeAccount;
        largeAccount = smallAccount;
        smallAccount = temp;
        switchAndSetLayout();
    }

    //挂断视频
    private void doHangUp() {
        releaseVideo();
        avChatController.hangUp(AVChatExitCode.HANGUP);
        closeSession();
        EventBus.getDefault().unregister(this);
    }

    //举报
    private void doTipOff() {
        EventBus.getDefault().post(new VideoChatEvent(VideoChatEvent.TIP_OFF, (Activity) context));
    }

    //送礼物
    private void doGift() {
        EventBus.getDefault().post(new VideoChatEvent(VideoChatEvent.GIT_DIALOG, (Activity) context));
    }

    //滤镜
    private void doFilter() {
        setFaceUnityRoot(faceUnityRoot.getVisibility() == View.GONE);
    }

    //关注
    private void doCare() {
        EventBus.getDefault().post(new VideoChatEvent(VideoChatEvent.FOLLOW));
    }

    // 拒绝来电
    private void doRefuseCall() {
        avChatController.hangUp(AVChatExitCode.HANGUP);
        closeSession();
    }

    private void doReceiveCall() {
        avChatController.receive(AVChatType.VIDEO, new AVChatControllerCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                canSwitchCamera = true;
            }

            @Override
            public void onFailed(int code, String errorMsg) {
                closeSession();
            }
        });
    }


    public void releaseVideo() {
        if (isReleasedVideo) {
            return;
        }
        isReleasedVideo = true;
        AVChatManager.getInstance().stopVideoPreview();
        AVChatManager.getInstance().disableVideo();
    }

    // 本地关闭了摄像头
    private void localVideoOff() {
        isLocalVideoOff = true;
        if (localPreviewInSmallSize)
            closeSmallSizePreview();
        else
            showNotificationLayout(LOCAL_CLOSE_CAMERA);
    }

    // 对方关闭了摄像头
    public void peerVideoOff() {
        isPeerVideoOff = true;
        if (localPreviewInSmallSize) { //local preview in small size layout, then peer preview should in large size layout
            showNotificationLayout(PEER_CLOSE_CAMERA);
        } else {  // peer preview in small size layout
            closeSmallSizePreview();
        }
    }

    // 对方打开了摄像头
    public void peerVideoOn() {
        isPeerVideoOff = false;
        if (localPreviewInSmallSize) {
            largeSizePreviewCoverLayout.setVisibility(View.GONE);
        } else {
            smallSizePreviewCoverImg.setVisibility(View.GONE);
        }
    }

    // 关闭小窗口
    private void closeSmallSizePreview() {
        smallSizePreviewCoverImg.setVisibility(View.VISIBLE);
    }

    // 界面提示
    private void showNotificationLayout(int closeType) {
        if (largeSizePreviewCoverLayout == null) {
            return;
        }
        TextView textView = (TextView) largeSizePreviewCoverLayout;
        switch (closeType) {
            case PEER_CLOSE_CAMERA:
                textView.setText(R.string.avchat_peer_close_camera);
                break;
            case LOCAL_CLOSE_CAMERA:
                textView.setText(R.string.avchat_local_close_camera);
                break;
            case AUDIO_TO_VIDEO_WAIT:
                textView.setText(R.string.avchat_audio_to_video_wait);
                break;
            default:
                return;
        }
        largeSizePreviewCoverLayout.setVisibility(View.VISIBLE);
    }


    /**********************录制**********************/

    private void closeSession() {
        ((Activity) context).finish();
    }

    public AVChatData getAvChatData() {
        return avChatData;
    }


    /************视频聊天页面相关事件**********************/

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onVideoChatEvent(VideoChatEvent event) {
        switch (event.type) {
            case VideoChatEvent.GIT_SHOW:
                showGift(event);
                break;
            case VideoChatEvent.CLOSE_ROOM:
                doHangUp();
                break;
            case VideoChatEvent.FOLLOW_SUCCESS:
                if (topCareTV != null) {
                    topCareTV.setText(event.followType);
                }
                break;
        }
    }

    //展示已送的礼物
    private void showGift(VideoChatEvent event) {
        middleGiftRoot.setVisibility(View.VISIBLE);
        Glide.with(context).load(event.giftUrl).into(middleGiftShowIV);
        if (mIsInComingCall) {
            middleGiftShowTV.setText(displayName + "赠送了" + event.giftName);
        } else {
            middleGiftShowTV.setText("赠送" + displayName + event.giftName);
        }
    }
}
