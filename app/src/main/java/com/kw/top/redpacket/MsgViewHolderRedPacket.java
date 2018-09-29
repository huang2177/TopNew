package com.kw.top.redpacket;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.widget.ImageView;

import com.kw.top.R;
import com.kw.top.bean.BaseBean;
import com.kw.top.retrofit.Api;
import com.kw.top.tools.ConstantValue;
import com.kw.top.ui.activity.user_center.RedPacketDetailsActivity;
import com.kw.top.utils.SPUtils;
import com.netease.nim.uikit.business.session.viewholder.MsgViewHolderBase;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static com.netease.nim.uikit.api.NimUIKit.getContext;

public class MsgViewHolderRedPacket extends MsgViewHolderBase {

    private ImageView ivRedPacket;
    private long mClickTime;

    public MsgViewHolderRedPacket(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }

    @Override
    protected int getContentResId() {
        return R.layout.red_packet_item;
    }

    @Override
    protected void inflateContentView() {
        ivRedPacket = findViewById(R.id.iv_red_packet);
    }

    @Override
    protected void bindContentView() {
        RedPacketAttachment attachment = (RedPacketAttachment) message.getAttachment();
        setLayoutParams(getWidth_Height()[0], getWidth_Height()[1], ivRedPacket);
    }

    @Override
    protected int leftBackground() {
        return R.color.transparent;
    }

    @Override
    protected int rightBackground() {
        return R.color.transparent;
    }

    /**
     * 拆红包
     */
    @Override
    protected void onItemClick() {
        RedPacketAttachment attachment = (RedPacketAttachment) message.getAttachment();
        String redPacketId = SPUtils.getString(getContext(), ConstantValue.RED_PACKET_ID);
        if (TextUtils.isEmpty(redPacketId)) {
            clickRedPacket(attachment.getRedPacketId());
        } else {
            RedPacketDetailsActivity.startActivity(getContext(), redPacketId);
        }
    }

    /**
     * 领取红包点击事件
     */
    private void clickRedPacket(final String redPacketId) {
        if (System.currentTimeMillis() - mClickTime <= 1000) {
            return;
        }
        mClickTime = System.currentTimeMillis();
        Api.getApiService().clickRedPackage(redPacketId, SPUtils.getString(getContext(), ConstantValue.KEY_TOKEN))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Action1<BaseBean>() {
                    @Override
                    public void call(BaseBean baseBean) {
                        if (baseBean.isSuccess()) {
                            //保存领取过的红包ID
                            SPUtils.saveString(getContext(), ConstantValue.RED_PACKET_ID, redPacketId);
                            RedPacketDetailsActivity.startActivity(getContext(), redPacketId);
                        } else {
                            RedPacketDetailsActivity.startActivity(getContext(), redPacketId);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                });
    }
    private int[] getWidth_Height() {
        int array[] = new int[2];
        Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.icon_red_packet);
        if (bitmap == null) {
            return array;
        }
        array[0] = bitmap.getWidth();
        array[1] = bitmap.getHeight();
        return array;
    }
}
