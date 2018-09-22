package com.kw.top.ui.activity;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.kw.top.R;
import com.kw.top.base.BaseActivity;
import com.kw.top.bean.BaseBean;
import com.kw.top.bean.SplashBean;
import com.kw.top.bean.VersionBean;
import com.kw.top.retrofit.Api;
import com.kw.top.retrofit.HttpHost;
import com.kw.top.tools.ConstantValue;
import com.kw.top.tools.GlideTools;
import com.kw.top.ui.activity.find.FindDetailsActivity;
import com.kw.top.ui.activity.login.LoginActivity;
import com.kw.top.utils.RxToast;
import com.kw.top.utils.SPUtils;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * author: zy
 * data  : 2018/6/2
 * des   :
 */

public class SplashActivity extends BaseActivity {


    private String VerName;
    private AlertDialog alertDialog;

    @Override
    public int getContentView() {
        return R.layout.activity_splash;
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                --count;
                if (count <= 0) {
                    jumpActivity();
                } else {
                    mTextView.setText(count + "");
                    mHandler.sendEmptyMessageDelayed(1, 1000);
                }
            }
        }
    };
    private ImageView mImageView;
    private TextView mTextView;
    private int count = 4;
    private String picUserId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        mImageView = findViewById(R.id.iv);
        mTextView = findViewById(R.id.tv);
        // mImageView.setVisibility(View.GONE);
        // mTextView.setVisibility(View.GONE);

        //VerName = SPUtils.getVerName(this);
        //showVerSionDialog();
        //VersionCode();
        initData();
        initListener();
    }

    private void initListener() {
        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpActivity();
            }
        });
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(picUserId)) {
                    FindDetailsActivity.startActivity(SplashActivity.this, picUserId);
                    finish();
                }
            }
        });
    }

    public void initData() {
        getPicImage();
       /* if (ChatHelper.getInstance().isLoggedIn()) {
            //加载本地会话和群组消息
            EMClient.getInstance().chatManager().loadAllConversations();
            EMClient.getInstance().groupManager().loadAllGroups();
        }*/
    }

    private Runnable mRunnable = new Runnable() {
                                                    @Override
        public void run() {
            jumpActivity();
        }
    };

    private void jumpActivity() {
        if (TextUtils.isEmpty(SPUtils.getString(SplashActivity.this, ConstantValue.KEY_TOKEN, "")) ||
                !SPUtils.getString(SplashActivity.this, ConstantValue.KEY_PROVE_STATE, "").equals("1")) {
            startActivity(LoginActivity.class);
        } else {
            //startActivity(MainActivity.class);
//            startActivity(ManVipActivity.class);
//            EditInfoActivity.startActivity(SplashActivity.this,false);
            startActivity(NewMainActivity.class);
        }
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mHandler) {
            mHandler.removeCallbacks(mRunnable);
            mHandler.removeMessages(1);
        }

        if (alertDialog != null) {
            alertDialog.dismiss();
        }
    }

    private void getPicImage() {
        Api.getApiService().queryPicture()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Action1<BaseBean<SplashBean>>() {
                    @Override
                    public void call(BaseBean<SplashBean> baseBean) {
                        if (baseBean.isSuccess()) {
                            picUserId = baseBean.getData().getUserId();
                            Glide.with(SplashActivity.this).load(HttpHost.qiNiu + baseBean.getData().getPicture())
                                    .apply(GlideTools.getOptions())
                                    .listener(new RequestListener<Drawable>() {
                                        @Override
                                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                            return false;
                                        }

                                        @Override
                                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                            mHandler.sendEmptyMessageDelayed(1, 1000);
                                            return false;
                                        }
                                    })
                                    .into(mImageView);
                        } else {
                            mHandler.postDelayed(mRunnable, 2000);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mHandler.postDelayed(mRunnable, 2000);
                    }
                });
    }


    private void VersionCode() {
        String token = getToken();
        //showProgressDialog();
        Api.getApiService().queryVersion("02", "1")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Action1<BaseBean<VersionBean>>() {
                    @Override
                    public void call(BaseBean<VersionBean> baseBean) {
                        if (VerName.equals(baseBean.getData().getVersion())) {
                            return;
                        }
                        showVerSionDialog();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                });
    }


    /**
     * 版本更新dialog
     */
    private void showVerSionDialog() {
        alertDialog = new AlertDialog.Builder(this)
                .setMessage("暂停更新升级，最新资讯加客服微信 jiemomo_2028")
                .setPositiveButton("复制", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //获取剪贴板管理器：
                        ClipboardManager cm = (ClipboardManager) getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
                        // 创建普通字符型ClipData
                        ClipData mClipData = ClipData.newPlainText("Label", "jiemomo_2028");
                        // 将ClipData内容放到系统剪贴板里。
                        cm.setPrimaryClip(mClipData);
                        RxToast.normal("客服微信已复制到剪切板");
                        //dialog.dismiss();
                        finish();
                    }
                }).show();

    }


}
