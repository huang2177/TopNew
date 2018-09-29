package com.kw.top.ui.fragment.news;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.kw.top.base.FriendBean;
import com.kw.top.redpacket.RedPacketAttachment;
import com.kw.top.ui.activity.news.ChatActivity;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.business.recent.RecentContactsCallback;
import com.netease.nim.uikit.business.recent.RecentContactsFragment;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.netease.nimlib.sdk.uinfo.model.UserInfo;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

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
        UserInfo userInfo = NimUIKit.getUserInfoProvider().getUserInfo(recent.getContactId());
        if (userInfo != null && getActivity() != null) {
            FriendBean data = new FriendBean(userInfo.getAvatar(), recent.getFromNick(), recent.getContactId());
            ChatActivity.startActivity(getContext(), data);
        }
    }


    @Override
    public String getDigestOfAttachment(RecentContact recent, MsgAttachment attachment) {
        if (attachment instanceof RedPacketAttachment) {
            return "[红包]";
        } else {
            return recent.getContent();
        }
    }

    @Override
    public String getDigestOfTipMsg(RecentContact recent) {
        return null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

}
