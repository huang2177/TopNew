package com.kw.top.app;

import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.StrictMode;
import android.support.multidex.MultiDex;

import com.kw.top.R;
import com.kw.top.crash.MyCrashHandler;
import com.kw.top.redpacket.CustomAttachParser;
import com.kw.top.redpacket.MsgViewHolderAVChat;
import com.kw.top.redpacket.MsgViewHolderRedPacket;
import com.kw.top.redpacket.NimManger;
import com.kw.top.redpacket.RedPacketAttachment;
import com.kw.top.ui.activity.NewMainActivity;
import com.kw.top.ui.fragment.find.videohelper.VideoChatHelper;
import com.netease.nim.avchatkit.AVChatKit;
import com.netease.nim.avchatkit.config.AVChatOptions;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.SDKOptions;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.netease.nimlib.sdk.avchat.model.AVChatAttachment;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.util.NIMUtil;
import com.qiniu.android.common.FixedZone;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UploadManager;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;

import cn.jpush.android.api.JPushInterface;

/**
 * author: 正义
 * date  : 2018/4/13
 * desc  : 入口基类
 */

public class BaseApplication extends Application {

    private static BaseApplication instance;
    private UploadManager uploadManager;

    public UploadManager getUploadManager() {
        return uploadManager;
    }

    public static BaseApplication getInstance() {
        if (null == instance) {
            instance = new BaseApplication();
        }
        return instance;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //最先执行
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initUtils();
        initJPush();
        initQiniu();
        initUmeng();
        initVmPolicy();
        initNetEase();
        MyCrashHandler.getInstance().init(this);
    }

    private void initNetEase() {
        NIMClient.init(this, null, options());
        if (NIMUtil.isMainProcess(this)) {
            NimManger.instance().init(this);
            initUIKit();
            initAVChatKit();
        }
    }


    private SDKOptions options() {
        SDKOptions options = new SDKOptions();
        // 如果将新消息通知提醒托管给 SDK 完成，需要添加以下配置。否则无需设置。
        StatusBarNotificationConfig config = new StatusBarNotificationConfig();
        config.notificationEntrance = NewMainActivity.class; // 点击通知栏跳转到该Activity
        config.notificationSmallIconId = R.mipmap.ic_launcher;
//        options.sdkStorageRootPath = FileUtil.getAppCacheDir(this) + "/nim";
        // 呼吸灯配置
        config.ledARGB = Color.GREEN;
        config.ledOnMs = 1000;
        config.ledOffMs = 1500;
        // 通知铃声的uri字符串
        options.statusBarNotificationConfig = config;

        // 配置是否需要预下载附件缩略图，默认为 true
        options.preloadAttach = true;
        return options;
    }

    /**
     * 初始化音视频模块
     */
    private void initAVChatKit() {
        AVChatOptions avChatOptions = new AVChatOptions() {
            @Override
            public void logout(Context context) {
                NewMainActivity.logout(context, true);
            }
        };
        AVChatKit.InComingCallListener callListener = new AVChatKit.InComingCallListener() {
            @Override
            public void inComingCall() {
                new VideoChatHelper().init(getApplicationContext());
            }
        };
        avChatOptions.entranceActivity = NewMainActivity.class;
        avChatOptions.notificationIconRes = R.mipmap.ic_launcher;
        AVChatKit.init(avChatOptions, callListener);
        AVChatKit.setContext(this);
    }

    /**
     * 初始化网易IM UI
     */
    private void initUIKit() {
        // 初始化
        NimUIKit.init(this);

        // 注册定义消息解析器
        NIMClient.getService(MsgService.class).registerCustomAttachmentParser(new CustomAttachParser());
        NimUIKit.registerMsgItemViewHolder(RedPacketAttachment.class, MsgViewHolderRedPacket.class);

        NimUIKit.registerMsgItemViewHolder(AVChatAttachment.class, MsgViewHolderAVChat.class);
    }

    /**
     * 初始化极光
     */
    private void initJPush() {
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
    }

    /**
     * 使用VmPolicy方式检测FileUriExposure异常，解决7.0路径问题
     */
    private void initVmPolicy() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }
    }

    private void initQiniu() {
        Configuration config = new Configuration.Builder()
                .chunkSize(512 * 1024)        // 分片上传时，每片的大小。 默认256K
                .putThreshhold(1024 * 1024)   // 启用分片上传阀值。默认512K
                .connectTimeout(10)           // 链接超时。默认10秒
                .useHttps(true)               // 是否使用https上传域名
                .responseTimeout(60)          // 服务器响应超时。默认60秒
//                .recorder(recorder)           // recorder分片上传时，已上传片记录器。默认null
//                .recorder(recorder, keyGen)   // keyGen 分片上传时，生成标识符，用于片记录器区分是那个文件的上传记录
                .zone(FixedZone.zone0)        // 设置区域，指定不同区域的上传域名、备用域名、备用IP。
                .build();
        // 重用uploadManager。一般地，只需要创建一个uploadManager对象
        uploadManager = new UploadManager(config);
    }


    private void initUtils() {
        Utils.init(this);
    }

    private void initUmeng() {
        UMConfigure.init(this, "5b3256f4b27b0a295c00001b"
                , "umeng", UMConfigure.DEVICE_TYPE_PHONE, "");//58edcfeb310c93091c000be2 5965ee00734be40b580001a0
    }

    {
        PlatformConfig.setWeixin("wx1b2c73cd08c41490", "cd62d835738b2e3d3767192782471ab7");
    }

    /**
     * 程序终止的时候执行
     */
    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    /**
     * 低内存时候运行
     */
    @Override
    public void onLowMemory() {
        super.onLowMemory();

    }

    /**
     * HOME键退出应用程序
     * 程序在内存清理的时候运行
     *
     * @param level
     */
    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }
}
