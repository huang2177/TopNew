package com.kw.top.ui.activity.user_center;

import android.Manifest;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Config;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.kw.top.R;
import com.kw.top.base.BaseActivity;
import com.kw.top.bean.BaseBean;
import com.kw.top.bean.InfoBean;
import com.kw.top.bean.InviteUrlBean;
import com.kw.top.retrofit.Api;
import com.kw.top.retrofit.HttpHost;
import com.kw.top.tools.ComResultTools;
import com.kw.top.tools.ConstantValue;
import com.kw.top.tools.GlideTools;
import com.kw.top.ui.activity.login.LoginActivity;
import com.kw.top.utils.RxToast;
import com.kw.top.utils.SPUtils;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMWeb;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * author: zy
 * data  : 2018/5/20
 * des   :
 */

public class InviteFriendActivity extends BaseActivity implements View.OnClickListener {
    ImageView mIvBack;
    TextView mTvTitle;
    TextView mTvTitleRight;
    RelativeLayout mRelativeTitle;
    ImageView mIvHead;
    TextView mTvInvite;
    private Dialog dialog;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.iv_vip_leave)
    ImageView imageView_leave;
    @BindView(R.id.iv_head_yaoqin)
    ImageView imageView_heade;

    private String invite_url;
    private UMWeb web;

    @Override
    public int getContentView() {
        return R.layout.activity_invite_firend;
    }

    public void initView() {
        mTvTitle.setText("邀请好友");
        mTvTitleRight.setText("邀请记录");
    }

    public void initData() {
        String head = HttpHost.qiNiu + SPUtils.getString(this, ConstantValue.KEY_HEAD, "");
        Glide.with(this).load(head).apply(GlideTools.getOptions()).into(mIvHead);
        Glide.with(this).load(head).apply(GlideTools.getOptions()).into(imageView_heade);
        GlideTools.setVipResourceS(imageView_leave, Integer.parseInt(SPUtils.getString(this, ConstantValue.KEY_VIP_GRADE, "")));
        tv_name.setText(SPUtils.getString(this, ConstantValue.KEY_NAME, ""));
    }

    public void initListener() {
        mTvTitleRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(InviteRecordActivity.class);
            }
        });
    }

    private void getinviteUrl() {
        showProgressDialog();
        Api.getApiService().getInviteUrl(getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Action1<BaseBean>() {
                    @Override
                    public void call(BaseBean baseBean) {
                        hideProgressDialog();
                        if (baseBean.isSuccess()) {
                            InviteUrlBean inviteUrlBean = null;
                            try {
                                inviteUrlBean = new Gson().fromJson(baseBean.getJsonData(), new TypeToken<InviteUrlBean>() {
                                }.getType());
                            } catch (JsonSyntaxException e) {
                                e.printStackTrace();
                            }
                            invite_url = inviteUrlBean.getInviteUrl();
                            showDialog();
                        } else {
                            ComResultTools.resultData(InviteFriendActivity.this, baseBean);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        hideProgressDialog();
                        RxToast.normal("获取邀请信息失败");
                    }
                });
    }

    private void showDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_invite_friend, null);
        view.findViewById(R.id.tv_invite_wechat_friend).setOnClickListener(mOnClickListener);
        view.findViewById(R.id.tv_invite_wechat_circle).setOnClickListener(mOnClickListener);
        view.findViewById(R.id.tv_invite_cancle).setOnClickListener(mOnClickListener);

        dialog = new Dialog(this);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(false);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //        window.setWindowAnimations(R.style.popupAnimation); // 添加动画
        dialog.show();
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dialog.dismiss();
            switch (v.getId()) {
                case R.id.tv_invite_wechat_friend:
                    share(SHARE_MEDIA.WEIXIN);
                    break;
                case R.id.tv_invite_wechat_circle:
                    share(SHARE_MEDIA.WEIXIN_CIRCLE);
                    break;
                case R.id.tv_invite_cancle:
                    break;
            }
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_invite:
                if (TextUtils.isEmpty(invite_url)) {
                    getinviteUrl();
                } else {
                    showDialog();
                }
//                getPermission();
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }

    private void share(SHARE_MEDIA share_media) {
        web = new UMWeb(invite_url);
        web.setTitle("TOP");//标题
//        web.setThumb(R.mipmap.ic_launcher);  //缩略图
        web.setDescription("top注册链接");//描述
        new ShareAction(InviteFriendActivity.this)
                .setPlatform(share_media)//传入平台
                .withText("hello")//分享内容
                .withMedia(web)
                .setCallback(shareListener)//回调监听器
                .share();
    }

    private UMShareListener shareListener = new UMShareListener() {
        /**
         * @descrption 分享开始的回调
         * @param platform 平台类型
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {

        }

        /**
         * @descrption 分享成功的回调
         * @param platform 平台类型
         */
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Toast.makeText(InviteFriendActivity.this, "分享成功", Toast.LENGTH_LONG).show();
        }

        /**
         * @descrption 分享失败的回调
         * @param platform 平台类型
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(InviteFriendActivity.this, "分享失败" + t.getMessage(), Toast.LENGTH_LONG).show();
        }

        /**
         * @descrption 分享取消的回调
         * @param platform 平台类型
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(InviteFriendActivity.this, "取消分享", Toast.LENGTH_LONG).show();

        }
    };

    private void getPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE, Manifest.permission.READ_LOGS, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.SET_DEBUG_APP, Manifest.permission.SYSTEM_ALERT_WINDOW, Manifest.permission.GET_ACCOUNTS, Manifest.permission.WRITE_APN_SETTINGS};
            ActivityCompat.requestPermissions(this, mPermissionList, 123);
        } else {
            showDialog();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (requestCode == 123) {
            showDialog();
        } else {
            RxToast.normal("没有分享权限");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        setContentView(getContentView());
        ButterKnife.bind(this);


        mIvBack = findViewById(R.id.iv_back);
        mTvTitle = findViewById(R.id.tv_title);
        mTvTitleRight = findViewById(R.id.tv_title_right);
        mRelativeTitle = findViewById(R.id.relative_title);
        mIvHead = findViewById(R.id.iv_head);
        mTvInvite = findViewById(R.id.tv_invite);
        mIvBack.setOnClickListener(this);
        mTvInvite.setOnClickListener(this);
        initView();
        initData();
        initListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UMShareAPI.get(this).release();
    }
}
