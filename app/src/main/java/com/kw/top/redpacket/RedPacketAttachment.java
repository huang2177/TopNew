package com.kw.top.redpacket;

import com.alibaba.fastjson.JSONObject;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;

public class RedPacketAttachment extends CustomAttachment {
    public static final int RED_PACKET = 100;

    private int width;
    private int height;
    private String redPacketId; //  红包id

    private static final String KEY_WIDTH = "width";
    private static final String KEY_HEIGHT = "height";
    private static final String KEY_ID = "redPacketId";

    public RedPacketAttachment() {
        super(RED_PACKET);
    }

    @Override
    protected void parseData(JSONObject data) {
        width = data.getInteger(KEY_WIDTH);
        redPacketId = data.getString(KEY_ID);
        height = data.getInteger(KEY_HEIGHT);
    }

    @Override
    protected JSONObject packData() {
        JSONObject data = new JSONObject();
        data.put(KEY_WIDTH, width);
        data.put(KEY_HEIGHT, height);
        data.put(KEY_ID, redPacketId);
        return data;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getRedPacketId() {
        return redPacketId;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setRedPacketId(String redPacketId) {
        this.redPacketId = redPacketId;
    }
}
