package com.kw.top.view;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.widget.EditText;
import android.widget.TextView;

import com.kw.top.R;

/**
 * Des:视频聊天的举报事件Dialog
 * Created by Administrator on 2018/9/30.
 */

public class TipOffDialog extends XBottomDialog {
    private EditText editText;
    private TextView tvCommit, tvCancel;
    private String content;

    public TipOffDialog(@NonNull Activity activity) {
        super(activity);
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
        content = editText.getText().toString();   //最多只能输入50个字
    }

    @Override
    protected void setListener() {
        tvCommit.setOnClickListener(this);
        tvCancel.setOnClickListener(this);
    }
}
