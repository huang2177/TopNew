package com.kw.top.ui.activity.club;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kw.top.R;
import com.kw.top.base.BaseActivity;
import com.kw.top.bean.BaseBean;
import com.kw.top.retrofit.Api;
import com.kw.top.utils.RxToast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * author  ： zy
 * date    ： 2018/8/14
 * des     ：
 */

public class ClubNoticeActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_title_right)
    TextView mTvTitleRight;
    @BindView(R.id.relative_title)
    RelativeLayout mRelativeTitle;
    @BindView(R.id.et_club_desc)
    EditText mEtClubDesc;
    @BindView(R.id.tv_confirm)
    TextView mTvConfirm;

    private String groupId;

    public static void startActivityForResult(Activity context, String groupId,int code){
        Intent intent = new Intent(context,ClubNoticeActivity.class);
        intent.putExtra("groupId",groupId);
        context.startActivityForResult(intent,code);
    }

    @Override
    public int getContentView() {
        return R.layout.activity_club_notice;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        ButterKnife.bind(this);

        groupId = getIntent().getStringExtra("groupId");
        mTvTitle.setText("修改群公告");
    }

    @OnClick({R.id.iv_back, R.id.tv_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_confirm:
                String desc = mEtClubDesc.getText().toString().trim();
                if (TextUtils.isEmpty(desc)) {
                    RxToast.normal("群公告不能为空");
                } else {
                    upDate(desc);
                }
                break;
        }
    }

    private void upDate(final String desc){
        showProgressDialog();
        Api.getApiService().updateClub(groupId,desc,getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Action1<BaseBean>() {
                    @Override
                    public void call(BaseBean baseBean) {
                        hideProgressDialog();
                        if (baseBean.isSuccess()){
                            RxToast.normal("修改成功");
                            Intent intent = new Intent();
                            intent.putExtra("desc",desc);
                            setResult(RESULT_OK,intent);
                            finish();
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        hideProgressDialog();
                    }
                });
    }

}

