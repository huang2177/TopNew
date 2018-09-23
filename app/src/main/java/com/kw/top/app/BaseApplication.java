package com.kw.top.app;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.StrictMode;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.kw.top.crash.MyCrashHandler;
import com.kw.top.redpacket.NIMRedPacketClient;
import com.kw.top.ui.activity.MainActivity;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.api.model.user.IUserInfoProvider;
import com.netease.nim.uikit.business.contact.core.query.PinYin;
import com.netease.nim.uikit.business.contact.core.util.ContactHelper;
import com.netease.nim.uikit.business.team.helper.TeamHelper;
import com.netease.nim.uikit.business.uinfo.UserInfoHelper;
import com.netease.nim.uikit.impl.NimUIKitImpl;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.uinfo.model.UserInfo;
import com.netease.nimlib.sdk.util.NIMUtil;
import com.netease.nrtc.engine.rawapi.IRtcEngine;
import com.qiniu.android.common.FixedZone;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UploadManager;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;

import java.util.Iterator;
import java.util.List;

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
        NIMClient.init(this, null, null);
        if (NIMUtil.isMainProcess(this)) {
            NIMRedPacketClient.init(this);
            initUIKit();
//            initAVChatKit();
        }
    }

    /**
     * 初始化音视频模块
     */
    private void initAVChatKit() {
//        AVChatOptions avChatOptions = new AVChatOptions() {
//            @Override
//            public void logout(Context context) {
//                MainActivity.logout(context, true);
//            }
//        };
//        avChatOptions.entranceActivity = WelcomeActivity.class;
//        avChatOptions.notificationIconRes = R.drawable.ic_stat_notify_msg;
//        AVChatKit.init(avChatOptions);
//
//        // 初始化日志系统
//        LogHelper.init();
//        // 设置用户相关资料提供者
//        AVChatKit.setUserInfoProvider(new IUserInfoProvider() {
//            @Override
//            public UserInfo getUserInfo(String account) {
//                return NimUIKit.getUserInfoProvider().getUserInfo(account);
//            }
//
//            @Override
//            public String getUserDisplayName(String account) {
//                return UserInfoHelper.getUserDisplayName(account);
//            }
//        });
//        // 设置群组数据提供者
//        AVChatKit.setTeamDataProvider(new ITeamDataProvider() {
//            @Override
//            public String getDisplayNameWithoutMe(String teamId, String account) {
//                return TeamHelper.getDisplayNameWithoutMe(teamId, account);
//            }
//
//            @Override
//            public String getTeamMemberDisplayName(String teamId, String account) {
//                return TeamHelper.getTeamMemberDisplayName(teamId, account);
//            }
//        });
    }

    /**
     * 初始化网易IM UI
     */
    private void initUIKit() {
        // 初始化
        NimUIKit.init(this);

//        // IM 会话窗口的定制初始化。
//        SessionHelper.init();
//
//        // 聊天室聊天窗口的定制初始化。
//        ChatRoomSessionHelper.init();
//
//        // 通讯录列表定制初始化
//        ContactHelper.init();
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
