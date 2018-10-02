package com.kw.top.view;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.kw.top.R;
import com.kw.top.bean.BaseBean;
import com.kw.top.retrofit.Api;
import com.kw.top.utils.RxTextTool;
import com.kw.top.utils.RxToast;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Des:视频聊天的举报事件Dialog
 * Created by Administrator on 2018/9/30.
 */

public class TipOffDialog extends XBottomDialog {
    private EditText editText;
    private TextView tvCommit, tvCancel;
    private String token;
    private String content;
    private String roomNum;

    public TipOffDialog(@NonNull Activity activity, String roomNum, String token) {
        super(activity);
        this.roomNum = roomNum;
        this.token = token;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_report;
    }

    @Override
    protected void initView() {
        editText = findViewById(R.id.report_ed);
        tvCommit = findViewById(R.id.report_commit_tv);
        tvCancel = findViewById(R.id.report_cancel_tv);
    }

    @Override
    protected void setListener() {
        tvCommit.setOnClickListener(this);
        tvCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.report_cancel_tv:
                dismiss();
                break;
            case R.id.report_commit_tv:
                content = editText.getText().toString();   //最多只能输入50个字
                report(roomNum, content, token);
                break;
        }
    }


    /**
     * 举报
     */
    public void report(String roomNum, String content, String token) {
        Api.getApiService().report(roomNum, content, token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Action1<BaseBean>() {
                    @Override
                    public void call(BaseBean baseBean) {
                        if (baseBean.isSuccess()) {
                            dismiss();
                            RxToast.normal("举报成功！");
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                });
    }
}
