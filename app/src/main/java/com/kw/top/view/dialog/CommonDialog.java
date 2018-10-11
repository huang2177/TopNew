package com.kw.top.view.dialog;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.kw.top.R;
import com.kw.top.listener.OnItemClickListener;
import com.netease.nim.uikit.common.ui.dialog.EasyAlertDialog;

/**
 * Des:
 * Created by huang on 2018/10/4 0004 11:22
 */
public class CommonDialog extends EasyAlertDialog implements View.OnClickListener {

    private OnItemClickListener mListener;

    public CommonDialog(Context context) {
        super(context);
    }

    public void setMessage(String message, OnItemClickListener listener) {
        setTitle("提示信息");
        setMessage(message);
        addNegativeButton("取消", this);
        addPositiveButton("确定", this);
        mListener = listener;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.easy_dialog_negative_btn:
                dismiss();
                break;
            case R.id.easy_dialog_positive_btn:
                dismiss();
                mListener.onItemClick(v, 1);
                break;
        }
    }
}
