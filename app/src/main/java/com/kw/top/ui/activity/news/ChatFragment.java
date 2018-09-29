package com.kw.top.ui.activity.news;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.kw.top.base.FriendBean;
import com.kw.top.redpacket.RedPacketAction;
import com.kw.top.tools.ConstantValue;
import com.kw.top.utils.SPUtils;
import com.netease.nim.uikit.api.model.session.SessionCustomization;
import com.netease.nim.uikit.business.session.actions.BaseAction;
import com.netease.nim.uikit.business.session.constant.Extras;
import com.netease.nim.uikit.business.session.fragment.MessageFragment;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 黄双 on 2018/9/29.
 */

public class ChatFragment extends MessageFragment {

    private RedPacketAction redPacketAction;

    public ChatFragment newInstance(FriendBean data) {
        ChatFragment fragment = new ChatFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", data);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.setArguments(getIMChartParams());
        super.onActivityCreated(savedInstanceState);
    }

    /**
     * 拼接参数信息
     */
    public Bundle getIMChartParams() {
        Bundle bundle = new Bundle();
        FriendBean bean = (FriendBean) getArguments().getSerializable("data");
        SessionCustomization customization = new SessionCustomization();
        redPacketAction = new RedPacketAction(bean.getHeadImg(), bean.getNickName(), bean.getFriendsId(), bean.getFriendAccount());
        customization.actions = Arrays.<BaseAction>asList(redPacketAction);
        bundle.putSerializable(Extras.EXTRA_TYPE, SessionTypeEnum.P2P);
        bundle.putSerializable(Extras.EXTRA_CUSTOMIZATION, customization);
        bundle.putSerializable(Extras.EXTRA_ACCOUNT, bean.getFriendAccount());
        return bundle;
    }

    @Override
    public boolean sendMessage(IMMessage message) {
        Map<String, Object> map = new HashMap<>();
        map.put(SPUtils.getString(getContext(), ConstantValue.KEY_ACCOUNT)
                , SPUtils.getString(getContext(), ConstantValue.KEY_USER_ID));
        message.setRemoteExtension(map);
        return super.sendMessage(message);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 8) {
            redPacketAction.onActivityResult(requestCode, resultCode, data);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
