package com.kw.top.redpacket;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.kw.top.R;
import com.kw.top.tools.ConstantValue;
import com.kw.top.ui.activity.news.SendRedbagActivity;
import com.kw.top.utils.SPUtils;
import com.netease.nim.uikit.business.session.actions.BaseAction;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.model.CustomMessageConfig;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.util.HashMap;
import java.util.Map;

public class RedPacketAction extends BaseAction {
    private static final int REQUEST_CODE_RED_PACKET = 8;
    private String name;
    private String headUrl;
    private String account;


    public RedPacketAction(String headUrl, String name, String account) {
        super(R.drawable.message_plus_rp_selector, R.string.red_packet);
        this.account = account;
        this.headUrl = headUrl;
        this.name = name;
    }

    @Override
    public void onClick() {
        Intent intent = new Intent(getActivity(), SendRedbagActivity.class);
        intent.putExtra("RED_TYPE", "0");
        intent.putExtra("TO_USER", name);
        intent.putExtra("HEAD", headUrl);
        getActivity().startActivityForResult(intent, REQUEST_CODE_RED_PACKET);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        sendRedPacket(data);
    }

    private void sendRedPacket(Intent data) {
        String id = data.getStringExtra("REDBAG_ID");
        RedPacketAttachment attachment = new RedPacketAttachment();
        // 红包id
        attachment.setRedPacketId(id);

        String content = "";
        // 不存云消息历史记录
        CustomMessageConfig config = new CustomMessageConfig();
        config.enableHistory = false;
        Map<String, Object> map = new HashMap<>();
        map.put(account, SPUtils.getString(getActivity(), ConstantValue.KEY_USER_ID));
        IMMessage message = MessageBuilder.createCustomMessage(getAccount(), getSessionType(), content, attachment, config);
        message.setLocalExtension(map);
        sendMessage(message);
    }
}
