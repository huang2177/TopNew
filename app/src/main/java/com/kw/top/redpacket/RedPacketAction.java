package com.kw.top.redpacket;

import android.app.Activity;
import android.content.Intent;

import com.jrmf360.normallib.rp.JrmfRpClient;
import com.jrmf360.normallib.rp.bean.EnvelopeBean;
import com.kw.top.ui.activity.news.SendRedbagActivity;
import com.netease.nim.uikit.R;
import com.netease.nim.uikit.business.session.actions.BaseAction;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.model.CustomMessageConfig;
import com.netease.nimlib.sdk.msg.model.IMMessage;

public class RedPacketAction extends BaseAction {
    private static final int REQUEST_CODE_RED_PACKET = 8;
    private String name;
    private String headUrl;

    public RedPacketAction(String headUrl, String name) {
        super(R.drawable.message_plus_rp_selector, R.string.red_packet);
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
        RedPacketAttachment attachment = new RedPacketAttachment();
        // 红包id，红包信息，红包名称
//        attachment.setRpId(groupRpBean.getEnvelopesID());
//        attachment.setRpContent(groupRpBean.getEnvelopeMessage());
//        attachment.setRpTitle(groupRpBean.getEnvelopeName());

        String content = "发来了一个红包";
        // 不存云消息历史记录
        CustomMessageConfig config = new CustomMessageConfig();
        config.enableHistory = false;

        IMMessage message = MessageBuilder.createCustomMessage(getAccount(), getSessionType(), content, attachment, config);

        sendMessage(message);
    }
}
