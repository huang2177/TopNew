package com.kw.top.ui.fragment.news;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.kw.top.bean.FriendBean;
import com.kw.top.redpacket.RedPacketAttachment;
import com.kw.top.ui.activity.news.ChatActivity;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.business.recent.RecentContactsCallback;
import com.netease.nim.uikit.business.recent.RecentContactsFragment;
import com.netease.nim.uikit.impl.cache.NimUserInfoCache;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.avchat.model.AVChatAttachment;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.netease.nimlib.sdk.uinfo.model.UserInfo;

import org.greenrobot.eventbus.EventBus;

/**
 * 最近回话列表
 */
public class RecentSessionFragment extends RecentContactsFragment implements RecentContactsCallback {

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        setCallback(this);
    }

    @Override
    public void onRecentContactsLoaded() {

    }

    @Override
    public void onUnreadCountChange(int unreadCount) {

    }

    @Override
    public void onItemClick(RecentContact recent) {
        UserInfo userInfo = NimUserInfoCache.getInstance().getUserInfo(recent.getFromAccount());
        if (userInfo != null && getActivity() != null) {
            FriendBean data = new FriendBean(userInfo.getAvatar(), userInfo.getName(), userInfo.getAccount());
            ChatActivity.startActivity(getContext(), data);
        }
    }


    @Override
    public String getDigestOfAttachment(RecentContact recent, MsgAttachment attachment) {
        if (attachment instanceof RedPacketAttachment) {
            return "[红包]";
        } else if (attachment instanceof AVChatAttachment) { //目前只有视频
            return "[视频聊天]";
        }
        {
            return recent.getContent();
        }
    }

    @Override
    public String getDigestOfTipMsg(RecentContact recent) {
        return null;
    }


    @Override
    public void onResume() {
        super.onResume();
        if (!isHidden()) {
            NIMClient.getService(MsgService.class).setChattingAccount(MsgService.MSG_CHATTING_ACCOUNT_ALL, SessionTypeEnum.None);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            NIMClient.getService(MsgService.class).setChattingAccount(MsgService.MSG_CHATTING_ACCOUNT_ALL, SessionTypeEnum.None);
        } else {
            NIMClient.getService(MsgService.class).setChattingAccount(MsgService.MSG_CHATTING_ACCOUNT_NONE, SessionTypeEnum.None);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        NIMClient.getService(MsgService.class).setChattingAccount(MsgService.MSG_CHATTING_ACCOUNT_NONE, SessionTypeEnum.None);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

}
