package com.kw.top.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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

public class TipOffDialog extends Dialog implements View.OnClickListener {
    private EditText editText;
    private TextView tvCommit, tvCancel;
    private String token;
    private String content;
    private String roomNum;

    public TipOffDialog(@NonNull Activity activity, String roomNum, String token) {
        super(activity, R.style.tran_dialog);
        this.roomNum = roomNum;
        this.token = token;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.gravity = Gravity.BOTTOM;
        getWindow().setAttributes(params);
        initView();
        setListener();
    }

    protected int getLayoutId() {
        return R.layout.dialog_report;
    }

    protected void initView() {
        editText = findViewById(R.id.report_ed);
        tvCommit = findViewById(R.id.report_commit_tv);
        tvCancel = findViewById(R.id.report_cancel_tv);
    }

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
                report(roomNum, token);
                break;
        }
    }


    /**
     * 举报
     */
    public void report(String roomNum, String token) {
        content = editText.getText().toString();
        if (TextUtils.isEmpty(content)) {
            RxToast.normal("请输入举报内容!");
            return;
        }
        if (content.length() > 50) {
            RxToast.normal("最多只能输入50个字!");
            return;
        }
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
