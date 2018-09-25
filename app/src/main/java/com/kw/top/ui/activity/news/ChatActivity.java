package com.kw.top.ui.activity.news;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kw.top.R;
import com.kw.top.base.FriendBean;
import com.kw.top.base.MyEaseBaseActivity;
import com.kw.top.bean.event.ExitClubEvent;
import com.kw.top.redpacket.MsgViewHolderRedPacket;
import com.kw.top.redpacket.RedPacketAction;
import com.kw.top.redpacket.RedPacketAttachment;
import com.kw.top.retrofit.HttpHost;
import com.kw.top.runtimepermissions.PermissionsManager;
import com.kw.top.tools.ConstantValue;
import com.kw.top.tools.NotificationTools;
import com.kw.top.ui.activity.task.ClubTaskListActivity;
import com.kw.top.utils.SPUtils;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.api.model.session.SessionCustomization;
import com.netease.nim.uikit.business.session.actions.BaseAction;
import com.netease.nim.uikit.business.session.constant.Extras;
import com.netease.nim.uikit.business.session.fragment.MessageFragment;
import com.netease.nim.uikit.business.session.module.MsgForwardFilter;
import com.netease.nim.uikit.business.session.module.MsgRevokeFilter;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.uinfo.model.UserInfo;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * author  ： zy
 * date    ： 2018/7/1
 * des     ：
 */

public class ChatActivity extends MyEaseBaseActivity implements View.OnClickListener {
    private static final int REQUEST_CODE_RED_PACKET = 10;
    @BindView(R.id.tv_task_state)
    TextView mTaskState;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    public String userId;
    private FriendBean bean;
    private MessageFragment fragment;
    private RedPacketAction redPacketAction;
//    ChatActivity.startActivity(getContext(), friendBean.getFriendAccount(), friendBean.getNickName(), HttpHost.qiNiu + friendBean.getHeadImg(), friendBean.getNickName());

    public static void startActivity(Context context, FriendBean bean) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra("data", bean);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.em_activity_chat);
        EventBus.getDefault().register(this);
        ButterKnife.bind(this);
        initView();
        back(ivBack);
//        getClubTaskState();
        doLogin();
    }


    private void initView() {
        NimUIKit.setUserInfo(new UserInfoImp());
        NimUIKit.setMsgForwardFilter(new MsgForwardFilter() {
            @Override
            public boolean shouldIgnore(IMMessage message) {
                return true;
            }
        });
        NimUIKit.setMsgRevokeFilter(new MsgRevokeFilter() {
            @Override
            public boolean shouldIgnore(IMMessage message) {
                return true;
            }
        });
        NimUIKit.registerMsgItemViewHolder(RedPacketAttachment.class, MsgViewHolderRedPacket.class);
        bean = (FriendBean) getIntent().getSerializableExtra("data");
        userId = bean.getFriendAccount();
        tvTitle.setText(bean.getNickName());

        fragment = new MessageFragment();
        fragment.setArguments(getBundle());
        fragment.setRedPacketClickListener(this);
        getSupportFragmentManager().beginTransaction().add(R.id.container, fragment).commit();

        mTaskState.setOnClickListener(this);
    }

    public Bundle getBundle() {
        Bundle bundle = new Bundle();
        SessionCustomization customization = new SessionCustomization();
        redPacketAction = new RedPacketAction(bean.getHeadImg(), bean.getNickName());
        customization.actions = Arrays.<BaseAction>asList(redPacketAction);
        bundle.putSerializable(Extras.EXTRA_TYPE, SessionTypeEnum.P2P);
        bundle.putSerializable(Extras.EXTRA_CUSTOMIZATION, customization);
        bundle.putSerializable(Extras.EXTRA_ACCOUNT, bean.getFriendAccount());
        return bundle;
    }

    private void getClubTaskState() {
        //        int chatType = getIntent().getIntExtra(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE);
//        if (chatType == EaseConstant.CHATTYPE_GROUP) {
//             Api.getApiService().userClubTaskState(userId, getToken())
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .unsubscribeOn(Schedulers.io())
//                .subscribe(new Action1<BaseBean>() {
//                    @Override
//                    public void call(BaseBean baseBean) {
//                        if (baseBean.isSuccess()) {
//                            String state = ((LinkedTreeMap<String, String>) baseBean.getData()).get("finishTaskState");
//                            if (state.equals("0")) {
//                                mTaskState.setVisibility(View.VISIBLE);
//                            }
//                        }
//                    }
//                }, new Action1<Throwable>() {
//                    @Override
//                    public void call(Throwable throwable) {
//
//                    }
//                });
//        }
    }

    /**
     * 登录网易云平台
     */
    public void doLogin() {
        String account = SPUtils.getString(this, ConstantValue.NET_EASE_ACCOUNT);
        if (!TextUtils.isEmpty(account)) {
            return;
        }
        String token = SPUtils.getString(this, ConstantValue.NET_EASE_TOKEN);
        LoginInfo info = new LoginInfo(SPUtils.getString(this, ConstantValue.KEY_ACCOUNT), token);
        NIMClient.getService(AuthService.class).login(info).setCallback(new RequestCallbackWrapper<LoginInfo>() {
            @Override
            public void onResult(int code, LoginInfo param, Throwable exception) {
                if (param != null) {
                    SPUtils.saveString(ChatActivity.this, ConstantValue.NET_EASE_TOKEN, param.getToken());
                    SPUtils.saveString(ChatActivity.this, ConstantValue.NET_EASE_ACCOUNT, param.getAccount());
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v != null) {
            ClubTaskListActivity.startActivity(ChatActivity.this, userId, false);
        } else {
            Intent intent = new Intent(this, SendRedbagActivity.class);
            intent.putExtra("RED_TYPE", "0");
            intent.putExtra("TO_USER", bean.getNickName());
            intent.putExtra("HEAD", bean.getHeadImg());
            startActivityForResult(intent, REQUEST_CODE_RED_PACKET);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // make sure only one chat activity is opened
        String username = intent.getStringExtra("userId");
        if (userId.equals(username))
            super.onNewIntent(intent);
        else {
            finish();
            startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionsManager.getInstance().notifyPermissionsChange(permissions, grantResults);
    }

    @Override
    protected void onResume() {
        super.onResume();
        NotificationTools.cancleNotification(NotificationTools.NEWS_ID);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void exitClubEvent(ExitClubEvent exitClubEvent) {
        if (!TextUtils.isEmpty(exitClubEvent.getGroupId()))
            ChatActivity.this.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 10) {
            redPacketAction.onActivityResult(requestCode, resultCode, data);
        } else {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    public File inputStreamToFile() {
        File file = new File("/storage/emulated/0/com.kw.top/temp", "red_packet.png");
        try {
            InputStream ins = getAssets().open("icon_red_packet.png");
            OutputStream os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = ins.read(buffer, 0, 1024)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    private class UserInfoImp implements UserInfo {
        @Override
        public String getAccount() {
            return SPUtils.getString(ChatActivity.this, ConstantValue.KEY_ACCOUNT);
        }

        @Override
        public String getName() {
            return SPUtils.getString(ChatActivity.this, ConstantValue.KEY_NAME);
        }

        @Override
        public String getAvatar() {
            return HttpHost.qiNiu + SPUtils.getString(ChatActivity.this, ConstantValue.KEY_HEAD);
        }
    }
}
