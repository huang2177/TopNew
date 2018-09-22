package com.hyphenate.easeui.widget.chatrow;

import android.content.Context;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.R;

/**
 * author  ： zy
 * date    ： 2018/7/13
 * des     ： 红包
 */

public class EaseChatRowRedbag extends EaseChatRow {

    private ImageView ivRedbag;

    public EaseChatRowRedbag(Context context, EMMessage message, int position, BaseAdapter adapter) {
        super(context, message, position, adapter);
    }

    @Override
    protected void onInflateView() {
        inflater.inflate(message.direct() == EMMessage.Direct.RECEIVE ?
                R.layout.ease_row_received_redbag:R.layout.ease_row_sent_redbag,null);
    }

    @Override
    protected void onFindViewById() {
        ivRedbag = (ImageView) findViewById(R.id.iv_redbag);
    }

    @Override
    protected void onViewUpdate(EMMessage msg) {
        switch (msg.status()) {
            case CREATE:
                onMessageCreate();
                break;
            case SUCCESS:
                onMessageSuccess();
                break;
            case FAIL:
                onMessageError();
                break;
            case INPROGRESS:
                onMessageInProgress();
                break;
        }
    }

    @Override
    protected void onSetUpView() {
//        ivRedbag.setImageDrawable(getResources().getDrawable(R.drawable.icon_redbag_bg));
    }

    private void onMessageCreate() {
        progressBar.setVisibility(View.VISIBLE);
        statusView.setVisibility(View.GONE);
    }

    private void onMessageSuccess() {
//        progressBar.setVisibility(View.GONE);
//        statusView.setVisibility(View.GONE);
    }

    private void onMessageError() {
        progressBar.setVisibility(View.GONE);
        statusView.setVisibility(View.VISIBLE);
    }

    private void onMessageInProgress() {
        progressBar.setVisibility(View.VISIBLE);
        statusView.setVisibility(View.GONE);
    }



}
