package com.kw.top.tools;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.EMGroupChangeListener;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMucSharedFile;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.util.NetUtils;
import com.kw.top.app.BaseApplication;
import com.kw.top.bean.event.MessageEvent;
import com.kw.top.bean.event.MsgCountEvent;
import com.kw.top.ui.activity.club.VipApplyActivity;
import com.kw.top.ui.activity.news.ChatActivity;
import com.kw.top.utils.RxToast;
import com.kw.top.utils.SPUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * author: zy
 * data  : 2018/6/2
 * des   :
 */

public class ChatHelper {

    private static ChatHelper instance;
    private AsyncTask<Void, Void, Void> mAsyncTask;
    private List<String> mFrendsList = new ArrayList<>();
    private List<EMMessage> mMessageList = new ArrayList<>();

    public synchronized static ChatHelper getInstance() {
        if (null == instance) {
            instance = new ChatHelper();
        }
        return instance;
    }

    /**
     * 初始化消息监听
     */
    public void init(Activity activity) {
        initMessageList();
        connectionListener(activity);
        initGroupChangeListerner(activity);
    }

    private void initGroupChangeListerner(final Activity activity) {
        EMClient.getInstance().groupManager().addGroupChangeListener(new EMGroupChangeListener() {

            @Override
            public void onInvitationReceived(String groupId, String groupName, String inviter, String reason) {
                //接收到群组加入邀请
                Log.e("tag", "================ 接收到群组加入邀请");
            }

            @Override
            public void onRequestToJoinReceived(String groupId, String groupName, String applyer, String reason) {
                //用户申请加入群
                Intent intent = new Intent(activity, VipApplyActivity.class);//点击之后进入MainActivity
                intent.putExtra("ID", groupId);
                PendingIntent pendingIntent = PendingIntent.getActivity(activity, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                NotificationTools.showNotification(activity, pendingIntent, "您有一条新的群验证消息", NotificationTools.GROUP_ID, NotificationTools.GROUP_CHANNEL_ID);
                Log.e("tag", "================ 用户申请加入群");
            }

            @Override
            public void onRequestToJoinAccepted(String groupId, String groupName, String accepter) {
                //加群申请被同意
                Log.e("tag", "================ 加群申请被同意");
            }

            @Override
            public void onRequestToJoinDeclined(String groupId, String groupName, String decliner, String reason) {
                //加群申请被拒绝
                Log.e("tag", "================ 加群申请被拒绝");
            }

            @Override
            public void onInvitationAccepted(String groupId, String inviter, String reason) {
                //群组邀请被同意
            }

            @Override
            public void onInvitationDeclined(String groupId, String invitee, String reason) {
                //群组邀请被拒绝
            }

            @Override
            public void onUserRemoved(String s, String s1) {
                Log.e("tag", "================ onUserRemoved");
            }

            @Override
            public void onGroupDestroyed(String s, String s1) {
                Log.e("tag", "================ onGroupDestroyed");
            }

            @Override
            public void onAutoAcceptInvitationFromGroup(String groupId, String inviter, String inviteMessage) {
                //接收邀请时自动加入到群组的通知
                Log.e("tag", "================ 接收邀请时自动加入到群组的通知");
            }

            @Override
            public void onMuteListAdded(String groupId, final List<String> mutes, final long muteExpire) {
                //成员禁言的通知
            }

            @Override
            public void onMuteListRemoved(String groupId, final List<String> mutes) {
                //成员从禁言列表里移除通知
            }

            @Override
            public void onAdminAdded(String groupId, String administrator) {
                //增加管理员的通知
            }

            @Override
            public void onAdminRemoved(String groupId, String administrator) {
                //管理员移除的通知
            }

            @Override
            public void onOwnerChanged(String groupId, String newOwner, String oldOwner) {
                //群所有者变动通知
            }

            @Override
            public void onMemberJoined(final String groupId, final String member) {
                //群组加入新成员通知
                Log.e("tag", "================ 群组加入新成员通知");
            }

            @Override
            public void onMemberExited(final String groupId, final String member) {
                //群成员退出通知
                Log.e("tag", "================ 群成员退出通知");
            }

            @Override
            public void onAnnouncementChanged(String groupId, String announcement) {
                //群公告变动通知
            }

            @Override
            public void onSharedFileAdded(String groupId, EMMucSharedFile sharedFile) {
                //增加共享文件的通知
            }

            @Override
            public void onSharedFileDeleted(String groupId, String fileId) {
                //群共享文件删除通知
            }
        });
    }

    /**
     * 是否登录
     *
     * @return
     */
    public boolean isLoggedIn() {
        return EMClient.getInstance().isLoggedInBefore();
    }


    /**
     * 初始化消息列表
     */
    public void initMessageList() {
        mAsyncTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                EMClient.getInstance().chatManager().addMessageListener(msgListener);
                return null;
            }
        }.execute();
    }

    EMMessageListener msgListener = new EMMessageListener() {

        @Override
        public void onMessageReceived(List<EMMessage> messages) {
            //收到消息
            mMessageList.clear();
            mMessageList = messages;
            for (int i = 0; i < messages.size(); i++) {
                EMMessage message = messages.get(i);
                EventBus.getDefault().post(new MessageEvent(message));
                EMTextMessageBody messageBody = (EMTextMessageBody) message.getBody();
//                EMMessage.ChatType chatType = emMessage.getChatType();
//                if (chatType == EMMessage.ChatType.GroupChat) {
//                    EMGroup emGroup = EMClient.getInstance().groupManager().getGroup(emMessage.getTo());
//                    Log.e("tag", "============  GroupName " + emGroup.getGroupName());
//                } else {
//                }
//                Log.e("tag", "==============  收到消息：" + messageBody.getMessage() + "  to " + emMessage.getTo() + "  id " + emMessage.getMsgId()
//                        + "   type " + emMessage.getType() + " chatType " + emMessage.getChatType() + " from " + emMessage.getFrom() + "  name " + emMessage.getUserName());
                Intent intent = new Intent(BaseApplication.getInstance().getApplicationContext(), ChatActivity.class);
                EMMessage.ChatType chatType = message.getChatType();
                String toUsername;
                if (chatType == EMMessage.ChatType.Chat) { // single chat message
                    toUsername = message.getFrom();
                    intent.putExtra("userId", message.getFrom());
                    intent.putExtra("chatType", Constant.CHATTYPE_SINGLE);
                    intent.putExtra(EaseConstant.EXTRA_TOOLBAR_TITLE, message.getStringAttribute(EaseConstant.NICK_NAME, ""));
                } else { // group chat message
                    // message.getTo() is the group id
                    toUsername = message.getTo();
                    intent.putExtra("userId", message.getTo());
                    if (chatType == EMMessage.ChatType.GroupChat) {
                        intent.putExtra("chatType", Constant.CHATTYPE_GROUP);
                        EMGroup emGroup = EMClient.getInstance().groupManager().getGroup(message.getTo());
                        intent.putExtra(EaseConstant.EXTRA_TOOLBAR_TITLE, emGroup.getGroupName());
                    } else {
                        intent.putExtra("chatType", Constant.CHATTYPE_CHATROOM);
                        toUsername = "";
                    }

                }
                PendingIntent pendingIntent = PendingIntent.getActivity(BaseApplication.getInstance().getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    if (ChatActivity.activityInstance != null && ChatActivity.activityInstance.getToChatUsername().equals(toUsername) && !ChatActivity.activityInstance.getBackgroud()){

                    }else {
                        NotificationTools.showNotification(BaseApplication.getInstance().getApplicationContext(), pendingIntent, "您有新的消息", NotificationTools.NEWS_ID, NotificationTools.NEWS_CHANNEL_ID);
                    }
                }
            }

            EventBus.getDefault().post(new MsgCountEvent(true));
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> messages) {
            //收到透传消息
            Log.e("tag", "==============  收到透传消息" + messages.size());
        }

        @Override
        public void onMessageRead(List<EMMessage> messages) {
            //收到已读回执
            Log.e("tag", "==============  收到已读回执" + messages.size());
        }

        @Override
        public void onMessageDelivered(List<EMMessage> message) {
            //收到已送达回执
        }

        @Override
        public void onMessageRecalled(List<EMMessage> messages) {
            //消息被撤回
        }

        @Override
        public void onMessageChanged(EMMessage message, Object change) {
            //消息状态变动
        }
    };


    /**
     * 获取所有会话
     *
     * @return
     */
    public Map<String, EMConversation> getAllConversations() {
        Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
        return conversations;
    }

    /**
     * 注册一个监听连接状态的listener
     *
     * @param activity
     */
    public void connectionListener(final Activity activity) {
        EMClient.getInstance().addConnectionListener(new EMConnectionListener() {
            @Override
            public void onConnected() {
            }

            @Override
            public void onDisconnected(final int error) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (error == EMError.USER_REMOVED) {
                            // 显示帐号已经被移除
                            RxToast.normal("账号已注销");
                        } else if (error == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                            // 显示帐号在其他设备登录
                            RxToast.normal("账号在其他设备登陆");
                            SPUtils.clear(activity);
                        } else {
                            if (NetUtils.hasNetwork(activity)) {
                                //连接不到聊天服务器
                                RxToast.normal("连接不到聊天服务器");
                            } else {
                                //当前网络不可用，请检查网络设置
                                RxToast.normal("当前网络不可用，请检查网络设置");
                            }
                        }
                    }
                });
            }
        });


    }

}
